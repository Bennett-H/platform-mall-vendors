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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.wx.entity.MsgReplyRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * @author cxd
 * @date 2021-05-06 18:30:15
 */
public interface MsgReplyRuleService extends IService<MsgReplyRuleEntity> {
    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    IPage queryPage(Map<String, Object> params);

    /**
     * 保存自动回复规则
     *
     * @param msgReplyRule msgReplyRule
     * @return boolean
     */
    @Override
    boolean save(MsgReplyRuleEntity msgReplyRule);

    /**
     * 获取所有的回复规则
     *
     * @return
     */
    List<MsgReplyRuleEntity> getRules();

    /**
     * 获取当前时段内所有有效的回复规则
     *
     * @return 有效的规则列表
     */
    List<MsgReplyRuleEntity> getValidRules();

    /**
     * 筛选符合条件的回复规则
     *
     * @param appid
     * @param exactMatch 是否精确匹配
     * @param keywords   关键词
     * @return 规则列表
     */
    List<MsgReplyRuleEntity> getMatchedRules(String appid, boolean exactMatch, String keywords);
}
