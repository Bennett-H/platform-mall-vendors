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
 * 首页展示分类实体
 *
 * @author cxd
 * @since 2019-07-02 21:45:27
 */
@Data
@TableName("MALL_CHANNEL")
public class MallChannelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 跳转链接
     */
    @NotBlank(message = "跳转链接不能为空")
    private String url;
    /**
     * ICON链接
     */
    @NotBlank(message = "ICON链接不能为空")
    private String iconUrl;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 频道类型
     */
    private String type;

    /**
     * 颜色
     */
    private String color;
    /**
     * 状态 0:隐藏 1:显示
     */
    private Integer status;
}
