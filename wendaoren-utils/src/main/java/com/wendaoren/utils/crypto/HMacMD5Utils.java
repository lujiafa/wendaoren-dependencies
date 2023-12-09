package com.wendaoren.utils.crypto;

import com.wendaoren.utils.data.ByteUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author lujiafa
 * @date 2016年7月25日
 * @Description h-mac-md5方式数据加密
 */
public final class HMacMD5Utils {
	
	/**
	 * 密钥生成器算法
	 */
	public static final String ALGORITHM = "HmacMD5";
	
	/**
	 * @Title: encryptHMACToBase64
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @return String 已加密base64字符串
	 */
	public static String encryptHMACToBase64(String data, String key) throws Exception {
		return Base64Utils.encode(encryptHMAC(data, key));
	}
	
	/**
	 * @Title: encryptHMACToHex
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @return String 已加密16进制字符串
	 */
	public static String encryptHMACToHex(String data, String key) throws Exception {
		return ByteUtils.toHex(encryptHMAC(data, key));
	}
	
	/**
	 * @Title: encryptHMAC
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @return String 已加密字节数组
	 */
	public static byte[] encryptHMAC(String data, String key) throws Exception {
		return encryptHMAC(data, key, StandardCharsets.UTF_8.name());
	}
	
	/**
	 * @Title: encryptHMACToBase64
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 编码方式，默认为UTF-8
	 * @return String 已加密base64字符串
	 */
	public static String encryptHMACToBase64(String data, String key, String charset) throws Exception {
		return Base64Utils.encode(encryptHMAC(data, key, charset));
	}
	
	/**
	 * @Title: encryptHMACToHex
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 编码方式，默认为UTF-8
	 * @return String 已加密16进制字符串
	 */
	public static String encryptHMACToHex(String data, String key, String charset) throws Exception {
		return ByteUtils.toHex(encryptHMAC(data, key, charset));
	}
	
	/**
	 * @Title: encryptHMAC
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 编码方式，默认为UTF-8
	 * @return byte[] 已加密字节数组
	 */
	public static byte[] encryptHMAC(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMAC(dataBytes, keyBytes);
	}
	
	/**
	 * @Title: encryptHMAC
	 * @Description: 通过h-mac-md5方式进行数据加密
	 * @param dataBytes 需加密数据
	 * @param keyBytes 加密key
	 * @return byte[] 已加密字节数组
	 */
	public static byte[] encryptHMAC(byte[] dataBytes, byte[] keyBytes) throws Exception {
		SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		byte[] bytes = mac.doFinal(dataBytes);
		return bytes;
	}
	
}