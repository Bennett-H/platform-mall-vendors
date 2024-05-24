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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品SKU实体
 *
 * @author cxd
 * @since 2019-07-04 00:05:33
 */
@Data
@TableName("MALL_GOODS_SKU")
@ApiModel(description = "商品SKU实体")
public class MallGoodsSkuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("SKU ID")
    @TableId
    private String id;
    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private String goodsId;
    /**
     * 商品编码
     */
    @ApiModelProperty("商品编码 ")
    private String goodsSn;
    /**
     * 商品库存
     */
    @ApiModelProperty("商品库存数量")
    private Integer goodsNumber;
    /**
     * 零售价格
     */
    @ApiModelProperty("零售价格")
    private BigDecimal retailPrice;
    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal marketPrice;
    /**
     * 拼团价格
     */
    @ApiModelProperty("拼团价格")
    private BigDecimal groupPrice;
    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String listPicUrl;
    /**
     * 起售数量
     */
    @ApiModelProperty("起售数量")
    private Integer minSell;
    /**
     * 规格值
     */
    @ApiModelProperty("规格值")
    @TableField(exist = false)
    private List<KeyValue> keyValue;
}
