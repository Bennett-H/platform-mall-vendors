/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.common.utils;

/**
 * 常量
 *
 * @author cxd
 */
public class Constant {
    public static final String WX_MA_CONFIG = "WX_MA_CONFIG";
    public static final String WX_MP_CONFIG = "WX_MP_CONFIG";
    public static final String USER_KEY = "LOGIN_USER_KEY";

    public static final String USER_SHOP_KEY = "LOGIN_USER_SHOP_KEY";
    public static final String MA_APPID = "MA_APPID";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String OK = "OK";
    /**
     * 注册商户发送短信码
     */
    public static final String REGISTER_SHOP = "REGISTER_SHOP_";
    /**
     * 商城订单
     */
    public static final Integer NORMAL_ORDER = 1;
    /**
     * 店铺自提订单
     */
    public static final Integer SHOPS_ORDER = 2;
    /**
     * 秒杀订单
     */
    public static final Integer SECKILL_ORDER = 3;
    /**
     * 积分订单
     */
    public static final Integer INTEGRALS_ORDER = 4;
    /**
     * 积分订单
     */
    public static final Integer GROUP_ORDER = 5;
    /**
     * 超级管理员ID
     */
    public static final String SUPER_ADMIN = "1";
    /**
     * 超级管理员所属机构
     */
    public static final String SUPER_ADMIN_ORG = "01";

    public static final String DEFAULT_PW = "888888";

    /**
     * 8小时后过期
     */
    public static final int EXPIRE = 3600 * 8;

    /**
     * 下单支付过期时间，单位分
     */
    public static final String ORDER_EXPIRE = "ORDER_EXPIRE";

    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";

    /**
     * 短信配置KEY
     */
    public final static String SMS_CONFIG_KEY = "SMS_CONFIG_KEY";
    /**
     * 邮费
     */
    public final static String SHIPPING_FEE = "SHIPPING_FEE";
    /**
     * 免运费门槛
     */
    public final static String SHIPPING_FEE_FREE = "SHIPPING_FEE_FREE";
    /**
     * 短信
     */
    public final static String PRE_SMS = "PRE_SMS:";
    /**
     * 商户绑定
     */
    public final static String SHOPS_BIND = "SHOPS_BIND:";
    /**
     * 是否开启余额支付
     */
    public final static String RECHARGE_STATUS = "RECHARGE_STATUS";
    /**
     * 是否开启分销功能
     */
    public final static String DISTRIBUTION_STATUS = "DISTRIBUTION_STATUS";

    /**
     * 是否开启分销商申请审核
     */
    public final static String DISTRIBUTION_AUDIT = "DISTRIBUTION_AUDIT";
    /**
     * 是否开启提现审核
     */
    public final static String WITHDRAW_AUDIT = "WITHDRAW_AUDIT";
    /**
     * 一级分销提成比例
     */
    public final static String COMMISSION_TYPE_DIST_1 = "COMMISSION_TYPE_DIST_1";
    /**
     * 二级分销提成比例
     */
    public final static String COMMISSION_TYPE_DIST_2 = "COMMISSION_TYPE_DIST_2";
    /**
     * 一级推广提成比例
     */
    public final static String COMMISSION_TYPE_PROMO_1 = "COMMISSION_TYPE_PROMO_1";
    /**
     * 二级推广提成比例
     */
    public final static String COMMISSION_TYPE_PROMO_2 = "COMMISSION_TYPE_PROMO_2";
    /**
     * 单次最低提现
     */
    public final static String WITHDRAW_SINGLE_LOWEST = "WITHDRAW_SINGLE_LOWEST";
    /**
     * 单次最高提现
     */
    public final static String WITHDRAW_SINGLE_HIGHEST = "WITHDRAW_SINGLE_HIGHEST";
    /**
     * 单日最高提现额度
     */
    public final static String WITHDRAW_DAY_HIGHEST = "WITHDRAW_DAY_HIGHEST";
    /**
     * 收货后可申请退款时间
     */
    public final static String ALLOW_REFUND_TIME = "ALLOW_REFUND_TIME";
    /**
     * 库存预警
     */
    public final static String STOCK_WARNING = "STOCK_WARNING";

    /**
     * 权限前缀
     */
    public static final String MALL = "mall";

    /**
     * 系统缓存前缀
     */
    public static final String SYS_CACHE = "SYS_CACHE:";

    /**
     * 业务系统缓存前缀
     */
    public static final String MTM_CACHE = "MTM_CACHE:";
    /**
     * 用户协议
     */
    public final static String USER_TREATY = "USER_TREATY";
    /**
     * 隐私协议
     */
    public final static String PRIVACY_TREATY = "PRIVACY_TREATY";
    /**
     * 客服电话
     */
    public final static String TELEPHONE = "TELEPHONE";
    /**
     * 商城名称
     */
    public final static String PROJECT_NAME = "PROJECT_NAME";
    /**
     * 腾讯位置KEY
     */
    public final static String QQ_MAP_KEY = "QQ_MAP_KEY";
    /**
     * LOGO
     */
    public final static String LOGO_URL = "LOGO_URL";
    /**
     * 快递鸟
     */
    public final static String KDN_BUSINESS_ID = "KDN_BUSINESS_ID";
    /**
     * 快递鸟
     */
    public final static String KDN_APP_KEY = "KDN_APP_KEY";

