package com.platform.cost.condition.impl;

import com.platform.cost.action.AbstractAction;
import com.platform.cost.condition.AbstractCondition;
import com.platform.cost.model.acct.*;
import com.platform.cost.model.condition.ConditionMode;


/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述 满额条件判断
 *
 * 活动满额  优惠条件
 * </pre>
 * @author caozj  cao.zhijun@163.com
 * @date 2024-01-20 下午5:16:08
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class FullAmountCondition extends AbstractCondition {

	/**
	 *
	 * @Title: checkCondition
	 * @author caozj  cao.zhijun@163.com
	 * @date 2024-01-20 下午5:11:57
	 * @Description: 方法功能描述  活动条件校验
	 * @param model
	 * @param
	 * @param
	 * @return
	 * @throws
	 */
	@Override
	public ConditionMode checkCondition(AcctModel model,
										AcctActitvtyJoinModel actitvtyJoinModel,
										ConditionMode conditionMode,
										String conditionValue) {
		// TODO Auto-generated method stub



		return null;
	}


	public Integer getCoditionObj(String  conditionValue){



		return Integer.valueOf(conditionValue);
	}


}
