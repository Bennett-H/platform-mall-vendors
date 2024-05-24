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
package com.platform.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallCouponEntity;
import com.platform.modules.mall.entity.MallUserCouponEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallCouponService;
import com.platform.modules.mall.service.MallUserCouponService;
import com.platform.modules.mall.vo.WriteOffCouponVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/coupon")
@Api(tags = "AppCouponController|优惠券接口")
public class AppCouponController extends AppBaseController {
    @Autowired
    MallUserCouponService userCouponService;
    @Autowired
    MallCouponService couponService;

    /**
     * 用户优惠券列表
     *
     * @param loginUser 登录用户
     * @param status    状态 0:未使用 1:已使用 2：过期
     * @return RestResponse
     */
    @GetMapping("/list")
    @ApiOperation(value = "用户优惠券列表", notes = "用户优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态 0:未使用 1:已使用 2：过期", example = "1", allowableValues = "0,1,2", dataType = "string")
    })
    public RestResponse list(@LoginUser MallUserEntity loginUser, String status) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> params = new HashMap<>(4);
        params.put("userId", userId);
        if (status != null) {
            params.put("status", status);
        }
        List<MallUserCouponEntity> list = userCouponService.queryAll(params);
        return RestResponse.success().put("data", list);
    }

    /**
     * 用户优惠券列表
     *
     * @param loginUser   登录用户
     * @param goodsIdList 商品ID_LIST
     * @param brandIdList 品牌ID_LIST
     * @param status      状态 0:未使用 1:已使用 2：过期
     * @return RestResponse
     */
    @GetMapping("/selectCoupon")
    @ApiOperation(value = "用户优惠券列表", notes = "用户优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "goodsIdList", value = "goodsIdList", example = "1,2,3", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "brandIdList", value = "brandIdList", example = "1,2,3", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态 0:未使用 1:已使用 2：过期", example = "1", allowableValues = "0,1,2", dataType = "string")
    })
    public RestResponse selectCoupon(@LoginUser MallUserEntity loginUser, String goodsIdList, String brandIdList, String status) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> params = new HashMap<>(8);
        params.put("userId", userId);
        params.put("nowDate", new Date());
        if (goodsIdList != null) {
            params.put("goodsIds", goodsIdList.split(Constant.COMMA));
        }
        if (brandIdList != null) {
            params.put("brandIds", brandIdList.split(Constant.COMMA));
        }
        if (status != null) {
            params.put("status", status);
        }
        List<MallUserCouponEntity> list = userCouponService.queryAll(params);
        //全场通用券
        params = new HashMap<>(8);
        params.put("userId", userId);
        params.put("limitType", "0");
        params.put("nowDate", new Date());
        if (status != null) {
            params.put("status", status);
        }
        list.addAll(userCouponService.queryAll(params));

        // 去重
        List<MallUserCouponEntity> data = list.stream().distinct().collect(Collectors.toList());
        return RestResponse.success().put("data", data);
    }

    /**
     * 可领取优惠券
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/couponList")
    @ApiOperation(value = "可领取优惠券", notes = "可领取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "limitType", name = "指定使用类型", value = "0：全场通用券 1：指定商品 2：指定品牌 3: 新人券", dataType = "integer"),
    })
    public RestResponse couponList(Integer limitType) {
        List<MallCouponEntity> couponEntityList = couponService.list(new QueryWrapper<MallCouponEntity>()
                        .eq(limitType!=null,"LIMIT_TYPE",limitType)
                .le("BEGIN_GET_TIME", new Date()).ge("END_GET_TIME", new Date()).eq("STATUS", 1)
        );
        return RestResponse.success().put("data", couponEntityList);
    }

    /**
     * 获取优惠券
     *
     * @param loginUser 登录用户
     * @param couponId  优惠券ID
     * @return RestResponse
     */
    @GetMapping("/getCouponUser")
    @ApiOperation(value = "获取优惠券", notes = "获取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "couponId", value = "优惠券ID", example = "1", required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse getCouponUser(@LoginUser MallUserEntity loginUser, @RequestParam String couponId) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        MallCouponEntity couponEntity = couponService.getById(couponId);
        if (null == couponEntity || couponEntity.getStatus() != 1) {
            throw new BusinessException("优惠券已失效");
        }
        if (System.currentTimeMillis() > couponEntity.getEndGetTime().getTime()) {
            throw new BusinessException("优惠券已失效");
        }
        if (couponEntity.getBeginGetTime().getTime() > System.currentTimeMillis()) {
            throw new BusinessException("暂未开放领取");
        }
        if (couponEntity.getSendCount() >= couponEntity.getTotalCount()) {
            throw new BusinessException("优惠券已领完");
        }
        Long count = userCouponService.count(new QueryWrapper<MallUserCouponEntity>().eq("USER_ID", userId).eq("COUPON_ID", couponId));
        if (count >= couponEntity.getLimitUser()) {
            throw new BusinessException("该券每人限领" + couponEntity.getLimitUser() + "张");
        }
        MallUserCouponEntity userCouponEntity = new MallUserCouponEntity();
        userCouponEntity.setUserId(userId);
        userCouponEntity.setCouponId(couponId);
        userCouponEntity.setAddTime(new Date());
        userCouponEntity.setType(2);
        userCouponEntity.setStatus(0);
        userCouponService.save(userCouponEntity);
        LambdaQueryWrapper<MallCouponEntity> eq = Wrappers.<MallCouponEntity>lambdaQuery()
                .eq(MallCouponEntity::getSendCount, couponEntity.getSendCount())
                .eq(MallCouponEntity::getId, couponEntity.getId());
        couponEntity.setSendCount(couponEntity.getSendCount() + 1);
        couponService.update(couponEntity, eq);
        return RestResponse.success();
    }

    /**
     * 用户优惠券数量
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("/userCount")
    @ApiOperation(value = "用户优惠券数量", notes = "用户优惠券数量")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse userCouponCount(@LoginUser MallUserEntity loginUser) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> params = new HashMap<>(4);
        params.put("userId", loginUser.getId());
        params.put("status", 0);
        return RestResponse.success().put("data", userCouponService.queryAll(params).size());
    }

    @GetMapping("/writeOffCoupon")
    @ApiOperation(value = "用户优惠券数量", notes = "用户优惠核销")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse writeOffCoupon(@LoginUser WriteOffCouponVo writeOffCouponVo) {

        Map<String, Object> params = new HashMap<>(4);
        return RestResponse.success().put("data", userCouponService.writeOffCoupon(writeOffCouponVo));
    }

    @ApiOperation(value = "根据券实例id，获取商品及订单信息", notes = "根据券实例id，获取商品及订单信息")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    @RequestMapping("/info")
    public RestResponse info(String id) {
        MallUserCouponEntity mallUserCoupon = userCouponService.getCouponInfoById(id);
        return RestResponse.success().put("data", mallUserCoupon);
    }

}
