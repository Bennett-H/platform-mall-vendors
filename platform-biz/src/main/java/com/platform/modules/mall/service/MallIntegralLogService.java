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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.mall.entity.MallIntegralLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 积分变动记录Service接口
 *
 * @author cxd
 * @since 2021-07-27 13:29:35
 */
public interface MallIntegralLogService extends IService<MallIntegralLogEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallIntegralLogEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询积分变动记录
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增积分变动记录
     *
     * @param mallIntegralLog 积分变动记录
     * @return 新增结果
     */
    boolean add(MallIntegralLogEntity mallIntegralLog);

    /**
     * 根据主键更新积分变动记录
     *
     * @param mallIntegralLog 积分变动记录
     * @return 更新结果
     */
    boolean update(MallIntegralLogEntity mallIntegralLog);

    /**
     * 根据主键删除积分变动记录
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
     * @param number     变动积分数量
     * @param typeDetail 1：签到奖励 2：购物奖励 3：抽奖奖励 4：系统发放 5：兑换支出 6：退款
     * @return
     */
    boolean saveIntegrals(String userId, Integer number, Integer typeDetail);
}
