package Tests;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.Direction;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Players.Euclid;
import Players.player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Tile[] listOfTiles;
    PlayerGameState playerGameState;
    Board board;
    Tile spare;
    PlayerInfoPublic player1;
    PlayerInfoPublic player2;
    ArrayList<PlayerInfoPublic> publicPlayerInfos = new ArrayList<>();
    player pe;

    @BeforeEach
    void setUp() {
        listOfTiles = new Tile[]{
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

        board = new Board(7, 7, listOfTiles);
        spare = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));
        player1 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(1, 1),
                new GameColors("purple"));
        player2 = new PlayerInfoPublic(new Coordinate(3, 1), new Coordinate(3, 1),
                new GameColors("blue"));

        publicPlayerInfos.add(player1);
        publicPlayerInfos.add(player2);

        playerGameState = new PlayerGameState(board, spare, publicPlayerInfos,0, Optional.empty());

        pe = new player(new Euclid(), "Jim");

        Optional<PlayerGameState> state = Optional.of(playerGameState);
        pe.setup(state, new Coordinate(4,4));
    }

    // proposeboard
    @Test
    void test_proposeboard() {
        Board b = pe.proposeBoard0(7,7);
        assertEquals(this.board, b);
    }

    // setup
    @Test
    void test_setup_givenstate() {
        PlayerInfoPrivate info = pe.getPrivateInfo();
        assertFalse(info.isGoingHome());
        assertEquals(info.getGoal(), new Coordinate(4,4));
    }

    @Test
    void test_setup_givennone() {
        Optional<PlayerGameState> state = Optional.empty();
        pe.setup(state, new Coordinate(3,3));
        PlayerInfoPrivate info = pe.getPrivateInfo();
        assertTrue(info.isGoingHome());
        assertEquals(info.getGoal(), new Coordinate(4,4));
    }

    // taketurn
    @Test
    void test_taketurn_move() {
        PlayerInfoPublic player0 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(0, 0),
                new GameColors("purple"));
        publicPlayerInfos.add(0, player0);

        PlayerGameState state = new PlayerGameState(board, spare, publicPlayerInfos, 0, Optional.empty());

        Optional<FullTurnInfo> turnInfo = pe.takeTurn(state);
        assertEquals(new FullTurnInfo(90, new ShiftInfo(0, Direction.LEFT),
                new Coordinate(1, 6)), turnInfo.get());
    }

    @Test
    void test_taketurn_pass() {
        PlayerInfoPublic player0 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(2, 4),
                new GameColors("purple"));
        publicPlayerInfos.add(0, player0);

        PlayerGameState state = new PlayerGameState(board, spare, publicPlayerInfos, 0, Optional.empty());

        Optional<FullTurnInfo> turnInfo = pe.takeTurn(state);
        assertTrue(turnInfo.isEmpty());
    }


    @Test
    void test_win_true() {
        assertEquals(":)", pe.win(true));
    }

    @Test
    void test_win_false() {
        assertEquals(":(", pe.win(false));
    }
}