package com.platform.cost.model.actitvty;

import java.util.List;
import java.util.Map;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 活动产品模型
 * @Description: 活动模型
 * @date 2024-01-20  19:45
 */
public class ActitvtyModel {

    //活动ID
    public String actitvtyId;

    //活动名称
    public String actitvtyName;

    //渠道列表
    public String channelIds;

    //门店列表
    public String shopIds;

    //商品范围类型   1 为单商品  2 为组合商品
    public String rangeType ;

    //商品列表
    public List skuIds ;

    //是否组合商品
    public boolean  ifCombination;

    //活动商品组合
    public Map skuCombination ;

    //模板编码
    public String tempCode;

    //模板类型
    public String tempType ;


    public List<ActitvtyRuleMode> actitvtyRuleModeList ;


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

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public String getShopIds() {
        return shopIds;
    }

    public void setShopIds(String shopIds) {
        this.shopIds = shopIds;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public List getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List skuIds) {
        this.skuIds = skuIds;
    }

    public Map getSkuCombination() {
        return skuCombination;
    }

    public void setSkuCombination(Map skuCombination) {
        this.skuCombination = skuCombination;
    }

    public String getTempCode() {
        return tempCode;
    }

    public void setTempCode(String tempCode) {
        this.tempCode = tempCode;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public boolean isIfCombination() {
        return ifCombination;
    }

    public void setIfCombination(boolean ifCombination) {
        this.ifCombination = ifCombination;
    }


    public List<ActitvtyRuleMode> getActitvtyRuleModeList() {
        return actitvtyRuleModeList;
    }

    public void setActitvtyRuleModeList(List<ActitvtyRuleMode> actitvtyRuleModeList) {
        this.actitvtyRuleModeList = actitvtyRuleModeList;
    }
}
