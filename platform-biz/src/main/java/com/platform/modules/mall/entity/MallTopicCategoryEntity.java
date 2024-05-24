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
 * 文章分类实体
 *
 * @author cxd
 * @since 2020-03-08 10:25:26
 */
@Data
@TableName("MALL_TOPIC_CATEGORY")
public class MallTopicCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 文章类别
     */
    @NotBlank(message = "文章类别不能为空")
    private String title;
    /**
     * 类别图片
     */
    @NotBlank(message = "类别图片不能为空")
    private String picUrl;

    /**
     * 文章大类型 族谱 商会资源 资讯
     */
    private String type;

    /**
     * icon链接
     */
    private String iconUrl;

    /**
     * 小程序内链接
     */
    private String appUrl;

    /**
     * 排序
     */
    private int sort;
}
