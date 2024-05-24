package com.platform.cost;

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
/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称  计算模板信息加载器
 * @Description: 类功能的描述
 * @date 2024-01-20  16:10
 */
public interface IPromotionTempLoader {


    /* *
     * @MethodName: 方法名称  获取营销模型信息
     * @Description: //TODO
     * @date 2024-01-20 16:12
     * @param [tempCode]
     * @return
     */
    public PromotionTempModel getPromotionTempByCode(String tempCode);
}
