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
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀库存表实体
 *
 * @author cxd
 * @since 2019-07-07 12:05:21
 */
@Data
@TableName("MALL_SECKILL")
public class MallSeckillEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 店铺ID
     */
    private String shopsId;
    /**
     * 商品ID
     */
    @NotBlank(message = "商品不能为空")
    private String goodsId;
    /**
     * 秒杀名称
     */
    @NotBlank(message = "秒杀名称不能为空")
    private String name;
    /**
     * 秒杀图片
     */
    @NotBlank(message = "秒杀图片不能为空")
    private String listPicUrl;
    /**
     * 秒杀金额
     */
    @NotNull(message = "秒杀金额不能为空")
    private BigDecimal price;
    /**
     * 市场价
     */
    @TableField(exist = false)
    private BigDecimal marketPrice;
    /**
     * 秒杀库存
     */
    @NotNull(message = "秒杀库存不能为空")
    private Integer stock;
    /**
     * 秒杀开启时间
     */
    @NotNull(message = "秒杀开启时间不能为空")
    private Date startTime;
    /**
     * 秒杀结束时间
     */
    @NotNull(message = "秒杀结束时间不能为空")
    private Date endTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 版本号
     */
    @Version
    private Integer version;
}
