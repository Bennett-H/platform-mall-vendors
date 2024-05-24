package com.platform.cost.model.actitvty;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称  促销模板信息
 * @Description: 类功能的描述
 * @date 2024-01-20  16:03
 */
public class PromotionTempModel {

    //模板ID
    public  Long  templateId ;

    //模板编码
    public  String templateCode ;

    //模板类型
    public  String templateType ;

    //模板名称
    public  String templateName ;

    //动作编码
    public String actionCode ;

    //条件编码
    public String conditionCode ;

    //限制规则
    public List limitRuleList;


    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public List getLimitRuleList() {
        return limitRuleList;
    }

    public void setLimitRuleList(List limitRuleList) {
        this.limitRuleList = limitRuleList;
    }
}
