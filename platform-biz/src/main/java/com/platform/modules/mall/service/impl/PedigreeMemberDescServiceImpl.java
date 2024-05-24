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
import com.platform.modules.mall.entity.PedigreeMemberDescEntity;
import com.platform.modules.mall.service.PedigreeMemberDescService;
import com.platform.modules.mall.dao.PedigreeMemberDescDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 族谱成员描述Service实现类
 *
 * @author cxd
 * @since 2023-11-21 21:09:21
 */
@Service("pedigreeMemberDescService")
public class PedigreeMemberDescServiceImpl extends ServiceImpl<PedigreeMemberDescDao, PedigreeMemberDescEntity> implements PedigreeMemberDescService {

    @Override
    public List<PedigreeMemberDescEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public List<PedigreeMemberDescEntity> queryAllByWrapper(QueryWrapper<PedigreeMemberDescEntity> queryWrapper) {
        return this.list(queryWrapper);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<PedigreeMemberDescEntity> page = new Query<PedigreeMemberDescEntity>(params).getPage();
        return page.setRecords(baseMapper.selectPedigreeMemberDescPage(page, params));
    }

    @Override
    public boolean add(PedigreeMemberDescEntity pedigreeMemberDesc) {
        return this.save(pedigreeMemberDesc);
    }

    @Override
    public boolean update(PedigreeMemberDescEntity pedigreeMemberDesc) {
        return this.updateById(pedigreeMemberDesc);
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
