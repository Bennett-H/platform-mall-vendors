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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 会员等级管理实体
 *
 * @author cxd
 * @since 2019-07-02 09:05:15
 */
@Data
@TableName("MALL_USER_LEVEL")
public class MallUserLevelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 等级名称
     */
    @NotBlank(message = "等级名称不能为空")
    private String name;
    /**
     * 会员完成订单金额满足则升级
     */
    @NotBlank(message = "消费达标金额不能为空")
    private String money;
    /**
     * 折扣
     */
    @NotBlank(message = "折扣不能为空")
    private String discount;
    /**
     * 会员等级图片
     */
    private String imageUrl;
    /**
     * 描述
     */
    private String description;
}
