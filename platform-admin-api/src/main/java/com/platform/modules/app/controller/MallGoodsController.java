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
import com.platform.common.utils.StringUtils;
import com.platform.common.validator.ValidatorUtils;
import com.platform.modules.app.dto.mall.MallGoodsQueryDTO;
import com.platform.modules.mall.entity.MallGoodsEntity;
import com.platform.modules.mall.entity.MallGoodsSkuEntity;
import com.platform.modules.mall.entity.MallShopsCategoryEntity;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.mall.service.MallGoodsSkuService;
import com.platform.modules.mall.service.MallShopsCategoryService;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.utils.SessionContextUtil;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品表Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 17:58:29
 */
@RestController
@RequestMapping("/app/goods")
@Api(tags = "商户商品管理")
public class MallGoodsController extends AbstractController {
    @Autowired
    private MallGoodsService mallGoodsService;
    @Autowired
    private MallShopsService mallShopsService;
    @Autowired
    private MallGoodsSkuService mallGoodsSkuService;
    @Autowired
    private MallShopsCategoryService shopsCategoryService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */

    /**
     * 分页查询商品表
     *
     * @param mallGoodsQueryDTO 查询参数
     * @return RestResponse
     */
    @ApiOperation(value = "查询商品信息列表", notes = "查询商品信息列表")
    @ApiImplicitParams({

    })
    @PostMapping("/queryMallGoods")
    public CommonRestResult queryMallGoods(@RequestBody  MallGoodsQueryDTO mallGoodsQueryDTO) {

        Map params = new HashMap();
        params.put("shopsCategoryId",mallGoodsQueryDTO.getShopsCategoryId());
        params.put("categoryIds",mallGoodsQueryDTO.getCategoryIds());
        params.put("shopsId", SessionContextUtil.getShopsId());
        params.put("name",mallGoodsQueryDTO.getGoodsName());
        List<MallGoodsEntity>  list = mallGoodsService.queryAll(params);
        return CommonRestResult.success(list);
    }

    @ApiOperation(value = "查询商品信息列表及sku规格", notes = "查询商品信息列表")
    @ApiImplicitParams({

    })
    @PostMapping("/queryMallGoodsSku")
    public CommonRestResult queryMallGoodsSku(@RequestBody  MallGoodsQueryDTO mallGoodsQueryDTO) {

        Map params = new HashMap();
        params.put("shopsCategoryId",mallGoodsQueryDTO.getShopsCategoryId());
        params.put("shopsId", SessionContextUtil.getShopsId());
        params.put("name",mallGoodsQueryDTO.getGoodsName());
        List<MallGoodsEntity>  list = mallGoodsService.queryAll(params);

        list.forEach(mallGoodsEntity -> {
            Map<String, Object> paramsBuf = new HashMap<>(2);
            paramsBuf.put("goodsId", mallGoodsEntity.getId());
            mallGoodsEntity.setGoodsSkuEntityList(mallGoodsSkuService.queryAll(paramsBuf));
        });

        return CommonRestResult.success(list);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @ApiOperation(value = "根据ID获取商品信息", notes = "根据ID获取商品信息")
    @GetMapping("/getGoodsInfoById")
    @RequiresPermissions("mall:goods:info")
    public CommonRestResult getGoodsInfoById( String id) {
        MallGoodsEntity mallGoods = mallGoodsService.queryById(id);

        return CommonRestResult.success(mallGoods);
    }

    /**
     * 新增商品表
     *
     * @param mallGoods mallGoods
     * @return RestResponse
     */
    @PostMapping("/save")
    @RequiresPermissions("mall:goods:save")
    @ApiOperation(value = "保存商品信息", notes = "保存商品信息")
    public RestResponse save(@RequestBody MallGoodsEntity mallGoods) {
        mallGoods.setShopsId(getShopsId());
        ValidatorUtils.validateEntity(mallGoods);
        if (StringUtils.isNotBlank(mallGoods.getShopsCategoryId())) {
            MallShopsCategoryEntity shopsCategoryEntity = shopsCategoryService.getById(mallGoods.getShopsCategoryId());
            mallGoods.setShopsId(shopsCategoryEntity.getShopsId());
        }
        mallGoods.setIsOnSale(0);
        mallGoodsService.add(mallGoods);

        return RestResponse.success();
    }

    /**
     * 修改商品表
     *
     * @param mallGoods mallGoods
     * @return RestResponse
     */
    @PostMapping("/update")
    @RequiresPermissions("mall:goods:update")
    @ApiOperation(value = "更新商品信息", notes = "更新商品信息")
    public RestResponse update(@RequestBody MallGoodsEntity mallGoods) {
        mallGoods.setShopsId(getShopsId());
        if (StringUtils.isNotBlank(mallGoods.getShopsCategoryId())&&StringUtils.isBlank(mallGoods.getShopsId())) {
            MallShopsCategoryEntity shopsCategoryEntity = shopsCategoryService.getById(mallGoods.getShopsCategoryId());
            mallGoods.setShopsId(shopsCategoryEntity.getShopsId());
        }
        ValidatorUtils.validateEntity(mallGoods);
        mallGoods.setIsOnSale(0);
        mallGoodsService.update(mallGoods);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品表
     *
     * @param ids ids
     * @return RestResponse
     */
    @PostMapping("/delete")
    @RequiresPermissions("mall:goods:delete")
    @ApiOperation(value = "删除商品", notes = "删除商品")
    public RestResponse delete(@RequestBody String[] ids) {
        mallGoodsService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 商品上架、下架
     *
     * @param ids ids
     * @return RestResponse
     */
    @PostMapping("/changeSale")
    @RequiresPermissions("mall:goods:changeSale")
    @ApiOperation(value = "商品上架", notes = "商品上架")
    public RestResponse changeSale(@RequestBody String[] ids) {
        mallGoodsService.changeSale(ids);

        return RestResponse.success();
    }
}
