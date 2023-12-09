package com.wendaoren.utils.crypto;

import com.wendaoren.utils.constant.ProviderConstant;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author lujiafa
 * @date 2016年8月11日
 * @Description: RSA工具类
 */
public final class RSAUtils {
	
	/**
	 * 密钥生成器算法
	 */
	public final static String KEY_ALGORITHM = "RSA";
	
	/**
	 * @date 2016年8月11日
	 * @Description key size
	 */
	public enum RSAKeySize {

		/** 
		 * RSA分段加密单段(block)最大长度值为117，由Cipher.doFinal(byte[])限制
		 * RSA分段解密单段(block)最大长度值为128，由RSACipher.doFinal(byte[])限制
		**/
		_1024(1024),
		
		/** 
		 * RSA分段加密单段(block)最大长度值为245，由Cipher.doFinal(byte[])限制
		 * RSA分段解密单段(block)最大长度值为256，由RSACipher.doFinal(byte[])限制
		 **/
		_2048(2048);
		
		private int keySize;

		private RSAKeySize(int keySize) {
			this.keySize = keySize;
		}

		public int getKeySize() {
			return keySize;
		}
	}
	
	/**
	 * @date 2016年8月11日
	 * @Description 密码转换器算法（算法/模式/填充）
	 */
	public enum RSATransformationAlgorithm {
		RSA_ECB_NOPADDING("RSA/ECB/NoPadding", ProviderConstant.PROVIDER_BOUNCY_CASTLE),
		RSA_ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding", ProviderConstant.PROVIDER_BOUNCY_CASTLE),
		RSA_NONE_NOPADDING("RSA/NONE/NoPadding", ProviderConstant.PROVIDER_BOUNCY_CASTLE),
		RSA_NONE_PKCS1PADDING("RSA/NONE/PKCS1Padding", ProviderConstant.PROVIDER_BOUNCY_CASTLE)
		;
		
		private String transFormationAlgorithm;
		private Provider provider;
		
