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
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallShopsWithdrawEntity;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商家提现Service接口
 *
 * @author cxd
 * @date 2020-05-05 08:56:53
 */
public interface MallShopsWithdrawService extends IService<MallShopsWithdrawEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallShopsWithdrawEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询商家提现
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增商家提现
     *
     * @param mallShopsWithdraw 商家提现
     * @return 新增结果
     */
    boolean add(MallShopsWithdrawEntity mallShopsWithdraw);

    /**
     * 根据主键更新商家提现
     *
     * @param mallShopsWithdraw 商家提现
     * @return 更新结果
     */
    boolean update(MallShopsWithdrawEntity mallShopsWithdraw);

    /**
     * 根据主键删除商家提现
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
     * 申请提现
     *
     * @return
     */
    RestResponse withdrawApply(String userId, String shopsId, String withdrawUserId, List<String> orderIds, String shopsBankCardId);

    RestResponse withdrawCash(String userId, String shopsId, String withdrawUserId, Double applyMoney, String shopsBankCardId);

    /**
     * 通过申请
     *
     * @param userCheckId
     * @param withDrawId
     * @return
     */
    RestResponse confirmWithdraw(String userCheckId, String withDrawId, String approvalRemark);

    /**
     * 提现打标记
     * @param ids
     * @param auditStatus
     * @param auditDesc
     * @return
     */
    RestResponse confirmCash(String[] ids, String auditStatus, String auditDesc);

    /**
     * 拒绝申请
     *
     * @param userCheckId
     * @param withDrawId
     * @return
     */
    RestResponse refuseWithdraw(String userCheckId, String withDrawId, String approvalRemark);

    /**
     * 根据id获取
     *
     * @param withdrawId 查询参数
     */
    MallShopsWithdrawEntity selectById(String withdrawId);

    /**
     * 查询可提现订单
     *
     * @param shopsId 查询参数
     * @return Map
     */
    List<MallOrderEntity> selectCanWithdrawList(String shopsId);

    /**
     * 查询关联订单
     *
     * @param withdrawId 查询参数
     * @return Map
     */
    List<MallOrderEntity> selectRelaOrderList(String withdrawId);


    void exportMyOrderExcel(Map<String, Object> params, HttpServletResponse response);


}
