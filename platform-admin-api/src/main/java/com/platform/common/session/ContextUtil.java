package com.platform.common.session;

/**
 * 保存用户登录信息
 */
public class ContextUtil {

	/** 保存当前的用户名称 */
	private static ThreadLocal<String> userNameHolder = new ThreadLocal<String>();

	/** 保存当前的用户ID */
	private static ThreadLocal<Integer> userIdHolder = new ThreadLocal<Integer>();

	/** 用户名称 */
	private static ThreadLocal<String> nickNameHolder = new ThreadLocal<String>();

	/** 专区组 **/
	private static ThreadLocal<Integer> PrefectureIdHolder = new ThreadLocal<Integer>();

	/** 专区组 **/
	private static ThreadLocal<String> AccessTokenHolder = new ThreadLocal<String>();

	/** 微信组 **/
	private static ThreadLocal<String> wechatUserIdHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> wechatOpenIdHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> roleNameHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> roleCodeHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> dealerCodeHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> dealerNameHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> ifNetSalesCodeHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> orgIdHolder = new ThreadLocal<String>();

	private static ThreadLocal<String> orgCodeHolder = new ThreadLocal<String>();

	/**
	 * 获取微信openID
	 * 
	 * @return
	 */
	public static String getWechatOpenId() {
		return wechatOpenIdHolder.get();
	}

	/**
	 * 获取微信用户ID
	 * 
	 * @return
	 */
	public static String getWechatUserId() {
		return wechatUserIdHolder.get();
	}

	/**
	 * 获取公司的ID
	 * 
	 * @return
	 */
	public static Integer getPrefectureId() {
		return PrefectureIdHolder.get();
	}

	/**
	 * 获得已登录的用户名
	 * 
	 * @return
	 */
	public static String getUserName() {
		return userNameHolder.get();
	}

	/**
	 * 获得已登录的用户ID
	 * 
	 * @return
	 */
	public static int getUserId() {
		return (userIdHolder.get() == null ? 0 : userIdHolder.get().intValue());
	}

	/**
	 * 获得已登录的用户名称
	 * 
	 * @return
	 */
	public static String getNickName() {
		return nickNameHolder.get();
	}

	/**
	 * 获得用户AccessToken
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		return AccessTokenHolder.get();
	}

	/**
	 * 设置微信openID
	 * 
	 * @param wechatOpenId
	 */
	public static void setWechatOpenId(String wechatOpenId) {
		wechatOpenIdHolder.set(wechatOpenId);
	}

	/**
	 * 设置微信用户ID
	 * 
	 * @param wechatUserId
	 */
	public static void setWechatUserId(String wechatUserId) {
		wechatUserIdHolder.set(wechatUserId);
	}

	/**
	 * 设置用户ID
	 * 
	 * @param userId
	 */
	public static void setUserId(int userId) {
		userIdHolder.set(userId);
	}

	/**
	 * 设置用户登录名
	 * 
	 * @param userName
	 */
	public static void setUserName(String userName) {
		userNameHolder.set(userName);
	}


	/**
	 * 设置用户名称
	 * 
	 * @param nickName
	 */
	public static void setNickName(String nickName) {
		nickNameHolder.set(nickName);
	}

	/**
	 * 设置用户AccessToken
	 * 
	 * @param AccessToken
	 */
	public static void setAccessToken(String AccessToken) {
		AccessTokenHolder.set(AccessToken);
	}

	// 设置角色编码
	public static void setRoleCode(String roleCode) {
		roleCodeHolder.set(roleCode);
	}

	// 获取角色编码
	public static String getRoleCode() {
		return roleCodeHolder.get();
	}

	// 设置经销商编码
	public static void setDealerCode(String dealerCode) {
		dealerCodeHolder.set(dealerCode);
	}

	// 获取经销商编码
	public static String getDealerCode() {
		return dealerCodeHolder.get();
	}

	// 设置经销商编码
	public static void setDealerName(String dealerCode) {
		dealerNameHolder.set(dealerCode);
	}

	// 获取经销商编码
	public static String getDealerName() {
		return dealerNameHolder.get();
	}


	// 设置经销商编码
	public static void setOrgId(String orgId) {
		orgIdHolder.set(orgId);
	}

	// 获取经销商编码
	public static String getOrgId() {
		return orgIdHolder.get();
	}

	// 设置经销商编码
	public static void setOrgCode(String orgId) {orgCodeHolder.set(orgId);}

	// 获取经销商编码
	public static String getOrgCode() {
		return orgCodeHolder.get();
	}

	/**
	 * 清空所有数据
	 */
	public static void clear() {
		userIdHolder.set(null);
		userNameHolder.set(null);
		nickNameHolder.set(null);
		PrefectureIdHolder.set(null);
		AccessTokenHolder.set(null);
	}
}
