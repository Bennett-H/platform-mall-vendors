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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallCategoryService;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.mall.service.MallKeywordsService;
import com.platform.modules.mall.service.MallSearchHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * AppSearchHistoryController
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @since 2017-03-23 15:31
 */
@Api(tags = "AppSearchHistoryController|搜索管理控制器")
@RestController
@RequestMapping("/app/search")
public class AppSearchHistoryController extends AppBaseController {
    @Autowired
    private MallKeywordsService keywordsService;
    @Autowired
    private MallSearchHistoryService searchHistoryService;
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallCategoryService categoryService;

    /**
     * 热搜
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("hotKeyword")
    @ApiOperation(value = "热搜", notes = "热搜")
    public RestResponse hotKeyword() {
        Map<String, Object> resultObj = new HashMap<>(2);
        //取出热闹关键词
        List<Map<String, Object>> hotKeywordList = keywordsService.listMaps(new QueryWrapper<MallKeywordsEntity>()
                .select("DISTINCT KEYWORD,IS_HOT,SCHEME_URL"));

        resultObj.put("hotKeywordList", hotKeywordList);
        return RestResponse.success(resultObj);
    }

    /**
     * 搜索历史记录
     *
     * @param loginUser 登录用户
     * @return
     */
    @GetMapping("history")
    @ApiOperation(value = "搜索历史记录", notes = "获取会员搜索历史记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse index(@LoginUser MallUserEntity loginUser) {
        Map<String, Object> resultObj = new HashMap<>(8);
        List<MallKeywordsEntity> keywordsEntityList = keywordsService.list(new QueryWrapper<MallKeywordsEntity>().eq("IS_DEFAULT", 1));
        //取出输入框默认的关键词
        MallKeywordsEntity defaultKeyword = null != keywordsEntityList && keywordsEntityList.size() > 0 ? keywordsEntityList.get(0) : null;
        //取出热闹关键词
        List<Map<String, Object>> hotKeywordList = keywordsService.listMaps(new QueryWrapper<MallKeywordsEntity>()
                .select("DISTINCT KEYWORD,IS_HOT,SCHEME_URL"));

        String[] historyKeywordList = null;
        if (null != loginUser) {
            List<MallSearchHistoryEntity> historyVoList = searchHistoryService.list(new QueryWrapper<MallSearchHistoryEntity>().select("distinct keyword").eq("USER_ID", loginUser.getId()));
            if (null != historyVoList && historyVoList.size() > 0) {
                historyKeywordList = new String[historyVoList.size()];
                int i = 0;
                for (MallSearchHistoryEntity historyVo : historyVoList) {
                    if (null != historyVo) {
                        historyKeywordList[i] = historyVo.getKeyword();
                    }
                    i++;
                }
            }
        }

        resultObj.put("defaultKeyword", defaultKeyword);
        resultObj.put("historyKeywordList", historyKeywordList);
        resultObj.put("hotKeywordList", hotKeywordList);
        return RestResponse.success(resultObj);
    }

