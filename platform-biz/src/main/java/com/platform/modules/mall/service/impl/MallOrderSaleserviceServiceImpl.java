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
import com.platform.modules.mall.dao.MallOrderSaleserviceDao;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallOrderRefundEntity;
import com.platform.modules.mall.entity.MallOrderSaleserviceEntity;
import com.platform.modules.mall.service.MallOrderRefundService;
import com.platform.modules.mall.service.MallOrderSaleserviceService;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 申请退款Service实现类
 *
 * @author cxd
 * @since 2021-04-09 11:33:21
 */
@Service("mallOrderSaleserviceService")
public class MallOrderSaleserviceServiceImpl extends ServiceImpl<MallOrderSaleserviceDao, MallOrderSaleserviceEntity> implements MallOrderSaleserviceService {
    @Autowired
    private MallOrderRefundService orderRefundService;
    @Autowired
    private PayService payService;
    @Autowired
    private MallOrderService orderService;

    @Override
    public List<MallOrderSaleserviceEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.CREATE_TIME");
        params.put("asc", false);
        Page<MallOrderSaleserviceEntity> page = new Query<MallOrderSaleserviceEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallOrderSaleservicePage(page, params));
    }

    @Override
    public boolean add(MallOrderSaleserviceEntity mallOrderSaleservice) {
        return this.save(mallOrderSaleservice);
    }

    @Override
    public boolean update(MallOrderSaleserviceEntity mallOrderSaleservice) {
        return this.updateById(mallOrderSaleservice);
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
    @Transactional(rollbackFor = Exception.class)
    public boolean adopt(MallOrderSaleserviceEntity mallOrderSaleservice, String userId) {
        //添加退款记录表
        MallOrderRefundEntity refund = new MallOrderRefundEntity();
        refund.setSaleserviceId(mallOrderSaleservice.getId());
        refund.setOrderSn(mallOrderSaleservice.getOrderSn());
        refund.setUserId(mallOrderSaleservice.getUserId());
        refund.setRefundType(3);
        refund.setRefundMoney(mallOrderSaleservice.getAmount());
        //这里设置为退款中，在退款成功的通知接口里设置为退款成功
        refund.setRefundStatus(1);
        refund.setRefundReason(mallOrderSaleservice.getReason());
        refund.setApprover(userId);
        refund.setApprovalTime(new Date());
        refund.setApprovalRemark(mallOrderSaleservice.getHandleReason());
        orderRefundService.add(refund);

        //已审核
        mallOrderSaleservice.setStatus(2);
        mallOrderSaleservice.setHandleTime(new Date());
        this.updateById(mallOrderSaleservice);

        //发起退款，资金远路返回
        MallOrderEntity orderVo = orderService.getOne(new LambdaQueryWrapper<MallOrderEntity>().eq(MallOrderEntity::getOrderSn, mallOrderSaleservice.getOrderSn()));
        payService.refund(orderVo, mallOrderSaleservice.getAmount().toString());

        return true;
    }

    @Override
    public MallOrderSaleserviceEntity getByOrderSn(String orderSn) {
        return this.getOne(new LambdaQueryWrapper<MallOrderSaleserviceEntity>().eq(MallOrderSaleserviceEntity::getOrderSn, orderSn), false);
    }
}
