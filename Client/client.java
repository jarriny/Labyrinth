package Client;

import Players.IPlayer;
import Players.player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Implementation of a client that TCP-connects a player to a server
 */
public class client implements Runnable {
    Socket socket;
    IPlayer player;

    public client(String hostName, int portNum, IPlayer player) {
        this.player = player;
        try {
            this.socket = new Socket(hostName, portNum);
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(),true);
            toServer.println(player.name());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public client(Socket socket, IPlayer player) {
        this.player = player;
        this.socket = socket;
    }

    @Override
    public void run() {
        runGame();
    }

    /**
     * Sends the server the player's name and sets up proxy referee to begin listening for commands.
     */
    private void runGame() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException ioException) {
            throw new IllegalStateException("Writer not connected");
        }
        writer.print(player.name());
        writer.flush();

        // create a proxy ref
        Remote.referee referee = new Remote.referee(socket, player);
        try {
            referee.runEntireGame();
        } catch (Exception e) {
            throw new IllegalStateException("Game not run properly");
        }
    }
}
