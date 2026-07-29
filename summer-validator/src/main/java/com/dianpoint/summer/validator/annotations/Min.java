package com.dianpoint.summer.validator.annotations;

@HandlesAnnotation(value = Min.class)
public @interface Min {
    long value();
    String message() default "must be greater than or equal to {value}";
}
