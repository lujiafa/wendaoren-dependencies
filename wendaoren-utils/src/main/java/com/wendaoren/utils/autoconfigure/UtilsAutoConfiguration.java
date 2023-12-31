package com.wendaoren.utils.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wendaoren.utils.common.JsonUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@AutoConfiguration
public class UtilsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Scope(value = "singleton")
    public JsonUtils jsonUtils(ObjectMapper jacksonObjectMapper) {
        return new JsonUtils(jacksonObjectMapper);
    }
}
