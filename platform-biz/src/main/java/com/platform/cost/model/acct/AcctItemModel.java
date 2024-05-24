package com.platform.cost.model.acct;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 费用模型明细
 * @Description: 费用模型明细
 * @date 2024-01-20  20:42
 */
public class  AcctItemModel {

    //商品行号
    public Integer lineNum;

    //订单ID
    public Long orderId ;

    //订单明细ID
    public Long orderDetailId ;

    //skuID
    public Long skuId ;

    //sku数量
    public int  skuNum ;

    //SKU名称
    public String skuName ;

    //零售单价
    public double retailPrice ;

    //优惠价
    public  double discountPrice;

    //最终价格
    public  double finalPrice ;


    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

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

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(int skuNum) {
        this.skuNum = skuNum;
    }
}
