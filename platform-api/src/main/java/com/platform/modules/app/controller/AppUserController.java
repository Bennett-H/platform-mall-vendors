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

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.common.validator.AbstractAssert;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author cxd
 */
@RestController
@RequestMapping("/app/user")
@Api(tags = "AppUserController|APP用户接口")
public class AppUserController {
    @Autowired
    private MallUserLevelService userLevelService;
    @Autowired
    private MallFootprintService footprintService;
    @Autowired
    private MallCollectService collectService;
    @Autowired
    private MallFeedbackService feedbackService;
    @Autowired
    private MallAccountLogService accountLogService;
    @Autowired
    private MallDistService mallDistService;
    @Autowired
    private MallUserBankCardService mallUserBankCardService;
    @Autowired
    private MallBankTypeService mallBankTypeService;
    @Autowired
    private MallPayFaceToFaceService payFaceToFaceService;
    @Autowired
    private MallIntegralLogService mallIntegralLogService;
    @Autowired
    private MallUserService userService;

    /**
     * 根据token获取当前登录用户信息
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取登录用户信息", notes = "根据token获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse userInfo(@LoginUser MallUserEntity user) {
        MallUserLevelEntity levelEntity = userLevelService.getById(user.getUserLevelId());
        if (null != levelEntity) {
            user.setUserLevelName(levelEntity.getName());
        } else {
            user.setUserLevelName("");
        }
        MallDistEntity mallDistEntity = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", user.getId()).eq("IS_AUDIT", 1));
        user.setIsDistributor(mallDistEntity != null);
        Integer securityLevel = 0;
        if (StringUtils.isNotBlank(user.getOpenId())) {
            securityLevel = 2;
        } else if (StringUtils.isNotBlank(user.getPassword()) && !user.getPassword().equals(DigestUtils.sha256Hex("123456"))) {
            securityLevel = 1;
        }
        return RestResponse.success().put("data", user).put("securityLevel", securityLevel);
    }

    /**
     * 查询当前登录用户足迹列表
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/footprintList")
    @ApiOperation(value = "查询当前登录用户足迹列表", notes = "查询当前登录用户足迹列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse footprintList(@LoginUser MallUserEntity user, @RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer limit) {
        if (user == null) {
            return RestResponse.success().put("data", new Page<MallFootprintEntity>());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        params.put("page", current);
        params.put("limit", limit);
        params.put("isOnSale", "1");

        IPage data = footprintService.queryPage(params);

        return RestResponse.success().put("data", data);
    }

    /**
     * 删除足迹
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/deleteFootPrint")
    @ApiOperation(value = "删除足迹", notes = "用户删除足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "footprintId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse deleteFootPrint(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String footprintId = jsonParam.getString("footprintId");
        if (footprintId == null) {
            return RestResponse.error("删除出错");
        }
        MallFootprintEntity footprintEntity = footprintService.getById(footprintId);
        //
        if (loginUser == null || loginUser.getId() == null || footprintEntity == null || footprintEntity.getGoodsId() == null) {
            return RestResponse.error("删除出错");
        }

        Map<String, Object> param = new HashMap<>(4);
        param.put("USER_ID", loginUser.getId());
        param.put("GOODS_ID", footprintEntity.getGoodsId());
        footprintService.removeByMap(param);

        return RestResponse.success("删除成功");
    }

    /**
     * 当前登录用户收藏列表
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/collectList")
    @ApiOperation(value = "当前登录用户收藏列表", notes = "当前登录用户收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
    })
    public RestResponse collectList(@LoginUser MallUserEntity user) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", user.getId());
        List<MallCollectEntity> dataList = collectService.queryAll(params);
        return RestResponse.success().put("data", dataList);
    }

    /**
     * 添加或删除用户收藏
     *
     * @param user      登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/addOrDelete")
    @ApiOperation(value = "添加或删除用户收藏", notes = "添加或删除用户收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "goodsId", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse addOrDelete(@LoginUser MallUserEntity user, @RequestBody JSONObject jsonParam) {
        String goodsId = jsonParam.getString("goodsId");

        List<MallCollectEntity> collectEntities = collectService.list(
                new QueryWrapper<MallCollectEntity>().eq("USER_ID", user.getId()).eq("GOODS_ID", goodsId));
        //
        boolean collectRes;
        String handleType = "add";
        if (null == collectEntities || collectEntities.size() < 1) {
            MallCollectEntity collectEntity = new MallCollectEntity();
            collectEntity.setAddTime(new Date());
            collectEntity.setGoodsId(goodsId);
            collectEntity.setUserId(user.getId());
            //添加收藏
            collectRes = collectService.save(collectEntity);
        } else {
            //取消收藏
            collectRes = collectService.delete(collectEntities.get(0).getId());
            handleType = "delete";
        }

        if (collectRes) {
            Map<String, Object> data = new HashMap<>(2);
            data.put("type", handleType);
            return RestResponse.success().put("data", data);
        }
        return RestResponse.error("操作失败");
    }

    /**
     * 添加反馈
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("saveFeedBack")
    @ApiOperation(value = "添加反馈", notes = "用户添加反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "feedType", value = "反馈类型 1:商品相关, 2:物流状况, 3:客户服务,4:优惠活动, 5:功能异常, 6:产品建议, 7:其他"),
                    @ExampleProperty(mediaType = "content", value = "反馈的内容")
            }), required = true, dataType = "string")
    })
    public RestResponse saveFeedBack(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        MallFeedbackEntity feedbackVo = new MallFeedbackEntity();
        feedbackVo.setUserId(loginUser.getId());
        feedbackVo.setMobile(jsonParam.getString("mobile"));
        feedbackVo.setFeedType(jsonParam.getInteger("feedType"));
        feedbackVo.setStatus(1);
        feedbackVo.setContent(jsonParam.getString("content"));
        feedbackVo.setAddTime(new Date());
        feedbackService.save(feedbackVo);
        return RestResponse.success();
    }

    /**
     * 当前登录用户账户余额变动记录
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("accountLogList")
    @ApiOperation(value = "当前登录用户账户余额变动记录", notes = "当前登录用户账户余额变动记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse accountLogList(@LoginUser MallUserEntity user) {
        List<MallAccountLogEntity> accountLogEntityList = accountLogService.list(
                new QueryWrapper<MallAccountLogEntity>().eq("USER_ID", user.getId()).gt("TYPE", 0).orderByDesc("ADD_TIME"));
        return RestResponse.success().put("data", accountLogEntityList);
    }

    /**
     * 当前登录用户账户余额变动记录
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("integralLogList")
    @ApiOperation(value = "当前登录用户账户余额变动记录", notes = "当前登录用户账户余额变动记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse integralLogList(@LoginUser MallUserEntity user) {
        List<MallIntegralLogEntity> integralLogEntities = mallIntegralLogService.list(
                new QueryWrapper<MallIntegralLogEntity>().eq("USER_ID", user.getId()).gt("TYPE", 0).orderByDesc("ADD_TIME"));
        return RestResponse.success().put("data", integralLogEntities);
    }

    /**
     * 当前登录用户当面付记录
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("payFaceToFaceList")
    @ApiOperation(value = "当前登录用户当面付记录", notes = "当前登录用户当面付记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse payFaceToFaceList(@LoginUser MallUserEntity user) {
        Map<String, Object> params = new HashMap<>();
        params.put("order", "DESC");
        params.put("userId", user.getId());
        params.put("payStatus", 3);
        List<MallPayFaceToFaceEntity> payFaceToFaceEntities = payFaceToFaceService.queryAll(params);
        return RestResponse.success().put("data", payFaceToFaceEntities);
    }

    /**
     * 当前登录用户银行卡列表
     *
     * @param user user
     * @return RestResponse
     */
    @GetMapping("/getBankCard")
    @ApiOperation(value = "当前登录用户银行卡列表", notes = "当前登录用户银行卡列表")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse getBankCard(@LoginUser MallUserEntity user) {
        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        List<MallUserBankCardEntity> list = mallUserBankCardService.getCardList(
                new QueryWrapper<MallUserBankCardEntity>()
                        .eq("USER_ID", userId)
                        .eq("CARD_STATUS", Constant.CardStatus.YBD.getValue())
        );

        list.forEach(r -> {
            String cardNumber = r.getCardNumber();
            int length = cardNumber.length();
            r.setCardNumber(cardNumber.substring(length - 4));
        });

        return RestResponse.success().put("data", list);
    }

