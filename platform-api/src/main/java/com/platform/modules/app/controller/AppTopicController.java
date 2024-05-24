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
package com.platform.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallTopicEntity;
import com.platform.modules.mall.service.impl.MallTopicServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2018-08-11 08:32<br>
 * 描述: AppTopicController <br>
 */
@Api(tags = "AppTopicController|文章管理控制器")
@RestController
@RequestMapping("/app/topic")
public class AppTopicController extends AppBaseController {
    @Autowired
    private MallTopicServiceImpl mallTopicService;

    /**
     * 文章列表
     *
     * @param current 当前页码
     * @param limit   每页条数
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("list")
    @ApiOperation(value = "文章列表", notes = "文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "topicCategoryId", value = "文章类别Id", example = "文章类别Id：1", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "文章大类别Id", example = "文章大类别Id：1", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeValue", value = "文章大类别字典值", example = "文章大类别字典值 首页:sy,族谱:zp,商会资源:shzy,资讯:zx", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orderBy", value = "排序字段", example = "readCount: 热度；date：新鲜", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序", example = "asc: 升序；desc：降序", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse topicList(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit,
                                  @RequestParam(defaultValue = "") String topicCategoryId, @RequestParam(defaultValue = "date") String orderBy,
                                  @RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String typeValue,
                                  @RequestParam(defaultValue = "desc") String sortType) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("page", current);
        map.put("limit", limit);
        map.put("topicCategoryId", topicCategoryId);
        map.put("type", type);
        map.put("typeValue", typeValue);
        map.put("orderBy", orderBy);
        map.put("sortType", sortType);
        Page data = mallTopicService.queryPage(map);
        return RestResponse.success().put("data", data);
    }

    /**
     * 文章详情
     *
     * @param id 文章id
     * @return RestResponse
     */
    @IgnoreAuth
    @ApiOperation(value = "文章详情", notes = "文章详情")
    @GetMapping("detail")
    @ApiImplicitParam(paramType = "query", name = "id", value = "文章id", example = "1", required = true, dataType = "string")
    public RestResponse topicDetail(@RequestParam String id) {
        MallTopicEntity topicEntity = mallTopicService.getById(id);

        topicEntity.setReadCount(topicEntity.getReadCount() + 1);
        mallTopicService.updateById(topicEntity);

        return RestResponse.success().put("data", topicEntity);
    }

    /**
     * 相关文章
     *
     * @return RestResponse
     */
    @GetMapping("related")
    @ApiOperation(value = "相关文章", notes = "相关文章")
    @IgnoreAuth
    public RestResponse related() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("limit", 4);
        Page data = mallTopicService.queryPage(map);
        return RestResponse.success().put("data", data);
    }
}
