package com.platform.modules.mall.dto.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MallSalesCountReqDTO {

    @ApiModelProperty("商品ID")
    private String  shopsId ;

    @ApiModelProperty("统计日期")
    private String  countDate ;
}
