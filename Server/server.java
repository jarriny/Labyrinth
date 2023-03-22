package Server;

import Common.Coordinate;
import Common.State.RefState;
import Players.IPlayer;
import Referee.gameResult;
import Remote.player;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Implementation of a server that signs up players, and then runs a complete game.
 */
public class server implements Runnable {

    final int WAITING_PERIOD_S = 20;
    final int PLAYER_WAIT_PERIOD_S = 3;
    final int MINIMUM_PLAYERS = 2;
    final int MAXIMUM_PLAYERS = 6;
    final int NUM_OF_WAITING_PERIODS = 2;

    ServerSocket serverSocket;
    ArrayList<IPlayer> players = new ArrayList<>(MAXIMUM_PLAYERS);

    gameResult result;

    RefState state;
    ArrayList<Coordinate> goals;

    /**
     * Creates a new server object.
     * @param portNum the port number the server is connected to
     * @param state the RefState that the game will be run with
     * @param goals the additional list of goals for the players
     */
    public server(int portNum, RefState state, ArrayList<Coordinate> goals) throws IOException {
        this.serverSocket = new ServerSocket(portNum);
        this.state = state;
        this.goals = goals;
    }

    @Override
    public void run() {
        runWaitingPeriods();

        try {
            runEntireGame();
        } catch (Exception e) {
            throw new IllegalStateException("Game did not work.");
        }

        returnResults();

        System.exit(0);
    }

    /**
     * Compiles the results of the game and prints them to System.out.
     */
    private void returnResults() {
        ArrayList<String> winners = new ArrayList<>();
        for (IPlayer player : result.getWinners()) {
            winners.add(player.name());
        }

        ArrayList<String> kicked = new ArrayList<>();
        for (IPlayer player : result.getKickedOut()) {
            kicked.add(player.name());
        }
        ArrayList<ArrayList<String>> resultArray = new ArrayList<>();
        resultArray.add(winners);
        resultArray.add(kicked);
        System.out.println(new Gson().toJson(resultArray));
    }

    /**
     * Runs the waiting period for the designated number of times.
     */
    private void runWaitingPeriods() {
        for (int count = 0; count < NUM_OF_WAITING_PERIODS; count++) {
            try {
                tryToGatherPlayers();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (this.players.size() >= MINIMUM_PLAYERS) {
                break;
            }
        }
    }

    /**
     * Gather players by them signing up and sending their name.
     * @throws Exception if the serverSocket is unsuccessful at accepting incoming connections
     */
    private void tryToGatherPlayers() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Uses FutureTask in order to include the time restraint on the sign up period
        // This task attempts to sign up players until the maximum number of players is reached
        FutureTask<?> task = new FutureTask<>(() -> {
            while (players.size() < MAXIMUM_PLAYERS) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    try {
                        Thread.sleep(PLAYER_WAIT_PERIOD_S * 1000);
                    } catch (InterruptedException e) {
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                tryToMakePlayer(executor, socket);
                if (Thread.interrupted()) {
                    return;
                }
            }
        }, null);
        Thread thread = new Thread(task);
        thread.start();

        try {
            task.get(WAITING_PERIOD_S, TimeUnit.SECONDS);
        }
        catch (TimeoutException te) {

        }
        executor.shutdownNow();
    }

    /**
     * Attempt to sign up a player.
     * @param executor helps makes things concurrent
     * @param socket the connection to the client
     */
    private void tryToMakePlayer(ExecutorService executor, Socket socket) {
        executor.execute(() -> {
            try {
                socket.setSoTimeout(PLAYER_WAIT_PERIOD_S * 1000);
                Scanner scanner = new Scanner(new InputStreamReader(socket.getInputStream()));
                String name = scanner.nextLine();
                if (name != null && !name.isBlank()) {
                    this.players.add(new player(socket, name));
                } else {
                    socket.close();
                }
            } catch (IOException e) {
            }
        });
    }

    /**
     * Tries twice to gather players, and if successful it runs a game. If unsuccessful it returns an empty result
     * @return the result of the game
     * @throws Exception if an error occurs while trying to gather players
     */
    private gameResult runEntireGame() throws Exception {
        if (this.players.size() < MINIMUM_PLAYERS) {
            return new gameResult(new ArrayList<>(), new ArrayList<>());
        }

        gameResult result = runSingleGame();
        this.result = result;
        return result;
    }

    /**
     * After gathering players, we run an entire game
     * @return the game result
     */
    private gameResult runSingleGame() {
        ArrayList<IPlayer> playerList = new ArrayList<>(players);
        Referee.referee referee = new Referee.referee(Optional.of(goals));
        Collections.reverse(playerList);
        return referee.runEntireGame(state, playerList);
    }
}
