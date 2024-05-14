package com.tchain.springcloud.loadbalancer.support.hint;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class HintFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String hint = HintContext.get();
        if (hint != null) {
            requestTemplate.header(HintContext.HINT_ATTR_NAME, hint);
        }
    }

}
