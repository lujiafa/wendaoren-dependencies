package com.wendaoren.web.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = WebProperties.PROPERTIES_PREFIX)
public class WebProperties {
	
	static final String PROPERTIES_PREFIX = "web";

	/** 是否禁用默认异常处理器。true-禁用 true-不禁用 **/
	private boolean disableDefaultExceptionResolver = true;

	/** 请求相关配置 **/
	private RequestProperties request = new RequestProperties();

	public static class RequestProperties {
		/** 是否开启可重复读取流 **/
		private boolean repeatStream = false;
		private List<String> urlPatterns = Arrays.asList("/*");

		public boolean isRepeatStream() {
			return repeatStream;
		}

		public void setRepeatStream(boolean repeatStream) {
			this.repeatStream = repeatStream;
		}

		public List<String> getUrlPatterns() {
			return urlPatterns;
		}

		public void setUrlPatterns(List<String> urlPatterns) {
			this.urlPatterns = urlPatterns;
		}
	}

	public boolean isDisableDefaultExceptionResolver() {
		return disableDefaultExceptionResolver;
	}

	public void setDisableDefaultExceptionResolver(boolean disableDefaultExceptionResolver) {
		this.disableDefaultExceptionResolver = disableDefaultExceptionResolver;
	}

	public RequestProperties getRequest() {
		return request;
	}

	public void setRequest(RequestProperties request) {
		this.request = request;
	}
}