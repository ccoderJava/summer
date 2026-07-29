package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.validator.AnnotationValidatorAdapter;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnnotationValidatorAdapterTest {

    @Test
    public void testCreate_returnsAdapter() {
        AnnotationValidatorAdapter<Object> adapter = AnnotationValidatorAdapter.create(Object.class);
        assertThat(adapter).isNotNull();
    }

    @Test
    public void testAddRule_throwsUnsupportedOperationException() {
        AnnotationValidatorAdapter<Object> adapter = AnnotationValidatorAdapter.create(Object.class);
        assertThatThrownBy(() -> adapter.addRule(target -> ValidationResult.success()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testValidate_nullTarget_returnsFailure() {
        AnnotationValidatorAdapter<Object> adapter = AnnotationValidatorAdapter.create(Object.class);
        List<ValidationResult> results = adapter.validate(null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getErrorMessage()).contains("校验对象不能为null");
    }

    @Test
    public void testIsValid_nullTarget_returnsFalse() {
        AnnotationValidatorAdapter<Object> adapter = AnnotationValidatorAdapter.create(Object.class);
        assertThat(adapter.isValid(null)).isFalse();
    }

    @Test
    public void testSkipOnFirstFailure_setsReturnSelf() {
        AnnotationValidatorAdapter<Object> adapter = AnnotationValidatorAdapter.create(Object.class);
        assertThat(adapter.skipOnFirstFailure(true)).isSameAs(adapter);
    }
}
