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
public final class RSA {
	
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
	 * encryptByPrivateKey - RSA私钥加密 <br>
	 * 算法默认采用“RSA/ECB/PKCS1Padding”模式
	 * @param data 需加密数据【M】
	 * @param privateKey RSA私钥【M】
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception{
		return encryptByPrivateKey(data, privateKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
	}
	
	/**
	 * encryptByPrivateKey - RSA私钥加密 <br>
	 * @param data 需加密数据【M】
	 * @param privateKey RSA私钥【M】
	 * @param transformationAlgorithm 算法模式【M】
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception{
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
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
		return encryptedData;
    }
	
	/**
	 * encryptByPublicKey - RSA公钥加密 <br>
	 * 算法默认采用“RSA/ECB/PKCS1Padding”模式
	 * @param data 需加密数据【M】
	 * @param publicKey RSA私钥【M】
	 * @return byte[] 加密数据
	 *
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception{
		return encryptByPublicKey(data, publicKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
	}
	
	/**
	 * encryptByPublicKey - RSA公钥加密 <br>
	 * @param data 需加密数据【M】
	 * @param publicKey RSA私钥【M】
	 * @param transformationAlgorithm 加密算法【M】
	 * @return byte[] 加密数据
	 */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception{
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
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
		return encryptedData;
    }
    
    /**
	 * decryptByPrivateKey - RSA私钥解密 <br>
	 * 默认解密算法为 “RSA/ECB/PKCS1Padding”
     * @param encryptedBytes 已加密数据【M】
     * @param privateKey 解密私钥【M】
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedBytes, byte[] privateKey) throws Exception{
    	return decryptByPrivateKey(encryptedBytes, privateKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
    }
    
    /**
	 * decryptByPrivateKey - RSA私钥解密 <br>
	 * @param encryptedBytes 已加密数据【M】
	 * @param privateKey 解密私钥【M】
	 * @param transformationAlgorithm 算法模式【M】
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedBytes, byte[] privateKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception{
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
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
	 * decryptByPublicKey - RSA公钥解密 <br>
	 * 默认解密算法为 “RSA/ECB/PKCS1Padding”
	 * @param encryptedBytes 已加密数据【M】
	 * @param publicKey 解密私钥【M】
	 * @return byte[] 解密数据
	 */
    public static byte[] decryptByPublicKey(byte[] encryptedBytes, byte[] publicKey) throws Exception {
    	return decryptByPublicKey(encryptedBytes, publicKey, RSATransformationAlgorithm.RSA_ECB_PKCS1PADDING);
    }
	
	/**
	 * decryptByPublicKey - RSA公钥解密 <br>
	 * @param encryptedBytes 已加密数据【M】
	 * @param publicKey 解密私钥【M】
	 * @param transformationAlgorithm 算法模式【M】
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedBytes, byte[] publicKey, RSATransformationAlgorithm transformationAlgorithm) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
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
	 * signMD5WithRSA - 私钥生成数据签名 <br>
	 * 签名算法为 “MD5withRSA”
	 * @param data 需签名的数据【M】
	 * @param privateKey 加密签名数据私钥【M】
	 * @return String 签名数据
	 */
	public static byte[] signMD5WithRSA(byte[] data, byte[] privateKey) throws Exception {
		return sign(data, privateKey, RSASignAlgorithm.MD5_WITH_RSA);
	}
	
	/**
	 * signSHA1WithRSA - 私钥生成数据签名 <br>
	 * 签名算法为 “SHA1withRSA”
	 * @param data 需签名的数据【M】
	 * @param privateKey 加密签名数据私钥【M】
	 * @return String 签名数据
	 */
	public static byte[] signSHA1WithRSA(byte[] data, byte[] privateKey) throws Exception {
		return sign(data, privateKey, RSASignAlgorithm.SHA1_WITH_RSA);
	}
	
	/**
	 * signSHA256WithRSA - 私钥生成数据签名 <br>
	 * 签名算法为 “SHA256withRSA”
	 * @param data 需签名的数据【M】
	 * @param privateKey 加密签名数据私钥【M】
	 * @return String 签名数据
	 */
	public static byte[] signSHA256WithRSA(byte[] data, byte[] privateKey) throws Exception {
		return sign(data, privateKey, RSASignAlgorithm.SHA256_WITH_RSA);
	}
	
	/**
	 * sign - 私钥生成数据签名 <br>
	 * @param data 需签名的数据【M】
	 * @param privateKey 加密签名数据私钥【M】
	 * @param algorithm 签名算法【M】
	 * @return String 签名数据
	 */
    private static byte[] sign(byte[] data, byte[] privateKey, RSASignAlgorithm algorithm) throws Exception {
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(algorithm.getAlgorithm());
        signature.initSign(priKey);
        signature.update(data);
        byte[] signByte = signature.sign();
        return signByte;
    }
	
	/**
	 * signVerifyMD5WithRSA - 公钥验证签名 <br>
	 * 签名算法为 “MD5withRSA”
	 * @param signSrc 需验证签名的数据【M】
	 * @param publicKey 加密签名数据私钥【M】
	 * @param sign 签名数据【M】
	 * @return boolean true-验证成功 false-验证失败
	 */
	public static boolean signVerifyMD5WithRSA(byte[] signSrc, byte[] publicKey, byte[] sign) throws Exception {
		return signVerify(signSrc, publicKey, sign, RSASignAlgorithm.MD5_WITH_RSA);
	}
	
	/**
	 * signVerifySHA1WithRSA - 公钥验证签名 <br>
	 * 签名算法为 “SHA1withRSA”
	 * @param signSrc 需验证签名的数据【M】
	 * @param publicKey 加密签名数据私钥【M】
	 * @param sign 签名数据【M】
	 * @return boolean true-验证成功 false-验证失败
	 */
	public static boolean signVerifySHA1WithRSA(byte[] signSrc, byte[] publicKey, byte[] sign) throws Exception {
		return signVerify(signSrc, publicKey, sign, RSASignAlgorithm.SHA1_WITH_RSA);
	}
	
	/**
	 * signVerifySHA256WithRSA - 公钥验证签名 <br>
	 * 签名算法为 “SHA1withRSA”
	 * @param signSrc 需验证签名的数据【M】
	 * @param publicKey 加密签名数据私钥【M】
	 * @param sign 签名数据【M】
	 * @return boolean true-验证成功 false-验证失败
	 */
	public static boolean signVerifySHA256WithRSA(byte[] signSrc, byte[] publicKey, byte[] sign) throws Exception {
		return signVerify(signSrc, publicKey, sign, RSASignAlgorithm.SHA256_WITH_RSA);
	}
    
    /**
	 * signVerify - 公钥验证签名 <br>
	 * 签名算法为 “SHA1withRSA”
	 * @param signSrc 需验证签名的数据【M】
	 * @param publicKey 加密签名数据私钥【M】
	 * @param sign 签名数据【M】
	 * @param algorithm 签名验证算法【M】
     * @return boolean true-验证成功 false-验证失败
     */
    private static boolean signVerify(byte[] signSrc, byte[] publicKey, byte[] sign, RSASignAlgorithm algorithm) throws Exception {
    	// 构造X509EncodedKeySpec对象
    	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
    	// KEY_ALGORITHM 指定的加密算法  
    	KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    	// 取公钥匙对象  
    	PublicKey publicKey_ = keyFactory.generatePublic(keySpec);
    	Signature signature = Signature.getInstance(algorithm.getAlgorithm());
    	signature.initVerify(publicKey_);
    	signature.update(signSrc);
    	// 验证签名
    	return signature.verify(sign);
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
			String publicKey = Base64.encode(publicKey_.getEncoded());
			String privateKey = Base64.encode(privateKey_.getEncoded());
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