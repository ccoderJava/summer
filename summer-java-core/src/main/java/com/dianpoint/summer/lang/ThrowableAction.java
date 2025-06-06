package com.dianpoint.summer.lang;

/**
 * @author: congccoder
 * @email: congccoder@gmail.com | <a href="https://github.com/ccoderJava">github-homepage</a>
 * @date: 2025/6/5 20:44
 */

@FunctionalInterface
public interface ThrowableAction {

    void execute() throws Throwable;
}
