package com.xdx.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtils {
	/**
	 * 加密盐
	 */
	private String salt;

	private PasswordUtils() {
		this.salt = "";
	}

	private PasswordUtils(String salt) {
		this.salt = salt;
	}

	public static PasswordUtils salt() {
		return new PasswordUtils();
	}

	public static PasswordUtils salt(String salt) {
		return new PasswordUtils(salt);
	}

	/**
	 * 密码加密
	 * 
	 * @param rawPass
	 * @return
	 * @author 苦酒
	 * @date 2018年5月15日
	 * @version 1.0
	 */
	public String encode(String rawPass) {
		String pwd = rawPass + salt;
		return DigestUtils.sha1Hex(pwd);
	}

	/**
	 * 密码验证
	 * @param pwd 
	 * @param oldPwd 
	 * @return
	 * @author 苦酒
	 * @date 2018年5月15日
	 * @version 1.0
	 */
	public boolean valid(String pwd, String oldPwd) {
		return oldPwd.equals(encode(pwd));
	}
}
