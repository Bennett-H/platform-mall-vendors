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
package com.platform.modules.sys.controller;

import com.platform.annotation.LoginUser;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.SysMpUserEntity;
import com.platform.modules.oss.cloud.UploadFactory;
import com.platform.modules.oss.entity.SysOssEntity;
import com.platform.modules.oss.service.SysOssService;
import com.platform.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;

/**
 * 文件上传
 *
 * @author cxd
 * @since 2019-01-17 16:21:01
 */
@Slf4j
@RestController
@RequestMapping("/app/oss")
public class SysOssController extends AbstractController {
    @Autowired
    private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * UEditor后台配置请求
     */
    private static final String CONFIG_ACTION = "config";

    /**
     * 文件管理列表
     */
    private static final String LIST_ACTION = "listimage";


    /**
     * 上传文件
     *
     * @param file file
     * @return RestResponse
     */
    @RequestMapping("/upload")
    public Object upload(@RequestParam(value = "file", required = false) MultipartFile file,
                         @LoginUser SysMpUserEntity loginUser) throws Exception {
        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(Constant.DOT));
        String url = UploadFactory.build().uploadSuffix(file.getBytes(), suffix);

        String suffixLowerCase = suffix.toLowerCase();
        String p12 = ".p12";
        if (!suffixLowerCase.contains(p12)) {
            //保存文件信息
            //SysUserEntity user = getUser();
            SysOssEntity ossEntity = new SysOssEntity();
            ossEntity.setUrl(url);
            ossEntity.setCreateDate(new Date());
            ossEntity.setCreateUserId(loginUser.getUserId());
            //ossEntity.setCreateUserOrgNo(user.getOrgNo());
            sysOssService.save(ossEntity);
        }
        //返回兼容UEditor的参数
        return RestResponse.success().put("url", url).put("state", "SUCCESS").put("title", url).put("name", file.getOriginalFilename()).put("original", url);
    }


}
