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
import com.platform.modules.mall.dao.MallIntegralLogDao;
import com.platform.modules.mall.entity.MallIntegralLogEntity;
import com.platform.modules.mall.service.MallIntegralLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 积分变动记录Service实现类
 *
 * @author cxd
 * @since 2021-07-27 13:29:35
 */
@Service("mallIntegralLogService")
public class MallIntegralLogServiceImpl extends ServiceImpl<MallIntegralLogDao, MallIntegralLogEntity> implements MallIntegralLogService {

    @Override
    public List<MallIntegralLogEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallIntegralLogEntity> page = new Query<MallIntegralLogEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallIntegralLogPage(page, params));
    }

    @Override
    public boolean add(MallIntegralLogEntity mallIntegralLog) {
        return this.save(mallIntegralLog);
    }

    @Override
    public boolean update(MallIntegralLogEntity mallIntegralLog) {
        return this.updateById(mallIntegralLog);
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

    @Override
    public boolean saveIntegrals(String userId, Integer number, Integer typeDetail) {
        MallIntegralLogEntity entity = new MallIntegralLogEntity();
        entity.setNumber(number);
        entity.setAddTime(new Date());
        entity.setType(1);
        entity.setUserId(userId);
        if (5 == typeDetail) {
            entity.setType(2);
        }
        entity.setTypeDetail(typeDetail);
        return this.save(entity);
    }
}
