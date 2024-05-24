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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 微信支付配置实体
 *
 * @author cxd
 * @since 2022-08-16 18:08:55
 */
@Data
@TableName("WX_PAY_CONFIG")
public class WxPayConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 微信小程序appId
     */
    @NotEmpty(message = "appId不得为空")
    private String appId;
    /**
     * 微信支付商户号
     */
    @NotEmpty(message = "mchId不得为空")
    private String mchId;
    /**
     * 微信支付商户密钥
     */
    @NotEmpty(message = "mchKey不得为空")
    private String mchKey;
    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置
     */
    private String subAppId;
    /**
     * 服务商模式下的子商户号，普通模式请不要配置
     */
    private String subMchId;
    /**
     * p12证书的位置
     */
    private String keyPath;
}
