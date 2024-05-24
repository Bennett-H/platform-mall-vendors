package com.platform.cost.temp;


import com.platform.cost.model.acct.AcctActitvtyJoinModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.actitvty.ActitvtyModel;
import com.platform.cost.model.actitvty.PromotionTempModel;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  16:54
 */
public interface ITempActuator {


    /**
     * @throws
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  计算模板价格
     */
    public void calTempPrice(ActitvtyModel actitvtyModel,
                             PromotionTempModel model,
                             AcctModel acctModel,
                             AcctActitvtyJoinModel actitvtyJoinModel);



}
