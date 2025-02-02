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
import java.util.Date;

/**
 * 积分变动记录实体
 *
 * @author cxd
 * @since 2021-07-27 13:29:35
 */
@Data
@TableName("MALL_INTEGRAL_LOG")
public class MallIntegralLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 会员ID
     */
    private String userId;
    /**
     * 类型：1=收入，2=支出
     */
    private Integer type;
    /**
     * 类型 1：签到奖励 2：购物奖励 3：抽奖奖励 4：系统发放 5：兑换支出 6：退款
     */
    private Integer typeDetail;
    /**
     * 变动积分数量
     */
    private Integer number;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
}
