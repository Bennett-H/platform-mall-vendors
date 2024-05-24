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
package com.platform.modules.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallIntegralGoodsDao;
import com.platform.modules.mall.entity.MallIntegralGoodsEntity;
import com.platform.modules.mall.service.MallIntegralGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 积分商品表Service实现类
 *
 * @author cxd
 * @since 2021-07-27 15:32:51
 */
@Service("mallIntegralGoodsService")
public class MallIntegralGoodsServiceImpl extends ServiceImpl<MallIntegralGoodsDao, MallIntegralGoodsEntity> implements MallIntegralGoodsService {

    @Override
    public List<MallIntegralGoodsEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.CREATE_TIME");
        params.put("asc", false);
        Page<MallIntegralGoodsEntity> page = new Query<MallIntegralGoodsEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallIntegralGoodsPage(page, params));
    }

    @Override
    public boolean add(MallIntegralGoodsEntity mallIntegralGoods) {
        return this.save(mallIntegralGoods);
    }

    @Override
    public boolean update(MallIntegralGoodsEntity mallIntegralGoods) {
        return this.updateById(mallIntegralGoods);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public int changeSale(String[] ids) {
        MallIntegralGoodsEntity integralGoodsEntity = this.getById(ids[0]);
        if (integralGoodsEntity.getIsOnSale() == 1) {
            integralGoodsEntity.setIsOnSale(0);
        } else {
            integralGoodsEntity.setIsOnSale(1);
        }
        return baseMapper.updateById(integralGoodsEntity);
    }
}
