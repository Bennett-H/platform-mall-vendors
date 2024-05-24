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
import com.platform.cost.temp.ITempActuator;
import com.platform.cost.temp.rule.IPomitionCalTempRule;import org.springframework.stereotype.Component;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  15:49
 */
@Component
public class PromotionPriceCal extends AbstractPromotionPriceCal{


    public PromotionPriceCal(IActitvtyLoader actitvtyLoader,
                             IPromotionTempLoader tempLoader,
                             ITempActuator tempActuator){
        this.actitvtyLoader = actitvtyLoader ;
        this.tempLoader = tempLoader ;
        this.tempActuator= tempActuator ;
    }



}
