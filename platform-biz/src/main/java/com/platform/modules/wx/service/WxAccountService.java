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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.wx.entity.WxAccountEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 公众号账号
 *
 * @author cxd
 * @date 2021-05-06 18:30:15
 */
public interface WxAccountService extends IService<WxAccountEntity> {
    /**
     * 查询所有用户数据
     *
     * @param params 查询参数
     * @return List 结果
     */
    List<WxAccountEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    IPage queryPage(Map<String, Object> params);

    /**
     * 新增、修改微信公众号
     *
     * @param entity 微信公众号
     * @return 新增结果
     */
    boolean saveWxAccount(WxAccountEntity entity);

    /**
     * 根据主键批量删除
     *
     * @param idList mchIds
     * @return 删除结果
     */
    boolean removeByAppIds(Collection<?> idList);
}

