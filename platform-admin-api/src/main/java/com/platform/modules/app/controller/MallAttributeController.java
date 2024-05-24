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
import com.platform.modules.app.dto.mall.MallAttrQueryDTO;
import com.platform.modules.mall.entity.MallAttributeEntity;
import com.platform.modules.mall.service.MallAttributeService;
import com.platform.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情参数Controller
 *
 * @author cxd
 * @since 2019-07-03 09:25:41
 */
@RestController
@RequestMapping("/app/attribute")
@Api(tags = "商品规格参数管理")
public class MallAttributeController extends AbstractController {
    @Autowired
    private MallAttributeService mallAttributeService;

    /**
     * 查看所有列表
     *
     * @param mallAttrQueryDTO 查询参数
     * @return RestResponse
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询规则列表", notes = "查询规则列表")
    public CommonRestResult queryAll() {
        List<MallAttributeEntity> list = mallAttributeService.queryAll(new HashMap<>());

        return CommonRestResult.success(list);
    }


}
