package com.wendaoren.websecurity.exception;

import com.wendaoren.core.exception.BusinessException;
import com.wendaoren.core.exception.ErrorCode;

public class PermissionException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	public PermissionException() {
        super();
    }
    
    public PermissionException(Throwable cause) {
        super(cause);
    }
    
    public PermissionException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public PermissionException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public PermissionException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}