package com.wendaoren.web.view;

import com.wendaoren.utils.common.IntrospectorUtils;
import com.wendaoren.utils.common.JsonUtils;
import com.wendaoren.utils.common.XmlUtils;
import com.wendaoren.web.model.response.EmbedResponseData;
import com.wendaoren.web.model.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SmartView implements View {
	
	protected static Logger logger = LoggerFactory.getLogger(SmartView.class);
	
	protected final static MediaType EXTENSION_SUPPORT_XML_MEDIA_TYPE = MediaType.parseMediaType("application/*+xml");
	protected final static MediaType EXTENSION_SUPPORT_JSON_MEDIA_TYPE = MediaType.parseMediaType("application/*+json");
	
	protected Object data;
	protected MediaType mediaType = MediaType.APPLICATION_JSON;
	protected Charset charset = StandardCharsets.UTF_8;

	public SmartView(Object data, MediaType mediaType) {
		this.data = data;
		if (mediaType != null) {
			this.mediaType = mediaType;
		}
	}
	
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding(charset.name());
		ServletOutputStream out = response.getOutputStream();
		try {
			response.setContentType(mediaType.toString());
			out.write(getContent(request).getBytes(charset.name()));
			out.flush();
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}
	
	@Override
	public String getContentType() {
		if (MediaType.APPLICATION_JSON.includes(mediaType)
				|| MediaType.TEXT_PLAIN.includes(mediaType)
				|| EXTENSION_SUPPORT_JSON_MEDIA_TYPE.includes(mediaType)) {
			return mediaType.toString();
		} else if (MediaType.APPLICATION_XML.includes(mediaType)
				|| MediaType.TEXT_HTML.includes(mediaType)
				|| MediaType.TEXT_XML.includes(mediaType)
				|| EXTENSION_SUPPORT_XML_MEDIA_TYPE.includes(mediaType)) {
			return mediaType.toString();
		}
		return MediaType.APPLICATION_JSON.toString();
	}
	
	protected String getContent(HttpServletRequest request) {
		String content = null;
		if (data == null) {
			content = "null";
			return content;
		}
		Object responseObj = data;
		if (data instanceof ResponseData && ((ResponseData<?>) data).getData() == null) {
			Map<String, Object> tempMap = IntrospectorUtils.toMap(data);
			tempMap.remove(ResponseData.DATA_FILTER_FIELD);
			responseObj = tempMap;
		} else if (data instanceof EmbedResponseData && ((EmbedResponseData) data).get(ResponseData.DATA_FILTER_FIELD) == null) {
			((EmbedResponseData) data).remove(ResponseData.DATA_FILTER_FIELD);
		}
		if (MediaType.APPLICATION_JSON.includes(mediaType)
				|| MediaType.TEXT_PLAIN.includes(mediaType)
				|| EXTENSION_SUPPORT_JSON_MEDIA_TYPE.includes(mediaType)) {
			content = getJsonContent(responseObj);
		} else if (MediaType.APPLICATION_XML.includes(mediaType)
				|| MediaType.TEXT_HTML.includes(mediaType)
				|| MediaType.TEXT_XML.includes(mediaType)
				|| EXTENSION_SUPPORT_XML_MEDIA_TYPE.includes(mediaType)) {
			content = XmlUtils.toXml(responseObj, charset);
		} else {
			//logger.warn("view not to support current media type[{}], use the default {}", mediaType.toString(), MediaType.APPLICATION_JSON_UTF8.toString());
			content = getJsonContent(responseObj);
		}
		return content;
	}
	
	/**
	 * @Title getJsonContent
	 * @Description 获取json字符串内容
	 * @param obj 待转换对象
	 * @return String
	 */
	private String getJsonContent(Object obj) {
		return JsonUtils.toString(obj);
	}

}