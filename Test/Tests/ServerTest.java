import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Players.IPlayer;
import Serialization.ChoiceAdapter;
import Serialization.StateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

public class ServerTest {
    /*
}

    Server.server server;
    FutureTask<?> serverFuture;
    Thread serverThread;

    @Mock
    IPlayer player;

    @Mock
    ServerSocket socket;

    @Captor
    ArgumentCaptor<Optional<PlayerGameState>> optionalGameStateCaptor;

    @Captor
    ArgumentCaptor<PlayerGameState> gameStateCaptor;

    @Captor
    ArgumentCaptor<Coordinate> coordinateCaptor;

    @Captor
    ArgumentCaptor<Boolean> boolCaptor;

    JsonReader reader;

    OutputStream playerOutputStream;

    Gson gson;
    PlayerGameState inputState;


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        this.server = new Server.server(socket);

        GsonBuilder gsonBuilder = new GsonBuilder();
        StateAdapter.register(gsonBuilder);
        ChoiceAdapter.register(gsonBuilder);
        gson = gsonBuilder.create();

        inputState = createState();

        serverFuture = makeServerFuture();
        serverThread = new Thread(serverFuture);
    }

    private JsonReader makeSocketOutputStream(Socket socket) throws IOException {
        PipedInputStream inputStream = new PipedInputStream();
        reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
        playerOutputStream = new BufferedOutputStream(new PipedOutputStream(inputStream));
        when(socket.getOutputStream()).thenReturn(playerOutputStream);
        return reader;
    }

    @AfterEach
    void cleanUp() throws ExecutionException, InterruptedException, TimeoutException {
        serverFuture.cancel(true);
        serverThread.stop();
    }

    //server programming waits at most 2 secs for a name after a connection is established
    @Test
    void WaitsTwoSec() throws IOException, ExecutionException, InterruptedException, TimeoutException {
//        Socket mockedSocket = mock(Socket.class);
//        when(socket.accept()).thenReturn(mockedSocket);
//        when(mockedSocket.getInputStream()).thenReturn(new NullInputStream());
//        when(mockedSocket.getOutputStream()).thenReturn(NullOutputStream.NULL_OUTPUT_STREAM);
//
//        serverThread.start();
//        serverFuture.get(100, TimeUnit.MILLISECONDS);
//        Mockito.verify(mockedSocket, after(1500).never()).close();
//        Mockito.verify(mockedSocket, after(500).times(1)).close();

    }

    //server waits 20 sec for at least 2 remote clients, and at most 6 remote clients
    @Test
    void serverWaitsForClientsTwo() {

    }

    //server waits 20 sec for at least 2 remote clients, and at most 6 remote clients
    @Test
    void serverWaitsForClientsSixPlus() {


//        LocalDateTime then = LocalDateTime.now();
//        while (true) {
//
//            if (ChronoUnit.SECONDS.between(then, LocalDateTime.now()) >= 20) break;
//        }


    }

    //server waits 20 sec for at least 2 remote clients, and at most 6 remote clients
    //If not enough try again exactly once
    @Test
    void serverWaitsForClientsOneTryAgain() {

    }

    //server waits 20 sec for at least 2 remote clients, and at most 6 remote clients
    //less than 2 player signs up the second time around, it returns an empty result
    @Test
    void serverWaitsForClientsTryAgainLesstwo() {

    }

    //ensure the turn order is identical to join order
    @Test
    void turnOrder() {

    }

    private FutureTask<?> makeServerFuture(){
        return new FutureTask<>(()-> {
            try {
                server.runEntireGame();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }, null);
    }

    public PlayerGameState createState(){
        Tile[] listOfTiles = new Tile[]{
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.ALEXANDRITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.ALEXANDRITE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.ALMANDINE_GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AMETHYST)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AMETRINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AMMOLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.APATITE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.APLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.APRICOT_SQUARE_RADIANT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AQUAMARINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AUSTRALIAN_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AVENTURINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.AZURITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BERYL)),
                //row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLACK_OBSIDIAN)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLACK_ONYX)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLACK_SPINEL_CUSHION)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLUE_CEYLON_SAPPHIRE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLUE_CUSHION)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLUE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BLUE_SPINEL_HEART)),
                //row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.BULLS_EYE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CARNELIAN)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CHROME_DIOPSIDE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CHRYSOBERYL_CUSHION)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CHRYSOLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CITRINE_CHECKERBOARD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CITRINE)),
                //row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CLINOHUMITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.COLOR_CHANGE_OVAL)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.CORDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.DIAMOND)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.DUMORTIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.EMERALD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.FANCY_SPINEL_MARQUISE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GOLDEN_DIAMOND_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GOLDSTONE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GRANDIDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GRAY_AGATE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GREEN_AVENTURINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GREEN_BERYL_ANTIQUE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GREEN_BERYL)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GREEN_PRINCESS_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.GROSSULAR_GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.HACKMANITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.HELIOTROPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.HEMATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE, Gem.IOLITE_EMERALD_CUT)),
        };

        Board board = new Board(7, 7, listOfTiles);
        Tile spare = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));
        PlayerInfoPublic player1 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(1, 1),
                new GameColors("purple"));
        PlayerInfoPublic player2 = new PlayerInfoPublic(new Coordinate(3, 1), new Coordinate(3, 1),
                new GameColors("blue"));

        ArrayList<PlayerInfoPublic> publicPlayerInfos = new ArrayList<>();
        publicPlayerInfos.add(player1);
        publicPlayerInfos.add(player2);

        return new PlayerGameState(board, spare, publicPlayerInfos,0, Optional.empty());

    }*/

}
