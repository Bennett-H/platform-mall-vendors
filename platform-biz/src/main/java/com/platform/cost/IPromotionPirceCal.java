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

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  17:43
 */
public interface IPromotionPirceCal {

    /**
     *
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述     计算订单价格
     * @throws
     */
    public void CostOrderPrice(AcctModel acctModel );



    /**
     *
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  对订单活动信息进行排序  对当产品减价需要先进行计算
     *               现在没有满减等基准价  不需要进行排序  可以进行顺序计算
     * @throws
     */
    public List<AcctActitvtyJoinModel> sortOrderActitvty(AcctModel acctModel);



    /**
     *
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  循环计算活动信息
     * @throws
     */
    public void calculateActitvty(List<AcctActitvtyJoinModel> acctActitvtyJoinModels,AcctModel acctModel);



    /**
     *
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  循环计算单个活动信息
     * @throws
     */
    public void calculateSingleActitvty(AcctActitvtyJoinModel actitvtyJoinModel,AcctModel acctModel);



    /* *
     * @MethodName: 方法处理所有订单费用明细数据
     * @Description: //TODO
     * @date 2024-01-20 21:03
     * @param [acctModel]
     * @return
     */
    public void handleAcctModel(AcctModel acctModel);


}
