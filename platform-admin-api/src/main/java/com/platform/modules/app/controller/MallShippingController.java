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

import com.platform.common.CommonRestResult;
import com.platform.common.utils.RestResponse;
import com.platform.modules.app.dto.mall.ShippingQueryDTO;
import com.platform.modules.mall.entity.MallShippingEntity;
import com.platform.modules.mall.service.MallShippingService;
import com.platform.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快递公司配置Controller
 *
 * @author cxd
 * @since 2019-07-03 16:57:09
 */
@RestController
@RequestMapping("/app/shipping")
@Api(tags = "快递公司管理")
public class MallShippingController extends AbstractController {
    @Autowired
    private MallShippingService mallShippingService;

    /**
     * 查看所有列表
     *
     * @param shippingQueryDTO 查询参数
     * @return RestResponse
     */
    @PostMapping("/getShippingList")
    @ApiOperation(value = "查询商品信息列表", notes = "查询商品信息列表")
    public CommonRestResult getShippingList(@RequestBody ShippingQueryDTO shippingQueryDTO) {

        Map<String, Object> params = new HashMap<>();
        params.put("name",shippingQueryDTO.getName());
        List<MallShippingEntity> list = mallShippingService.queryAll(params);

        return CommonRestResult.success(list);
    }


}
