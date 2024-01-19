package com.wendaoren.springcloud.feign.handler;

import com.wendaoren.springcloud.feign.anotation.AutoFeign;
import com.wendaoren.springcloud.feign.constant.FeignConstant;
import com.wendaoren.utils.web.WebUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.List;

/**
 * @date 2019年7月13日
 * @author jonlu
 */
public class FeignHandlerMethodReturnValueHandler extends RequestResponseBodyMethodProcessor {

    public FeignHandlerMethodReturnValueHandler(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        AutoFeign autoFeign = (AutoFeign) WebUtils.getRequest().getAttribute(FeignConstant.USE_FEIGN_HANDLER);
        return autoFeign != null && autoFeign.value() && autoFeign.responseBody();
    }

    @Override
    protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        super.writeWithMessageConverters(value, returnType, webRequest);
        // WebUtils.getRequest().removeAttribute(FeignConstant.USE_FEIGN_HANDLER);
    }

    @Override
    protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        super.writeWithMessageConverters(value, returnType, inputMessage, outputMessage);
        // WebUtils.getRequest().removeAttribute(FeignConstant.USE_FEIGN_HANDLER);
    }
}
