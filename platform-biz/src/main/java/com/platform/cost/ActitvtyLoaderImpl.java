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
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称  加载活动信息
 * @Description: 类功能的描述
 * @date 2024-01-20  20:41
 */
@Component
public class ActitvtyLoaderImpl  implements IActitvtyLoader{

    public static Map<String,ActitvtyModel> map ;

    @Override
    public ActitvtyModel getActitvtyById(String actitvtyId) {
        //
        if(map==null){
            initMap();
        }

        return map.get(actitvtyId);
    }

    /* *
     * @MethodName: 方法名称
     * @Description: //TODO
     * @date 2024-01-20 18:16
     * @param []
     * @return
     */
    public void initMap(){
        map = new HashMap<>();
        ActitvtyModel actitvtyModel = new ActitvtyModel();
        actitvtyModel.setActitvtyId("1");
        actitvtyModel.setActitvtyName("测试活动");
        actitvtyModel.setTempCode("1");

        List list = Lists.newArrayList();
        ActitvtyRuleMode ruleMode = new ActitvtyRuleMode();
        ruleMode.setConditionValue("");
        ruleMode.setActionValue("30");
        list.add(ruleMode);

        actitvtyModel.setActitvtyRuleModeList(list);
        map.put("1",actitvtyModel);
    }
}
