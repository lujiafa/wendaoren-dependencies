package com.wendaoren.web.model.response;

import com.wendaoren.core.constant.ErrorCodeConstant;
import com.wendaoren.core.exception.ErrorCode;
import com.wendaoren.utils.web.WebUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Locale;

/**
 * @ClassName ResponseData
 * @date 2016年9月11日
 * @Description 响应数据
 */
public class ResponseData<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 当响应为失败/错误内容时，可忽略字段名称 **/
	public final static String DATA_FILTER_FIELD = "data";
	
	private int code = ErrorCodeConstant.SUCCESS;
	private String message = ErrorCodeConstant.SUCCESS_MESSAGE;
	private T data;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * @Title hasSuccess
	 * @Description 判断状态是否为成功
	 * @return true-成功
	 */
	public boolean hasSuccess() {
		return ErrorCodeConstant.SUCCESS.equals(code);
	}
	
	public static <T> ResponseData<T> success() {
		return new ResponseData<T>();
	}

	public static <T> ResponseData<T> success(T data) {
		ResponseData<T> responseData = new ResponseData<>();
		responseData.setData(data);
		return responseData;
	}

	public static <T> ResponseData<T> success(Locale locale) {
		return success(locale, null);
	}
	
	public static <T> ResponseData<T> success(Locale locale, T data) {
		Locale currentLocale = locale;
		if (currentLocale == null) {
			try {
				currentLocale = WebUtils.getRequest().getLocale();
			} catch (Throwable e) {}
		}
		ErrorCode errorCode = ErrorCode.build(ErrorCodeConstant.SUCCESS, currentLocale);
		ResponseData<T> responseData = new ResponseData<T>();
		responseData.setCode(errorCode.getCode());
		responseData.setMessage(errorCode.getMessage());
		responseData.setData(data);
		return responseData;
	}
	
	public static <T> ResponseData<T> fail(ErrorCode errorCode) {
		Assert.notNull(errorCode, "parameter errorCode cannot be null.");
		return fail(errorCode.getCode(), errorCode.getMessage());
	}

	public static <T> ResponseData<T> fail(int code, String message) {
		ResponseData<T> responseData = new ResponseData<T>();
		responseData.setCode(code);
		responseData.setMessage(message);
		return responseData;
	}
	
}