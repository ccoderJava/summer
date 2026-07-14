package com.dianpoint.summer.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static com.dianpoint.summer.utils.AssertUtils.assertNotNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AssertUtils 单元测试")
class AssertUtilsTest {

    @Test
    @DisplayName("assertNotNull：非 null 对象不抛异常")
    void testAssertNotNull_nonNull_doesNotThrow() {
        assertNotNull(new Object(), "should not throw");
    }

    @Test
    @DisplayName("assertNotNull(String)：null 对象抛出 IllegalArgumentException")
    void testAssertNotNull_null_throwsException() {
        assertThatThrownBy(() -> assertNotNull(null, "value is null"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value is null");
    }

    @Test
    @DisplayName("assertNotNull(Supplier)：非 null 对象不抛异常")
    void testAssertNotNull_supplier_nonNull_doesNotThrow() {
        assertNotNull("hello", (Supplier<String>) () -> "should not throw");
    }

    @Test
    @DisplayName("assertNotNull(Supplier)：null 对象抛出自定义消息")
    void testAssertNotNull_supplier_null_throwsException() {
        assertThatThrownBy(() -> assertNotNull(null, (Supplier<String>) () -> "custom error"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("custom error");
    }

    @Test
    @DisplayName("assertNotNull(Supplier)：messageSupplier 为 null 时使用 null 消息")
    void testAssertNotNull_nullSupplier_noExceptionOnNullMessage() {
        assertThatThrownBy(() -> assertNotNull(null, (Supplier<String>) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(null);
    }
}
