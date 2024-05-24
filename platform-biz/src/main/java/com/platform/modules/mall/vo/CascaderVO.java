package com.platform.modules.mall.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 级联VO
 */
public class CascaderVO {
    private String value;//代码值
    private String label;//名称
    private String pid;//父节点ID
    private String level;//父节点ID
    private List<CascaderVO> children = new ArrayList<>(); //子节点集合
    private Object data;//数据

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<CascaderVO> getChildren() {
        return children;
    }

    public void setChildren(List<CascaderVO> children) {
        this.children = children;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
