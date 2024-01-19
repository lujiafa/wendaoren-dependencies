package com.wendaoren.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wendaoren.core.exception.table.CommonErrorCodeTable;
import com.wendaoren.web.model.BaseDTO;
import com.wendaoren.web.model.BaseForm;
import com.wendaoren.utils.web.WebUtils;
import com.wendaoren.web.view.SmartErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @date 2016年6月4日
 * @Description 参数解析处理器
 */
public class DefaultHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ObjectMapper objectMapper;

	public DefaultHandlerMethodArgumentResolver(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (BaseForm.class.isAssignableFrom(parameter.getParameterType())
				|| BaseDTO.class.isAssignableFrom(parameter.getParameterType())) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		try {
			Map<String, Object> parameterMap = WebUtils.getRequestAllParameters(request);
			return objectMapper.convertValue(parameterMap, parameter.getParameterType());
		} catch (Exception e) {
			logger.error("参数解析异常|" + e.getMessage(), e);
			throw new ModelAndViewDefiningException(new ModelAndView(
					new SmartErrorView(CommonErrorCodeTable.NOT_SUPPORT_PARAMS_TYPE_CONVERT.toErrorCode(), WebUtils.getResponseMediaType(request))));
		}
	}

}