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
import com.platform.cost.temp.rule.IPomitionCalTempRule;import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称  活动模板加载
 * @Description: 类功能的描述
 * @date 2024-01-20  16:14
 */
@Component
public class PromotionTemploader implements IPromotionTempLoader{

    public static Map<String,PromotionTempModel> map ;


    @Override
    public PromotionTempModel getPromotionTempByCode(String tempCode) {
        if(map==null){
            this.init();
        }

        return map.get(tempCode);
    }

    public void init() {
        map = new HashMap<>();
        PromotionTempModel tempModel = new PromotionTempModel();
        tempModel.setTemplateId(1l);
        tempModel.setTemplateCode("GoodsCutPriceRuleImpl");
        tempModel.setActionCode("ACTION_TYPE_CUT");
        tempModel.setConditionCode("CONDITION-TYPE-NOT");


        map.put("1",tempModel);
    }
}
