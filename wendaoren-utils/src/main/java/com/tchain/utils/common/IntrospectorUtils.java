package com.tchain.utils.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author lujiafa
 * @email lujiafayx@163.com
 * @date 2019年3月5日
 * @Description: 通过内省将对象转Map
 */
public class IntrospectorUtils {

	/**
	 * @Title toMap
	 * @Description 通过内省方式获取属性
	 * @param bean
	 */
	public static Map<String, Object> toMap(Object bean) {
		return toMap(bean, Object.class);
	}

	/**
	 * @Title toStringMap
	 * @Description 对象通过内省方式转map
	 * @param bean 对象
	 */
	public static Map<String, String> toStringMap(Object bean) {
		return toMap(bean, String.class);
	}
	
	/**
	 * @Title toStringMap
	 * @Description 对象通过内省方式转map
	 * @param bean
	 * @param targetValueClazz 值：1.CharSequence及实现 2.Object
	 */
	@SuppressWarnings("unchecked")
	private static <T> Map<String, T> toMap(Object bean, Class<T> targetValueClazz) {
		if (bean == null) {
			return Collections.emptyMap();
		}
		try {
			Map<String, T> map = new LinkedHashMap<String, T>();
			Class<? extends Object> clazz = bean.getClass();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			// propertiesDescriptor来至于对相关Method的解析
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
				String propertyName = propertyDescriptor.getName();
				Method readMethod = propertyDescriptor.getReadMethod();
				if ("class".equals(propertyName) || readMethod == null) {
					continue;
				}
				Object propertyValue = readMethod.invoke(bean);
				if (propertyValue == null || !CharSequence.class.isAssignableFrom(targetValueClazz)) {
					map.put(propertyName, (T) propertyValue);
					continue;
				}
				// Value转JSON字符串
				String tempValue = null;
				Class<? extends Object> propertyValueClass = propertyValue.getClass();
				if (propertyValueClass.isPrimitive()
						|| Number.class.isAssignableFrom(propertyValueClass)
						|| CharSequence.class.isAssignableFrom(propertyValueClass)) {
					tempValue = propertyValue.toString();
				} else if (Date.class.isAssignableFrom(propertyValueClass)) {
					Date dateValue = (Date) propertyValue;
					JsonFormat jsonFormat = propertyDescriptor.getReadMethod().getAnnotation(JsonFormat.class);
					if (jsonFormat == null) {
						Field field = ReflectionUtils.findField(clazz, propertyName);
						if (field != null) {
							jsonFormat = field.getAnnotation(JsonFormat.class);
						}
					}
					if (jsonFormat != null && jsonFormat.pattern() != null && jsonFormat.pattern().length() > 0) {
						DateFormat dateFormat = null;
						if (!JsonFormat.DEFAULT_LOCALE.equals(jsonFormat.locale())
								&& jsonFormat.locale() != null && jsonFormat.locale().length() > 0) {
							dateFormat = new SimpleDateFormat(jsonFormat.pattern(), new Locale(jsonFormat.locale()));
						} else {
							dateFormat = new SimpleDateFormat(jsonFormat.pattern());
						}
						dateFormat.setLenient(jsonFormat.lenient().asPrimitive());
						if (!JsonFormat.DEFAULT_TIMEZONE.equals(jsonFormat.timezone())
								&& jsonFormat.timezone() != null && jsonFormat.timezone().length() > 0) {
							dateFormat.setTimeZone(TimeZone.getTimeZone(jsonFormat.timezone()));
						}
						tempValue = dateFormat.format(dateValue);
					} else {
						tempValue = JsonUtils.toString(dateValue);
					}
				} else {
					tempValue = JsonUtils.toString(propertyValue);
				}
				map.put(propertyName, (T) tempValue);
			}
			return map;
		} catch (IntrospectionException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}