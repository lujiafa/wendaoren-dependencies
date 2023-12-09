package com.wendaoren.websecurity.autoconfigure;

import com.wendaoren.websecurity.sign.SignatureValidator;
import com.wendaoren.websecurity.sign.validator.HMacMD5SignatureValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class SignatureConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SignatureValidator signatureValidator() {
		return new HMacMD5SignatureValidator();
	}

}