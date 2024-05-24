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
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallRoomEntity;
import com.platform.modules.mall.service.MallRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/live")
@Api(tags = "AppLiveController|直播接口")
public class AppLiveController extends AppBaseController {
    @Autowired
    MallRoomService roomService;

    /**
     * 直播间列表
     *
     * @param current 当前页码
     * @param limit   每页条数
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/roomList")
    @ApiOperation(value = "直播间列表", notes = "直播间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse roomList(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {

        Map<String, Object> params = new HashMap<>(8);
        params.put("page", current);
        params.put("limit", limit);

        Page<MallRoomEntity> page = roomService.queryPage(params);

        return RestResponse.success().put("data", page);
    }
}
