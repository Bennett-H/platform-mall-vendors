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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallBankTypeService;
import com.platform.modules.mall.service.MallShopsBankCardService;
import com.platform.modules.sys.controller.AbstractController;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户银行卡表Controller
 *
 * @author 李鹏军
 * @since 2024-03-24 09:04:12
 */
@RestController
@RequestMapping("/app/shopsbankcard")
public class MallShopsBankCardController extends AbstractController {
    @Autowired
    private MallShopsBankCardService mallShopsBankCardService;

    @Autowired
    private MallBankTypeService mallBankTypeService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:shopsbankcard:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        params.put("shopsId",this.getShopsId());
        List<MallShopsBankCardEntity> list = mallShopsBankCardService.queryAll(params);

        //隐藏银行卡号，只显示后四位
        for (MallShopsBankCardEntity entity : list) {
            String cardNumber = entity.getCardNumber();
            if (StringUtils.isNotBlank(cardNumber) && cardNumber.length() > 4) {
                entity.setCardNumber("**** **** **** " + cardNumber.substring(cardNumber.length() - 4));
            }
        }

        return RestResponse.success().put("list", list);
    }

    @GetMapping("/bankTypeList")
    @RequiresPermissions("mall:shopsbankcard:list")
    @ApiOperation(value = "获取银行卡类型表", notes = "获取银行卡类型表")
    public RestResponse bankTypeList() {
        List<MallBankTypeEntity> list = mallBankTypeService.list(
                new QueryWrapper<>());
        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询用户银行卡表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:shopsbankcard:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        params.put("shopsId",this.getShopsId());
        Page page = mallShopsBankCardService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:shopsbankcard:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallShopsBankCardEntity mallShopsBankCard = mallShopsBankCardService.getById(id);

        return RestResponse.success().put("shopsbankcard", mallShopsBankCard);
    }

    /**
     * 绑定银行卡
     *
     * @param user      登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/bindingCard")
    @ApiOperation(value = "绑定银行卡", notes = "绑定银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "cardName", value = "收款人姓名"),
                    @ExampleProperty(mediaType = "cardNumber", value = "银行卡号"),
                    @ExampleProperty(mediaType = "cardType", value = "卡类型"),
                    @ExampleProperty(mediaType = "phone", value = "手机号码"),
                    @ExampleProperty(mediaType = "bankTypeId", value = "银行类型ID")
            }), required = true, dataType = "string")
    })
    public RestResponse bindingCard(@RequestBody JSONObject jsonParam) {
        String cardName = StringUtils.trimToNull(jsonParam.getString("cardName"));
        String cardNumber = StringUtils.trimToNull(jsonParam.getString("cardNumber"));
        String cardType = StringUtils.trimToNull(jsonParam.getString("cardType"));
        String bankTypeId = StringUtils.trimToNull(jsonParam.getString("bankTypeId"));
        String phone = StringUtils.trimToNull(jsonParam.getString("phone"));
        if (cardName == null || cardNumber == null || bankTypeId == null) {
            throw new BusinessException("信息不全！");
        }
        // 先前是否绑定过
        MallShopsBankCardEntity entity = mallShopsBankCardService.getOne(new LambdaQueryWrapper<MallShopsBankCardEntity>().eq(MallShopsBankCardEntity::getCardNumber, cardNumber).eq(MallShopsBankCardEntity::getShopsId, getShopsId()));
        if (null != entity) {
            // 是解绑状态就恢复
            if (entity.getCardStatus() == 2) {
                entity.setShopsId(getShopsId());
                entity.setCardName(cardName);
                entity.setCardNumber(cardNumber);
                entity.setCardType(cardType);
                entity.setBankTypeId(Integer.valueOf(bankTypeId));
                entity.setCardStatus(Constant.CardStatus.YBD.getValue());
                mallShopsBankCardService.update(entity);
                return RestResponse.success();
            }
            throw new BusinessException("已绑定当前卡！");
        }


        MallShopsBankCardEntity mallShopsBankCardEntity = new MallShopsBankCardEntity();
        mallShopsBankCardEntity.setShopsId(getShopsId());
        mallShopsBankCardEntity.setCardName(cardName);
        mallShopsBankCardEntity.setPhone(phone);
        mallShopsBankCardEntity.setCardNumber(cardNumber);
        mallShopsBankCardEntity.setCardType(cardType);
        mallShopsBankCardEntity.setBankTypeId(Integer.valueOf(bankTypeId));
        mallShopsBankCardEntity.setCardStatus(Constant.CardStatus.YBD.getValue());
        mallShopsBankCardService.add(mallShopsBankCardEntity);

        return RestResponse.success();
    }

    /**
     * 解绑银行卡
     *
     * @param user      user
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("unbindingCard")
    @ApiOperation(value = "解绑银行卡", notes = "解绑银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "id", value = "ID")
            }), required = true, dataType = "string")
    })
    public RestResponse unbindingCard(@LoginUser MallUserEntity user, @RequestBody JSONObject jsonParam) {
        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        // 绑定的银行卡列id
        String id = StringUtils.trimToNull(jsonParam.getString("id"));
        if (id == null) {
            throw new BusinessException("解绑信息有误，请稍后再尝试！");
        }

        MallShopsBankCardEntity cardEntity = mallShopsBankCardService.getById(id);
        if (cardEntity == null) {
            throw new BusinessException("解绑信息有误，请稍后再尝试！");
        }
        if (!cardEntity.getShopsId().equals(this.getShopsId())) {
            throw new BusinessException("越权，不可解绑他人银行卡");
        }

        cardEntity.setCardStatus(Constant.CardStatus.YJB.getValue());
        mallShopsBankCardService.update(cardEntity);

        return RestResponse.success();
    }

    /**
     * 新增用户银行卡表
     *
     * @param mallShopsBankCard mallShopsBankCard
     * @return RestResponse
     */
    @RequestMapping("/save")
    @RequiresPermissions("mall:shopsbankcard:save")
    public RestResponse save(@RequestBody MallShopsBankCardEntity mallShopsBankCard) {
        mallShopsBankCard.setShopsId(this.getShopsId());
        mallShopsBankCardService.add(mallShopsBankCard);

        return RestResponse.success();
    }

    /**
     * 修改用户银行卡表
     *
     * @param mallShopsBankCard mallShopsBankCard
     * @return RestResponse
     */
    @RequestMapping("/update")
    @RequiresPermissions("mall:shopsbankcard:update")
    public RestResponse update(@RequestBody MallShopsBankCardEntity mallShopsBankCard) {

        mallShopsBankCardService.update(mallShopsBankCard);

        return RestResponse.success();
    }

    /**
     * 根据主键删除用户银行卡表
     *
     * @param ids ids
     * @return RestResponse
     */
    @RequestMapping("/delete")
    @RequiresPermissions("mall:shopsbankcard:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallShopsBankCardService.deleteBatch(ids);

        return RestResponse.success();
    }
}
