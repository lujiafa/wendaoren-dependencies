package com.wendaoren.core.exception.provier;

import com.wendaoren.core.exception.ErrorI18nProvider;

public class CoreErrorI18nProvider implements ErrorI18nProvider {
    @Override
    public String getBasename() {
        return "classpath:/i18n/core/error";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
