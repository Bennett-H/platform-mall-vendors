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

import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallUserCouponEntity;
import com.platform.modules.mall.service.MallUserCouponService;
import com.platform.modules.mall.vo.WriteOffCouponVo;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员优惠券Controller
 *
 * @author cxd
 * @since 2020-04-10 16:02:58
 */
@RestController
@RequestMapping("/app/usercoupon")
public class MallUserCouponController extends AbstractController {
    @Autowired
    private MallUserCouponService mallUserCouponService;

    /**
     * 根据券实例id，获取商品及订单信息
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info")
    public RestResponse info(String id) {
        MallUserCouponEntity mallUserCoupon = mallUserCouponService.getCouponInfoById(id);
        return RestResponse.success().put("data", mallUserCoupon);
    }

    /**
     * 核销
     *
     * @param writeOffCouponVo  writeOffCouponVo
     * @return RestResponse
     */
    @RequestMapping("/writeOff")
    public RestResponse writeOff(WriteOffCouponVo writeOffCouponVo) {
        mallUserCouponService.writeOffCoupon(writeOffCouponVo);
        return RestResponse.success();
    }


}
