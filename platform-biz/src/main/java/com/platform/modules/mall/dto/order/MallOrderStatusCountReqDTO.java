package com.platform.modules.mall.dto.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MallOrderStatusCountReqDTO {

    //订单状态
    @ApiModelProperty("订单状态")
    private String orderStatus ;


    //商户ID
    @ApiModelProperty("商户ID")
    private String  shopsId;
}
