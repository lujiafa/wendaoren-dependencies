package com.wendaoren.utils.data;

/**
 * @author lujiafa
 * @email lujiafayx@163.com
 * @date 2019年3月5日
 * @Description: 十六进制数转换工具类
 */
public final class HexUtils {
	
	/**
	 * @Title toBinary
	 * @Description 将十六进制字符串转为byte数组
	 * @param hexStr 十六进制字符串
	 * @return byte[]
	 */
	public static byte[] toBinary(String hexStr) {
		if (hexStr == null) {
			hexStr = "";
		} else if (hexStr.length() % 2 != 0) {
			hexStr = "0" + hexStr;
		}
		char[] charArray = hexStr.toCharArray();
		int len = charArray.length;
		byte[] out = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			int h = hexToDec(charArray[i]);
			int l = hexToDec(charArray[i + 1]);
			if (h == -1 || l == -1) {
				throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexStr);
			}
			out[i / 2] = (byte) (h << 4 | l);
		}
		return out;
	}
	
	/**
	 * @Title hex2Dec
	 * @Description 16进制字符串转十进制数
	 * @param hexStr
	 * @return long
	 */
	public static long toDec(String hexStr) {
		if (hexStr == null || hexStr.length() == 0) {
			hexStr = "00";
		}
		return ByteUtils.toDec(toBinary(hexStr));
	}
	
	/**
	 * @Title hexToAscii
	 * @Description 十六进制字符转10进制数值
	 * @param ch 十六进制字符
	 * @return int ascii数值
	 */
	private static int hexToDec(char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		}
		if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		}
		return -1;
	}

}