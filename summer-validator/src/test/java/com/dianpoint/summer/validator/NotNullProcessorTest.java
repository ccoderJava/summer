package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.constraintvalidators.NotNullProcessor;
import com.dianpoint.summer.validator.constraintvalidators.PatternProcessor;
import com.dianpoint.summer.validator.processor.AnnotationProcessor;
import com.dianpoint.summer.validator.processor.AnnotationProcessorRegister;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotNullProcessorTest {

    @Test
    public void testSupport_notNullAnnotation_returnsTrue() {
        NotNullProcessor processor = new NotNullProcessor();
        assertThat(processor.support(com.dianpoint.summer.validator.annotations.NotNull.class)).isTrue();
    }

    @Test
    public void testSupport_otherAnnotation_returnsFalse() {
        NotNullProcessor processor = new NotNullProcessor();
        assertThat(processor.support(Override.class)).isFalse();
    }
}
