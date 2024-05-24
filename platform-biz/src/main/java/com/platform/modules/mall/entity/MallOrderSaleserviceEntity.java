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
 * 申请退款实体
 *
 * @author cxd
 * @since 2021-04-09 13:27:33
 */
@Data
@TableName("MALL_ORDER_SALESERVICE")
public class MallOrderSaleserviceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private String id;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 申请人ID
     */
    private String userId;
    /**
     * 退款单编号
     */
    private String saleserviceSn;
    /**
     * 申请退款原因
     */
    private String reason;
    /**
     * 申请退款金额
     */
    private BigDecimal amount;
    /**
     * 状态1：已申请 2：已审核 3：已退款 4：已驳回
     */
    private Integer status;
    /**
     * 申请时间
     */
    private Date createTime;
    /**
     * 审核时间
     */
    private Date handleTime;
    /**
     * 退款时间
     */
    private Date refundTime;
    /**
     * 审核原因
     */
    private String handleReason;
    /**
     * 用户备注
     */
    private String remark;
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
