package com.platform.cost.model.condition;

import java.io.Serializable;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 计算费用条件模型
 * @Description: 类功能的描述
 * @date 2024-01-20  19:29
 */
public class ConditionMode implements Serializable {

    private static final long serialVersionUID = 4880439447071912381L;


    //范围类型  全场  商品  品牌  分类
    public String rangeType ;

    //条件标志   true  满足条件  false 不满足条件  只要满足一次就算满足
    public boolean conditionFlag ;

    //满足最近条件的优惠值  可能是折扣  特价  减免 值  由具体规则定义
    public int  discountValue ;

    //满足条件的值
    public String  currConditionValue ;

    //当前满足条件的值
    public String  currFullConditionValue ;

    //满足当前条件动作值
    public String  actionValue ;

    //下个满足的条件
    public String  nextFullConditionValue ;

    //当前条件标志
    public boolean currConditionFlag ;

    /**
     * 满足下一条件 所需的 值 可能是  金额  件数
     * 例如  满100  减20   满200减  50
     * 现订单金额为 80  该值为20  如果当前订单金额为  160  该值为40
     */
    public int  meetNextCondition ;

    /**
     * 满足条件的 折扣值
     * 满足下一条件动作值
     */
    public String  meetNextActionValue ;

    /**
     * 满足最近条件消息
     *例如  满100  减20   满200减  50
     * 现订单金额为 80  该值为20  如果当前订单金额为  160  该值为40
     */
    public String  meetMsg ;


    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public boolean isConditionFlag() {
        return conditionFlag;
    }

    public void setConditionFlag(boolean conditionFlag) {
        this.conditionFlag = conditionFlag;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public int getMeetNextCondition() {
        return meetNextCondition;
    }

    public void setMeetNextCondition(int meetNextCondition) {
        this.meetNextCondition = meetNextCondition;
    }

    public String getMeetMsg() {
        return meetMsg;
    }

    public void setMeetMsg(String meetMsg) {
        this.meetMsg = meetMsg;
    }

    public boolean isCurrConditionFlag() {
        return currConditionFlag;
    }

    public void setCurrConditionFlag(boolean currConditionFlag) {
        this.currConditionFlag = currConditionFlag;
    }

    public String getCurrConditionValue() {
        return currConditionValue;
    }

    public void setCurrConditionValue(String currConditionValue) {
        this.currConditionValue = currConditionValue;
    }

    public String getCurrFullConditionValue() {
        return currFullConditionValue;
    }

    public void setCurrFullConditionValue(String currFullConditionValue) {
        this.currFullConditionValue = currFullConditionValue;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    public String getNextFullConditionValue() {
        return nextFullConditionValue;
    }

    public void setNextFullConditionValue(String nextFullConditionValue) {
        this.nextFullConditionValue = nextFullConditionValue;
    }

    public String getMeetNextActionValue() {
        return meetNextActionValue;
    }

    public void setMeetNextActionValue(String meetNextActionValue) {
        this.meetNextActionValue = meetNextActionValue;
    }
}
