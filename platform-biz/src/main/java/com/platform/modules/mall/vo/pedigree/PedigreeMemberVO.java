package com.platform.modules.mall.vo.pedigree;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PedigreeMemberVO {

    /**
     * 主键
     */
    private String id;
    /**
     * 姓
     */
    private String firstName;
    /**
     * 名
     */
    private String lastName;
    /**
     * 姓名
     */
    private String name;
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
     * 背景图片
     */
    private String backImg;

    /**
     * 父代id
     */
    private String parentId;

    /**
     * 父代姓名
     */
    private String parentName;

    /**
     * 父代头像图片
     */
    private String parentHeadImg;

    /**
     * 是否名人 0：不是 1：是
     */
    private Integer isFamous;

    /**
     * 第几代
     */
    private int layer;

    /**
     * 简介
     */
    private String intro;

    /**
     * 描述
     */
    private String description;

    private List<Brother> brothers;

    @Data
    @Builder
    public static class Brother {
        /**
         * 主键
         */
        private String id;
        /**
         * 姓名
         */
        private String name;
        /**
         * 头像图片
         */
        private String headImg;
    }
}
