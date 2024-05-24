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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.CommonRestResult;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.modules.app.dto.mall.MallCategoryQueryDTO;
import com.platform.modules.mall.entity.MallCategoryEntity;
import com.platform.modules.mall.entity.MallGoodsEntity;
import com.platform.modules.mall.entity.MallShopsCategoryEntity;
import com.platform.modules.mall.service.MallCategoryService;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.mall.service.MallShopsCategoryService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.utils.SessionContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类Controller
 *
 * @author cxd
 * @since 2019-07-02 19:53:21
 */
@RestController
@RequestMapping("/app/category")
@Api(tags = "商户商品分类管理")
public class MallCategoryController extends AbstractController {
    @Autowired
    private MallShopsCategoryService mallShopsCategoryService;

    @Autowired
    private MallCategoryService mallCategoryService;

    @Autowired
    private MallGoodsService mallGoodsService;


    /**
     * 查询商户分类
     *
     * @param mallCategoryQueryDTO
     * @return RestResponse
     */
    @PostMapping("/getMallCategory")
    @RequiresPermissions("mall:category:getMallCategory")
    @ApiOperation(value = "获取商品分类", notes = "获取商品分类")
    public CommonRestResult<List<MallShopsCategoryEntity>> getMallCategory(@RequestBody MallCategoryQueryDTO mallCategoryQueryDTO) {

        Map params = new HashMap();
        params.put("shopId", SessionContextUtil.getShopsId());
        params.put("parentId",mallCategoryQueryDTO.getParentId());
        List<MallShopsCategoryEntity> list = mallShopsCategoryService.queryAll(params);
        return CommonRestResult.success(list);
    }

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public CommonRestResult<List<MallCategoryEntity>> queryAll(@RequestParam Map<String, Object> params) {
        List<MallCategoryEntity> list = mallCategoryService.queryAll(params);
        return CommonRestResult.success(list);
    }


    /**
     * 新增商品分类
     *
     * @param mallCategory mallCategory
     * @return RestResponse
     */
    @PostMapping("/save")
    @RequiresPermissions("mall:category:save")
    @ApiOperation(value = "保存商品分类", notes = "保存商品分类")
    public CommonRestResult save(@RequestBody MallShopsCategoryEntity mallCategory) {
        ValidatorUtils.validateEntity(mallCategory);

        mallShopsCategoryService.add(mallCategory);
        return CommonRestResult.success(null);
    }

    /**
     * 修改商品分类
     *
     * @param mallCategory mallCategory
     * @return RestResponse
     */
    @PostMapping("/update")
    @RequiresPermissions("mall:category:update")
    @ApiOperation(value = "修改商品分类", notes = "修改商品分类")
    public CommonRestResult update(@RequestBody MallShopsCategoryEntity mallCategory) {
        ValidatorUtils.validateEntity(mallCategory);

        mallShopsCategoryService.update(mallCategory);
        return CommonRestResult.success(null);
    }

}
