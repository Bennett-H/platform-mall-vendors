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

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveRoomInfo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.modules.mall.dao.MallRoomDao;
import com.platform.modules.mall.entity.MallRoomAllGoodsEntity;
import com.platform.modules.mall.entity.MallRoomEntity;
import com.platform.modules.mall.entity.MallRoomGoodsEntity;
import com.platform.modules.mall.entity.MallRoomMediaEntity;
import com.platform.modules.mall.service.MallRoomAllGoodsService;
import com.platform.modules.mall.service.MallRoomGoodsService;
import com.platform.modules.mall.service.MallRoomMediaService;
import com.platform.modules.mall.service.MallRoomService;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直播房间信息Service实现类
 *
 * @author cxd
 * @since 2020-03-30 18:52:09
 */
@Service("mallRoomService")
@CacheConfig(cacheNames = {"mallRoomServiceCache"})
public class MallRoomServiceImpl extends ServiceImpl<MallRoomDao, MallRoomEntity> implements MallRoomService {
    @Autowired
    private WxMaService maService;
    @Autowired
    private MallRoomGoodsService roomGoodsService;
    @Autowired
    private MallRoomAllGoodsService roomAllGoodsService;
    @Autowired
    private MallRoomMediaService roomMediaService;

    @Override
    @Cacheable
    public List<MallRoomEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    @Cacheable
    public Page<MallRoomEntity> queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.START_TIME");
        params.put("asc", false);
        Page<MallRoomEntity> page = new Query<MallRoomEntity>(params).getPage();
        Page<MallRoomEntity> result = page.setRecords(baseMapper.selectMallRoomPage(page, params));

        if (!CollectionUtils.isEmpty(result.getRecords())) {
            for (MallRoomEntity roomEntity : result.getRecords()) {
                Map<String, Object> goodsParam = new HashMap<>(4);
                goodsParam.put("roomid", roomEntity.getRoomid());
                List<MallRoomGoodsEntity> allGoodsEntities = roomGoodsService.queryAll(goodsParam);
                roomEntity.setAllGoods(allGoodsEntities);
            }
        }
        return result;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean add(MallRoomEntity mallRoom) {
        WxMaLiveRoomInfo roomInfo = new WxMaLiveRoomInfo();
        roomInfo.setName(mallRoom.getName());
        roomInfo.setStartTime(mallRoom.getStartTime() / 1000);
        roomInfo.setEndTime(mallRoom.getEndTime() / 1000);
        roomInfo.setAnchorName(mallRoom.getAnchorName());
        roomInfo.setAnchorWechat(mallRoom.getAnchorWechat());
        roomInfo.setSubAnchorWechat(mallRoom.getSubAnchorWechat());
        roomInfo.setCreaterWechat(mallRoom.getCreaterWechat());
        roomInfo.setIsFeedsPublic(mallRoom.getIsFeedsPublic());
        roomInfo.setType(mallRoom.getType());
        roomInfo.setScreenType(mallRoom.getScreenType());
        roomInfo.setCloseLike(mallRoom.getCloseLike());
        roomInfo.setCloseGoods(mallRoom.getCloseGoods());
        roomInfo.setCloseComment(mallRoom.getCloseComment());
        roomInfo.setCloseReplay(mallRoom.getCloseReplay());
        roomInfo.setCloseShare(mallRoom.getCloseShare());
        roomInfo.setCloseKf(mallRoom.getCloseKf());
        try {
            // 上传临时素材
            WxMediaUploadResult mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                    HttpUtils.getNetResource(mallRoom.getCoverImg()));
            roomInfo.setCoverImg(mediaUploadResult.getMediaId());

            // 上传临时素材
            mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                    HttpUtils.getNetResource(mallRoom.getFeedsImg()));
            roomInfo.setFeedsImg(mediaUploadResult.getMediaId());

