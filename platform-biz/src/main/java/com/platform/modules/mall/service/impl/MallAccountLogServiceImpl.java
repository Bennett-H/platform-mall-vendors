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
import com.platform.modules.mall.dao.MallAccountLogDao;
import com.platform.modules.mall.entity.MallAccountLogEntity;
import com.platform.modules.mall.service.MallAccountLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户账户余额变动记录Service实现类
 *
 * @author cxd
 * @since 2019-07-11 16:56:08
 */
@Service("mallAccountLogService")
public class MallAccountLogServiceImpl extends ServiceImpl<MallAccountLogDao, MallAccountLogEntity> implements MallAccountLogService {

    @Override
    public List<MallAccountLogEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallAccountLogEntity> page = new Query<MallAccountLogEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallAccountLogPage(page, params));
    }

    @Override
    public boolean add(MallAccountLogEntity mallAccountLog) {
        return this.save(mallAccountLog);
    }

    @Override
    public boolean update(MallAccountLogEntity mallAccountLog) {
        return this.updateById(mallAccountLog);
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
