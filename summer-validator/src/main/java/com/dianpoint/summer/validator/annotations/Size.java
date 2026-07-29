package com.dianpoint.summer.validator.annotations;

@HandlesAnnotation(value = Size.class)
public @interface Size {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "size must be between {min} and {max}";
}
