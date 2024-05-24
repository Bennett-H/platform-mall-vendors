package com.platform.modules.app.dto;

import com.platform.modules.app.entity.FullUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel(description= "商户登录对象")
public class MpUserLoginDTO implements Serializable {

    //用户名
    @ApiModelProperty(name = "用户名")
    private String userName;

    //密码
    @ApiModelProperty(name = "密码")
    private String password;

    //openId
    @ApiModelProperty(name = "code")
    private String code ;

    //昵称
    @ApiModelProperty(name = "昵称")
    private String nickname;

    //头
    @ApiModelProperty(name = "头像")
    private String headImgUrl;

    @ApiModelProperty(name = "微信用户信息")
    private FullUserInfo fullUserInfo;

}
