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

import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.service.impl.MallTopicCategoryServiceImpl;
import com.platform.modules.mall.vo.mall.MallTopicCategoryVO;
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
import java.util.List;
import java.util.Map;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2018-08-11 08:32<br>
 * 描述: AppTopicController <br>
 */
@Api(tags = "AppTopicCategoryController|文章分类管理控制器")
@RestController
@RequestMapping("/app/topiccategory")
public class AppTopicCategoryController extends AppBaseController {
    @Autowired
    private MallTopicCategoryServiceImpl mallTopicCategoryService;

    /**
     * 文章分类列表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("list")
    @ApiOperation(value = "文章分类列表", notes = "文章分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "大分类类型id", example = "", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "typeValue", value = "大分类类型字典值", example = "文章大类别字典值 首页:sy,族谱:zp,商会资源:shzy,资讯:zx", dataType = "string")
    })
    public RestResponse topicCategoryList(@RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String typeValue) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("type", type);
        map.put("typeValue", typeValue);
        List<MallTopicCategoryVO> list = mallTopicCategoryService.queryAll(map);
        return RestResponse.success().put("data", list);
    }
}
