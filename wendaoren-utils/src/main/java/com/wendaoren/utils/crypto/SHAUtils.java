package com.wendaoren.utils.crypto;

import com.wendaoren.utils.data.ByteUtils;

import java.security.MessageDigest;

public final class SHAUtils {
	
	/**
	 * SHA1加密算法
	 */
	public static final String ALGORITHM = "SHA";
	
	/**
	 * SHA256加密算法
	 */
	public static final String ALGORITHM_256 = "SHA-256";
	
	/**
	 * SHA384加密算法
	 */
	public static final String ALGORITHM_384 = "SHA-384";
	
	/**
	 * SHA512加密算法
	 */
	public static final String ALGORITHM_512 = "SHA-512";
	
	/**
	 * @Title encryptSHAToBase64
	 * @Description SHA-1消息摘要算法
	 * @param data 需要加密的字符串byte数组
	 * @return String 通过加密算法计算后的base64字符串
	 */
	public static String encryptSHAToBase64(byte[] data) throws Exception {
		return Base64Utils.encode(encryptSHA(data));
	}
	
	/**
	 * @Title encryptSHAToHex
	 * @Description SHA-1消息摘要算法
	 * @param data 需要加密的字符串byte数组
	 * @return String 通过加密算法计算后的base64字符串
	 */
	public static String encryptSHAToHex(byte[] data) throws Exception {
		return ByteUtils.toHex(encryptSHA(data));
	}
	
	/**
	 * @Title encryptSHA
	 * @Description SHA-1消息摘要算法
	 * @param data 需要加密的字符串byte数组
	 * @return String 通过加密算法计算后字节数组
	 */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        // 执行摘要方法
        byte[] digest = md.digest(data);
        return digest;
    }
    
    /**
     * @Title encryptSHA256
     * @Description SHA-256消息摘要算法
     * @param data 需要加密的数据
     * @return String 通过加密算法计算后的base64字符串
     */
    public static String encryptSHA256ToBase64(byte[] data) throws Exception {
    	return Base64Utils.encode(encryptSHA256(data));
    }
    
    /**
     * @Title encryptSHA256
     * @Description SHA-256消息摘要算法
     * @param data 需要加密的数据
     * @return String 通过加密算法计算后的16进制字符串
     */
    public static String encryptSHA256ToHex(byte[] data) throws Exception {
    	return ByteUtils.toHex(encryptSHA256(data));
    }
    
    /**
	 * @Title encryptSHA256
	 * @Description SHA-256消息摘要算法
	 * @param data 需要加密的数据
	 * @return byte[] 通过加密算法计算后的字节数组
	 */
    public static byte[] encryptSHA256(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_256);
        // 执行摘要方法
        byte[] digest = md.digest(data);
        return digest;
    }
    
    /**
     * @Title encryptSHA384ToBase64
     * @Description SHA-384消息摘要算法
     * @param data 需要加密的数据
     * @return String 通过加密算法计算后的base64字符串
     */
    public static String encryptSHA384ToBase64(byte[] data) throws Exception {
    	return Base64Utils.encode(encryptSHA384(data));
    }
    
    /**
     * @Title encryptSHA384
     * @Description SHA-384消息摘要算法
     * @param data 需要加密的数据
     * @return String 通过加密算法计算后的16进制字符串
     */
    public static String encryptSHA384ToHex(byte[] data) throws Exception {
    	return ByteUtils.toHex(encryptSHA384(data));
    }
    
    /**
   	 * @Title encryptSHA384
   	 * @Description SHA-384消息摘要算法
   	 * @param data 需要加密的数据
   	 * @return String 通过加密算法计算后的字节数组
   	 */
    public static byte[] encryptSHA384(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_384);
        // 执行摘要方法
        byte[] digest = md.digest(data);
        return digest;
    }
    
    /**
     * @Title encryptSHA512ToBase64
     * @Description SHA-512消息摘要算法
     * @param data 需要加密的数据
     * @return String 通过加密算法计算后的base64字符串
     */
    public static String encryptSHA512ToBase64(byte[] data) throws Exception {
    	return Base64Utils.encode(encryptSHA512(data));
    }
    
    /**
     * @Title encryptSHA512ToHex
     * @Description SHA-512消息摘要算法
     * @param data 需要加密的数据
     * @return String 通过加密算法计算后的16进制字符串
     */
    public static String encryptSHA512ToHex(byte[] data) throws Exception {
    	return ByteUtils.toHex(encryptSHA512(data));
    }
  
    /**
   	 * @Title encryptSHA512
   	 * @Description SHA-512消息摘要算法
   	 * @param data 需要加密的数据
   	 * @return byte[] 通过加密算法计算后的字节数组
   	 */
    public static byte[] encryptSHA512(byte[] data) throws Exception {
    	// 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_512);
        // 执行摘要方法
        byte[] digest = md.digest(data);
        return digest;
    }
    
}