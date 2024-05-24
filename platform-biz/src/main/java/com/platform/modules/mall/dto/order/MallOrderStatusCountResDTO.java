package com.platform.modules.mall.dto.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MallOrderStatusCountResDTO {

    //商户ID
    private String  shopsId;

    //待发货
    @ApiModelProperty("待发货")
    private String toSendCount = "0" ;

    //待收货
    @ApiModelProperty("待收货")
    private String toDeliveryCount = "0";

    //待退货
    @ApiModelProperty("待退货")
    private String returnCount = "0";
}
