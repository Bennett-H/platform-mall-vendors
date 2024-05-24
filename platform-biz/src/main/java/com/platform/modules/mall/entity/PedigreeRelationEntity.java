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
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 族谱关系实体
 *
 * @author cxd
 * @since 2023-11-21 21:09:21
 */
@Data
@Builder
@TableName("PEDIGREE_RELATION")
public class PedigreeRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 本人族谱id
     */
    private String selfId;
    /**
     * 父代族谱id
     */
    private String parentId;
    /**
     * 族谱第几代
     */
    private Integer layer;
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
