package com.dianpoint.summer.test.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import com.dianpoint.summer.beans.factory.annotation.Primary;
import com.dianpoint.summer.context.annotation.Lazy;
import com.dianpoint.summer.context.annotation.Scope;
import org.junit.Test;

@Scope("prototype")
@Primary
@Lazy
public class ScopePrimaryLazyTest {

    @Test
    public void testScopeAnnotationDetected() {
        Scope scope = getClass().getAnnotation(Scope.class);
        assertThat(scope).isNotNull();
        assertThat(scope.value()).isEqualTo("prototype");
    }

    @Test
    public void testPrimaryAnnotationDetected() {
        Primary primary = getClass().getAnnotation(Primary.class);
        assertThat(primary).isNotNull();
    }

    @Test
    public void testLazyAnnotationDetected() {
        Lazy lazy = getClass().getAnnotation(Lazy.class);
        assertThat(lazy).isNotNull();
        assertThat(lazy.value()).isTrue();
    }
}
