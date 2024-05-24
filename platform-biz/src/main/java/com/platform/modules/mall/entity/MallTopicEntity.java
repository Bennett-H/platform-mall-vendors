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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文章实体
 *
 * @author cxd
 * @since 2020-03-08 10:25:25
 */
@Data
@TableName("MALL_TOPIC")
public class MallTopicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 作者
     */
    @NotBlank(message = "作者不能为空")
    private String author;
    /**
     * 条例图片
     */
    @NotBlank(message = "条例图片不能为空")
    private String itemPicUrl;
    /**
     * 子标题
     */
    private String subtitle;
    /**
     * 文章类别
     */
    private String topicCategoryId;
    /**
     * 文章类别主题
     */
    @TableField(exist = false)
    private String categoryTitle;
    /**
     * 起售价
     */
    private BigDecimal priceInfo;
    /**
     * 阅读数
     */
    private Integer readCount;
    /**
     * 场景图片
     */
    private String scenePicUrl;
    /**
     * 文章模板Id
     */
    private String topicTemplateId;
    /**
     * 文章标签Id
     */
    private String topicTagId;
    /**
     * 创建时间
     */
    private Date createTime;
}
