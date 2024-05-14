package com.tchain.utils.web;

import com.tchain.utils.common.JsonUtils;
import com.tchain.utils.common.XmlUtils;
import com.tchain.utils.constant.SeparatorChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WebUtils extends org.springframework.web.util.WebUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
	
	private final static String ATTREBUTE_REQUEST_MEDIA_TYPE_READED = "_REQUEST_MEDIA_TYPE_READED_";
	private final static String ATTREBUTE_RESPONSE_MEDIA_TYPE_READED = "_RESPONSE_MEDIA_TYPE_READED_";
	private final static String ATTREBUTE_REQUEST_URLENCODE_READED = "_REQUEST_URLENCODE_READED_DATA_";
	private final static String ATTREBUTE_REQUEST_STREAM_READED = "_REQUEST_STREAM_READED_DATA_";
	private final static String IP_UNKNOWN = "unknown";
	private final static String IP_LOCAL = "127.0.0.1";
	
	/**
	 * @Title getRequest
	 * @Description 应用中获取request对象
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
		if (reqAttr != null && reqAttr instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) reqAttr).getRequest();
		}
		throw new RuntimeException("request fetch failed, please check the configuration is correct.");
	}
	
	/**
	 * @Title getResponse
	 * @Description 应用中获取response对象
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
		if (reqAttr != null && reqAttr instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) reqAttr).getResponse();
		}
		throw new RuntimeException("response fetch failed, please check the configuration is correct.");
	}
	
	/**
	 * @Title isHttpPost
	 * @Description 判断请求是否为post请求
	 * @param request
	 * @return boolean true-是 false-否
	 */
	public static boolean isHttpPost(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		return RequestMethod.POST.equals(getRequestMethod(request));
	}
	
	/**
	 * @Title isHttpGet
	 * @Description 判断请求是否为get请求
	 * @param request
	 * @return boolean true-是 false-否
	 */
	public static boolean isHttpGet(HttpServletRequest request) {
		return RequestMethod.GET.equals(getRequestMethod(request));
	}
	
	/**
	 * @Title isHttpMultipart
	 * @Description 是否为Multipart请求
	 * @param request
	 * @return boolean true-是 false-否
	 */
	public static boolean isHttpMultipart(HttpServletRequest request) {
		if (isHttpGet(request)) {
			return false;
		}
		MediaType mediaType = WebUtils.getRequestMediaType(request);
		if (MediaType.MULTIPART_FORM_DATA.includes(mediaType)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Title getRequestMethod
	 * @Description 获取请求方法类型
	 * @param request
	 * @return RequestMethod
	 */
	public static RequestMethod getRequestMethod(HttpServletRequest request) {
		return RequestMethod.valueOf(request.getMethod());
	}
	
	/**
	 * @Title getRequestAllParameters
	 * @Description 获取请求中所有参数集合
	 * @param request
	 */
	public static Map<String, Object> getRequestAllParameters(HttpServletRequest request) throws IOException {
		Map<String, Object> uemap = getRequestUrlParameters(request);
		if (isHttpGet(request)) {
			return uemap;
		}
		Map<String, Object> bmap = getRequestBodyParameters(request);
		if (uemap.size() > 0 || bmap.size() > 0) {
			Map<String, Object> pmap = new HashMap<>(uemap);
			pmap.putAll(bmap);
			return pmap;
		}
		return uemap;
	}
	
	/**
	 * @Title getRequestUrlParameters
	 * @Description 获取请求中参数集合，对应application/x-www-form-urlencoded部分参数
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestUrlParameters(HttpServletRequest request) {
		Map<String, Object> uemap = (Map<String, Object>) request.getAttribute(ATTREBUTE_REQUEST_URLENCODE_READED);
		if (uemap == null) {
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String val = request.getParameter(name);
				if (uemap == null) {
					uemap = new HashMap<String, Object>();
				}
				uemap.put(name, val);
			}
			if (uemap == null) {
				uemap = Collections.EMPTY_MAP;
			}
			request.setAttribute(ATTREBUTE_REQUEST_URLENCODE_READED, uemap);
		}
		return uemap;
	}
	
	/**
	 * @Title getRequestBodyParameters
	 * @Description 获取请求body中参数集合
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestBodyParameters(HttpServletRequest request) throws IOException {
			Map<String, Object> bmap = (Map<String, Object>) request.getAttribute(ATTREBUTE_REQUEST_STREAM_READED);
			if (bmap != null) {
				return bmap;
			}
			MediaType mediaType = WebUtils.getRequestMediaType(request);
			if (isHttpGet(request)
					|| MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType)
					|| MediaType.MULTIPART_FORM_DATA.includes(mediaType)) {
				return Collections.emptyMap();
			}
			Charset charset = StandardCharsets.UTF_8;
			String characterEncoding = request.getCharacterEncoding();
			if (StringUtils.hasLength(characterEncoding)) {
				charset = Charset.forName(characterEncoding);
			}
			boolean json = false;
			boolean xml = false;
			boolean allowCache = true;
			if (json = MediaType.APPLICATION_JSON.includes(mediaType) || MediaType.TEXT_PLAIN.includes(mediaType)) {
				String tempStr = new String(getRequestBodyStream(request), charset).trim();
				bmap = JsonUtils.parseObject(tempStr, LinkedHashMap.class);
				if (tempStr.length() > 10240) {
					allowCache = false;
				}
				tempStr = null;
			} else if (xml = MediaType.APPLICATION_XML.includes(mediaType) || MediaType.TEXT_XML.includes(mediaType)) {
				String tempStr = new String(getRequestBodyStream(request), charset).trim();
				bmap = new HashMap<String,Object>(XmlUtils.parseToMap(tempStr));
				if (tempStr.length() > 10240) {
					allowCache = false;
				}
				tempStr = null;
			}
			if (allowCache) {
				request.setAttribute(ATTREBUTE_REQUEST_STREAM_READED, bmap);
			}
			return bmap;
	}
	
	/**
	 * 获取请求体流数据
	 * @param request
	 * @throws IOException
	 */
	public static byte[] getRequestBodyStream(HttpServletRequest request) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ServletInputStream is = request.getInputStream();
		if (is.markSupported()) {
			is.mark(Integer.MAX_VALUE);
			StreamUtils.copy(is, os);
			is.reset();
		} else {
			StreamUtils.copy(is, os);
		}
		byte[] bs = os.toByteArray();
		is = null;
		os = null;
		return bs;
	}
	
	/**
	 * @Title clearRequestCacheParameters
	 * @Description 清楚请求缓存参数信息
	 * @param request
	 */
	public static void clearRequestCacheParameters(HttpServletRequest request) {
		request.removeAttribute(ATTREBUTE_REQUEST_URLENCODE_READED);
		request.removeAttribute(ATTREBUTE_REQUEST_STREAM_READED);
	}
	
	/**
	 * @Title getSessionId
	 * @Description 获取Session ID
	 * @param request 请求对象
	 * @param name 请求头中session对应Key名
	 * @return Session ID
	 */
	public static String getSessionId(HttpServletRequest request, String name) {
		Assert.hasText(name, "parameter session name is empty");
		String sessionId = request.getHeader(name);
		if (sessionId != null
				&& sessionId.length() > 0) {
			return sessionId;
		}
		Map<String, String> cookieMap = getCookieMap(request);
		return cookieMap.get(name);
	}
	
	/**
	 * @Title getCookieMap
	 * @Description 获取cookie信息
	 * @param request 请求对象
	 * @return cookie信息
	 */
	public static Map<String, String> getCookieMap(HttpServletRequest request) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null
				&& cookies.length > 0) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}
	
	/**
	 * @Title writeCookie
	 * @Description 写出cookie
	 * @param response 响应对象
	 * @param name cookie名称
	 * @param value cookie值
	 */
	public static boolean writeCookie(HttpServletResponse response, String name, String value) {
		return writeCookie(response, name, value, null, null, null);
	}
	
	/**
	 * @Title writeCookie
	 * @Description 写出cookie
	 * @param response 响应对象
	 * @param name cookie名称
	 * @param value cookie值
	 * @param expire 有效期（秒）
	 */
	public static boolean writeCookie(HttpServletResponse response, String name, String value, Integer expire) {
		return writeCookie(response, name, value, null, null, expire);
	}
	
	/**
	 * @Title writeCookie
	 * @Description 写出cookie
	 * @param response 响应对象
	 * @param name cookie名称
	 * @param value cookie值
	 * @param path 路径
	 * @param domain 域名
	 */
	public static boolean writeCookie(HttpServletResponse response, String name, String value, String path, String domain) {
		return writeCookie(response, name, value, path, domain, null);
	}
	
	/**
	 * @Title writeCookie
	 * @Description 写出cookie
	 * @param response 响应对象
	 * @param name cookie名称
	 * @param value cookie值
	 * @param path 路径
	 * @param domain 域名
	 * @param expire 有效期（秒）
	 */
	public static boolean writeCookie(HttpServletResponse response, String name, String value, String path, String domain, Integer expire) {
		Assert.notNull(response, "object response cannot be null");
		Assert.hasText(name, "cookie name can not be empty");
		Cookie cookie = new Cookie(name, value);
		if (path == null) {
			cookie.setPath("/");
		} else {
			cookie.setPath(path);
		}
		if (StringUtils.hasLength(domain)) {
			cookie.setDomain(domain);
		}
		if (expire == null) {
			cookie.setMaxAge(-1);
		} else {
			cookie.setMaxAge(expire);
		}
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return true;
	}
	
	/**
	 * @Title removeCookie
	 * @Description 移除请求头中cookie
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param name cookie名
	 */
	public static boolean removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Assert.notNull(response, "object response cannot be null");
		Assert.hasText(name, "cookie name can not be empty");
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie != null
						&& name.equals(cookie.getName())) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * @Title getRequestMediaType
	 * @Description 获取客户端请求MIME类型
	 * @param request
	 * @return MediaType
	 */
	public static MediaType getRequestMediaType(ServletRequest request) {
		MediaType mediaType = (MediaType) request.getAttribute(ATTREBUTE_REQUEST_MEDIA_TYPE_READED);
		if (mediaType == null) {
			mediaType = getRequestMediaTypes(request).get(0);
			request.setAttribute(ATTREBUTE_REQUEST_MEDIA_TYPE_READED, mediaType);
		}
		return mediaType;
	}
	
	/**
	 * @Title getRequestMediaTypes
	 * @Description 获取客户端请求MIME类型
	 * @param request
	 * @return MediaType
	 */
	public static List<MediaType> getRequestMediaTypes(ServletRequest request) {
		String contentType = request.getContentType();
		if (StringUtils.hasLength(contentType)) {
			try {
				List<MediaType> mediaTypes = MediaType.parseMediaTypes(contentType);
				if (mediaTypes != null && mediaTypes.size() > 0) {
					return mediaTypes;
				}
			} catch (Exception e) {
				logger.debug("获取请求头accept参数失败|{}", e.getMessage()) ;
			}
		}
		return Arrays.asList(MediaType.ALL);
	}

	/**
	 * @Title getResponseMediaType
	 * @Description 获取客户端需要的响应Media类型
	 * @param request
	 * @return MediaType
	 */
	public static MediaType getResponseMediaType(HttpServletRequest request) {
		MediaType mediaType = (MediaType) request.getAttribute(ATTREBUTE_RESPONSE_MEDIA_TYPE_READED);
		if (mediaType == null) {
			mediaType = getResponseMediaTypes(request).get(0);
			request.setAttribute(ATTREBUTE_RESPONSE_MEDIA_TYPE_READED, mediaType);
		}
		return mediaType;
	}
	
	/**
	 * @Title getResponseMediaTypes
	 * @Description 获取客户端需要的响应Media类型
	 * @param request
	 * @return MediaType
	 */
	public static List<MediaType> getResponseMediaTypes(HttpServletRequest request) {
		String headerAccept = request.getHeader(HttpHeaders.ACCEPT);
		if (StringUtils.hasLength(headerAccept)) {
			try {
				List<MediaType> mediaTypes = MediaType.parseMediaTypes(headerAccept);
				if (mediaTypes != null && mediaTypes.size() > 0) {
					MediaType.sortBySpecificityAndQuality(mediaTypes);
					return mediaTypes;
				}
			} catch (Exception e) {
				logger.debug("获取请求头accept参数失败|{}", e.getMessage()) ;
			}
		}
		return Arrays.asList(MediaType.ALL);
	}
	
	/**
	 * @Title getRequestIp
	 * @Description 获取请求IP
	 * @param request
	 * @return String
	 */
	public static String getRequestIp(HttpServletRequest request) {
		String requestIp = getFirstIp(request.getHeader("x-forwarded-for"));
		if (requestIp == null || IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			requestIp = getFirstIp(request.getHeader("Proxy-Client-IP"));
		}
		if (requestIp == null || IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			requestIp = getFirstIp(request.getHeader("WL-Proxy-Client-IP"));
		}
		if (requestIp == null || IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			requestIp = getFirstIp(request.getHeader("HTTP_CLIENT_IP"));
		}
		if (requestIp == null || IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			requestIp = getFirstIp(request.getHeader("HTTP_X_FORWARDED_FOR"));
		}
		if (requestIp == null || IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			requestIp = getFirstIp(request.getHeader("X-Real-IP"));
		}
		if (requestIp == null || IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			requestIp = getFirstIp(request.getRemoteAddr());
			if (requestIp != null
					&& (IP_LOCAL.equals(requestIp)
							|| "0:0:0:0:0:0:0:1".equals(requestIp))) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					String hostAddress = inetAddress.getHostAddress();
					if (!StringUtils.hasLength(hostAddress)) {
						requestIp = IP_LOCAL;
					} else {
						requestIp = getFirstIp(hostAddress);
					}
				} catch (UnknownHostException e) {
					requestIp = IP_LOCAL;
				}
			}
		}
		if (IP_UNKNOWN.equalsIgnoreCase(requestIp)) {
			return IP_LOCAL;
		}
		if (requestIp != null) {
			return requestIp.trim();
		}
		throw new RuntimeException("获取请求IP信息失败");
	}
	
	/**
	 * @Title getRequestIpChain
	 * @Description 获取请求IP链
	 * @param request
	 * @return String
	 */
	public static String getRequestIpChain(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(request.getHeader("x-forwarded-for")).append(SeparatorChar.VERTICAL_BAR)
		.append(request.getHeader("Proxy-Client-IP")).append(SeparatorChar.VERTICAL_BAR)
		.append(request.getHeader("WL-Proxy-Client-IP")).append(SeparatorChar.VERTICAL_BAR)
		.append(request.getHeader("HTTP_CLIENT_IP")).append(SeparatorChar.VERTICAL_BAR)
		.append(request.getHeader("HTTP_X_FORWARDED_FOR")).append(SeparatorChar.VERTICAL_BAR)
		.append(request.getHeader("X-Real-IP")).append(SeparatorChar.VERTICAL_BAR)
		.append(request.getRemoteAddr());
		return builder.toString();
	}
	

	/**
	 * @Title getFirstIp
	 * @Description 工具方法，用于获取ip英文逗号分割数组中第一个ip值
	 * @param ipArrayStr
	 * @return String
	 */
	private static String getFirstIp(String ipArrayStr) {
		if (ipArrayStr == null
				|| ipArrayStr.length() == 0) {
			return "";
		}
		String[] ipArray = ipArrayStr.split(SeparatorChar.COMMA);
		for (String ip : ipArray) {
			if (!StringUtils.hasLength(ip)
					|| "unknown".equalsIgnoreCase(ip)) {
				continue;
			}
			return ip;
		}
		return "";
	}
	
}