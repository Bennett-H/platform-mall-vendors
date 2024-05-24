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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallUserCouponDao;
import com.platform.modules.mall.entity.MallCommentEntity;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallOrderGoodsEntity;
import com.platform.modules.mall.entity.MallUserCouponEntity;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.mall.service.MallOrderGoodsService;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.MallUserCouponService;
import com.platform.modules.mall.vo.WriteOffCouponVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员优惠券Service实现类
 *
 * @author cxd
 * @since 2020-04-10 16:02:58
 */
@Service("mallUserCouponService")
public class MallUserCouponServiceImpl extends ServiceImpl<MallUserCouponDao, MallUserCouponEntity> implements MallUserCouponService {

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallGoodsService mallGoodsService;

    @Autowired
    private MallOrderGoodsService orderGoodsService;

    @Autowired
    private MallOrderService orderService;


    @Override
    public List<MallUserCouponEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallUserCouponEntity> page = new Query<MallUserCouponEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserCouponPage(page, params));
    }

    @Override
    public boolean add(MallUserCouponEntity mallUserCoupon) {
        return this.save(mallUserCoupon);
    }

    @Override
    public boolean update(MallUserCouponEntity mallUserCoupon) {
        return this.updateById(mallUserCoupon);
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
    public boolean checkUserCouponLimit(String userId, String couponId, Integer limit) {
        List<MallUserCouponEntity> userCouponEntityList = baseMapper.selectList(
                new QueryWrapper<MallUserCouponEntity>().eq("USER_ID", userId).eq("COUPON_ID", couponId));
        if (userCouponEntityList == null || userCouponEntityList.size() == 0 || userCouponEntityList.size() < limit) {
            return false;
        }
        return true;
    }

    @Override
    public void backCoupon(String orderId) {
        List<MallUserCouponEntity> userCouponEntityList = baseMapper.selectList(
                new QueryWrapper<MallUserCouponEntity>().eq("ORDER_ID", orderId));
        if (CollectionUtils.isEmpty(userCouponEntityList)) {
            return;
        }
        MallUserCouponEntity userCouponEntity = userCouponEntityList.get(0);
        userCouponEntity.setOrderId("");
        userCouponEntity.setStatus(0);
        baseMapper.updateById(userCouponEntity);
    }

    /**
     *
     * @param writeOffCouponVo
     * @return
     */
    @Override
    public boolean writeOffCoupon(WriteOffCouponVo writeOffCouponVo) {
//        MallUserCouponEntity mallUserCouponEntity = baseMapper.selectById(writeOffCouponVo.getCouponId());
//        if(mallUserCouponEntity.getStatus()!=0)
//            throw new RuntimeException("优惠券已使用或已过期");
//        mallUserCouponEntity.setStatus(1);
//        mallUserCouponEntity.setUsedTime(new Date());
//        baseMapper.updateById(mallUserCouponEntity);

        //订单ID
//        MallOrderEntity  orderInfo = orderService.getById(writeOffCouponVo.getOrderId());
//
//        MallOrderEntity parentOrder  =  orderService.getById(orderInfo.getParentId());
//        parentOrder.setOrderStatus(Constant.OrderStatus.QRSH.getValue());
//        parentOrder.setShippingStatus(Constant.ShippingStatus.YSH.getValue());
//        orderService.update(parentOrder);
//
//        orderInfo.setOrderStatus(Constant.OrderStatus.QRSH.getValue());
//        orderInfo.setShippingStatus(Constant.ShippingStatus.YSH.getValue());
//        orderService.update(orderInfo);
        return true;
    }

    @Override
    public MallUserCouponEntity getCouponInfoById(String id) {
        MallUserCouponEntity mallUserCouponEntity = this.getById(id);
        if(StringUtils.isNotBlank(mallUserCouponEntity.getBuyOrderId())){
            mallUserCouponEntity.setOrderEntity(mallOrderService.queryById(mallUserCouponEntity.getBuyOrderId()));
            //订单的商品
            if(mallUserCouponEntity.getOrderEntity()!=null) {
                List<MallOrderGoodsEntity> goodsList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", mallUserCouponEntity.getOrderEntity().getId()));
                mallUserCouponEntity.getOrderEntity().setOrderGoodsEntityList(goodsList);
            }
        }
        if(StringUtils.isNotBlank(mallUserCouponEntity.getBuyGoodsId())){
            mallUserCouponEntity.setGoodsEntity(mallGoodsService.queryById(mallUserCouponEntity.getBuyGoodsId()));
        }
        return mallUserCouponEntity;
    }
}
