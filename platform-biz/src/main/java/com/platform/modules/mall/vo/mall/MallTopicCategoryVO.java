package com.platform.modules.mall.vo.mall;

import com.platform.modules.mall.entity.MallTopicCategoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MallTopicCategoryVO extends MallTopicCategoryEntity {

    /**
     * 大类型-字典名称
     */
    private String typeName;

    /**
     * 大类型-字典值
     */
    private String typeValue;
}
