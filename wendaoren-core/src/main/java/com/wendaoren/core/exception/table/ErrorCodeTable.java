package com.wendaoren.core.exception.table;

import com.wendaoren.core.exception.ErrorCode;

/**
 * @ClassName ErrorCodeTable
 * @date 2016年9月11日
 * @Description 错误枚举
 */
public interface ErrorCodeTable {

    ErrorCode toErrorCode(Object... args);
}