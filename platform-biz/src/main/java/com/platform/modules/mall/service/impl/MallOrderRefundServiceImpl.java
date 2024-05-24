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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallOrderRefundDao;
import com.platform.modules.mall.entity.MallOrderRefundEntity;
import com.platform.modules.mall.service.MallOrderRefundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 退款记录Service实现类
 *
 * @author cxd
 * @since 2021-04-09 11:33:21
 */
@Service("mallOrderRefundService")
public class MallOrderRefundServiceImpl extends ServiceImpl<MallOrderRefundDao, MallOrderRefundEntity> implements MallOrderRefundService {

    @Override
    public List<MallOrderRefundEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.REFUND_TIME");
        params.put("asc", false);
        Page<MallOrderRefundEntity> page = new Query<MallOrderRefundEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallOrderRefundPage(page, params));
    }

    @Override
    public boolean add(MallOrderRefundEntity mallOrderRefund) {
        return this.save(mallOrderRefund);
    }

    @Override
    public boolean update(MallOrderRefundEntity mallOrderRefund) {
        return this.updateById(mallOrderRefund);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public MallOrderRefundEntity getByOrderSn(String orderSn) {
        return this.getOne(new LambdaQueryWrapper<MallOrderRefundEntity>().eq(MallOrderRefundEntity::getOrderSn, orderSn), false);
    }
}
