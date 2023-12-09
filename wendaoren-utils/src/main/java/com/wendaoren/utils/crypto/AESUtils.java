package com.wendaoren.utils.crypto;

import com.wendaoren.utils.constant.ProviderConstant;
import com.wendaoren.utils.data.ByteUtils;
import com.wendaoren.utils.data.HexUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Provider;

/**
 * @author lujiafa
 * @date 2016年8月15日
 * @Description: AES加密/解密工具类
 */
public final class AESUtils {
	
	/**
	 * 密钥生成器算法
	 */
	public static final String KEY_ALGORITHM = "AES";
	
	/**
	 * 偏移量
	 */
	public static final byte[] DEFAULT_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	/**
	 * 密码转换器算法（算法/模式/填充。默认："AES/CBC/PKCS5Padding"）
	 */
	public enum AESTransformationAlgorithm {
		AES_CBC_NoPadding("AES/CBC/NoPadding", null),
		AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding", null),
		AES_CBC_PKCS7Padding("AES/CBC/PKCS7Padding", ProviderConstant.PROVIDER_BOUNCY_CASTLE),
		;
		
		private String transFormationAlgorithm;
		private Provider provider;
		
		private AESTransformationAlgorithm(String transFormationAlgorithm, BouncyCastleProvider provider) {
			this.transFormationAlgorithm = transFormationAlgorithm;
			this.provider = provider;
		}
		
		public String getTransFormationAlgorithm() {
			return transFormationAlgorithm;
		}
		
		public Provider getProvider() throws Exception {
			return provider;
		}
	}
	
	/**
	 * @Title: encryptToBase64
	 * @Description: AES加密
	 * @param srcStr 需加密字数据
	 * @param keyStr 密钥
	 * @return String 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToBase64(String srcStr, String keyStr)
			throws Exception {
		return Base64Utils.encode(encrypt(srcStr.getBytes(StandardCharsets.UTF_8), keyStr.getBytes(StandardCharsets.UTF_8), null, null));
	}
	
	/**
	 * @Title: encryptToHex
	 * @Description: AES加密
	 * @param srcStr 需加密字数据
	 * @param keyStr 密钥
	 * @return String 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToHex(String srcStr, String keyStr)
			throws Exception {
		return ByteUtils.toHex(encrypt(srcStr.getBytes(StandardCharsets.UTF_8), keyStr.getBytes(StandardCharsets.UTF_8), null, null));
	}
	
	/**
	 * @Title: encrypt
	 * @Description: AES加密
	 * @param srcStr 需加密字数据
	 * @param keyStr 密钥
	 * @return byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static byte[] encrypt(String srcStr, String keyStr)
			throws Exception {
		return encrypt(srcStr.getBytes(StandardCharsets.UTF_8), keyStr.getBytes(StandardCharsets.UTF_8), null, null);
	}
	
	/**
	 * @Title: encryptToBase64
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @return String 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToBase64(byte[] data, byte[] keyBytes)
			throws Exception {
		return Base64Utils.encode(encrypt(data, keyBytes, null, null));
	}
	
	/**
	 * @Title: encryptToHex
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @return String 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToHex(byte[] data, byte[] keyBytes)
			throws Exception {
		return ByteUtils.toHex(encrypt(data, keyBytes, null, null));
	}
	
	/**
	 * @Title: encrypt
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @return byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static byte[] encrypt(byte[] data, byte[] keyBytes)
			throws Exception {
		return encrypt(data, keyBytes, null, null);
	}
	
	/**
	 * @Title: encryptToBase64
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @return String 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToBase64(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return Base64Utils.encode(encrypt(data, keyBytes, transformationAlgorithm, null));
	}
	
	/**
	 * @Title: encryptToHex
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @return String 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToHex(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return ByteUtils.toHex(encrypt(data, keyBytes, transformationAlgorithm, null));
	}
	
	/**
	 * @Title: encrypt
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @return byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static byte[] encrypt(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return encrypt(data, keyBytes, transformationAlgorithm, null);
	}
	
	/**
	 * @Title: encrypt
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @param iv 偏移量
	 * @return  byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToBase64(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, byte[] iv) throws Exception {
		return Base64Utils.encode(encrypt(data, keyBytes, transformationAlgorithm, iv));
	}
	
	/**
	 * @Title: encryptToHex
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @param iv 偏移量
	 * @return  byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static String encryptToHex(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, byte[] iv) throws Exception {
		return ByteUtils.toHex(encrypt(data, keyBytes, transformationAlgorithm, iv));
	}
	
	/**
	 * @Title: encrypt
	 * @Description: AES加密
	 * @param data 需加密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @param iv 偏移量
	 * @return  byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static byte[] encrypt(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, byte[] iv)
			throws Exception {
		SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
		if (transformationAlgorithm == null) {
			transformationAlgorithm = AESTransformationAlgorithm.AES_CBC_PKCS5Padding;
		}
		// 创建密码器，它用于完成实际的加密操作（//算法/模式/填充。默认："AES/ECB/PKCS5Padding"）
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		if (iv == null) {
			iv = DEFAULT_IV;
		}
		IvParameterSpec spec = new IvParameterSpec(iv);
		// 初始化Cipher对象，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
		byte[] doFinal = cipher.doFinal(data);
		return doFinal;
	}
	
	
	/**
	 * @Title: decryptFromBase64ToString
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param encryptedStr 密钥
	 * @throws Exception 
	 */
	public static String decryptFromBase64ToString(String encryptedStr, byte[] keyBytes, Charset charset)
			throws Exception {
		return new String(decryptFromBase64(encryptedStr, keyBytes), (charset == null ? StandardCharsets.UTF_8 : charset).name());
	}
	
