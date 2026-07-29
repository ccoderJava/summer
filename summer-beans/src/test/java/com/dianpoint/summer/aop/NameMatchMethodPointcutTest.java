package com.dianpoint.summer.aop;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

public class NameMatchMethodPointcutTest {

    public static class TestService {
        public void doAction() {}
        public String getName() { return "test"; }
        public void processOrder() {}
        public void processPayment() {}
        public void doSomething() {}
        protected void protectedMethod() {}
    }

    @Test
    public void testExactMatch() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("doAction");
        Method method = TestService.class.getMethod("doAction");
        assertThat(pointcut.matches(method, TestService.class)).isTrue();
    }

    @Test
    public void testExactMismatch() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("doAction");
        Method method = TestService.class.getMethod("getName");
        assertThat(pointcut.matches(method, TestService.class)).isFalse();
    }

    @Test
    public void testWildcardPrefix() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("process*");
        assertThat(pointcut.matches(TestService.class.getMethod("processOrder"), TestService.class)).isTrue();
        assertThat(pointcut.matches(TestService.class.getMethod("processPayment"), TestService.class)).isTrue();
        assertThat(pointcut.matches(TestService.class.getMethod("doAction"), TestService.class)).isFalse();
    }

    @Test
    public void testWildcardSuffix() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("*Action");
        assertThat(pointcut.matches(TestService.class.getMethod("doAction"), TestService.class)).isTrue();
        assertThat(pointcut.matches(TestService.class.getMethod("getName"), TestService.class)).isFalse();
    }

    @Test
    public void testWildcardBothEnds() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("do*ing");
        assertThat(pointcut.matches(TestService.class.getMethod("doSomething"), TestService.class)).isTrue();
        assertThat(pointcut.matches(TestService.class.getMethod("doAction"), TestService.class)).isFalse();
    }

    @Test
    public void testSingleAsterisk() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut("*");
        assertThat(pointcut.matches(TestService.class.getMethod("doAction"), TestService.class)).isTrue();
        assertThat(pointcut.matches(TestService.class.getMethod("getName"), TestService.class)).isTrue();
    }

    @Test
    public void testNullMappedNameMatchesAll() throws Exception {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        assertThat(pointcut.matches(TestService.class.getMethod("doAction"), TestService.class)).isTrue();
        assertThat(pointcut.matches(TestService.class.getMethod("getName"), TestService.class)).isTrue();
    }
}
