package com.platform.cost.temp.rule;

import com.platform.cost.condition.impl.FullAmountCondition;
import com.platform.cost.condition.impl.FullNumCondition;
import com.platform.cost.condition.impl.NoCondition;
import com.platform.cost.constants.ConditionType;
import com.platform.cost.model.acct.AcctActitvtyJoinModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.acct.PriceModel;
import com.platform.cost.model.actitvty.ActitvtyModel;
import com.platform.cost.model.actitvty.ActitvtyRuleMode;
import com.platform.cost.model.actitvty.PromotionTempModel;
import com.platform.cost.model.condition.ConditionMode;

import java.util.List;

/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  模板计算规则接口
 * </pre>
 * @author caozj  cao.zhijun@163.com
 * @date 2024-01-20 下午3:52:36
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public interface IPomitionCalTempRule {


    /**
     *
     * @Title: checkCondition
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 下午3:08:45
     * @Description: 初始化商品信息
     * @return
     * @throws
     */
    public  void   excute(AcctModel model,
                          ActitvtyModel activity,
                          PromotionTempModel tempModel,
                          AcctActitvtyJoinModel actitvtyJoinModel);


    /* *
     * @MethodName: 方法名称  对规则进行排序
     * @Description: //如果存在满减的  满折的时候 需要进行排序 再处理
     *                 例如 规则  满100  减 20
     *                           满200  减 50
     *                           满300  减 90
     * @date 2024-01-20 18:50
     * @param [ruleModes]
     * @return
     */
    public List<ActitvtyRuleMode>  sortTempRule(List<ActitvtyRuleMode> ruleModes);


    /* *
     * @MethodName: 方法名称  处理规则条件
     * @date 2024-01-20 18:50
     * @param [ruleModes]
     * @return
     */
    public ConditionMode handelCodition(AcctModel model,
                                        AcctActitvtyJoinModel actitvtyJoinModel,
                                        PromotionTempModel tempModel,
                                        List<ActitvtyRuleMode> ruleModes);

    /**
     *
     * @Title: checkCondition
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 下午3:08:45
     * @Description: 方法功能描述  规则条件判断
     * @param model
     * @param actitvtyJoinModel
     * @return
     * @throws
     */

    public ConditionMode checkCondition(AcctModel model,
                                        AcctActitvtyJoinModel actitvtyJoinModel,
                                        PromotionTempModel tempModel,
                                        ConditionMode conditionMode,
                                        ActitvtyRuleMode rule);


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
                              ConditionMode conditionMode);

    /**
     *
     * @Title: handelPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 下午3:08:45
     * @Description: 方法功能描述  处理计算价格
     * @param model
     * @param priceVO
     * @param condition
     * @throws
     */
    public void handelPriceAllocation(AcctModel model, PriceModel priceVO, ConditionMode condition) ;



}
