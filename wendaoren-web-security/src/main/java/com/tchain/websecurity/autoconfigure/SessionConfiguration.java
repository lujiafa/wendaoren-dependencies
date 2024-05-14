package com.tchain.websecurity.autoconfigure;

import com.tchain.websecurity.prop.SecurityProperties;
import com.tchain.websecurity.session.SessionContext;
import com.tchain.websecurity.session.SessionValidator;
import com.tchain.websecurity.session.validator.SimpleSessionValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;

public class SessionConfiguration {

	@Bean
	@Scope("singleton")
	public SessionContext sessionContext(RedisTemplate redisTemplate, SecurityProperties securityProperties) {
		return new SessionContext(redisTemplate, securityProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	public SessionValidator sessionValidator() {
		return new SimpleSessionValidator();
	}

}