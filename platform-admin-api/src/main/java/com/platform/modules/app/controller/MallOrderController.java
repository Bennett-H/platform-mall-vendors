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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.LoginUser;
import com.platform.common.CommonRestResult;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.app.dto.mall.MallOrderCountResDTO;
import com.platform.modules.app.dto.mall.MallOrderQueryDTO;
import com.platform.modules.app.dto.mall.OrderSendGoodsReqDTO;
import com.platform.modules.mall.dto.order.MallOrderStatusCountReqDTO;
import com.platform.modules.mall.dto.order.MallOrderStatusCountResDTO;
import com.platform.modules.mall.dto.order.MallSalesCountReqDTO;
import com.platform.modules.mall.dto.order.MallSalesCountResDTO;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallIntegralGoodsService;
import com.platform.modules.mall.service.MallOrderGoodsService;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysConfigService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.utils.SessionContextUtil;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单表Controller
 *
 * @author cxd
 * @since 2019-07-05 19:29:18
 */
@RestController
@RequestMapping("/app/order")
@Api(tags = "商户订单管理")

public class MallOrderController extends AbstractController {
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallShopsService shopsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private MallIntegralGoodsService integralGoodsService;

    /**
     * 查看所有列表
     *
     * @param mallOrderQueryDTO 查询参数
     * @return RestResponse
     */
    @PostMapping("/getOrderList")
    @RequiresPermissions("mall:order:list")
    @ApiOperation(value = "查询订单列表", notes = "查询订单列表")
    public CommonRestResult<List<MallOrderEntity>> getOrderList(@RequestBody MallOrderQueryDTO mallOrderQueryDTO) {

        Map params = new HashMap();
        params.put("shopsId", SessionContextUtil.getShopsId());
        params.put("orderStatus", mallOrderQueryDTO.getOrderStaus());
        params.put("parentId", true);
        List<MallOrderEntity> list = mallOrderService.queryAll(params);

        for (MallOrderEntity item : list) {
            if (null != mallOrderQueryDTO.getOrderType() && mallOrderQueryDTO.getOrderType() == 4) {
                MallIntegralGoodsEntity entity = integralGoodsService.getById(item.getGoodsId());
                item.setGoodsCount(1);
                item.setIntegralGoodsEntity(entity);
            } else {
                //订单的商品
                List<MallOrderGoodsEntity> goodsList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", item.getId()));
                Integer goodsCount = 0;
                for (MallOrderGoodsEntity orderGoodsEntity : goodsList) {
                    goodsCount += orderGoodsEntity.getNumber();
                }
                //item.setCommentCount(commentService.count(new QueryWrapper<MallCommentEntity>().eq("ORDER_ID", item.getId())));
                item.setGoodsCount(goodsCount);
                item.setOrderGoodsEntityList(goodsList);
            }
        }


        return CommonRestResult.success(list);
    }


    @PostMapping("/getOrderCount")
    @RequiresPermissions("mall:order:list")
    @ApiOperation(value = "查询商户订单状态统计", notes = "查询商户订单状态统计")

    public CommonRestResult<MallOrderStatusCountResDTO> getOrderStatusCount(@RequestBody MallOrderQueryDTO mallOrderQueryDTO) {

        MallOrderStatusCountReqDTO mallOrderStatusCountReqDTO = new MallOrderStatusCountReqDTO();
        mallOrderStatusCountReqDTO.setShopsId(SessionContextUtil.getShopsId());
        MallOrderStatusCountResDTO mallOrderStatusCountResDTO = mallOrderService.getOrderStatusCount(mallOrderStatusCountReqDTO);
        return CommonRestResult.success(mallOrderStatusCountResDTO);
    }

    /**
     * @param mallOrderQueryDTO
     * @return
     */
    @PostMapping("/getOrderSaleCount")
    @RequiresPermissions("mall:order:list")
    @ApiOperation(value = "查询店铺销售统计", notes = "查询店铺销售统计")

    public CommonRestResult<MallSalesCountResDTO> getMallOrderSaleCount(@RequestBody MallOrderQueryDTO mallOrderQueryDTO) {

        MallSalesCountReqDTO mallSalesCountReqDTO = new MallSalesCountReqDTO();
        mallSalesCountReqDTO.setShopsId(SessionContextUtil.getShopsId());
        MallSalesCountResDTO mallSalesCountResDTO = mallOrderService.getMallOrderSaleCount(mallSalesCountReqDTO);
        return CommonRestResult.success(mallSalesCountResDTO);
    }


