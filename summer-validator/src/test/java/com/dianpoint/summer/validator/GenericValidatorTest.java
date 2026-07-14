package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.validator.GenericValidator;
import com.dianpoint.summer.validator.validator.Validator;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericValidatorTest {

    @Test
    public void testCreate_returnsNewInstance() {
        GenericValidator<String> validator = GenericValidator.create();
        assertThat(validator).isNotNull();
    }

    @Test
    public void testValidate_nullTarget_defaultBehavior_returnsFailure() {
        Validator<String> validator = GenericValidator.create();
        List<ValidationResult> results = validator.validate(null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("对象不能为null");
    }

    @Test
    public void testValidate_nullTarget_allowNull_returnsEmpty() {
        GenericValidator<String> gv = GenericValidator.create();
        gv.allowNull(true);
        List<ValidationResult> results = gv.validate(null);
        assertThat(results).isEmpty();
    }

    @Test
    public void testValidate_nullTarget_customMessage() {
        GenericValidator<String> gv = GenericValidator.create();
        gv.nullErrorMessage("custom null error");
        List<ValidationResult> results = gv.validate(null);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("custom null error");
    }

    @Test
    public void testValidate_allRulesPass_returnsEmpty() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> s.length() > 2, "too short");
        List<ValidationResult> results = validator.validate("hello");
        assertThat(results).isEmpty();
    }

    @Test
    public void testValidate_ruleFails_returnsFailure() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> s.length() > 10, "too short", "name");
        List<ValidationResult> results = validator.validate("hello");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("too short");
        assertThat(results.get(0).getFieldName()).isEqualTo("name");
    }

    @Test
    public void testValidate_multipleRulesFail_returnsAllFailures() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> s.length() > 5, "min length", "field1")
                .addRule(s -> s.length() < 3, "max length", "field2");
        List<ValidationResult> results = validator.validate("abcd");
        assertThat(results).hasSize(2);
    }

    @Test
    public void testValidate_skipOnFirstFailure_stopsAtFirst() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> false, "rule1 fail")
                .addRule(s -> false, "rule2 fail");
        validator.skipOnFirstFailure(true);
        List<ValidationResult> results = validator.validate("test");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("rule1 fail");
    }

    @Test
    public void testIsValid_allRulesPass_returnsTrue() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> s.length() > 2, "too short");
        assertThat(validator.isValid("hello")).isTrue();
    }

    @Test
    public void testIsValid_ruleFails_returnsFalse() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> s.length() > 10, "too short");
        assertThat(validator.isValid("hello")).isFalse();
    }

    @Test
    public void testIsValid_nullTarget_returnsFalse() {
        Validator<String> validator = GenericValidator.create();
        assertThat(validator.isValid(null)).isFalse();
    }

    @Test
    public void testAddRule_withPredicateAndMessage() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> !s.isEmpty(), "empty not allowed");
        List<ValidationResult> results = validator.validate("");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("empty not allowed");
    }

    @Test
    public void testAddRule_withPredicateMessageAndField() {
        Validator<String> validator = GenericValidator.<String>create()
                .addRule(s -> !s.isEmpty(), "empty not allowed", "username");
        List<ValidationResult> results = validator.validate("");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFieldName()).isEqualTo("username");
    }

    @Test
    public void testFieldName_setAndUsed() {
        GenericValidator<String> gv = GenericValidator.<String>create();
        gv.fieldName("testField");
        gv.addRule(s -> s != null, "should not be null");
        List<ValidationResult> results = gv.validate(null);
        assertThat(results.get(0).getFieldName()).isEqualTo("testField");
    }
}
