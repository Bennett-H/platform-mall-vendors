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
package com.platform.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallBulletinDao;
import com.platform.modules.mall.entity.MallBulletinEntity;
import com.platform.modules.mall.service.MallBulletinService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商城公告Service实现类
 *
 * @author cxd
 * @since 2019-07-08 16:38:26
 */
@Service("mallBulletinService")
@CacheConfig(cacheNames = {"mallBulletinServiceCache"})
public class MallBulletinServiceImpl extends ServiceImpl<MallBulletinDao, MallBulletinEntity> implements MallBulletinService {

    @Override
    @Cacheable
    public List<MallBulletinEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    //@Cacheable
    public List<MallBulletinEntity> queryAllByWrapper(QueryWrapper<MallBulletinEntity> wrapper) {
        return this.list(wrapper);
    }

    @Override
    @Cacheable
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallBulletinEntity> page = new Query<MallBulletinEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallBulletinPage(page, params));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean add(MallBulletinEntity mallBulletin) {
        mallBulletin.setAddTime(new Date());
        this.save(mallBulletin);
        if (mallBulletin.getEnabled() == 1) {
            baseMapper.updateEnableById(mallBulletin.getId());
        }
        return true;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(MallBulletinEntity mallBulletin) {
        if (mallBulletin.getEnabled() == 1) {
            baseMapper.updateEnableById(mallBulletin.getId());
        }
        return this.updateById(mallBulletin);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }
}
