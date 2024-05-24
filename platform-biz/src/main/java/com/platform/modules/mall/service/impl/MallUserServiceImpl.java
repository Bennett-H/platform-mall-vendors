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

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.config.RedisTemplateUtil;
import com.platform.common.utils.Query;
import com.platform.common.validator.AbstractAssert;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cxd
 */
@Service("userService")
public class MallUserServiceImpl extends ServiceImpl<MallUserDao, MallUserEntity> implements MallUserService {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    @Override
    public List<MallUserEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page<MallUserEntity> queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.REGISTER_TIME");
        params.put("asc", false);
        Page<MallUserEntity> page = new Query<MallUserEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserPage(page, params));
    }

    @Override
    public boolean add(MallUserEntity mallUser) {
        return this.save(mallUser);
    }

    @Override
    public boolean update(MallUserEntity mallUser) {
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
    public MallUserEntity queryByMobile(String mobile) {
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("MOBILE", mobile), false);
    }

    @Override
    public MallUserEntity confirmMember(String userId) {
        MallUserEntity mallUser = this.getById(userId);
        if (mallUser == null) {
            throw new BusinessException("用户不存在");
        }
        if(StrUtil.isNotBlank(mallUser.getUserTags())){
            List<String> tags = Arrays.asList(mallUser.getUserTags().split(","));
            if(tags.contains("1"))
                throw new BusinessException("已经是商城客户");
            if(!tags.contains("2")) {
                throw new BusinessException("用户未做商城会员申请");
            }else{
                tags = tags.stream().filter(item->!"2".equals(item)).collect(Collectors.toList());
                tags.add("1");
                mallUser.setUserTags(StrUtil.join(",",tags.iterator()));
                baseMapper.updateById(mallUser);
            }
        }

        return mallUser;
    }

    @Override
    public MallUserEntity loginByMobile(String mobile, String password) {
        MallUserEntity user = queryByMobile(mobile);
        AbstractAssert.isNull(user, "该手机暂未绑定用户");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new BusinessException("手机号或密码错误");
        }

        return user;
    }

    @Override
    public MallUserEntity selectByOpenId(String openId) {
        MallUserEntity userEntity = new MallUserEntity();
        userEntity.setOpenId(openId);
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("OPEN_ID", openId), false);
    }

    @Override
    public MallUserEntity selectByMpOpenId(String mpOpenId) {
        MallUserEntity userEntity = new MallUserEntity();
        userEntity.setMpOpenId(mpOpenId);
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("MP_OPEN_ID", mpOpenId), false);
    }

    @Override
    public MallUserEntity selectByOpenUnionId(String unionId) {
        MallUserEntity userEntity = new MallUserEntity();
        userEntity.setUnionId(unionId);
        return this.getOne(new QueryWrapper<MallUserEntity>().eq("UNION_ID", unionId), false);
    }
}
