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
package com.platform.modules.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.wx.entity.WxPayConfigEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 微信支付配置Service接口
 *
 * @author cxd
 * @since 2022-08-16 18:08:55
 */
public interface WxPayConfigService extends IService<WxPayConfigEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<WxPayConfigEntity> queryAll(Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param queryWrapper 查询参数
     * @return List
     */
    List<WxPayConfigEntity> queryAllByWrapper(QueryWrapper<WxPayConfigEntity> queryWrapper);

    /**
     * 分页查询微信支付配置
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增微信支付配置
     *
     * @param wxPayConfig 微信支付配置
     * @return 新增结果
     */
    boolean savePayConfig(WxPayConfigEntity wxPayConfig);

    /**
     * 根据主键批量删除
     *
     * @param mchIds mchIds
     * @return 删除结果
     */
    boolean removeByMchIds(Collection<?> mchIds);
}
