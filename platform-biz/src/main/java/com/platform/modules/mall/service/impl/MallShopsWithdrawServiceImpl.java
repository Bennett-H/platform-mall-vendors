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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.modules.mall.dao.MallOrderDao;
import com.platform.modules.mall.dao.MallShopsWithdrawDao;
import com.platform.modules.mall.dao.MallShopsWithdrawOrderDao;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallDistOrderService;
import com.platform.modules.mall.service.MallShopsBankCardService;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.mall.service.MallShopsWithdrawService;
import com.platform.modules.sys.service.SysConfigService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 商家提现Service实现类
 *
 * @author cxd
 * @date 2020-05-05 08:56:53
 */
@Service("mallShopsWithdrawService")
public class MallShopsWithdrawServiceImpl extends ServiceImpl<MallShopsWithdrawDao, MallShopsWithdrawEntity> implements MallShopsWithdrawService {

    @Autowired
    private MallUserDao mallUserDao;
    @Autowired
    private MallOrderDao mallOrderDao;
    @Autowired
    private MallShopsWithdrawOrderDao mallShopsWithdrawOrderDao;
    @Autowired
    private MallShopsBankCardService mallShopsBankCardService;
    @Autowired
    private MallShopsService mallShopsService;

    @Autowired
    private MallDistOrderService mallDistOrderService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private WxPayService wxPayService;

