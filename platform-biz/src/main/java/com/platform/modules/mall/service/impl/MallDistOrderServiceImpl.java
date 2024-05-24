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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallDistOrderDao;
import com.platform.modules.mall.dao.MallOrderDao;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 分销订单Service实现类
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Slf4j
@Service("mallDistOrderService")
public class MallDistOrderServiceImpl extends ServiceImpl<MallDistOrderDao, MallDistOrderEntity> implements MallDistOrderService {

    @Autowired
    private MallDistService mallDistService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallDistPromoService mallDistPromoService;
    @Autowired
    private MallUserService mallUserService;
    @Autowired
    private MallOrderGoodsService mallOrderGoodsService;
    @Autowired
    private MallOrderDao mallOrderDao;

    @Override
    public List<MallDistOrderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.INCOME_TIME");
        params.put("asc", false);
        Page<MallDistOrderEntity> page = new Query<MallDistOrderEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistOrderPage(page, params));
    }

    @Override
    public boolean add(MallDistOrderEntity mallDistOrder) {
        return this.save(mallDistOrder);
    }

    @Override
    public boolean update(MallDistOrderEntity mallDistOrder) {
        return this.updateById(mallDistOrder);
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
    public void confirmAudit(String[] ids, String auditStatus, String auditDesc) throws WxPayException {
        for (String id : ids) {
            final MallDistOrderEntity orderEntity = baseMapper.selectById(id);
            log.info(String.valueOf(orderEntity));
            // 订单类型
            Integer orderType = orderEntity.getType();
            if (!Constant.DistOrderType.WITHDRAW.getValue().equals(orderType)) {
                throw new BusinessException("此订单非佣金提现，不能审核！");
            }
            // 审核状态
            if (!orderEntity.getAuditStatus().equals(Constant.AuditStatus.AUDITING.getValue())) {
                throw new BusinessException("此订单已审核，不能重复审核！");
            }
            // 当前时间
            Date now = new Date();
            // 更新审核状态
            orderEntity.setAuditStatus(Constant.AuditStatus.valueOf(auditStatus).getValue());
            orderEntity.setAuditDesc(auditDesc);
            orderEntity.setIncomeTime(now);
            update(orderEntity);

            String userId = orderEntity.getUserId();
            String orderSn = orderEntity.getOrderId();
            BigDecimal income = orderEntity.getIncome().abs();
            Integer withdrawType = orderEntity.getWithdrawType();
            boolean isPayBank = withdrawType.equals(Constant.WithdrawType.PAY_BANK.getValue());
            if (Constant.AuditStatus.valueOf(auditStatus).equals(Constant.AuditStatus.AUDIT_PASSED)) {
                // 企业付款
//                log.info("企业付款到余额/银行卡");
//                String encBankNo = orderEntity.getEncBankNo();
//                String encTrueName = orderEntity.getEncTrueName();
//                String bankCode = orderEntity.getBankCode();
//                if (isPayBank) {
//                    mallDistService.payBank(income, orderSn, encBankNo, encTrueName, bankCode);
//                } else {
//                    mallDistService.entPay(userId, income, orderSn);
//                }
            } else {
                // 审核不通过，返回用户佣金
                log.info("审核不通过");
                BigDecimal handlingFee = new BigDecimal("0.00");
                if (isPayBank) {
                    // 计算利息
                    handlingFee = mallDistService.calculateTheHandlingFee(income);
                }
                // 可提现佣金-=(本金+手续费)，已提取的佣金-=本金
                mallDistService.updateAmount(userId, income.add(handlingFee).negate(), income.negate());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmCash(String[] ids, String auditStatus, String auditDesc) throws WxPayException {
        for (String id : ids) {
            final MallDistOrderEntity orderEntity = baseMapper.selectById(id);
            log.info(String.valueOf(orderEntity));
            // 订单类型
            Integer orderType = orderEntity.getType();
            if (!Constant.DistOrderType.WITHDRAW.getValue().equals(orderType)) {
                throw new BusinessException("此订单非佣金提现，不能提现！");
            }
            // 审核状态
            if (orderEntity.getAuditStatus().equals(Constant.AuditStatus.AUDIT_CASHED.getValue())) {
                throw new BusinessException("此订单已提现，不能重复提现！");
            }
            orderEntity.setAuditStatus(Constant.AuditStatus.AUDIT_CASHED.getValue());
            orderEntity.setPaymentNo(auditDesc);
            update(orderEntity);
        }
    }

    @Override
    public MallDistOrderEntity queryById(String id) {
        return baseMapper.queryById(id);
    }

    @Override
    public BigDecimal getTotalWithdraw(String userId, Timestamp startTime, Timestamp endTime) {
        return baseMapper.getTotalWithdraw(userId, startTime, endTime, Constant.DistOrderType.WITHDRAW.getValue());
    }

    @Override
    public IPage<MallDistOrderDetailEntity> distOrderPage(Page<MallDistOrderDetailEntity> page, QueryWrapper<MallDistOrderDetailEntity> queryWrapper) {
        return baseMapper.distOrderPage(page, queryWrapper);
    }

    @Override
    public BigDecimal getIncomeDetails(String userId, Timestamp startTime, Timestamp endTime) {
        List<Integer> orderType = new ArrayList<>();
        orderType.add(Constant.DistOrderType.PROXY.getValue());
        orderType.add(Constant.DistOrderType.SALE.getValue());
        List<Integer> orderStatus = new ArrayList<>();
        orderStatus.add(Constant.OrderStatus.QRSH.getValue());
        return baseMapper.getIncomeDetails(userId, startTime, endTime, orderType, orderStatus);
    }

    @Override
    public IPage<IncomeDetailsEntity> getIncomeDetailsPage(Page<IncomeDetailsEntity> page, QueryWrapper<IncomeDetailsEntity> queryWrapper) {
        return baseMapper.getIncomeDetailsPage(page, queryWrapper);
    }

    @Override
    public Long getOrderCount(String userId) {
        return baseMapper.selectCount(new QueryWrapper<MallDistOrderEntity>().eq("USER_ID", userId).isNotNull("COMMISSION_TYPE"));
    }

    @Override
    public Long getOrderCount(String userId, Timestamp startTime, Timestamp endTime) {
        return baseMapper.selectCount(new QueryWrapper<MallDistOrderEntity>().eq("USER_ID", userId).isNotNull("COMMISSION_TYPE")
                .between("INCOME_TIME", startTime, endTime));
    }

    /**
     * 支付时调用创建分销/推广提成订单
     */
    @Override
    public void addDistOrder(String userId, String orderId) {
        if (userId == null || orderId == null) {
            return;
        }

        // 根据订单号获取订单实付价、总价
        MallOrderEntity orderEntity = mallOrderDao.selectById(orderId);
        BigDecimal actualPrice = orderEntity.getActualPrice();
        BigDecimal orderPrice = orderEntity.getOrderPrice();

        // 根据订单号查找【推广追踪表】区分分销订单与推广订单
        List<MallDistPromoEntity> promoList = mallDistPromoService.getPromoListByOrderId(orderId);
        // 根据订单号查找包含的商品
        List<MallOrderGoodsEntity> goodsList = mallOrderGoodsService.getGoodsList(orderId);
        log.info("处理推广订单,orderId ：{}", orderId);
        if (promoList.size() != 0) {
            if (goodsList.size() == promoList.size()) {
                // 该订单的商品均为推广订单
                addDistPromoOrder(userId, promoList, goodsList, actualPrice, orderPrice);
                //return;
            } else {
                // 部分为推广订单，通过addDistPromoOrder方法生成推广订单并剔除推广商品
                goodsList = addDistPromoOrder(userId, promoList, goodsList, actualPrice, orderPrice);
            }
        }
        log.info("处理分销订单,orderId ：{}", orderId);
        // 处理分销订单
        for (MallOrderGoodsEntity goods : mallOrderGoodsService.getGoodsList(orderId)) {
            BigDecimal goodsActualPrice = calcActualPrice(goods.getRetailPrice(), goods.getNumber(), actualPrice, orderPrice);
            addDistProxyOrder(userId, orderId, goods.getGoodsId(), goods.getSkuId(), goodsActualPrice);
        }
    }

    @Override
    public List<MallDistOrderEntity> getDistOrderList(QueryWrapper<MallDistOrderEntity> qw) {
        return baseMapper.selectList(qw);
    }

    private List<MallOrderGoodsEntity> addDistPromoOrder(String userId, List<MallDistPromoEntity> promoList, List<MallOrderGoodsEntity> goodsList, BigDecimal actualPrice, BigDecimal orderPrice) {
        promoList.forEach(promo -> {
            for (MallOrderGoodsEntity goods : goodsList) {
                String goodsId = goods.getGoodsId();
                String skuId = goods.getSkuId();
                String orderId = goods.getOrderId();
                String promoGoodsId = promo.getGoodsId();
                String promoSkuId = promo.getSkuId();
                if (goodsId.equals(promoGoodsId)
                        && ((skuId == null && promo == null) || (skuId != null && promo != null && skuId.equals(promoSkuId)))) {
                    // 一级推广提成
                    MallDistEntity fMallDist = mallDistService.getByUserId(promo.getUserId());
                    if (fMallDist == null) {
                        return; // 本次循环无分销商记录，开启下一次循环
                    }
                    BigDecimal goodsActualPrice = calcActualPrice(goods.getRetailPrice(), goods.getNumber(), actualPrice, orderPrice);
                    addDistOrder(fMallDist.getId(), userId, orderId, goodsId, skuId, goodsActualPrice, Constant.CommissionType.PROMO_1);
                    Integer sDistId = fMallDist.getSuperiorId();
                    if (sDistId != null) {
                        // 二级推广提成
                        addDistOrder(sDistId, userId, orderId, goodsId, skuId, goodsActualPrice, Constant.CommissionType.PROMO_2);
                    }
                    goodsList.remove(goods);//剔除推广订单，剩余均可通过分销订单处理
                    return;
                }
            }
        });
        return goodsList;
    }

    /**
     * 商品实付价格 = (商品零售价 * 商品个数 / 订单总价) * 订单实付价
     *
     * @param retailPrice 商品零售价
     * @param number      商品个数
     * @param actualPrice 订单实付价
     * @param orderPrice  订单总价
     * @return
     */
    private BigDecimal calcActualPrice(BigDecimal retailPrice, Integer number, BigDecimal actualPrice, BigDecimal orderPrice) {

        BigDecimal d = retailPrice.multiply(BigDecimal.valueOf(number)).multiply(actualPrice).divide(orderPrice, 2, BigDecimal.ROUND_HALF_UP);
        // 保留小数点后两位并四舍五入
        DecimalFormat df = new DecimalFormat("0.00");
        return new BigDecimal(df.format(d));
    }

    // 创建代理提成订单
    private void addDistProxyOrder(String userId, String orderId, String goodsId, String skuId, BigDecimal actualPrice) {
        MallDistEntity mallDist = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", userId).isNotNull("SUPERIOR_ID"));
        //用户的一级分销商
        Integer fDistId = null;
        if (mallDist == null) {
            log.info("不是分销商或者为一级分销商，找客户当前绑定的分销用户id,orderId ：{}", orderId);
            // 不是分销商或者为一级分销商
            MallUserEntity mallUser = mallUserService.getById(userId);
            mallDist = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", mallUser.getReferUserId()));
            if (mallDist == null) {
                log.info("不是分销商或者为一级分销商，不生成分销订单,orderId ：{}", orderId);
                return;
            }
            //用户的一级分销商
            fDistId = mallDist.getId();
        } else {
            //用户的一级分销商
            fDistId = mallDist.getSuperiorId();
        }
        log.info("生成一级分销订单,orderId ：{},userId: {} fDistId: {} ", orderId, userId, fDistId);
        addDistOrder(fDistId, userId, orderId, goodsId, skuId, actualPrice, Constant.CommissionType.DIST_1);
        //用户的二级分销商
        MallDistEntity fMallDist = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("ID", fDistId));
        Integer sDistId = fMallDist.getSuperiorId();
        if (sDistId != null) {
            // 用户拥有二级分销商
            log.info("生成二级分销订单,orderId ：{},userId: {} sDistId: {} ", orderId, userId, sDistId);
            addDistOrder(sDistId, userId, orderId, goodsId, skuId, actualPrice, Constant.CommissionType.DIST_2);
        }
    }

    /**
     * @param fDistId        提成者分销id（确定为分销商）
     * @param buyerId        购买者id
     * @param orderId        订单id
     * @param goodsId        商品id
     * @param skuId          商品sku
     * @param actualPrice    实际付款金额
     * @param commissionType 提成类型
     */
    private void addDistOrder(Integer fDistId, String buyerId, String orderId, String goodsId, String skuId, BigDecimal actualPrice, Constant.CommissionType commissionType) {
        String userId = mallDistService.getById(fDistId).getUserId();
        // 分销订单类型
        Constant.DistOrderType type;
        // 提成类型
        String sCommission;
        switch (commissionType) {
            case DIST_1:
                type = Constant.DistOrderType.PROXY;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_DIST_1, "0.00");
                break;
            case DIST_2:
                type = Constant.DistOrderType.PROXY;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_DIST_2, "0.00");
                break;
            case PROMO_1:
                type = Constant.DistOrderType.SALE;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_PROMO_1, "0.00");
                break;
            case PROMO_2:
                type = Constant.DistOrderType.SALE;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_PROMO_2, "0.00");
                break;
            default:
                throw new BusinessException("找不到提成类型");
        }
        BigDecimal commission = new BigDecimal(sCommission);
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal income = new BigDecimal(df.format(actualPrice.multiply(commission)));

        MallDistOrderEntity mallDistOrderEntity = new MallDistOrderEntity();
        mallDistOrderEntity.setUserId(userId);
        mallDistOrderEntity.setBuyerId(buyerId);
        mallDistOrderEntity.setOrderId(orderId);
        mallDistOrderEntity.setType(type.getValue());
        mallDistOrderEntity.setIncome(income);
        mallDistOrderEntity.setIncomeTime(new Date());
        mallDistOrderEntity.setCommissionType(commissionType.getValue());
        mallDistOrderEntity.setCommission(commission);
        // 提成订单不用审核
        mallDistOrderEntity.setAuditStatus(Constant.AuditStatus.AUDIT_PASSED.getValue());
        mallDistOrderEntity.setGoodsId(goodsId);
        mallDistOrderEntity.setSkuId(skuId);
        // 保存分销订单
        this.save(mallDistOrderEntity);
    }


    @Override
    public void exportMyOrderExcel(Map<String, Object> params, HttpServletResponse response) {
        // 查询未付款的订单
        List<MallDistOrderEntity> list = baseMapper.selectList(new LambdaQueryWrapper<MallDistOrderEntity>()
                .isNull(MallDistOrderEntity::getPaymentNo)
                .eq(MallDistOrderEntity::getAuditStatus, Constant.AuditStatus.AUDIT_PASSED.getValue())
                .eq(MallDistOrderEntity::getType, Constant.DistOrderType.WITHDRAW.getValue())
                .orderByDesc(MallDistOrderEntity::getIncomeTime));
        try {
            // 每个ExcelExportEntity存放Map行数据的key
            List<ExcelExportEntity> keyList = new ArrayList<>();

            // 同一列对应的cell,在从Map里面取值时，会共用同一个key
            // 因此ExcelExportEntity的个数要保持和列数做多的行的rowMap.size()大小一致
            ExcelExportEntity excelExportEntity = new ExcelExportEntity("分销订单号", "id");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("提现用户名", "userName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("购买者用户名", "buyerName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("购买者订单编号", "orderId");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("结算收益", "income");
            excelExportEntity.setStatistics(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("结算时间", "incomeTime");
            excelExportEntity.setWrap(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("提成比例", "commission");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("银行卡号", "encBankNo");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("用户名", "encTrueName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("开户行", "bankCode");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("电话", "phone");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("付款成功编号", "paymentNo");
            keyList.add(excelExportEntity);

            // Map作为每一行的数据容器，List作为行的容器
            List<Map<String, Object>> rowDataList = new ArrayList<>();
            Map<String, Object> rowMap;
            // 填充查询到的数据
            for (MallDistOrderEntity item : list) {
                MallUserEntity mallUser = mallUserService.getById(item.getUserId());
                MallUserEntity mallBuyer = mallUserService.getById(item.getBuyerId());
                // 一个Map对应一行数据（如果需要导出多行数据，那么需要多个Map）
                rowMap = new HashMap<>(32);
                rowMap.put("id", item.getId());
                rowMap.put("userName", mallUser.getUserName());
                rowMap.put("buyerName", mallUser.getUserName());
                rowMap.put("orderId", item.getOrderId());
                rowMap.put("income", item.getIncome());
                rowMap.put("incomeTime", item.getIncomeTime());
                rowMap.put("commission", item.getCommission());
                rowMap.put("encBankNo", item.getEncBankNo());
                rowMap.put("encTrueName", item.getEncTrueName());
                rowMap.put("bankCode", item.getBankCode());
                rowMap.put("phone", item.getPhone());
                rowMap.put("paymentNo", item.getPaymentNo());
                rowDataList.add(rowMap);
            }

            // 导出时间
            String exportDate = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
            // excel总体设置
            ExportParams exportParams = new ExportParams("导出时间" + exportDate, "未结算提现订单");
            exportParams.setAutoSize(true);
            exportParams.setHeaderColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
            exportParams.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());

            // 生成workbook 并导出
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, keyList, rowDataList);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + exportDate + ".xlsx");
            response.addHeader("Content-Length", "" + barray.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(barray, response.getOutputStream());
        } catch (Exception e) {
            throw new BusinessException("导出订单失败，" + e.getMessage());
        }
    }
}
