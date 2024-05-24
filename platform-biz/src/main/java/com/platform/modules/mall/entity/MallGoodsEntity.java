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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品表实体
 *
 * @author cxd
 * @since 2019-07-03 17:58:29
 */
@Data
@TableName("MALL_GOODS")
public class MallGoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String name;
    /**
     * 商家ID
     */
    @NotBlank(message = "商家不能为空")
    private String shopsId;
    /**
     * 商品类型Id
     */
    @NotBlank(message = "商品类型不能为空")
    private String categoryId;
    /**
     * 商家商品分类ID
     */
    private String shopsCategoryId;
    /**
     * 商品编码
     */
    //@NotBlank(message = "商品编码不能为空")
    private String goodsSn;
    /**
     * 品牌Id
     */
    private String brandId;
    /**
     * 商品库存
     */
    @NotNull(message = "商品库存不能为空")
    private Integer goodsNumber;
    /**
     * 是否热销 0:否 1:是
     */
    private Integer isHot;
    /**
     * 是否上架 0:否 1:是
     */
    private Integer isOnSale;
    /**
     * 是否新商品 0:否 1:是
     */
    private Integer isNew;
    /**
     * 是否限购 0:否 1:是
     */
    private Integer isLimited;

    /**
     * 是否实体券 0:否 1:是
     */
    private Integer isCoupon;
    /**
     * 是否开启拼团 0:否 1:是
     */
    private Integer isGroup;
    /**
     * 拼团人数(阶梯团取最大阶梯人数)，最大99999
     */
    private Integer groupNumber;
    /**
     * 拼团价格
     */
    private BigDecimal groupPrice;
    /**
     * 状态 0：逻辑删除 1：正常
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
     * 进价
     */
    private BigDecimal unitPrice;
    /**
     * 市场价
     */
    @NotNull(message = "市场价不能为空")
    private BigDecimal marketPrice;
    /**
     * 零售价格
     */
    @NotNull(message = "零售价格不能为空")
    private BigDecimal retailPrice;
    /**
     * 专柜价格
     */
    private BigDecimal counterPrice;
    /**
     * 起售数量
     */
    @NotNull(message = "起售数量不能为空")
    private Integer minSell;
    /**
     * 是否是APP专属 0:否 1:是
     */
    private Integer isAppExclusive;
    /**
     * APP专享价
     */
    private BigDecimal appExclusivePrice;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 推广标签
     */
    private String promotionTag;
    /**
     * 推广描述
     */
    private String promotionDesc;
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
     * 修改人ID
     */
    private String updateUserId;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 视频链接
     */
    private String videoUrl;
    /**
     * 品牌名称
     */
    @TableField(exist = false)
    private String brandName;
    /**
     * 分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 店铺分类名称
     */
    @TableField(exist = false)
    private String shopsCategoryName;

    /**
     * 分类颜色
     */
    @TableField(exist = false)
    private String categoryColor;
    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String shopsName;
    /**
     * 门店编码
     */
    @TableField(exist = false)
    private String shopsSn;
    /**
     * 轮播图
     */
    @TableField(exist = false)
    private List<MallAttachmentEntity> attachmentEntityList;
    /**
     * 商品详情页参数列表
     */
    @TableField(exist = false)
    private List<MallGoodsAttributeEntity> goodsAttributeEntityList;
    /**
     * 商品SKU键表
     */
    @TableField(exist = false)
    private List<MallSpecificationEntity> specificationEntityList;
    /**
     * 商品SKU键表
     */
    @TableField(exist = false)
    private List<MallGoodsSkuEntity> goodsSkuEntityList;
}
