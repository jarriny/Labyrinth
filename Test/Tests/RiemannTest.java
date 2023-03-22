package Tests;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.Direction;
import Common.PartsOfTurn.*;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Players.Euclid;
import Players.Riemann;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiemannTest {
    Tile[] listOfTiles;
    PlayerGameState playerGameState;
    Board board;
    Tile spare;
    PlayerInfoPublic player1;
    PlayerInfoPublic player2;
    ArrayList<PlayerInfoPublic> publicPlayerInfos = new ArrayList<>();

    @BeforeEach
    void setUp() {

        listOfTiles = new Tile[]{
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.EMERALD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.APLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.APATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AZURITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALMANDINE_GARNET)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETHYST)),
                new Tile(new boolean[]{true, true, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETRINE)),
                new Tile(new boolean[]{true, true, false, true}, new GemPair(Gem.DIAMOND, Gem.AMMOLITE)),
                new Tile(new boolean[]{true, false, true, true}, new GemPair(Gem.DIAMOND, Gem.APRICOT_SQUARE_RADIANT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AQUAMARINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AUSTRALIAN_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AVENTURINE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CUSHION)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BERYL)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.BULLS_EYE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_ONYX)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_OBSIDIAN)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_SPINEL_CUSHION)),
                // row
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CEYLON_SAPPHIRE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_SPINEL_HEART)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CITRINE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CARNELIAN)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CHRYSOLITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CLINOHUMITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CHROME_DIOPSIDE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.CORDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.CITRINE_CHECKERBOARD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.COLOR_CHANGE_OVAL)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.DUMORTIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.FANCY_SPINEL_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GOLDSTONE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GRANDIDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GRAY_AGATE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_BERYL)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.GOLDEN_DIAMOND_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_AVENTURINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_BERYL_ANTIQUE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GROSSULAR_GARNET)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HEMATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HACKMANITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HELIOTROPE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.IOLITE_EMERALD_CUT)),
                new Tile(new boolean[]{true, true, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPER)),
                new Tile(new boolean[]{true, true, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPILITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE)),
        };

        board = new Board(7, 7, listOfTiles);
        spare = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));
        player1 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(1, 1),
                new GameColors("purple"));
        player2 = new PlayerInfoPublic(new Coordinate(3, 1), new Coordinate(3, 1),
                new GameColors("blue"));

        publicPlayerInfos.add(player1);
        publicPlayerInfos.add(player2);

        playerGameState = new PlayerGameState(board, spare, publicPlayerInfos, 0, Optional.empty());

    }


    @Test
    void test_move_to_next_poss() {
        PlayerInfoPublic player0 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(0, 0),
                new GameColors("purple"));
        PlayerInfoPrivate playerPrivate = new PlayerInfoPrivate(new Coordinate(6, 6));
        publicPlayerInfos.add(0, player0);
        spare = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));
        PlayerGameState state = new PlayerGameState(board, spare, publicPlayerInfos,
                0, Optional.empty());
        Riemann stratForUnreachable = new Riemann(state, player0, playerPrivate);
        Optional<FullTurnInfo> turnInfo = stratForUnreachable.findBestTurn();
        assertEquals(new FullTurnInfo(180, new ShiftInfo(0, Direction.RIGHT),
                new Coordinate(0, 0)), turnInfo.get());
    }

    @Test
    void test_move_to_next_poss2() {
        PlayerInfoPrivate playerPrivate = new PlayerInfoPrivate(new Coordinate(6, 6));
        PlayerGameState state = new PlayerGameState(board, spare, publicPlayerInfos,
                0, Optional.empty());
        Riemann strat = new Riemann(state, player1, playerPrivate);
        Optional<FullTurnInfo> turnInfo = strat.findBestTurn();
        assertEquals(new FullTurnInfo(0, new ShiftInfo(0, Direction.LEFT),
                new Coordinate(0, 2)), turnInfo.get());
    }

    @Test
    void test_move_no_possible() {
        PlayerInfoPublic player0 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(1, 5),
                new GameColors("purple"));
        PlayerInfoPrivate playerPrivate = new PlayerInfoPrivate(new Coordinate(5, 3));
        publicPlayerInfos.add(0, player0);
        PlayerGameState state = new PlayerGameState(board, spare, publicPlayerInfos,0, Optional.empty());
        Euclid strat = new Euclid(state, player0, playerPrivate);
        Optional<FullTurnInfo> turnInfo = strat.findBestTurn();

        assertTrue(turnInfo.isEmpty());
    }

    @Test
    void test_move_to_goal() {
        PlayerInfoPublic player0 = new PlayerInfoPublic(new Coordinate(0, 0), new Coordinate(6, 4),
                new GameColors("purple"));
        PlayerInfoPrivate playerPrivate = new PlayerInfoPrivate(new Coordinate(6, 6));
        publicPlayerInfos.add(0, player0);
        PlayerGameState state = new PlayerGameState(board, spare, publicPlayerInfos,
                0, Optional.empty());
        Riemann strat = new Riemann(state, player0, playerPrivate);
        Optional<FullTurnInfo> turnInfo = strat.findBestTurn();
        assertEquals(new FullTurnInfo(0, new ShiftInfo(0, Direction.LEFT),
                new Coordinate(6, 6)), turnInfo.get());
    }
}
