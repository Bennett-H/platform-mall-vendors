package com.platform.cost.action;


import com.platform.cost.model.acct.AcctActitvtyJoinModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.acct.PriceModel;
import com.platform.cost.model.condition.ConditionMode;

/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述 营销动作  优惠计算方式
 *
 * 优惠的计算方式有三种
 * 1 优惠为M元     例如   某商品特价100    优惠为原价 -100元
 * 2 优惠M元       例如   满100  减掉50   则为优惠50元
 * 3 折扣         例如   全场8折
 * </pre>
 * @author caozj  cao.zhijun@163.com
 * @date 2024-01-20 上午9:41:41
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public interface IPromotionAction<T> {

    /* *
     * @MethodName: 执行动作
     * @Description: //TODO
     * @date 2024-01-20 18:22
     * @param [priceModel, actionValue]
     * @return
     */
    public void handle(AcctModel model,
                       AcctActitvtyJoinModel actitvtyJoinModel,
                       ConditionMode conditionMode);



    public PriceModel calPrice(double basicPrice, int value);
    /* *
     * @MethodName: 方法名称 格式化配置的动作数据
     * @Description: //TODO
     * @date 2024-01-20 18:22
     * @param [actionValue]
     * @return
     */
    public T getActionObj(String actionValue);

}
