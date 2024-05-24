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

import cn.binarywang.wx.miniapp.api.WxMaSecCheckService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.Base64Util;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallCommentEntity;
import com.platform.modules.mall.entity.MallCommentPictureEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallCommentPictureService;
import com.platform.modules.mall.service.MallCommentService;
import com.platform.modules.mall.service.MallUserService;
import io.swagger.annotations.*;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: AppCommentController <br>
 */
@Api(tags = "AppCommentController|商品评论管理控制器")
@RestController
@RequestMapping("/app/comment")
public class AppCommentController extends AppBaseController {
    @Autowired
    private MallCommentService commentService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallCommentPictureService commentPictureService;
    @Autowired
    private WxMaService maService;

    /**
     * 发表评论
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     * @throws WxErrorException
     */
    @PostMapping("post")
    @ApiOperation(value = "发表评论", notes = "发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "1"),
                    @ExampleProperty(mediaType = "evalLevel", value = "5"),
                    @ExampleProperty(mediaType = "deliveryLevel", value = "5"),
                    @ExampleProperty(mediaType = "goodsList", value = "json"),
                    @ExampleProperty(mediaType = "type", value = "0")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public RestResponse post(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) throws WxErrorException {
        String orderId = jsonParam.getString("orderId");
        Integer evalLevel = jsonParam.getInteger("evalLevel");
        Integer deliveryLevel = jsonParam.getInteger("deliveryLevel");
        Integer type = jsonParam.getInteger("type");
        List<Map<String, Object>> goodsList = jsonParam.getObject("goodsList", List.class);
        if (getMaAppId() != null) {
            maService.switchover(getMaAppId());
        }
        WxMaSecCheckService maSecCheckService = maService.getSecCheckService();

        for (Map<String, Object> goods : goodsList) {
            List<String> pics = (List<String>) goods.get("pics");
            String goodsId = goods.get("goodsId").toString();
            String goodsSpecifitionNameValue = MapUtils.getString(goods, "goodsSpecifitionNameValue");
            String goodsLevel = goods.getOrDefault("goodsLevel", 5).toString();
            String content = MapUtils.getString(goods, "comment");
            //调用限制 4000 次/分钟，2,000,000 次/天
            maSecCheckService.checkMessage(content);
            MallCommentEntity commentEntity = new MallCommentEntity();
            commentEntity.setGoodsId(goodsId);
            commentEntity.setOrderId(orderId);
            commentEntity.setUserId(loginUser.getId());
            commentEntity.setGoodsSpecifitionNameValue(goodsSpecifitionNameValue);
            commentEntity.setStatus(0);
            commentEntity.setType(type);
            commentEntity.setAddTime(new Date());
            commentEntity.setEvalLevel(evalLevel);
            commentEntity.setDeliveryLevel(deliveryLevel);
            commentEntity.setGoodsLevel(Integer.parseInt(goodsLevel));
            commentEntity.setContent(Base64Util.encode(content));

            boolean flag = commentService.save(commentEntity);
            if (flag && null != pics && pics.size() > 0) {
                int i = 0;
                for (String imgLink : pics) {
                    maSecCheckService.checkImage(imgLink);
                    i++;
                    MallCommentPictureEntity pictureVo = new MallCommentPictureEntity();
                    pictureVo.setCommentId(commentEntity.getId());
                    pictureVo.setPicUrl(imgLink);
                    pictureVo.setSort(i);
                    commentPictureService.save(pictureVo);
                }
            }
        }
        return RestResponse.success();
    }

    /**
     * 评论总数
     *
     * @param type    用户评论的类型;0评论的是商品,1评论的是文章
     * @param from    查看来源, 1 查看所有状态 否则查询审核通过的
     * @param goodsId 商品ID
     * @param orderId 订单ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("count")
    @ApiOperation(value = "评论总数", notes = "评论总数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "用户评论的类型;0评论的是商品,1评论的是文章", allowableValues = "0,1", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "from", value = "查看来源, 1 查看所有状态 否则查询审核通过的", dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品Id", dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单Id", dataType = "string", example = "1")
    })
    public RestResponse count(@RequestParam String type, @RequestParam(defaultValue = "1") Integer from, String goodsId, String orderId) {
        Long goodsCount = commentService.count(new QueryWrapper<MallCommentEntity>().eq(from != 1, "STATUS", 1).eq(StringUtils.isNotBlank(orderId), "ORDER_ID", orderId)
                .eq("TYPE", type).eq(StringUtils.isNotBlank(goodsId), "GOODS_ID", goodsId));

        Map<String, Object> param = new HashMap<>(2);
        param.put("goodsId", goodsId);
        param.put("orderId", orderId);
        param.put("type", type);
        param.put("from", from);
        param.put("status", 1);
        Integer picCount = commentService.queryHasPicTotal(param);

        return RestResponse.success().put("goodsCount", goodsCount).put("picCount", picCount);
    }

    /**
     * 根据商品ID获取评论列表
     *
     * @param type     用户评论的类型;0评论的是商品,1评论的是文章
     * @param from     查看来源, 1 查看所有状态 否则查询审核通过的
     * @param orderId  订单ID
     * @param goodsId  商品ID
     * @param showType 所有评价、有图评价
     * @param current  当前页码
     * @param limit    每页条数
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("list")
    @ApiOperation(value = "评论列表", notes = "根据商品ID获取评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "用户评论的类型;0评论的是商品,1评论的是文章", allowableValues = "0,1", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "from", value = "查看来源, 1 查看所有状态 否则查询审核通过的", dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单Id", dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品Id", dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "showType", value = "所有评价、有图评价", allowableValues = "0,1", example = "0", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse commentList(@RequestParam String type, @RequestParam(defaultValue = "0") Integer from, String orderId, String goodsId, Integer showType,
                                    @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        Page<MallCommentEntity> page = new Page<>(current, limit);
        IPage<MallCommentEntity> data = commentService.page(page, new QueryWrapper<MallCommentEntity>().eq("TYPE", type).eq(StringUtils.isNotBlank(goodsId), "GOODS_ID", goodsId)
                .eq(StringUtils.isNotBlank(orderId), "ORDER_ID", orderId).eq(from != 1, "STATUS", 1)
                .exists(showType == 1, "SELECT 1 FROM MALL_COMMENT_PICTURE WHERE MALL_COMMENT.ID = MALL_COMMENT_PICTURE.COMMENT_ID"));

        List<MallCommentEntity> commentList = data.getRecords();

        for (MallCommentEntity commentItem : commentList) {
            commentItem.setContent(Base64Util.decode(commentItem.getContent()));
            commentItem.setUserInfo(userService.getById(commentItem.getUserId()));

            List<MallCommentPictureEntity> commentPictureEntities = commentPictureService.list(
                    new QueryWrapper<MallCommentPictureEntity>().eq("COMMENT_ID", commentItem.getId()));
            commentItem.setCommentPictureEntityList(commentPictureEntities);
        }

        return RestResponse.success().put("data", data);
    }

    /**
     * 获取商品评价
     *
     * @param goodsId 商品Id
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("goodsScores")
    @ApiOperation(value = "获取商品评价", notes = "获取商品评价")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品Id", required = true, dataType = "string", example = "1"),
    })
    public RestResponse goodsScores(@RequestParam String goodsId) {

        QueryWrapper<MallCommentEntity> qw = new QueryWrapper();
        qw.select("GOODS_LEVEL as goodsLevel,count(id) as levelCount");//查询自定义列
        qw.eq("GOODS_ID", goodsId);
        qw.eq("STATUS", 1);
        qw.groupBy("GOODS_LEVEL");
        List<Map<String, Object>> maps = commentService.listMaps(qw);

        Integer[] scores = new Integer[6];
        Arrays.fill(scores, 0);
        Integer total = 0;
        for (Map<String, Object> commentItem : maps) {
            Integer goodsLevel = MapUtils.getInteger(commentItem, "goodsLevel");
            Integer levelCount = MapUtils.getInteger(commentItem, "levelCount");
            scores[6 - goodsLevel] = levelCount;
            total += levelCount;
        }
        scores[0] = total;
        return RestResponse.success().put("data", scores);
    }
}
