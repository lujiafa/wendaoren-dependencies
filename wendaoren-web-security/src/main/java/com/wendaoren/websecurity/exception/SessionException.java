package com.wendaoren.websecurity.exception;

import com.wendaoren.core.exception.BusinessException;
import com.wendaoren.core.exception.ErrorCode;

import java.util.Locale;

/**
 * @date 2019年6月18日
 * @Description 会话异常类
 */
public class SessionException extends BusinessException {

	private static final long serialVersionUID = 1L;

    public SessionException(Throwable cause, Locale locale) {
        super(cause, locale);
    }

    public SessionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SessionException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public SessionException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}