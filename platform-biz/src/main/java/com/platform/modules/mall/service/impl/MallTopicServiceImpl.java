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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallTopicDao;
import com.platform.modules.mall.entity.MallTopicEntity;
import com.platform.modules.mall.service.MallTopicService;
import com.platform.modules.mall.vo.mall.MallTopicVO;
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
 * 文章Service实现类
 *
 * @author cxd
 * @since 2020-03-08 10:25:25
 */
@Service("mallTopicService")
@CacheConfig(cacheNames = {"mallTopicServiceCache"})
public class MallTopicServiceImpl extends ServiceImpl<MallTopicDao, MallTopicEntity> implements MallTopicService {

    @Override
    @Cacheable
    public List<MallTopicVO> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    @Cacheable
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", StrUtil.equals("readCount", (String) params.get("orderBy")) ? "T.READ_COUNT" : "T.CREATE_TIME");
        params.put("asc", "asc".equals(params.get("sortType")));
        Page<MallTopicVO> page = new Query<MallTopicVO>(params).getPage();
        return page.setRecords(baseMapper.selectMallTopicPage(page, params));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean add(MallTopicEntity mallTopic) {
        mallTopic.setCreateTime(new Date());
        return this.save(mallTopic);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(MallTopicEntity mallTopic) {
        return this.updateById(mallTopic);
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