    /**
     * 一级分销提成比例
     */
    public final static String COMMISSION_TYPE_PLATFORM = "COMMISSION_TYPE_PLATFORM";


    /**
     * 物流收货后自动确认时间(单位：天)
     */
    public final static String AUTO_CONFIRM_TIME = "AUTO_CONFIRM_TIME";

    public final static String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String AES = "aes";
    public static final String BUY = "buy";
    public static final String ALL = "ALL";
    public static final String NULL = "null";
    /**
     * 直接分享
     */
    public static final String FL = "FL";
    /**
     * 推广分销
     */
    public static final String SL = "SL";

    public static final String STR_ZERO = "0";
    public static final String STR_ONE = "1";
    public static final String STR_TWO = "2";
    public static final String STR_THREE = "3";
    public static final String STR_FOUR = "4";
    public static final String STR_FIVE = "5";
    public static final String STR_SIX = "6";
    public static final String STR_SEVEN = "7";
    public static final String STR_EIGHT = "8";
    public static final String STR_NINE = "9";
    public static final String STR_TEN = "10";
    public static final String STR_EELEVEN = "11";

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    /**
     * 未关注
     */
    public static final int UNSUBSCRIBE = 0;
    /**
     * 关注
     */
    public static final int SUBSCRIBE = 1;
    /**
     * 空字符串
     */
    public static final String BLANK = "";
    /**
     * 斜杠
     */
    public static final String SLASH = "/";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 句号
     */
    public static final String DOT = ".";
    /**
     * 冒号
     */
    public static final String COLON = ":";
    /**
     * 下划线
     */
    public static final String UNDERSCORE = "_";

    public static final String BPMN20 = ".bpmn20.xml";

    public static final String IMAGE = "image";

    public static final String XML = "xml";
    public static final String PNG = "png";
    public static final String BAR = "bar";
    public static final String ZIP = "zip";
    public static final String BPMN = "bpmn";

    /**
     * 系统邮件签名
     */
    public static final String SIGNATURE_STR = "<br><font color='red'>-------------------------------------------------------------------<br>以上内容为邮件系统自动发送，请勿直接回复。</font>";

    /**
     * 系统自动邮件
     */
    public static final int SYS_SEND = 0;
    /**
     * 操作人主动邮件
     */
    public static final int USER_SEND = 1;
    /**
     * 商铺管理员默认角色
     */
    public static final String SHOPS_ROLE_ID = "7ca23144c8dcc3e48651508dcf8a27ca";

