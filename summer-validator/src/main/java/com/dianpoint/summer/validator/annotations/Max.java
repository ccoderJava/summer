package com.dianpoint.summer.validator.annotations;

@HandlesAnnotation(value = Max.class)
public @interface Max {
    long value();
    String message() default "must be less than or equal to {value}";
}
