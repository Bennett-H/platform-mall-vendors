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
package com.platform.modules.wx.service.impl;

import com.platform.common.utils.Constant;
import com.platform.modules.wx.dto.PageSizeConstant;
import com.platform.modules.wx.service.WxAssetsService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author cxd
 * @date 2021-05-06 18:30:15
 */
@Service("wxAssetsService")
@Slf4j
public class WxAssetsServiceImpl implements WxAssetsService {
    @Autowired
    WxMpService wxMpService;

    @Override
    public WxMpMaterialCountResult materialCount(String appid) throws WxErrorException {
        log.info("从API获取素材总量");
        wxMpService.switchoverTo(appid);
        return wxMpService.getMaterialService().materialCount();
    }

    @Override
    public WxMpMaterialNews materialNewsInfo(String appid, String mediaId) throws WxErrorException {
        log.info("从API获取图文素材详情,mediaId={}", mediaId);
        wxMpService.switchoverTo(appid);
        return wxMpService.getMaterialService().materialNewsInfo(mediaId);
    }

    @Override
    public WxMpMaterialFileBatchGetResult materialFileBatchGet(String appid, String type, int page) throws WxErrorException {
        log.info("从API获取媒体素材列表,type={},page={}", type, page);
        wxMpService.switchoverTo(appid);
        final int pageSize = PageSizeConstant.PAGE_SIZE_SMALL;
        int offset = (page - 1) * pageSize;
        return wxMpService.getMaterialService().materialFileBatchGet(type, offset, pageSize);
    }

    @Override
    public WxMpMaterialNewsBatchGetResult materialNewsBatchGet(String appid, int page) throws WxErrorException {
        log.info("从API获取媒体素材列表,page={}", page);
        wxMpService.switchoverTo(appid);
        final int pageSize = PageSizeConstant.PAGE_SIZE_SMALL;
        int offset = (page - 1) * pageSize;
        return wxMpService.getMaterialService().materialNewsBatchGet(offset, pageSize);
    }

    @Override
    public WxMpMaterialUploadResult materialFileUpload(String appid, String mediaType, String fileName, MultipartFile file) throws WxErrorException, IOException {
        log.info("上传媒体素材：{}", fileName);
        wxMpService.switchoverTo(appid);
        String originalFilename = file.getOriginalFilename();
        Path tempFile = Files.createTempFile(fileName + "--", Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(Constant.DOT)));
        file.transferTo(tempFile);
        WxMpMaterial wxMaterial = new WxMpMaterial();
        wxMaterial.setFile(tempFile.toFile());
        wxMaterial.setName(fileName);
        if (WxConsts.MediaFileType.VIDEO.equals(mediaType)) {
            wxMaterial.setVideoTitle(fileName);
        }
        WxMpMaterialUploadResult res = wxMpService.getMaterialService().materialFileUpload(mediaType, wxMaterial);
        tempFile.toFile().deleteOnExit();
        return res;
    }

    @Override
    public boolean materialDelete(String appid, String mediaId) throws WxErrorException {
        log.info("删除素材，mediaId={}", mediaId);
        wxMpService.switchoverTo(appid);
        return wxMpService.getMaterialService().materialDelete(mediaId);
    }
}
