package com.tchain.springcloud.loadbalancer.support.hint;

public final class HintContext {

    public static final String HINT_ATTR_NAME = "X-SC-LB-Hint";
    static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<String>();

    public static String get() {
        return THREAD_LOCAL.get();
    }

    public static void set(String value) {
        THREAD_LOCAL.set(value);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
