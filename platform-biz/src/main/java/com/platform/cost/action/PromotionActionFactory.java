package com.platform.cost.action;

import com.platform.cost.action.AbstractAction;
import com.platform.cost.action.impl.*;
import com.platform.cost.constants.ActionType;
import com.platform.cost.model.acct.*;
import com.platform.cost.model.condition.ConditionMode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  00:42
 */
public class PromotionActionFactory {

    private static PromotionActionFactory factory ;

    private PromotionActionFactory(){

    }

    public static Map<String, IPromotionAction> actionMap = null;

    private  void initActionRule(){
        actionMap = new HashMap<>();
        actionMap.put(ActionType.ACTION_TYPE_ADD_BUY,new AddPriceBuyAction());
        actionMap.put(ActionType.ACTION_TYPE_CUT,new CutPriceAction());
        actionMap.put(ActionType.ACTION_TYPE_CUT_FOR,new CutPriceForAction());
        actionMap.put(ActionType.ACTION_TYPE_DISCOUNT,new DicountAction());
        actionMap.put(ActionType.ACTION_TYPE_SCORE_DEDUCT,new ScoreDeductAction());

    }
    public  static  PromotionActionFactory getNewInstance(){

        if(factory==null){
            factory = new PromotionActionFactory();
        }
        return factory ;
    }


    public  IPromotionAction getActionByType(String actionType){

        if(actionMap==null){
            initActionRule();
        }

        return actionMap.get(actionType) ;
    }
}
