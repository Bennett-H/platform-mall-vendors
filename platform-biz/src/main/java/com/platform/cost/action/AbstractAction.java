package com.platform.cost.action;


import com.google.common.collect.Lists;
import com.platform.cost.model.acct.AcctActitvtyInst;
import com.platform.cost.model.acct.AcctItemModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.acct.PriceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 类中文名称
 * @Description: 类功能的描述
 * @date 2024-01-20  01:39
 */
public abstract  class AbstractAction implements IPromotionAction{


    /* *
     * @MethodName: 方法名称  根据行号获取商品明细
     * @Description: //TODO
     * @date 2024-01-20 01:41
     * @param []
     * @return
     */
    public List<AcctItemModel> getAcctDetailByLineNums(AcctModel model, List<Integer> linNums){
        List<AcctItemModel> items =  model.getAcctItemModelList();

        Map<Integer,AcctItemModel> map = new HashMap<Integer,AcctItemModel>();
        for(AcctItemModel acctItemModel:items){
            map.put(acctItemModel.getLineNum(),acctItemModel);
        }

        List<AcctItemModel> acctItems = Lists.newArrayList();
        linNums.forEach(item->{

            acctItems.add(map.get(item));
        });

        return acctItems ;

    }


    /* *
     * @MethodName: 方法名称 费用分摊
     * @Description: //TODO
     * @date 2024-01-20 15:21
     * @param []
     * @return
     */
    public PriceModel amountAllocation(double totalDiscontAmount, double retailPrice){

        //商品分摊金额  按照  商品数量*100  除总数取整  再
        double  allocationAmount = ((retailPrice*100)/totalDiscontAmount);

        PriceModel priceModel = new PriceModel();
        //原价
        priceModel.setRetailPrice(retailPrice);

        //优惠金额
        priceModel.setDiscountPrice(allocationAmount);

        //最终价格
        priceModel.setFinalPrice(retailPrice-allocationAmount);

        return  priceModel;

    }


    /* *
     * @MethodName: 方法名称
     * @Description: //创建活动明细数据
     * @date 2024-01-20 14:08
     * @param [acctItemModel, priceModel]
     * @return
     */
    public AcctActitvtyInst.AcctActitvtyDetailInst createActitvtyDetailInst(AcctItemModel acctItemModel, PriceModel priceModel) {
        //每个商品减价即可
        AcctActitvtyInst.AcctActitvtyDetailInst detailInst = new AcctActitvtyInst.AcctActitvtyDetailInst();

        //行号
        detailInst.setLineNum(acctItemModel.getLineNum());
        //单商品原价
        detailInst.setRetailPrice(acctItemModel.getRetailPrice());
        //折扣价
        detailInst.setDiscountPrice(priceModel.getDiscountPrice());
        //最终价格
        detailInst.setFinalPrice(priceModel.getFinalPrice());
        //skuiD
        detailInst.setSkuId(acctItemModel.getSkuId());
        //skunum
        detailInst.setSkuNum(acctItemModel.getSkuNum());
        //sku名称
        detailInst.setSkuName(acctItemModel.getSkuName());

        return detailInst;

    }
}
