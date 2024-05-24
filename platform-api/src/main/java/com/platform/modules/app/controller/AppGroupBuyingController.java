package com.platform.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.app.entity.BuyGoodsVo;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.service.SysConfigService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/group")
@Api(tags = "AppGroupBuyingController|拼团接口")
public class AppGroupBuyingController extends AppBaseController {
    @Autowired
    private MallGroupBuyingRecordService buyingRecordService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallAddressService addressService;
    @Autowired
    private MallGoodsSkuService goodsSkuService;
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private WxSendMsgService sendMsgService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    /**
     * 用户拼团记录
     *
     * @param status    状态：1拼团中 2拼团成功 0拼团失败
     * @param goodsId   商品ID
     * @param userId    用户ID
     * @param leaderId  团长ID
     * @param isLeader  是否团长(开团的人非团购的团长) 1=是 0=否
     * @param payStatus 付款状态 1:未付款 2:付款中 3:已付款 4:退款
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/list")
    @ApiOperation(value = "用户拼团记录", notes = "用户拼团记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态：1拼团中 2拼团成功 0拼团失败", allowableValues = "1,2,0", dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品ID", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "leaderId", value = "团长ID", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "isLeader", value = "是否团长(开团的人非团购的团长) 1=是 0=否", allowableValues = "1,0", dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "payStatus", value = "付款状态 1:未付款 2:付款中 3:已付款 4:退款", allowableValues = "1,2,3,4", dataType = "int", example = "1")
    })
    public RestResponse groupGoodsList(Integer status, String goodsId, String userId, String leaderId, Integer isLeader, Integer payStatus) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("status", status);
        params.put("goodsId", goodsId);
        params.put("payStatus", payStatus);
        params.put("userId", userId);
        params.put("leaderId", leaderId);
        params.put("isLeader", isLeader);
        List<MallGroupBuyingRecordEntity> data = buyingRecordService.queryAll(params);

        for (MallGroupBuyingRecordEntity item : data) {
            //订单的商品
            List<MallOrderGoodsEntity> goodsList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", item.getOrderSn()));
            Integer goodsCount = 0;
            for (MallOrderGoodsEntity orderGoodsEntity : goodsList) {
                goodsCount += orderGoodsEntity.getNumber();
            }
            item.setNumber(goodsCount);
        }
        return RestResponse.success().put("data", data);
    }

    /**
     * 拼团详情
     *
     * @param groupId 拼团ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/detail")
    @ApiOperation(value = "拼团详情", notes = "拼团详情")
    @ApiImplicitParam(paramType = "query", name = "groupId", value = "拼团ID", required = true, dataType = "string")
    public RestResponse groupDetail(@RequestParam String groupId) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("groupId", groupId);
        List<MallGroupBuyingRecordEntity> list = buyingRecordService.queryAll(params);
        if (CollectionUtils.isEmpty(list)) {
            return RestResponse.error("查询有误！");
        }
        MallGroupBuyingRecordEntity data = list.get(0);
        params = new HashMap<>(8);
        // 团长
        if (data.getIsLeader() == 1) {
            params.put("leaderId", data.getId());
            list = buyingRecordService.queryAll(params);
            list.add(data);
        } else {
            // 团员
            params.put("leaderId", data.getLeaderId());
            list = buyingRecordService.queryAll(params);
            list.add(buyingRecordService.getById(data.getLeaderId()));
        }
        return RestResponse.success().put("data", data).put("list", list);
    }

    /**
     * 订单提交前的检验和填写相关订单信息
     *
     * @param loginUser 登录用户
     * @param addressId 地址ID
     * @return RestResponse
     */
    @GetMapping("checkout")
    @ApiOperation(value = "校验订单", notes = "订单提交前的检验和填写相关订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "addressId", value = "addressId", required = true, dataType = "string", example = "1")
    })
    @SuppressWarnings("unchecked")
    public RestResponse groupCheckout(@LoginUser MallUserEntity loginUser, @RequestParam String addressId) {
        Map<String, Object> resultObj = new HashMap<>(16);
        MallAddressEntity addressVo = addressService.getById(addressId);
        if (addressVo == null) {
            //默认收货地址
            addressVo = addressService.getOne(new QueryWrapper<MallAddressEntity>().eq("IS_DEFAULT", 1).eq("USER_ID", loginUser.getId()), false);
            if (addressVo == null) {
                //如果没有默认地址选其中一个地址
                addressVo = addressService.getOne(new QueryWrapper<MallAddressEntity>().eq("USER_ID", loginUser.getId()), false);
            }
        }
        resultObj.put("checkedAddress", addressVo);

        // * 获取要购买的商品和总价
        List<MallCartEntity> checkedGoodsList = new ArrayList<>();

        BuyGoodsVo goodsRedis = (BuyGoodsVo) redisTemplateUtil.get(Constant.MTM_CACHE + "group" + loginUser.getId());
        if (null == goodsRedis) {
            return RestResponse.error("发生错误，请返回重试！");
        }
        MallGoodsSkuEntity productInfo = goodsSkuService.getById(goodsRedis.getSkuId());
        //计算订单的费用
        //商品总价
        BigDecimal goodsTotalPrice = productInfo.getGroupPrice().multiply(new BigDecimal(goodsRedis.getNumber()));
        //判断商品是否可以购买
        MallGoodsEntity goods = goodsService.getById(goodsRedis.getGoodsId());
        if (null == goods || goods.getIsOnSale() == 0) {
            return RestResponse.error("商品已下架！");
        }

        MallCartEntity cartVo = new MallCartEntity();
        cartVo.setGoodsName(goods.getName());
        cartVo.setNumber(goodsRedis.getNumber());
        cartVo.setShopsId(goodsRedis.getShopsId());
        cartVo.setGoodsSn(productInfo.getGoodsSn());
        cartVo.setRetailPrice(productInfo.getGroupPrice());
        cartVo.setGoodsId(goods.getId());
        cartVo.setListPicUrl(goods.getListPicUrl());
        cartVo.setGoodsSpecifitionNameValue(goodsRedis.getSelectedText());
        checkedGoodsList.add(cartVo);
        Integer checkedGoodsCount = goodsRedis.getNumber();

        BigDecimal shippingFee = BigDecimal.valueOf(0);
        //免邮门槛
        String shippingFeeFre = sysConfigService.getValue(Constant.SHIPPING_FEE_FREE, "80");
        if (goodsTotalPrice.compareTo(new BigDecimal(shippingFeeFre)) <= 0) {
            shippingFee = new BigDecimal(sysConfigService.getValue(Constant.SHIPPING_FEE, "12"));
        }
        //订单的总价=商品金额+运费
        BigDecimal orderTotalPrice = goodsTotalPrice.add(shippingFee);

        resultObj.put("shippingFee", shippingFee);
        resultObj.put("checkedGoodsList", checkedGoodsList);
        resultObj.put("goodsTotalPrice", goodsTotalPrice);
        resultObj.put("orderTotalPrice", orderTotalPrice);
        resultObj.put("checkedGoodsCount", checkedGoodsCount);
        return RestResponse.success(resultObj);
    }

    /**
     * 提交拼团订单
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("submitOrder")
    @ApiOperation(value = "提交拼团订单", notes = "提交拼团订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "fromType", value = "用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:头条小程序"),
                    @ExampleProperty(mediaType = "postscript", value = "订单说明"),
                    @ExampleProperty(mediaType = "checkedAddress", value = "AddressVo")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse submitOrderGroup(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {

        String postscript = jsonParam.getString("postscript");
        Integer fromType = jsonParam.getInteger("fromType");

        BuyGoodsVo goodsRedis = (BuyGoodsVo) redisTemplateUtil.get(Constant.MTM_CACHE + "group" + loginUser.getId());
        if (null == goodsRedis) {
            return RestResponse.error("发生错误，请返回重试！");
        }

        MallGoodsSkuEntity productInfo = goodsSkuService.getById(goodsRedis.getSkuId());
        if (null == productInfo || productInfo.getGoodsNumber() < goodsRedis.getNumber() || productInfo.getGoodsNumber() <= 0) {
            throw new BusinessException("商品库存不足");
        }
        productInfo.setGoodsNumber(productInfo.getGoodsNumber() - goodsRedis.getNumber());
        // 库存预警通知
        goodsService.stockWarning(productInfo.getGoodsNumber());
        goodsSkuService.update(productInfo);
        //计算订单的费用
        //商品总价
        BigDecimal goodsTotalPrice = productInfo.getGroupPrice().multiply(new BigDecimal(goodsRedis.getNumber()));
        MallGoodsEntity goods = goodsService.getById(goodsRedis.getGoodsId());
        if (goods.getIsOnSale() != 1) {
            throw new BusinessException(goods.getName() + "已下架！");
        }
        goods.setGoodsNumber(goods.getGoodsNumber() - goodsRedis.getNumber());
        // 库存预警通知
        goodsService.stockWarning(goods.getGoodsNumber());
        if (null == goods.getGoodsNumber() || goods.getGoodsNumber() < goodsRedis.getNumber() || goods.getGoodsNumber() <= 0) {
            throw new BusinessException("商品库存不足");
        }
        goodsService.updateById(goods);

        BigDecimal shippingFee = BigDecimal.valueOf(0);
        //非店铺订单加上运费
        //免邮门槛
        String shippingFeeFre = sysConfigService.getValue(Constant.SHIPPING_FEE_FREE, "80");
        if (goodsTotalPrice.compareTo(new BigDecimal(shippingFeeFre)) <= 0) {
            shippingFee = new BigDecimal(sysConfigService.getValue(Constant.SHIPPING_FEE, "12"));
        }
        String orderSn = StringUtils.transFromType(fromType) + StringUtils.generateOrderNumber();

        //订单价格计算//订单的总价
        BigDecimal orderTotalPrice = goodsTotalPrice.add(shippingFee);
        // 团ID
        String groupId = goodsRedis.getGroupId();

        MallOrderEntity orderInfo = new MallOrderEntity();
        orderInfo.setId(orderSn);
        orderInfo.setOrderSn(orderSn);
        orderInfo.setUserId(loginUser.getId());
        orderInfo.setShopsId(goodsRedis.getShopsId());
        orderInfo.setGoodsId(goods.getId());

        orderInfo.setOrderType(Constant.GROUP_ORDER);
        MallAddressEntity addressVo = jsonParam.getObject("checkedAddress", MallAddressEntity.class);
        orderInfo.setConsignee(addressVo.getUserName());
        orderInfo.setMobile(addressVo.getMobile());
        orderInfo.setCountry(addressVo.getNationalCode());
        orderInfo.setProvince(addressVo.getProvinceName());
        orderInfo.setCity(addressVo.getCityName());
        orderInfo.setDistrict(addressVo.getCountyName());
        orderInfo.setAddress(addressVo.getDetailInfo());
        orderInfo.setPostalCode(addressVo.getPostalCode());
        orderInfo.setFromType(fromType);
        //留言
        orderInfo.setPostscript(postscript);
        orderInfo.setAddTime(new Date());
        orderInfo.setExpireTime(DateUtils.addDateMinutes(new Date(), Integer.parseInt(sysConfigService.getValue(Constant.ORDER_EXPIRE, "30"))));
        orderInfo.setActualPrice(orderTotalPrice);
        orderInfo.setOrderPrice(orderTotalPrice);
        // 待付款
        orderInfo.setOrderStatus(Constant.OrderStatus.DFK.getValue());
        orderInfo.setShippingStatus(Constant.ShippingStatus.WFH.getValue());
        orderInfo.setPayStatus(Constant.PayStatus.WFK.getValue());
        orderInfo.setShippingFee(shippingFee);
        orderInfo.setIntegralMoney(new BigDecimal(0));
        orderInfo.setGroupId(groupId);
        orderInfo.setParentId("拼团订单");
        //插入订单信息和订单商品
        orderService.save(orderInfo);
        if (null == orderInfo.getId()) {
            return RestResponse.error("订单提交失败！");
        }

        List<MallOrderGoodsEntity> orderGoodsData = new ArrayList<>();

        MallOrderGoodsEntity orderGoodsVo = new MallOrderGoodsEntity();
        orderGoodsVo.setOrderId(orderInfo.getId());
        orderGoodsVo.setGoodsId(goodsRedis.getGoodsId());
        orderGoodsVo.setGoodsSn(productInfo.getGoodsSn());
        orderGoodsVo.setSkuId(goodsRedis.getSkuId());
        orderGoodsVo.setGoodsName(goods.getName());
        orderGoodsVo.setListPicUrl(goods.getListPicUrl());
        orderGoodsVo.setMarketPrice(productInfo.getMarketPrice());
        orderGoodsVo.setRetailPrice(productInfo.getGroupPrice());
        orderGoodsVo.setNumber(goodsRedis.getNumber());
        orderGoodsVo.setGoodsSpecifitionNameValue(goodsRedis.getSelectedText());
        orderGoodsService.save(orderGoodsVo);

        orderGoodsData.add(orderGoodsVo);

        orderInfo.setOrderGoodsEntityList(orderGoodsData);

        try {
            if (fromType.equals(Constant.FromType.MA.getValue())) {
                // 微信通知
                sendMsgService.notifyPaySuccess(orderInfo, Constant.TemplateType.XDD.getValue());
            } else if (fromType.equals(Constant.FromType.MP.getValue())) {
                //TODO 公众号下单，发送消息
            } else if (fromType.equals(Constant.FromType.ALI.getValue())) {
                //TODO 支付宝小程序下单，发送消息
            } else if (fromType.equals(Constant.FromType.QQ.getValue())) {
                //TODO QQ小程序下单，发送消息
            } else if (fromType.equals(Constant.FromType.TT.getValue())) {
                //TODO 头条小程序下单，发送消息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestResponse.success().put("data", orderInfo);
    }
}
