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
import com.platform.modules.mall.entity.MallGroupBuyingRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品-拼团记录表Service接口
 *
 * @author cxd
 * @since 2021-11-14 22:52:41
 */
public interface MallGroupBuyingRecordService extends IService<MallGroupBuyingRecordEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallGroupBuyingRecordEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询商品-拼团记录表
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增商品-拼团记录表
     *
     * @param mallGroupBuyingRecord 商品-拼团记录表
     * @return 新增结果
     */
    boolean add(MallGroupBuyingRecordEntity mallGroupBuyingRecord);

    /**
     * 根据主键更新商品-拼团记录表
     *
     * @param mallGroupBuyingRecord 商品-拼团记录表
     * @return 更新结果
     */
    boolean update(MallGroupBuyingRecordEntity mallGroupBuyingRecord);

    /**
     * leaderId下所有参团人数+1
     *
     * @param leaderId
     * @return
     */
    int addJoinNumber(String leaderId);

    /**
     * 拼团状态
     *
     * @param groupId
     * @param status
     * @return
     */
    int updateStatus(String groupId, int status);
}
