package com.wendaoren.utils.crypto;

import com.wendaoren.utils.data.ByteUtils;

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
	 * @Title encryptHMacSHA1ToBase64
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA1ToBase64(String data, String key) throws Exception {
		return Base64Utils.encode(encryptHMacSHA1(data, key, null));
	}
	
	/**
	 * @Title encryptHMacSHA1ToHex
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA1ToHex(String data, String key) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA1(data, key, null));
	}
	
	/**
	 * @Title encryptHMacSHA1
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA1(String data, String key) throws Exception {
		return encryptHMacSHA1(data, key, null);
	}
	
	/**
	 * @Title encryptHMacSHA1ToBase64
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA1ToBase64(String data, String key, String charset) throws Exception {
		return Base64Utils.encode(encryptHMacSHA1(data, key, charset));
	}
	
	/**
	 * @Title encryptHMacSHA1ToHex
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA1ToHex(String data, String key, String charset) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA1(data, key, charset));
	}
	
	/**
	 * @Title encryptHMacSHA1
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA1(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMacSHA1(dataBytes, keyBytes);
	}
	
	/**
	 * @Title encryptHMacSHA1ToBase64
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA1ToBase64(byte[] data, byte[] key) throws Exception {
		return Base64Utils.encode(encryptHMac(data, key, ALGORITHM_1));
	}
	
	/**
	 * @Title encryptHMacSHA1ToHex
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA1ToHex(byte[] data, byte[] key) throws Exception {
		return ByteUtils.toHex(encryptHMac(data, key, ALGORITHM_1));
	}
	
	/**
	 * @Title encryptHMacSHA1
	 * @Description 过h-mac-sha1方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA1(byte[] data, byte[] key) throws Exception {
		 return encryptHMac(data, key, ALGORITHM_1);
	}
	
	/**
	 * @Title encryptHMacSHA256ToBase64
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA256ToBase64(String data, String key) throws Exception {
		return Base64Utils.encode(encryptHMacSHA256(data, key, null));
	}
	
	/**
	 * @Title encryptHMacSHA256ToHex
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA256ToHex(String data, String key) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA256(data, key, null));
	}
	
	/**
	 * @Title encryptHMacSHA256
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA256(String data, String key) throws Exception {
		return encryptHMacSHA256(data, key, null);
	}
	
	/**
	 * @Title encryptHMacSHA256ToBase64
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA256ToBase64(String data, String key, String charset) throws Exception {
		return Base64Utils.encode(encryptHMacSHA256(data, key, charset));
	}
	
	/**
	 * @Title encryptHMacSHA256ToHex
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA256ToHex(String data, String key, String charset) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA256(data, key, charset));
	}
	
	/**
	 * @Title encryptHMacSHA256
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA256(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMacSHA256(dataBytes, keyBytes);
	}
	
	/**
	 * @Title encryptHMacSHA256ToBase64
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA256ToBase64(byte[] data, byte[] key) throws Exception {
		return Base64Utils.encode(encryptHMac(data, key, ALGORITHM_256));
	}

	/**
	 * @Title encryptHMacSHA256ToHex
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA256ToHex(byte[] data, byte[] key) throws Exception {
		return ByteUtils.toHex(encryptHMac(data, key, ALGORITHM_256));
	}
	
	/**
	 * @Title encryptHMacSHA256
	 * @Description 过h-mac-sha256方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA256(byte[] data, byte[] key) throws Exception {
		 return encryptHMac(data, key, ALGORITHM_256);
	}
	
	/**
	 * @Title encryptHMacSHA512ToBase64
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA512ToBase64(String data, String key) throws Exception {
		return Base64Utils.encode(encryptHMacSHA512(data, key, null));
	}
	
	/**
	 * @Title encryptHMacSHA512ToHex
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA512ToHex(String data, String key) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA512(data, key, null));
	}
	
	/**
	 * @Title encryptHMacSHA512
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA512(String data, String key) throws Exception {
		return encryptHMacSHA512(data, key, null);
	}
	
	/**
	 * @Title encryptHMacSHA512ToBase64
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA512ToBase64(String data, String key, String charset) throws Exception {
		return Base64Utils.encode(encryptHMacSHA512(data, key, charset));
	}
	
	/**
	 * @Title encryptHMacSHA512ToHex
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA512ToHex(String data, String key, String charset) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA512(data, key, charset));
	}
	
	/**
	 * @Title encryptHMacSHA512
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @param charset 字符串编码方式
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] encryptHMacSHA512(String data, String key, String charset) throws Exception {
		byte[] dataBytes = data.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		byte[] keyBytes = key.getBytes(charset == null ? StandardCharsets.UTF_8.name() : charset);
		return encryptHMacSHA512(dataBytes, keyBytes);
	}
	
	/**
	 * @Title encryptHMacSHA512ToBase64
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA512ToBase64(byte[] data, byte[] key) throws Exception {
		return Base64Utils.encode(encryptHMacSHA512(data, key));
	}
	
	/**
	 * @Title encryptHMacSHA512ToHex
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
	 * @throws Exception
	 * @return String
	 */
	public static String encryptHMacSHA512ToHex(byte[] data, byte[] key) throws Exception {
		return ByteUtils.toHex(encryptHMacSHA512(data, key));
	}
	
	/**
	 * @Title encryptHMacSHA512
	 * @Description 过h-mac-sha512方式进行数据加密
	 * @param data 需加密数据
	 * @param key 加密key
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