            // 上传临时素材
            mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                    HttpUtils.getNetResource(mallRoom.getShareImg()));
            roomInfo.setShareImg(mediaUploadResult.getMediaId());

            Integer roomId = maService.getLiveService().createRoom(roomInfo).getRoomId();
            mallRoom.setRoomid(roomId);
        } catch (WxErrorException e) {
            int errorCode = 300036;
            if (e.getError().getErrorCode() == errorCode) {
                throw new BusinessException(JSON.parseObject(e.getError().getJson()).get("qrcode_url") + Constant.BLANK, errorCode);
            } else {
                throw new BusinessException(e.getError().getErrorMsg(), e.getError().getErrorCode());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        mallRoom.setStartTime(mallRoom.getStartTime() / 1000);
        mallRoom.setEndTime(mallRoom.getEndTime() / 1000);
        mallRoom.setLiveStatus(102);
        return this.save(mallRoom);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(MallRoomEntity mallRoom) {
        WxMaLiveRoomInfo roomInfo = new WxMaLiveRoomInfo();
        roomInfo.setId(mallRoom.getRoomid());
        roomInfo.setName(mallRoom.getName());
        roomInfo.setStartTime(mallRoom.getStartTime() / 1000);
        roomInfo.setEndTime(mallRoom.getEndTime() / 1000);
        roomInfo.setAnchorName(mallRoom.getAnchorName());
        roomInfo.setAnchorWechat(mallRoom.getAnchorWechat());
        roomInfo.setIsFeedsPublic(mallRoom.getIsFeedsPublic());
        roomInfo.setCloseLike(mallRoom.getCloseLike());
        roomInfo.setCloseGoods(mallRoom.getCloseGoods());
        roomInfo.setCloseComment(mallRoom.getCloseComment());
        roomInfo.setCloseReplay(mallRoom.getCloseReplay());
        roomInfo.setCloseShare(mallRoom.getCloseShare());
        roomInfo.setCloseKf(mallRoom.getCloseKf());
        try {
            // 上传临时素材
            WxMediaUploadResult mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                    HttpUtils.getNetResource(mallRoom.getCoverImg()));
            roomInfo.setCoverImg(mediaUploadResult.getMediaId());

            // 上传临时素材
            if (mallRoom.getFeedsImg() != null) {
                mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                        HttpUtils.getNetResource(mallRoom.getFeedsImg()));
                roomInfo.setFeedsImg(mediaUploadResult.getMediaId());
            }

            // 上传临时素材
            mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                    HttpUtils.getNetResource(mallRoom.getShareImg()));
            roomInfo.setShareImg(mediaUploadResult.getMediaId());

            maService.getLiveService().editRoom(roomInfo);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        mallRoom.setStartTime(mallRoom.getStartTime() / 1000);
        mallRoom.setEndTime(mallRoom.getEndTime() / 1000);
        mallRoom.setLiveStatus(102);
        return this.updateById(mallRoom);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int delete(Integer roomid) {
        try {
            maService.getLiveService().deleteRoom(roomid);
        } catch (WxErrorException e) {
            throw new BusinessException(e.getMessage());
        }
        return baseMapper.delete(new UpdateWrapper<MallRoomEntity>().eq("ROOMID", roomid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteBatch(Integer[] roomids) {
        for (Integer roomid : roomids) {
            try {
                maService.getLiveService().deleteRoom(roomid);
                baseMapper.delete(new UpdateWrapper<MallRoomEntity>().eq("ROOMID", roomid));
            } catch (WxErrorException e) {
                throw new BusinessException(e.getMessage());
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean getLiveInfo(Integer start, Integer limit) {
        try {
            WxMaLiveResult maGetLiveInfo = maService.getLiveService().getLiveInfo(start, limit);
            if (maGetLiveInfo.getRoomInfos().size() > 0) {
                List<MallRoomEntity> mallRoomEntityList = new ArrayList<>();
                maGetLiveInfo.getRoomInfos().forEach(roomInfo -> {
                    MallRoomEntity roomEntity = new MallRoomEntity();
                    roomEntity.setRoomid(roomInfo.getRoomId());
                    roomEntity.setName(roomInfo.getName());
                    roomEntity.setCoverImg(roomInfo.getCoverImg());
                    roomEntity.setStartTime(roomInfo.getStartTime());
                    roomEntity.setEndTime(roomInfo.getEndTime());
                    roomEntity.setAnchorName(roomInfo.getAnchorName());
                    roomEntity.setShareImg(roomInfo.getShareImg());
                    roomEntity.setLiveStatus(roomInfo.getLiveStatus());
                    roomEntity.setGoods(roomInfo.getGoods());
                    mallRoomEntityList.add(roomEntity);
                });
                baseMapper.delete(new UpdateWrapper<>());
                this.saveBatch(mallRoomEntityList);

                if (mallRoomEntityList.size() > 0) {
                    roomGoodsService.remove(new UpdateWrapper<>());
                    List<MallRoomGoodsEntity> mallRoomGoodsEntities = new ArrayList<>();
                    List<MallRoomMediaEntity> mallRoomMediaEntities = new ArrayList<>();
                    mallRoomEntityList.forEach(mallRoomEntity -> {
                        mallRoomEntity.getGoods().forEach(goods -> {
                            MallRoomGoodsEntity mallRoomGoodsEntity = new MallRoomGoodsEntity();

                            mallRoomGoodsEntity.setRoomid(mallRoomEntity.getRoomid());
                            mallRoomGoodsEntity.setName(goods.getName());
                            mallRoomGoodsEntity.setPrice(goods.getPrice());
                            mallRoomGoodsEntity.setUrl(goods.getUrl());
                            mallRoomGoodsEntity.setCoverImg(goods.getCoverImg());

                            mallRoomGoodsEntities.add(mallRoomGoodsEntity);
                        });

                        try {
                            WxMaLiveResult result = maService.getLiveService().getLiveReplay(mallRoomEntity.getRoomid(), 0, 100);
                            List<WxMaLiveResult.LiveReplay> list = result.getLiveReplay();
                            if (list != null && list.size() > 0) {
                                list.forEach(liveReplay -> {
                                    MallRoomMediaEntity mallRoomMediaEntity = new MallRoomMediaEntity();
                                    mallRoomMediaEntity.setRoomid(mallRoomEntity.getRoomid());
                                    mallRoomMediaEntity.setCreateTime(DateUtils.stringToDate(liveReplay.getCreateTime()));
                                    mallRoomMediaEntity.setExpireTime(DateUtils.stringToDate(liveReplay.getExpireTime()));
                                    mallRoomMediaEntity.setMediaUrl(liveReplay.getMediaUrl());
                                    mallRoomMediaEntities.add(mallRoomMediaEntity);
                                });
                            }
                        } catch (WxErrorException e) {
                            e.printStackTrace();
                        }
                    });
                    roomMediaService.saveBatch(mallRoomMediaEntities);
                    roomGoodsService.saveBatch(mallRoomGoodsEntities);
                }
            }
        } catch (WxErrorException e) {
            throw new BusinessException("获取直播房间列表失败；" + e.getMessage());
        }
        return true;
    }

    @Override
    @SuppressWarnings("uncheck")
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean addgoods(Map<String, Object> params) {
        Integer roomId = Integer.parseInt(params.get("roomId").toString());
        try {
            List<Integer> goodsIds = ObjectUtils.objToList(params.get("goodsIds"), Integer.class);
            List<MallRoomGoodsEntity> mallRoomGoodsEntities = new ArrayList<>();
            for (Integer goodsId : goodsIds) {
                MallRoomAllGoodsEntity roomAllGoods = roomAllGoodsService.getById(goodsId);
                MallRoomGoodsEntity mallRoomGoodsEntity = new MallRoomGoodsEntity();
                mallRoomGoodsEntity.setRoomid(roomId);
                mallRoomGoodsEntity.setName(roomAllGoods.getName());
                mallRoomGoodsEntity.setPrice(roomAllGoods.getPrice());
                mallRoomGoodsEntity.setPrice2(roomAllGoods.getPrice2());
                mallRoomGoodsEntity.setUrl(roomAllGoods.getUrl());
                mallRoomGoodsEntity.setCoverImg(roomAllGoods.getCoverImgUrl());
                mallRoomGoodsEntities.add(mallRoomGoodsEntity);
            }
            roomGoodsService.saveBatch(mallRoomGoodsEntities);
            return maService.getLiveService().addGoodsToRoom(roomId, goodsIds);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("直播间导入商品失败；" + e.getMessage());
        }
    }

    @Override
    @Cacheable
    public String getPushUrl(Integer roomId) {
        try {
            return maService.getLiveService().getPushUrl(roomId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取直播间推流地址失败；" + e.getMessage());
        }
    }

    @Override
    @Cacheable
    public String getSharedCode(Integer roomId, String params) {
        try {
            return maService.getLiveService().getSharedCode(roomId, params).getPosterUrl();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取直播间分享二维码失败；" + e.getMessage());
        }
    }
}
