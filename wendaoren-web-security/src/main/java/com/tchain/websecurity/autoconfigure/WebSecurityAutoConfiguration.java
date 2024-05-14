package com.tchain.websecurity.autoconfigure;

import com.tchain.core.autoconfigure.CoreAutoConfiguration;
import com.tchain.websecurity.config.WebSecurityWebMvcConfigurer;
import com.tchain.websecurity.handler.WebSecurityHandlerInterceptor;
import com.tchain.websecurity.permission.PermissionValidator;
import com.tchain.websecurity.prop.SecurityProperties;
import com.tchain.websecurity.session.SessionValidator;
import com.tchain.websecurity.sign.SignatureValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.beans.Introspector;

@AutoConfiguration
@ConditionalOnWebApplication
@AutoConfigureAfter(CoreAutoConfiguration.class)
@EnableConfigurationProperties({SecurityProperties.class})
@Import({SessionConfiguration.class, PermissionConfiguration.class, SignatureConfiguration.class})
public class WebSecurityAutoConfiguration {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(WebSecurityHandlerInterceptor.class)
    public WebSecurityHandlerInterceptor webSecurityHandlerInterceptor(SecurityProperties securityProperties,
                                                                       SessionValidator sessionValidator,
                                                                       PermissionValidator permissionValidator,
                                                                       SignatureValidator signatureValidator,
                                                                       RedisTemplate redisTemplate) {
        return new WebSecurityHandlerInterceptor(securityProperties, sessionValidator, permissionValidator, signatureValidator, redisTemplate);
    }

    @Bean
    public FilterRegistrationBean<WebSecurityHandlerInterceptor> webSecurityHandlerInterceptorRegistrationBean(WebSecurityHandlerInterceptor webSecurityHandlerInterceptor) {
        FilterRegistrationBean<WebSecurityHandlerInterceptor> requestSerialRegistration = new FilterRegistrationBean<WebSecurityHandlerInterceptor>();
        requestSerialRegistration.setFilter(webSecurityHandlerInterceptor);
        requestSerialRegistration.addUrlPatterns("/*");
        requestSerialRegistration.setName(Introspector.decapitalize(WebSecurityHandlerInterceptor.class.getSimpleName()));
        requestSerialRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return requestSerialRegistration;
    }

    @Bean
    public WebMvcConfigurer securityWebMvcConfigurer(WebSecurityHandlerInterceptor handlerInterceptor) {
        WebSecurityWebMvcConfigurer webMvcConfigurer = new WebSecurityWebMvcConfigurer();
        webMvcConfigurer.addInterceptor(handlerInterceptor);
        return webMvcConfigurer;
    }
}
