package com.tchain.core.exception.table;

import com.tchain.core.exception.ErrorCode;

/**
 * @author Jon
 * @email lujiafayx@163.com
 * @date 2017年4月19日
 * @Description 通用异常枚举
 */
public enum CommonErrorCodeTable implements ErrorCodeTable {
	SUCCESS(0, "OK"),

    INTERNAL_ERROR(1,"内部错误"),
    INTERNAL_ERROR_P(1,"内部错误({0})"),
    SERVER_BUSY(2, "系统繁忙,请稍后再试"),
    NETWORK_ERROR(3, "网络异常，请稍后再试"),
    OPERATION_FAIL(4, "操作失败，请稍后再试"),
    OPERATION_FAIL_P(4, "操作失败({0})"),
    REQUEST_INVALID(5, "无效的请求"),
    REQUEST_INVALID_IP(6, "无效的请求IP"),
    REQUEST_INVALID_DATA(7, "无效的请求数据"),
	REQUEST_REPEAT(8, "重复的请求"),
    REQUEST_TOO_FREQUENCY(9, "请求频次太高，请稍后再试"),
    REQUEST_TOO_FREQUENCY_P(9, "请求频次太高，请{0}分钟后再试"),

    USERNAME_NOT_EXIST(10, "用户名不存在"),
    ACCOUNT_LOCKED(11, "账号已锁定"),
    ACCOUNT_EXCEPTION(12, "账号异常"),

    PASSWORD_ERROR(13, "密码错误"),
    PASSWORD_ERROR_P(13, "密码错误({0})"),
    USERNAME_OR_PASSWORD_ERROR(14, "用户名或密码错误"),
    SESSION_EXPIRED(15, "会话已过期，请重新登陆"),
    SESSION_KICK_OUT_EXPIRED(16, "会话已过期，当前用户已在其它终端登录"),

    PARAMS_ERROR(30, "参数错误"),
    PARAMS_EMPTY(31, "参数不能为空"),
    PARAMS_EMPTY_P(31, "参数不能为空({0})"),
    PARAMS_FORMAT_ERROR(32, "参数格式错误"),
    PARAMS_FORMAT_ERROR_P(32, "参数格式错误({0})"),
    PARAMS_TYPE_ERROR(33, "参数类型错误"),
    PARAMS_VALID_ERROR(34, "参数验证错误"),
    PARAMS_VALID_ERROR_P(34, "参数验证错误（{0}）"),
    NOT_SUPPORT_PARAMS_TYPE_CONVERT(35, "不支持的参数类型转换"),
    NOT_SUPPORT_PARAMS_TYPE_CONVERT_P(35, "不支持的参数类型[{0}->{1}]转换"),
    VERIFICATION_CODE_ERROR(36, "验证码错误"),

    DATA_LOAD_FAIL(40, "数据加载失败，请稍候再试"),
    DATA_NOT_EXISTS(41, "数据信息不存在"),
    DATA_NOT_EXISTED(42, "数据信息已存在"),

    OBJECT_SERIALIZABLE_FAIL(90, "对象序列化失败"),
    CLASS_NOT_IMPLEMENTED_SERIALIZABLE(91, "类未实现Serializable接口"),
    CONFIG_PARAMS_ERROR_P(92, "配置参数错误({0})"),
	CONFIG_PARAMS_FORMAT_ERROR(93, "配置参数格式错误"),
	CONFIG_PARAMS_FORMAT_ERROR_P(93, "配置参数格式错误({0})"),

	UNDETERMINED_ERROR(99, "{0}");

	private final int code;
    private final String message;
    
	private CommonErrorCodeTable(int code, String message) {
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