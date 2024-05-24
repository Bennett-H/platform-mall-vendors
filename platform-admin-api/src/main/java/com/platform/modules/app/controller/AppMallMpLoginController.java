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

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.CommonContants;
import com.platform.common.CommonRestResult;
import com.platform.common.exception.BusinessException;
import com.platform.common.session.SessionData;
import com.platform.common.utils.*;
import com.platform.common.validator.AbstractAssert;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.app.dto.MpUserLoginByOpenIdDTO;
import com.platform.modules.app.dto.MpUserLoginDTO;
import com.platform.modules.app.entity.AppleUserInfo;
import com.platform.modules.app.entity.FullUserInfo;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.SysMpUserEntity;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.mall.service.SysMpUserService;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysUserService;
import com.platform.utils.JwtUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP登录授权
 *
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/auth")
@Api(tags = "商户管理小程序登录")
public class AppMallMpLoginController extends AppBaseController {
    @Autowired
    private SysMpUserService userService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private MallShopsService mallShopsService;


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    /**
     * 用户注册
     *
     * @param mpUserLoginDTO JSON格式参数
     * @return RestResponse
     */
    @IgnoreAuth
    @PostMapping("appregister")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public CommonRestResult register(@RequestBody MpUserLoginDTO mpUserLoginDTO) {
        String headImgUrl = mpUserLoginDTO.getHeadImgUrl();

        //String nickname = mpUserLoginDTO.getNickname();
        //AbstractAssert.isBlank(nickname, "用户昵称不能为空");

        String userName = mpUserLoginDTO.getUserName();
        String password = mpUserLoginDTO.getPassword();
        AbstractAssert.isBlank(password, "密码不能为空");

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(mpUserLoginDTO.getCode());
            // 用户信息校验
            log.info("》》》微信返回sessionData：" + session.toString());

//            if (!wxMaService.getUserService().checkUserInfo(session.getSessionKey(), mpUserLoginDTO.getFullUserInfo().getRawData(), mpUserLoginDTO.getFullUserInfo().getSignature())) {
//                log.error("登录失败：数据签名验证失败");
//                return CommonRestResult.fail("", "登录失败");
//            }

//            // 解密用户信息
//            WxMaUserInfo wxMpUser = wxMaService.getUserService().getUserInfo(session.getSessionKey(), mpUserLoginDTO.getFullUserInfo().getEncryptedData(), mpUserLoginDTO.getFullUserInfo().getIv());

            //用户信息
            SysUserEntity user = sysUserService.queryByUserName(userName);
            // 账号不存在
            if (user == null) {
                return CommonRestResult.error(CommonContants.ERROR_CODE_LOGIN, "账号或密码不正确");
            }
            // 账号不存在
            String passwordStr = new Sha256Hash(password, user.getSalt()).toHex();
            if (!user.getPassword().equals(passwordStr)) {
                return CommonRestResult.error(CommonContants.ERROR_CODE_LOGIN, "账号或密码不正确");
            }

            //账号锁定
            if (user.getStatus() == 0) {
                return CommonRestResult.error(CommonContants.ERROR_CODE_LOGIN, "账号已被锁定,请联系管理员");
            }

            Map<String, Object> params = new HashMap<>();
            String userId = user.getUserId();
            //超级管理员查看所有
            if (!Constant.SUPER_ADMIN.equals(userId)) {
                params.put("userId", userId);
            }
            List<MallShopsEntity> list = mallShopsService.queryAll(params);
            if (list == null || list.size() == 0) {
                return CommonRestResult.error(CommonContants.ERROR_CODE_LOGIN, "该账号不是商城管理账号");
            }

            SysMpUserEntity sysMpUserEntity = userService.selectByMpOpenId(session.getOpenid());
            if (sysMpUserEntity == null) {
                sysMpUserEntity = new SysMpUserEntity();
                sysMpUserEntity.setHeadImgUrl(headImgUrl);
                sysMpUserEntity.setUserId(user.getUserId());
                sysMpUserEntity.setNickname(user.getUserName());
                sysMpUserEntity.setUserName(user.getUserName());
                sysMpUserEntity.setRegisterTime(new Date());
                sysMpUserEntity.setLastLoginTime(new Date());
                sysMpUserEntity.setRegisterIp(this.getClientIp());
                sysMpUserEntity.setLastLoginIp(this.getClientIp());
                sysMpUserEntity.setIntegral(0);
                sysMpUserEntity.setUserLevelId("1");
                sysMpUserEntity.setGender(0);
                sysMpUserEntity.setOpenId(session.getOpenid());
                sysMpUserEntity.setQqOpenId("123123123");
                sysMpUserEntity.setMpOpenId("656fdgdfgdfgfd");
                sysMpUserEntity.setShopId(list.get(0).getId());
                userService.add(sysMpUserEntity);
            }

            //生成token
            SessionData sessionData = new SessionData();
            sessionData.setUserId(sysMpUserEntity.getId());
            sessionData.setShopsId(sysMpUserEntity.getShopId());
            sessionData.setWechatOpenId(sysMpUserEntity.getOpenId());
            String token = jwtUtils.getMpAppToken(sessionData);

            Map<String, Object> map = new HashMap<>(4);
            map.put("token", token);
            map.put("userInfo", sysMpUserEntity);

            return CommonRestResult.success(map);
        } catch (WxErrorException e) {
            log.error("登录失败：" + e.getMessage());
            return CommonRestResult.fail("", "登录失败");
        }
    }


    /**
     * 根据openId换取登录token，方便本地开发调试
     *
     * @param loginByOpenIdDTO
     * @return
     */
    @IgnoreAuth
    @PostMapping("adminLoginByOpenId")
    @ApiOperation(value = "openId登录", notes = "openId登录")
    @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
            @ExampleProperty(mediaType = "openId", value = "ok8KW5GEIwAYTa-Z92JfbzxkVNpA")
    }), required = true, dataType = "string")
    public CommonRestResult loginByOpenId(@RequestBody MpUserLoginByOpenIdDTO loginByOpenIdDTO) {
        String openId = loginByOpenIdDTO.getOpenId();

        //查询用户新
        SysMpUserEntity user = userService.selectByOpenId(openId);
        CommonRestResult.error(CommonContants.ERROR_CODE_LOGIN, "登录失败：用户为空");
        if (user == null) {
            return CommonRestResult.fail("201", "登录失败：找不到对应的用户信息");
        }
        if (StringUtils.isEmpty(user.getShopId())) {
            return CommonRestResult.fail("", "用户没有绑定商户");
        }
        //生成token
        String token = jwtUtils.generateToken(user.getId());
        Map<String, Object> map = new HashMap<>(8);
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("openId", openId);
        map.put("user", user);

        return CommonRestResult.success(map);
    }


}
