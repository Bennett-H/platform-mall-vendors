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

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisConfigImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.modules.wx.dao.WxMaConfigDao;
import com.platform.modules.wx.entity.WxMaConfigEntity;
import com.platform.modules.wx.service.WxMaConfigService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序配置Service实现类
 *
 * @author cxd
 * @since 2020-04-05 21:58:47
 */
@Slf4j
@Service("wxMaConfigService")
public class WxMaConfigServiceImpl extends ServiceImpl<WxMaConfigDao, WxMaConfigEntity> implements WxMaConfigService {
    @Autowired
    WxMaService wxMaService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<WxMaConfigEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<WxMaConfigEntity> page = new Query<WxMaConfigEntity>(params).getPage();
        return page.setRecords(baseMapper.selectWxMaConfigPage(page, params));
    }

    @PostConstruct
    public void loadWxMaConfigStorages() {
        List<WxMaConfigEntity> list = this.list();
        if (list == null || list.isEmpty()) {
            log.info("未读取到小程序配置，请在管理后台添加");
            return;
        }
        log.info("加载到{}条小程序配置", list.size());
        list.forEach(this::addMaConfigToRuntime);

        wxMaService.switchoverTo("wx5b7c6c5637838950");
    }

    @Override
    public boolean saveMaConfig(WxMaConfigEntity entity) {
        Assert.notNull(entity, "WxMaConfig不得为空");
        String appid = entity.getAppId();
        //已有此appid信息，更新
        if (this.isMaConfigInRuntime(appid)) {
            log.info("更新小程序配置");
            wxMaService.removeConfig(appid);
            this.addMaConfigToRuntime(entity);

            return SqlHelper.retBool(this.baseMapper.updateById(entity));
        } else {
            //没有此appid信息，新增
            log.info("新增小程序配置");
            this.addMaConfigToRuntime(entity);

            return SqlHelper.retBool(this.baseMapper.insert(entity));
        }
    }

    @Override
    public boolean removeByMaIds(Collection<?> idList) {
        Assert.notEmpty(idList, "WxMaConfig不得为空");

        // 更新wxMaService配置
        log.info("同步移除小程序配置");
        idList.forEach(id -> wxMaService.removeConfig((String) id));

        return SqlHelper.retBool(this.baseMapper.deleteBatchIds(idList));
    }

    /**
     * 判断当前账号是存在
     *
     * @param appid
     * @return
     */
    private boolean isMaConfigInRuntime(String appid) {
        try {
            return wxMaService.switchover(appid);
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
    private synchronized void addMaConfigToRuntime(WxMaConfigEntity entity) {
        String appid = entity.getAppId();
        WxMaRedisBetterConfigImpl config = toWxMpConfigStorage(entity);
        try {
            wxMaService.addConfig(appid, config);
        } catch (NullPointerException e) {
            log.info("需初始化configStorageMap...");
            Map<String, WxMaConfig> configStorages = new HashMap<>(4);
            configStorages.put(appid, config);
            wxMaService.setMultiConfigs(configStorages, appid);
        }
    }

    private WxMaRedisBetterConfigImpl toWxMpConfigStorage(WxMaConfigEntity entity) {
        WxMaRedisBetterConfigImpl wxMaRedisBetterConfig = new WxMaRedisBetterConfigImpl(new RedisTemplateWxRedisOps(stringRedisTemplate), Constant.WX_MA_CONFIG);
        wxMaRedisBetterConfig.setAppid(entity.getAppId());
        wxMaRedisBetterConfig.setSecret(entity.getSecret());
        wxMaRedisBetterConfig.setToken(entity.getToken());
        wxMaRedisBetterConfig.setAesKey(entity.getAesKey());
        wxMaRedisBetterConfig.setMsgDataFormat(entity.getMsgDataFormat());
        return wxMaRedisBetterConfig;
    }
}
