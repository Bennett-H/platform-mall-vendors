package com.platform.cost.model.order;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称  订单明细商品  参与活动信息
 * @Description: 类功能的描述
 * @date 2024-01-20  22:10
 */
public class ActOrdDetailsActitvtyMode {

    //订单ID
    public Long  orderId ;

    //订单明细
    public Long  orderDetailId ;


    public Integer lineNum ;

    //商品活动明细
    public Long  actitvtyId ;

    //商品活动分组
    public String groupId ;

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

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public Long getActitvtyId() {
        return actitvtyId;
    }

    public void setActitvtyId(Long actitvtyId) {
        this.actitvtyId = actitvtyId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
