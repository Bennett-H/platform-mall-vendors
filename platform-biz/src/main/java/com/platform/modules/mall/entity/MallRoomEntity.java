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
package com.platform.modules.mall.entity;

import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 直播房间信息实体
 *
 * @author cxd
 * @since 2020-03-30 18:52:09
 */
@Data
@TableName("MALL_ROOM")
public class MallRoomEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 房间 id
     */
    @TableId(type = IdType.INPUT)
    private Integer roomid;
    /**
     * 房间名
     */
    @NotEmpty(message = "房间名不能为空")
    private String name;
    /**
     * 直播间背景图，图片规则：建议像素1080*1920，大小不超过2M
     */
    @NotEmpty(message = "直播间背景图不能为空")
    private String coverImg;
    /**
     * 直播间分享图，图片规则：建议像素800*640，大小不超过1M
     */
    @NotEmpty(message = "直播间分享图不能为空")
    private String shareImg;
    /**
     * 直播计划开始时间，列表按照 start_time 降序排列
     */
    @NotEmpty(message = "直播计划开始时间不能为空")
    private Long startTime;
    /**
     * 直播计划结束时间
     */
    @NotEmpty(message = "直播计划结束时间不能为空")
    private Long endTime;
    /**
     * 主播名
     */
    @NotEmpty(message = "主播昵称不能为空")
    private String anchorName;
    /**
     * 直播状态 101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常, 107: 已过期
     */
    private Integer liveStatus;

    /**
     * 主播副号微信号，如果未实名认证，需要先前往“小程序直播”小程序进行实名验证, 小程序二维码链接：https://res.wx.qq.com/op_res/BbVNeczA1XudfjVqCVoKgfuWe7e3aUhokktRVOqf_F0IqS6kYR--atCpVNUUC3zr
     **/
    @TableField(exist = false)
    private String subAnchorWechat;
    /**
     * 创建者微信号，不传入则此直播间所有成员可见。传入则此房间仅创建者、管理员、超管、直播间主播可见
     **/
    @TableField(exist = false)
    private String createrWechat;
    /**
     * 购物直播频道封面图，填入mediaID（mediaID获取后，三天内有效）；图片mediaID的获取，请参考以下文档： https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html; 购物直播频道封面图，图片规则：建议像素800*800，大小不超过100KB；
     **/
    @NotEmpty(message = "购物直播频道封面图不能为空")
    @TableField(exist = false)
    private String feedsImg;
    /**
     * 是否开启官方收录 【1: 开启，0：关闭】，默认开启收录
     **/
    @TableField(exist = false)
    private Integer isFeedsPublic;
    /**
     * 是否关闭回放 【0：开启，1：关闭】默认关闭回放
     **/
    @TableField(exist = false)
    private Integer closeReplay;
    /**
     * 是否关闭分享 【0：开启，1：关闭】默认开启分享（直播开始后不允许修改）
     **/
    @TableField(exist = false)
    private Integer closeShare;
    /**
     * 是否关闭客服 【0：开启，1：关闭】 默认关闭客服
     **/
    @TableField(exist = false)
    private Integer closeKf;
    /**
     * 主播微信号
     */
    @NotEmpty(message = "主播微信号不能为空")
    @TableField(exist = false)
    private String anchorWechat;
    /**
     * 直播间类型 【1: 推流，0：手机直播】
     */
    @TableField(exist = false)
    private Integer type;
    /**
     * 横屏、竖屏 【1：横屏，0：竖屏】（横屏：视频宽高比为16:9、4:3、1.85:1 ；竖屏：视频宽高比为9:16、2:3）
     */
    @TableField(exist = false)
    private Integer screenType;
    /**
     * 是否关闭点赞 【0：开启，1：关闭】（若关闭，观众端不展示点赞入口，直播开始后不允许开启）
     */
    @NotEmpty(message = "点赞不能为空")
    @TableField(exist = false)
    private Integer closeLike;
    /**
     * 是否关闭货架 【0：开启，1：关闭】（若关闭，观众端不展示商品货架，直播开始后不允许开启）
     */
    @NotEmpty(message = "货架不能为空")
    @TableField(exist = false)
    private Integer closeGoods;
    /**
     * 是否关闭评论 【0：开启，1：关闭】（若关闭，观众端不展示评论入口，直播开始后不允许开启）
     */
    @NotEmpty(message = "评论不能为空")
    @TableField(exist = false)
    private Integer closeComment;
    @TableField(exist = false)
    List<WxMaLiveResult.Goods> goods;
    @TableField(exist = false)
    List<MallRoomGoodsEntity> allGoods;
}
