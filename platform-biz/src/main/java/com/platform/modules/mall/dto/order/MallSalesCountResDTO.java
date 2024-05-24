package com.platform.modules.mall.dto.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MallSalesCountResDTO {


    //门店ID
    private String  shopsId ;

    //总销售额
    @ApiModelProperty("总销售额")
    private String  totalSalesAmount = "0";

    //总订单数
    @ApiModelProperty("总订单数")
    private String totalOrderCount = "0";

    //总收益
    @ApiModelProperty("总收益")
    private String totalIncome = "0";

    //代办事项
    @ApiModelProperty("代办事项")
    private String totalTodo = "0";


}
