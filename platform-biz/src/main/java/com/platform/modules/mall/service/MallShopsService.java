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
package com.platform.modules.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.mall.entity.MallShopsEntity;

import java.util.List;
import java.util.Map;

/**
 * 店铺Service接口
 *
 * @author cxd
 * @since 2019-07-03 13:51:29
 */
public interface MallShopsService extends IService<MallShopsEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallShopsEntity> queryAll(Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param wrapper 查询参数
     * @return List
     */
    List<MallShopsEntity> queryAllByWrapper(QueryWrapper<MallShopsEntity> wrapper);

    /**
     * 分页查询店铺
     *
     * @param params 查询参数
     * @return Page
     */
    IPage queryPage(Map<String, Object> params);

    /**
     * 新增店铺
     *
     * @param mallShops 店铺
     * @return 新增结果
     */
    boolean add(MallShopsEntity mallShops);

    /**
     * 根据主键更新店铺
     *
     * @param mallShops 店铺
     * @return 更新结果
     */
    boolean update(MallShopsEntity mallShops);

    /**
     * 根据主键删除店铺
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(String id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(String[] ids);

    /**
     * 修改我的店铺
     *
     * @param mallShops mallShops
     * @param userId    userId
     * @return boolean
     */
    boolean myUpdate(MallShopsEntity mallShops, String userId);

    /**
     * 生成店铺小程序码
     *
     * @param id id
     * @return boolean
     */
    boolean createQrCode(String id);

    /**
     * 生成店铺小程序绑定码
     *
     * @param id id
     * @return boolean
     */
    boolean createQrCodeBind(String id);

    /**
     * 获取门店数据权限
     *
     * @param userId userId
     * @return boolean
     */
    MallShopsEntity getShopsInfo(String userId);
}
