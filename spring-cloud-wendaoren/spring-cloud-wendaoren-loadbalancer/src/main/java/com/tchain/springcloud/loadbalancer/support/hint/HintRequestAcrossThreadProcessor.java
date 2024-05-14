package com.tchain.springcloud.loadbalancer.support.hint;

import com.tchain.core.concurrent.AcrossThreadProcessor;

public class HintRequestAcrossThreadProcessor implements AcrossThreadProcessor<String> {

    @Override
    public String parentGet() {
        return HintContext.get();
    }

    @Override
    public void childExecuteBefore(Thread parentThread, String value) {
        HintContext.set(value);
    }

    @Override
    public void childExecuteAfter(Thread parentThread, String value) {
        HintContext.remove();
    }


}
