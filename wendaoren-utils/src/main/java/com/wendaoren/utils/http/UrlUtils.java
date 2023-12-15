package com.wendaoren.utils.http;

import com.wendaoren.utils.constant.SeparatorChar;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lujiafa
 * @email lujiafayx@163.com
 * @date 2019年3月5日
 * @Description: URL拼接工具类
 */
public class UrlUtils {
	
	/**
	 * @Description 通过url前缀、相对路径和参数集合组成新url
	 * @param basePrefixUrl url前缀
	 * @param subPath 子路径、相对路径【可缺省】
	 * @param pmap 参数集合
	 * @return String
	 */
	public static String concat(String basePrefixUrl, String subPath, Map<String, String> pmap) {
		if (!StringUtils.hasText(basePrefixUrl)) {
			throw new IllegalArgumentException("The parameter sourcePrefixUrl cannot be null.");
		}
		String sourceUrl = basePrefixUrl.trim();
		if (StringUtils.hasText(subPath)) {
			sourceUrl = (sourceUrl + SeparatorChar.SLASH + subPath.trim()).replaceAll("/+", SeparatorChar.SLASH);
		}
		return concat(sourceUrl, pmap);
	}
	
	/**
	 * @Description 通过url和参数集合组成新的url
	 * @param sourceUrl 源url
	 * @param pmap 参数
	 * @return String
	 */
	public static String concat(String sourceUrl, Map<String, String> pmap) {
		if (!StringUtils.hasText(sourceUrl)) {
			throw new IllegalArgumentException("The parameter sourceUrl cannot be empty.");
		}
		if (pmap == null || pmap.size() == 0) {
			return sourceUrl;
		}
		String url = sourceUrl;
		if (pmap != null && pmap.size() > 0) {
			int sourceQuerySplitIndex = sourceUrl.indexOf('?');
			AtomicBoolean existsSeparator = new AtomicBoolean(sourceQuerySplitIndex > -1);
			AtomicBoolean existsQuery = new AtomicBoolean(existsSeparator.get() && sourceUrl.length() > (sourceQuerySplitIndex + 1));
			
			StringBuilder stringBuilder = new StringBuilder(sourceUrl);
			if (existsSeparator.compareAndSet(false, true)) {
				stringBuilder.append('?');
			}
			pmap.entrySet().forEach(p -> {
				try {
					if (existsQuery.get()) {
						stringBuilder.append('&');
					}
					stringBuilder.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8.name()))
						.append('=');
					if (p.getValue() != null) {
						stringBuilder.append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8.name()));
					}
					existsQuery.set(true);
				} catch (UnsupportedEncodingException e) {}
			});
			url = stringBuilder.toString();
		}
		return url;
	}
	
}