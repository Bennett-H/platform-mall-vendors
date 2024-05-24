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

import java.io.Serializable;
import java.util.Date;

/**
 * 族谱申请实体
 *
 * @author cxd
 * @since 2023-12-03 15:09:49
 */
@Data
@TableName("PEDIGREE_APPLICATION")
public class PedigreeApplicationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 微信号
     */
    private String wechatNo;
    /**
     * 微信昵称
     */
    private String wechatName;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 地区
     */
    private String region;
    /**
     * 身份
     */
    private String identity;
    /**
     * 职业
     */
    private String job;
    /**
     * 申请状态 0:申请中 1:已通过 -1:已拒绝
     */
    private Integer status;
    /**
     * 创建人ID
     */
    private String createUserId;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 修改人ID
     */
    private String updateUserId;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 删除状态 0：已删除 1：正常
     */
    private Integer isDelete;
}
