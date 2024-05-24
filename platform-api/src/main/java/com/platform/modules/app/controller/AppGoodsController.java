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

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Base64Util;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.oss.cloud.UploadFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/goods")
@Api(tags = "AppGoodsController|商品接口")
public class AppGoodsController extends AppBaseController {
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallFootprintService footprintService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallIssueService issueService;
    @Autowired
    private MallBrandService brandService;
    @Autowired
    private MallCommentService commentService;
    @Autowired
    private MallCommentPictureService commentPictureService;
    @Autowired
    private MallCollectService collectService;
    @Autowired
    private MallIntegralGoodsService integralGoodsService;
    @Autowired
    private WxMaQrcodeService maQrcodeService;
    @Autowired
    private MallCategoryService categoryService;

    /**
     * 热销、新品推荐列表
     *
     * @param categoryId 分类ID
     * @param type       类型：IS_HOT：热销商品；IS_NEW：新品推荐
     * @param order      排序字段
     * @param sortType   排序类型
     * @param current    当前页码
     * @param limit      每页条数
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/list")
    @ApiOperation(value = "热销、新品推荐列表", notes = "热销、新品推荐列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "分类ID", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：IS_HOT：热销商品；IS_NEW：新品推荐", allowableValues = "IS_HOT,IS_NEW", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "RETAIL_PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse goodsList(String categoryId, String type, String order, String sortType, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        Page<MallGoodsEntity> page = new Page<>(current, limit);

        IPage<MallGoodsEntity> data = goodsService.page(page, new QueryWrapper<MallGoodsEntity>().eq(StringUtils.isNotBlank(categoryId), "CATEGORY_ID", categoryId)
                .eq(StringUtils.isNotBlank(type), type, 1).eq("IS_ON_SALE", 1).orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order).orderByDesc("SORT"));

        List<String> categoryIds = new ArrayList<>();
        for (MallGoodsEntity goodsVo : data.getRecords()) {
            categoryIds.add(goodsVo.getCategoryId());
        }
        //筛选的分类
        List<MallCategoryEntity> filterCategory = categoryService.getCategoryByData(categoryIds);

        return RestResponse.success().put("data", data).put("filterCategory", filterCategory);
    }

    /**
     * 根据ID查询商品详情，并记录用户浏览足迹
     *
     * @param goodsId 商品ID
     * @param userId  会员ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/detail")
    @ApiOperation(value = "商品详情", notes = "根据ID查询商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品ID", example = "1", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "会员ID", example = "1", dataType = "string")
    })
    public RestResponse goodsDetail(@RequestParam String goodsId, String userId) {
        Map<String, Object> resultObj = new HashMap<>(4);
        MallGoodsEntity info = goodsService.queryById(goodsId);
        //
        MallBrandEntity brand = brandService.getById(info.getBrandId());
        if (null != brand) {
            info.setBrandName(brand.getName());
        }
        MallCategoryEntity category = categoryService.getById(info.getCategoryId());
        if (null != category) {
            info.setCategoryName(category.getName());
            info.setCategoryColor(category.getColor());
        }


        if (StringUtils.isNotBlank(userId)) {
            //记录用户的足迹 同一商品只记录最后访问时间
            MallFootprintEntity footprintEntity = footprintService.getOne(
                    new QueryWrapper<MallFootprintEntity>().eq("USER_ID", userId).eq("GOODS_ID", goodsId), false);
            if (footprintEntity == null) {
                footprintEntity = new MallFootprintEntity();
            }
            footprintEntity.setAddTime(new Date());
            footprintEntity.setUserId(userId);
            footprintEntity.setGoodsId(goodsId);
            footprintService.saveOrUpdate(footprintEntity);
        }
        resultObj.put("info", info);
        //
        List<JSONObject> specKeyList = new ArrayList();
        for (MallSpecificationEntity specificationEntity : info.getSpecificationEntityList()) {
            JSONObject item = new JSONObject();
            item.put("specKey", specificationEntity.getName());
            item.put("selecteValue", ""); // 选中规格值
            item.put("customValue", ""); // 自定义值
            //
            boolean has = false;
            for (JSONObject jsonObject : specKeyList) {
                if (jsonObject.getString("specKey").equals(specificationEntity.getName())) {
                    has = true;
                }
            }
            if (!has) {
                specKeyList.add(item);
            }
        }
        resultObj.put("specKeyList", specKeyList);
        //
        List<JSONObject> specList = new ArrayList();
        Map<String, String> specKeyMap = new HashMap();
        for (MallGoodsSkuEntity skuItem : info.getGoodsSkuEntityList()) {
            for (KeyValue keyValue : skuItem.getKeyValue()) {
                String specKey = keyValue.getKey() + "@@" + keyValue.getValue();
                if (!specKeyMap.containsKey(specKey)) {
                    JSONObject specObj = new JSONObject();
                    specObj.put("name", keyValue.getKey());
                    specObj.put("description", keyValue.getValue());
                    specList.add(specObj);
                    specKeyMap.put(specKey, specKey);
                }
            }
        }
        resultObj.put("specList", specList);
        //
        return RestResponse.success().put("data", resultObj);
    }

    /**
     * 根据ID查询商品其他详情
     *
     * @param goodsId 商品ID
     * @param userId  会员ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/otherDetail")
    @ApiOperation(value = "商品其他详情", notes = "根据ID查询商品其他详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品ID", example = "1", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "会员ID", example = "1", dataType = "string")
    })
    public RestResponse otherDetail(@RequestParam String goodsId, String userId) {
        Map<String, Object> resultObj = new HashMap<>(8);
        List<MallIssueEntity> issue = issueService.queryAll(new HashMap<>());
        //
        QueryWrapper<MallCommentEntity> commentQuery = new QueryWrapper<MallCommentEntity>().eq("GOODS_ID", goodsId).eq("STATUS", 1);
        Long commentCount = commentService.count(commentQuery);
        List<MallCommentEntity> hotComment = commentService.list(commentQuery);
        Map<String, Object> commentInfo = new HashMap<>(8);
        if (null != hotComment && hotComment.size() > 0) {
            MallUserEntity commentUser = userService.getById(hotComment.get(0).getUserId());
            commentInfo.put("content", Base64Util.decode(hotComment.get(0).getContent()));
            commentInfo.put("addTime", DateUtils.format(hotComment.get(0).getAddTime(), DateUtils.DATE_PATTERN));
            commentInfo.put("nickname", commentUser.getNickname());
            commentInfo.put("goodsSpecifitionNameValue", hotComment.get(0).getGoodsSpecifitionNameValue());
            commentInfo.put("headImgUrl", commentUser.getHeadImgUrl());
            List<MallCommentPictureEntity> commentPictureEntities = commentPictureService.list(
                    new QueryWrapper<MallCommentPictureEntity>().eq("comment_id", hotComment.get(0).getId()));
            commentInfo.put("picList", commentPictureEntities);
        }
        Map<String, Object> comment = new HashMap<>(4);
        comment.put("count", commentCount);
        comment.put("data", commentInfo);

        long userHasCollect = 0;
        if (!StringUtils.isNullOrEmpty(userId)) {
            //当前用户是否收藏
            userHasCollect = collectService.count(new QueryWrapper<MallCollectEntity>().eq("USER_ID", userId).eq("GOODS_ID", goodsId));
            if (userHasCollect > 0) {
                userHasCollect = 1;
            }
        }
        //
        resultObj.put("userHasCollect", userHasCollect);
        resultObj.put("issue", issue);
        resultObj.put("comment", comment);
        return RestResponse.success().put("data", resultObj);
    }

    /**
     * 商品总计
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/count")
    @ApiOperation(value = "商品总计", notes = "商品总计")
    public RestResponse goodsSum() {
        return RestResponse.success().put("data", goodsService.count(new QueryWrapper<MallGoodsEntity>().eq("IS_ON_SALE", 1)));
    }

    /**
     * 获取商品小程序码
     *
     * @param user    登录用户
     * @param goodsId 商品ID
     * @param width   小程序码宽度
     * @return RestResponse
     */
    @GetMapping("/getGoodsQrCode")
    @ApiOperation(value = "获取商品小程序码", notes = "获取商品小程序码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品ID", example = "1", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "width", value = "小程序码宽度", example = "250", required = true, dataType = "int")
    })
    public RestResponse getGoodsQrCode(@LoginUser MallUserEntity user, @RequestParam String goodsId, @RequestParam(defaultValue = "250") Integer width) {
        String page = "/pages/goods/goods?id=" + goodsId + "&referrer=" + user.getId();
        try {
            byte[] bytes = maQrcodeService.createWxaCodeBytes(page, "release", width, false, new WxMaCodeLineColor("0", "0", "0"), true);

            String url = UploadFactory.build().uploadSuffix(bytes, ".png");

            return RestResponse.success().put("url", url);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取小程序码失败！");
        }
    }

    /**
     * 获取拼团商品小程序码
     *
     * @param user    登录用户
     * @param goodsId 商品ID
     * @param width   小程序码宽度
     * @return RestResponse
     */
    @GetMapping("/getGroupQrCode")
    @ApiOperation(value = "获取拼团商品小程序码", notes = "获取拼团商品小程序码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品ID", example = "1", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "width", value = "小程序码宽度", example = "250", required = true, dataType = "int")
    })
    public RestResponse getGroupQrCode(@LoginUser MallUserEntity user, @RequestParam String goodsId, @RequestParam(defaultValue = "250") Integer width) {
        String page = "/pages/groupDetail/groupDetail?id=" + goodsId + "&referrer=" + user.getId();
        try {
            byte[] bytes = maQrcodeService.createWxaCodeBytes(page, "release", width, false, new WxMaCodeLineColor("0", "0", "0"), true);

            String url = UploadFactory.build().uploadSuffix(bytes, ".png");

            return RestResponse.success().put("url", url);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取小程序码失败！");
        }
    }

    /**
     * 根据goodsId查询访问记录
     *
     * @param goodsId goodsId
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/history")
    @ApiOperation(value = "根据goodsId查询访问记录", notes = "根据goodsId查询访问记录")
    @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品ID", example = "1", required = true, dataType = "string")
    public RestResponse history(@RequestParam String goodsId) {

        Map<String, Object> params = new HashMap<>(4);
        params.put("goodsId", goodsId);

        // 查询当天的记录
        params.put("starTime", DateUtils.format(new Date()));

        Page<MallFootprintEntity> data = footprintService.queryPage(params);

        return RestResponse.success().put("data", data);
    }

    /**
     * 积分商品列表
     *
     * @param order    排序字段
     * @param sortType 排序类型
     * @param current  当前页码
     * @param limit    每页条数
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/integralGoodsList")
    @ApiOperation(value = "积分商品列表", notes = "积分商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "RETAIL_PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse integralGoodsList(String order, String sortType, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        Page<MallIntegralGoodsEntity> page = new Page<>(current, limit);

        IPage<MallIntegralGoodsEntity> data = integralGoodsService.page(page, new QueryWrapper<MallIntegralGoodsEntity>()
                .eq("IS_ON_SALE", 1).orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order).orderByDesc("SORT"));

        return RestResponse.success().put("data", data);
    }

    /**
     * 根据ID查询积分商品详情
     *
     * @param goodsId 积分商品ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/integralGoodsDetail")
    @ApiOperation(value = "积分商品详情", notes = "根据ID查询积分商品详情")
    @ApiImplicitParam(paramType = "query", name = "goodsId", value = "积分商品ID", example = "1", required = true, dataType = "string")
    public RestResponse integralGoodsDetail(@RequestParam String goodsId) {
        MallIntegralGoodsEntity info = integralGoodsService.getById(goodsId);
        return RestResponse.success().put("data", info);
    }
}