    /**
     * 商铺管理员默认角色
     */
    public static final String OPERATOR_USER_ID = "9cf965cf6474ed305a2a56edb6cd6645";

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3),
        /**
         * 服务器存储
         */
        DISCK(4),
        /**
         * MINIO
         */
        MINIO(5);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 订单状态
     */
    public enum OrderStatus {
        /**
         * 所有
         */
        ALL(-2),
        /**
         * 秒杀成功，待下单补充信息
         */
        DXD(-1),
        /**
         * 0:订单创建成功等待付款
         */
        DFK(0),
        /**
         * 100:已过期
         */
        YGQ(100),
        /**
         * 101:订单已取消
         */
        YQX(101),
        /**
         * 102:订单已删除
         */
        YSC(102),
        /**
         * 201:订单已付款,等待发货
         */
        YFK(201),
        /**
         * 300:订单已发货
         */
        YFH(300),
        /**
         * 301:用户确认收货
         */
        QRSH(301),
        /**
         * 400:申请退款
         */
        SQTK(400),
        /**
         * 401:没发货，退款
         */
        TK(401),
        /**
         * 402:已收货，退款退货
         */
        THTK(402);

        private Integer value;

        OrderStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 模板类型
     */
    public enum TemplateType {

        /**
         * 0:新订单提醒
         */
        XDD(0),
        /**
         * 1:下单成功通知
         */
        XDCG(1),
        /**
         * 2:订单评价提醒
         */
        DDPJ(2),
        /**
         * 3:退款
         */
        TK(3),
        /**
         * 4:秒杀成功通知
         */
        MSCG(4),
        /**
         * 5:订单配送通知
         */
        DDPS(5);

        private Integer value;

        TemplateType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 订单类型
     */
    public enum OrderType {

        /**
         * 1:商城订单
         */
        SC(1),
        /**
         * 2:店铺自提订单
         */
        DP(2),
        /**
         * 3:秒杀订单
         */
        MS(3),
        /**
         * 4:积分订单
         */
        JF(4);

        private Integer value;

        OrderType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 发货状态，商品配送情况
     */
    public enum ShippingStatus {
        /**
         * 1:未发货
         */
        WFH(1),
        /**
         * 2:已发货
         */
        YFH(2),
        /**
         * 3:已收货
         */
        YSH(3),
        /**
         * 4:退货
         */
        TH(4);

        private Integer value;

        ShippingStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 付款状态
     */
    public enum PayStatus {
        /**
         * 1:未付款
         */
        WFK(1),
        /**
         * 2:付款中
         */
        FKZ(2),
        /**
         * 3:已付款
         */
        YFK(3),
        /**
         * 4:退款
         */
        TK(4);

        private Integer value;

        PayStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 付款状态
     */
    public enum PayType {
        /**
         * 1:微信支付
         */
        WX(1),
        /**
         * 2:余额支付
         */
        YE(2),
        /**
         * 3:支付宝支付
         */
        ZFB(3),
        /**
         * 4:积分兑换
         */
        JF(4),
        /**
         * 5:字节跳动
         */
        TT(5);

        private Integer value;

        PayType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 优惠券类型：1:代金券 2:折扣
     */
    public enum CouponType {
        /**
         * 1:代金券
         */
        DJQ(1),
        /**
         * 2:折扣
         */
        ZK(2);

        private Integer value;

        CouponType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 用户下单来源类型
     */
    public enum FromType {
        /**
         * 1:微信小程序
         */
        MA(1),
        /**
         * 2:微信公众号
         */
        MP(2),
        /**
         * 3:app
         */
        APP(3),
        /**
         * 4:H5
         */
        H5(4),
        /**
         * 支付宝小程序
         */
        ALI(5),
        /**
         * QQ小程序
         */
        QQ(6),
        /**
         * 头条小程序
         */
        TT(7);

        private Integer value;

        FromType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 分销订单类型
     */
    public enum DistOrderType {
        /**
         * 1:代理提成
         */
        PROXY(1),
        /**
         * 2:推广提成
         */
        SALE(2),
        /**
         * 3:佣金提现
         */
        WITHDRAW(3);

        private final Integer value;

        DistOrderType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 提成分类
     */
    public enum CommissionType {
        /**
         * 1:一级分销提成
         */
        DIST_1(1),
        /**
         * 2:二级分销提成
         */
        DIST_2(2),
        /**
         * 3:一级promotion
         */
        PROMO_1(3),
        /**
         * 4:二级推广提成
         */
        PROMO_2(4);

        private final Integer value;

        CommissionType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 提现方式
     */
    public enum WithdrawType {
        /**
         * 余额
         */
        ENT_PAY(1),
        /**
         * 银行卡
         */
        PAY_BANK(2);

        private final Integer value;

        WithdrawType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 审核状态
     */
    public enum AuditStatus {
        /**
         * 审核进行中
         */
        AUDITING(0),
        /**
         * 审核通过
         */
        AUDIT_PASSED(1),
        /**
         * 审核不通过
         */
        AUDIT_NOT_PASSED(2),

        /**
         * 已提现
         */
        AUDIT_CASHED(3);

        private final Integer value;

        AuditStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 佣金定时到账状态
     */
    public enum AmountStatus {
        /**
         * 未到账，时间未到
         */
        WDZ(0),
        /**
         * 已到账
         */
        YDZ(1),
        /**
         * 购买者取消订单，失效
         */
        YSX(2);

        private final Integer value;

        AmountStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 推广状态
     */
    public enum PromoStatus {
        /**
         * 加入购物车
         */
        CART(1),
        /**
         * 已生成订单
         */
        ORDER(2),
        /**
         * 已支付
         */
        PAY(3);

        private final Integer value;

        PromoStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 银行卡状态
     */
    public enum CardStatus {
        /**
         * 已绑定
         */
        YBD(1),
        /**
         * 已解绑
         */
        YJB(2);

        private final Integer value;

        CardStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 系统用户状态
     */
    public enum SysUserStatus {
        /**
         * 暂停
         */
        DISABLE(0),
        /**
         * 正常
         */
        ENABLE(1);

        private Integer value;

        SysUserStatus(int value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 商户状态
     */
    public enum ShopsStatus {
        /**
         * 未审核
         */
        UNCHECK(0),
        /**
         * 审核
         */
        CHECK(1);

        private Integer value;

        ShopsStatus(int value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum SmsType {
        /**
         * 腾讯云
         */
        TX(1),
        /**
         * 阿里云
         */
        ALI(2);

        private final Integer value;

        SmsType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum ApplyType {
        /**
         * 订单结算
         */
        JIESUAN(1),
        /**
         * 线下提现
         */
        TIXIAN(2);

        private final Integer value;

        ApplyType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
