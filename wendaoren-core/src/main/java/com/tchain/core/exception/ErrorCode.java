package com.tchain.core.exception;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @ClassName ErrorCode
 * @date 2016年9月11日
 * @Description 错误码、错误消息载体封装
 */
public class ErrorCode implements Serializable {

	private static final long serialVersionUID = 1L;

	// 状态码
	private int code;
	// 状态信息
	private String message;
	// 状态信息参数
	private Object[] args;
	
    public ErrorCode(int code, String message) {
		this(code, message, null);
	}
    
	public ErrorCode(int code, String message, Object[] args) {
		Assert.notNull(code, "parameter error code cannot be null");
		Assert.notNull(message, "parameter error message cannot be null");
        this.code = code;
        this.message = message;
        this.args = args;
    }

	public int getCode() {
		return code;
	}
    
    public String getNativeMessage() {
    	return this.message;
    }

	public String getMessage() {
        if (this.args == null || args.length == 0) {
        	return this.message;
        }
        return MessageFormat.format(this.message, this.args);
    }

	public Object[] getArgs() {
		return args;
	}
	
	@Override
	public String toString() {
		return "{\"code\":\"" + code + "\",\"message\":\"" + getMessage() + "\"}";
	}
	
}