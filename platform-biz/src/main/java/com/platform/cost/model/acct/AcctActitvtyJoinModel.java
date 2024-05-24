package com.platform.cost.model.acct;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 活动参与记录  商品的活动参与记录
 * @Description: 类功能的描述
 * @date 2024-01-20  15:29
 */
public class AcctActitvtyJoinModel {

    //活动ID
    public String actitvtyId;

    //活动名称
    public String actitvtyName;


    public boolean  ifCombination;


    /* *
     * @MethodName: 活动订单行列表
     * @Description: //TODO
     * @date 2024-01-20 15:34
     * @param
     * @return
     */
    public List<Integer> lineNums ;


    public String getActitvtyId() {
        return actitvtyId;
    }

    public void setActitvtyId(String actitvtyId) {
        this.actitvtyId = actitvtyId;
    }

    public String getActitvtyName() {
        return actitvtyName;
    }

    public void setActitvtyName(String actitvtyName) {
        this.actitvtyName = actitvtyName;
    }

    public List<Integer> getLineNums() {
        return lineNums;
    }

    public void setLineNums(List<Integer> lineNums) {
        this.lineNums = lineNums;
    }

    public boolean isIfCombination() {
        return ifCombination;
    }

    public void setIfCombination(boolean ifCombination) {
        this.ifCombination = ifCombination;
    }
}
