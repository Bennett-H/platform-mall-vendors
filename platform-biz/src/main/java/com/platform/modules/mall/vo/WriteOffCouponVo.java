package com.platform.modules.mall.vo;


import lombok.Data;

@Data
public class WriteOffCouponVo {

    //优惠券ID
    private String couponId ;

    //商品ID
    private String shopId ;

    //用户ID
    private String userId ;

    //核销的订单ID
    private String orderId ;
}
