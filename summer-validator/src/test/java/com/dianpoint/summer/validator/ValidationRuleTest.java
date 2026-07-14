package com.dianpoint.summer.validator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationRuleTest {

    @Test
    public void testValidate_success() {
        ValidationRule<String> rule = target -> ValidationResult.success();
        ValidationResult result = rule.validate("test");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void testValidate_failure() {
        ValidationRule<String> rule = target -> ValidationResult.failure("bad input");
        ValidationResult result = rule.validate("test");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("bad input");
    }

    @Test
    public void testAnd_bothSuccess_returnsSuccess() {
        ValidationRule<String> rule1 = target -> ValidationResult.success();
        ValidationRule<String> rule2 = target -> ValidationResult.success();
        ValidationResult result = rule1.and(rule2).validate("test");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void testAnd_firstFails_returnsFirstFailure() {
        ValidationRule<String> rule1 = target -> ValidationResult.failure("first fail", "field1");
        ValidationRule<String> rule2 = target -> ValidationResult.success();
        ValidationResult result = rule1.and(rule2).validate("test");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("first fail");
        assertThat(result.getFieldName()).isEqualTo("field1");
    }

    @Test
    public void testAnd_secondFails_returnsSecondFailure() {
        ValidationRule<String> rule1 = target -> ValidationResult.success();
        ValidationRule<String> rule2 = target -> ValidationResult.failure("second fail", "field2");
        ValidationResult result = rule1.and(rule2).validate("test");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("second fail");
    }

    @Test
    public void testOr_firstSuccess_returnsSuccess() {
        ValidationRule<String> rule1 = target -> ValidationResult.success();
        ValidationRule<String> rule2 = target -> ValidationResult.failure("second fail");
        ValidationResult result = rule1.or(rule2).validate("test");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void testOr_firstFails_secondSuccess_returnsSuccess() {
        ValidationRule<String> rule1 = target -> ValidationResult.failure("first fail");
        ValidationRule<String> rule2 = target -> ValidationResult.success();
        ValidationResult result = rule1.or(rule2).validate("test");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void testOr_bothFail_returnsSecondFailure() {
        ValidationRule<String> rule1 = target -> ValidationResult.failure("first fail");
        ValidationRule<String> rule2 = target -> ValidationResult.failure("second fail");
        ValidationResult result = rule1.or(rule2).validate("test");
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("second fail");
    }
}
