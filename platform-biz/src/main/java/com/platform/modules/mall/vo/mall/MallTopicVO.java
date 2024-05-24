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
package com.platform.modules.mall.vo.mall;

import com.platform.modules.mall.entity.MallTopicEntity;
import lombok.Data;

/**
 * 文章实体
 *
 * @author cxd
 * @since 2020-03-08 10:25:25
 */
@Data
public class MallTopicVO extends MallTopicEntity {

    /**
     * 大分类字典值
     */
    private String typeValue;
}
