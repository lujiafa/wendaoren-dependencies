package com.wendaoren.core.autoconfigure;

import com.wendaoren.core.context.SpringApplicationContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 1)
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringApplicationContext springApplicationContext() {
        return new SpringApplicationContext();
    }
}
