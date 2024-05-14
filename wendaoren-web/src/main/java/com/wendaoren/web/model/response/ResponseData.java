package com.wendaoren.web.model.response;

import com.wendaoren.core.exception.ErrorCode;
import com.wendaoren.core.exception.table.CommonErrorCodeTable;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @ClassName ResponseData
 * @date 2016年9月11日
 * @Description 响应数据
 */
public class ResponseData<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 当响应为失败/错误内容时，可忽略字段名称 **/
	public final static String DATA_FILTER_FIELD = "data";
	
	private int code = CommonErrorCodeTable.SUCCESS.getCode();
	private String message = CommonErrorCodeTable.SUCCESS.getMessage();
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
		return ((Integer) CommonErrorCodeTable.SUCCESS.getCode()).equals(code);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseData<T> success() {
		return (ResponseData<T>) success(null);
	}
	
	public static <T> ResponseData<T> success(T data) {
		ResponseData<T> responseData = new ResponseData<T>();
		responseData.setData(data);
		return responseData;
	}
	
	public static <T> ResponseData<T> fail(ErrorCode errorCode) {
		Assert.notNull(errorCode, "parameter errorCode cannot be null.");
		return fail(errorCode.getCode(), errorCode.getMessage());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseData<T> fail(int code, String message) {
		Assert.hasText(message, "parameter message cannot be empty.");
		ResponseData<Object> responseData = new ResponseData<Object>();
		responseData.setCode(code);
		responseData.setMessage(message);
		return (ResponseData<T>) responseData;
	}
	
}