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
 * 退款记录实体
 *
 * @author cxd
 * @since 2021-04-09 13:27:33
 */
@Data
@TableName("MALL_ORDER_REFUND")
public class MallOrderRefundEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 订单ID
     */
    private String orderSn;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 退款类型 1用户退款 2系统退款 3售后退款
     */
    private Integer refundType;
    /**
     * 退款时间
     */
    private Date refundTime;
    /**
     * 退款金额
     */
    private BigDecimal refundMoney;
    /**
     *  退款状态 1申请中 2退款成功 3已拒绝
     */
    private Integer refundStatus;
    /**
     * 退款原因
     */
    private String refundReason;
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
     * 售后单ID
     */
    private String saleserviceId;
    /**
     * 会员昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 会员手机号
     */
    @TableField(exist = false)
    private String mobile;
}
