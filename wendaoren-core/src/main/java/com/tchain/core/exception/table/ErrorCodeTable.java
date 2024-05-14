package com.tchain.core.exception.table;

import com.tchain.core.exception.ErrorCode;

/**
 * @ClassName ErrorCodeTable
 * @date 2016年9月11日
 * @Description 错误枚举
 */
public interface ErrorCodeTable {

    ErrorCode toErrorCode(Object... args);
}