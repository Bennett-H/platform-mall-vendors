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
import com.platform.modules.mall.dto.goods.SaveGoodsStockReqDTO;
import com.platform.modules.mall.entity.MallGoodsSkuEntity;
import com.platform.modules.mall.service.MallGoodsSkuService;
import com.platform.modules.sys.controller.AbstractController;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品SKUController
 *
 * @author 李鹏军
 * @since 2019-07-04 00:05:33
 */
@RestController
@RequestMapping("/app/goodssku")
@Api(tags = "商户商品SKU管理")
public class MallGoodsSkuController extends AbstractController {
    @Autowired
    private MallGoodsSkuService mallGoodsSkuService;


    /**
     * 分页查询商品SKU
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @ApiOperation(value = "查询商品SKU信息列表", notes = "查询商品SKU信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "mobile"),
                    @ExampleProperty(mediaType = "code", value = "code")
            }), required = true, dataType = "string")
    })
    @GetMapping("/list")
    @RequiresPermissions("mall:goodssku:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallGoodsSkuService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("mall:goodssku:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallGoodsSkuEntity mallGoodsSku = mallGoodsSkuService.getById(id);

        return RestResponse.success().put("goodssku", mallGoodsSku);
    }

    /**
     * 新增商品SKU
     *
     * @param mallGoodsSku mallGoodsSku
     * @return RestResponse
     */
    @PostMapping("/save")
    @RequiresPermissions("mall:goodssku:save")
    public RestResponse save(@RequestBody MallGoodsSkuEntity mallGoodsSku) {

        mallGoodsSkuService.add(mallGoodsSku);

        return RestResponse.success();
    }

    /**
     * 修改商品SKU
     *
     * @param mallGoodsSku mallGoodsSku
     * @return RestResponse
     */
    @PostMapping("/update")
    @RequiresPermissions("mall:goodssku:update")
    public RestResponse update(@RequestBody MallGoodsSkuEntity mallGoodsSku) {

        mallGoodsSkuService.update(mallGoodsSku);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品SKU
     *
     * @param ids ids
     * @return RestResponse
     */
    @PostMapping("/delete")
    @RequiresPermissions("mall:goodssku:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallGoodsSkuService.deleteBatch(ids);

        return RestResponse.success();
    }


    /**
     * 根据goodsId查询商品
     *
     * @param goodsId
     * @return
     */
    @GetMapping("/queryByGoodsId")
    @ApiOperation(value = "根据商品信息查询SKU列表", notes = "根据商品信息查询SKU列表")
    public CommonRestResult<List<MallGoodsSkuEntity>> queryByGoodsId(String goodsId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("goodsId", goodsId);
        List<MallGoodsSkuEntity> list = mallGoodsSkuService.queryAll(params);

        return CommonRestResult.success(list);
    }


    @PostMapping("/saveGoodsStock")
    @ApiOperation(value = "保存商品库存数据", notes = "保存商品库存数据")
    public CommonRestResult saveGoodsStock(@RequestBody List<SaveGoodsStockReqDTO> productList) {
        mallGoodsSkuService.saveGoodsStock(productList);

        return CommonRestResult.success(null);
    }

    /**
     * 保存
     */
    @PostMapping("/saveGoodsProduct")
    @ApiOperation(value = "保存商品信息", notes = "保存商品信息")
    public CommonRestResult saveGoodsProduct(@RequestBody List<MallGoodsSkuEntity> productList) {
        mallGoodsSkuService.saveGoodsProduct(productList);

        return CommonRestResult.success(null);
    }
}
