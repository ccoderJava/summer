package com.dianpoint.summer.lang;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static com.dianpoint.summer.lang.Predicates.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: congccoder
 * @email: congccoder@gmail.com | <a href="https://github.com/ccoderJava">github-homepage</a>
 * @date: 2025/6/27 16:32
 */

public class PredicatesTest {

    @Test
    public void testEmptyArray() {
        assertEmptyArray(emptyArray());
    }

    private void assertEmptyArray(Predicate<?>[] predicates) {
        assertNotNull(predicates);
        assertEquals(0, predicates.length);
    }

    @Test
    public void testAlwaysTrue() {
        assertTrue(alwaysTrue().test(null));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(alwaysFalse().test(null));
    }

    @Test
    public void testAnd() {
        assertTrue(and(alwaysTrue(), alwaysTrue(), alwaysTrue()).test(null));
        assertFalse(and(alwaysFalse(), alwaysFalse(), alwaysFalse()).test(null));
        assertFalse(and(alwaysTrue(), alwaysFalse(), alwaysFalse()).test(null));
        assertFalse(and(alwaysTrue(), alwaysTrue(), alwaysFalse()).test(null));
    }


}
