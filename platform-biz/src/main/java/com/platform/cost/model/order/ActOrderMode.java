package com.platform.cost.model.order;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 计算费用订单信息
 * @Description: 类功能的描述
 * @date 2024-01-20  21:52
 */
public class ActOrderMode {

    //订单ID
    public Long orderId ;

    //订单编码
    public Long orderCode ;

    //订单商品明细
    public List<ActOrderDetailModel> orderDetailModelList ;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public List<ActOrderDetailModel> getOrderDetailModelList() {
        return orderDetailModelList;
    }

    public void setOrderDetailModelList(List<ActOrderDetailModel> orderDetailModelList) {
        this.orderDetailModelList = orderDetailModelList;
    }
}
