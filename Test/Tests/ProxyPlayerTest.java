import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.Direction;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Players.IPlayer;
import Remote.player;
import Serialization.StateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.google.gson.JsonParser.parseReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ProxyPlayerTest {

    IPlayer player;

    @Mock
    Socket socket;

    JsonReader reader;

    OutputStream playerOutputStream;

    Gson gson;
    PlayerGameState inputState;

    /**
     * proxy
     * - that sends the expected json for setup, takeTurn, and won
     * - That accepts valid json and rejects invalid json input
     * - Try to make a check where the client never responds?
     */

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        this.player = new player(socket, "Jim");
        PipedInputStream inputStream = new PipedInputStream();
        reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
        playerOutputStream = new BufferedOutputStream(new PipedOutputStream(inputStream));
        when(socket.getOutputStream()).thenReturn(playerOutputStream);

        GsonBuilder gsonBuilder = new GsonBuilder();
        StateAdapter.register(gsonBuilder);
        gson = gsonBuilder.create();

        inputState = createState();
    }

    //sends the expected json for setup
    //make sure takes valid json
    @Test
    void testSetUp() throws Exception {
        String expectedStr = "[\"setup\", [" + gson.toJson(inputState) + ", {\"column#\": 3,\"row#\": 3}]]";
        InputStream clientResponse = IOUtils.toInputStream("\"void\"", StandardCharsets.UTF_16);
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = setupFuture(Optional.of(inputState));
        Thread t = new Thread(future);
        t.start();

        StringReader expectedStringReader = new StringReader(expectedStr);
        assertEquals(parseReader(expectedStringReader), parseReader(reader));
        future.get();
    }

    private FutureTask<?>  setupFuture(Optional<PlayerGameState> inputState){
        FutureTask<?> future = new FutureTask<>(()-> {
            try {
                player.setup(inputState, new Coordinate(3, 3));

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }, null);

        return future;
    }

    //sends the expected json for setup
    //receives no json
    //expect an interrupted exception to be thrown

    @Test
    void testSetUpInterrupt() throws IOException {
        //never gets sent so should timeout

        InputStream clientResponse = InputStream.nullInputStream();
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = setupFuture(Optional.of(inputState));
        Thread t = new Thread(future);
        t.start();

        assertThrows(RuntimeException.class, () -> {
            t.interrupt();
            try{
                future.get();
            }
            catch (ExecutionException e){
                throw e.getCause();
            }
        });
    }

    //sends the expected json for setup
    //where state0 is null, when a player is getting their treasure
    //make sure takes valid json
    @Test
    void testSetUpOptional() throws Exception {
        InputStream clientResponse = IOUtils.toInputStream("\"void\"", StandardCharsets.UTF_16);
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = setupFuture(Optional.empty());
        Thread t = new Thread(future);
        t.start();

        String expectedStr = "[\"setup\", [false, {\"column#\": 3,\"row#\": 3}]]";
        StringReader expectedStringReader = new StringReader(expectedStr);

        assertEquals(parseReader(expectedStringReader), parseReader(reader));
        future.get();

    }

    //sends the expected json for takeTurn
    //make sure takes valid json
    @Test
    void testTakeTurn() throws Exception {

        InputStream clientResponse = IOUtils.toInputStream("[6,\"UP\",0,{\"column#\":6,\"row#\":0}]",
                StandardCharsets.UTF_16);
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = new FutureTask<>(()-> {
            try {
                Optional<FullTurnInfo> result = player.takeTurn(inputState);
                assertEquals(Optional.of(new FullTurnInfo(0, new ShiftInfo(6, Direction.UP),
                        new Coordinate( 0, 6))), result);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }, null);
        Thread t = new Thread(future);
        t.start();

        String expectedStr = "[\"take-turn\", [" + gson.toJson(inputState) + "]]";
        StringReader expectedStringReader = new StringReader(expectedStr);

        assertEquals(parseReader(expectedStringReader), parseReader(reader));

    }

    //sends the expected json for takeTurn
    //make sure takes valid json
    @Test
    void testTakeTurnPass() throws Exception {
        InputStream clientResponse = IOUtils.toInputStream("\"PASS\"",
                StandardCharsets.UTF_16);
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = new FutureTask<>(()-> {
            try {
                Optional<FullTurnInfo> result = player.takeTurn(inputState);
                assertEquals(Optional.empty(), result);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }, null);
        Thread t = new Thread(future);
        t.start();

    }

    //sends the expected json for takeTurn
    //receives no json
    //expect an interrupted exception to be thrown
    @Test
    void testTakeTurnInterrupt() throws IOException {
        InputStream clientResponse = InputStream.nullInputStream();
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = new FutureTask<>(()-> {
            try {
                player.takeTurn(inputState);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }, null);
        Thread t = new Thread(future);
        t.start();

        assertThrows(RuntimeException.class, () -> {
            t.interrupt();
            try{
                future.get();
            }
            catch (ExecutionException e){
                throw e.getCause();
            }
        });
    }


    //sends the expected json for win
    //make sure takes valid json
    @Test
    void testWin() throws Exception {
        InputStream clientResponse = IOUtils.toInputStream("\"void\"", StandardCharsets.UTF_16);
        when(socket.getInputStream()).thenReturn(clientResponse);

        PlayerGameState inputState = createState();
        player.win(false);

        String expectedStr = "[\"win\", [false]]";
        StringReader expectedStringReader = new StringReader(expectedStr);

        assertEquals(parseReader(expectedStringReader), parseReader(reader));

    }

    //sends the expected json for win
    //receives no json
    //expect an interrupted exception to be thrown
    @Test
    void testWinInterrupt() throws IOException {
        InputStream clientResponse = InputStream.nullInputStream();
        when(socket.getInputStream()).thenReturn(clientResponse);

        FutureTask<?> future = new FutureTask<>(()-> {
            try {
                player.win(false);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }, null);
        Thread t = new Thread(future);
        t.start();

        assertThrows(RuntimeException.class, () -> {
            t.interrupt();
            try{
                future.get();
            }
            catch (ExecutionException e){
                throw e.getCause();
            }
        });
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

    }


}
