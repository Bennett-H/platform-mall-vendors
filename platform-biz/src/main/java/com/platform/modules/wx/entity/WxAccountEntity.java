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
package com.platform.modules.wx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 公众号账号
 *
 * @author cxd
 * @date 2021-05-06 18:30:15
 */
@Data
@TableName("WX_ACCOUNT")
public class WxAccountEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    @NotEmpty(message = "appid不得为空")
    private String appid;
    /**
     * 公众号名称
     */
    @NotEmpty(message = "名称不得为空")
    private String name;
    /**
     * 账号类型
     */
    private int type;
    /**
     * 认证状态
     */
    private boolean verified;
    /**
     * appsecret
     */
    @NotEmpty(message = "appSecret不得为空")
    private String secret;
    /**
     * token
     */
    private String token;
    /**
     * aesKey
     */
    private String aesKey;
    /**
     * 关注后回复
     */
    private String content;
}
