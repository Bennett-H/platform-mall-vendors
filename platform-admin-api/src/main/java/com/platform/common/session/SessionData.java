package com.platform.common.session;

import lombok.Data;

/**
 *
 */

@Data
public class SessionData   {

    //用户ID
    private String userId;

    //用户名称
    private String userName;

    //用户昵称
    private String nickName;

    //用户昵称
    private String shopsId;


    private String accessToken ;


    private String wechatUserId;


    private String wechatOpenId;


}
