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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品-拼团记录表实体
 *
 * @author cxd
 * @since 2021-11-14 22:52:41
 */
@Data
@TableName("MALL_GROUP_BUYING_RECORD")
public class MallGroupBuyingRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商品规格
     */
    private String goodsDetail;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 团员昵称
     */
    private String nickname;
    /**
     * 团员头像
     */
    private String headImgUrl;
    /**
     * 是否团长(开团的人非团购的团长) 1=是 0=否
     */
    private Integer isLeader;
    /**
     * pid 如果是团长 则=0
     */
    private String leaderId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 到期时间(最迟成团时间，开团时间24小时内)
     */
    private Date expireTime;
    /**
     * 参加人数
     */
    private Integer joinNumber = 0;
    /**
     * 拼团的价格
     */
    private BigDecimal price;
    /**
     * 状态 1拼团中 2拼团成功 0拼团失败
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 拼团人数(阶梯团取最大阶梯人数)，最大99999
     */
    @TableField(exist = false)
    private Integer groupNumber;
    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String leaderNickname;
    /**
     * 团长头像
     */
    @TableField(exist = false)
    private String leaderHeadImgUrl;
    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String goodsName;
    /**
     * 商品图片
     */
    @TableField(exist = false)
    private String listPicUrl;
    /**
     * 订单总价
     */
    @TableField(exist = false)
    private BigDecimal orderPrice;
    /**
     * 商品数量
     */
    @TableField(exist = false)
    private Integer number;
    /**
     * 销量
     */
    @TableField(exist = false)
    private Integer sales;
    /**
     * 付款状态 1:未付款 2:付款中 3:已付款 4:退款
     */
    @TableField(exist = false)
    private Integer payStatus;
}
