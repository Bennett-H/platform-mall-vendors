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
 * 会员收藏实体
 *
 * @author cxd
 * @since 2019-07-02 14:51:33
 */
@Data
@TableName("MALL_COLLECT")
public class MallCollectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 商品
     */
    @TableField(exist = false)
    private String goodsName;
    @TableField(exist = false)
    private String listPicUrl;
    @TableField(exist = false)
    private String goodsBrief;
    @TableField(exist = false)
    private String retailPrice;
}
