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
package com.platform.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 飞鹅打印机实体
 *
 * @author cxd
 * @since 2020-06-05 14:25:19
 */
@Data
@TableName("SYS_PRINTER")
public class SysPrinterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private String id;
    /**
     * 登录管理后台的账号名
     */
    @NotBlank(message = "商品不能为空")
    private String user;
    /**
     * 注册账号后生成的UKEY
     */
    @NotBlank(message = "商品不能为空")
    private String ukey;
    /**
     * 打印机名称
     */
    @NotBlank(message = "商品不能为空")
    private String name;
    /**
     * 打印机编号
     */
    @NotBlank(message = "商品不能为空")
    private String sn;
    /**
     * 所属门店
     */
    @NotBlank(message = "商品不能为空")
    private String shopsId;
    /**
     * 存根打印机
     */
    private String stubSn;
    /**
     * 所属门店
     */
    @TableField(exist = false)
    private String shopsName;
}
