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
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallCategoryEntity;
import com.platform.modules.mall.service.MallCategoryService;
import com.platform.modules.mall.vo.CascaderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
@RequestMapping("/app/category")
@Api(tags = "AppCategoryController|商品分类列表接口")
public class AppCategoryController extends AppBaseController {
    @Autowired
    private MallCategoryService categoryService;


    /**
     * 商品分类列表
     *
     * @param level 分类等级
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/categoryList")
    @ApiOperation(value = "商品分类列表", notes = "商品分类列表")
    @ApiImplicitParam(paramType = "query", name = "level", value = "分类等级", required = true, dataType = "string", allowableValues = "1,2,3", example = "1")
    public RestResponse categoryList(@RequestParam String level) {
        QueryWrapper<MallCategoryEntity> categoryListWrapper = new QueryWrapper<>();

        //查询列表数据
        List<MallCategoryEntity> data = categoryService.queryAllByWrapper(categoryListWrapper.eq("LEVEL", level).eq("IS_SHOW", 1).orderByDesc("SORT"));

        return RestResponse.success().put("data", data);
    }

    /**
     * 根据商品分类ID获取分类下的所有子分类
     *
     * @param id 商品分类ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("current")
    @ApiOperation(value = "子商品分类", notes = "根据商品分类ID获取分类下的所有子分类")
    @ApiImplicitParam(paramType = "query", name = "id", value = "商品分类ID", required = true, dataType = "string", example = "1")
    public RestResponse current(@RequestParam String id) {
        MallCategoryEntity currentCategory = categoryService.getById(id);
        //获取子分类数据
        if (null != currentCategory) {
            Map<String, Object> params = new HashMap<>(4);
            params.put("isShow", 1);
            params.put("parentId", id);
            currentCategory.setSubCategoryList(categoryService.queryAll(params));
        }
        return RestResponse.success().put("data", currentCategory);
    }

    /**
     * 商品分类列表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/categoryCascaderList")
    @ApiOperation(value = "商品分类列表", notes = "商品分类列表")
    public RestResponse categoryCascaderList() {
        Map<String, Object> params = new HashMap<>(4);
        List<CascaderVO> result = new ArrayList<>();
        List<MallCategoryEntity> list = categoryService.queryAll(params);
        //将所有节点存入到map集合
        Map<String, CascaderVO> map = new HashMap<>(4);
        Iterator<MallCategoryEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            MallCategoryEntity item = iterator.next();
            //将所有id做为key，Area对象做值，存入到map集合
            map.put(item.getId(), categoryToCascaderVO(item));
        }

        for (CascaderVO item : map.values()) {
            //子节点
            if (!"0".equals(item.getPid())) {
                //通过子节点的pid获取父节点
                CascaderVO parent = map.get(item.getPid());
                //将子节点放入父节点中
                parent.getChildren().add(item);
            }
        }

        for (CascaderVO item : map.values()) {
            //取出所有的父节点
            if ("0".equals(item.getPid())) {
                result.add(item);
            }
        }
        for (CascaderVO item : map.values()) {
            //取出所有的父节点
            if (CollectionUtils.isEmpty(item.getChildren())) {
                item.setChildren(null);
            }
        }
        return RestResponse.success().put("data", result);
    }

    /**
     * 转成级联实体
     *
     * @param categoryEntity
     * @return CascaderVO
     */
    private CascaderVO categoryToCascaderVO(MallCategoryEntity categoryEntity) {
        CascaderVO item = new CascaderVO();
        item.setLabel(categoryEntity.getName());
        item.setValue(categoryEntity.getId());
        item.setPid(categoryEntity.getParentId());
        item.setData(categoryEntity);
        return item;
    }

    /**
     * 分类列表
     *
     * @param id 分类ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/childList")
    @ApiOperation(value = "子分类列表", notes = "子分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "level", value = "分类ID", required = true, dataType = "string", example = "1")
    })
    public RestResponse childList(@RequestParam String id) {
        //查询列表数据
        List<MallCategoryEntity> data = categoryService.list(new QueryWrapper<MallCategoryEntity>().eq("PARENT_ID", id).eq("IS_SHOW", 1));

        return RestResponse.success().put("data", data);
    }

    /**
     * 根据商品分类ID获取分类下的所有子分类
     *
     * @param id 商品分类ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("sameLevelCategoryList")
    @ApiOperation(value = "同级分类", notes = "根据分类ID获取同级所有分类")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "商品分类ID", required = true, dataType = "string", example = "1")
    })
    public RestResponse sameLevelCategoryList(@RequestParam String id) {
        MallCategoryEntity currentCategory = categoryService.getById(id);
        //获取子分类数据
        if (null != currentCategory) {
            Map<String, Object> params = new HashMap<>(4);
            params.put("isShow", 1);
            params.put("parentId", currentCategory.getParentId());
            return RestResponse.success().put("data", categoryService.queryAll(params));
        }
        return RestResponse.success().put("data", null);
    }
}
