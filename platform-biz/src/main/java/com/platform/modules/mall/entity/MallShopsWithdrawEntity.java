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
package com.platform.modules.mall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家提现实体
 *
 * @author cxd
 * @date 2020-05-05 08:56:53
 */
@Data
@TableName("MALL_SHOPS_WITHDRAW")
public class MallShopsWithdrawEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 店铺Id
     */
    private String shopsId;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 类型 1申请提现
     */
    private Integer applyType;
    /**
     * 申请时间
     */
    private Date applyTime;
    /**
     * 申请金额
     */
    private BigDecimal applyMoney;
    /**
     * 分销金额
     */
    private BigDecimal distMoney;
    /**
     * 平台金额
     */
    private BigDecimal platformMoney;
    /**
     * 审核人
     */
    private String approver;
    /**
     * 审核时间
     */
    private Date approvalTime;
    /**
     * 审核备注
     */
    private String approvalRemark;
    /**
     * 提现账号
     */
    private String withdrawUserId;
    /**
     * 申请状态 1申请中 2退款成功 3已拒绝
     */
    private Integer applyStatus;
    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 手机号
     */
    @TableField(exist = false)
    private String mobile;
    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopsName;
    /**
     * 店铺编码
     */
    @TableField(exist = false)
    private String shopsSn;

    /**
     * 收款方银行卡号
     */
    private String encBankNo;
    /**
     * 收款方用户名
     */
    private String encTrueName;
    /**
     * 收款方开户行
     */
    private String bankCode;
    /**
     * 收款方银行绑定电话
     */
    private String bankPhone;

    /**
     * 企业付款成功，返回的微信付款单号
     */
    private String paymentNo;
}
