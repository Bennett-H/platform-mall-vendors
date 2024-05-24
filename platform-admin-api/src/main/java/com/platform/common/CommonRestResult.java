package com.platform.common;

import com.platform.common.utils.RestResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonRestResult<T>  implements Serializable {

    @ApiModelProperty("返回编码 ")
    private String code = "0";

    @ApiModelProperty("返回消息 ")
    private String msg;

    @ApiModelProperty("返回对象 ")
    private  T  data;

    public CommonRestResult(){
        code = "0";
        msg = "请求成功";
    }

    public static CommonRestResult success(Object data) {
        CommonRestResult commonRestResult =  new CommonRestResult();
        commonRestResult.setData(data);
        return commonRestResult;
    }


    public static CommonRestResult fail(String code ,String message) {
        CommonRestResult commonRestResult =  new CommonRestResult();
        commonRestResult.setCode(code);
        commonRestResult.setMsg(message);
        return commonRestResult;
    }

    public static CommonRestResult error(String code ,String message) {
        CommonRestResult commonRestResult =  new CommonRestResult();
        commonRestResult.setCode(code);
        commonRestResult.setMsg(message);
        return commonRestResult;
    }
}
