package com.tchain.web.config;

import com.tchain.web.validation.util.ValidatorUtils;
import org.springframework.context.annotation.Bean;

import javax.validation.Validator;

/**
 * @date 2019年5月30日
 * @author jonlu
 */
public class ValidatorConfiguration {

	@Bean
	public Validator validator() {
		return ValidatorUtils.buildValidator();
	}



}