    @Override
    public List<MallShopsWithdrawEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.APPLY_TIME");
        params.put("asc", false);
        Page<MallShopsWithdrawEntity> page = new Query<MallShopsWithdrawEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallShopsWithdrawPage(page, params));
    }

    @Override
    public boolean add(MallShopsWithdrawEntity mallShopsWithdraw) {
        return this.save(mallShopsWithdraw);
    }

    @Override
    public boolean update(MallShopsWithdrawEntity mallShopsWithdraw) {
        return this.updateById(mallShopsWithdraw);
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

    /**
     * 确认提现
     *
     * @param userCheckId
     * @param withDrawId
     * @return
     */
    @Override
    @Transactional
    public RestResponse confirmWithdraw(String userCheckId, String withDrawId, String approvalRemark) {
        MallShopsWithdrawEntity dbAccount = baseMapper.getById(withDrawId);
        MallShopsEntity mallShopsEntity = mallShopsService.getById(dbAccount.getShopsId());
        if (dbAccount.getApplyStatus().equals(2)) {
            return RestResponse.error("已确认提现，请勿重复操作");
        }
        if (dbAccount.equals(3)) {
            return RestResponse.error("已拒绝提现");
        }
        // 打款到客户微信账号上
//        EntPayResult payResult = null;
//        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
//        try {
//            MallUserEntity toUser = mallUserDao.selectById(dbAccount.getWithdrawUserId());
//            EntPayRequest entPayRequest = new EntPayRequest();
//            entPayRequest.setAmount(dbAccount.getApplyMoney().multiply(new BigDecimal(100)).intValue()); // todo
//            entPayRequest.setSpbillCreateIp(IpUtils.getLocalHostAdress());
//            entPayRequest.setOpenid(toUser.getOpenId());
//            entPayRequest.setCheckName("NO_CHECK");
//            // 不校验真实姓名
//            if (StringUtils.isNotBlank(toUser.getUserName())) {
//                entPayRequest.setReUserName(toUser.getUserName());
//            } else {
//                entPayRequest.setReUserName(toUser.getNickname());
//            }
//            entPayRequest.setNonceStr(randomStr);
//            entPayRequest.setPartnerTradeNo(dbAccount.getId());
//            entPayRequest.setDescription("商家提现");
//            payResult = wxPayService.getEntPayService().entPay(entPayRequest);
//        } catch (WxPayException e) {
//            return RestResponse.error(e.getCustomErrorMsg());
//        }
//        if (null != payResult && "SUCCESS".equals(payResult.getResultCode()) && "SUCCESS".equals(payResult.getReturnCode())) {
        //
        dbAccount.setApplyStatus(2);
        dbAccount.setApprover(userCheckId);
        dbAccount.setApprovalTime(new Date());
        dbAccount.setApprovalRemark(approvalRemark);
        baseMapper.updateById(dbAccount);

        BigDecimal applyMoney = dbAccount.getApplyMoney();
        if(applyMoney.doubleValue()>=0) {
            //增加金额
            BigDecimal actualMoney = applyMoney.subtract(dbAccount.getDistMoney()).subtract(dbAccount.getPlatformMoney());
            mallShopsEntity.setAmountTotal(mallShopsEntity.getAmountTotal().add(actualMoney));
            mallShopsEntity.setAmountAvailable(mallShopsEntity.getAmountAvailable().add(actualMoney));
        }else{
            mallShopsEntity.setAmountAvailable(mallShopsEntity.getAmountAvailable().subtract(applyMoney.abs()));
            mallShopsEntity.setAmountWithdrawn(mallShopsEntity.getAmountWithdrawn().add(applyMoney.abs()));
        }
        mallShopsService.update(mallShopsEntity);

//        } else {
//            if (null != payResult.getErrCodeDes()) {
//                return RestResponse.error(payResult.getErrCodeDes());
//            } else {
//                return RestResponse.error(payResult.getReturnMsg());
//            }
//        }
        return RestResponse.success();
    }

    @Override
    public RestResponse confirmCash(String[] ids, String auditStatus, String auditDesc) {
        for (String id : ids) {
            MallShopsWithdrawEntity mallShopsWithdrawEntity = baseMapper.selectById(id);
            //log.info(String.valueOf(orderEntity));
            // 订单类型
            Integer applyType = mallShopsWithdrawEntity.getApplyType();
            if (!Constant.ApplyType.TIXIAN.getValue().equals(applyType)) {
                throw new BusinessException("此订单非佣金提现，不能提现！");
            }
            // 审核状态
            if (mallShopsWithdrawEntity.getApplyStatus().equals(4)) {
                throw new BusinessException("此订单已提现，不能重复提现！");
            }
            //提现成功
            mallShopsWithdrawEntity.setApplyStatus(4);
            mallShopsWithdrawEntity.setPaymentNo(auditDesc);
            update(mallShopsWithdrawEntity);
        }
        return RestResponse.success();
    }

    /**
     * 拒绝提现
     *
     * @param userCheckId
     * @param withDrawId
     * @return
     */
    @Override
    @Transactional
    public RestResponse refuseWithdraw(String userCheckId, String withDrawId, String approvalRemark) {
        MallShopsWithdrawEntity dbAccount = baseMapper.getById(withDrawId);
        if (dbAccount.getApplyStatus().equals(2)) {
            return RestResponse.error("已确认，无法回退");
        }
        if (dbAccount.getApplyStatus().equals(3)) {
            return RestResponse.error("请勿重复操作");
        }
        dbAccount.setApprover(userCheckId);
        dbAccount.setApprovalRemark(approvalRemark);
        dbAccount.setApplyStatus(3);
        dbAccount.setApprovalTime(new Date());
        baseMapper.updateById(dbAccount);
        return RestResponse.success();
    }

    @Override
    @Transactional
    public RestResponse withdrawApply(String userId, String shopsId, String withdrawUserId, List<String> orderIds, String shopsBankCardId) {
        BigDecimal applyMoney = new BigDecimal(0);
        BigDecimal distMoney = new BigDecimal(0);
        BigDecimal platformMoney = new BigDecimal(0);
        List<MallOrderEntity> orderEntities = new ArrayList<>();
        if (!orderIds.isEmpty()) {
            orderEntities = mallOrderDao.selectBatchIds(orderIds);
            applyMoney = orderEntities.stream().map(MallOrderEntity::getActualPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        //平台费用
        platformMoney = applyMoney.multiply(BigDecimal.valueOf(Double.parseDouble(sysConfigService.getValue(Constant.COMMISSION_TYPE_PLATFORM, "0.00")))).setScale(2, RoundingMode.HALF_UP);

        //找出分销佣金并相减
        for (MallOrderEntity mallOrderEntity : orderEntities) {
            List<MallDistOrderEntity> mallDistOrderEntityList = mallDistOrderService.getDistOrderList(new QueryWrapper<MallDistOrderEntity>().eq("ORDER_ID", mallOrderEntity.getId()));
            BigDecimal distOrderMoney = mallDistOrderEntityList.stream().map(MallDistOrderEntity::getIncome).reduce(BigDecimal.ZERO, BigDecimal::add);
            distMoney = distMoney.add(distOrderMoney);
        }

        MallShopsWithdrawEntity dbAccount = new MallShopsWithdrawEntity();
        //扣件平台服务费
        dbAccount.setApplyMoney(applyMoney);
        dbAccount.setDistMoney(distMoney);
        dbAccount.setPlatformMoney(platformMoney);
        dbAccount.setUserId(userId);
        dbAccount.setShopsId(shopsId);
        dbAccount.setApplyType(Constant.ApplyType.JIESUAN.getValue());
        dbAccount.setApplyTime(new Date());
        dbAccount.setApplyStatus(1);
        dbAccount.setWithdrawUserId(withdrawUserId);
        //有银行卡才更新
        if (StringUtils.isNotBlank(shopsBankCardId)) {
            MallShopsBankCardEntity mallShopsBankCardEntity = mallShopsBankCardService.getById(shopsBankCardId);
            dbAccount.setEncBankNo(mallShopsBankCardEntity.getCardNumber());
            dbAccount.setEncTrueName(mallShopsBankCardEntity.getCardName());
            dbAccount.setBankCode(mallShopsBankCardEntity.getBankCode());
            dbAccount.setBankPhone(mallShopsBankCardEntity.getPhone());
        }
        baseMapper.insert(dbAccount);
        //
        orderEntities.forEach(item -> {
            MallShopsWithdrawOrderEntity shopsWithdrawOrderEntity = new MallShopsWithdrawOrderEntity();
            shopsWithdrawOrderEntity.setOrderId(item.getId());
            shopsWithdrawOrderEntity.setWithdrawId(dbAccount.getId());
            mallShopsWithdrawOrderDao.insert(shopsWithdrawOrderEntity);
        });
        return RestResponse.success().put(dbAccount);
    }

    @Override
    public RestResponse withdrawCash(String userId, String shopsId, String withdrawUserId,Double applyMoney, String shopsBankCardId) {
        MallShopsWithdrawEntity dbAccount = new MallShopsWithdrawEntity();
        dbAccount.setApplyMoney(new BigDecimal(-1*applyMoney));
        dbAccount.setUserId(userId);
        dbAccount.setShopsId(shopsId);
        dbAccount.setApplyType(Constant.ApplyType.TIXIAN.getValue());
        dbAccount.setApplyTime(new Date());
        dbAccount.setApplyStatus(1);
        dbAccount.setWithdrawUserId(withdrawUserId);
        //有银行卡才更新
        if (StringUtils.isNotBlank(shopsBankCardId)) {
            MallShopsBankCardEntity mallShopsBankCardEntity = mallShopsBankCardService.getById(shopsBankCardId);
            dbAccount.setEncBankNo(mallShopsBankCardEntity.getCardNumber());
            dbAccount.setEncTrueName(mallShopsBankCardEntity.getCardName());
            dbAccount.setBankCode(mallShopsBankCardEntity.getBankCode());
            dbAccount.setBankPhone(mallShopsBankCardEntity.getPhone());
        }
        baseMapper.insert(dbAccount);
        return RestResponse.success().put(dbAccount);
    }

    @Override
    public MallShopsWithdrawEntity selectById(String withdrawId) {
        return baseMapper.getById(withdrawId);
    }

    @Override
    public List<MallOrderEntity> selectCanWithdrawList(String shopsId) {
        return baseMapper.selectCanWithdrawList(shopsId);
    }

    @Override
    public List<MallOrderEntity> selectRelaOrderList(String withdrawId) {
        return baseMapper.selectRelaOrderList(withdrawId);
    }

    @Override
    public void exportMyOrderExcel(Map<String, Object> params, HttpServletResponse response) {
        // 查询未付款的订单
        List<MallShopsWithdrawEntity> list = baseMapper.selectList(new LambdaQueryWrapper<MallShopsWithdrawEntity>()
                .eq(MallShopsWithdrawEntity::getApplyStatus,2)
                //审核通过的
                .eq(MallShopsWithdrawEntity::getApplyType, Constant.ApplyType.TIXIAN.getValue())
                .orderByDesc(MallShopsWithdrawEntity::getApplyTime));
        try {
            // 每个ExcelExportEntity存放Map行数据的key
            List<ExcelExportEntity> keyList = new ArrayList<>();

            // 同一列对应的cell,在从Map里面取值时，会共用同一个key
            // 因此ExcelExportEntity的个数要保持和列数做多的行的rowMap.size()大小一致
            ExcelExportEntity excelExportEntity = new ExcelExportEntity("提现订单号", "id");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("提现店铺编码", "shopsSn");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("提现店铺名", "shopsName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("申请时间", "applyTime");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("申请金额", "applyMoney");
            excelExportEntity.setStatistics(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("审核人", "approver");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("审核时间", "approvalTime");
            excelExportEntity.setWrap(true);
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("审核备注", "approvalRemark");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("收款方银行卡号", "encBankNo");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("收款方开户行", "encTrueName");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("收款方银行绑定电话", "bankPhone");
            keyList.add(excelExportEntity);
            excelExportEntity = new ExcelExportEntity("付款成功编号", "paymentNo");
            keyList.add(excelExportEntity);

            // Map作为每一行的数据容器，List作为行的容器
            List<Map<String, Object>> rowDataList = new ArrayList<>();
            Map<String, Object> rowMap;
            // 填充查询到的数据
            for (MallShopsWithdrawEntity item : list) {
                MallShopsEntity mallShops = mallShopsService.getById(item.getShopsId());
                // 一个Map对应一行数据（如果需要导出多行数据，那么需要多个Map）
                rowMap = new HashMap<>(32);
                rowMap.put("id", item.getId());
                rowMap.put("shopsSn", item.getShopsSn());
                rowMap.put("shopsName", mallShops.getName());
                rowMap.put("applyTime", item.getApplyTime());
                rowMap.put("applyMoney", item.getApplyMoney());
                rowMap.put("approver", item.getApprover());
                rowMap.put("approvalTime", item.getApprovalTime());
                rowMap.put("approvalRemark", item.getApprovalRemark());
                rowMap.put("encBankNo", item.getEncBankNo());
                rowMap.put("encTrueName", item.getEncTrueName());
                rowMap.put("bankPhone", item.getBankCode());
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
