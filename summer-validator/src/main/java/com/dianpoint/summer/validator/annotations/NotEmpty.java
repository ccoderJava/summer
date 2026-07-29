package com.dianpoint.summer.validator.annotations;

@HandlesAnnotation(value = NotEmpty.class)
public @interface NotEmpty {
    String message() default "must not be empty";
}
