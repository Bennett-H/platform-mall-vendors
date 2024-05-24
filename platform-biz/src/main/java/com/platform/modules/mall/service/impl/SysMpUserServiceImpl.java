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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.validator.AbstractAssert;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.mall.dao.SysMpUserDao;
import com.platform.modules.mall.entity.SysMpUserEntity;
import com.platform.modules.mall.service.SysMpUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 李鹏军
 */
@Service("sysMpUserService")
public class SysMpUserServiceImpl extends ServiceImpl<SysMpUserDao, SysMpUserEntity> implements SysMpUserService {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    @Override
    public List<SysMpUserEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }



    @Override
    public boolean add(SysMpUserEntity mallUser) {
        return this.save(mallUser);
    }

    @Override
    public boolean update(SysMpUserEntity mallUser) {
        return this.updateById(mallUser);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public SysMpUserEntity queryByMobile(String mobile) {
        return this.getOne(new QueryWrapper<SysMpUserEntity>().eq("MOBILE", mobile), false);
    }

    @Override
    public SysMpUserEntity loginByMobile(String mobile, String password) {
        SysMpUserEntity user = queryByMobile(mobile);
        AbstractAssert.isNull(user, "该手机暂未绑定用户");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new BusinessException("手机号或密码错误");
        }

        return user;
    }

    @Override
    public SysMpUserEntity selectByOpenId(String openId) {
        SysMpUserEntity userEntity = new SysMpUserEntity();
        userEntity.setOpenId(openId);
        return this.getOne(new QueryWrapper<SysMpUserEntity>().eq("OPEN_ID", openId), false);
    }

    @Override
    public SysMpUserEntity selectByMpOpenId(String mpOpenId) {
        SysMpUserEntity userEntity = new SysMpUserEntity();
        userEntity.setMpOpenId(mpOpenId);
        return this.getOne(new QueryWrapper<SysMpUserEntity>().eq("MP_OPEN_ID", mpOpenId), false);
    }

    @Override
    public SysMpUserEntity selectByOpenUnionId(String unionId) {
        SysMpUserEntity userEntity = new SysMpUserEntity();
        userEntity.setUnionId(unionId);
        return this.getOne(new QueryWrapper<SysMpUserEntity>().eq("UNION_ID", unionId), false);
    }
}
