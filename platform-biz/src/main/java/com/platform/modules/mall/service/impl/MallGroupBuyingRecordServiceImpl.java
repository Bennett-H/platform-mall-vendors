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
import com.platform.modules.mall.dao.MallGroupBuyingRecordDao;
import com.platform.modules.mall.entity.MallGroupBuyingRecordEntity;
import com.platform.modules.mall.service.MallGroupBuyingRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品-拼团记录表Service实现类
 *
 * @author cxd
 * @since 2021-11-14 22:52:41
 */
@Service("mallGroupBuyingRecordService")
public class MallGroupBuyingRecordServiceImpl extends ServiceImpl<MallGroupBuyingRecordDao, MallGroupBuyingRecordEntity> implements MallGroupBuyingRecordService {

    @Override
    public List<MallGroupBuyingRecordEntity> queryAll(Map<String, Object> params) {
        params.put("sidx", "T.CREATE_TIME");
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.CREATE_TIME");
        params.put("asc", false);
        Page<MallGroupBuyingRecordEntity> page = new Query<MallGroupBuyingRecordEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallGroupBuyingRecordPage(page, params));
    }

    @Override
    public boolean add(MallGroupBuyingRecordEntity mallGroupBuyingRecord) {
        mallGroupBuyingRecord.setCreateTime(new Date());
        return this.save(mallGroupBuyingRecord);
    }

    @Override
    public boolean update(MallGroupBuyingRecordEntity mallGroupBuyingRecord) {
        mallGroupBuyingRecord.setUpdateTime(new Date());
        return this.updateById(mallGroupBuyingRecord);
    }

    @Override
    public int addJoinNumber(String leaderId) {
        return baseMapper.addJoinNumber(leaderId);
    }

    @Override
    public int updateStatus(String groupId, int status) {
        return baseMapper.updateStatus(groupId, status);
    }
}
