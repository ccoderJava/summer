package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.constraintvalidators.NotNullProcessor;
import com.dianpoint.summer.validator.constraintvalidators.PatternProcessor;
import com.dianpoint.summer.validator.processor.AnnotationProcessor;
import com.dianpoint.summer.validator.processor.AnnotationProcessorRegister;
import com.dianpoint.summer.validator.validator.AnnotationValidatorAdapter;
import com.dianpoint.summer.validator.validator.collection.CollectionValidator;
import com.dianpoint.summer.validator.validator.collection.DefaultCollectionValidator;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidatorsTest {

    @Test
    public void testGeneric_returnsGenericValidator() {
        com.dianpoint.summer.validator.validator.Validator<String> validator = Validators.generic();
        assertThat(validator).isNotNull();
    }

    @Test
    public void testString_returnsStringValidator() {
        com.dianpoint.summer.validator.validator.Validator<String> validator = Validators.string();
        List<ValidationResult> results = validator.validate("");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).isEqualTo("字符串不能为空");
    }

    @Test
    public void testString_validString_success() {
        com.dianpoint.summer.validator.validator.Validator<String> validator = Validators.string();
        assertThat(validator.isValid("hello")).isTrue();
    }

    @Test
    public void testString_nullString_failure() {
        com.dianpoint.summer.validator.validator.Validator<String> validator = Validators.string();
        assertThat(validator.isValid(null)).isFalse();
    }

    @Test
    public void testString_blankString_failure() {
        com.dianpoint.summer.validator.validator.Validator<String> validator = Validators.string();
        assertThat(validator.isValid("   ")).isFalse();
    }

    @Test
    public void testInteger_returnsIntegerValidator() {
        com.dianpoint.summer.validator.validator.Validator<Integer> validator = Validators.integer();
        assertThat(validator).isNotNull();
        assertThat(validator.isValid(42)).isTrue();
        assertThat(validator.isValid(null)).isFalse();
    }

    @Test
    public void testRule_conditionTrue_returnsSuccess() {
        ValidationRule<String> rule = Validators.rule(s -> s != null, "cannot be null", "field");
        ValidationResult result = rule.validate("hello");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void testRule_conditionFalse_returnsFailure() {
        ValidationRule<String> rule = Validators.rule(s -> s != null, "cannot be null", "field");
        ValidationResult result = rule.validate(null);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("cannot be null");
        assertThat(result.getFieldName()).isEqualTo("field");
    }

    @Test
    public void testAnnotated_returnsAnnotationValidator() {
        com.dianpoint.summer.validator.validator.Validator<Object> validator = Validators.annotated(Object.class);
        assertThat(validator).isInstanceOf(AnnotationValidatorAdapter.class);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testCollection_returnsDefaultCollectionValidator() {
        CollectionValidator<String, java.util.List<String>> validator = Validators.collection((Class) java.util.List.class, String.class);
        assertThat(validator).isInstanceOf(DefaultCollectionValidator.class);
    }
}
