package com.platform.modules.mall.vo.pedigree;

import com.platform.modules.mall.entity.PedigreeMemberEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PedigreeMemberSaveVO extends PedigreeMemberEntity {

    /**
     * 简介
     */
    private String intro;

    /**
     * 描述
     */
    private String description;

    /**
     * 父代id
     */
    private String parentId;

}
