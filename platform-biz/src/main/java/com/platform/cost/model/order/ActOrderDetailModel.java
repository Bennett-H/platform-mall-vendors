package com.platform.cost.model.order;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 算费订单明细信息
 * @Description: 类功能的描述
 * @date 2024-01-20  21:53
 */
public class ActOrderDetailModel {

    //订单ID
    public Long orderId ;

    //订单明细ID
    public Long orderDetailId ;

    //订单行号
    public Integer lineNumber;

    //skuID
    public Long skuId ;

    //SKU名称
    public String skuName ;

    //商品参加的活动明细
    public List<ActOrdDetailsActitvtyMode> detailActityList ;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
