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
package com.platform.modules.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.mall.entity.MallOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单表Dao
 *
 * @author cxd
 * @since 2019-07-05 19:29:18
 */
@Mapper
public interface MallOrderDao extends BaseMapper<MallOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallOrderEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallOrderEntity> selectMallOrderPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 统计个人中心订单数据
     *
     * @param params params
     * @return Map
     */
    Map<String, Object> queryUserCountMap(Map<String, Object> params);

    MallOrderEntity queryById(String orderId);

    /**
     * 店铺商品销售统计
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> shopsGoodsSalesCount(@Param("params") Map<String, Object> params);

    /**
     * 所有分店销售统计
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> allShopsGoodsSalesCount(@Param("params") Map<String, Object> params);


    /**
     * 门店订单状态统计
     * @param params
     * @return
     */
    List<Map<String, Object>> shopsOrderStatusCount(@Param("params") Map<String, Object> params);


    /**
     * 门店订单各状态统计
     * @param params
     * @return
     */
    Map<String, Object> shopsOrderAllStatusCount(@Param("params") Map<String, Object> params);
}
