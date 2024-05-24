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

import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallShopsCategoryEntity;
import com.platform.modules.mall.service.MallShopsCategoryService;
import com.platform.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 店铺商品分类Controller
 *
 * @author cxd
 * @since 2019-07-03 16:23:39
 */
@RestController
@RequestMapping("/app/shopscategory")
public class MallShopsCategoryController extends AbstractController {
    @Autowired
    private MallShopsCategoryService mallShopsCategoryService;




    /**
     * 查看我的店铺下的商品分类列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryMyShopCategory")
    public RestResponse queryMyShopCategory(@RequestParam Map<String, Object> params) {
        getShopsScope(params);
        List<MallShopsCategoryEntity> list = mallShopsCategoryService.queryAll(params);

        return RestResponse.success().put("list", list);
    }
}
