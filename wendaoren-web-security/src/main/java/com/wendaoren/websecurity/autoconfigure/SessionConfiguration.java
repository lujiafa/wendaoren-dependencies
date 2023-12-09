package com.wendaoren.websecurity.autoconfigure;

import com.wendaoren.websecurity.prop.SecurityProperties;
import com.wendaoren.websecurity.session.SessionContext;
import com.wendaoren.websecurity.session.SessionValidator;
import com.wendaoren.websecurity.session.validator.SimpleSessionValidator;
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