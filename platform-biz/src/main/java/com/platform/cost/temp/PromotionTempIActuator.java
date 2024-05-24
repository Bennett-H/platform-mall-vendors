package com.platform.cost.temp;

import com.platform.cost.action.IPromotionAction;
import com.platform.cost.action.PromotionActionFactory;
import com.platform.cost.condition.ConditionFactory;
import com.platform.cost.condition.IConditionMate;
import com.platform.cost.model.acct.AcctActitvtyJoinModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.acct.PriceModel;
import com.platform.cost.model.actitvty.ActitvtyModel;
import com.platform.cost.model.actitvty.ActitvtyRuleMode;
import com.platform.cost.model.actitvty.PromotionTempModel;
import com.platform.cost.model.condition.ConditionMode;
import com.platform.cost.temp.rule.IPomitionCalTempRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  16:35
 */
@Component
public class PromotionTempIActuator implements ITempActuator{

    private static Logger logger = LoggerFactory.getLogger(PromotionTempIActuator.class);


    //所有计算模板
    public Map<String, IPomitionCalTempRule> promotionTempRuleMap ;

    public PromotionTempIActuator(Map<String, IPomitionCalTempRule> promotionTempRuleMap){
        this.promotionTempRuleMap = promotionTempRuleMap ;
    }

    /* *
     * @MethodName: 方法名称  获取执行类
     * @Description: //TODO
     * @date 2024-01-20 17:37
     * @param [tempCode]
     * @return
     */
    private IPomitionCalTempRule getTempRuleByCode(String tempCode){

        if(tempCode==""){
            tempCode = "CurrencyTempCostRule";
        }
        IPomitionCalTempRule rule = promotionTempRuleMap.get(tempCode);
        return rule ;
    }



    /**
     * @throws
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  计算模板价格
     */
    @Override
    public void calTempPrice(ActitvtyModel actitvtyModel,
                             PromotionTempModel tempModel,
                             AcctModel acctModel,
                             AcctActitvtyJoinModel actitvtyJoinModel) {

        //获取当前活动所有规则信息
        //List<ActitvtyRuleMode> rules =  actitvtyModel.getActitvtyRuleModeList();

        //获取当前模型信息
        String  tempCode = tempModel.getTemplateCode() ;

        //获取当前活动计算模板
        IPomitionCalTempRule tempRule = promotionTempRuleMap.get(tempCode);

        //执行计算规则
        tempRule.excute(acctModel,actitvtyModel,tempModel,actitvtyJoinModel);

    }
}
