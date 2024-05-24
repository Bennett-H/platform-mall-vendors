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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.mall.service.MallShopsWithdrawService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家提现Controller
 *
 * @author cxd
 * @date 2020-05-05 08:56:53
 */
@RestController
@RequestMapping("/app/shopswithdraw")
public class MallShopsWithdrawController extends AbstractController {
    @Autowired
    private MallShopsWithdrawService mallShopsWithdrawService;
    @Autowired
    private MallShopsService mallShopsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:shopswithdraw:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallShopsWithdrawEntity> list = mallShopsWithdrawService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询商家提现
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:shopswithdraw:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallShopsWithdrawService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:shopswithdraw:info")
    public RestResponse info(@PathVariable("id") String id) {

        MallShopsWithdrawEntity mallShopsWithdraw = mallShopsWithdrawService.selectById(id);

        List<MallOrderEntity> relaOrderList = mallShopsWithdrawService.selectRelaOrderList(mallShopsWithdraw.getId());

        return RestResponse.success().put("shopswithdraw", mallShopsWithdraw).put("relaOrderList", relaOrderList);
    }


    /**
     * 店铺金额
     *
     * @return RestResponse
     */
    @RequestMapping("/withdrawDetail")
    @RequiresPermissions("mall:shopswithdraw:info")
    public RestResponse withdrawDetail() {
        MallShopsEntity shopsEntity = mallShopsService.getById(this.getShopsId());
        if (null == shopsEntity) {
            return RestResponse.error("当前账号没有关联的商铺");
        }
        Map<String, Object> result = new HashMap<>(4);
        //查询在途体现金额
//        // 可以提现
        Map<String, Object> params  = new HashMap<>();
        params.put("shopsId",this.getShopsId());
        params.put("applyStatus","1");
        List<MallShopsWithdrawEntity> mallShopsWithdrawEntities = mallShopsWithdrawService.queryAll(params);
        BigDecimal unconfirmApplyMoney =  mallShopsWithdrawEntities.stream().map(MallShopsWithdrawEntity::getApplyMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        shopsEntity.setAmountAvailable(shopsEntity.getAmountAvailable().subtract(unconfirmApplyMoney));
        //result.put("canWithdrawList", canWithdrawList);
        result.put("shopsEntity", shopsEntity);
        return RestResponse.success().put("data", result);
    }

    @RequestMapping("/cash")
    @RequiresPermissions("mall:shopswithdraw:save")
    public RestResponse cash(@RequestBody JSONObject jsonParam, @LoginUser SysMpUserEntity loginUser) {
        String shopsBankCardId = jsonParam.getString("shopsBankCardId");
        Double applyMoney = jsonParam.getDouble("applyMoney");
        MallShopsEntity shopsEntity = mallShopsService.getById(getShopsId());
        return mallShopsWithdrawService.withdrawCash(loginUser.getUserId(), getShopsId(), shopsEntity.getWithdrawUserId(), applyMoney, shopsBankCardId);
    }
}
