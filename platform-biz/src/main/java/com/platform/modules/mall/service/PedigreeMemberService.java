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
package com.platform.modules.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.PedigreeMemberEntity;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberAppTreeVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberSaveVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberVO;
import com.platform.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 族谱成员Service接口
 *
 * @author cxd
 * @since 2023-11-21 21:09:20
 */
public interface PedigreeMemberService extends IService<PedigreeMemberEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PedigreeMemberVO> queryAll(Map<String, Object> params);

    /**
     * 分页查询族谱成员
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    PedigreeMemberVO info(String id);

    /**
     * 新增族谱成员
     *
     * @param pedigreeMember 族谱成员
     * @return 新增结果
     */
    boolean add(SysUserEntity user, PedigreeMemberSaveVO pedigreeMember);

    /**
     * 根据主键更新族谱成员
     *
     * @param pedigreeMember 族谱成员
     * @return 更新结果
     */
    boolean update(SysUserEntity user, PedigreeMemberSaveVO pedigreeMember);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    RestResponse deleteBatch(SysUserEntity user, String[] ids);

    Page queryAppPage(Map<String, Object> params);

    List<PedigreeMemberAppTreeVO> pedigreeTree();
}
