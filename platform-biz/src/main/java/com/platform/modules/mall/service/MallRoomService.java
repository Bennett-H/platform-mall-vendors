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
import com.platform.modules.mall.entity.MallRoomEntity;

import java.util.List;
import java.util.Map;

/**
 * 直播房间信息Service接口
 *
 * @author cxd
 * @since 2020-03-30 18:52:09
 */
public interface MallRoomService extends IService<MallRoomEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallRoomEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询直播房间信息
     *
     * @param params 查询参数
     * @return Page
     */
    Page<MallRoomEntity> queryPage(Map<String, Object> params);

    /**
     * 创建直播间
     * <pre>
     * 调用此接口创建直播间，创建成功后将在直播间列表展示，调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#1
     * http请求方式：POST https://api.weixin.qq.com/wxaapi/broadcast/room/create?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param mallRoom 直播间信息
     * @return .
     */
    boolean add(MallRoomEntity mallRoom);

    /**
     * 编辑直播间
     * <pre>
     * 调用此接口编辑直播间，调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#6
     * http请求方式：POST https://api.weixin.qq.com/wxaapi/broadcast/room/editroom?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param mallRoom 直播间信息
     * @return .
     */
    boolean update(MallRoomEntity mallRoom);

    /**
     * 删除直播间
     * <pre>
     * 调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#5
     * http请求方式：POST https://api.weixin.qq.com/wxaapi/broadcast/room/deleteroom?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param roomid 直播间id
     * @return .
     */
    int delete(Integer roomid);

    /**
     * 删除直播间
     * <pre>
     * 调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#5
     * http请求方式：POST https://api.weixin.qq.com/wxaapi/broadcast/room/deleteroom?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param roomids 直播间ids
     * @return .
     */
    boolean deleteBatch(Integer[] roomids);

    /**
     * 同步直播房间列表
     *
     * @param start 起始拉取房间，start = 0 表示从第 1 个房间开始拉取
     * @param limit 每次拉取的个数上限，不要设置过大，建议 100 以内
     * @return boolean
     */
    boolean getLiveInfo(Integer start, Integer limit);

    /**
     * 直播间导入商品
     * <p>
     * 调用接口往指定直播间导入已入库的商品
     * 调用频率
     * 调用额度：10000次/一天
     * <p>
     * http请求方式：POST https://api.weixin.qq.com/wxaapi/broadcast/room/addgoods?access_token=ACCESS_TOKEN
     * <pre>
     * @param params roomId 房间ID  goodsIds 数组列表，可传入多个，里面填写 商品 ID
     * @return 导入商品是否成功
     */
    boolean addgoods(Map<String, Object> params);

    /**
     * 获取直播间推流地址
     * <pre>
     * 调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#7
     * http请求方式：GET https://api.weixin.qq.com/wxaapi/broadcast/room/getpushurl?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param roomId 直播间id
     * @return .
     */
    String getPushUrl(Integer roomId);

    /**
     * 获取直播间分享二维码
     * <pre>
     * 调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#8
     * http请求方式：GET https://api.weixin.qq.com/wxaapi/broadcast/room/getsharedcode?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param roomId 直播间id
     * @return .
     */
    String getSharedCode(Integer roomId, String params);
}
