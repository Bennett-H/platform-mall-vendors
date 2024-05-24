package com.platform.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.Constant;
import com.platform.config.RedisTemplateUtil;
import com.platform.common.utils.RestResponse;
import com.platform.modules.app.entity.BuyGoodsVo;
import com.platform.modules.mall.entity.MallUserEntity;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lipengjun
 */
@RestController
@RequestMapping("/app/buy")
@Api(tags = "AppBuyController|商品购买")
public class AppBuyController extends AppBaseController {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    /**
     * 直接购买商品操作添加商品，然后进行订单提交前的检验
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/add")
    @ApiOperation(value = "商品添加", notes = "直接购买商品操作添加商品，然后进行订单提交前的检验")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "skuId", value = "1"),
                    @ExampleProperty(mediaType = "shopsId", value = "1"),
                    @ExampleProperty(mediaType = "goodsId", value = "230000"),
                    @ExampleProperty(mediaType = "number", value = "1"),
                    @ExampleProperty(mediaType = "selectedText", value = "颜色：红色")
            }), required = true, dataType = "string")
    })
    public Object addBuy(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String skuId = jsonParam.getString("skuId");
        String shopsId = jsonParam.getString("shopsId");
        String goodsId = jsonParam.getString("goodsId");
        Integer number = jsonParam.getInteger("number");
        String selectedText = jsonParam.getString("selectedText");

        BuyGoodsVo goodsVo = new BuyGoodsVo();
        goodsVo.setSkuId(skuId);
        goodsVo.setGoodsId(goodsId);
        goodsVo.setNumber(number);
        goodsVo.setShopsId(shopsId);
        goodsVo.setSelectedText(selectedText);
        redisTemplateUtil.set(Constant.MTM_CACHE + "goods" + loginUser.getId(), goodsVo);
        return RestResponse.success("添加成功");
    }

    /**
     * 参与拼团商品操作添加，然后进行订单提交前的检验
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/addGroup")
    @ApiOperation(value = "拼团商品添加", notes = "参与拼团商品操作添加，然后进行订单提交前的检验")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "skuId", value = "1"),
                    @ExampleProperty(mediaType = "goodsId", value = "230000"),
                    @ExampleProperty(mediaType = "shopsId", value = "1"),
                    @ExampleProperty(mediaType = "number", value = "1"),
                    @ExampleProperty(mediaType = "selectedText", value = "颜色：红色"),
                    @ExampleProperty(mediaType = "leaderId", value = "1"),
                    @ExampleProperty(mediaType = "groupId", value = "1")
            }), required = true, dataType = "string")
    })
    public Object addGroup(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String skuId = jsonParam.getString("skuId");
        String goodsId = jsonParam.getString("goodsId");
        Integer number = jsonParam.getInteger("number");
        String selectedText = jsonParam.getString("selectedText");
        String leaderId = jsonParam.getString("leaderId");
        String groupId = jsonParam.getString("groupId");
        String shopsId = jsonParam.getString("shopsId");

        BuyGoodsVo goodsVo = new BuyGoodsVo();
        goodsVo.setSkuId(skuId);
        goodsVo.setGoodsId(goodsId);
        goodsVo.setNumber(number);
        goodsVo.setSelectedText(selectedText);
        goodsVo.setLeaderId(leaderId);
        goodsVo.setGroupId(groupId);
        goodsVo.setShopsId(shopsId);
        redisTemplateUtil.set(Constant.MTM_CACHE + "group" + loginUser.getId(), goodsVo);
        return RestResponse.success("添加成功");
    }
}