    /**
     * 根据主键查询详情
     *
     * @param orderId 主键
     * @return RestResponse
     */
    @GetMapping("/getOrderInfoByID")
    @RequiresPermissions("mall:order:info")
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    public CommonRestResult<MallOrderEntity> getOrderInfoByID(String orderId) {
        MallOrderEntity mallOrder = mallOrderService.queryById(orderId);

        List<MallOrderGoodsEntity> orderGoodsEntityList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", mallOrder.getId()));
        mallOrder.setOrderGoodsEntityList(orderGoodsEntityList);
        return CommonRestResult.success(mallOrder);
    }


    /**
     * 发货
     *
     * @param orderEntity
     * @return
     */
    @RequestMapping("/sendGoods")
    @RequiresPermissions("mall:order:sendGoods")
    public RestResponse sendGoods(@RequestBody MallOrderEntity orderEntity) {
        mallOrderService.sendGoods(orderEntity);
        return RestResponse.success();
    }

    /**
     * 获取当日、当周、当月、当季的收益、订单数
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("/getSummaryByType")
    @ApiOperation(value = "获取当日、当周、当月、当季的收益、订单数", notes = "获取当日、当周、当月、当季的收益、订单数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：DAY:当日 WEEK:当周 MONTH:当月 QUARTER:当季", required = true, allowableValues = "DAY,WEEK,MONTH,QUARTER", dataType = "string")
    })
    public RestResponse getIncomeDetailsByType(@LoginUser SysMpUserEntity loginUser, @RequestParam String type) {
        String userId = loginUser.getUserId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> data = new HashMap<>(4);
        data.put("income", new BigDecimal("0.00"));
        data.put("totalAmount", new BigDecimal("0.00"));
        data.put("orderCount", 0);

        Date[] result = new Date[2];

        switch (type) {
            case "DAY":
                result = DateUtils.getDayStartAndEndByDate(new Date());
                break;
            case "WEEK":
                result = DateUtils.getWeekStartAndEndByDate(new Date());
                break;
            case "MONTH":
                result = DateUtils.getMonthStartAndEnd(new Date());
                break;
            case "QUARTER":
                result = DateUtils.getQuarterStartAndEndByDate(new Date());
                break;
            default:
        }


        Map params = new HashMap();
        params.put("shopsId", SessionContextUtil.getShopsId());
        params.put("startTime", DateUtils.format(result[0], DateUtils.DATE_TIME_PATTERN));
        params.put("endTime", DateUtils.format(result[1], DateUtils.DATE_TIME_PATTERN));
        //获取订单数据
        Map<String, Object> countMap = mallOrderService.shopsOrderAllStatusCount(params);
        BigDecimal priceSum = (BigDecimal) (countMap.get("priceSum") == null ? BigDecimal.ZERO : countMap.get("priceSum"));
        BigDecimal distSum = (BigDecimal) (countMap.get("distSum") == null ? BigDecimal.ZERO : countMap.get("distSum"));
        data.put("totalAmount", priceSum.doubleValue());   //总金额
        data.put("distAmount", distSum.doubleValue());   //分销佣金
        data.put("incomeAmount", priceSum.subtract(distSum).multiply(BigDecimal.valueOf(1 - Double.parseDouble(sysConfigService.getValue(Constant.COMMISSION_TYPE_PLATFORM, "0.00")))).setScale(2, RoundingMode.HALF_UP));   //订单收入金额
        data.put("orderCount", countMap.get("orders") == null ? 0 : countMap.get("orders"));    //订单数
        data.put("shNum", countMap.get("shNum") == null ? 0 : countMap.get("shNum")); //售后
        data.put("unPayNum", countMap.get("unPayNum") == null ? 0 : countMap.get("unPayNum")); //未支付
        data.put("unSendNum", countMap.get("unSendNum") == null ? 0 : countMap.get("unSendNum")); //未发货
        data.put("unTakeNum", countMap.get("unTakeNum") == null ? 0 : countMap.get("unTakeNum"));  //未收货

        return RestResponse.success().put("data", data);
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
    public RestResponse confirmOrder(@RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");
        MallOrderEntity orderInfo = mallOrderService.getById(orderId);

        if (!this.getShopsId().equals(orderInfo.getShopsId())) {
            return RestResponse.error("越权操作！");
        }

        mallOrderService.confirmReceive(orderId);

        return RestResponse.success("确认成功");
    }

}
