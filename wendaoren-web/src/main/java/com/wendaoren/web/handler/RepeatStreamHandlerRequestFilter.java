package com.wendaoren.web.handler;

import com.wendaoren.core.exception.BusinessException;
import com.wendaoren.core.exception.table.CommonErrorCodeTable;
import com.wendaoren.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

/**
 * @date 2019年5月29日
 * @author jonlu
 */
public class RepeatStreamHandlerRequestFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(RepeatStreamHandlerRequestFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ServletRequest targetRequest = request;
		ServletResponse targetResponse = response;
		boolean enableRepeat = false;

		MediaType mediaType = WebUtils.getRequestMediaType(request);
		if (request instanceof HttpServletRequest
				&& !WebUtils.isHttpGet((HttpServletRequest) request)
				&& !MediaType.MULTIPART_FORM_DATA.includes(mediaType)) {
			targetRequest = new RepeatStreamHandlerRequestWrapper((HttpServletRequest) request);
			enableRepeat = true;
		}
		chain.doFilter(targetRequest, targetResponse);
		if (enableRepeat) {
			((RepeatStreamHandlerRequestWrapper) targetRequest).releaseRepreatCache();
		}
	}
	
	static class RepeatStreamHandlerRequestWrapper extends HttpServletRequestWrapper {
		public RepeatStreamHandlerRequestWrapper(HttpServletRequest request) {
			super(request);
		}
		
		@Override
		public String getParameter(String name) {
			return super.getParameter(name);
		}
		
		@Override
		public String[] getParameterValues(String name) {
			return super.getParameterValues(name);
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			return super.getParameterMap();
		}
		
		@Override
		public Enumeration<String> getParameterNames() {
			return super.getParameterNames();
		}
		
		@Override
		public BufferedReader getReader() throws IOException {
			Charset charset = StandardCharsets.UTF_8;
			String characterEncoding = getCharacterEncoding();
			if (StringUtils.hasLength(characterEncoding)) {
				charset = Charset.forName(characterEncoding);
			}
			return new BufferedReader(new InputStreamReader(getInputStream(), charset));
		}

		private ByteArrayOutputStream outputStream = null;
		public void releaseRepreatCache() {
			outputStream = null;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			if (outputStream == null) {
				loadCacheData();
			}
			return new ServletInputStream() {
				
				ByteArrayInputStream bais = new ByteArrayInputStream(outputStream.toByteArray());
				
				@Override
				public int read() throws IOException {
					return bais.read();
				}
				
				@Override
				public void setReadListener(ReadListener listener) {
				}
				
				@Override
				public boolean isReady() {
					return false;
				}
				
				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public void close() throws IOException {
					bais = null;
					super.close();
				}
			};
		}
		
		private synchronized void loadCacheData() {
			if (outputStream != null) {
				return;
			}
			try {
				ServletInputStream is = super.getInputStream();
				outputStream = new ByteArrayOutputStream();
				StreamUtils.copy(is, outputStream);
				is.close();
			} catch (Exception e) {
				logger.error("获取请求数据异常|{}", e.getMessage(), e);
				throw new BusinessException(CommonErrorCodeTable.DATA_LOAD_FAIL.toErrorCode(), e);
			}
		}
	}

}
