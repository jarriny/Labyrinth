package Tests;

import Common.Board.Gem;
import Common.Board.GemPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GemPairTest {
    GemPair gm1;
    GemPair gm2;
    GemPair gm3;
    GemPair gm4;

    @BeforeEach
    void setUp() {
        gm1 = new GemPair(Gem.DIAMOND, Gem.EMERALD);
        gm2 = new GemPair(Gem.DIAMOND, Gem.EMERALD);
        gm3 = new GemPair(Gem.EMERALD, Gem.DIAMOND);
        gm4 = new GemPair(Gem.DIAMOND, Gem.YELLOW_BAGUETTE);
    }

    @Test
    void test_isSameUnordered_same_order_true() {
        assertTrue(this.gm1.isSameUnordered(gm2));
    }

    @Test
    void test_isSameUnordered_diff_order_true() {
        assertTrue(this.gm1.isSameUnordered(gm3));
    }

    @Test
    void test_isSameUnordered_false() {
        assertFalse(this.gm1.isSameUnordered(gm4));
    }
}