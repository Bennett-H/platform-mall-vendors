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

import com.platform.annotation.LoginUser;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.SysMpUserEntity;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.service.SysConfigService;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.modules.sys.service.SysUserRoleService;
import com.platform.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 店铺Controller
 *
 * @author cxd
 * @since 2019-07-03 13:51:29
 */
@Slf4j
@RestController
@RequestMapping("/app/shops")
@Api(tags = "店铺信息")
public class MallShopsController extends AbstractController {
    @Autowired
    private MallShopsService mallShopsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysSmsLogService sysSmsLogService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysSmsLogService smsLogService;
    @Autowired
    private MallUserService mallUserService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;


    /**
     * 查询我的所有店铺
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryMyShop")
    public RestResponse queryMyShop(@RequestParam Map<String, Object> params, @LoginUser SysMpUserEntity loginUser) {
        String userId = loginUser.getUserId();
        //超级管理员查看所有
        if (!Constant.SUPER_ADMIN.equals(userId)) {
            params.put("userId", userId);
        }
        List<MallShopsEntity> list = mallShopsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }


}
