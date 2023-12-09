package com.wendaoren.utils.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JsonUtils {
	
	static ObjectMapper objectMapper;

	static {
		// 初始化默认Mapper
		objectMapper = getDefaultObjectMapper(null, true);
	}

	public JsonUtils(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		objectMapper = getDefaultObjectMapper(jackson2ObjectMapperBuilder, true);
	}


	/**
	 * 对象序列化为字符串（空值不序列化）
	 * @Title toString
	 * @Description 对象转string字符串
	 * @param bean 对象
	 * @return String json字符串
	 */
	public static String toString(Object bean) {
		try {
			return objectMapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * @Title parseObject
	 * @Description json字符串转对象
	 * @param jsonString json字符串
	 * @param clzss 对象Class类型
	 * @return T
	 */
	public static <T> T parseObject(String jsonString, Class<T> clzss) {
		try {
			return objectMapper.readValue(jsonString, clzss);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * @Title parseObject
	 * @Description json字符串转对象
	 * @param jsonString json字符串
	 * @param typeReference 指定返回的类型和类型泛型参数
	 * @return T
	 */
	public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(jsonString, typeReference);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * @Title convertValue
	 * @Description 对象数据复制转换
	 * @param fromValue
	 * @param toValueType
	 * @return T
	 */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		return objectMapper.convertValue(fromValue, toValueType);
	}
	
	/**
	 * @Title convertValue
	 * @Description 对象数据复制转换
	 * @param fromValue
	 * @param toValueTypeRef
	 * @return T
	 */
	 public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
		return objectMapper.convertValue(fromValue, toValueTypeRef);
	}

	/**
	 * @Title getDefaultObjectMapper
	 * @Description 获取ObjectMapper
	 * @param jackson2ObjectMapperBuilder ObjectMapper对象创建器（可缺省）
	 * @param serializationIgnoreNull 序列化时是否忽略空值 true-忽略 false-保留
	 * @return ObjectMapper
	 */
	static ObjectMapper getDefaultObjectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder,
			boolean serializationIgnoreNull) {
		ObjectMapper objectMapper = getObjectMapper(jackson2ObjectMapperBuilder);
		// mapper在反序列化时，忽略类对象中没有的属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 禁用序列化空属性对象时抛异常
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		if (serializationIgnoreNull) {
			// 序列化时忽略空值
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
		// 允许对象序列化空值转空字符串
		// objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
		// 	@Override
		// 	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
		// 			throws IOException {
		// 		gen.writeString("");
		// 	}
		// });
		return objectMapper;
	}

	/**
	 * @Title getObjectMapper
	 * @Description 获取ObjectMapper
	 * @param jackson2ObjectMapperBuilder ObjectMapper对象创建器（可缺省）
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		ObjectMapper objectMapper = null;
		if (jackson2ObjectMapperBuilder != null) {
			objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
		} else {
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}

}