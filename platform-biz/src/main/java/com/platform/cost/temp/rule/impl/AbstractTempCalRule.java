package com.platform.cost.temp.rule.impl;

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

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  14:36
 */
public abstract class AbstractTempCalRule implements IPomitionCalTempRule {

    /**
     * @return
     * @throws
     * @Title: checkCondition
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 下午3:08:45
     * @Description: 计算价格
     */
    @Override
    public void excute(AcctModel model,
                       ActitvtyModel activity,
                       PromotionTempModel tempModel,
                       AcctActitvtyJoinModel actitvtyJoinModel) {

        //获取所有规则 进行排序
        List<ActitvtyRuleMode> ruleModes = this.sortTempRule(activity.getActitvtyRuleModeList());

        //处理所有的计算条件  返回当前满足条件
        ConditionMode conditionMode = this.handelCodition(model,actitvtyJoinModel,tempModel,ruleModes);

        //执行动作
        executeAction( model,actitvtyJoinModel,tempModel,conditionMode);


    }

    /**
     * @return
     * @throws
     * @Title: checkCondition
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 下午3:08:45
     * @Description: 计算价格
     */
    @Override
    public ConditionMode handelCodition(AcctModel model,
                               AcctActitvtyJoinModel actitvtyJoinModel,
                               PromotionTempModel tempModel,
                               List<ActitvtyRuleMode> ruleModes){

        ConditionMode conditionMode = new ConditionMode();

        //循环处理规则  看当前处于那个条件档
        for(ActitvtyRuleMode rule:ruleModes){


            //获取条件值
            String conditonValue = rule.getConditionValue() ;

            //校验当前条件值
            this.checkCondition(model,actitvtyJoinModel,tempModel,conditionMode,rule);

            //判断当前条件是否成功 如果当前条件不满足  直接退出
            if(!conditionMode.isCurrConditionFlag()){

                break;
            }else{

                //当前条件满足 获取当前动作值  存入动作对象
                conditionMode.setActionValue(rule.getActionValue());
            }
        };

        return conditionMode;
    }

    /* *
     * @MethodName: 方法名称 判断当前是否满足条件值  具体类可以重写方法
     * @Description: //TODO
     * @date 2024-01-20 19:03
     * @param [model, actitvtyJoinModel, conditonValue]
     * @return
     */
    @Override
    public ConditionMode checkCondition(AcctModel model,
                                        AcctActitvtyJoinModel actitvtyJoinModel,
                                        PromotionTempModel tempModel,
                                        ConditionMode conditionMode,
                                        ActitvtyRuleMode rule) {

        //获取当前条件判断规则
        IConditionMate conditionMate = ConditionFactory.getCondionMate(tempModel.getConditionCode());

        //校验当前条件
        conditionMate.checkCondition(model,actitvtyJoinModel,conditionMode,rule.getConditionValue());


        conditionMode.setActionValue(rule.getActionValue());

        return conditionMode;
    }

    @Override
    public void handelPriceAllocation(AcctModel model, PriceModel priceVO, ConditionMode condition) {


    }


    /**
     *
     * @Title: checkCondition
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 下午3:08:45
     * @Description: 方法功能描述  执行订单动作
     * @param model
     * @param actitvtyJoinModel
     * @return
     * @throws
     */
    public void executeAction(AcctModel model,
                              AcctActitvtyJoinModel actitvtyJoinModel,
                              PromotionTempModel tempModel,
                              ConditionMode conditionMode){

        //获取当前条件需要执行的动作
        IPromotionAction action = PromotionActionFactory.getNewInstance().getActionByType(tempModel.getActionCode());

        //执行当前的动作
        action.handle(model,actitvtyJoinModel,conditionMode);

    }



    /* *
     * @MethodName: 方法名称  对规则进行排序  暂时不需要处理
     * @Description: //如果存在满减的  满折的时候 需要进行排序 再处理
     *                 例如 规则  满100  减 20
     *                           满200  减 50
     *                           满300  减 90
     * @date 2024-01-20 18:50
     * @param [ruleModes]
     * @return
     */
    @Override
    public List<ActitvtyRuleMode> sortTempRule(List<ActitvtyRuleMode> ruleModes) {


        return ruleModes;
    }

}