    /**
     * 绑定银行卡
     *
     * @param user      登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("bindingCard")
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
    public RestResponse bindingCard(@LoginUser MallUserEntity user, @RequestBody JSONObject jsonParam) {

        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }

        String cardName = StringUtils.trimToNull(jsonParam.getString("cardName"));
        String cardNumber = StringUtils.trimToNull(jsonParam.getString("cardNumber"));
        String cardType = StringUtils.trimToNull(jsonParam.getString("cardType"));
        String bankTypeId = StringUtils.trimToNull(jsonParam.getString("bankTypeId"));
        String phone = StringUtils.trimToNull(jsonParam.getString("phone"));
        if (cardName == null || cardNumber == null || bankTypeId == null) {
            throw new BusinessException("信息不全！");
        }
        // 先前是否绑定过
        MallUserBankCardEntity entity = mallUserBankCardService.getOne(new QueryWrapper<MallUserBankCardEntity>().eq("CARD_NUMBER", cardNumber).eq("USER_ID", userId));
        if (null != entity) {
            // 是解绑状态就恢复
            if (entity.getCardStatus() == 2) {
                entity.setUserId(userId);
                entity.setCardName(cardName);
                entity.setCardNumber(cardNumber);
                entity.setCardType(cardType);
                entity.setBankTypeId(Integer.valueOf(bankTypeId));
                entity.setCardStatus(Constant.CardStatus.YBD.getValue());
                mallUserBankCardService.update(entity);
                return RestResponse.success();
            }
            throw new BusinessException("已绑定当前卡！");
        }


        MallUserBankCardEntity mallUserBankCardEntity = new MallUserBankCardEntity();
        mallUserBankCardEntity.setUserId(userId);
        mallUserBankCardEntity.setCardName(cardName);
        mallUserBankCardEntity.setPhone(phone);
        mallUserBankCardEntity.setCardNumber(cardNumber);
        mallUserBankCardEntity.setCardType(cardType);
        mallUserBankCardEntity.setBankTypeId(Integer.valueOf(bankTypeId));
        mallUserBankCardEntity.setCardStatus(Constant.CardStatus.YBD.getValue());
        mallUserBankCardService.add(mallUserBankCardEntity);

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

        MallUserBankCardEntity cardEntity = mallUserBankCardService.getById(id);
        if (cardEntity == null) {
            throw new BusinessException("解绑信息有误，请稍后再尝试！");
        }
        if (!cardEntity.getUserId().equals(userId)) {
            throw new BusinessException("越权，不可解绑他人银行卡");
        }

        cardEntity.setCardStatus(Constant.CardStatus.YJB.getValue());
        mallUserBankCardService.update(cardEntity);

        return RestResponse.success();
    }

    /**
     * 获取银行卡类型表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("bankTypeList")
    @ApiOperation(value = "获取银行卡类型表", notes = "获取银行卡类型表")
    public RestResponse bankTypeList() {
        List<MallBankTypeEntity> list = mallBankTypeService.list(
                new QueryWrapper<>());
        return RestResponse.success().put("data", list);
    }

    /**
     * 修改用户信息
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("modifyUser")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userName", value = "admin"),
                    @ExampleProperty(mediaType = "gender", value = "1"),
                    @ExampleProperty(mediaType = "birthday", value = "2020-10-10")
            }), required = true, dataType = "string")
    })
    public RestResponse modifyUser(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String userName = jsonParam.getString("userName");
        AbstractAssert.isBlank(userName, "用户名称不能为空");

        MallUserEntity user = new MallUserEntity();
        user.setUserName(userName);
        //Nickname 也同时修改
        user.setNickname(userName);
        user.setId(loginUser.getId());
        Integer gender = jsonParam.getInteger("gender");
        String birthday = jsonParam.getString("birthday");
        if (null != gender) {
            user.setGender(gender);
        }
        if (StringUtils.isNotBlank(birthday)) {
            user.setBirthday(DateUtils.strToDate(birthday));
        }
        userService.updateById(user);
        user = userService.getById(loginUser.getId());
        return RestResponse.success().put("userInfo", user);
    }

    /**
     * 修改用户信息(referUserId)
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("modifyReferUser")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "referUserId", value = "绑定分销用户id"),
                    @ExampleProperty(mediaType = "gender", value = "1"),
                    @ExampleProperty(mediaType = "birthday", value = "2020-10-10")
            }), required = true, dataType = "string")
    })
    public RestResponse modifyReferUser(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        MallUserEntity user = new MallUserEntity();
        user.setId(loginUser.getId());
        String referUserId = jsonParam.getString("referUserId");
        if (null != referUserId) {
            user.setReferUserId(referUserId);
        }
        MallDistEntity mallDist = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", user.getReferUserId()));
        if(mallDist != null){
            userService.updateById(user);
        }
        user = userService.getById(loginUser.getId());
        return RestResponse.success().put("userInfo", user);
    }

    /**
     * 待审核会员
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @PostMapping("toAuditMemberTags")
    @ApiOperation(value = "待审核会员", notes = "待审核会员")
    public RestResponse toAuditMemberTags(@LoginUser MallUserEntity loginUser) {
        MallUserEntity user = new MallUserEntity();
        user.setId(loginUser.getId());

        String userTags = user.getUserTags();
        if(StrUtil.isNotBlank(userTags)){
            List<String> tags = Arrays.asList(userTags.split(","));
            if( tags.contains("1"))
                throw new BusinessException("已经是会员");
            if(!tags.contains("2"))
                tags.add("2");
            user.setUserTags(String.join(",", tags));
        }else{
            user.setUserTags("2");
        }

        userService.updateById(user);
        user = userService.getById(loginUser.getId());
        return RestResponse.success().put("userInfo", user);
    }



    /**
     * 修改头像信息
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("modifyHeadImgUrl")
    @ApiOperation(value = "修改头像信息", notes = "修改头像信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "headImgUrl", value = "headImgUrl")
            }), required = true, dataType = "string")
    })
    public RestResponse modifyHeadImgUrl(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String headImgUrl = jsonParam.getString("headImgUrl");
        AbstractAssert.isBlank(headImgUrl, "头像地址不能为空");

        MallUserEntity user = new MallUserEntity();
        user.setHeadImgUrl(headImgUrl);
        user.setId(loginUser.getId());
        userService.updateById(user);
        user = userService.getById(loginUser.getId());
        return RestResponse.success().put("userInfo", user);
    }
}
