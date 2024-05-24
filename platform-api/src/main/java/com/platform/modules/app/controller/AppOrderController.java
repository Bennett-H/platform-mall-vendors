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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.app.entity.BuyGoodsVo;
import com.platform.modules.app.entity.SubmitCartVo;
import com.platform.modules.app.entity.SubmitVo;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.mall.service.impl.KdniaoTrackQueryService;
import com.platform.modules.sys.service.SysConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: Controller <br>
 */
@Api(tags = "AppOrderController|订单管理控制器")
@RestController
@RequestMapping("/app/order")
public class AppOrderController {
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallGoodsSkuService goodsSkuService;
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallCartService cartService;
    @Autowired
    private WxSendMsgService sendMsgService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallCommentService commentService;
    @Autowired
    private PayService payService;
    @Autowired
    private MallCouponService couponService;
    @Autowired
    private MallUserCouponService userCouponService;
    @Autowired
    private MallDistPromoService mallDistPromoService;
    @Autowired
    private MallIntegralGoodsService integralGoodsService;
    @Autowired
    private MallIntegralLogService integralLogService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private KdniaoTrackQueryService kdniaoTrackQueryService;

    /**
     * 获取订单列表
     *
     * @param loginUser   登录用户
     * @param orderStatus 订单状态 -2:查询所有，-1:秒杀成功(这里只是秒杀成功，还需要用户下单补充订单信息) 0:订单创建成功等待付款 100:订单超时已过期 101:订单已取消 102:订单已删除 201:订单已付款,等待发货 300:订单已发货 301:用户确认收货 400:申请退款 401:没发货，退款　402:已收货，退款退货
     * @param orderType   订单类型 1：商城订单 2：店铺自提订单 3：秒杀订单 4：积分订单 5：拼团订单
     * @param current     当前页码
     * @param limit       每页条数
     * @return RestResponse
     */
    @GetMapping("list")
    @ApiOperation(value = "订单列表", notes = "获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", dataType = "int", example = "1", allowableValues = "-2,-1,0,100,101,102,201,300,301,400,401,402",
                    value = "订单状态 -2:查询所有，-1:秒杀成功(这里只是秒杀成功，还需要用户下单补充订单信息) 0:订单创建成功等待付款 100:订单超时已过期 101:订单已取消 102:订单已删除 201:订单已付款,等待发货 300:订单已发货 301:用户确认收货 400:申请退款 401:没发货，退款　402:已收货，退款退货"),
            @ApiImplicitParam(paramType = "query", name = "orderType", example = "1", dataType = "int", allowableValues = "1,2,3,4,5",
                    value = "订单类型 1：商城订单 2：店铺自提订单 3：秒杀订单 4：积分订单 5：拼团订单"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse orderList(@LoginUser MallUserEntity loginUser, Integer orderStatus, Integer orderType,
                                  @RequestParam(defaultValue = "0", required = false) Integer ifComment,
                                  @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", current);
        params.put("limit", limit);
        params.put("userId", loginUser.getId());
        // -2 查询所有orderStatus状态
        if (orderStatus != -2 && orderStatus != 400) {
            params.put("orderStatus", orderStatus);
        }
        if ((Constant.OrderStatus.QRSH.getValue().equals(orderStatus)
                || Constant.OrderStatus.SQTK.getValue().equals(orderStatus)) && ifComment == 1) {
            params.put("notExists", orderStatus);
        }
        if (orderType != null) {
            params.put("orderType", orderType);
        }
        if (null == orderType || orderType != 4) {
            params.put("parentId", true);
        }

        Page<MallOrderEntity> data = orderService.queryPage(params);
        //
        for (MallOrderEntity item : data.getRecords()) {
            if (null != orderType && orderType == 4) {
                MallIntegralGoodsEntity entity = integralGoodsService.getById(item.getGoodsId());
                item.setGoodsCount(1);
                item.setIntegralGoodsEntity(entity);
            } else {
                //订单的商品
                List<MallOrderGoodsEntity> goodsList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", item.getId()));
                Integer goodsCount = 0;
                for (MallOrderGoodsEntity orderGoodsEntity : goodsList) {
                    goodsCount += orderGoodsEntity.getNumber();
                    orderGoodsEntity.setGoods(goodsService.getById(orderGoodsEntity.getGoodsId()));
                }
                item.setCommentCount(commentService.count(new QueryWrapper<MallCommentEntity>().eq("ORDER_ID", item.getId())));
                item.setGoodsCount(goodsCount);
                item.setOrderGoodsEntityList(goodsList);
            }
        }
        return RestResponse.success().put("data", data);
    }

    /**
     * 根据订单ID获取订单详情
     *
     * @param loginUser 登录用户
     * @param orderId   订单ID
     * @return RestResponse
     */
    @GetMapping("detail")
    @ApiOperation(value = "订单详情", notes = "根据订单ID获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true, dataType = "string", example = "1")
    })
    public RestResponse orderDetail(@LoginUser MallUserEntity loginUser, String orderId) {
        Map<String, Object> resultObj = new HashMap<>(4);
        //
        MallOrderEntity orderInfo = orderService.queryById(orderId);
        if (null == orderInfo) {
            return RestResponse.error("订单不存在！");
        }
        if (!loginUser.getId().equals(orderInfo.getUserId())) {
            return RestResponse.error("越权操作！");
        }
        //订单的商品
        if (orderInfo.getOrderType() == 4) {
            MallIntegralGoodsEntity info = integralGoodsService.getById(orderInfo.getGoodsId());
            resultObj.put("orderGoods", info);
        } else {
            List<MallOrderGoodsEntity> orderGoods = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", orderId));
            BigDecimal goodsAmount = new BigDecimal(0);
            Integer goodsCount = 0;
            for (MallOrderGoodsEntity item : orderGoods) {
                goodsCount += item.getNumber();
                goodsAmount = goodsAmount.add(item.getRetailPrice().multiply(new BigDecimal(item.getNumber())));
                item.setGoods(goodsService.getById(item.getGoodsId()));
            }
            orderInfo.setGoodsCount(goodsCount);
            orderInfo.setGoodsAmount(goodsAmount);
            resultObj.put("orderGoods", orderGoods);
        }

        orderInfo.setCommentCount(commentService.count(new QueryWrapper<MallCommentEntity>().eq("ORDER_ID", orderId)));
        resultObj.put("orderInfo", orderInfo);
        return RestResponse.success(resultObj);
    }

