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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallBrandDao;
import com.platform.modules.mall.entity.MallBrandEntity;
import com.platform.modules.mall.service.MallBrandService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 品牌制造商Service实现类
 *
 * @author cxd
 * @since 2019-07-03 09:34:37
 */
@Service("mallBrandService")
@CacheConfig(cacheNames = {"mallBrandServiceCache"})
public class MallBrandServiceImpl extends ServiceImpl<MallBrandDao, MallBrandEntity> implements MallBrandService {

    @Override
    @Cacheable
    public List<MallBrandEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    @Cacheable
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.SORT");
        Page<MallBrandEntity> page = new Query<MallBrandEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallBrandPage(page, params));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean add(MallBrandEntity mallBrand) {
        return this.save(mallBrand);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(MallBrandEntity mallBrand) {
        return this.updateById(mallBrand);
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
