package com.dianpoint.summer.validator.validator;


import com.dianpoint.summer.validator.ValidationResult;
import com.dianpoint.summer.validator.ValidationRule;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author: congccoder
 * @email: congccoder@gmail.com | <a href="https://github.com/ccoderJava">github-homepage</a>
 * @date: 2025/5/19 21:48
 */

public interface Validator<T> {

    Validator<T> addRule(ValidationRule<T> rule);

    Validator<T> addRule(Predicate<T> condition, String errorMessage);

    Validator<T> addRule(Predicate<T> condition, String errorMessage, String fieldName);

    List<ValidationResult> validate(T target);

    boolean isValid(T target);

    Validator<T> skipOnFirstFailure(boolean skip);

}
