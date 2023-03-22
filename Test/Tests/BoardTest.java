package Tests;

import Common.*;
import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    Tile[] listOfTiles;
    Board board;
    Tile spare;

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
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETRINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMMOLITE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.APRICOT_SQUARE_RADIANT)),
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
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPER)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPILITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE)),
        };

        board = new Board(7,7, listOfTiles);
        spare = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));
    }



    @Test
    void test_constructor_given_dimensions_and_tiles() {
        assertEquals(this.board.getRowCount(), 7);
        assertEquals(this.board.getColCount(), 7);

        Tile[][] boardRep = this.board.getBoardRep();
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                assertEquals(this.listOfTiles[r * 7 + c], boardRep[r][c]);
            }
        }
    }

    @Test
    void test_constructor_given_dimensions_and_no_tiles() {
        Exception exception = Assertions
            .assertThrows(IllegalArgumentException.class, () -> new Board(7,7, new Tile[0]));
        assertEquals(exception.getMessage(), "Invalid number tiles given");
    }

    @Test
    void test_constructor_given_dimensions_and_wrong_number_of_tiles() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Board(7,7, Arrays.copyOf(this.listOfTiles, this.listOfTiles.length-1)));
        assertEquals(exception.getMessage(), "Invalid number tiles given");
    }

    @Test
    void test_constructor_invalid_col_dimension_and_tiles() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Board(6,0, this.listOfTiles));
        assertEquals(exception.getMessage(), "Valid column number not given");
    }

    @Test
    void test_constructor_invalid_row_dimension_and_tiles() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Board(0,6, this.listOfTiles));
        assertEquals(exception.getMessage(), "Valid row number not given");
    }

    @Test
    void test_constructor_board_too_large() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Board(73,73, this.listOfTiles));
        assertEquals(exception.getMessage(), "Size of board require more tiles than possible number of tiles");
    }

    @Test
    void test_make_board_repeat_gems() {
        this.listOfTiles[1] = new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.EMERALD));
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Board(7,7, this.listOfTiles));
        assertEquals(exception.getMessage(), "All tiles need unordered unique pair of gems.");
    }

    @Test
    void test_turn_odd() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> this.board.copyAndTurn(Direction.DOWN, 1, this.spare));
        assertEquals(exception.getMessage(), "Index needs to be even since only even rows and columns are movable.");
    }

    @Test
    void test_turn_with_slide_left() {
        Board board = this.board.copyAndTurn(Direction.LEFT, 0, this.spare);

        Tile[] expectedFirstRow = new Tile[] {
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.APLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.APATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AZURITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALMANDINE_GARNET)),
                this.spare};

        Tile[] boardFirstRow = board.getBoardRep()[0];
        for (int i = 0; i < 7; i++) {
            assertEquals(expectedFirstRow[i], boardFirstRow[i]);
        }
    }

    @Test
    void test_turn_with_slide_right() {
        Board board = this.board.copyAndTurn(Direction.RIGHT, 0, this.spare);

        Tile[] expectedFirstRow = new Tile[] {this.spare,
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.EMERALD)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE_PEAR_SHAPE)),
            new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.APLITE)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.APATITE)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AZURITE))};

        Tile[] boardFirstRow = board.getBoardRep()[0];
        for (int i = 0; i < 7; i++) {
            assertEquals(expectedFirstRow[i], boardFirstRow[i]);
        }
    }

    @Test
    void test_turn_with_slide_down() {
        Board board = this.board.copyAndTurn(Direction.DOWN, 0, this.spare);

        Tile[] expectedFirstRow = new Tile[] {this.spare,
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.EMERALD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETHYST)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CUSHION)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CEYLON_SAPPHIRE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.CORDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GRANDIDIERITE))};

        for (int i = 0; i < 7; i++) {
            assertEquals(expectedFirstRow[i], board.getBoardRep()[i][0]);
        }
    }

    @Test
    void test_turn_with_slide_up() {
        Board board = this.board.copyAndTurn(Direction.UP, 0, this.spare);

        Tile[] expectedFirstRow = new Tile[] {
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETHYST)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CUSHION)),
            new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CEYLON_SAPPHIRE)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.CORDIERITE)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GRANDIDIERITE)),
            new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HEMATITE)),
                this.spare};

        for (int i = 0; i < 7; i++) {
            assertEquals(expectedFirstRow[i], board.getBoardRep()[i][0]);
        }
    }

    @Test
    void test_reachable_none(){
        Set<Coordinate> expectedSet = new HashSet<>();
        expectedSet.add(new Coordinate(0,0));
        assertEquals(expectedSet, this.board.reachable(new Coordinate(0,0)));
    }

    // see if better way to compare two sets
    @Test
    void test_reachable() {
        Set<Coordinate> expectedSet = new HashSet<>();
        expectedSet.add(new Coordinate(0,3));
        expectedSet.add(new Coordinate(1,3));
        expectedSet.add(new Coordinate(2,3));
        expectedSet.add(new Coordinate(3,0));
        expectedSet.add(new Coordinate(3,1));
        expectedSet.add(new Coordinate(3,2));
        expectedSet.add(new Coordinate(3,3));
        expectedSet.add(new Coordinate(3,4));
        expectedSet.add(new Coordinate(3,5));
        expectedSet.add(new Coordinate(3,6));
        expectedSet.add(new Coordinate(4,3));
        expectedSet.add(new Coordinate(5,3));
        expectedSet.add(new Coordinate(6,3));

        Set<Coordinate> set = this.board.reachable(new Coordinate(3,3));

        for (Coordinate c: set) {
            expectedSet.remove(c);
        }
        assertTrue(expectedSet.isEmpty());
    }

    @Test
    void test_getCoordinatesOfTile_valid() {
        assertEquals( new Coordinate(4,3),
                this.board.getCoordinatesOfTile(
                        new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.DUMORTIERITE))));
    }

    @Test
    void test_getCoordinatesOfTile_invalid_w_same_pair() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> this.board.getCoordinatesOfTile(
                        new Tile(new boolean[]{true, false, true, true}, new GemPair(Gem.DIAMOND, Gem.DUMORTIERITE))));
        assertEquals(exception.getMessage(), "Tile not found in board");
    }

    @Test
    void test_getCoordinatesOfTile_invalid_w_diff_pair() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> this.board.getCoordinatesOfTile(
                        new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.MAGNESITE, Gem.DUMORTIERITE))));
        assertEquals(exception.getMessage(), "Tile not found in board");
    }
}