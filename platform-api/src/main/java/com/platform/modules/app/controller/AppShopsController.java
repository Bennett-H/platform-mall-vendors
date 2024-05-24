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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.Constant;
import com.platform.config.RedisTemplateUtil;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallGoodsEntity;
import com.platform.modules.mall.entity.MallShopsCategoryEntity;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.mall.service.MallShopsCategoryService;
import com.platform.modules.mall.service.MallShopsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 店铺
 *
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/shops")
@Api(tags = "AppShopsController|店铺管理接口")
public class AppShopsController {
    @Autowired
    private MallShopsService shopsService;
    @Autowired
    private MallShopsCategoryService shopsCategoryService;
    @Autowired
    private MallGoodsService goodsService;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    /**
     * 店铺列表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/shopsList")
    @ApiOperation(value = "店铺列表", notes = "店铺列表")
    public RestResponse shopsList() {
        List<MallShopsEntity> shopsEntityList = shopsService.list(
                new QueryWrapper<MallShopsEntity>().select("IMG_URL", "SHOPS_SN", "NAME", "WORK_TIME", "LONGITUDE", "LATITUDE", "DETAILS")
                        .eq("APPLY_STATUS", 1).orderByDesc("SORT"));
        if (CollectionUtils.isEmpty(shopsEntityList)) {
            return RestResponse.success().put("data", null);
        }
        return RestResponse.success().put("data", shopsEntityList.stream()
                .filter(i -> StringUtils.isNotBlank(i.getShopsSn())).collect(Collectors.toList()));
    }

    /**
     * 店铺详情
     *
     * @param shopsSn 店铺SN
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/detailBySn")
    @ApiOperation(value = "店铺详情", notes = "店铺详情")
    @ApiImplicitParam(paramType = "query", name = "shopsSn", value = "店铺SN", example = "1", required = true, dataType = "string")
    public RestResponse detailBySn(@RequestParam String shopsSn) {
        MallShopsEntity shopsEntity = shopsService.getOne(new QueryWrapper<MallShopsEntity>().eq("SHOPS_SN", shopsSn), false);
        if (null == shopsEntity) {
            return RestResponse.error("店铺暂未开放");
        }
        return RestResponse.success().put("data", shopsEntity);
    }

    /**
     * 根据店铺ID获取店铺商品分类
     *
     * @param shopsId 店铺ID
     * @return RestReponse
     */
    @IgnoreAuth
    @GetMapping("/shopsCategory")
    @ApiOperation(value = "店铺商品分类列表", notes = "根据店铺ID获取店铺商品分类")
    @ApiImplicitParam(paramType = "query", name = "shopsId", value = "店铺ID", example = "1", required = true, dataType = "string")
    public RestResponse shopsCategory(@RequestParam String shopsId) {
        List<MallShopsCategoryEntity> shopsCategoryEntityList = shopsCategoryService.list(
                new QueryWrapper<MallShopsCategoryEntity>().eq("SHOPS_ID", shopsId).eq("STATUS", 1)
                        .orderByAsc("SORT"));
        if (shopsCategoryEntityList != null && shopsCategoryEntityList.size() > 0) {
            Map<String, Object> params = new HashMap<>(2);
            params.put("isOnSale", 1);
            for (MallShopsCategoryEntity item : shopsCategoryEntityList) {
                params.put("shopsCategoryId", item.getId());
                item.setShopsGoodsList(goodsService.queryAll(params));
            }
        }
        return RestResponse.success().put("data", shopsCategoryEntityList);
    }

    /**
     * 店铺商品分类列表商品
     *
     * @param shopsCategoryId 店铺商品分类ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/shopsCategoryGoods")
    @ApiOperation(value = "店铺商品分类列表商品", notes = "根据店铺商品分类ID获取商品列表")
    @ApiImplicitParam(paramType = "query", name = "shopsCategoryId", value = "店铺商品分类ID", example = "1", required = true, dataType = "string")
    public RestResponse shopsCategoryGoods(@RequestParam String shopsCategoryId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("shopsCategoryId", shopsCategoryId);
        params.put("isOnSale", 1);
        List<MallGoodsEntity> shopsCategoryEntityList = goodsService.queryAll(params);
        return RestResponse.success().put("data", shopsCategoryEntityList);
    }

    /**
     * 商户绑定
     *
     * @param loginUser 登录用户
     * @param jsonParam  JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/shopsBind")
    @ApiOperation(value = "商户绑定", notes = "商户绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "shopsId", value = "shopsId"),
                    @ExampleProperty(mediaType = "checkCode", value = "checkCode")
            }), required = true, dataType = "string")
    })
    public RestResponse shopsBind(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String shopsId = jsonParam.getString("shopsId");
        String checkCode = jsonParam.getString("checkCode");
        if (StringUtils.isNullOrEmpty(checkCode)) {
            return RestResponse.error("验证码不能为空");
        }
        Object codeCache = redisTemplateUtil.get(Constant.SHOPS_BIND + shopsId);
        if (StringUtils.isNullOrEmpty(codeCache)) {
            return RestResponse.error("验证码已失效为空");
        }
        redisTemplateUtil.del(Constant.SHOPS_BIND + shopsId);
        if (!codeCache.equals(checkCode)) {
            return RestResponse.error("验证码错误");
        }
        MallShopsEntity shopsEntity = new MallShopsEntity();
        shopsEntity.setId(shopsId);
        shopsEntity.setWithdrawUserId(loginUser.getId());
        shopsService.updateById(shopsEntity);
        return RestResponse.success();
    }
}
