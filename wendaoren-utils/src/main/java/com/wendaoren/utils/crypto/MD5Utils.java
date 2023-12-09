package com.wendaoren.utils.crypto;

import com.wendaoren.utils.data.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class MD5Utils {
	
	/**
	 * 算法
	 */
	public static final String ALGORITHM = "MD5";
	
	/***
	 * @Description 生成MD5加密字符串
	 * @param data 要转MD5的字符串
	 * @return String 加密后字符串
	 */
	public static String encryptToBase64(String data) {
		return Base64Utils.encode(encrypt(data));
	}
	
	/***
	 * @Description 生成MD5加密字符串
	 * @param data 要转MD5的字符串
	 * @return String 加密后字符串
	 */
	public static String encryptToHex(String data) {
		return ByteUtils.toHex(encrypt(data));
	}
	
	/***
	 * @Description 生成MD5加密字符串
	 * @param data 要转MD5的字符串
	 * @return String 加密后数据
	 */
	public static byte[] encrypt(String data) {
		return encrypt(data, StandardCharsets.UTF_8.name());
	}
	
	/***
	 * @Description 生成MD5加密字符串
	 * @param data 要转MD5的数据
	 * @param charset 编码格式
	 * @return String 加密后字符串
	 */
	public static String encryptToBase64(String data, String charset) {
		return Base64Utils.encode(encrypt(data, charset));
	}
	
	/***
	 * @Description 生成MD5加密字符串
	 * @param data 要转MD5的数据
	 * @param charset 编码格式
	 * @return String 加密后字符串
	 */
	public static String encryptToHex(String data, String charset) {
		return ByteUtils.toHex(encrypt(data, charset));
	}
	
	/***
     * @Description 生成MD5加密字符串
     * @param data 要转MD5的数据
     * @param charset 编码格式
     * @return byte[] 加密后数据
     */
    public static byte[] encrypt(String data, String charset) {
    	try {
	    	byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
	    	return encrypt(dataBytes);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
    
    public static String encryptToBase64(byte[] dataBytes) {
    	return Base64Utils.encode(encrypt(dataBytes));
    }
    
    public static String encryptToHex(byte[] dataBytes) {
    	return ByteUtils.toHex(encrypt(dataBytes));
    }
    
    public static byte[] encrypt(byte[] dataBytes) {
    	try {
	    	MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
	    	byte[] digest = md5.digest(dataBytes);
	    	return digest;
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
  
}