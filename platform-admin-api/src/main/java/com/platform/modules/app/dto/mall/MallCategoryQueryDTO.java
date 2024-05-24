package com.platform.modules.app.dto.mall;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MallCategoryQueryDTO {

    @NotBlank(message = "商户ID")
    private String shopId ;


    private String parentId ;
}
