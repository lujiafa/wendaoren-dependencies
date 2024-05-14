package com.tchain.websecurity.annotation;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@CheckRepeatRequest
public @interface CheckSign {
	
	/**
	 * @Title value
	 * @Description 是否开启签名验证。true-验证 false-不验证
	 */
	public boolean value() default true;
	
}