package edu.xidian.pnaWeb.web.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description
 * @Author He
 * @Date 2022/1/17 19:34
 */
@Slf4j
public class Util {
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
				// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress="";
		}
		// ipAddress = this.getRequest().getRemoteAddr();

		return ipAddress;
	}
	// 编码密码,可自定义
	private static final String ENCODED_PASSWORD = "codey";

	/**
	 * 编码
	 *
	 * @param str
	 * @return
	 */
	public static String encoded(String str) {
		return strToHex(encodedString(str, ENCODED_PASSWORD));
	}

	/**
	 * 转换
	 *
	 * @param str
	 * @param password
	 * @return
	 */
	private static String encodedString(String str, String password) {
		char[] pwd = password.toCharArray();
		int pwdLen = pwd.length;

		char[] strArray = str.toCharArray();
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = (char) (strArray[i] ^ pwd[i % pwdLen] ^ pwdLen);
		}
		return new String(strArray);
	}

	private static String strToHex(String s) {
		return bytesToHexStr(s.getBytes());
	}

	private static String bytesToHexStr(byte[] bytesArray) {
		StringBuilder builder = new StringBuilder();
		String hexStr;
		for (byte bt : bytesArray) {
			hexStr = Integer.toHexString(bt & 0xFF);
			if (hexStr.length() == 1) {
				builder.append("0");
				builder.append(hexStr);
			} else {
				builder.append(hexStr);
			}
		}
		return builder.toString();
	}

	/**
	 * 解码
	 *
	 * @param str
	 * @return
	 */
	public static String decoded(String str) {
		String hexStr = null;
		try {
			hexStr = hexStrToStr(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (hexStr != null) {
			hexStr = encodedString(hexStr, ENCODED_PASSWORD);
		}
		return hexStr;
	}

	private static String hexStrToStr(String hexStr) {
		return new String(hexStrToBytes(hexStr));
	}

	private static byte[] hexStrToBytes(String hexStr) {
		String hex;
		int val;
		byte[] btHexStr = new byte[hexStr.length() / 2];
		for (int i = 0; i < btHexStr.length; i++) {
			hex = hexStr.substring(2 * i, 2 * i + 2);
			val = Integer.valueOf(hex, 16);
			btHexStr[i] = (byte) val;
		}
		return btHexStr;
	}
}
