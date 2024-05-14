package com.tchain.websecurity.annotation;


import com.tchain.websecurity.permission.Logic;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
	
	public String[] value();
	
	public Logic logic() default Logic.OR;
	
}