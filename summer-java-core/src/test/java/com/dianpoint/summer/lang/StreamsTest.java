package com.dianpoint.summer.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dianpoint.summer.lang.Predicates.alwaysFalse;
import static com.dianpoint.summer.lang.Predicates.alwaysTrue;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Streams 单元测试")
class StreamsTest {

    @Test
    @DisplayName("stream：传入数组返回对应 Stream")
    void testStream_fromArray() {
        List<String> result = Streams.stream("a", "b", "c").collect(java.util.stream.Collectors.toList());
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    @DisplayName("stream：传入 Iterable 返回对应 Stream")
    void testStream_fromIterable() {
        List<String> list = Arrays.asList("x", "y");
        List<String> result = Streams.stream(list).collect(java.util.stream.Collectors.toList());
        assertThat(result).containsExactly("x", "y");
    }

    @Test
    @DisplayName("filterStream：按条件过滤数组")
    void testFilterStream_fromArray() {
        List<String> result = Streams.filterStream(new String[]{"a1", "b2", "c1", "d3"}, s -> s.endsWith("1"))
                .collect(java.util.stream.Collectors.toList());
        assertThat(result).containsExactly("a1", "c1");
    }

    @Test
    @DisplayName("filterStream：按条件过滤 Iterable")
    void testFilterStream_fromIterable() {
        List<String> result = Streams.filterStream(Arrays.asList("a1", "b2", "c1"), s -> s.endsWith("2"))
                .collect(java.util.stream.Collectors.toList());
        assertThat(result).containsExactly("b2");
    }

    @Test
    @DisplayName("filterList：过滤数组返回 List")
    void testFilterList_fromArray() {
        List<String> result = Streams.filterList(new String[]{"x", "y", "z"}, s -> !s.equals("y"));
        assertThat(result).containsExactly("x", "z");
    }

    @Test
    @DisplayName("filterList：过滤 Iterable 返回 List")
    void testFilterList_fromIterable() {
        List<Integer> result = Streams.filterList(Arrays.asList(1, 2, 3, 4), n -> n % 2 == 0);
        assertThat(result).containsExactly(2, 4);
    }

    @Test
    @DisplayName("filterSet：过滤数组返回 Set")
    void testFilterSet_fromArray() {
        Set<String> result = Streams.filterSet(new String[]{"a", "b", "a", "c"}, s -> !s.equals("c"));
        assertThat(result).hasSize(2).containsExactlyInAnyOrder("a", "b");
    }

    @Test
    @DisplayName("filter：对 Set 类型返回 Set")
    void testFilter_returnsSet() {
        Set<String> input = new HashSet<>(Arrays.asList("a1", "a2", "b1"));
        Set<String> result = Streams.filter(input, s -> s.startsWith("a"));
        assertThat(result).isInstanceOf(Set.class).hasSize(2);
    }

    @Test
    @DisplayName("filter：对 List 类型返回 List")
    void testFilter_returnsList() {
        List<String> result = Streams.filter(Arrays.asList("a1", "b1", "a2"), s -> s.startsWith("a"));
        assertThat(result).isInstanceOf(List.class).hasSize(2);
    }

    @Test
    @DisplayName("filterAll：AND 组合过滤")
    void testFilterAll_andPredicates() {
        List<String> result = Streams.filterAll(Arrays.asList("a1", "b1", "a2"),
                s -> s.startsWith("a"), s -> s.endsWith("1"));
        assertThat(result).containsExactly("a1");
    }

    @Test
    @DisplayName("filterAll：无匹配条件时返回全部")
    void testFilterAll_noMatch_returnsAll() {
        List<String> result = Streams.filterAllList(new String[]{"x", "y"}, alwaysTrue());
        assertThat(result).containsExactly("x", "y");
    }

    @Test
    @DisplayName("filterAllList：数组 AND 过滤")
    void testFilterAllList() {
        List<String> result = Streams.filterAllList(new String[]{"a1", "b2", "a2", "c1"},
                s -> s.startsWith("a"), s -> s.endsWith("1"));
        assertThat(result).containsExactly("a1");
    }

    @Test
    @DisplayName("filterAllSet：数组 AND 过滤返回 Set")
    void testFilterAllSet() {
        Set<String> result = Streams.filterAllSet(new String[]{"a1", "b2", "a2", "c1"},
                s -> s.startsWith("a"), s -> s.endsWith("2"));
        assertThat(result).containsExactly("a2");
    }

    @Test
    @DisplayName("filterAny：OR 组合过滤")
    void testFilterAny_orPredicates() {
        List<String> result = Streams.filterAny(Arrays.asList("a1", "b1", "a2"),
                s -> s.startsWith("a"), s -> s.endsWith("1"));
        assertThat(result).containsExactlyInAnyOrder("a1", "b1", "a2");
    }

    @Test
    @DisplayName("filterAnyList：数组 OR 过滤")
    void testFilterAnyList() {
        List<String> result = Streams.filterAnyList(new String[]{"a1", "b1", "c2"},
                s -> s.startsWith("a"), s -> s.startsWith("b"));
        assertThat(result).containsExactlyInAnyOrder("a1", "b1");
    }

    @Test
    @DisplayName("filterAnySet：数组 OR 过滤返回 Set")
    void testFilterAnySet() {
        Set<String> result = Streams.filterAnySet(new String[]{"a1", "b1", "c2"},
                s -> s.startsWith("a"), s -> s.startsWith("c"));
        assertThat(result).containsExactlyInAnyOrder("a1", "c2");
    }

    @Test
    @DisplayName("filterFirst：返回第一个匹配元素")
    void testFilterFirst_returnsFirstMatch() {
        String result = Streams.filterFirst(Arrays.asList("a1", "b1", "a2"),
                s -> s.startsWith("a"));
        assertThat(result).isEqualTo("a1");
    }

    @Test
    @DisplayName("filterFirst：无匹配时返回 null")
    void testFilterFirst_noMatch_returnsNull() {
        String result = Streams.filterFirst(Arrays.asList("a1", "b1"), alwaysFalse());
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("filterFirst：空集合返回 null")
    void testFilterFirst_emptyCollection_returnsNull() {
        String result = Streams.filterFirst(Collections.emptyList(), alwaysTrue());
        assertThat(result).isNull();
    }
}
