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
package com.platform.modules.wx.service;

import com.platform.modules.wx.form.TemplateMsgBatchForm;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

/**
 * 发送微信模版消息
 *
 * @author cxd
 * @date 2019-11-12 18:30:15
 */
public interface TemplateMsgService {
    /**
     * 发送微信模版消息
     *
     * @param msg   msg
     * @param appid appid
     */
    void sendTemplateMsg(WxMpTemplateMessage msg, String appid);

    /**
     * 批量消息发送
     *
     * @param form  form
     * @param appid appid
     */
    void sendMsgBatch(TemplateMsgBatchForm form, String appid);
}
