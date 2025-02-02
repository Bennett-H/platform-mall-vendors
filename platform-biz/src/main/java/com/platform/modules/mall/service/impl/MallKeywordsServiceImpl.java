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
import com.platform.modules.mall.dao.MallKeywordsDao;
import com.platform.modules.mall.entity.MallKeywordsEntity;
import com.platform.modules.mall.service.MallKeywordsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 搜索关键词Service实现类
 *
 * @author cxd
 * @since 2019-07-03 12:02:04
 */
@Service("mallKeywordsService")
public class MallKeywordsServiceImpl extends ServiceImpl<MallKeywordsDao, MallKeywordsEntity> implements MallKeywordsService {

    @Override
    public List<MallKeywordsEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.SORT");
        params.put("asc", false);
        Page<MallKeywordsEntity> page = new Query<MallKeywordsEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallKeywordsPage(page, params));
    }

    @Override
    public boolean add(MallKeywordsEntity mallKeywords) {
        return this.save(mallKeywords);
    }

    @Override
    public boolean update(MallKeywordsEntity mallKeywords) {
        return this.updateById(mallKeywords);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }
}
