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
import com.platform.modules.mall.dao.MallCommentPictureDao;
import com.platform.modules.mall.entity.MallCommentPictureEntity;
import com.platform.modules.mall.service.MallCommentPictureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 会员评价图片Service实现类
 *
 * @author cxd
 * @since 2019-07-04 14:46:38
 */
@Service("mallCommentPictureService")
public class MallCommentPictureServiceImpl extends ServiceImpl<MallCommentPictureDao, MallCommentPictureEntity> implements MallCommentPictureService {

    @Override
    public List<MallCommentPictureEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.COMMENT_ID");
        params.put("asc", false);
        Page<MallCommentPictureEntity> page = new Query<MallCommentPictureEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallCommentPicturePage(page, params));
    }

    @Override
    public boolean add(MallCommentPictureEntity mallCommentPicture) {
        return this.save(mallCommentPicture);
    }

    @Override
    public boolean update(MallCommentPictureEntity mallCommentPicture) {
        return this.updateById(mallCommentPicture);
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
