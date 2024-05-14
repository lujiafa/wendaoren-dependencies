package com.tchain.websecurity.exception;

import com.tchain.core.exception.BusinessException;
import com.tchain.core.exception.ErrorCode;
/**
 * @date 2019年6月18日
 * @Description 签名异常类
 */
public class SignatureException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	public SignatureException() {
        super();
    }
    
    public SignatureException(Throwable cause) {
        super(cause);
    }
    
    public SignatureException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public SignatureException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public SignatureException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}