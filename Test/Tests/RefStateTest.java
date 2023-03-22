package Tests;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Common.State.RefState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefStateTest {
    Tile[] listOfTiles;
    Board board;
    Tile spare;
    ArrayList<PlayerInfoPublic> playerInfoPublics;
    ArrayList<PlayerInfoPrivate> playerInfoPrivates;
    RefState refState;

    @BeforeEach
    void setUp() {
        listOfTiles = new Tile[]{
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.EMERALD)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.APLITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.APATITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AZURITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.ALMANDINE_GARNET)),
                // row
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AMETHYST)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AMETRINE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AMMOLITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.APRICOT_SQUARE_RADIANT)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AQUAMARINE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AUSTRALIAN_MARQUISE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.AVENTURINE)),
                // row
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CUSHION)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BERYL)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BULLS_EYE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_ONYX)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_OBSIDIAN)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_SPINEL_CUSHION)),
                // row
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CEYLON_SAPPHIRE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_SPINEL_HEART)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CITRINE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CARNELIAN)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CHRYSOLITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CLINOHUMITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CHROME_DIOPSIDE)),
                // row
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CORDIERITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CITRINE_CHECKERBOARD)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.COLOR_CHANGE_OVAL)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.DUMORTIERITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.FANCY_SPINEL_MARQUISE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GARNET)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GOLDSTONE)),
                // row
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GRANDIDIERITE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GRAY_AGATE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_BERYL)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GOLDEN_DIAMOND_CUT)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_AVENTURINE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_BERYL_ANTIQUE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.GROSSULAR_GARNET)),
                // row
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.HEMATITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.HACKMANITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.HELIOTROPE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.IOLITE_EMERALD_CUT)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPER)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPILITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE)),
        };

        board = new Board(7,7, listOfTiles);
        spare = new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));

        playerInfoPublics = new ArrayList<>();

        playerInfoPublics.add(new PlayerInfoPublic(new Coordinate(1,1), new Coordinate(1,1), new GameColors("black")));
        playerInfoPublics.add(new PlayerInfoPublic(new Coordinate(3,1), new Coordinate(3,1), new GameColors("red")));
        playerInfoPublics.add(new PlayerInfoPublic(new Coordinate(5,1), new Coordinate(5,1), new GameColors("blue")));

        playerInfoPrivates = new ArrayList<>();

        playerInfoPrivates.add(new PlayerInfoPrivate(new Coordinate(5,5)));
        playerInfoPrivates.add(new PlayerInfoPrivate(new Coordinate(3,5)));
        playerInfoPrivates.add(new PlayerInfoPrivate(new Coordinate(1,5)));

        refState = new RefState(this.board, this.spare, this.playerInfoPublics, this.playerInfoPrivates, 0, Optional.empty());
    }

    // completePlayerTurnAndReadyForNextPlayer
    // goToNextPlayer
    // isOnGoalTile
    // kickPlayerOut

    @Test
    void test_nextPlayer() {
        assertEquals(this.playerInfoPublics.get(0), this.refState.getCurrentPublicPlayerInfo());
        RefState rs = this.refState.goToNextPlayer();
        assertEquals(this.playerInfoPublics.get(1), rs.getCurrentPublicPlayerInfo());
        rs = rs.goToNextPlayer();
        assertEquals(this.playerInfoPublics.get(2), rs.getCurrentPublicPlayerInfo());
        rs = rs.goToNextPlayer();
        assertEquals(this.playerInfoPublics.get(0), rs.getCurrentPublicPlayerInfo());
    }

    @Test
    void test_toPlayerGameState() {
        PlayerGameState pgs = new PlayerGameState(this.board, this.spare, this.playerInfoPublics, 0, Optional.empty());
        assertEquals(pgs, this.refState.toPlayerGameState());
    }

}