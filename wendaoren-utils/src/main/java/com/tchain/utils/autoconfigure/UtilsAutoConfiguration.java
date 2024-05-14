package com.tchain.utils.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tchain.utils.common.JsonUtils;
import com.tchain.utils.http.HttpClients;
import com.tchain.utils.prop.HttpClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@AutoConfiguration
@EnableConfigurationProperties(HttpClientProperties.class)
public class UtilsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Scope(value = "singleton")
    public HttpClients httpClients(HttpClientProperties httpClientProperties) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new HttpClients(httpClientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(value = "singleton")
    public JsonUtils jsonUtils(ObjectMapper jacksonObjectMapper) {
        return new JsonUtils(jacksonObjectMapper);
    }
}
