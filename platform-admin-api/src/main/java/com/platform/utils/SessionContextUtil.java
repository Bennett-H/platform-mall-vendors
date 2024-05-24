package com.platform.utils;

import com.platform.common.session.SessionData;

/**
 * 保存用户登录信息
 */
public class SessionContextUtil {

	/** 保存当前的用户名称 */
	private static ThreadLocal<String>  shopsIdHolder = new ThreadLocal<String>();

	/** 保存当前的用户ID */
	private static ThreadLocal<String>  userIdHolder = new ThreadLocal<String>();

	/** 用户名称 */
	private static ThreadLocal<String>  WxOpenIdHolder = new ThreadLocal<String>();




	/**
	 * 设置用户ID
	 * 
	 * @param userId
	 */
	public static void setUserId(String userId) {
		userIdHolder.set(userId);
	}

	/**
	 * 设置用户登录名
	 * 
	 * @param shopsId
	 */
	public static void setShopsId(String shopsId) {
		shopsIdHolder.set(shopsId);
	}

	/**
	 * 设置用户名称
	 * 
	 * @param openId
	 */
	public static void setWxOpenId(String openId) {
		WxOpenIdHolder.set(openId);
	}




	public static String getUserId() {
		return userIdHolder.get();
	}

	/**
	 * 设置用户登录名
	 *
	 * @param
	 */
	public static String getShopsId() {
		return shopsIdHolder.get();
	}

	/**
	 * 设置用户名称
	 *
	 * @param
	 */
	public static String getWxOpenId() {
		return WxOpenIdHolder.get();
	}





	public static void setSessionData(SessionData sessionData){
		userIdHolder.set(sessionData.getUserId());
		//shopsIdHolder.set(sessionData.getShopsId());
		WxOpenIdHolder.set(sessionData.getWechatOpenId());
	}

	/**
	 * 清空所有数据
	 */
	public static void clear() {
		userIdHolder.set(null);
		shopsIdHolder.set(null);
		WxOpenIdHolder.set(null);
	}
}
