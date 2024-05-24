package com.platform.modules.app.dto.mall;


import lombok.Data;

@Data
public class OrderSendGoodsReqDTO {

    private String  orderId ;

    private String  fastMailCompany;

    private String  fastMailNumber;

    private String  imageUrl ;
}
