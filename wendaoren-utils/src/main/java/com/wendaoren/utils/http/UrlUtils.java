package com.wendaoren.utils.http;

import com.wendaoren.utils.constant.CommonConstant;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author lujiafa
 * @email lujiafayx@163.com
 * @date 2019年3月5日
 * @Description: URL拼接工具类
 */
public class UrlUtils {
	
	private static final String URL_SPLIT = "/";

	/**
	 * @Title getFullUrl
	 * @Description 通过url前缀、相对路径和参数组成新url
	 * @param sourcePrefixUrl url前缀
	 * @param relativePath 相对路径
	 * @param pmap 参数
	 * @return String
	 */
	public static String getFullUrl(String sourcePrefixUrl, String relativePath, Map<String, String> pmap) {
		if (!StringUtils.hasLength(sourcePrefixUrl)) {
			throw new IllegalArgumentException("The parameter sourcePrefixUrl cannot be empty.");
		}
		if (relativePath == null) {
			relativePath = CommonConstant.EMPTY;
		}
		String sourceUrl = sourcePrefixUrl;
		if (!StringUtils.hasLength(relativePath)) {
			if (sourcePrefixUrl.endsWith(URL_SPLIT)
					&& relativePath.startsWith(URL_SPLIT)) {
				sourceUrl += relativePath.substring(1);
			} else if (sourcePrefixUrl.endsWith(URL_SPLIT)
					|| relativePath.startsWith(URL_SPLIT)) {
				sourceUrl += relativePath;
			} else {
				sourceUrl +=  URL_SPLIT + relativePath;
			}
		}
		return getFullUrl(sourceUrl, pmap);
	}
	
	/**
	 * @Title getFullUrl
	 * @Description 通过url和参数组成新的url
	 * @param sourceUrl 源url
	 * @param pmap 参数
	 * @return String
	 */
	public static String getFullUrl(String sourceUrl, Map<String, String> pmap) {
		if (!StringUtils.hasLength(sourceUrl)) {
			throw new IllegalArgumentException("The parameter sourceUrl cannot be empty.");
		}
		if (pmap == null || pmap.size() == 0) {
			return sourceUrl;
		}
		String url = sourceUrl;
		if (pmap != null && pmap.size() > 0) {
			int sourceQuerySplitIndex = sourceUrl.indexOf('?');
			boolean existsSeparator = sourceQuerySplitIndex > -1;
			boolean existsQuery = sourceUrl.length() > (sourceQuerySplitIndex + 1);
			
			StringBuilder stringBuilder = new StringBuilder(sourceUrl);
			if (!existsSeparator) {
				stringBuilder.append('?');
			}
			pmap.entrySet().forEach(p -> {
				try {
					if (existsQuery 
							|| (existsSeparator && stringBuilder.length() > sourceUrl.length())
							|| (!existsSeparator && stringBuilder.length() > (sourceUrl.length() + 1))) {
						stringBuilder.append('&');
					}
					stringBuilder
						.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8.name()))
						.append('=');
					if (p.getValue() != null) {
						stringBuilder.append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8.name()));
					}
				} catch (UnsupportedEncodingException e) {}
			});
			url = stringBuilder.toString();
		}
		return url;
	}
	
}