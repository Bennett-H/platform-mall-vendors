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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallTemplateConfDao;
import com.platform.modules.mall.entity.MallTemplateConfEntity;
import com.platform.modules.mall.service.MallTemplateConfService;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 微信订阅消息Service实现类
 *
 * @author cxd
 * @since 2019-07-04 18:19:09
 */
@Service("mallTemplateConfService")
@CacheConfig(cacheNames = {"mallTemplateConfServiceCache"})
public class MallTemplateConfServiceImpl extends ServiceImpl<MallTemplateConfDao, MallTemplateConfEntity> implements MallTemplateConfService {

    @Override
    @Cacheable
    public List<MallTemplateConfEntity> queryAll(Map<String, Object> params) {
        String templateType = (String) params.get("templateType");

        return baseMapper.selectList(new QueryWrapper<MallTemplateConfEntity>()
                .eq(StringUtils.isNotBlank(templateType), "TEMPLATE_TYPE", templateType));
    }

    @Override
    //@Cacheable
    public List<String> queryAllTemplateIdsByWrapper(QueryWrapper<MallTemplateConfEntity> wrapper) {
        List<MallTemplateConfEntity> result = this.list(wrapper);

        List<String> templateIds = new ArrayList<>();
        if (result != null && result.size() > 0) {
            for (MallTemplateConfEntity item : result) {
                templateIds.add(item.getTemplateId());
            }
        }
        return templateIds;
    }

    @Override
    @Cacheable
    public IPage queryPage(Map<String, Object> params) {
        String templateType = (String) params.get("templateType");

        IPage<MallTemplateConfEntity> page = new Query<MallTemplateConfEntity>(params).getPage();
        return baseMapper.selectPage(page, new QueryWrapper<MallTemplateConfEntity>()
                .eq(StringUtils.isNotBlank(templateType), "TEMPLATE_TYPE", templateType));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean add(MallTemplateConfEntity mallTemplateConf) {
        QueryWrapper<MallTemplateConfEntity> query = new QueryWrapper<MallTemplateConfEntity>().eq("TEMPLATE_TYPE", mallTemplateConf.getTemplateType());
        List<MallTemplateConfEntity> templateTypes = list(query);
        if (templateTypes != null && templateTypes.size() > 0) {
            remove(query);
        }
        return this.save(mallTemplateConf);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(MallTemplateConfEntity mallTemplateConf) {
        return this.updateById(mallTemplateConf);
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
