package com.tchain.websecurity.sign;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * SignKey获取器
 */
@FunctionalInterface
public interface SignKeyGetter {

    /**
     * 获取签名密钥
     * @return 密钥
     */
    String getSignKey(HttpServletRequest request, Method method);

}
