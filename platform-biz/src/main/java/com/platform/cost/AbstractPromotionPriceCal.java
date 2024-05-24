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
import com.platform.cost.model.acct.*;
import com.platform.cost.temp.ITempActuator;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  17:39
 */
public abstract class AbstractPromotionPriceCal implements IPromotionPirceCal {



    public IActitvtyLoader actitvtyLoader;


    public IPromotionTempLoader tempLoader;


    public ITempActuator tempActuator;


    /**
     * @throws
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述     计算订单商品价格
     */
    @Override
    public void CostOrderPrice(AcctModel acctModel) {

        //加载当前信息参加的所有活动
        List<AcctActitvtyJoinModel> actitvtyList = this.sortOrderActitvty(acctModel);

        //循环计算所有活动信息
        this.calculateActitvty(actitvtyList,acctModel);

        this.handleAcctModel(acctModel);
    }

    /**
     * @throws
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  对订单活动信息进行排序  对当产品减价需要先进行计算
     * 现在没有满减等基准价  不需要进行排序  可以进行顺序计算
     */
    public List<AcctActitvtyJoinModel> sortOrderActitvty(AcctModel acctModel) {

        return acctModel.getAcctActitvtyJoinModels();
    }

    /**
     * @throws
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  逐步计算单个活动费用信息
     */
    public void calculateActitvty(List<AcctActitvtyJoinModel> acctActitvtyJoinModels,
                                        AcctModel acctModel) {

        //循环单个活动参与信息
        acctActitvtyJoinModels.forEach(item->{

            this.calculateSingleActitvty(item,acctModel);

        });

    }


    /**
     * @throws
     * @Title: CostOrderPrice
     * @author caozj  cao.zhijun@163.com
     * @date 2024-01-20 上午10:35:58
     * @Description: 方法功能描述  逐步计算单个活动费用信息
     */
    public void calculateSingleActitvty(AcctActitvtyJoinModel actitvtyJoinModel,AcctModel acctModel){

        //获取活动信息
        String actitvtyId = actitvtyJoinModel.getActitvtyId();

        //加载活动信息
        ActitvtyModel actitvtyModel = actitvtyLoader.getActitvtyById(actitvtyId);

        //获取活动计算模板信息
        PromotionTempModel tempModel = tempLoader.getPromotionTempByCode(actitvtyModel.getTempCode());

        //模板执行器 执行活动
        tempActuator.calTempPrice(actitvtyModel,tempModel,acctModel,actitvtyJoinModel);

    }


    /* *
     * @MethodName: 计算完成  处理所有的费用信息
     * @Description: //TODO
     * @date 2024-01-20 21:02
     * @param []
     * @return
     */
    @Override
    public void handleAcctModel(AcctModel acctModel) {

        //获取活动明细列表
        List<AcctItemModel> list = acctModel.getAcctItemModelList();

        double OriginTotalAmount = 0.0;
        for(AcctItemModel acctItemModel:list){
            OriginTotalAmount +=acctItemModel.getRetailPrice()*acctItemModel.getLineNum();

        }
        acctModel.setOriginTotalAmount(OriginTotalAmount);
        acctModel.setActualAmount(OriginTotalAmount-acctModel.getPomitionAmount());

    }

}
