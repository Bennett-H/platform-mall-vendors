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
package com.platform.modules.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.utils.Query;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.wx.TaskExcutor;
import com.platform.modules.wx.entity.TemplateMsgLogEntity;
import com.platform.modules.wx.form.TemplateMsgBatchForm;
import com.platform.modules.wx.service.MsgTemplateService;
import com.platform.modules.wx.service.TemplateMsgLogService;
import com.platform.modules.wx.service.TemplateMsgService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送微信模版消息Service
 *
 * @author cxd
 * @date 2019-11-12 18:30:15
 */
@Slf4j
@Service("templateMsgService")
public class TemplateMsgServiceImpl implements TemplateMsgService {
    @Autowired
    private TemplateMsgLogService templateMsgLogService;
    @Autowired
    private WxMpService wxService;
    @Autowired
    MsgTemplateService msgTemplateService;
    @Autowired
    MallUserService userService;

    /**
     * 发送微信模版消息,使用固定线程的线程池
     */
    @Override
    @Async
    public void sendTemplateMsg(WxMpTemplateMessage msg, String appid) {
        TaskExcutor.submit(() -> {
            String result;
            try {
                wxService.switchover(appid);
                result = wxService.getTemplateMsgService().sendTemplateMsg(msg);
            } catch (WxErrorException e) {
                result = e.getMessage();
            }

            //保存发送日志
            TemplateMsgLogEntity log = new TemplateMsgLogEntity(msg, appid, result);
            templateMsgLogService.addLog(log);
        });
    }

    @Override
    @Async
    public void sendMsgBatch(TemplateMsgBatchForm form, String appid) {
        log.info("批量发送模板消息任务开始,参数：{}", form.toString());
        wxService.switchover(appid);
        WxMpTemplateMessage.WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder()
                .templateId(form.getTemplateId())
                .url(form.getUrl())
                .miniProgram(form.getMiniprogram())
                .data(form.getData());
        // 测试
//        WxMpTemplateMessage msg = builder.toUser("okfdW57vWbjb4ArfeEtTbuS6AKKI").build();
//        this.sendTemplateMsg(msg, appid);

        Map<String, Object> filterParams = new HashMap<>(8);
        long currentPage = 1L, totalPages = Long.MAX_VALUE;
        filterParams.put("limit", "500");
        Page<MallUserEntity> page = new Query<MallUserEntity>(filterParams).getPage();
        while (currentPage <= totalPages) {
            filterParams.put("page", String.valueOf(currentPage));
            //按条件查询用户
            Page<MallUserEntity> wxUsers = userService.page(page, new QueryWrapper<MallUserEntity>().apply("MP_OPEN_ID IS NOT NULL"));
            log.info("批量发送模板消息任务,使用查询条件，处理第{}页，总用户数：{}", currentPage, wxUsers.getTotal());
            wxUsers.getRecords().forEach(user -> {
                WxMpTemplateMessage msg = builder.toUser(user.getMpOpenId()).build();
                this.sendTemplateMsg(msg, appid);
            });
            currentPage = wxUsers.getCurrent() + 1L;
            totalPages = wxUsers.getPages();
        }

        log.info("批量发送模板消息任务结束");
    }
}
