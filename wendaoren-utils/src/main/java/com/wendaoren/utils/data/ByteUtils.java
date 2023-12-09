package com.wendaoren.utils.data;

/**
 * @author lujiafa
 * @date 2014年7月24日
 * @Description 二进制工具类
 */
public final class ByteUtils {

	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

	/**
	 * @Title  byte2Hex
	 * @Description 将byte数组转为十六进制字符串
	 * @param data 字节数组
	 * @return String 十六进制字符串
	 */
	public static String toHex(byte[] data) {
		if (data == null) {
			data = new byte[0];
		}
		StringBuilder sb = new StringBuilder(data.length * 2);
		for (byte b : data) {
			sb.append(hexCode[(b >> 4) & 0xF]);
			sb.append(hexCode[(b & 0xF)]);
		}
		return sb.toString();
	}
	
	/**
	 * @Title toDec
	 * @Description byte数组转十进制数（二进制转10进制）
	 * @param data
	 */
	public static long toDec(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("byte array cannot be null or empty");
		}
		switch (data.length) {
			case 1: return data[0];
			case 2: return toDecShort(data);
			case 4: return toDecInt(data);
			case 8: return toDecLong(data);
			default:
				throw new IllegalArgumentException("byte array conversion length does not match");
		}
	}
	
	/**
	 * @Title toDecShort
	 * @Description byte数组转十进制数（二进制转10进制）
	 * @param data
	 * @return short
	 */
	public static short toDecShort(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("byte array cannot be null or empty");
		}
		if (data.length != 2) {
			throw new IllegalArgumentException("byte array conversion length does not match");
		}
		short r = 0;
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				r = (short) (r << 8);
			}
			r |= (data[i] & 0xFF);
		}
		return r;
	}
	
	/**
	 * @Title toDecInt
	 * @Description byte数组转十进制数（二进制转10进制）
	 * @param data
	 * @return int
	 */
	public static int toDecInt(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("byte array cannot be null or empty");
		}
		if (data.length > 4) {
			throw new IllegalArgumentException("byte array conversion length does not match");
		}
		int r = 0;
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				r = r << 8;
			}
			r |= (data[i] & 0xFF);
		}
		return r;
	}
	
	/**
	 * @Title toDecLong
	 * @Description byte数组转长整型十进制数（二进制转10进制）
	 * @param data
	 * @return long
	 */
	public static long toDecLong(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("byte array cannot be null or empty");
		}
		if (data.length > 8) {
			throw new IllegalArgumentException("byte array conversion length does not match");
		}
		long r = 0;
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				r = r << 8;
			}
			r |= (data[i] & 0xFF);
		}
		return r;
	}
	
}