		private RSATransformationAlgorithm(String transFormationAlgorithm, BouncyCastleProvider provider) {
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
	 * @date 2016年8月11日
	 * @Description 签名加密算法类型
	 */
	public enum RSASignAlgorithm {
		MD5_WITH_RSA("MD5withRSA"),
		SHA1_WITH_RSA("SHA1withRSA"),
		SHA256_WITH_RSA("SHA256withRSA");
		
		private String algorithm;

		private RSASignAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		public String getAlgorithm() {
			return algorithm;
		}
	}
	
	/**
	 * @Title: encryptByPrivateKey
	 * @Description: 数据RSA通过私钥加密
	 * @param data 需加密数据
	 * @param privateKey RSA私钥
	 * @return String 加密字符串
	 */
	public static String encryptByPrivateKey(byte[] data, String privateKey) throws Exception{
		return encryptByPrivateKey(data, privateKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
	}
	
	/**
	 * @Title: encryptByPrivateKey
	 * @Description: 数据RSA通过私钥加密
	 * @param data 需加密数据
	 * @param privateKey RSA私钥
	 * @param transformationAlgorithm 算法模式
	 * @return String 加密字符串
	 */
	public static String encryptByPrivateKey(byte[] data, String privateKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception{
		byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int dataLen = data.length;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int blockSize = cipher.getBlockSize();
		int offSet = 0;
		byte[] b;
		// 对数据分段加密
		while (dataLen - offSet > 0) {
			if (dataLen - offSet > blockSize) {
				b = cipher.doFinal(data, offSet, blockSize);
				offSet += blockSize;
			} else {
				b = cipher.doFinal(data, offSet, dataLen - offSet);
				offSet = dataLen;
			}
			baos.write(b, 0, b.length);
		}
		byte[] encryptedData = baos.toByteArray();
		return Base64Utils.encode(encryptedData);
    }
	
	/**
	 * @Title: encryptByPublicKey
	 * @Description: 数据RSA通过公钥加密
	 * @param data 需加密数据
	 * @param publicKey RSA公钥
	 * @return String 加密字符串
	 */
	public static String encryptByPublicKey(byte[] data, String publicKey) throws Exception{
		return encryptByPublicKey(data, publicKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
	}
	
	/**
	 * @Title: encryptByPublicKey
	 * @Description: 数据RSA通过公钥加密
	 * @param data 需加密数据
	 * @param publicKey RSA公钥
	 * @param transformationAlgorithm 算法模式
	 * @return String 加密字符串
	 */
    public static String encryptByPublicKey(byte[] data, String publicKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception{
        byte[] keyBytes = Base64Utils.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int dataLen = data.length;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int blockSize = cipher.getBlockSize();
		int offSet = 0;
		byte[] b;
		System.out.println(cipher.getBlockSize());
		// 对数据分段加密
		while (dataLen - offSet > 0) {
			if (dataLen - offSet > blockSize) {
				b = cipher.doFinal(data, offSet, blockSize);
				offSet += blockSize;
			} else {
				b = cipher.doFinal(data, offSet, dataLen - offSet);
				offSet = dataLen;
			}
			baos.write(b, 0, b.length);
		}
		byte[] encryptedData = baos.toByteArray();
		return Base64Utils.encode(encryptedData);
    }
    
    /**
     * @Title: decryptByPrivateKey
     * @Description: RSA加密字符串通过私钥解密
     * @param encryptedStr 已加密字符串
     * @param privateKey 解密私钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(String encryptedStr, String privateKey) throws Exception{
    	return decryptByPrivateKey(encryptedStr, privateKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
    }
    
    /**
     * @Title: decryptByPrivateKey
     * @Description: RSA加密字符串通过私钥解密
     * @param encryptedStr 已加密字符串
     * @param privateKey 解密私钥
	 * @param transformationAlgorithm 算法模式
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(String encryptedStr, String privateKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception{
		byte[] encryptedBytes = Base64Utils.decode(encryptedStr);
		byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int dataLen = encryptedBytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int blockSize = cipher.getBlockSize();
		int offSet = 0;
		byte[] cache;
		System.out.println(cipher.getBlockSize());
		// 对数据分段解密
		while (dataLen - offSet > 0) {
			if (dataLen - offSet > blockSize) {
				cache = cipher.doFinal(encryptedBytes, offSet, blockSize);
				offSet += blockSize;
			} else {
				cache = cipher.doFinal(encryptedBytes, offSet, dataLen - offSet);
				offSet = dataLen;
			}
			out.write(cache, 0, cache.length);
		}
		byte[] decryptedData = out.toByteArray();
		return decryptedData;
    }
    
    /**
     * @Title: decryptByPublicKey
     * @Description: RSA加密字符串通过公钥解密
     * @param encryptedStr 已加密字符串
     * @param publicKey 解密公钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(String encryptedStr, String publicKey) throws Exception {
    	return decryptByPublicKey(encryptedStr, publicKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
    }
	
	/**
	 * @Title: decryptByPublicKey
	 * @Description: RSA加密字符串通过公钥解密
	 * @param encryptedStr 已加密字符串
	 * @param publicKey 解密公钥
	 * @param transformationAlgorithm 算法模式
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPublicKey(String encryptedStr, String publicKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception {
		byte[] encryptedBytes = Base64Utils.decode(encryptedStr);
		byte[] keyBytes = Base64Utils.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = null;
		if (transformationAlgorithm.getProvider() == null) {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm());
		} else {
			cipher = Cipher.getInstance(transformationAlgorithm.getTransFormationAlgorithm(), transformationAlgorithm.getProvider());
		}
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int dataLen = encryptedBytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int blockSize = cipher.getBlockSize();
		int offSet = 0;
		byte[] cache;
		// 对数据分段解密
		while (dataLen - offSet > 0) {
			if (dataLen - offSet > blockSize) {
				cache = cipher.doFinal(encryptedBytes, offSet, blockSize);
				offSet += blockSize;
			} else {
				cache = cipher.doFinal(encryptedBytes, offSet, dataLen - offSet);
				offSet = dataLen;
			}
			out.write(cache, 0, cache.length);
		}
		byte[] decryptedData = out.toByteArray();
		return decryptedData;
	}
	
	/**
	 * @Title: signMD5WithRSA
	 * @Description: 生成数据签名
	 * @param data 需签名的数据
	 * @param privateKey 加密签名数据私钥
	 * @return String 签名数据
	 */
	public static String signMD5WithRSA(byte[] data, String privateKey) throws Exception {
		return sign(data, privateKey, RSASignAlgorithm.MD5_WITH_RSA);
	}
	
	/**
	 * @Title: signSHA1WithRSA
	 * @Description: 生成数据签名
	 * @param data 需签名的数据
	 * @param privateKey 加密签名数据私钥
	 * @return String 签名数据
	 */
	public static String signSHA1WithRSA(byte[] data, String privateKey) throws Exception {
		return sign(data, privateKey, RSASignAlgorithm.SHA1_WITH_RSA);
	}
	
	/**
	 * @Title: signSHA256WithRSA
	 * @Description: 生成数据签名
	 * @param data 需签名的数据
	 * @param privateKey 加密签名数据私钥
	 * @return String 签名数据
	 */
	public static String signSHA256WithRSA(byte[] data, String privateKey) throws Exception {
		return sign(data, privateKey, RSASignAlgorithm.SHA256_WITH_RSA);
	}
	
	/**
	 * @Title: sign
	 * @Description: 生成数据签名
	 * @param data 需签名的数据
	 * @param privateKey 加密签名数据私钥
	 * @return String 签名数据
	 */
    private static String sign(byte[] data, String privateKey, RSASignAlgorithm algorithm) throws Exception {
        // 私钥字符串转字节数组
        byte[] keyBytes = Base64Utils.decode(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(algorithm.getAlgorithm());
        signature.initSign(priKey);
        signature.update(data);
        byte[] signByte = signature.sign();
        return Base64Utils.encode(signByte);
    }
	
	/**
	 * @Title:signVerifyMD5WithRSA
	 * @Description: 签名验证
	 * @param signSrc 需签名的数据
	 * @param publicKey 验证签名数据公钥
	 * @param sign 签名数据
	 * @return boolean true-验证成功 false-验证失败
	 */
	public static boolean signVerifyMD5WithRSA(byte[] signSrc, String publicKey, String sign) throws Exception {
		return signVerify(signSrc, publicKey, sign, RSASignAlgorithm.MD5_WITH_RSA);
	}
	
	/**
	 * @Title: signVerifySHA1WithRSA
	 * @Description: 签名验证
	 * @param signSrc 需签名的数据
	 * @param publicKey 验证签名数据公钥
	 * @param sign 签名数据
	 * @return boolean true-验证成功 false-验证失败
	 */
	public static boolean signVerifySHA1WithRSA(byte[] signSrc, String publicKey, String sign) throws Exception {
		return signVerify(signSrc, publicKey, sign, RSASignAlgorithm.SHA1_WITH_RSA);
	}
	
	/**
	 * @Title:signVerifySHA256WithRSA
	 * @Description: 签名验证
	 * @param signSrc 需签名的数据
	 * @param publicKey 验证签名数据公钥
	 * @param sign 签名数据
	 * @return boolean true-验证成功 false-验证失败
	 */
	public static boolean signVerifySHA256WithRSA(byte[] signSrc, String publicKey, String sign) throws Exception {
		return signVerify(signSrc, publicKey, sign, RSASignAlgorithm.SHA256_WITH_RSA);
	}
    
    /**
     * @Title:signVerify
     * @Description: 签名验证
     * @param signSrc 需签名的数据
     * @param publicKey 验证签名数据公钥
     * @param sign 签名数据
     * @return boolean true-验证成功 false-验证失败
     */
    private static boolean signVerify(byte[] signSrc, String publicKey, String sign, RSASignAlgorithm algorithm) throws Exception {
    	// 公钥字符串转字节数组
    	byte[] keyBytes = Base64Utils.decode(publicKey);
    	// 构造X509EncodedKeySpec对象  
    	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    	// KEY_ALGORITHM 指定的加密算法  
    	KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    	// 取公钥匙对象  
    	PublicKey publicKey_ = keyFactory.generatePublic(keySpec);
    	Signature signature = Signature.getInstance(algorithm.getAlgorithm());
    	signature.initVerify(publicKey_);
    	signature.update(signSrc);
    	// 验证签名
    	return signature.verify(Base64Utils.decode(sign));
    }
    
    /**
     * @Title:genKeyPair
     * @Description: 生成密钥对(公钥和私钥)
     * @return RSAKeyPair 密钥对
     */
    public static RSAKeyPair genKeyPair() {
    	return genKeyPair(RSAKeySize._1024);
    }
	
    /**
     * @Title:genKeyPair
     * @Description: 生成密钥对(公钥和私钥)
     * @param keySize key数据长度类型
     * @return RSAKeyPair 密钥对
     */
    public static RSAKeyPair genKeyPair(RSAKeySize keySize) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(keySize.getKeySize());
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPublicKey publicKey_ = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey_ = (RSAPrivateKey) keyPair.getPrivate();
			String publicKey = Base64Utils.encode(publicKey_.getEncoded());
			String privateKey = Base64Utils.encode(privateKey_.getEncoded());
			String modulus = String.valueOf(publicKey_.getModulus());
			return new RSAKeyPair(publicKey, privateKey, modulus);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
     * @author lujiafa
     * @date 2016年8月11日
     * @Description: RSA公/私钥对
     */
    public static class RSAKeyPair {
    	/** RSA公钥 **/
    	private String publicKey;
    	/** RSA私钥 **/
    	private String privateKey;
    	/** 模 **/
    	private String modulus;
    	
		public RSAKeyPair(String publicKey, String privateKey, String modulus) {
			super();
			this.publicKey = publicKey;
			this.privateKey = privateKey;
			this.modulus = modulus;
		}

		public String getPublicKey() {
			return publicKey;
		}

		public String getPrivateKey() {
			return privateKey;
		}

		public String getModulus() {
			return modulus;
		}
    }
    
}