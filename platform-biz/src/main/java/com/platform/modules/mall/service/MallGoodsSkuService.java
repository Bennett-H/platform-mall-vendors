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
import com.platform.modules.mall.dto.goods.SaveGoodsStockReqDTO;
import com.platform.modules.mall.entity.MallGoodsSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品SKUService接口
 *
 * @author cxd
 * @since 2019-07-04 00:05:33
 */
public interface MallGoodsSkuService extends IService<MallGoodsSkuEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallGoodsSkuEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询商品SKU
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增商品SKU
     *
     * @param mallGoodsSku 商品SKU
     * @return 新增结果
     */
    boolean add(MallGoodsSkuEntity mallGoodsSku);

    /**
     * 根据主键更新商品SKU
     *
     * @param mallGoodsSku 商品SKU
     * @return 更新结果
     */
    boolean update(MallGoodsSkuEntity mallGoodsSku);

    /**
     * 根据主键删除商品SKU
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

    int saveGoodsProduct(List<MallGoodsSkuEntity> productList);

    int saveGoodsStock(List<SaveGoodsStockReqDTO> productList);
}
