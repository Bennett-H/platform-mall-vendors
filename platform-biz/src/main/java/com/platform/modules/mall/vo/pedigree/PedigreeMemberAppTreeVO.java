package com.platform.modules.mall.vo.pedigree;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PedigreeMemberAppTreeVO {

    /**
     * 主键
     */
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 是否名人 0：不是 1：是
     */
    private Integer isFamous;
    /**
     * 头像图片
     */
    private String headImg;
    /**
     * 出生日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date birthDate;
    /**
     * 离世日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date deathDate;

    /**
     * 父代id
     */
    private String parentId;

    /**
     * 第几代
     */
    private int layer;

    /**
     * 子孙
     */
    private List<PedigreeMemberAppTreeVO> children;
}
