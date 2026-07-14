package com.dianpoint.summer.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SetUtils 单元测试")
class SetUtilsTest {

    @Test
    @DisplayName("isSet：参数为 Set 时返回 true")
    void testIsSet_withSet_returnsTrue() {
        assertThat(SetUtils.isSet(new HashSet<>())).isTrue();
    }

    @Test
    @DisplayName("isSet：参数为 List 时返回 false")
    void testIsSet_withList_returnsFalse() {
        assertThat(SetUtils.isSet(java.util.Collections.singletonList("a"))).isFalse();
    }

    @Test
    @DisplayName("of：传入 null 返回空 Set")
    void testOf_withNull_returnsEmptySet() {
        Set<String> result = SetUtils.of(null);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("of：传入空数组返回空 Set")
    void testOf_emptyArray_returnsEmptySet() {
        Set<String> result = SetUtils.of();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("of：传入单个元素返回单元素不可变 Set")
    void testOf_singleElement_returnsSingletonSet() {
        Set<String> result = SetUtils.of("hello");
        assertThat(result).hasSize(1).contains("hello");
    }

    @Test
    @DisplayName("of：传入多个元素返回不可变 Set")
    void testOf_multipleElements_returnsUnmodifiableSet() {
        Set<String> result = SetUtils.of("a", "b", "c");
        assertThat(result).hasSize(3).containsExactlyInAnyOrder("a", "b", "c");
    }

    @Test
    @DisplayName("ofSet：与 of 行为一致")
    void testOfSet_sameAsOf() {
        Set<Integer> result = SetUtils.ofSet(1, 2, 3);
        assertThat(result).hasSize(3).containsExactlyInAnyOrder(1, 2, 3);
    }
}
