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

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.contants.OrderContants;
import com.platform.modules.mall.dao.MallOrderDao;
import com.platform.modules.mall.dao.MallShippingDao;
import com.platform.modules.mall.dto.order.MallOrderStatusCountReqDTO;
import com.platform.modules.mall.dto.order.MallOrderStatusCountResDTO;
import com.platform.modules.mall.dto.order.MallSalesCountReqDTO;
import com.platform.modules.mall.dto.order.MallSalesCountResDTO;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallOrderGoodsEntity;
import com.platform.modules.mall.entity.MallShippingEntity;
import com.platform.modules.mall.entity.MallShopsWithdrawEntity;
import com.platform.modules.mall.service.*;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * 订单表Service实现类
 *
 * @author cxd
 * @since 2019-07-05 19:29:18
 */
@Service("mallOrderService")
public class MallOrderServiceImpl extends ServiceImpl<MallOrderDao, MallOrderEntity> implements MallOrderService {
    @Autowired
    private MallShippingDao shippingDao;
    @Autowired
    private WxSendMsgService sendMsgService;
    @Autowired
    private MallDistAmountScheduledService mallDistAmountScheduledService;
    @Autowired
    private MallShopsWithdrawService mallShopsWithdrawService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;

    @Override
    public List<MallOrderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page<MallOrderEntity> queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallOrderEntity> page = new Query<MallOrderEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallOrderPage(page, params));
    }

    @Override
    public boolean add(MallOrderEntity mallOrder) {
        return this.save(mallOrder);
    }

    @Override
    public boolean update(MallOrderEntity mallOrder) {
        return this.updateById(mallOrder);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendGoods(MallOrderEntity orderEntity) {
        MallOrderEntity order = baseMapper.selectById(orderEntity.getId());
        //付款状态
        Integer payStatus = order.getPayStatus();
        if (!Constant.PayStatus.YFK.getValue().equals(payStatus)) {
            throw new BusinessException("此订单未付款！");
        }

        MallShippingEntity shippingEntity = shippingDao.selectById(orderEntity.getShippingId());
        if (null != shippingEntity) {
            order.setShippingName(shippingEntity.getName());
            order.setShippingCode(shippingEntity.getCode());
        }
        order.setShippingId(orderEntity.getShippingId());
        order.setShippingNo(orderEntity.getShippingNo());
        order.setPostalCode(orderEntity.getPostalCode());
        //订单已发货
        order.setConfirmTime(new Date());
        order.setOrderStatus(Constant.OrderStatus.YFH.getValue());
        //已发货
        order.setShippingStatus(Constant.ShippingStatus.YFH.getValue());
        update(order);

        //发货通知
        try {
            if (orderEntity.getFromType().equals(Constant.FromType.MA.getValue())) {
                sendMsgService.notifyPaySuccess(order, Constant.TemplateType.DDPS.getValue());
            } else if (orderEntity.getFromType().equals(Constant.FromType.MP.getValue())) {
                //TODO 公众号发货通知
            } else if (orderEntity.getFromType().equals(Constant.FromType.ALI.getValue())) {
                //TODO 支付宝小程序发货通知
            } else if (orderEntity.getFromType().equals(Constant.FromType.QQ.getValue())) {
                //TODO QQ小程序发货通知
            } else if (orderEntity.getFromType().equals(Constant.FromType.TT.getValue())) {
                //TODO 头条小程序发货通知
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceive(String id) {
        MallOrderEntity orderEntity = baseMapper.selectById(id);
        //发货状态
        Integer shippingStatus = orderEntity.getShippingStatus();
        //付款状态
        Integer payStatus = orderEntity.getPayStatus();
        if (!Constant.PayStatus.YFK.getValue().equals(payStatus)) {
            throw new BusinessException("此订单未付款，不能确认收货！");
        }
        if (!Constant.ShippingStatus.YFH.getValue().equals(shippingStatus)) {
            throw new BusinessException("此订单未发货或正在退货，不能确认收货！");
        }
        orderEntity.setShippingStatus(Constant.ShippingStatus.YSH.getValue());
        orderEntity.setOrderStatus(Constant.OrderStatus.QRSH.getValue());
        //设置确认收货时间
        orderEntity.setConfirmReceiveTime(new Date());
        update(orderEntity);

        // 添加分销定时到账记录
        if (orderEntity.getOrderType() != 4) {
            mallDistAmountScheduledService.addAmountScheduled(orderEntity.getId());
        }
    }

    @Override
    public Map<String, Object> queryUserCountMap(Map<String, Object> params) {
        return baseMapper.queryUserCountMap(params);
    }

    public Map<String, Object> shopsOrderAllStatusCount(Map<String, Object> params){
        return baseMapper.shopsOrderAllStatusCount(params);
    }

    @Override
    public MallOrderEntity queryById(String orderId) {

        return baseMapper.queryById(orderId);
    }

    @Override
    public List<Map<String, Object>> shopsGoodsSalesCount(Map<String, Object> params) {
        return baseMapper.shopsGoodsSalesCount(params);
    }

    @Override
    public List<Map<String, Object>> allShopsGoodsSalesCount(Map<String, Object> params) {
        return baseMapper.allShopsGoodsSalesCount(params);
    }

    @Override
    public void exportOrderExcel(Map<String, Object> params, HttpServletResponse response) {
        // 查询到的订单
        List<MallOrderEntity> list = baseMapper.queryAll(params);
        try {
            // 每个ExcelExportEntity存放Map行数据的key
            List<ExcelExportEntity> keyList = new ArrayList<>();

            // 同一列对应的cell,在从Map里面取值时，会共用同一个key
            // 因此ExcelExportEntity的个数要保持和列数做多的行的rowMap.size()大小一致
            ExcelExportEntity excelExportEntity = new ExcelExportEntity("会员昵称", "userName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("订单编号", "orderSn");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("订单类型", "orderType");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("付款状态", "payStatus");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("订单金额(元)", "orderPrice");
            excelExportEntity.setStatistics(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("支付金额(元)", "actualPrice");
            excelExportEntity.setStatistics(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("全部商品名称", "goodsNameList");
            excelExportEntity.setWrap(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("商品种类数", "goodsCategoryCount");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("订单状态", "orderStatus");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("发货状态", "shippingStatus");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("收货人", "consignee");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("手机号", "mobile");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("详细地址", "address");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("下单时间", "addTime");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("付款时间", "payTime");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("快递公司", "shippingName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("快递单号", "shippingNo");
            keyList.add(excelExportEntity);

            // Map作为每一行的数据容器，List作为行的容器
            List<Map<String, Object>> rowDataList = new ArrayList<>();
            Map<String, Object> rowMap;
            List<MallOrderGoodsEntity> orderGoodsEntityList;
            // 填充查询到的数据
            for (MallOrderEntity item : list) {
                // 循环查询大数据量下会有问题
                orderGoodsEntityList = orderGoodsService.list(new QueryWrapper<MallOrderGoodsEntity>().eq("ORDER_ID", item.getId()));
                StringBuilder goodsNameList = new StringBuilder();
                orderGoodsEntityList.forEach(og -> {
                    goodsNameList.append(og.getGoodsName()).append(StringUtils.isBlank(og.getGoodsSpecifitionNameValue()) ? "" : "（" + og.getGoodsSpecifitionNameValue() + "）").append("（").append(og.getNumber()).append("件）\r\n");
                });

                // 一个Map对应一行数据（如果需要导出多行数据，那么需要多个Map）
                rowMap = new HashMap<>(32);
                rowMap.put("userName", item.getUserName());
                rowMap.put("orderSn", item.getOrderSn());
                rowMap.put("orderType", item.getOrderTypeText());
                rowMap.put("payStatus", item.getPayStatusText());
                rowMap.put("orderPrice", item.getOrderPrice());
                rowMap.put("actualPrice", item.getActualPrice());
                rowMap.put("goodsNameList", goodsNameList);
                rowMap.put("goodsCategoryCount", orderGoodsEntityList.size());
                rowMap.put("orderStatus", item.getOrderStatusText());
                rowMap.put("shippingStatus", item.getShippingStatusText());
                rowMap.put("consignee", item.getConsignee());
                rowMap.put("mobile", item.getMobile());
                rowMap.put("address", item.getAddress());
                rowMap.put("addTime", DateUtils.format(item.getAddTime(), DateUtils.DATE_TIME_PATTERN));
                rowMap.put("payTime", DateUtils.format(item.getPayTime(), DateUtils.DATE_TIME_PATTERN));
                rowMap.put("shippingName", item.getShippingName());
                rowMap.put("shippingNo", item.getShippingNo());
                rowDataList.add(rowMap);
            }

            // 查询条件
            StringBuilder sb = new StringBuilder();
            for (String obj : params.keySet()) {
                if (!"token".equalsIgnoreCase(obj) && !StringUtils.isNullOrEmpty(params.get(obj))) {
                    sb.append(obj).append(" = ").append(params.get(obj)).append(" ");
                }
            }
            // 导出时间
            String exportDate = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
            // excel总体设置
            ExportParams exportParams = new ExportParams("导出时间" + exportDate + "；查询条件：" + sb.toString(), "订单统计");
            exportParams.setAutoSize(true);
            exportParams.setHeaderColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
            exportParams.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());

            // 生成workbook 并导出
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, keyList, rowDataList);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + exportDate + ".xls");
            response.addHeader("Content-Length", "" + barray.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(barray, response.getOutputStream());
        } catch (Exception e) {
            throw new BusinessException("导出订单失败，" + e.getMessage());
        }
    }


    /**
     *
     * @param mallOrderStatusCountReqDTO
     * @return
     */
    @Override
    public MallOrderStatusCountResDTO getOrderStatusCount(MallOrderStatusCountReqDTO mallOrderStatusCountReqDTO) {
        Map params = new HashMap();
        params.put("shopsId",mallOrderStatusCountReqDTO.getShopsId());
        List<Map<String,Object>> result = this.getBaseMapper().shopsOrderStatusCount(params);
        MallOrderStatusCountResDTO mallOrderStatusCountResDTO = new MallOrderStatusCountResDTO();
        mallOrderStatusCountResDTO.setShopsId(mallOrderStatusCountReqDTO.getShopsId());
        if(result!=null){
            result.forEach(item->{
                //待发货 已付款
                if(OrderContants.ORDER_TO_SEDN_GOODS.equals(item.get("ORDER_STATUS").toString())){
                    mallOrderStatusCountResDTO.setToSendCount(item.get("ORDER_COUNT").toString());
                }
                //待收货
                if(OrderContants.ORDER_SEND_GOODS.equals(item.get("ORDER_STATUS").toString())){
                    mallOrderStatusCountResDTO.setToSendCount(item.get("ORDER_COUNT").toString());
                }
                //待退货申请
                if(OrderContants.ORDER_REFUND.equals(item.get("ORDER_STATUS").toString())){
                    mallOrderStatusCountResDTO.setToSendCount(item.get("ORDER_COUNT").toString());
                }
            });
        }
        return mallOrderStatusCountResDTO;
    }

    @Override
    public MallSalesCountResDTO getMallOrderSaleCount(MallSalesCountReqDTO mallSalesCountReqDTO) {

        Map params = new HashMap();
        params.put("shopsId",mallSalesCountReqDTO.getShopsId());
        List<Map<String,Object>> result  = this.getBaseMapper().allShopsGoodsSalesCount(params);


        MallSalesCountResDTO mallSalesCountResDTO = new MallSalesCountResDTO();
        mallSalesCountResDTO.setShopsId(mallSalesCountReqDTO.getShopsId());
        //mallSalesCountResDTO.setTotalSalesAmount();
        return mallSalesCountResDTO;
    }
}
