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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.dao.PedigreeMemberDao;
import com.platform.modules.mall.dao.PedigreeRelationDao;
import com.platform.modules.mall.entity.PedigreeMemberDescEntity;
import com.platform.modules.mall.entity.PedigreeMemberEntity;
import com.platform.modules.mall.entity.PedigreeRelationEntity;
import com.platform.modules.mall.service.PedigreeMemberDescService;
import com.platform.modules.mall.service.PedigreeMemberService;
import com.platform.modules.mall.service.PedigreeRelationService;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberAppPageVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberAppTreeVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberSaveVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberVO;
import com.platform.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 族谱成员Service实现类
 *
 * @author cxd
 * @since 2023-11-21 21:09:20
 */
@Service("pedigreeMemberService")
public class PedigreeMemberServiceImpl extends ServiceImpl<PedigreeMemberDao, PedigreeMemberEntity> implements PedigreeMemberService {

    @Autowired
    private PedigreeRelationService pedigreeRelationService;
    @Autowired
    private PedigreeMemberDescService pedigreeMemberDescService;
    @Autowired
    private PedigreeRelationDao pedigreeRelationDao;

    @Override
    public List<PedigreeMemberVO> queryAll(Map<String, Object> params) {
        String id = params.get("id").toString();
        if (StrUtil.isNotEmpty(id)) {
            List<String> childrenIds = pedigreeRelationDao.childrenIds(id);
            childrenIds.add(id);
            params.put("childrenIds", childrenIds);
        }
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        Page<PedigreeMemberVO> page = new Query<PedigreeMemberVO>(params).getPage();
        return page.setRecords(baseMapper.selectPedigreeMemberPage(page, params));
    }

    @Override
    public Page queryAppPage(Map<String, Object> params) {
        Page<PedigreeMemberAppPageVO> page = new Query<PedigreeMemberAppPageVO>(params).getPage();
        return page.setRecords(baseMapper.selectPedigreeMemberAppPage(page, params));
    }

    @Override
    public List<PedigreeMemberAppTreeVO> pedigreeTree() {
        Map<String, Object> params = new HashMap<>();
        params.put("layer", 0);
        List<PedigreeMemberAppTreeVO> roots = baseMapper.simpleQueryList(params);
        for (PedigreeMemberAppTreeVO root : roots) {
            getTree(root);
        }
        return roots;
    }

