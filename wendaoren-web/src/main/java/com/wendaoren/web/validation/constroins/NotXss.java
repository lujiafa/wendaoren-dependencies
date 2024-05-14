package com.wendaoren.web.validation.constroins;

import com.wendaoren.web.validation.validator.XssConstraintValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@NotNull
@Constraint(validatedBy = { XssConstraintValidator.class })
public @interface NotXss {
	
	static final String DEFAULT_MESSAGE = "内容包含不安全信息";

	String message() default DEFAULT_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
