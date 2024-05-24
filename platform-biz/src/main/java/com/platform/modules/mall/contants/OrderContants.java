package com.platform.modules.mall.contants;

public class OrderContants {


    //订单状态 0:订单创建成功等待付款 100:订单超时已过期 101:订单已取消 102:订单已删除 201:订单已付款,等待发货 300:订单已发货 301:用户确认收货 400:申请退款 401:没有发货，退款 402:已收货，退款退货
    public static  String   ORDER_TO_PAY = "0";  //待付款

    public static  String   ORDER_PAY_TIMEOUT = "100";  //付款超时

    public static  String   ORDER_CANCEL = "101";  //订单取消

    public static  String   ORDER_DELETE = "102";  //订单删除

    public static  String   ORDER_TO_SEDN_GOODS= "201";  //待付款

    public static  String   ORDER_SEND_GOODS = "300";  //订单等等发货  待收货

    public static  String   ORDER_DELIVERY_GOODS = "301";  //订单收货

    public static  String   ORDER_REFUND_AUDIT = "400";  //退款申请

    public static  String   ORDER_REFUND = "401";  //退款

    public static  String   ORDER_REFUND_RETURN_GOODS = "402";  //退款退货

}
