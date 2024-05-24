package com.platform.modules.app.dto.mall;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MallOrderQueryDTO {

    //订单状态 0:订单创建成功等待付款 100:订单超时已过期 101:订单已取消 102:订单已删除 201:订单已付款,等待发货 300:订单已发货 301:用户确认收货 400:申请退款 401:没有发货，退款　402:已收货，退款退货
    private String  orderStaus ;


    private Integer orderType;
}
