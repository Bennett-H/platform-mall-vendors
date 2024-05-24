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
import com.platform.modules.mall.dto.order.MallOrderStatusCountReqDTO;
import com.platform.modules.mall.dto.order.MallOrderStatusCountResDTO;
import com.platform.modules.mall.dto.order.MallSalesCountReqDTO;
import com.platform.modules.mall.dto.order.MallSalesCountResDTO;
import com.platform.modules.mall.entity.MallOrderEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 订单表Service接口
 *
 * @author cxd
 * @since 2019-07-05 19:29:18
 */
public interface MallOrderService extends IService<MallOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallOrderEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询订单表
     *
     * @param params 查询参数
     * @return Page
     */
    Page<MallOrderEntity> queryPage(Map<String, Object> params);

    /**
     * 新增订单表
     *
     * @param mallOrder 订单表
     * @return 新增结果
     */
    boolean add(MallOrderEntity mallOrder);

    /**
     * 根据主键更新订单表
     *
     * @param mallOrder 订单表
     * @return 更新结果
     */
    boolean update(MallOrderEntity mallOrder);

    /**
     * 根据主键删除订单表
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
     * 管理员发货
     *
     * @param orderEntity orderEntity
     * @return boolean
     */
    boolean sendGoods(MallOrderEntity orderEntity);

    /**
     * 管理员确认收货
     *
     * @param id id
     */
    void confirmReceive(String id);

    /**
     * 统计个人中心订单数据
     *
     * @param params params
     * @return Map
     */
    Map<String, Object> queryUserCountMap(Map<String, Object> params);



    /**
     * 门店订单各状态统计
     * @param params
     * @return
     */
    Map<String, Object> shopsOrderAllStatusCount(Map<String, Object> params);

    /**
     * @param orderId orderId
     * @return List
     */
    MallOrderEntity queryById(String orderId);

    /**
     * 分店商品销售统计
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> shopsGoodsSalesCount(Map<String, Object> params);

    /**
     * 所有分店销售统计
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> allShopsGoodsSalesCount(Map<String, Object> params);

    /**
     * 根据查询条件导出订单
     *
     * @param params
     * @param response
     */
    void exportOrderExcel(Map<String, Object> params, HttpServletResponse response);


    MallOrderStatusCountResDTO getOrderStatusCount(MallOrderStatusCountReqDTO mallOrderStatusCountReqDTO);


    MallSalesCountResDTO getMallOrderSaleCount(MallSalesCountReqDTO mallSalesCountReqDTO);
}
