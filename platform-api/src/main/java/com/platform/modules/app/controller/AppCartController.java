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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.Constant;
import com.platform.config.RedisTemplateUtil;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.app.entity.BuyGoodsVo;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.service.SysConfigService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/cart")
@Api(tags = "AppCartController|购物车接口")
public class AppCartController extends AppBaseController {
    @Autowired
    private MallCartService cartService;
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallAddressService addressService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallGoodsSkuService goodsSkuService;
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private MallCouponService couponService;
    @Autowired
    private MallDistPromoService mallDistPromoService;
    @Autowired
    private MallShopsService mallShopsService;
    @Autowired
    private MallIntegralGoodsService integralGoodsService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;


    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("myCart")
    @ApiOperation(value = "获取购物车信息", notes = "获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse myCart(@LoginUser MallUserEntity loginUser) {
        return RestResponse.success(getCart(loginUser));
    }

    /**
     * 根据订单号添加商品到购物车
     *
     * @param loginUser 登录用户
     * @param orderId   订单ID
     * @return RestResponse
     */
    @GetMapping("addByOrder")
    @ApiOperation(value = "再次购买", notes = "再次购买，根据订单号添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true, dataType = "string", example = "1")
    })
    public RestResponse addByOrder(@LoginUser MallUserEntity loginUser, String orderId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("orderId", orderId);
        List<MallOrderGoodsEntity> orderGoodsVos = orderGoodsService.queryAll(params);
        List<MallCartEntity> addCartVos = new ArrayList<>();
        StringBuilder tipsMsg = new StringBuilder();
        String shopsId = null;
        List<MallCartEntity> promoList = new ArrayList<>();
        for (MallOrderGoodsEntity orderGoodsVo : orderGoodsVos) {
            //判断商品是否可以购买
            MallGoodsEntity goodsInfo = goodsService.getById(orderGoodsVo.getGoodsId());
            if (null == goodsInfo || goodsInfo.getIsOnSale() == 0) {
                tipsMsg.append(orderGoodsVo.getGoodsName()).append("商品已下架，");
                continue;
            }
            MallCartEntity cartInfo = new MallCartEntity();
            cartInfo.setCreateTime(new Date());

            MallOrderEntity order = orderService.getById(orderId);
            if (order != null) {
                shopsId = order.getShopsId();
                cartInfo.setShopsId(shopsId);
            }

            String skuId = orderGoodsVo.getSkuId();
            List<MallCartEntity> cartInfoList = cartService.list(new QueryWrapper<MallCartEntity>().eq("GOODS_ID", orderGoodsVo.getGoodsId())
                    .eq(StringUtils.isNotBlank(shopsId), "SHOPS_ID", shopsId).eq(StringUtils.isNotBlank(skuId), "SKU_ID", skuId).eq("USER_ID", loginUser.getId()));
            if (null != cartInfoList && cartInfoList.size() > 0) {
                //购物车已有 数量+1
                cartInfo = cartInfoList.get(0);
                cartInfo.setNumber(cartInfo.getNumber() + 1);
            } else {
                cartInfo.setGoodsId(orderGoodsVo.getGoodsId());
                cartInfo.setGoodsSn(orderGoodsVo.getGoodsSn());
                cartInfo.setGoodsName(orderGoodsVo.getGoodsName());
                cartInfo.setListPicUrl(orderGoodsVo.getListPicUrl());
                cartInfo.setNumber(orderGoodsVo.getNumber());
                cartInfo.setUserId(loginUser.getId());

                //使用最新商品价格
                MallGoodsSkuEntity productInfo = goodsSkuService.getById(orderGoodsVo.getSkuId());
                cartInfo.setRetailPrice(productInfo.getRetailPrice());
                cartInfo.setMarketPrice(productInfo.getMarketPrice());

                cartInfo.setShopsId(shopsId);
                cartInfo.setGoodsSpecifitionNameValue(orderGoodsVo.getGoodsSpecifitionNameValue());
                cartInfo.setChecked(1);
                cartInfo.setSkuId(orderGoodsVo.getSkuId());

                promoList.add(cartInfo); // 推广追踪用
            }
            addCartVos.add(cartInfo);
        }
        if (addCartVos.size() < 1) {
            return RestResponse.error(tipsMsg.substring(0, tipsMsg.length() - 1));
        }
        cartService.saveOrUpdateBatch(addCartVos);

        if (StringUtils.isNullOrEmpty(tipsMsg.toString())) {
            tipsMsg = new StringBuilder("添加成功");
        }

        // 添加推广追踪
        for (MallCartEntity entity : promoList) {
            String skuId = entity.getSkuId();
            List<MallCartEntity> cartInfoList = cartService.list(new QueryWrapper<MallCartEntity>().eq("GOODS_ID", entity.getGoodsId())
                    .eq(StringUtils.isNotBlank(shopsId), "SHOPS_ID", shopsId).eq(StringUtils.isNotBlank(skuId), "SKU_ID", skuId).eq("USER_ID", loginUser.getId()));
            if (null != cartInfoList && cartInfoList.size() > 0) {
                MallCartEntity cartInfo = cartInfoList.get(0);
                MallDistPromoEntity promoEntity = mallDistPromoService.getOne(new QueryWrapper<MallDistPromoEntity>().eq("ORDER_ID", orderId).eq("GOODS_ID", cartInfo.getGoodsId()).eq(StringUtils.isNotBlank(skuId), "SKU_ID", skuId));
                if (promoEntity == null) {
                    continue;
                }
                MallDistPromoEntity mallDistPromoEntity = new MallDistPromoEntity();
                mallDistPromoEntity.setCartId(cartInfo.getId());
                mallDistPromoEntity.setStatus(Constant.PromoStatus.CART.getValue());
                mallDistPromoEntity.setGoodsId(cartInfo.getGoodsId());
                mallDistPromoEntity.setUserId(promoEntity.getUserId());
                mallDistPromoEntity.setSkuId(cartInfo.getSkuId());
                mallDistPromoService.addPromoRecord(loginUser.getId(), mallDistPromoEntity);
            }
        }

        return RestResponse.success(tipsMsg.toString());
    }

    /**
     * 添加商品到购物车
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("add")
    @ApiOperation(value = "添加购物车", notes = "添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "skuId", value = "1"),
                    @ExampleProperty(mediaType = "goodsId", value = "1"),
                    @ExampleProperty(mediaType = "number", value = "1"),
                    @ExampleProperty(mediaType = "selectedText", value = "颜色：红色"),
                    @ExampleProperty(mediaType = "shopsId", value = "1"),
                    @ExampleProperty(mediaType = "referrerUserId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse add(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String goodsId = jsonParam.getString("goodsId");
        String skuId = jsonParam.getString("skuId");
        Integer number = jsonParam.getInteger("number");
        String selectedText = jsonParam.getString("selectedText");
        String shopsId = jsonParam.getString("shopsId");
        String referrerUserId = jsonParam.getString("referrerUserId");

        //判断商品是否可以购买
        MallGoodsEntity goodsInfo = goodsService.getById(goodsId);
        if (null == goodsInfo || goodsInfo.getIsOnSale() == 0) {
            return RestResponse.error("商品已下架！");
        }

        MallGoodsSkuEntity productVo = goodsSkuService.getById(skuId);

        //判断购物车中是否存在此规格商品
        List<MallCartEntity> cartInfoList = cartService.list(new QueryWrapper<MallCartEntity>().eq("GOODS_ID", goodsId)
                .eq(StringUtils.isNotBlank(shopsId), "SHOPS_ID", shopsId)
                .eq(StringUtils.isNotBlank(skuId), "SKU_ID", skuId).eq("USER_ID", loginUser.getId()));
        MallCartEntity cartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        if (null == cartInfo) {
            cartInfo = new MallCartEntity();
            cartInfo.setCreateTime(new Date());
            cartInfo.setSkuId(skuId);
            cartInfo.setGoodsId(goodsId);
            cartInfo.setGoodsSn(productVo != null ? productVo.getGoodsSn() : goodsInfo.getGoodsSn());
            cartInfo.setGoodsName(goodsInfo.getName());
            cartInfo.setListPicUrl(goodsInfo.getListPicUrl());
            cartInfo.setNumber(number);
            cartInfo.setUserId(loginUser.getId());
            cartInfo.setRetailPrice(productVo != null ? productVo.getRetailPrice() : goodsInfo.getRetailPrice());
            cartInfo.setShopsId(shopsId);
            cartInfo.setMarketPrice(productVo != null ? productVo.getMarketPrice() : goodsInfo.getMarketPrice());
            cartInfo.setChecked(1);
            if (StringUtils.isNotBlank(selectedText)) {
                cartInfo.setGoodsSpecifitionNameValue(selectedText);
            }
            cartService.save(cartInfo);

            // 添加推广追踪
            MallDistPromoEntity mallDistPromoEntity = new MallDistPromoEntity();
            mallDistPromoEntity.setCartId(cartInfo.getId());
            mallDistPromoEntity.setStatus(Constant.PromoStatus.CART.getValue());
            mallDistPromoEntity.setGoodsId(cartInfo.getGoodsId());
            mallDistPromoEntity.setUserId(referrerUserId);
            mallDistPromoEntity.setSkuId(cartInfo.getSkuId());
            mallDistPromoService.addPromoRecord(loginUser.getId(), mallDistPromoEntity);
        } else {
            //如果已经存在购物车中，则数量增加
            cartInfo.setNumber(cartInfo.getNumber() + number);
            cartService.update(cartInfo);
        }
        return RestResponse.success(getCart(loginUser));
    }

    /**
     * 更新购物车sku
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("updateSku")
    @ApiOperation(value = "更新购物车sku", notes = "更新购物车sku")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "skuId", value = "1"),
                    @ExampleProperty(mediaType = "goodsId", value = "1"),
                    @ExampleProperty(mediaType = "number", value = "1"),
                    @ExampleProperty(mediaType = "selectedText", value = "颜色：红色"),
                    @ExampleProperty(mediaType = "shopsId", value = "1"),
                    @ExampleProperty(mediaType = "referrerUserId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse updateSku(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String goodsId = jsonParam.getString("goodsId");
        String skuId = jsonParam.getString("skuId");
        Integer number = jsonParam.getInteger("number");
        String selectedText = jsonParam.getString("selectedText");
        String shopsId = jsonParam.getString("shopsId");
        String referrerUserId = jsonParam.getString("referrerUserId");

        //判断商品是否可以购买
        MallGoodsEntity goodsInfo = goodsService.getById(goodsId);
        if (null == goodsInfo || goodsInfo.getIsOnSale() == 0) {
            return RestResponse.error("商品已下架！");
        }

        MallGoodsSkuEntity productVo = goodsSkuService.getById(skuId);

        //判断购物车中是否存在此规格商品
        MallCartEntity cartInfo = cartService.getOne(new QueryWrapper<MallCartEntity>().eq("GOODS_ID", goodsId)
                .eq(StringUtils.isNotBlank(shopsId), "SHOPS_ID", shopsId).eq("USER_ID", loginUser.getId()), false);

        if (null != cartInfo) {
            cartInfo.setSkuId(skuId);
            cartInfo.setGoodsId(goodsId);
            cartInfo.setGoodsSn(productVo != null ? productVo.getGoodsSn() : goodsInfo.getGoodsSn());
            cartInfo.setGoodsName(goodsInfo.getName());
            cartInfo.setListPicUrl(goodsInfo.getListPicUrl());
            cartInfo.setNumber(number);
            cartInfo.setUserId(loginUser.getId());
            cartInfo.setRetailPrice(productVo != null ? productVo.getRetailPrice() : goodsInfo.getRetailPrice());
            cartInfo.setShopsId(shopsId);
            cartInfo.setMarketPrice(productVo != null ? productVo.getMarketPrice() : goodsInfo.getMarketPrice());
            if (StringUtils.isNotBlank(selectedText)) {
                cartInfo.setGoodsSpecifitionNameValue(selectedText);
            }
            cartService.update(cartInfo);

            // 添加推广追踪
            MallDistPromoEntity mallDistPromoEntity = new MallDistPromoEntity();
            mallDistPromoEntity.setCartId(cartInfo.getId());
            mallDistPromoEntity.setStatus(Constant.PromoStatus.CART.getValue());
            mallDistPromoEntity.setGoodsId(cartInfo.getGoodsId());
            mallDistPromoEntity.setUserId(referrerUserId);
            mallDistPromoEntity.setSkuId(cartInfo.getSkuId());
            mallDistPromoService.addPromoRecord(loginUser.getId(), mallDistPromoEntity);
        }
        return RestResponse.success(getCart(loginUser));
    }


    /**
     * 同步页面缓存购物车
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("syncSku")
    @ApiOperation(value = "同步页面缓存购物车", notes = "同步页面缓存购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "skuId", value = "1"),
                    @ExampleProperty(mediaType = "goodsId", value = "1"),
                    @ExampleProperty(mediaType = "number", value = "1"),
            }), required = true, dataType = "string")
    })
    public RestResponse syncSku(@LoginUser MallUserEntity loginUser, @RequestBody JSONArray jsonParam) {

        for (Object obj : jsonParam) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(obj));
            String goodsId = jsonObject.getString("goodsId");
            String skuId = jsonObject.getString("skuId");
            Integer number = jsonObject.getInteger("number");

            //判断商品是否可以购买
            MallGoodsEntity goodsInfo = goodsService.getById(goodsId);
            if (null == goodsInfo || goodsInfo.getIsOnSale() == 0) {
                return RestResponse.error("商品已下架！");
            }

            MallGoodsSkuEntity productVo = goodsSkuService.getById(skuId);

            //判断购物车中是否存在此规格商品
            MallCartEntity cartInfo = cartService.getOne(new QueryWrapper<MallCartEntity>().eq("GOODS_ID", goodsId)
                    .eq("USER_ID", loginUser.getId()), false);

            if (null == cartInfo) {
                cartInfo = new MallCartEntity();
                cartInfo.setSkuId(skuId);
                cartInfo.setGoodsId(goodsId);
                cartInfo.setGoodsSn(productVo != null ? productVo.getGoodsSn() : goodsInfo.getGoodsSn());
                cartInfo.setGoodsName(goodsInfo.getName());
                cartInfo.setListPicUrl(goodsInfo.getListPicUrl());
                cartInfo.setNumber(number);
                cartInfo.setChecked(1);
                cartInfo.setUserId(loginUser.getId());
                cartInfo.setRetailPrice(productVo != null ? productVo.getRetailPrice() : goodsInfo.getRetailPrice());
                cartInfo.setShopsId(goodsInfo.getShopsId());
                cartInfo.setMarketPrice(productVo != null ? productVo.getMarketPrice() : goodsInfo.getMarketPrice());
                cartService.add(cartInfo);
            }
        }
        return RestResponse.success(getCart(loginUser));
    }

    /**
     * 减少商品到购物车
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("minus")
    @ApiOperation(value = "减少购物车", notes = "减少商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "goodsId", value = "1"),
                    @ExampleProperty(mediaType = "number", value = "1"),
                    @ExampleProperty(mediaType = "shopsId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse minus(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String goodsId = jsonParam.getString("goodsId");
        Integer number = jsonParam.getInteger("number");
        String shopsId = jsonParam.getString("shopsId");
        //判断商品是否可以购买
        MallGoodsEntity goodsInfo = goodsService.getById(goodsId);
        if (null == goodsInfo || goodsInfo.getIsOnSale() == 0) {
            return RestResponse.error("商品已下架！");
        }
        //判断购物车中是否存在此规格商品
        Map<String, Object> cartParam = new HashMap<>(8);
        cartParam.put("goodsId", goodsId);
        cartParam.put("userId", loginUser.getId());
        if (StringUtils.isNotBlank(shopsId)) {
            cartParam.put("shopsId", shopsId);
        } else {
            cartParam.put("normal", 1);
        }
        List<MallCartEntity> cartInfoList = cartService.queryAll(cartParam);
        MallCartEntity cartInfo;
        int cartNum = 0;
        if (null != cartInfoList && cartInfoList.size() > 0) {
            cartInfo = cartInfoList.get(0);
            if (!loginUser.getId().equals(cartInfo.getUserId())) {
                return RestResponse.error("越权操作！");
            }
            if (cartInfo.getNumber() > number) {
                cartInfo.setNumber(cartInfo.getNumber() - number);
                cartService.update(cartInfo);
                cartNum = cartInfo.getNumber();
            } else if (cartInfo.getNumber() == 1) {
                cartService.delete(cartInfo.getId());
            }

        }
        return RestResponse.success().put("data", cartNum);
    }

    /**
     * 更新指定的购物车信息
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("update")
    @ApiOperation(value = "更新购物车", notes = "更新指定的购物车信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "goodsId", value = "1"),
                    @ExampleProperty(mediaType = "number", value = "1"),
                    @ExampleProperty(mediaType = "shopsId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse update(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String goodsId = jsonParam.getString("goodsId");
        Integer number = jsonParam.getInteger("number");
        String id = jsonParam.getString("id");
        //判断商品是否可以购买
        MallGoodsEntity goodsInfo = goodsService.getById(goodsId);
        if (null == goodsInfo || goodsInfo.getIsOnSale() == 0) {
            return RestResponse.error("商品已下架！");
        }
        if (null != id) {
            MallCartEntity cartInfo = cartService.getById(id);
            if (!loginUser.getId().equals(cartInfo.getUserId())) {
                return RestResponse.error("越权操作！");
            }
            cartInfo.setNumber(number);
            if (number == 0) {
                cartService.delete(id);
            } else {
                cartService.update(cartInfo);
            }
        } else {
            return RestResponse.error("操作有误！");
        }
        return RestResponse.success(getCart(loginUser));
    }

    /**
     * 是否选择商品，如果已经选择，则取消选择，批量操作
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("checked")
    @ApiOperation(value = "是否选中商品", notes = "是否选择商品，如果已经选择，则取消选择，批量操作")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "id", value = "1"),
                    @ExampleProperty(mediaType = "isChecked", value = "true"),
                    @ExampleProperty(mediaType = "carIds", value = "1"),
                    @ExampleProperty(mediaType = "shopsId", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse checked(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String id = jsonParam.getString("id");
        JSONArray cartIds = jsonParam.getJSONArray("cartIds");
        Integer isChecked = jsonParam.getInteger("isChecked");

        if (id != null) {
            MallCartEntity cartVo = cartService.getById(id);
            if (null == cartVo) {
                return RestResponse.error("更新异常！");
            }
            MallGoodsEntity entity = goodsService.getById(cartVo.getGoodsId());
            if (null == entity || entity.getIsOnSale() != 1) {
                cartVo.setChecked(0);
                cartService.update(cartVo);
                return RestResponse.error("商品已下架！");
            }
            if (!loginUser.getId().equals(cartVo.getUserId())) {
                return RestResponse.error("非法操作！");
            }
            cartVo.setChecked(isChecked);
            cartService.update(cartVo);
        }
        if (cartIds != null) {

            List<String> ids = new ArrayList<>();
            for (int i = 0; i < cartIds.size(); i++) {
                ids.add(cartIds.getString(i));
            }
            LambdaQueryWrapper<MallCartEntity> eq = Wrappers.<MallCartEntity>lambdaQuery()
                    .in(MallCartEntity::getId, ids);
            List<MallCartEntity> list = cartService.list(eq);

            List<MallGoodsEntity> goodsList = goodsService.list(Wrappers.<MallGoodsEntity>lambdaQuery().select(MallGoodsEntity::getId, MallGoodsEntity::getIsOnSale, MallGoodsEntity::getName)
                    .in(MallGoodsEntity::getId, list.stream().map(MallCartEntity::getGoodsId).collect(Collectors.toList())));
            Map<String, MallGoodsEntity> goodsMap = goodsList.stream().collect(Collectors.toMap(MallGoodsEntity::getId, item -> item));
            for (MallCartEntity cartVo : list) {
                MallGoodsEntity goods = goodsMap.get(cartVo.getGoodsId());
                if (goods == null) {
                    return RestResponse.error("更新异常！");
                }

                if (!loginUser.getId().equals(cartVo.getUserId())) {
                    return RestResponse.error("非法操作！");
                }

                if (1 != goods.getIsOnSale()) {
                    cartVo.setChecked(0);
                    cartService.update(cartVo);
                    return RestResponse.error(goods.getName() + "商品已下架！");
                } else {
                    cartVo.setChecked(isChecked);
                }

                cartService.update(cartVo);
            }
        }
        return RestResponse.success(getCart(loginUser));
    }

    /**
     * 删除选中的购物车商品，批量删除
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("delete")
    @ApiOperation(value = "删除购物车", notes = "删除选中的购物车商品，批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "cartIds", value = "1,2,3")
            }), required = true, dataType = "string")
    })
    public RestResponse deleteCart(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {

        String cartIds = jsonParam.getString("cartIds");
        if (StringUtils.isNullOrEmpty(cartIds)) {
            return RestResponse.error("删除出错");
        }
        String[] cartIdsArray = cartIds.split(",");

        cartService.deleteBatch(cartIdsArray);

        return RestResponse.success(getCart(loginUser));
    }

    /**
     * 获取购物车商品的总数量
     *
     * @param userId 会员ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("goodsCount")
    @ApiOperation(value = "购物车数量", notes = "获取购物车商品的总数量")
    @ApiImplicitParam(paramType = "query", name = "userId", value = "会员ID", example = "1", required = true, dataType = "string")
    public RestResponse goodsCount(String userId) {
        Map<String, Object> resultObj = new HashMap<>(4);
        //查询列表数据
        Map<String, Object> param = new HashMap<>(2);

        if (StringUtils.isNotBlank(userId)) {
            param.put("userId", userId);
            param.put("normal", 1);

            List<MallCartEntity> cartList = cartService.queryAll(param);
            //获取购物车统计信息
            Integer goodsCount = 0;
            for (MallCartEntity cartItem : cartList) {
                goodsCount += cartItem.getNumber();
            }
            resultObj.put("cartList", cartList);
            resultObj.put("goodsCount", goodsCount);
        } else {
            resultObj.put("cartList", new ArrayList<>());
            resultObj.put("goodsCount", 0);
        }

        return RestResponse.success(resultObj);
    }

    /**
     * 订单提交前的检验和填写相关订单信息
     *
     * @param loginUser 登录用户
     * @param addressId addressId
     * @param couponId  优惠券ID
     * @param type      购买方式
     * @return RestResponse
     */
    @GetMapping("checkout")
    @ApiOperation(value = "校验订单", notes = "订单提交前的检验和填写相关订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "addressId", value = "addressId", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "couponId", value = "couponId", dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "type", required = true, dataType = "string", example = "buy")
    })
    @SuppressWarnings("unchecked")
    public RestResponse checkout(@LoginUser MallUserEntity loginUser, String addressId,
                                 @RequestParam(required = false) String couponId, String type) {
        Map<String, Object> resultObj = new HashMap<>(16);
        MallAddressEntity addressVo = addressService.getById(addressId);
        if (addressVo == null) {
            //默认收货地址
            addressVo = addressService.getOne(new QueryWrapper<MallAddressEntity>().eq("IS_DEFAULT", 1).eq("USER_ID", loginUser.getId()), false);
            if (addressVo == null) {
                //如果没有默认地址选其中一个地址
                addressVo = addressService.getOne(new QueryWrapper<MallAddressEntity>().eq("USER_ID", loginUser.getId()), false);
            }
        }
        resultObj.put("checkedAddress", addressVo);

        List<String> checkedGoodsIdList = new ArrayList<>();
        List<String> checkedBrandIdList = new ArrayList<>();

        // * 获取要购买的商品和总价
        List<MallShopsEntity> checkedShopsList = new ArrayList();
        BigDecimal goodsTotalPrice;
        Integer checkedGoodsCount = 0;

        // 是直接购买的
        if ("buy".equals(type)) {
            BuyGoodsVo goodsVO = (BuyGoodsVo) redisTemplateUtil.get(Constant.MTM_CACHE + "goods" + loginUser.getId());
            if (null == goodsVO) {
                return RestResponse.error("发生错误，请返回重试！");
            }
            MallGoodsSkuEntity productInfo = goodsSkuService.getById(goodsVO.getSkuId());
            //计算订单的费用
            //商品总价
            goodsTotalPrice = productInfo.getRetailPrice().multiply(new BigDecimal(goodsVO.getNumber()));
            //判断商品是否可以购买
            MallGoodsEntity entity = goodsService.getById(goodsVO.getGoodsId());
            if (null == entity || entity.getIsOnSale() == 0) {
                return RestResponse.error("商品已下架！");
            }

            checkedGoodsIdList.add(entity.getId());
            checkedBrandIdList.add(entity.getBrandId());

            MallCartEntity cartVo = new MallCartEntity();
            cartVo.setGoodsName(entity.getName());
            cartVo.setNumber(goodsVO.getNumber());
            cartVo.setRetailPrice(productInfo.getRetailPrice());
            cartVo.setGoodsId(entity.getId());
            cartVo.setListPicUrl(entity.getListPicUrl());
            cartVo.setGoodsSpecifitionNameValue(goodsVO.getSelectedText());
            cartVo.setShopsId(goodsVO.getShopsId());
            cartVo.setGoodsSn(productInfo.getGoodsSn());
            MallShopsEntity shopsEntity = mallShopsService.getById(goodsVO.getShopsId());
            cartVo.setShopsName(shopsEntity.getName());
            cartVo.setShopsSn(shopsEntity.getShopsSn());

            List<MallCartEntity> cartList = new ArrayList<>();
            cartList.add(cartVo);
            shopsEntity.setCartList(cartList);
            checkedShopsList.add(shopsEntity);

            checkedGoodsCount = goodsVO.getNumber();
        } else {
            //获取要购买的商品
            Map<String, Object> cartData = getCart(loginUser);
            checkedShopsList = (List<MallShopsEntity>) cartData.get("shopsCartList");
            checkedShopsList.forEach(item -> {
                List<MallCartEntity> cartList = item.getCartList().stream().filter(cart -> {
                    if(cart.getChecked() == 1){
                        checkedGoodsIdList.add(cart.getGoodsId());
                        checkedBrandIdList.add(cart.getBrandId());
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                item.setCartList(cartList);
            });
            //计算订单的费用
            //商品总价
            goodsTotalPrice = (BigDecimal) ((HashMap) cartData.get("cartTotal")).get("checkedGoodsAmount");
            checkedGoodsCount = (Integer) ((HashMap) cartData.get("cartTotal")).get("checkedGoodsCount");
        }

        resultObj.put("checkedGoodsIdList", checkedGoodsIdList);
        resultObj.put("checkedBrandIdList", checkedBrandIdList);

        BigDecimal shippingFee = BigDecimal.valueOf(0);
        //免邮门槛
        String shippingFeeFre = sysConfigService.getValue(Constant.SHIPPING_FEE_FREE, "80");
        if (goodsTotalPrice.compareTo(new BigDecimal(shippingFeeFre)) <= 0) {
            shippingFee = new BigDecimal(sysConfigService.getValue(Constant.SHIPPING_FEE, "12"));
        }
        //共计优惠金额
        BigDecimal sumSubPrice = new BigDecimal(0);
        //优惠金额
        BigDecimal subPrice = new BigDecimal(0);
        MallCouponEntity couponEntity = null;
        if (StringUtils.isNotBlank(couponId)) {
            couponEntity = couponService.getById(couponId);
            if (null != couponEntity) {
                // 商品总价 >= 最低消费金额
                BigDecimal minPrice = couponEntity.getMinPrice();
                if (goodsTotalPrice.compareTo(minPrice) > -1) {
                    Integer couponType = couponEntity.getCouponType();
                    //代金券类型
                    if (Constant.CouponType.DJQ.getValue().equals(couponType)) {
                        subPrice = couponEntity.getSubPrice();
                    }
                    //折扣
                    if (Constant.CouponType.ZK.getValue().equals(couponType)) {
                        //这里使用商品总价乘以（1-折扣/10），然后四舍五入保留两位小数
                        subPrice = new BigDecimal(1).subtract(couponEntity.getDiscount().divide(new BigDecimal(10))).multiply(goodsTotalPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
            }
        }
        sumSubPrice = sumSubPrice.add(subPrice);
        resultObj.put("sumSubPrice", sumSubPrice);
        //订单的总价=商品金额+运费
        BigDecimal orderTotalPrice = goodsTotalPrice.add(shippingFee);
        //减去其它支付的金额后，要实际支付的金额
        BigDecimal actualPrice;
        if (orderTotalPrice.compareTo(subPrice) < 1) {
            //最少付0.01元
            if (null != couponEntity) {
                actualPrice = new BigDecimal(0.01);
            } else {
                actualPrice = orderTotalPrice.subtract(subPrice);
            }
        } else {
            actualPrice = orderTotalPrice.subtract(subPrice);
        }

        resultObj.put("shippingFee", shippingFee);
        resultObj.put("checkedGoodsList", checkedShopsList);
        resultObj.put("goodsTotalPrice", goodsTotalPrice);
        resultObj.put("orderTotalPrice", orderTotalPrice);
        resultObj.put("actualPrice", actualPrice);
        resultObj.put("checkedGoodsCount", checkedGoodsCount);
        return RestResponse.success(resultObj);
    }

    /**
     * 获取购物车中的数据
     */
    private RestResponse getCart(MallUserEntity loginUser) {
        //查询列表数据
        Map<String, Object> param = new HashMap<>(2);
        Map<String, Object> cartTotal = new HashMap<>(8);
        List<MallShopsEntity> shopsCartList = new ArrayList<>();
        List<MallCartEntity> able = new ArrayList<>();
        List<MallCartEntity> disabled = new ArrayList<>();
        // 还没登录
        if (loginUser == null) {
            return RestResponse.success().put("cartList", new ArrayList<MallCartEntity>()).put("shopsCartList", shopsCartList)
                    .put("able", able).put("disabled", disabled).put("cartTotal", cartTotal);
        } else {
            param.put("userId", loginUser.getId());
        }
        List<MallCartEntity> cartList = cartService.queryAll(param);
        //获取购物车统计信息
        Integer goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0.00);
        Integer checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);

        // 涉及的店铺ID
        List<String> shopsIds = new ArrayList<>();
        for (MallCartEntity cartItem : cartList) {
            goodsCount += cartItem.getNumber();
            goodsAmount = goodsAmount.add(cartItem.getRetailPrice().multiply(new BigDecimal(cartItem.getNumber())));
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked() && 1 == cartItem.getIsOnSale()) {
                checkedGoodsCount += cartItem.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cartItem.getRetailPrice().multiply(new BigDecimal(cartItem.getNumber())));
            }
            if (cartItem.getIsOnSale() == 1) {
                able.add(cartItem);
                if (StringUtils.isNotBlank(cartItem.getShopsId()) && !shopsIds.contains(cartItem.getShopsId())) {
                    shopsIds.add(cartItem.getShopsId());
                }
            }
            if (cartItem.getIsOnSale() == 0) {
                disabled.add(cartItem);
            }
        }

        shopsIds.forEach(shopsId -> {
            List<MallCartEntity> cartEntities = new ArrayList<>();
            MallShopsEntity shops = new MallShopsEntity();
            able.forEach(cart -> {
                if (shopsId.equals(cart.getShopsId())) {
                    shops.setShopsSn(cart.getShopsSn());
                    shops.setId(cart.getShopsId());
                    shops.setName(cart.getShopsName());
                    cartEntities.add(cart);
                }
            });
            shops.setCartList(cartEntities);
            shopsCartList.add(shops);
        });

        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", goodsAmount);
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);
        return RestResponse.success().put("cartList", cartList).put("shopsCartList", shopsCartList)
                .put("able", able).put("disabled", disabled).put("cartTotal", cartTotal);
    }

    /**
     * 积分订单提交前的检验和填写相关订单信息
     *
     * @param loginUser 登录用户
     * @param addressId 地址ID
     * @param goodsId   商品ID
     * @return RestResponse
     */
    @GetMapping("checkoutIntegral")
    @ApiOperation(value = "校验积分订单", notes = "积分订单提交前的检验和填写相关订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "addressId", value = "addressId", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "addressId", required = true, dataType = "string", example = "1")
    })
    public RestResponse checkoutIntegral(@LoginUser MallUserEntity loginUser, String addressId, String goodsId) {
        Map<String, Object> resultObj = new HashMap<>(16);
        MallAddressEntity addressVo = addressService.getById(addressId);
        if (addressVo == null) {
            //默认收货地址
            addressVo = addressService.getOne(new QueryWrapper<MallAddressEntity>().eq("IS_DEFAULT", 1).eq("USER_ID", loginUser.getId()), false);
            if (addressVo == null) {
                //如果没有默认地址选其中一个地址
                addressVo = addressService.getOne(new QueryWrapper<MallAddressEntity>().eq("USER_ID", loginUser.getId()), false);
            }
        }
        resultObj.put("checkedAddress", addressVo);
        MallIntegralGoodsEntity info = integralGoodsService.getById(goodsId);

        resultObj.put("goods", info);
        return RestResponse.success(resultObj);
    }
}
