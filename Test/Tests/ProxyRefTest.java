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
import Serialization.ChoiceAdapter;
import Serialization.StateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.gson.JsonParser.parseReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ProxyRefTest {


    Remote.referee referee;
    FutureTask<?> refereeFuture;
    Thread refereeThread;

    @Mock
    IPlayer player;

    @Mock
    Socket socket;

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



    /**
     * Proxy referee
     * - Accept valid json inputs
     *  - wait for json commands
     *  - read a valid command for each type
     *
     * - Call corresponding method in its player
     * - Encode valid json responses
     * - sends back correct json
     */

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        this.referee = new Remote.referee(socket, player);
        PipedInputStream inputStream = new PipedInputStream();
        reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
        playerOutputStream = new BufferedOutputStream(new PipedOutputStream(inputStream));
        when(socket.getOutputStream()).thenReturn(playerOutputStream);

        GsonBuilder gsonBuilder = new GsonBuilder();
        StateAdapter.register(gsonBuilder);
        ChoiceAdapter.register(gsonBuilder);
        gson = gsonBuilder.create();

        inputState = createState();

        refereeFuture = makeRefereeFuture();
        refereeThread = new Thread(refereeFuture);
    }

    @AfterEach
    void cleanUp() throws ExecutionException, InterruptedException, TimeoutException {
        refereeFuture.cancel(true);
        refereeThread.stop();
    }

    //Accept valid json inputs
    //make sure sends void
    @Test
    void testSetUp() throws Exception {
        String remoteInput = "[\"setup\", [" + gson.toJson(inputState) + ", {\"column#\": 3,\"row#\": 3}]]";
        InputStream remoteInputStream = IOUtils.toInputStream(remoteInput, StandardCharsets.UTF_8);
        when(socket.getInputStream()).thenReturn(remoteInputStream);
        doNothing().when(player).setup(optionalGameStateCaptor.capture(), coordinateCaptor.capture());
        refereeThread.start();

        refereeFuture.get(100, TimeUnit.MILLISECONDS);
        Optional<PlayerGameState> gameState = optionalGameStateCaptor.getValue();
        Coordinate coordiante = coordinateCaptor.getValue();

        System.out.println(this.inputState.equals(gameState.get()));

        assertEquals(Optional.of(this.inputState), gameState);
        assertEquals(new Coordinate(3, 3), coordiante);

        StringReader expectedStringReader = new StringReader("\"void\"");
        assertEquals(parseReader(expectedStringReader), parseReader(reader));

    }

    //Accept valid json inputs
    //make sure sends void
    @Test
    void testSetUpOptional() throws Exception {
        String remoteInput = "[\"setup\", [false, {\"column#\": 3,\"row#\": 3}]]";
        InputStream remoteInputStream = IOUtils.toInputStream(remoteInput, StandardCharsets.UTF_8);
        when(socket.getInputStream()).thenReturn(remoteInputStream);

        doNothing().when(player).setup(optionalGameStateCaptor.capture(), coordinateCaptor.capture());

        refereeThread.start();
        refereeFuture.get(100, TimeUnit.MILLISECONDS);

        Optional<PlayerGameState> gameState = optionalGameStateCaptor.getValue();
        Coordinate coordiante = coordinateCaptor.getValue();

        assertEquals(Optional.empty(), gameState);
        assertEquals(new Coordinate(3, 3), coordiante);

        StringReader expectedStringReader = new StringReader("\"void\"");
        assertEquals(parseReader(expectedStringReader), parseReader(reader));

    }

    //Accept valid json inputs
    //make sure sends a valid Choice
    @Test
    void testTakeTurnPass() throws Exception {
        String remoteInput = "[\"take-turn\", [" + gson.toJson(inputState) + "]]";
        InputStream remoteInputStream = IOUtils.toInputStream(remoteInput, StandardCharsets.UTF_8);
        when(socket.getInputStream()).thenReturn(remoteInputStream);

        when(player.takeTurn(gameStateCaptor.capture())).thenReturn(Optional.empty());

        refereeThread.start();
        refereeFuture.get(100, TimeUnit.MILLISECONDS);

        PlayerGameState gameState = gameStateCaptor.getValue();

        assertEquals(this.inputState, gameState);

        StringReader expectedStringReader = new StringReader("\"PASS\"");
        assertEquals(parseReader(expectedStringReader), parseReader(reader));

    }

    //Accept valid json inputs
    //make sure sends a valid Choice
    @Test
    void testTakeTurn() throws Exception {
        String remoteInput = "[\"take-turn\", [" + gson.toJson(inputState) + "]]";
        InputStream remoteInputStream = IOUtils.toInputStream(remoteInput, StandardCharsets.UTF_8);
        when(socket.getInputStream()).thenReturn(remoteInputStream);

        FullTurnInfo expectedTurnInfo = new FullTurnInfo(90 ,
                new ShiftInfo(0, Direction.UP), new Coordinate(5, 4));

        when(player.takeTurn(gameStateCaptor.capture())).thenReturn(Optional.of(expectedTurnInfo));

        refereeThread.start();
        refereeFuture.get(100, TimeUnit.MILLISECONDS);

        PlayerGameState gameState = gameStateCaptor.getValue();

        assertEquals(this.inputState, gameState);

        StringReader expectedStringReader =
                new StringReader(gson.toJson(Optional.of(expectedTurnInfo), ChoiceAdapter.type));
        assertEquals(parseReader(expectedStringReader), parseReader(reader));

    }

    //Accept valid json inputs
    //make sure sends void
    @Test
    void testWin() throws Exception {
        String remoteInput = "[\"win\", [false]]";
        InputStream remoteInputStream = IOUtils.toInputStream(remoteInput, StandardCharsets.UTF_8);
        when(socket.getInputStream()).thenReturn(remoteInputStream);

        when(player.win(boolCaptor.capture())).thenReturn("false");

        refereeThread.start();
        refereeFuture.get(100, TimeUnit.MILLISECONDS);

        boolean boolValue = boolCaptor.getValue();
        assertFalse(boolValue);

        StringReader expectedStringReader = new StringReader("\"void\"");
        assertEquals(parseReader(expectedStringReader), parseReader(reader));
    }

    private FutureTask<?> makeRefereeFuture(){
        return new FutureTask<>(()-> {
            try {
                referee.runEntireGame();
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

    }
}