    private void getTree(PedigreeMemberAppTreeVO root) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", root.getId());
        List<PedigreeMemberAppTreeVO> children = baseMapper.simpleQueryList(params);
        for (PedigreeMemberAppTreeVO child : children) {
            getTree(child);
        }
        root.setChildren(children);
    }

    @Override
    public PedigreeMemberVO info(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<PedigreeMemberVO> list = baseMapper.selectList(params);
        if (CollectionUtil.isNotEmpty(list)) {
            PedigreeMemberVO vo = list.get(0);
            String parentId = vo.getParentId();
            List<PedigreeMemberVO.Brother> brothers = new ArrayList<>();
            if (StrUtil.isNotEmpty(parentId)) {
                params.remove("id");
                params.put("parentId", parentId);
                List<PedigreeMemberVO> brotherList = baseMapper.selectList(params);
                for (PedigreeMemberVO item : brotherList) {
                    if (StrUtil.equals(id, item.getId())) {
                        continue;
                    }
                    brothers.add(PedigreeMemberVO.Brother.builder()
                            .id(item.getId()).name(item.getName()).headImg(item.getHeadImg()).build());
                }
            }
            vo.setBrothers(brothers);
            return vo;
        }
        return null;
    }

    @Override
    public boolean add(SysUserEntity user, PedigreeMemberSaveVO pedigreeMember) {
        Date date = new Date();
        pedigreeMember.setName(pedigreeMember.getFirstName() + pedigreeMember.getLastName());
        pedigreeMember.setCreateTime(date);
        pedigreeMember.setUpdateTime(date);
        pedigreeMember.setCreateUserId(user.getUserId());
        pedigreeMember.setUpdateUserId(user.getUserId());
        this.save(pedigreeMember);
        PedigreeMemberDescEntity pedigreeMemberDescEntity = PedigreeMemberDescEntity.builder().intro(pedigreeMember.getIntro())
                .description(pedigreeMember.getDescription()).selfId(pedigreeMember.getId())
                .createTime(date).createUserId(user.getUserId()).updateTime(date).updateUserId(user.getUserId()).build();
        pedigreeMemberDescService.save(pedigreeMemberDescEntity);
        String parentId = pedigreeMember.getParentId();
        int layer;
        if (StrUtil.isBlank(parentId)) {
            layer = 0;
        } else {
            PedigreeRelationEntity parent = pedigreeRelationService.getOne(
                    Wrappers.<PedigreeRelationEntity>lambdaQuery().eq(PedigreeRelationEntity::getSelfId, parentId));
            layer = parent.getLayer() + 1;
        }
        PedigreeRelationEntity pedigreeRelationEntity = PedigreeRelationEntity.builder().selfId(pedigreeMember.getId())
                .parentId(parentId).layer(layer).createTime(date).createUserId(user.getUserId()).updateTime(date)
                .updateUserId(user.getUserId()).build();
        pedigreeRelationService.save(pedigreeRelationEntity);
        return true;
    }

    @Override
    public boolean update(SysUserEntity user, PedigreeMemberSaveVO pedigreeMember) {
        Date date = new Date();
        pedigreeMember.setUpdateTime(date);
        pedigreeMember.setUpdateUserId(user.getUserId());
        pedigreeMember.setName(pedigreeMember.getFirstName() + pedigreeMember.getLastName());
        this.updateById(pedigreeMember);
        pedigreeMemberDescService.update(Wrappers.<PedigreeMemberDescEntity>lambdaUpdate()
                .set(PedigreeMemberDescEntity::getIntro, pedigreeMember.getIntro())
                .set(PedigreeMemberDescEntity::getDescription, pedigreeMember.getDescription())
                .set(PedigreeMemberDescEntity::getUpdateTime, date)
                .set(PedigreeMemberDescEntity::getUpdateUserId, user.getUserId())
                .eq(PedigreeMemberDescEntity::getSelfId, pedigreeMember.getId()));
        String parentId = pedigreeMember.getParentId();
        PedigreeRelationEntity pedigreeRelationEntity = pedigreeRelationService.getOne(
                Wrappers.<PedigreeRelationEntity>lambdaQuery()
                .eq(PedigreeRelationEntity::getSelfId, pedigreeMember.getId()));
        if (!StrUtil.equals(parentId,pedigreeRelationEntity.getParentId())) {
            PedigreeRelationEntity parent = pedigreeRelationService.getOne(
                    Wrappers.<PedigreeRelationEntity>lambdaQuery()
                    .eq(PedigreeRelationEntity::getSelfId, parentId));
            int layer = parent == null ? 0 : parent.getLayer() + 1;
            pedigreeRelationEntity.setLayer(layer);
            pedigreeRelationEntity.setParentId(parentId);
            pedigreeRelationEntity.setUpdateUserId(user.getUserId());
            pedigreeRelationEntity.setUpdateTime(date);
            List<PedigreeRelationEntity> children = new ArrayList<>();
            children.add(pedigreeRelationEntity);
            children(user.getUserId(), pedigreeRelationEntity, children);
            pedigreeRelationService.updateBatchById(children, children.size());
        }
        return true;
    }

    /**
     * 递归更新子代的layer
     * @param userId
     * @param parent
     * @param relationTrees
     */
    private void children(String userId, PedigreeRelationEntity parent, List<PedigreeRelationEntity> relationTrees) {
        Date date = new Date();
        List<PedigreeRelationEntity> children = pedigreeRelationService.list(Wrappers.<PedigreeRelationEntity>lambdaQuery()
                .eq(PedigreeRelationEntity::getParentId, parent.getSelfId()));
        for (PedigreeRelationEntity child : children) {
            child.setLayer(parent.getLayer() + 1);
            child.setUpdateTime(date);
            child.setUpdateUserId(userId);
            relationTrees.add(child);
            children(userId, child, relationTrees);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteBatch(SysUserEntity user, String[] ids) {
        Date date = new Date();
        List<String> canDelete = new ArrayList<>();
        List<String> cantDelete = new ArrayList<>();
        for (String id : ids) {
            List<PedigreeRelationEntity> children = pedigreeRelationService.list(Wrappers.<PedigreeRelationEntity>lambdaQuery()
                    .eq(PedigreeRelationEntity::getParentId, id));
            if (CollectionUtil.isEmpty(children)) {
                canDelete.add(id);
            } else {
                cantDelete.add(id);
            }
        }
        if (CollectionUtil.isNotEmpty(canDelete)) {
            this.update(Wrappers.<PedigreeMemberEntity>lambdaUpdate().in(PedigreeMemberEntity::getId, canDelete)
                    .set(PedigreeMemberEntity::getUpdateTime, date)
                    .set(PedigreeMemberEntity::getUpdateUserId, user.getUserId())
                    .set(PedigreeMemberEntity::getIsDelete, 0));
            pedigreeMemberDescService.update(Wrappers.<PedigreeMemberDescEntity>lambdaUpdate().in(PedigreeMemberDescEntity::getSelfId, canDelete)
                    .set(PedigreeMemberDescEntity::getUpdateTime, date)
                    .set(PedigreeMemberDescEntity::getUpdateUserId, user.getUserId())
                    .set(PedigreeMemberDescEntity::getIsDelete, 0));
            pedigreeRelationService.update(Wrappers.<PedigreeRelationEntity>lambdaUpdate().in(PedigreeRelationEntity::getSelfId, canDelete)
                    .set(PedigreeRelationEntity::getUpdateTime, date)
                    .set(PedigreeRelationEntity::getUpdateUserId, user.getUserId())
                    .set(PedigreeRelationEntity::getIsDelete, 0));
        }
        if (CollectionUtil.isNotEmpty(cantDelete)) {
            Map<String, Object> params = new HashMap<>();
            params.put("ids", cantDelete);
            List<PedigreeMemberVO> list = this.baseMapper.selectList(params);
            String cantName = list.stream().map(PedigreeMemberVO::getName).collect(Collectors.joining(","));
            return RestResponse.success().put("message", cantName + "有子类不能删");
        }
        return RestResponse.success().put("message", "操作成功");
    }
}
