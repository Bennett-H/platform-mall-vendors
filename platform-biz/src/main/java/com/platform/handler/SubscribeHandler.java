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
package com.platform.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.builder.TextBuilder;
import com.platform.common.utils.Constant;
import com.platform.common.utils.DateUtils;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.wx.entity.WxAccountEntity;
import com.platform.modules.wx.service.MsgReplyService;
import com.platform.modules.wx.service.WxAccountService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * 微信公众号消息处理（用户关注）
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractHandler {
    @Autowired
    private MallUserService userService;
    @Autowired
    MsgReplyService msgReplyService;
    @Autowired
    WxAccountService accountService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser() + "，事件：" + wxMessage.getEventKey());

        String appid = WxMpConfigStorageHolder.get();
        this.logger.info("appid:{}", appid);
        refreshUserInfo(weixinService, wxMessage.getFromUser(), appid);

        msgReplyService.tryAutoReply(appid, true, wxMessage.getFromUser(), wxMessage.getEvent());

        // 处理特殊事件，如用户扫描带参二维码关注
        if (StringUtils.hasText(wxMessage.getEventKey())) {
            msgReplyService.tryAutoReply(appid, true, wxMessage.getFromUser(), wxMessage.getEventKey());
        }
        try {
            WxAccountEntity mpConfigEntity = accountService.getOne(new QueryWrapper<WxAccountEntity>().eq("APPID", WxMpConfigStorageHolder.get()));
            return new TextBuilder().build(mpConfigEntity.getContent(), wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

    private MallUserEntity refreshUserInfo(WxMpService wxMpService, String openid, String appid) {
        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = wxMpService.getUserService().userInfo(openid, null);
            if (userWxInfo == null) {
                this.logger.error("获取不到用户信息，无法更新,openid:{}", openid);
                return null;
            } else {
                this.logger.info("用户: " + userWxInfo.toString());
                MallUserEntity entity = userService.getOne(new QueryWrapper<MallUserEntity>().eq("UNION_ID", userWxInfo.getUnionId()));
                if (entity == null) {
                    entity = userService.selectByMpOpenId(openid);
                }
                if (entity == null) {
                    entity = new MallUserEntity();
                    entity.setRegisterTime(new Date());
                    entity.setIntegral(0);
                    entity.setUserLevelId("1");
                }
                entity.setMpOpenId(userWxInfo.getOpenId());
                entity.setUnionId(userWxInfo.getUnionId());
                entity.setPassword(DigestUtils.sha256Hex("123456"));
                // 微信接口规则改动，取不到性别字段
                entity.setGender(0);
                entity.setSubscribe(Constant.SUBSCRIBE);
                entity.setSubscribeTime(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                userService.saveOrUpdate(entity);
                return entity;
            }
        } catch (WxErrorException e) {
            this.logger.error(e.getLocalizedMessage());
        }
        return null;
    }
}