	/**
	 * @Title: decryptFromBase64
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param encryptedStr 密钥
	 * @throws Exception 
	 */
	public static byte[] decryptFromBase64(String encryptedStr, byte[] keyBytes)
			throws Exception {
		return decrypt(Base64Utils.decode(encryptedStr), keyBytes);
	}
	
	/**
	 * @Title: decryptFromHexToUTF8
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param encryptedStr 密钥
	 * @param charset 字符集
	 * @throws Exception 
	 */
	public static String decryptFromHexToString(String encryptedStr, byte[] keyBytes, Charset charset)
			throws Exception {
		return new String(decryptFromHex(encryptedStr, keyBytes), (charset == null ? StandardCharsets.UTF_8 : charset).name());
	}
	
	/**
	 * @Title: decryptFromHex
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param keyBytes 密钥
	 * @throws Exception 
	 */
	public static byte[] decryptFromHex(String encryptedStr, byte[] keyBytes)
			throws Exception {
		return decrypt(HexUtils.toBinary(encryptedStr), keyBytes);
	}
	
	/**
	 * @Title: decryptFromHex
	 * @Description: AES解密
	 * @param encryptedBytes 需解密字数据
	 * @param keyBytes 密钥
	 * @param charset 字符集
	 * @throws Exception 
	 */
	public static String decryptToString(byte[] encryptedBytes, byte[] keyBytes, Charset charset)
			throws Exception {
		return new String(decrypt(encryptedBytes, keyBytes, null, null), (charset == null ? StandardCharsets.UTF_8 : charset).name());
	}
	
	/**
	 * @Title: decryptFromHex
	 * @Description: AES解密
	 * @param encryptedBytes 需解密字数据
	 * @param keyBytes 密钥
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes)
			throws Exception {
		return decrypt(encryptedBytes, keyBytes, null, null);
	}
	
	/**
	 * @Title: decryptFromBase64ToString
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @throws Exception 
	 */
	public static String decryptFromBase64ToString(String encryptedStr, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, Charset charset)
			throws Exception {
		return new String(decrypt(Base64Utils.decode(encryptedStr), keyBytes, transformationAlgorithm, null), (charset == null ? StandardCharsets.UTF_8 : charset).name());
	}
	
	/**
	 * @Title: decryptFromBase64
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @throws Exception 
	 */
	public static byte[] decryptFromBase64(String encryptedStr, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return decrypt(Base64Utils.decode(encryptedStr), keyBytes, transformationAlgorithm, null);
	}
	
	/**
	 * @Title: decryptFromHexToString
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @throws Exception 
	 */
	public static String decryptFromHexToString(String encryptedStr, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, Charset charset)
			throws Exception {
		return new String(decrypt(HexUtils.toBinary(encryptedStr), keyBytes, transformationAlgorithm, null), (charset == null ? StandardCharsets.UTF_8 : charset).name());
	}
	
	/**
	 * @Title: decryptFromHex
	 * @Description: AES解密
	 * @param encryptedStr 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @throws Exception 
	 */
	public static byte[] decryptFromHex(String encryptedStr, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return decrypt(HexUtils.toBinary(encryptedStr), keyBytes, transformationAlgorithm, null);
	}
	
	/**
	 * @Title: decryptToString
	 * @Description: AES解密
	 * @param encryptedBytes 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @throws Exception 
	 */
	public static String decryptToString(byte[] encryptedBytes, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, Charset charset)
			throws Exception {
		return new String(decrypt(encryptedBytes, keyBytes, transformationAlgorithm, null), (charset == null ? StandardCharsets.UTF_8 : charset).name());
	}
	
	/**
	 * @Title: decrypt
	 * @Description: AES解密
	 * @param encryptedBytes 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return decrypt(encryptedBytes, keyBytes, transformationAlgorithm, null);
	}
	
	/**
	 * @Title: decrypt
	 * @Description: AES解密
	 * @param encryptedBytes 需解密字数据
	 * @param keyBytes 密钥
	 * @param transformationAlgorithm 算法模式
	 * @param iv 偏移量
	 * @return byte[] 解密后的数据
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, byte[] iv)
			throws Exception {
		SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
		if (transformationAlgorithm == null) {
			transformationAlgorithm = AESTransformationAlgorithm.AES_CBC_PKCS5Padding;
		}
		// 创建密码器，它用于完成实际的加密操作（//算法/模式/填充。默认："AES/ECB/PKCS5Padding"）
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		if (iv == null) {
			iv = DEFAULT_IV;
		}
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return cipher.doFinal(encryptedBytes);
	}
	
}