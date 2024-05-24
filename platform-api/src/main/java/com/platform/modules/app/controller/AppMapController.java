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

import com.alibaba.fastjson.JSONObject;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.AbstractAssert;
import com.platform.modules.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/map")
@Api(tags = "AppMapController|地图接口")
public class AppMapController extends AppBaseController {
    @Autowired
    private SysConfigService configService;

    public static final String URL_SEARCH = "https://apis.map.qq.com/ws/place/v1/search";
    public static final String URL_SUGGESTION = "https://apis.map.qq.com/ws/place/v1/suggestion";
    public static final String URL_GET_GEOCODER = "https://apis.map.qq.com/ws/geocoder/v1/";
    public static final String URL_CITY_LIST = "https://apis.map.qq.com/ws/district/v1/list";
    public static final String URL_AREA_LIST = "https://apis.map.qq.com/ws/district/v1/getchildren";
    public static final String URL_DISTANCE = "https://apis.map.qq.com/ws/distance/v1/";
    public static final String URL_DIRECTION = "https://apis.map.qq.com/ws/direction/v1/";

    /**
     * 地址解析
     *
     * @param address 地址（注：地址中请包含城市名称，以及需要对地址进行URL编码，否则会影响解析效果）
     * @return RestResponse
     */
    @GetMapping("/geocoder")
    @ApiOperation(value = "地址解析", notes = "地址解析")
    @ApiImplicitParam(paramType = "query", name = "address", value = "地址", required = true, example = "北京市海淀区彩和坊路海淀西大街74号", dataType = "string")
    public RestResponse geocoder(@RequestParam String address) {
        AbstractAssert.isBlank(address, "地址不能为空！");

        String key = configService.getValue(Constant.QQ_MAP_KEY);
        Map<String, String> hashMap = new HashMap<>(4);
        hashMap.put("key", key);
        hashMap.put("address", address);

        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(URL_GET_GEOCODER + "?key={key}&address={address}", JSONObject.class, hashMap);

        return RestResponse.success().put("result", json.get("result"));
    }

    /**
     * 逆地址解析
     * 经纬度（GCJ02坐标系），格式：
     *
     * @param location location=lat<纬度>,lng<经度>
     * @return RestResponse
     */
    @GetMapping("/reverseGeocoder")
    @ApiOperation(value = "逆地址解析", notes = "逆地址解析")
    @ApiImplicitParam(paramType = "query", name = "location", value = "经纬度", required = true, example = "lat<纬度>,lng<经度>", dataType = "string")
    public RestResponse reverseGeocoder(@RequestParam String location) {
        AbstractAssert.isBlank(location, "位置不能为空！");

        String key = configService.getValue(Constant.QQ_MAP_KEY);
        Map<String, String> hashMap = new HashMap<>(4);
        hashMap.put("key", key);
        hashMap.put("location", location);

        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(URL_GET_GEOCODER + "?key={key}&location={location}", JSONObject.class, hashMap);

        return RestResponse.success().put("result", json.get("result"));
    }

    /**
     * 智能地址解析
     *
     * @param address 地址（地址中请包含城市名称，否则会影响准确度）
     * @return RestResponse
     */
    @GetMapping("/aiGeocoder")
    @ApiOperation(value = "智能地址解析", notes = "智能地址解析")
    @ApiImplicitParam(paramType = "query", name = "address", value = "地址", required = true, example = "北京市海淀区彩和坊路海淀西大街74号张三丰13666665254", dataType = "string")
    public RestResponse aiGeocoder(@RequestParam String address) {
        AbstractAssert.isBlank(address, "地址不能为空！");

        String key = configService.getValue(Constant.QQ_MAP_KEY);
        Map<String, String> hashMap = new HashMap<>(4);
        hashMap.put("key", key);
        hashMap.put("smart_address", address);

        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(URL_GET_GEOCODER + "?key={key}&smart_address={smart_address}", JSONObject.class, hashMap);

        return RestResponse.success().put("result", json.get("result"));
    }
}
