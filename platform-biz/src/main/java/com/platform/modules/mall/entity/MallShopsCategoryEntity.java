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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺商品分类实体
 *
 * @author cxd
 * @since 2019-07-03 16:23:39
 */
@Data
@TableName("MALL_SHOPS_CATEGORY")
public class MallShopsCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;
    /**
     * 店铺ID
     */
    @NotBlank(message = "所属店铺不能为空")
    private String shopsId;
    /**
     * 状态 0:隐藏 1:显示
     */
    @NotBlank(message = "状态不能为空")
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 创建人
     */
    private String createUserId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人所属部门
     */
    private String createUserOrgNo;
    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopsName;

    @TableField(exist = false)
    private List<MallGoodsEntity> shopsGoodsList;
}
