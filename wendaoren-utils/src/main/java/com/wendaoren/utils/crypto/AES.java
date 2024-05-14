package com.wendaoren.utils.crypto;

import com.wendaoren.utils.constant.ProviderConstant;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;

/**
 * @author lujiafa
 * @date 2016年8月15日
 * @Description: AES加密/解密工具类
 */
public final class AES {
	
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
	 /**
	 * encrypt - AES加密 <br>
	 *   采用默认加密算法 AES/CBC/PKCS5Padding
	 *   采用默认偏移量 IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} <br>
	 * @param data 需加密字数据【M】
	 * @param keyBytes 密钥【M】
	 * @return byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static byte[] encrypt(byte[] data, byte[] keyBytes)
			throws Exception {
		return encrypt(data, keyBytes, AESTransformationAlgorithm.AES_CBC_PKCS5Padding, null);
	}
	
	/**
	 * encrypt - AES加密 <br>
	 *   采用默认偏移量 IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	 * @param data 需加密字数据【M】
	 * @param keyBytes 密钥【M】
	 * @param transformationAlgorithm 算法模式【M】
	 * @return byte[] 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return encrypt(data, keyBytes, transformationAlgorithm, DEFAULT_IV);
	}
	
	/**
	 * encrypt - AES加密 <br>
	 * @Description: AES加密
	 * @param data 需加密字数据【M】
	 * @param keyBytes 密钥【M】
	 * @param transformationAlgorithm 算法模式【M】
	 * @param iv 偏移量【M】
	 * @return  byte[] 加密后的数据
	 * @throws Exception 
	 */
	public static byte[] encrypt(byte[] data, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, byte[] iv)
			throws Exception {
		SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
		// 创建密码器，它用于完成实际的加密操作（//算法/模式/填充。默认："AES/ECB/PKCS5Padding"）
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		IvParameterSpec spec = new IvParameterSpec(iv);
		// 初始化Cipher对象，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
		byte[] doFinal = cipher.doFinal(data);
		return doFinal;
	}
	
	/**
	 * decrypt - 解密 <br>
	 *   采用默认解密算法 AES/CBC/PKCS5Padding
	 *   采用默认偏移量 IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} <br>
	 * @param encryptedBytes 需解密字数据【M】
	 * @param keyBytes 密钥【M】
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes)
			throws Exception {
		return decrypt(encryptedBytes, keyBytes, AESTransformationAlgorithm.AES_CBC_PKCS5Padding, DEFAULT_IV);
	}
	

	/**
	 * decrypt - 解密 <br>
	 *   采用默认偏移量 IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} <br>
	 * @param encryptedBytes 需解密字数据【M】
	 * @param keyBytes 密钥【M】
	 * @param transformationAlgorithm 算法模式【M】
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm)
			throws Exception {
		return decrypt(encryptedBytes, keyBytes, transformationAlgorithm, DEFAULT_IV);
	}
	
	/**
	 * decrypt - 解密 <br>
	 * @param encryptedBytes 需解密字数据【M】
	 * @param keyBytes 密钥【M】
	 * @param transformationAlgorithm 算法模式【M】
	 * @param iv 偏移量【M】
	 * @return byte[] 解密后的数据
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, AESTransformationAlgorithm transformationAlgorithm, byte[] iv)
			throws Exception {
		SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
		// 创建密码器，它用于完成实际的加密操作（//算法/模式/填充。默认："AES/ECB/PKCS5Padding"）
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return cipher.doFinal(encryptedBytes);
	}
	
}