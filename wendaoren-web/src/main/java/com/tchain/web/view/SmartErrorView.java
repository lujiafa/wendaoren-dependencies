package com.tchain.web.view;

import com.tchain.core.exception.ErrorCode;
import com.tchain.web.constan.WebSupportConstant;
import com.tchain.web.model.response.ResponseData;
import com.tchain.web.util.SupportDefaultErrorPageTemplate;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

public class SmartErrorView extends SmartView {
	
	private ErrorCode errorCode;
	
	public SmartErrorView(ErrorCode errorCode, MediaType mediaType) {
		super(ResponseData.fail(errorCode), mediaType, true);
		this.errorCode = errorCode;
	}
	
	@Override
	public String getContentType() {
		if (MediaType.TEXT_HTML.includes(mediaType)
				|| MediaType.APPLICATION_XHTML_XML.includes(mediaType)) {
			return mediaType.toString();
		}
		return super.getContentType();
	}
	
	@Override
	protected String getContent(HttpServletRequest request) {
		if (MediaType.TEXT_HTML.includes(mediaType)
				|| MediaType.APPLICATION_XHTML_XML.includes(mediaType)) {
			return SupportDefaultErrorPageTemplate.getPage(errorCode.getMessage(), (String) request.getAttribute(WebSupportConstant.ERROR_REDIRECT_PAGE_ATTR_NAME));
		}
		return super.getContent(request);
	}

}