package com.wendaoren.web.model.response;

import com.wendaoren.core.exception.ErrorCode;
import com.wendaoren.core.exception.table.CommonErrorCodeTable;
import com.wendaoren.core.model.IModel;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName EmbedResponseData
 * @date 2016年9月11日
 * @Description 响应数据
 */
public class EmbedResponseData extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public final static String CODE_NAME = "code";
	public final static String MESSAGE_NAME = "message";

	/**
	 * @Title hasSuccess
	 * @Description 判断状态是否为成功
	 * @return true-成功
	 */
	public boolean hasSuccess() {
		return ((Integer) CommonErrorCodeTable.SUCCESS.getCode()).equals(get(CODE_NAME));
	}

	public static EmbedResponseData success() {
		EmbedResponseData responseData = new EmbedResponseData();
		responseData.put(CODE_NAME, CommonErrorCodeTable.SUCCESS.getCode());
		responseData.put(MESSAGE_NAME, CommonErrorCodeTable.SUCCESS.getMessage());
		return responseData;
	}

	public static EmbedResponseData success(Map<String, Object> data) {
		EmbedResponseData responseData = success();
		if (data == null) {
			return responseData;
		}
		responseData.putAll(data);
		return responseData;
	}

	public static EmbedResponseData success(IModel data) {
		EmbedResponseData responseData = success();
		if (data == null) {
			return responseData;
		}
		responseData.putIntrospector(data);
		return responseData;
	}

	public static EmbedResponseData fial(ErrorCode errorCode) {
		Assert.notNull(errorCode, "parameter errorCode cannot be null.");
		return fail(errorCode.getCode(), errorCode.getMessage());
	}

	public static EmbedResponseData fail(int code, String message) {
		Assert.hasText(message, "parameter message cannot be empty.");
		EmbedResponseData responseData = new EmbedResponseData();
		responseData.put(CODE_NAME, code);
		responseData.put(MESSAGE_NAME, message);
		return responseData;
	}

	private void putIntrospector(Object bean) {
		if (bean == null) {
			return;
		}
		try {
			Class<? extends Object> clazz = bean.getClass();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
				String propertyName = propertyDescriptor.getName();
				Method readMethod = propertyDescriptor.getReadMethod();
				if ("class".equals(propertyName) || readMethod == null) {
					continue;
				}
				Object propertyValue = readMethod.invoke(bean);
				put(propertyName, propertyValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}