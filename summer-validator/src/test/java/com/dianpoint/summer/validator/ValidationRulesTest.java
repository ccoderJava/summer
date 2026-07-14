package com.dianpoint.summer.validator;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationRulesTest {

    @Test
    public void testMinLength_success() {
        ValidationRule<String> rule = ValidationRules.minLength(3);
        assertThat(rule.validate("abc").isSuccess()).isTrue();
        assertThat(rule.validate("abcd").isSuccess()).isTrue();
    }

    @Test
    public void testMinLength_failure() {
        ValidationRule<String> rule = ValidationRules.minLength(3);
        ValidationResult result = rule.validate("ab");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("长度不能小于3");
    }

    @Test
    public void testMaxLength_success() {
        ValidationRule<String> rule = ValidationRules.maxLength(5);
        assertThat(rule.validate("abc").isSuccess()).isTrue();
        assertThat(rule.validate("abcde").isSuccess()).isTrue();
    }

    @Test
    public void testMaxLength_failure() {
        ValidationRule<String> rule = ValidationRules.maxLength(3);
        ValidationResult result = rule.validate("abcd");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("长度不能大于3");
    }

    @Test
    public void testMatches_success() {
        ValidationRule<String> rule = ValidationRules.matches("\\d+");
        assertThat(rule.validate("12345").isSuccess()).isTrue();
    }

    @Test
    public void testMatches_failure() {
        ValidationRule<String> rule = ValidationRules.matches("\\d+");
        ValidationResult result = rule.validate("abc");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("格式不符合要求");
    }

    @Test
    public void testMin_success() {
        ValidationRule<Integer> rule = ValidationRules.min(10);
        assertThat(rule.validate(10).isSuccess()).isTrue();
        assertThat(rule.validate(20).isSuccess()).isTrue();
    }

    @Test
    public void testMin_failure() {
        ValidationRule<Integer> rule = ValidationRules.min(10);
        ValidationResult result = rule.validate(5);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("不能小于10");
    }

    @Test
    public void testMax_success() {
        ValidationRule<Integer> rule = ValidationRules.max(100);
        assertThat(rule.validate(50).isSuccess()).isTrue();
        assertThat(rule.validate(100).isSuccess()).isTrue();
    }

    @Test
    public void testMax_failure() {
        ValidationRule<Integer> rule = ValidationRules.max(100);
        ValidationResult result = rule.validate(200);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("不能大于100");
    }

    @Test
    public void testMinsize_success() {
        ValidationRule<Collection<?>> rule = ValidationRules.minsize(2);
        assertThat(rule.validate(Arrays.asList("a", "b")).isSuccess()).isTrue();
    }

    @Test
    public void testMinsize_failure() {
        ValidationRule<Collection<?>> rule = ValidationRules.minsize(3);
        ValidationResult result = rule.validate(Collections.singletonList("a"));
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("集合大小不能小于3");
    }

    @Test
    public void testMaxSize_success() {
        ValidationRule<Collection<?>> rule = ValidationRules.maxSize(3);
        assertThat(rule.validate(Arrays.asList("a", "b")).isSuccess()).isTrue();
    }

    @Test
    public void testMaxSize_failure() {
        ValidationRule<Collection<?>> rule = ValidationRules.maxSize(1);
        ValidationResult result = rule.validate(Arrays.asList("a", "b"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void testRange_inRange_success() {
        ValidationRule<Collection<?>> rule = ValidationRules.range(1, 3);
        assertThat(rule.validate(Arrays.asList("a", "b")).isSuccess()).isTrue();
    }

    @Test
    public void testRange_outOfRange_failure() {
        ValidationRule<Collection<?>> rule = ValidationRules.range(2, 3);
        ValidationResult result = rule.validate(Collections.singletonList("a"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void testUniqueElements_noDuplicates_success() {
        ValidationRule<Collection<String>> rule = ValidationRules.uniqueElements();
        assertThat(rule.validate(Arrays.asList("a", "b", "c")).isSuccess()).isTrue();
    }

    @Test
    public void testUniqueElements_hasDuplicates_failure() {
        ValidationRule<Collection<String>> rule = ValidationRules.uniqueElements();
        ValidationResult result = rule.validate(Arrays.asList("a", "b", "a"));
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).contains("重复元素");
    }

    @Test
    public void testUniqueElements_null_success() {
        ValidationRule<Collection<String>> rule = ValidationRules.uniqueElements();
        assertThat(rule.validate(null).isSuccess()).isTrue();
    }
}
