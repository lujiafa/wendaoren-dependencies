package com.wendaoren.websecurity.exception.table;

import com.wendaoren.core.exception.ErrorCode;
import com.wendaoren.core.exception.table.ErrorCodeTable;

public enum WebSecurityErrorCodeTable implements ErrorCodeTable {

	NO_OPERATION_PERMISSION(70, "您没有操作权限"),



    PARAM_SIGNATURE_VALID_FAIL(73, "签名验证失败");

    private final int code;
    private final String message;

    private WebSecurityErrorCodeTable(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public ErrorCode toErrorCode(Object... args) {
        return new ErrorCode(this.getCode(), this.getMessage(), args);
    }
}