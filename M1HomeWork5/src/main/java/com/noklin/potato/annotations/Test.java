package com.noklin.potato.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.noklin.potato.exceptions.DefaultExpectedException;


@Target(value = ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Test {
	String description() default "";
	Class<? extends Throwable> expected() default DefaultExpectedException.class;
}