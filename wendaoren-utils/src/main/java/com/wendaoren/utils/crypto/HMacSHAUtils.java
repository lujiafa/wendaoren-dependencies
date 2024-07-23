package com.wendaoren.utils.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName HMacSHA
 * @author lujiafa
 * @date 2017年10月26日
 * @Description  h-mac-sha方式加密
 */
public class HMacSHAUtils {
	
	/**
	 * HmacSHA1加密算法
	 */
	public static final String ALGORITHM_1 = "HmacSHA1";
	
	/**
	 * HmacSHA256加密算法
	 */
	public static final String ALGORITHM_256 = "HmacSHA256";
	
	/**
	 * HmacSHA512加密算法
	 */
	public static final String ALGORITHM_512 = "HmacSHA512";
	
	/**
	 * encryptHMacSHA1 - h-mac-sha1方式进行数据加密 <br>
	 * @param data 需加密数据【M】
	 * @param key 加密key【M】
	 * @param charset 字符串编码方式【O】,默认UTF-8
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA1(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMacSHA1(dataBytes, keyBytes);
	}
	
	/**
	 * encryptHMacSHA1 - h-mac-sha1方式进行数据加密 <br>
	 * @param data 需加密数据【M】
	 * @param key 加密key【M】
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA1(byte[] data, byte[] key) throws Exception {
		 return encryptHMac(data, key, ALGORITHM_1);
	}
	
	/**
	 * encryptHMacSHA256 - h-mac-sha256方式进行数据加密 <br>
	 * @param data 需加密数据【M】
	 * @param key 加密key【M】
	 * @param charset 字符串编码方式【O】,默认UTF-8
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA256(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMacSHA256(dataBytes, keyBytes);
	}
	
	/**
	 * encryptHMacSHA256 - h-mac-sha256方式进行数据加密 <br>
	 * @param data 需加密数据【M】
	 * @param key 加密key【M】
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA256(byte[] data, byte[] key) throws Exception {
		 return encryptHMac(data, key, ALGORITHM_256);
	}
	
	/**
	 * encryptHMacSHA512 - h-mac-sha512方式进行数据加密 <br>
	 * @param data 需加密数据【M】
	 * @param key 加密key【M】
	 * @param charset 字符串编码方式【O】,默认UTF-8
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA512(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMacSHA512(dataBytes, keyBytes);
	}
	
	/**
	 * encryptHMacSHA512 - h-mac-sha512方式进行数据加密 <br>
	 * @param data 需加密数据【M】
	 * @param key 加密key【M】
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA512(byte[] data, byte[] key) throws Exception {
		return encryptHMac(data, key, ALGORITHM_512);
	}
	
	/**
	 * @Title encryptHMac
	 * @Description 过加密算法algorithm进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param algorithm 加密算法
	 * @throws Exception
	 * @return byte[]
	 */
	private static byte[] encryptHMac(byte[] data, byte[] key, String algorithm) throws Exception {
		SecretKeySpec signingKey = new SecretKeySpec(key, algorithm);
		Mac mac = Mac.getInstance(algorithm);
		mac.init(signingKey);
		return mac.doFinal(data);
	}
	
	
}