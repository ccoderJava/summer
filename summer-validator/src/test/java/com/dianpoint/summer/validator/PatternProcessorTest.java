package com.dianpoint.summer.validator;

import com.dianpoint.summer.validator.constraintvalidators.PatternProcessor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PatternProcessorTest {

    @Test
    public void testSupport_patternAnnotation_returnsTrue() {
        PatternProcessor processor = new PatternProcessor();
        assertThat(processor.support(com.dianpoint.summer.validator.annotations.Pattern.class)).isTrue();
    }

    @Test
    public void testSupport_otherAnnotation_returnsFalse() {
        PatternProcessor processor = new PatternProcessor();
        assertThat(processor.support(Override.class)).isFalse();
    }
}
