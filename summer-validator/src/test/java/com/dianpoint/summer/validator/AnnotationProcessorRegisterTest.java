package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.constraintvalidators.NotNullProcessor;
import com.dianpoint.summer.validator.constraintvalidators.PatternProcessor;
import com.dianpoint.summer.validator.processor.AnnotationProcessor;
import com.dianpoint.summer.validator.processor.AnnotationProcessorRegister;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationProcessorRegisterTest {

    @Test
    public void testRegisterAndGet() {
        AnnotationProcessor processor = new NotNullProcessor();
        AnnotationProcessorRegister.register(com.dianpoint.summer.validator.annotations.NotNull.class, processor);
        AnnotationProcessor retrieved = AnnotationProcessorRegister.getProcessor(
                com.dianpoint.summer.validator.annotations.NotNull.class);
        assertThat(retrieved).isSameAs(processor);
    }

    @Test
    public void testGetProcessor_notRegistered_returnsNull() {
        AnnotationProcessor retrieved = AnnotationProcessorRegister.getProcessor(Override.class);
        assertThat(retrieved).isNull();
    }

    @Test
    public void testHasProcessor_registered_returnsTrue() {
        AnnotationProcessor processor = new PatternProcessor();
        AnnotationProcessorRegister.register(com.dianpoint.summer.validator.annotations.Pattern.class, processor);
        assertThat(AnnotationProcessorRegister.hasProcessor(
                com.dianpoint.summer.validator.annotations.Pattern.class)).isTrue();
    }

    @Test
    public void testHasProcessor_notRegistered_returnsFalse() {
        assertThat(AnnotationProcessorRegister.hasProcessor(Deprecated.class)).isFalse();
    }
}
