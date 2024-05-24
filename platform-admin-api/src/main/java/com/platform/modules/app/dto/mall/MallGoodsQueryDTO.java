package com.platform.modules.app.dto.mall;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class MallGoodsQueryDTO {

    private String shopsCategoryId ;

    private List<String> categoryIds;

    private String goodsName ;


}
