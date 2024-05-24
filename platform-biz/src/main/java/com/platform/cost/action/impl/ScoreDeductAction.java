package com.platform.cost.action.impl;


import com.platform.cost.action.AbstractAction;
import com.platform.cost.model.acct.*;
import com.platform.cost.model.condition.ConditionMode;

import java.util.List;

/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  积分抵扣
 * </pre>
 *
 * 优惠计算规则
 * 2000积分抵扣  20元
 * 4000积分抵扣  40元
 * @author caozj cao.zhijun@163.com
 * @date 2024-01-20 上午11:36:59
 * @version 1.00.00
 *
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class ScoreDeductAction extends AbstractAction {


    @Override
    public void handle(AcctModel model,
                       AcctActitvtyJoinModel actitvtyJoinModel,
                       ConditionMode conditionMode) {
        //格式化当前动作值
        Integer value = getActionObj(conditionMode.getActionValue());

        //减价分成两种情况  单商品减  组合商品减
        if(actitvtyJoinModel.isIfCombination()){
            //组合商品计算
            this.combinedSkuCutPrice( model,  actitvtyJoinModel,  conditionMode);

        }else{
            //单商品减
            this.signSkuCutPrice( model,  actitvtyJoinModel,  conditionMode);
        }

    }

    @Override
    public PriceModel calPrice(double basicPrice, int value) {



        return null;
    }



    @Override
    public Integer getActionObj(String actionValue) {


        return null;
    }

    /* *
     * @MethodName: 方法名称  组合商品减价
     * @Description: //TODO
     * @date 2024-01-20 14:20
     * @param [model, actitvtyJoinModel, conditionMode]
     * @return
     */
    public AcctActitvtyInst combinedSkuCutPrice(AcctModel model, AcctActitvtyJoinModel actitvtyJoinModel, ConditionMode conditionMode) {

        List<Integer> lineNums = actitvtyJoinModel.getLineNums();

        AcctActitvtyInst inst = new AcctActitvtyInst();
        inst.setActitvtyId(actitvtyJoinModel.getActitvtyId());
        inst.setActitvtyName(actitvtyJoinModel.getActitvtyName());

        //获取所有参加的商品
        List<AcctItemModel> acctItemModels = this.getAcctDetailByLineNums(model, lineNums);

        double totalAmount = 0.0;
        //计算总价 后面考虑分组  假如  2个  A+B  情况
        for(AcctItemModel acctItemModel:acctItemModels ){
            totalAmount = totalAmount +(acctItemModel.getRetailPrice()*acctItemModel.getSkuNum());
        }

        //计算优惠价格 现在先当成 商品里面就是一个分组  没有多余的商品  信任前端传的数据
        PriceModel priceModel = this.calPrice(totalAmount,
                this.getActionObj(conditionMode.getActionValue()));

        //总优惠价格
        double totalDiscontAmount = priceModel.getDiscountPrice();

        //计算商品逐个的分摊金额
        for(AcctItemModel acctItemModel:acctItemModels ){

            double discountTotalPrice = priceModel.getDiscountPrice();

            //单个商品分摊金额
            PriceModel allocationPrice = amountAllocation(totalDiscontAmount,acctItemModel.getRetailPrice());

            //构造活动明细数据
            AcctActitvtyInst.AcctActitvtyDetailInst detailInst = this.createActitvtyDetailInst(acctItemModel, allocationPrice);

            //新增优惠明细数据
            inst.addActitvtyDetailInst(detailInst);

        }

        return inst;
    }


    /* *
     * @MethodName: 单商品减
     * @Description: //TODO
     * @date 2024-01-20 01:13
     * @param [actitvtyJoinModel]
     * @return
     */
    public AcctActitvtyInst signSkuCutPrice(AcctModel model, AcctActitvtyJoinModel actitvtyJoinModel, ConditionMode conditionMode) {

        List<Integer> lineNums = actitvtyJoinModel.getLineNums();

        AcctActitvtyInst inst = new AcctActitvtyInst();
        inst.setActitvtyId(actitvtyJoinModel.getActitvtyId());
        inst.setActitvtyName(actitvtyJoinModel.getActitvtyName());

        List<AcctItemModel> acctItemModels = this.getAcctDetailByLineNums(model, lineNums);

        //循环创建商品减价记录
        acctItemModels.forEach(acctItemModel -> {

            //计算单商品价格
            PriceModel priceModel = this.calPrice(acctItemModel.getRetailPrice(),
                    this.getActionObj(conditionMode.getActionValue()));

            //构造活动明细数据
            AcctActitvtyInst.AcctActitvtyDetailInst detailInst = this.createActitvtyDetailInst(acctItemModel, priceModel);

            //新增优惠明细数据
            inst.addActitvtyDetailInst(detailInst);

        });

        return inst;
    }



}
