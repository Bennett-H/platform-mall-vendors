package com.platform.modules.app.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubmitCartVo {
    private BigDecimal retailPrice;

    private Integer number;
}
