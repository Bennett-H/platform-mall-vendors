package com.platform.cost.action.impl;


import com.platform.cost.action.IPromotionAction;
import com.platform.cost.model.acct.AcctActitvtyJoinModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.acct.PriceModel;
import com.platform.cost.model.condition.ConditionMode;

/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  商品价格增加
 *
 * 例如 加价购
 * </pre>
 *
 * @author caozj cao.zhijun@163.com
 * @date 2024年6月21日 上午11:42:32
 * @version 1.00.00
 *
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class AddPriceBuyAction implements IPromotionAction {



	/* *
	 * @MethodName: 执行动作
	 * @Description: //TODO
	 * @date 2024-01-20 18:22
	 * @param [priceModel, actionValue]
	 * @return
	 */
	@Override
	public void handle(AcctModel model,
					   AcctActitvtyJoinModel actitvtyJoinModel,
					   ConditionMode conditionMode) {

//		Integer addPrice = getActionObj(actionValue);
//
//		// 现价为原价减优惠价格
//		int finalPrice = priceModel.getBasePrice() + addPrice;
//
//		// 优惠价格 为原价 减现价
//		int cheap_price = 0;
//
//		// 构造返回值
//		PriceModel ret = new PriceModel();

	}

	@Override
	public PriceModel calPrice(double basicPrice, int value) {


		return null;
	}

	@Override
	public Integer getActionObj(String actionValue){


		return Integer.valueOf(actionValue);
	}


}
