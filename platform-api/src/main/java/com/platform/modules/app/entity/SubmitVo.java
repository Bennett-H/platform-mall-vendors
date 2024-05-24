package com.platform.modules.app.entity;

import lombok.Data;

import java.util.List;

@Data
public class SubmitVo {
    private String id;
    private String postscript;
    List<SubmitCartVo> cartList;
}
