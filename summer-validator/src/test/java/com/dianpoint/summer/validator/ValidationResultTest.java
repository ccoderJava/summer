package com.dianpoint.summer.validator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationResultTest {

    @Test
    public void testSuccess() {
        ValidationResult result = ValidationResult.success();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isFailure()).isFalse();
        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getFieldName()).isNull();
    }

    @Test
    public void testFailure_withMessageOnly() {
        ValidationResult result = ValidationResult.failure("error occurred");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("error occurred");
        assertThat(result.getFieldName()).isNull();
    }

    @Test
    public void testFailure_withMessageAndField() {
        ValidationResult result = ValidationResult.failure("invalid value", "username");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("invalid value");
        assertThat(result.getFieldName()).isEqualTo("username");
    }
}
