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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallCategoryDao;
import com.platform.modules.mall.entity.MallCategoryEntity;
import com.platform.modules.mall.entity.MallGoodsEntity;
import com.platform.modules.mall.service.MallCategoryService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 商品分类Service实现类
 *
 * @author cxd
 * @since 2019-07-02 19:53:21
 */
@Service("mallCategoryService")
@CacheConfig(cacheNames = {"mallCategoryServiceCache"})
public class MallCategoryServiceImpl extends ServiceImpl<MallCategoryDao, MallCategoryEntity> implements MallCategoryService {

    @Override
    //@Cacheable
    public List<MallCategoryEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    //@Cacheable
    public List<MallCategoryEntity> queryAllByWrapper(QueryWrapper<MallCategoryEntity> wrapper) {
        return this.list(wrapper);
    }

    @Override
    //@Cacheable
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.SORT");
        params.put("asc", false);
        Page<MallCategoryEntity> page = new Query<MallCategoryEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallCategoryPage(page, params));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean add(MallCategoryEntity mallCategory) {
        if (mallCategory.getLevel() == Constant.TWO && StringUtils.isBlank(mallCategory.getParentId())) {
            throw new BusinessException("父节点不能为空！");
        }
        if (StringUtils.isBlank(mallCategory.getParentId())) {
            mallCategory.setParentId("0");
        }
        return this.save(mallCategory);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(MallCategoryEntity mallCategory) {
        if (StringUtils.isBlank(mallCategory.getParentId())) {
            mallCategory.setParentId("0");
        }
        return this.updateById(mallCategory);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean delete(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    @Cacheable
    public List<MallCategoryEntity> getCategoryByData(List<String> categoryIds) {
        List<MallCategoryEntity> filterCategory = new ArrayList<>();
        MallCategoryEntity rootCategory = new MallCategoryEntity();
        rootCategory.setId(Constant.BLANK);
        rootCategory.setName("全部");
        rootCategory.setChecked(false);
        filterCategory.add(rootCategory);

        if (null != categoryIds && categoryIds.size() > 0) {
            //查找二级分类的parent_id
            Map<String, Object> categoryParam = new HashMap<>(2);
            categoryParam.put("ids", categoryIds);
            List<MallCategoryEntity> parentCategoryList = this.queryAll(categoryParam);
            //
            List<String> parentIds = new ArrayList<>();
            for (MallCategoryEntity categoryEntity : parentCategoryList) {
                parentIds.add(categoryEntity.getParentId());
            }
            //一级分类
            categoryParam = new HashMap<>(2);
            categoryParam.put("ids", parentIds);
            List<MallCategoryEntity> parentCategory = this.queryAll(categoryParam);
            if (null != parentCategory) {
                filterCategory.addAll(parentCategory);
            }
        }
        return filterCategory;
    }
}
