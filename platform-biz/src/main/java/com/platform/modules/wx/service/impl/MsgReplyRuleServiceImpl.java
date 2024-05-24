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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.modules.wx.dao.MsgReplyRuleDao;
import com.platform.modules.wx.entity.MsgReplyRuleEntity;
import com.platform.modules.wx.service.MsgReplyRuleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cxd
 * @date 2021-05-06 18:30:15
 */
@Service("msgReplyRuleService")
public class MsgReplyRuleServiceImpl extends ServiceImpl<MsgReplyRuleDao, MsgReplyRuleEntity> implements MsgReplyRuleService {

    @Override
    public IPage queryPage(Map<String, Object> params) {
        String matchValue = (String) params.get("matchValue");
        String appid = (String) params.get("appid");

        Page<MsgReplyRuleEntity> page = new Query<MsgReplyRuleEntity>(params).getPage();
        return baseMapper.selectPage(page,
                new QueryWrapper<MsgReplyRuleEntity>().eq(StringUtils.hasText(appid), "APPID", appid)
                        .or()
                        .apply("APPID IS NULL OR APPID = ''")
                        .like(StringUtils.hasText(matchValue), "MATCH_VALUE", matchValue)
                        .orderByDesc("UPDATE_TIME")
        );
    }

    /**
     * 保存自动回复规则
     *
     * @param msgReplyRule
     */

    @Override
    public boolean save(MsgReplyRuleEntity msgReplyRule) {
        if (StringUtils.hasText(msgReplyRule.getRuleId())) {
            baseMapper.updateById(msgReplyRule);
        } else {
            baseMapper.insert(msgReplyRule);
        }
        return false;
    }

    /**
     * 获取所有的回复规则
     *
     * @return
     */
    @Override
    public List<MsgReplyRuleEntity> getRules() {
        return baseMapper.selectList(new QueryWrapper<MsgReplyRuleEntity>().orderByDesc("RULE_ID"));
    }

    /**
     * 获取当前时段内所有有效的回复规则
     *
     * @return
     */
    @Override
    public List<MsgReplyRuleEntity> getValidRules() {
        return baseMapper.selectList(
                new QueryWrapper<MsgReplyRuleEntity>()
                        .eq("STATUS", 1)
                        .isNotNull("MATCH_VALUE")
                        .ne("MATCH_VALUE", Constant.BLANK)
                        .orderByDesc("PRIORITY"));
    }

    /**
     * 筛选符合条件的回复规则
     *
     * @param appid      公众号appid
     * @param exactMatch 是否精确匹配
     * @param keywords   关键词
     * @return 规则列表
     */
    @Override
    public List<MsgReplyRuleEntity> getMatchedRules(String appid, boolean exactMatch, String keywords) {
        LocalTime now = LocalTime.now();
        return this.getValidRules().stream()
                // 检测是否是对应公众号的规则，如果appid为空则为通用规则
                .filter(rule -> !StringUtils.hasText(rule.getAppid()) || appid.equals(rule.getAppid()))
                // 检测是否在有效时段，effectTimeStart为null则一直有效
                .filter(rule -> null == rule.getEffectTimeStart() || rule.getEffectTimeStart().toLocalTime().isBefore(now))
                // 检测是否在有效时段，effectTimeEnd为null则一直有效
                .filter(rule -> null == rule.getEffectTimeEnd() || rule.getEffectTimeEnd().toLocalTime().isAfter(now))
                //检测是否符合匹配规则
                .filter(rule -> isMatch(exactMatch || rule.isExactMatch(), rule.getMatchValue().split(Constant.COMMA), keywords))
                .collect(Collectors.toList());
    }

    /**
     * 检测文字是否匹配规则
     * 精确匹配时，需关键词与规则词语一致
     * 非精确匹配时，检测文字需包含任意一个规则词语
     *
     * @param exactMatch 是否精确匹配
     * @param ruleWords  规则列表
     * @param checkWords 需检测的文字
     * @return
     */
    public static boolean isMatch(boolean exactMatch, String[] ruleWords, String checkWords) {
        if (!StringUtils.hasText(checkWords)) {
            return false;
        }
        for (String words : ruleWords) {
            if (exactMatch && words.equals(checkWords)) {
                //精确匹配，需关键词与规则词语一致
                return true;
            }
            if (!exactMatch && checkWords.contains(words)) {
                //模糊匹配
                return true;
            }
        }
        return false;
    }
}
