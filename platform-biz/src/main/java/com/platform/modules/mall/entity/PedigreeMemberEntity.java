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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 族谱成员实体
 *
 * @author cxd
 * @since 2023-11-21 21:09:20
 */
@Data
@TableName("PEDIGREE_MEMBER")
public class PedigreeMemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 姓
     */
    private String firstName;
    /**
     * 名
     */
    private String lastName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像图片
     */
    private String headImg;
    /**
     * 出生日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date birthDate;
    /**
     * 离世日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date deathDate;
    /**
     * 背景图片
     */
    private String backImg;

    /**
     * 是否名人 0：不是 1：是
     */
    private Integer isFamous;
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
