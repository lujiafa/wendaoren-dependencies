package com.tchain.core.exception;

import com.tchain.core.exception.table.CommonErrorCodeTable;
import org.springframework.util.Assert;

/**
 * @author Jon
 * @email lujiafayx@163.com
 * @date 2017年4月19日
 * @Description 业务异常基类
 */
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private ErrorCode errorCode;
    
    public BusinessException() {
        super(CommonErrorCodeTable.SERVER_BUSY.getMessage());
        this.errorCode = CommonErrorCodeTable.SERVER_BUSY.toErrorCode();
    }
    
    public BusinessException(Throwable cause) {
        super(cause);
        this.errorCode = CommonErrorCodeTable.SERVER_BUSY.toErrorCode();
    }
    
    public BusinessException(ErrorCode errorCode) {
        super(errorCode == null ? null : errorCode.getMessage());
        Assert.notNull(errorCode, "parameter object errorCode cannot be null");
        this.errorCode = errorCode;
    }
    
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode == null ? null : errorCode.getMessage(), cause);
        Assert.notNull(errorCode, "parameter object errorCode cannot be null");
        this.errorCode = errorCode;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
		Assert.notNull(message, "parameter error message cannot be null");
        this.errorCode = new ErrorCode(code, message);
    }
    
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
}