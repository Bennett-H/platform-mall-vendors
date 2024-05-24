/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.sys.controller;

import com.platform.common.utils.Constant;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.service.MallShopsService;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysRoleOrgService;
import com.platform.utils.SessionContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Controller公共组件
 *
 * @author 李鹏军
 */
@Slf4j
public abstract class AbstractController {
    protected Logger logger = log;

    @Autowired
    private SysRoleOrgService sysRoleOrgService;

    @Autowired
    private MallShopsService mallShopsService;





    /**
     * 门店数据权限构造
     */
    protected void getShopsScope(Map<String, Object> params) {
        params.put("shopsId", SessionContextUtil.getShopsId());
    }

    /**
     * 获取门店数据
     */
    protected String getShopsId() {

        return SessionContextUtil.getShopsId();
    }
}
