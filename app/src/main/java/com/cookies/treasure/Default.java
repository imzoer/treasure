package com.cookies.treasure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by masonqwli on 15/11/22.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    String stringValue() default "";

    int intValue() default 0;

    long longValue() default 0;

    boolean boolValue() default false;

    float floatValue() default 0f;
}
