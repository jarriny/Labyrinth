package Tests;

import Common.*;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    Tile t1;
    Tile t2;
    Tile t3;
    Tile t4;
    @BeforeEach
    void setUp() {
        t1 = new Tile(new boolean[]{true,false,true,false}, new GemPair(Gem.ALEXANDRITE, Gem.YELLOW_BAGUETTE));
        t2 = new Tile(new boolean[]{true,false,false,true}, new GemPair(Gem.YELLOW_BAGUETTE, Gem.ALEXANDRITE));
        t3 = new Tile(new boolean[]{false,true,true,true}, new GemPair(Gem.TANZANITE_TRILLION, Gem.YELLOW_BAGUETTE));
        t4 = new Tile(new boolean[]{true,true,true,true}, new GemPair(Gem.BERYL, Gem.YELLOW_BAGUETTE));
    }

    @Test
    void testRotatePipeShape90() {
        t1 = t1.copyAndRotate(90);
        assertArrayEquals(t1.getShape(), new boolean[]{false,true,false,true});
        t1 = t1.copyAndRotate(90);
        assertArrayEquals(t1.getShape(), new boolean[]{true,false,true,false});
    }

    @Test
    void testRotatePipeShape180() {
        t1 = t1.copyAndRotate(180);
        assertArrayEquals(t1.getShape(), new boolean[]{true,false,true,false});
    }

    @Test
    void testRotatePipeShape270() {
        t1 = t1.copyAndRotate(270);
        assertArrayEquals(t1.getShape(), new boolean[]{false,true,false,true});
    }
    
    @Test
    void testRotateLShape90() {
        t2 = t2.copyAndRotate(90);
        assertArrayEquals(t2.getShape(), new boolean[]{false,false,true,true});
        t2 = t2.copyAndRotate(90);
        assertArrayEquals(t2.getShape(), new boolean[]{false,true,true,false});
        t2 = t2.copyAndRotate(90);
        assertArrayEquals(t2.getShape(), new boolean[]{true,true,false,false});
        t2 = t2.copyAndRotate(90);
        assertArrayEquals(t2.getShape(), new boolean[]{true,false,false,true});
    }

    @Test
    void testRotateLShape180() {
        t2 = t2.copyAndRotate(180);
        assertArrayEquals(t2.getShape(), new boolean[]{false,true,true,false});
        t2 = t2.copyAndRotate(180);
        assertArrayEquals(t2.getShape(), new boolean[]{true,false,false,true});
    }

    @Test
    void testRotateLShape270() {
        t2 = t2.copyAndRotate(270);
        assertArrayEquals(t2.getShape(), new boolean[]{true,true,false,false});
    }


    @Test
    void testRotateTShape90() {
        t3 = t3.copyAndRotate(90);
        assertArrayEquals(t3.getShape(), new boolean[]{true,true,true,false});
        t3 = t3.copyAndRotate(90);
        assertArrayEquals(t3.getShape(), new boolean[]{true,true,false,true});
        t3 = t3.copyAndRotate(90);
        assertArrayEquals(t3.getShape(), new boolean[]{true,false,true,true});
        t3 = t3.copyAndRotate(90);
        assertArrayEquals(t3.getShape(), new boolean[]{false,true,true,true});
    }

    @Test
    void testRotateTShape180() {
        t3 = t3.copyAndRotate(180);
        assertArrayEquals(t3.getShape(), new boolean[]{true,true,false,true});
        t3 = t3.copyAndRotate(180);
        assertArrayEquals(t3.getShape(), new boolean[]{false,true,true,true});
    }

    @Test
    void testRotateTShape270() {
        t3 = t3.copyAndRotate(270);
        assertArrayEquals(t3.getShape(), new boolean[]{true,false,true,true});
    }

    @Test
    void testAreConnectedUPTrue() {
        assertTrue(t4.areConnected(t1, Direction.UP));
    }

    @Test
    void testAreConnectedUPFalse() {
        assertFalse(t4.areConnected(t2, Direction.UP));
    }

    @Test
    void testAreConnectedDOWNTrue() {
        assertTrue(t4.areConnected(t1, Direction.DOWN));
    }

    @Test
    void testAreConnectedDOWNFalse() {
        assertFalse(t4.areConnected(t3, Direction.DOWN));
    }

    @Test
    void testAreConnectedLEFTTrue() {
        assertTrue(t4.areConnected(t3, Direction.LEFT));
    }

    @Test
    void testAreConnectedLEFTFalse() {
        assertFalse(t4.areConnected(t1, Direction.LEFT));
    }

    @Test
    void testAreConnectedRIGHTTrue() {
        assertTrue(t4.areConnected(t3, Direction.RIGHT));
    }

    @Test
    void testAreConnectedRIGHTFalse() {
        assertFalse(t4.areConnected(t1, Direction.RIGHT));
    }

    @Test
    void testHaveSameGemPairTrue() {
        assertTrue(t1.haveSameGemPair(t2));
    }

    @Test
    void testHaveSameGemPairFalse() {
        assertFalse(t1.haveSameGemPair(t3));
    }
}