package com.platform.modules.mall.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveGoodsStockReqDTO {

    @ApiModelProperty("SKU id")
    private String id;
    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private String goodsId;


    @ApiModelProperty("商品数量")
    private Integer goodsNumber;
}
