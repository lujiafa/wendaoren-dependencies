package com.wendaoren.web.handler;

import com.wendaoren.web.model.response.EmbedResponseData;
import com.wendaoren.web.model.response.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author jon
 * @date 2021年7月13日
 * 注：使用 ResponseBodyAdvice<T> 可以方便地实现全局性的响应处理，例如在响应数据加密、压缩、格式化等方面进行定制。你可以通过实现这个接口并注册为 Spring Bean，
 * 或者使用 @ControllerAdvice 注解结合 @RestControllerAdvice 或 @ResponseBody 注解进行全局配置。
 */
@ControllerAdvice
public class ResponseDataResponseBodyTransferAdvice implements ResponseBodyAdvice<Object> {
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		if (ResponseData.class.isAssignableFrom(returnType.getParameterType())
				|| EmbedResponseData.class.isAssignableFrom(returnType.getParameterType())) {
			return true;
		}
		return false;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		// 暂未实现
		return body;
	}

}
