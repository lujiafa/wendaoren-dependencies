package com.tchain.websecurity.autoconfigure;

import com.tchain.websecurity.permission.PermissionValidator;
import com.tchain.websecurity.permission.PermissionValidatorHandler;
import com.tchain.websecurity.permission.simple.SimplePermissionValidator;
import com.tchain.websecurity.prop.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;


@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "checkSession", havingValue = "true", matchIfMissing = true)
public class PermissionConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "checkPermission", havingValue = "true", matchIfMissing = true)
	public PermissionValidator permissionValidator() {
		return new SimplePermissionValidator();
	}

	@Bean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "checkPermission", havingValue = "true", matchIfMissing = true)
	public PermissionValidatorHandler permissionValidatorHandler(PermissionValidator permissionValidator) {
		return new PermissionValidatorHandler(permissionValidator);
	}
	
}