    /**
     * 根据用户输入获取系统已有的关键字
     *
     * @param keyword 关键字
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("helper")
    @ApiOperation(value = "搜索关键字", notes = "根据用户输入获取系统已有的关键字")
    @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字", required = true, dataType = "string")
    public RestResponse helper(@RequestParam String keyword) {
        List<MallKeywordsEntity> keywords = keywordsService.list(new QueryWrapper<MallKeywordsEntity>()
                .select("DISTINCT KEYWORD").like(StringUtils.isNotBlank(keyword), "KEYWORD", keyword));
        String[] keys = null;
        if (null != keywords && keywords.size() > 0) {
            int i = 0;
            keys = new String[keywords.size()];
            for (MallKeywordsEntity keywordsVo : keywords) {
                keys[i] = keywordsVo.getKeyword();
                i++;
            }
        }
        return RestResponse.success().put("data", keys);
    }

    /**
     * 搜索商品
     *
     * @param loginUser        登录用户
     * @param type             类型：IS_HOT：热销商品；IS_NEW：新品推荐
     * @param keyword          关键词
     * @param searchFrom       1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序
     * @param categoryIds      商品分类ID
     * @param brandIds         品牌制造商ID
     * @param order            排序字段
     * @param sortType         排序类型
     * @param parentCategoryId parentCategoryId
     * @param minPrice         最低价格
     * @param maxPrice         最高价格
     * @param current          当前页码
     * @param limit            每页条数
     * @return RestResponse
     */
    @GetMapping("seachList")
    @ApiOperation(value = "搜索商品", notes = "根据条件搜索商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：IS_HOT：热销商品；IS_NEW：新品推荐", allowableValues = "IS_HOT,IS_NEW", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键词", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "searchFrom", example = "1", dataType = "int",
                    allowableValues = "1,2,3,4,5,6,7,", value = "1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序"),
            @ApiImplicitParam(paramType = "query", name = "categoryIds", value = "商品分类ID", example = "1", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "brandIds", value = "品牌制造商ID", example = "1", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "RETAIL_PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "parentCategoryId", value = "parentCategoryId", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "minPrice", value = "最低价格", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "maxPrice", value = "最高价格", example = "99999", dataType = "int")
    })
    public RestResponse seachList(@LoginUser MallUserEntity loginUser, String type, String keyword, Integer searchFrom, String[] categoryIds, String[] brandIds, String order, String sortType,
                                  String parentCategoryId,
                                  BigDecimal minPrice, BigDecimal maxPrice, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        if (StringUtils.isNotBlank(keyword) && null != loginUser) {
            MallSearchHistoryEntity searchHistoryEntity = new MallSearchHistoryEntity();
            searchHistoryEntity.setAddTime(new Date());
            searchHistoryEntity.setKeyword(keyword);
            searchHistoryEntity.setUserId(loginUser.getId());
            searchHistoryEntity.setSearchFrom(searchFrom);
            searchHistoryService.save(searchHistoryEntity);
        }

        Page<MallGoodsEntity> page = new Page<>(current, limit);

        IPage<MallGoodsEntity> data = goodsService.page(page, new QueryWrapper<MallGoodsEntity>().in(categoryIds.length > 0, "CATEGORY_ID", categoryIds)
                .in(brandIds.length > 0, "BRAND_ID", brandIds).eq(StringUtils.isNotBlank(type), type, 1).eq("IS_ON_SALE", 1)
                .exists(StringUtils.isNotBlank(parentCategoryId), "SELECT 1 FROM MALL_CATEGORY WHERE MALL_CATEGORY.ID = MALL_GOODS.CATEGORY_ID and MALL_CATEGORY.PARENT_ID = '" + parentCategoryId + "'")
                .ge(!StringUtils.isEmpty(minPrice) && minPrice.intValue() != 0, "RETAIL_PRICE", minPrice).le(!StringUtils.isEmpty(maxPrice) && maxPrice.intValue() != 0, "RETAIL_PRICE", maxPrice)
                .and(StringUtils.isNotBlank(keyword), wrapper -> wrapper.like("KEYWORDS", keyword).or().like("NAME", keyword))
                .orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order).orderByDesc("SORT"));
        //筛选的分类
        return RestResponse.success().put("data", data);
    }

    /**
     * 搜索历史记录
     *
     * @param loginUser  登录用户
     * @param keyword    关键词
     * @param searchFrom 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序
     * @param order      排序字段
     * @param sortType   排序类型
     * @param current    当前页码
     * @param limit      每页条数
     * @return RestResponse
     */
    @GetMapping("searchGoods")
    @ApiOperation(value = "搜索历史记录", notes = "获取会员搜索历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键词", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "searchFrom", example = "1", dataType = "int",
                    allowableValues = "1,2,3,4,5,6,7,", value = "1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序"),
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "品牌制造商ID", example = "1", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "RETAIL_PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse searchGoods(@LoginUser MallUserEntity loginUser, String keyword, Integer searchFrom, String categoryId, String order, String sortType,
                                    @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        if (StringUtils.isNotBlank(keyword)) {
            MallSearchHistoryEntity searchHistoryEntity = new MallSearchHistoryEntity();
            searchHistoryEntity.setAddTime(new Date());
            searchHistoryEntity.setKeyword(keyword);
            searchHistoryEntity.setUserId(loginUser.getId());
            searchHistoryEntity.setSearchFrom(searchFrom);
            searchHistoryService.save(searchHistoryEntity);
        }

        Page<MallGoodsEntity> page = new Page<>(current, limit);

        IPage<MallGoodsEntity> data = goodsService.page(page, new QueryWrapper<MallGoodsEntity>().eq(StringUtils.isNotBlank(categoryId), "CATEGORY_ID", categoryId)
                .and(StringUtils.isNotBlank(keyword), wrapper -> wrapper.like("KEYWORDS", keyword).or().like("NAME", keyword))
                .eq("IS_ON_SALE", 1)
                .orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order).orderByDesc("SORT"));

        List<String> categoryIds = new ArrayList<>();
        for (MallGoodsEntity goodsVo : data.getRecords()) {
            categoryIds.add(goodsVo.getCategoryId());
        }
        //筛选的分类
        List<MallCategoryEntity> filterCategory = categoryService.getCategoryByData(categoryIds);
        return RestResponse.success().put("data", data).put("filterCategory", filterCategory);
    }

    /**
     * 清空当前用户搜索记录
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @PostMapping("clearHistory")
    @ApiOperation(value = "清空当前用户搜索记录", notes = "清空当前用户搜索记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse clearHistory(@LoginUser MallUserEntity loginUser) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("USER_ID", loginUser.getId());
        searchHistoryService.removeByMap(params);
        return RestResponse.success();
    }
}
