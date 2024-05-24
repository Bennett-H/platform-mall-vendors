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
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.platform.common.utils.Constant;
import com.platform.config.RedisTemplateUtil;
import com.platform.common.utils.Query;
import com.platform.modules.wx.dao.WxAccountDao;
import com.platform.modules.wx.entity.WxAccountEntity;
import com.platform.modules.wx.service.WxAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cxd
 * @date 2021-05-06 18:30:15
 */
@Slf4j
@Service("wxAccountService")
public class WxAccountServiceImpl extends ServiceImpl<WxAccountDao, WxAccountEntity> implements WxAccountService {

    @Autowired
    WxMpService wxMpService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<WxAccountEntity> queryAll(Map<String, Object> params) {
        String name = (String) params.get("name");

        return baseMapper.selectList(new QueryWrapper<WxAccountEntity>().like(StringUtils.hasText(name), "NAME", name)
                .orderByDesc("TYPE"));
    }

    @Override
    public IPage queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");

        Page<WxAccountEntity> page = new Query<WxAccountEntity>(params).getPage();

        return baseMapper.selectPage(page,
                new QueryWrapper<WxAccountEntity>().like(StringUtils.hasText(name), "NAME", name)
                        .orderByDesc("TYPE"));
    }

    @PostConstruct
    public void loadWxMpConfigStorages() {
        List<WxAccountEntity> accountList = this.list();
        if (accountList == null || accountList.isEmpty()) {
            log.info("未读取到公众号配置，请在管理后台添加");
            return;
        }
        log.info("加载到{}条公众号配置", accountList.size());
        accountList.forEach(this::addAccountToRuntime);
    }

    @Override
    public boolean saveWxAccount(WxAccountEntity entity) {
        Assert.notNull(entity, "WxAccount不得为空");
        String appid = entity.getAppid();
        //已有此appid信息，更新
        if (this.isAccountInRuntime(appid)) {
            log.info("更新公众号配置");
            wxMpService.removeConfigStorage(appid);
            this.addAccountToRuntime(entity);

            return SqlHelper.retBool(this.baseMapper.updateById(entity));
        } else {
            //没有此appid信息，新增
            log.info("新增公众号配置");
            this.addAccountToRuntime(entity);

            return SqlHelper.retBool(this.baseMapper.insert(entity));
        }

    }

    @Override
    public boolean removeByAppIds(Collection<?> idList) {
        Assert.notEmpty(idList, "WxAccount不得为空");

        // 更新wxMpService配置
        log.info("同步移除公众号配置");
        idList.forEach(id -> wxMpService.removeConfigStorage((String) id));

        return SqlHelper.retBool(this.baseMapper.deleteBatchIds(idList));
    }

    /**
     * 判断当前账号是存在
     *
     * @param appid
     * @return
     */
    private boolean isAccountInRuntime(String appid) {
        try {
            return wxMpService.switchover(appid);
        } catch (NullPointerException e) {
            // sdk bug，未添加任何账号时configStorageMap为null会出错
            return false;
        }
    }

    /**
     * 添加账号到当前程序，如首次添加需初始化configStorageMap
     *
     * @param entity
     */
    private synchronized void addAccountToRuntime(WxAccountEntity entity) {
        String appid = entity.getAppid();
        WxMpDefaultConfigImpl config = toWxMpConfigStorage(entity);
        try {
            wxMpService.addConfigStorage(appid, config);
        } catch (NullPointerException e) {
            log.info("需初始化configStorageMap...");
            Map<String, WxMpConfigStorage> configStorages = new HashMap<>(4);
            configStorages.put(appid, config);
            wxMpService.setMultiConfigStorages(configStorages, appid);
        }
    }

    private WxMpRedisConfigImpl toWxMpConfigStorage(WxAccountEntity entity) {
        WxMpRedisConfigImpl wxMpInRedisConfigStorage = new WxMpRedisConfigImpl(new RedisTemplateWxRedisOps(stringRedisTemplate), Constant.WX_MP_CONFIG);
        wxMpInRedisConfigStorage.setAppId(entity.getAppid());
        wxMpInRedisConfigStorage.setSecret(entity.getSecret());
        wxMpInRedisConfigStorage.setToken(entity.getToken());
        wxMpInRedisConfigStorage.setAesKey(entity.getAesKey());
        return wxMpInRedisConfigStorage;
    }
}
