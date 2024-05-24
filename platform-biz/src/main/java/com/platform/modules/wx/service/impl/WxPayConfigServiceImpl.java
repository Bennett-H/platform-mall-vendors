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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.platform.common.utils.Query;
import com.platform.modules.wx.dao.WxPayConfigDao;
import com.platform.modules.wx.entity.WxPayConfigEntity;
import com.platform.modules.wx.service.WxPayConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付配置Service实现类
 *
 * @author cxd
 * @since 2022-08-16 18:08:55
 */
@Slf4j
@Service("wxPayConfigService")
public class WxPayConfigServiceImpl extends ServiceImpl<WxPayConfigDao, WxPayConfigEntity> implements WxPayConfigService {
    @Autowired
    private WxPayService wxPayService;

    @Override
    public List<WxPayConfigEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public List<WxPayConfigEntity> queryAllByWrapper(QueryWrapper<WxPayConfigEntity> queryWrapper) {
        return this.list(queryWrapper);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<WxPayConfigEntity> page = new Query<WxPayConfigEntity>(params).getPage();
        return page.setRecords(baseMapper.selectWxPayConfigPage(page, params));
    }

    @PostConstruct
    public void loadWxPayConfigStorages() {
        List<WxPayConfigEntity> list = this.list();
        if (list == null || list.isEmpty()) {
            log.info("未读取到微信支付配置，请在管理后台添加");
            return;
        }
        log.info("加载到{}条微信支付配置", list.size());
        list.forEach(this::addPayConfigToRuntime);
    }

    @Override
    public boolean savePayConfig(WxPayConfigEntity entity) {
        Assert.notNull(entity, "WxPayConfig不得为空");
        String mchId = entity.getMchId();
        //已有此appid信息，更新
        if (this.isPayConfigInRuntime(mchId)) {
            log.info("更新微信支付配置");
            wxPayService.removeConfig(mchId);
            this.addPayConfigToRuntime(entity);

            return SqlHelper.retBool(this.baseMapper.updateById(entity));
        } else {
            log.info("新增微信支付配置");
            this.addPayConfigToRuntime(entity);

            return SqlHelper.retBool(this.baseMapper.insert(entity));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByMchIds(Collection<?> mchIds) {
        Assert.notEmpty(mchIds, "WxPayConfig不得为空");

        // 更新wxPayService配置
        log.info("同步移除微信支付配置");
        mchIds.forEach(mchId -> {
            wxPayService.removeConfig((String) mchId);

            Map<String, Object> map = new HashMap<>(4);
            map.put("MCH_ID", mchId);
            removeByMap(map);
        });
        return true;
    }

    /**
     * 判断当前账号是存在
     *
     * @param mchId
     * @return
     */
    private boolean isPayConfigInRuntime(String mchId) {
        try {
            return wxPayService.switchover(mchId);
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
    private synchronized void addPayConfigToRuntime(WxPayConfigEntity entity) {
        String mchId = entity.getMchId();
        WxPayConfig config = toWxPayConfigStorage(entity);
        try {
            wxPayService.addConfig(mchId, config);
        } catch (NullPointerException e) {
            log.info("需初始化configStorageMap...");
            Map<String, WxPayConfig> configStorages = new HashMap<>(4);
            configStorages.put(mchId, config);
            wxPayService.setMultiConfig(configStorages, mchId);
        }
    }

    private WxPayConfig toWxPayConfigStorage(WxPayConfigEntity entity) {
        WxPayConfig configStorage = new WxPayConfig();
        configStorage.setAppId(entity.getAppId());
        configStorage.setMchId(entity.getMchId());
        configStorage.setMchKey(entity.getMchKey());
        configStorage.setSubAppId(entity.getSubAppId());
        configStorage.setSubMchId(entity.getSubMchId());
        configStorage.setKeyPath(entity.getKeyPath());
        return configStorage;
    }
}
