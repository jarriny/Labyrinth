//package Tests;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.RefState;
import Players.Euclid;
import Players.IPlayer;
import Players.Riemann;
import Players.player;
import Referee.referee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RefereeTest {
    Tile[] listOfTiles;
    Tile[] dummyTiles;
    Tile spare;
    Board board;
    Board dummyBoard;
    RefState state;
    RefState dummyState;
    PlayerInfoPublic playerpublic1;
    PlayerInfoPublic playerpublic2;
    PlayerInfoPrivate playerprivate1;
    PlayerInfoPrivate playerprivate2;
    ArrayList<PlayerInfoPublic> publicPlayerInfos = new ArrayList<>();
    ArrayList<PlayerInfoPrivate> privatePlayerInfos = new ArrayList<>();
    ArrayList<IPlayer> players = new ArrayList<>();
    player pe;
    player pr;

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
        // to easily test pass
        dummyTiles = new Tile[]{
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.ALEXANDRITE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.ALEXANDRITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.APLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.APATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AZURITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.ALMANDINE_GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AMETHYST)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AMETRINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AMMOLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.APRICOT_SQUARE_RADIANT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AQUAMARINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AUSTRALIAN_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.AVENTURINE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLUE_CUSHION)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLUE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BERYL)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BULLS_EYE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLACK_ONYX)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLACK_OBSIDIAN)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLACK_SPINEL_CUSHION)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLUE_CEYLON_SAPPHIRE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.BLUE_SPINEL_HEART)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CITRINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CARNELIAN)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CHRYSOLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CLINOHUMITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CHROME_DIOPSIDE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CORDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.CITRINE_CHECKERBOARD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.COLOR_CHANGE_OVAL)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.DUMORTIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.EMERALD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.FANCY_SPINEL_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GOLDSTONE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GRANDIDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GRAY_AGATE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GREEN_BERYL)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GOLDEN_DIAMOND_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GREEN_AVENTURINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GREEN_BERYL_ANTIQUE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.GROSSULAR_GARNET)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.HEMATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.HACKMANITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.HELIOTROPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.IOLITE_EMERALD_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.JASPER)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.JASPILITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.ALEXANDRITE_PEAR_SHAPE, Gem.KUNZITE)),
        };

        board = new Board(7, 7, listOfTiles);
        dummyBoard = new Board(7, 7, dummyTiles);

        spare = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));

        playerpublic1 = new PlayerInfoPublic(new Coordinate(5, 5), new Coordinate(3, 3),
                new GameColors("purple"));
        playerpublic2 = new PlayerInfoPublic(new Coordinate(1, 1), new Coordinate(3, 3),
                new GameColors("blue"));
        publicPlayerInfos.add(playerpublic1);
        publicPlayerInfos.add(playerpublic2);

        playerprivate1 = new PlayerInfoPrivate(new Coordinate(1, 1));
        playerprivate2 = new PlayerInfoPrivate(new Coordinate(5, 5));
        privatePlayerInfos.add(playerprivate1);
        privatePlayerInfos.add(playerprivate2);

        dummyState = new RefState(board, spare, publicPlayerInfos,privatePlayerInfos,0, Optional.empty());
        state = new RefState(board, spare, publicPlayerInfos,privatePlayerInfos,0, Optional.empty());

        pe = new player(new Euclid(), "Monroe");
        pr = new player(new Riemann(), "Yolanda");

        players.add(pe);
        players.add(pr);
    }

    @Test
    void test_run_fullGame(){
        this.publicPlayerInfos = new ArrayList<>();

        playerpublic1 = new PlayerInfoPublic(new Coordinate(1, 1), new Coordinate(3, 3),
                new GameColors("purple"));
        playerpublic2 = new PlayerInfoPublic(new Coordinate(5, 5), new Coordinate(3, 3),
                new GameColors("blue"));
        publicPlayerInfos.add(playerpublic1);
        publicPlayerInfos.add(playerpublic2);

        this.privatePlayerInfos = new ArrayList<>();
        playerprivate1 = new PlayerInfoPrivate(new Coordinate(3, 1));
        playerprivate2 = new PlayerInfoPrivate(new Coordinate(3, 1));

        privatePlayerInfos.add(playerprivate2);
        privatePlayerInfos.add(playerprivate1);

        state = new RefState(board, spare, publicPlayerInfos,privatePlayerInfos,0, Optional.empty());

        referee ref = new referee();
        ref.runEntireGame(state, players);
        assertFalse(pr.isWon());
        assertTrue(pe.isWon());
    }

    @Test
    void test_run_untilAllPass(){

        Board board2 = new Board(7, 7, listOfTiles);

        Tile spare2 = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));

        ArrayList<PlayerInfoPublic> publicPlayerInfos2 = new ArrayList<>();
        ArrayList<PlayerInfoPrivate> privatePlayerInfos2 = new ArrayList<>();

        PlayerInfoPublic player0 = new PlayerInfoPublic(new Coordinate(3, 3), new Coordinate(1, 5),
                new GameColors("purple"));
        PlayerInfoPrivate playerPrivate = new PlayerInfoPrivate(new Coordinate(5, 3));

        privatePlayerInfos2.add(playerPrivate);
        publicPlayerInfos2.add(0, player0);

        PlayerInfoPublic player02 = new PlayerInfoPublic(new Coordinate(1, 1), new Coordinate(1, 5),
                new GameColors("blue"));
        PlayerInfoPrivate playerPrivate2 = new PlayerInfoPrivate(new Coordinate(5, 3));
        publicPlayerInfos2.add(0, player02);
        privatePlayerInfos2.add(playerPrivate2);

        RefState state = new RefState(board2, spare2, publicPlayerInfos2,privatePlayerInfos2,
                0, Optional.empty());


        player pe2 = new player(new Euclid(), "Ben");
        player pr2 = new player(new Euclid(), "Matthias");

        ArrayList<IPlayer> players2 = new ArrayList<>();
        players2.add(pe2);
        players2.add(pr2);

        referee ref = new referee();
        ref.runEntireGame(state, players2);

        assertTrue(pe2.isWon());
        assertTrue(pr2.isWon());
    }

    @Test
    void test_run_until1000rounds(){
        referee ref = new referee();
        ref.runEntireGame(state, players);
        assertFalse(pr.isWon());
        assertTrue(pe.isWon());
    }

    //running the game successfully with multiple goals
    @Test
    void testMultipleGoals() {
        this.publicPlayerInfos = new ArrayList<>();

        playerpublic1 = new PlayerInfoPublic(new Coordinate(1, 1), new Coordinate(3, 3),
                new GameColors("purple"));
        playerpublic2 = new PlayerInfoPublic(new Coordinate(5, 5), new Coordinate(3, 3),
                new GameColors("blue"));
        publicPlayerInfos.add(playerpublic1);
        publicPlayerInfos.add(playerpublic2);

        this.privatePlayerInfos = new ArrayList<>();
        playerprivate1 = new PlayerInfoPrivate(new Coordinate(3, 1));
        playerprivate2 = new PlayerInfoPrivate(new Coordinate(3, 1));

        privatePlayerInfos.add(playerprivate2);
        privatePlayerInfos.add(playerprivate1);

        state = new RefState(board, spare, publicPlayerInfos,privatePlayerInfos,0, Optional.empty());

        referee ref = new referee(Optional.empty());
        ref.runEntireGame(state, players);
        assertFalse(pe.isWon());
        assertTrue(pr.isWon());
    }

    //test with one winner that reached more goals than the other
    @Test
    void testGetPlayersWithMostGoalsReached() {
        this.publicPlayerInfos = new ArrayList<>();

        playerpublic1 = new PlayerInfoPublic(new Coordinate(1, 1), new Coordinate(1, 1),
                new GameColors("purple"));
        playerpublic2 = new PlayerInfoPublic(new Coordinate(5, 5), new Coordinate(5, 5),
                new GameColors("blue"));
        publicPlayerInfos.add(playerpublic1);
        publicPlayerInfos.add(playerpublic2);

        this.privatePlayerInfos = new ArrayList<>();
        playerprivate1 = new PlayerInfoPrivate(new Coordinate(3, 1));
        playerprivate2 = new PlayerInfoPrivate(new Coordinate(5, 1));

        privatePlayerInfos.add(playerprivate2);
        privatePlayerInfos.add(playerprivate1);

        state = new RefState(board, spare, publicPlayerInfos,privatePlayerInfos,0, Optional.empty());

        referee ref = new referee(Optional.empty());
        ref.runEntireGame(state, players);
        assertTrue(pe.getPrivateInfo().getGoalsReached() < pr.getPrivateInfo().getGoalsReached());
        assertFalse(pe.isWon());
        assertTrue(pr.isWon());
    }

    //test where both players got the same amount of goals, and winner is chosen by Euclidean distance
    @Test
    void testGetDistanceTieGoalsReached() {
        this.publicPlayerInfos = new ArrayList<>();

        playerpublic1 = new PlayerInfoPublic(new Coordinate(1, 1), new Coordinate(1, 1),
                new GameColors("purple"));
        playerpublic2 = new PlayerInfoPublic(new Coordinate(5, 5), new Coordinate(5, 5),
                new GameColors("blue"));
        publicPlayerInfos.add(playerpublic1);
        publicPlayerInfos.add(playerpublic2);

        this.privatePlayerInfos = new ArrayList<>();
        playerprivate1 = new PlayerInfoPrivate(new Coordinate(3, 1));
        playerprivate2 = new PlayerInfoPrivate(new Coordinate(3, 1));

        privatePlayerInfos.add(playerprivate1);
        privatePlayerInfos.add(playerprivate2);

        state = new RefState(board, spare, publicPlayerInfos,privatePlayerInfos,0, Optional.empty());

        referee ref = new referee(Optional.empty());
        ref.runEntireGame(state, players);
        assertEquals(state.getPrivatePlayerInfos().get(0).getGoalsReached(),
                state.getPrivatePlayerInfos().get(1).getGoalsReached());
        assertFalse(pr.isWon());
        assertTrue(pe.isWon());
    }

    @Test
    void testMinEuclidDistanceFromGoal() {

    }
}
