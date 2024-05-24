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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.mall.entity.PedigreeMemberEntity;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberAppPageVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberAppTreeVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 族谱成员Dao
 *
 * @author cxd
 * @since 2023-11-21 21:09:20
 */
@Mapper
public interface PedigreeMemberDao extends BaseMapper<PedigreeMemberEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PedigreeMemberVO> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PedigreeMemberVO> selectPedigreeMemberPage(IPage page, @Param("params") Map<String, Object> params);

    List<PedigreeMemberAppPageVO> selectPedigreeMemberAppPage(IPage page, @Param("params") Map<String, Object> params);

    List<PedigreeMemberAppTreeVO> simpleQueryList(@Param("params") Map<String, Object> params);

    List<PedigreeMemberVO> selectList(@Param("params") Map<String, Object> params);
}
