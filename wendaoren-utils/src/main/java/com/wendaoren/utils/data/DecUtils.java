package com.wendaoren.utils.data;

/**
 * @author lujiafa
 * @email lujiafayx@163.com
 * @date 2017年3月5日
 * @Description: 十进制数转换工具类
 */
public class DecUtils {
	
	/**
	 * @Title toBinary
	 * @Description 十进制数转byte数组（十进制转二进制）
	 * @param num
	 * @return byte[]
	 */
	public static byte[] toBinary(short num) {
		return new byte[] {
	        (byte) ((num >> 8) & 0xFF),
	        (byte) (num & 0xFF)
	    };
	}
	
	/**
	 * @Title toBinary
	 * @Description 十进制数转byte数组（十进制转二进制）
	 * @param num
	 * @return byte[]
	 */
	public static byte[] toBinary(int num) {
		return new byte[] {
				(byte) ((num >> 24) & 0xFF),
				(byte) ((num >> 16) & 0xFF),
				(byte) ((num >> 8) & 0xFF),
				(byte) (num & 0xFF)
		};
	}
	
	/**
	 * @Title toBinary
	 * @Description 十进制数转byte数组（十进制转二进制）
	 * @param num
	 * @return byte[]
	 */
	public static byte[] toBinary(long num) {
		return new byte[] {
	        (byte) ((num >> 56) & 0xFF),
	        (byte) ((num >> 48) & 0xFF),
	        (byte) ((num >> 40) & 0xFF),
	        (byte) ((num >> 32) & 0xFF),
	        (byte) ((num >> 24) & 0xFF),
	        (byte) ((num >> 16) & 0xFF),
	        (byte) ((num >> 8) & 0xFF),
	        (byte) (num & 0xFF)
	    };
	}
	
	/**
	 * @Title toHex
	 * @Description 十进制数转16进制字符串
	 * @param num
	 * @return String
	 */
	public static String toHex(int num) {
		return ByteUtils.toHex(toBinary(num));
	}
	
	/**
	 * @Title toHex
	 * @Description 十进制数转16进制字符串
	 * @param num
	 * @return String
	 */
	public static String toHex(long num) {
		return ByteUtils.toHex(toBinary(num));
	}

}