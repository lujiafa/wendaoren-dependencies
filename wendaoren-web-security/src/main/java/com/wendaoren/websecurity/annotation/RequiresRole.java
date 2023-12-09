package com.wendaoren.websecurity.annotation;


import com.wendaoren.websecurity.permission.Logic;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
	
	public String[] value();
	
	public Logic logic() default Logic.OR;
	
}