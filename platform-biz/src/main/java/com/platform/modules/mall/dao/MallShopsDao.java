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
package com.platform.modules.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.modules.mall.entity.MallShopsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 店铺Dao
 *
 * @author cxd
 * @since 2019-07-03 13:51:29
 */
@Mapper
public interface MallShopsDao extends BaseMapper<MallShopsEntity> {
    /**
     * 分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MallShopsEntity> selectListPage(Page<MallShopsEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 列表查询
     *
     * @param params 查询参数
     * @return List
     */
    List<MallShopsEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 根据管理员id，获取店铺
     *
     * @param params
     * @return
     */
    List<MallShopsEntity> selectByUserId(@Param("params") Map<String, Object> params);
}