    /**
     * 根据订单ID获取物流详情
     *
     * @param loginUser 登录用户
     * @param orderId   订单ID
     * @return RestResponse
     */
    @GetMapping("track")
    @ApiOperation(value = "订单物流信息", notes = "根据订单ID获取订单物流信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = true, dataType = "string", example = "1")
    })
    public RestResponse orderTrack(@LoginUser MallUserEntity loginUser, String orderId) {
        Map<String, Object> resultObj = new HashMap<>(4);
        //
        MallOrderEntity orderInfo = orderService.queryById(orderId);
        if (null == orderInfo) {
            return RestResponse.error("订单不存在！");
        }
        if (!loginUser.getId().equals(orderInfo.getUserId())) {
            return RestResponse.error("越权操作！");
        }

        //订单物流信息
        JSONObject result = kdniaoTrackQueryService.getOrderTracesByJson(orderInfo.getShippingCode(),orderInfo.getShippingNo(),orderInfo.getMobile());

        resultObj.put("orderInfo", orderInfo);
        resultObj.put("trackInfo", result);
        return RestResponse.success(resultObj);
    }

    /**
     * 提交订单
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("submitOrder")
    @ApiOperation(value = "提交订单", notes = "购物车页面提交订单，商品为购物车中已经选中的")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "fromType", value = "用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序"),
                    @ExampleProperty(mediaType = "postscript", value = "订单说明"),
                    @ExampleProperty(mediaType = "checkedAddress", value = "AddressVo"),
                    @ExampleProperty(mediaType = "couponId", value = "couponId")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse submitOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {

        List<SubmitVo> shopsList = new Gson().fromJson(new Gson().toJson(jsonParam.get("checkedGoodsList")), new TypeToken<List<SubmitVo>>() {
        }.getType());
        String type = jsonParam.getString("type");
        String couponId = jsonParam.getString("couponId");
        String promoUserId = jsonParam.getString("promoUserId");

        Integer fromType = jsonParam.getInteger("fromType");

        // * 获取要购买的商品和总价
        List<MallCartEntity> checkedGoodsList = new ArrayList<>();
        BigDecimal goodsTotalPrice;

        /**
         * 是直接购买的
         * 库存校验和操作
         */
        if ("buy".equals(type)) {
            BuyGoodsVo goodsVO = (BuyGoodsVo) redisTemplateUtil.get(Constant.MTM_CACHE + "goods" + loginUser.getId());
            if (null == goodsVO) {
                return RestResponse.error("发生错误，请返回重试！");
            }

            MallGoodsSkuEntity productInfo = goodsSkuService.getById(goodsVO.getSkuId());
            if (null == productInfo || productInfo.getGoodsNumber() < goodsVO.getNumber() || productInfo.getGoodsNumber() <= 0) {
                throw new BusinessException("商品库存不足");
            }
            productInfo.setGoodsNumber(productInfo.getGoodsNumber() - goodsVO.getNumber());
            goodsSkuService.update(productInfo);
            //计算订单的费用
            //商品总价
            goodsTotalPrice = productInfo.getRetailPrice().multiply(new BigDecimal(goodsVO.getNumber()));
            MallGoodsEntity entity = goodsService.getById(goodsVO.getGoodsId());
            if (entity.getIsOnSale() != 1) {
                throw new BusinessException(entity.getName() + "已下架！");
            }
            if (null == entity.getGoodsNumber() || entity.getGoodsNumber() < goodsVO.getNumber() || entity.getGoodsNumber() <= 0) {
                throw new BusinessException("商品库存不足");
            }
            entity.setGoodsNumber(entity.getGoodsNumber() - goodsVO.getNumber());
            goodsService.updateById(entity);

            MallCartEntity cartVo = new MallCartEntity();
            cartVo.setGoodsName(entity.getName());
            cartVo.setNumber(goodsVO.getNumber());
            cartVo.setGoodsId(goodsVO.getGoodsId());
            cartVo.setSkuId(goodsVO.getSkuId());
            cartVo.setRetailPrice(productInfo.getRetailPrice());
            cartVo.setListPicUrl(entity.getListPicUrl());
            cartVo.setGoodsSn(productInfo.getGoodsSn());
            cartVo.setShopsId(entity.getShopsId());
            cartVo.setGoodsSpecifitionNameValue(goodsVO.getSelectedText());
            checkedGoodsList.add(cartVo);
        } else {
            //获取要购买的商品
            Map<String, Object> param = new HashMap<>(4);
            param.put("userId", loginUser.getId());
            param.put("checked", 1);
            checkedGoodsList = cartService.queryAll(param);
            if (null == checkedGoodsList || checkedGoodsList.size() == 0) {
                return RestResponse.error("请选择商品！");
            }

            goodsTotalPrice = new BigDecimal(0.00);
            for (MallCartEntity cartItem : checkedGoodsList) {

                //减商品库存
                MallGoodsEntity goodsVo = goodsService.getById(cartItem.getGoodsId());
                if (goodsVo.getIsOnSale() != 1) {
                    throw new BusinessException(goodsVo.getName() + "已下架！");
                }
                if (null == goodsVo.getGoodsNumber() || goodsVo.getGoodsNumber() < cartItem.getNumber() || goodsVo.getGoodsNumber() <= 0) {
                    throw new BusinessException(cartItem.getGoodsName() + "库存不足");
                }
                goodsVo.setGoodsNumber(goodsVo.getGoodsNumber() - cartItem.getNumber());
                goodsVo.setSales(goodsVo.getSales() + cartItem.getNumber());
                goodsService.update(Wrappers.<MallGoodsEntity>lambdaUpdate().eq(MallGoodsEntity::getId, goodsVo.getId())
                        .set(MallGoodsEntity::getSales, goodsVo.getSales())
                        .set(MallGoodsEntity::getGoodsNumber, goodsVo.getGoodsNumber()));
                //减产品库存
                MallGoodsSkuEntity goodsSkuEntity = goodsSkuService.getById(cartItem.getSkuId());
                if (null == goodsSkuEntity || goodsSkuEntity.getGoodsNumber() < cartItem.getNumber() || goodsSkuEntity.getGoodsNumber() <= 0) {
                    throw new BusinessException(cartItem.getGoodsName() + "库存不足");
                }
                goodsSkuEntity.setGoodsNumber(goodsSkuEntity.getGoodsNumber() - cartItem.getNumber());
                goodsSkuService.update(goodsSkuEntity);

                //统计商品总价
                goodsTotalPrice = goodsTotalPrice.add(cartItem.getRetailPrice().multiply(new BigDecimal(cartItem.getNumber())));
            }
            //清空购物车已选中的商品
            cartService.remove(new QueryWrapper<MallCartEntity>().eq("USER_ID", loginUser.getId()).eq("checked", 1));
        }

        /**
         * 运费判断
         */
        BigDecimal shippingFee = BigDecimal.valueOf(0);
        //免邮门槛
        String shippingFeeFre = sysConfigService.getValue(Constant.SHIPPING_FEE_FREE, "80");
        if (goodsTotalPrice.compareTo(new BigDecimal(shippingFeeFre)) <= 0) {
            shippingFee = new BigDecimal(sysConfigService.getValue(Constant.SHIPPING_FEE, "12"));
        }
        String orderSn = StringUtils.transFromType(fromType) + StringUtils.generateOrderNumber();

        /**
         * 优惠金额
         */
        BigDecimal subPrice = new BigDecimal(0);
        MallCouponEntity couponEntity = null;
        if (StringUtils.isNotBlank(couponId)) {
            couponEntity = couponService.getById(couponId);
            if (null != couponEntity && couponEntity.getStatus() == 1) {
                MallUserCouponEntity userCouponEntity = userCouponService.getOne(new QueryWrapper<MallUserCouponEntity>().eq("COUPON_ID", couponId).eq("USER_ID", loginUser.getId()), false);
                if (userCouponEntity.getStatus() == 0) {
                    userCouponEntity.setStatus(1);
                    userCouponEntity.setUsedTime(new Date());
                    userCouponEntity.setOrderId(orderSn);
                    userCouponService.update(userCouponEntity);
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
        }

        /**
         * 订单价格计算//订单的总价
         */
        BigDecimal orderTotalPrice = goodsTotalPrice.add(shippingFee);
        BigDecimal actualPrice;
        if (orderTotalPrice.compareTo(subPrice) < 1) {
            //最少付0.01元
            if (null != couponEntity) {
                actualPrice = new BigDecimal("0.01");
            } else {
                actualPrice = orderTotalPrice.subtract(subPrice);
            }
        } else {
            actualPrice = orderTotalPrice.subtract(subPrice);
        }

        // 主订单
        MallAddressEntity addressVo = jsonParam.getObject("checkedAddress", MallAddressEntity.class);
        MallOrderEntity orderEntity = new MallOrderEntity();
        orderEntity.setId(orderSn);
        orderEntity.setOrderSn(orderSn);
        orderEntity.setUserId(loginUser.getId());
        orderEntity.setCouponId(couponId);
        orderEntity.setCouponPrice(subPrice);
        orderEntity.setOrderType(Constant.NORMAL_ORDER);
        orderEntity.setConsignee(addressVo.getUserName());
        orderEntity.setMobile(addressVo.getMobile());
        orderEntity.setCountry(addressVo.getNationalCode());
        orderEntity.setProvince(addressVo.getProvinceName());
        orderEntity.setCity(addressVo.getCityName());
        orderEntity.setDistrict(addressVo.getCountyName());
        orderEntity.setAddress(addressVo.getDetailInfo());
        orderEntity.setPostalCode(addressVo.getPostalCode());
        orderEntity.setFromType(fromType);
        orderEntity.setAddTime(new Date());
        orderEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), Integer.parseInt(sysConfigService.getValue(Constant.ORDER_EXPIRE, "30"))));
        orderEntity.setActualPrice(actualPrice);
        orderEntity.setOrderPrice(orderTotalPrice);
        // 待付款
        orderEntity.setOrderStatus(Constant.OrderStatus.DFK.getValue());
        orderEntity.setShippingStatus(Constant.ShippingStatus.WFH.getValue());
        orderEntity.setPayStatus(Constant.PayStatus.WFK.getValue());
        orderEntity.setShippingFee(shippingFee);
        orderEntity.setIntegralMoney(new BigDecimal(0));
        orderService.save(orderEntity);

        /**
         * 子订单表入库
         */
        List<MallOrderEntity> orderList = new ArrayList<>();
        //留言
        if (null != shopsList) {
            for (SubmitVo shopsEntity : shopsList) {
                MallOrderEntity orderInfo = new MallOrderEntity();
                orderSn = StringUtils.transFromType(fromType) + StringUtils.generateOrderNumber();
                orderInfo.setParentId(orderEntity.getId());
                orderInfo.setId(orderSn);
                orderInfo.setOrderSn(orderSn);
                orderInfo.setUserId(loginUser.getId());
                orderInfo.setOrderType(Constant.NORMAL_ORDER);
                orderInfo.setConsignee(addressVo.getUserName());
                orderInfo.setMobile(addressVo.getMobile());
                orderInfo.setCountry(addressVo.getNationalCode());
                orderInfo.setProvince(addressVo.getProvinceName());
                orderInfo.setCity(addressVo.getCityName());
                orderInfo.setDistrict(addressVo.getCountyName());
                orderInfo.setAddress(addressVo.getDetailInfo());
                orderInfo.setPostalCode(addressVo.getPostalCode());
                orderInfo.setFromType(fromType);
                orderInfo.setAddTime(new Date());
                orderInfo.setExpireTime(DateUtils.addDateMinutes(new Date(), Integer.parseInt(sysConfigService.getValue(Constant.ORDER_EXPIRE, "30"))));

                List<SubmitCartVo> cartEntities = shopsEntity.getCartList();
                final BigDecimal[] actPrice = {new BigDecimal(0)};

                cartEntities.forEach(item -> actPrice[0] = actPrice[0].add(item.getRetailPrice().multiply(new BigDecimal(item.getNumber()))));

                orderInfo.setActualPrice(actPrice[0].add(shippingFee));
                orderInfo.setOrderPrice(orderTotalPrice);

                // 待付款
                orderInfo.setOrderStatus(Constant.OrderStatus.DFK.getValue());
                orderInfo.setShippingStatus(Constant.ShippingStatus.WFH.getValue());
                orderInfo.setPayStatus(Constant.PayStatus.WFK.getValue());
                orderInfo.setShippingFee(shippingFee);
                orderInfo.setIntegralMoney(new BigDecimal(0));
                orderInfo.setPostscript(shopsEntity.getPostscript());
                orderInfo.setShopsId(shopsEntity.getId());
                //插入订单信息和订单商品
                orderList.add(orderInfo);
            }
            if (!orderService.saveBatch(orderList)) {
                return RestResponse.error("订单提交失败！");
            }
        }

        List<String> mallCartIds = new ArrayList<>();//推广追踪用
        List<MallOrderGoodsEntity> orderGoodsData = new ArrayList<>();
        for (MallCartEntity goodsItem : checkedGoodsList) {
            orderList.forEach(item -> {
                mallCartIds.add(goodsItem.getId());
                if (goodsItem.getShopsId().equals(item.getShopsId())) {
                    MallOrderGoodsEntity orderGoodsVo = new MallOrderGoodsEntity();
                    orderGoodsVo.setOrderId(item.getId());
                    orderGoodsVo.setGoodsId(goodsItem.getGoodsId());
                    orderGoodsVo.setGoodsSn(goodsItem.getGoodsSn());
                    orderGoodsVo.setSkuId(goodsItem.getSkuId());
                    orderGoodsVo.setGoodsName(goodsItem.getGoodsName());
                    orderGoodsVo.setListPicUrl(goodsItem.getListPicUrl());
                    orderGoodsVo.setMarketPrice(goodsItem.getMarketPrice());
                    orderGoodsVo.setRetailPrice(goodsItem.getRetailPrice());
                    orderGoodsVo.setNumber(goodsItem.getNumber());
                    orderGoodsVo.setGoodsSpecifitionNameValue(goodsItem.getGoodsSpecifitionNameValue());
                    orderGoodsData.add(orderGoodsVo);
                }
            });
        }
        orderGoodsService.saveBatch(orderGoodsData);

        // 添加推广追踪记录
        // 直接购买有goodsId
        // 购物车结算无goodsId，但可以通过购物车记录获取通过推广而购买的商品
        if ("buy".equals(type)) {
            MallCartEntity mallCartEntity = checkedGoodsList.get(0);
            MallDistPromoEntity distPromoEntity = new MallDistPromoEntity();
            distPromoEntity.setOrderId(orderList.get(0).getId());
            distPromoEntity.setUserId(promoUserId);
            distPromoEntity.setGoodsId(mallCartEntity.getGoodsId());
            distPromoEntity.setStatus(Constant.PromoStatus.ORDER.getValue());
            distPromoEntity.setSkuId(mallCartEntity.getSkuId());
            mallDistPromoService.addPromoRecord(loginUser.getId(), distPromoEntity);
        } else {
            List<MallDistPromoEntity> entityList = mallDistPromoService.getPromoListByCartIds(mallCartIds);
            for (MallDistPromoEntity distPromoEntity : entityList) {
                // 查得出数据代表有过“将推广商品添加入购物车”行为
                orderList.forEach(item -> {
                    distPromoEntity.setOrderId(item.getId());
                    distPromoEntity.setStatus(Constant.PromoStatus.ORDER.getValue());
                    mallDistPromoService.addPromoRecord(loginUser.getId(), distPromoEntity);
                });
            }
        }

        // 发送通知消息
        orderList.forEach(item -> {
            try {
                if (item.getFromType().equals(Constant.FromType.MA.getValue())) {
                    sendMsgService.notifyPaySuccess(item, Constant.TemplateType.XDD.getValue());
                } else if (item.getFromType().equals(Constant.FromType.MP.getValue())) {
                    //TODO 公众号下单，发送消息
                } else if (item.getFromType().equals(Constant.FromType.ALI.getValue())) {
                    //TODO 支付宝小程序下单，发送消息
                } else if (item.getFromType().equals(Constant.FromType.QQ.getValue())) {
                    //TODO QQ小程序下单，发送消息
                } else if (item.getFromType().equals(Constant.FromType.TT.getValue())) {
                    //TODO 头条小程序下单，发送消息
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return RestResponse.success().put("data", orderEntity);
    }

    /**
     * 确认收货
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("confirmOrder")
    @ApiOperation(value = "确认收货", notes = "确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "orderId")
            }), required = true, dataType = "string")
    })
    public RestResponse confirmOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");
        MallOrderEntity orderInfo = orderService.getById(orderId);

        if (!loginUser.getId().equals(orderInfo.getUserId())) {
            return RestResponse.error("越权操作！");
        }

        orderService.confirmReceive(orderId);

        return RestResponse.success("确认成功");
    }

    /**
     * 取消订单
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("cancelOrder")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse cancelOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");

        MallOrderEntity orderVo = orderService.getById(orderId);
        if (!loginUser.getId().equals(orderVo.getUserId())) {
            return RestResponse.error("越权操作！");
        }
        if (orderVo.getOrderStatus().equals(Constant.OrderStatus.YFH.getValue())) {
            return RestResponse.error("已发货，不能取消！");
        } else if (orderVo.getOrderStatus().equals(Constant.OrderStatus.QRSH.getValue())) {
            return RestResponse.error("已收货，不能取消！");
        }
        return payService.refund(orderVo, orderVo.getActualPrice().toString());
    }

    /**
     * 修改秒杀订单的收货地址
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("updateOrderAddress")
    @ApiOperation(value = "修改秒杀订单的收货地址", notes = "修改秒杀订单的收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "1"),
                    @ExampleProperty(mediaType = "consignee", value = "联系人"),
                    @ExampleProperty(mediaType = "province", value = "省"),
                    @ExampleProperty(mediaType = "city", value = "市"),
                    @ExampleProperty(mediaType = "district", value = "区"),
                    @ExampleProperty(mediaType = "address", value = "详细地址"),
                    @ExampleProperty(mediaType = "mobile", value = "手机号")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateOrderAddress(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");

        MallOrderEntity orderVo = orderService.getById(orderId);
        if (!loginUser.getId().equals(orderVo.getUserId())) {
            return RestResponse.error("越权操作！");
        }
        String consignee = jsonParam.getString("consignee"),
                province = jsonParam.getString("province"),
                city = jsonParam.getString("city"),
                district = jsonParam.getString("district"),
                address = jsonParam.getString("address"),
                mobile = jsonParam.getString("mobile");
        orderVo.setConsignee(consignee);
        orderVo.setProvince(province);
        orderVo.setCity(city);
        orderVo.setDistrict(district);
        orderVo.setAddress(address);
        orderVo.setMobile(mobile);
        orderService.update(orderVo);

        return RestResponse.success();
    }

    /**
     * 申请退款
     *
     * @param loginUser        登录用户
     * @param orderSaleservice MallOrderSaleserviceEntity
     * @return RestResponse
     */
    @PostMapping("applyRefund")
    @ApiOperation(value = "申请退款", notes = "申请退款")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "orderSaleservice", value = "MallOrderSaleserviceEntity", examples = @Example({
                    @ExampleProperty(mediaType = "orderSn", value = "1"),
                    @ExampleProperty(mediaType = "reason", value = "1"),
                    @ExampleProperty(mediaType = "amount", value = "1"),
                    @ExampleProperty(mediaType = "remark", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse applyRefund(@LoginUser MallUserEntity loginUser, @RequestBody MallOrderSaleserviceEntity orderSaleservice) {
        orderSaleservice.setUserId(loginUser.getId());
        payService.applyRefund(orderSaleservice);
        return RestResponse.success();
    }

    /**
     * 提交订单
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("submitIntegralsOrder")
    @ApiOperation(value = "提交订单", notes = "购物车页面提交订单，商品为购物车中已经选中的")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "fromType", value = "用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序"),
                    @ExampleProperty(mediaType = "postscript", value = "订单说明"),
                    @ExampleProperty(mediaType = "checkedAddress", value = "AddressVo"),
                    @ExampleProperty(mediaType = "couponId", value = "couponId"),
                    @ExampleProperty(mediaType = "shopsId", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse submitIntegralsOrder(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {

        String postscript = jsonParam.getString("postscript");
        String goodsId = jsonParam.getString("goodsId");

        Integer fromType = jsonParam.getInteger("fromType");

        MallIntegralGoodsEntity info = integralGoodsService.getById(goodsId);
        if (null == info) {
            return RestResponse.error("请选择商品！");
        }

        //更新积分商品库存
        if (info.getIsOnSale() != 1) {
            throw new BusinessException(info.getName() + "已下架！");
        }
        if (null == info.getGoodsNumber() || info.getGoodsNumber() < 1 || info.getGoodsNumber() <= 0) {
            throw new BusinessException(info.getName() + "库存不足");
        }
        info.setGoodsNumber(info.getGoodsNumber() - 1);
        info.setSales(info.getSales() + 1);
        integralGoodsService.updateById(info);

        // 生成订单
        String orderSn = StringUtils.transFromType(fromType) + StringUtils.generateOrderNumber();

        MallOrderEntity orderInfo = new MallOrderEntity();
        orderInfo.setGoodsId(goodsId);
        orderInfo.setId(orderSn);
        orderInfo.setOrderSn(orderSn);
        orderInfo.setUserId(loginUser.getId());

        orderInfo.setOrderType(Constant.INTEGRALS_ORDER);
        MallAddressEntity addressVo = jsonParam.getObject("checkedAddress", MallAddressEntity.class);
        orderInfo.setConsignee(addressVo.getUserName());
        orderInfo.setMobile(addressVo.getMobile());
        orderInfo.setCountry(addressVo.getNationalCode());
        orderInfo.setProvince(addressVo.getProvinceName());
        orderInfo.setCity(addressVo.getCityName());
        orderInfo.setDistrict(addressVo.getCountyName());
        orderInfo.setAddress(addressVo.getDetailInfo());
        orderInfo.setPostalCode(addressVo.getPostalCode());
        orderInfo.setFromType(fromType);
        //留言
        orderInfo.setPostscript(postscript);
        orderInfo.setAddTime(new Date());
        orderInfo.setPayTime(new Date());
        orderInfo.setActualPrice(new BigDecimal(info.getRetailPrice()));
        orderInfo.setOrderPrice(new BigDecimal(info.getRetailPrice()));
        orderInfo.setOrderStatus(Constant.OrderStatus.YFK.getValue());
        orderInfo.setShippingStatus(Constant.ShippingStatus.WFH.getValue());
        orderInfo.setPayStatus(Constant.PayStatus.YFK.getValue());
        orderInfo.setIntegralMoney(new BigDecimal(0));
        //插入订单信息和订单商品
        orderService.save(orderInfo);

        //更新会员表积分
        MallUserEntity userVo = userService.getById(loginUser.getId());
        userVo.setIntegral(userVo.getIntegral() - info.getRetailPrice());
        userService.update(userVo);

        // 添加积分消费记录
        integralLogService.saveIntegrals(loginUser.getId(), info.getRetailPrice(), 5);
        if (null == orderInfo.getId()) {
            return RestResponse.error("订单提交失败！");
        }

        return RestResponse.success().put("data", orderInfo);
    }
}
