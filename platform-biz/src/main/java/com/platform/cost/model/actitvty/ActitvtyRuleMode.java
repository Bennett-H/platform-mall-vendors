package com.platform.cost.model.actitvty;

import java.io.Serializable;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  15:17
 */
public class ActitvtyRuleMode implements Serializable, Comparable {

    private static final long serialVersionUID = -3299892826624673456L;

    /**
     * 所属营销活动ID
     */
    private Integer actitvtyId;

    /* *
     * @Description: 条件值 例如 条件
     * @date 2024-01-20 16:28
     * @param
     * @return
     */
    private String conditionValue;


    /**
     * 动作值  减价  折扣值
     */
    private String actionValue;



    public Integer getActitvtyId() {
        return actitvtyId;
    }

    public void setActitvtyId(Integer actitvtyId) {
        this.actitvtyId = actitvtyId;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }



}
