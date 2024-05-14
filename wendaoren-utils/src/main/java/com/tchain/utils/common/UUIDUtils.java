package com.tchain.utils.common;

import java.util.UUID;

public class UUIDUtils {
	
	/**
	 * @Title genUUIDString
	 * @Description 生成UUID字符串
	 * @return String
	 */
	public static String genUUIDString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}