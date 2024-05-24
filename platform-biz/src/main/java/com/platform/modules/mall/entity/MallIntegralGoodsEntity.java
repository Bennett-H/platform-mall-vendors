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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分商品表实体
 *
 * @author cxd
 * @since 2021-07-27 15:32:51
 */
@Data
@TableName("MALL_INTEGRAL_GOODS")
public class MallIntegralGoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String name;
    /**
     * 商品编码
     */
    @NotBlank(message = "商品编码不能为空")
    private String goodsSn;
    /**
     * 商品库存
     */
    @NotBlank(message = "商品库存不能为空")
    private Integer goodsNumber;
    /**
     * 是否上架 0:否 1:是
     */
    private Integer isOnSale;
    /**
     * 删除状态 0：已删除 1：正常
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 列表图
     */
    @NotBlank(message = "列表图不能为空")
    private String listPicUrl;
    /**
     * 关键词
     */
    private String keywords;
    /**
     * 简明介绍
     */
    private String goodsBrief;
    /**
     * 积分价格
     */
    @NotBlank(message = "积分价格不能为空")
    private Integer retailPrice;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 销量
     */
    private Integer sales;
    /**
     * 创建人ID
     */
    private String createUserId;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 创建人所属部门
     */
    private String createUserOrgNo;
    /**
     * 视频链接
     */
    private String videoUrl;
}
