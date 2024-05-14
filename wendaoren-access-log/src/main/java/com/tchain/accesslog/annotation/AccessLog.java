package com.tchain.accesslog.annotation;

import com.tchain.accesslog.handler.LogFilterHandler;
import com.tchain.accesslog.handler.SimpleLogFilterHandler;

import java.lang.annotation.*;

@Documented
@Target(value={ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLog {
	
	/**
	 * @description 是否开启访问日志
	 * @return boolean true-开启日志/启用 false-关闭日志/不启用
	 */
	boolean value() default true;

	/**
	 * 是否启用输出RequestBody参数数据
	 * @return true-开启 false-关闭
	 */
	boolean requestBody() default false;
	
	/**
	 * @Title logFilterHandler
	 * @Description 参数过滤处理器
	 * @return Class<LogFilterHandler>
	 */
	Class<? extends LogFilterHandler> logFilterHandler() default SimpleLogFilterHandler.class;

}