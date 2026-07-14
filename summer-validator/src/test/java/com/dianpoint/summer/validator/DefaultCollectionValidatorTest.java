package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.validator.collection.CollectionValidator;
import com.dianpoint.summer.validator.validator.collection.DefaultCollectionValidator;
import com.dianpoint.summer.validator.validator.Validator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCollectionValidatorTest {

    @Test
    public void testValidate_nullCollection_defaultBehavior_returnsFailure() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        List<ValidationResult> results = validator.validate(null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("对象不能为null");
    }

    @Test
    public void testValidate_emptyCollection_noElementRules_returnsEmpty() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        List<ValidationResult> results = validator.validate(Collections.emptyList());
        assertThat(results).isEmpty();
    }

    @Test
    public void testValidate_nonEmptyCollection_noElementRules_returnsEmpty() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        List<ValidationResult> results = validator.validate(Arrays.asList("a", "b"));
        assertThat(results).isEmpty();
    }

    @Test
    public void testValidate_withElementRule_nullElement_failsWithDefaultNullMessage() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        validator.elementRule(s -> s != null, "element is null");
        List<ValidationResult> results = validator.validate(Arrays.asList("a", null, "c"));
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).contains("对象不能为null");
    }

    @Test
    public void testValidate_withElementRule_nonNullElementsPass_returnsEmpty() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        validator.elementRule(s -> !s.isEmpty(), "element is empty");
        List<ValidationResult> results = validator.validate(Arrays.asList("a", "b"));
        assertThat(results).isEmpty();
    }

    @Test
    public void testValidate_withCollectionRule_andElementRule_combinedFailures() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        validator.addRule(ValidationRules.minsize(3));
        validator.elementRule(s -> !s.isEmpty(), "element is empty");
        List<ValidationResult> results = validator.validate(Arrays.asList("a", ""));
        assertThat(results).hasSize(2);
    }

    @Test
    public void testValidate_withElementValidator_customValidator() {
        Validator<String> elementValidator = Validators.<String>generic()
                .addRule(s -> s != null && s.length() > 1, "string too short");

        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        validator.elementValidator(elementValidator);

        List<ValidationResult> results = validator.validate(Arrays.asList("a", "ab", "c"));
        assertThat(results).hasSize(2);
    }

    @Test
    public void testValidate_nullCollection_elementValidatorNotCalled() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        validator.elementRule(s -> s != null, "element is null");
        List<ValidationResult> results = validator.validate(null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("对象不能为null");
    }

    @Test
    public void testElementRule_withPredicate() {
        CollectionValidator<Integer, List<Integer>> validator = new DefaultCollectionValidator<>(List.class, Integer.class);
        validator.elementRule(n -> n > 0, "must be positive");
        List<ValidationResult> results = validator.validate(Arrays.asList(1, -1, 3));
        assertThat(results).hasSize(1);
    }

    @Test
    public void testChaining_addRule_returnsCollectionValidator() {
        CollectionValidator<String, List<String>> validator = new DefaultCollectionValidator<>(List.class, String.class);
        CollectionValidator<String, List<String>> result = validator.addRule(ValidationRules.minsize(1));
        assertThat(result).isSameAs(validator);
    }
}
