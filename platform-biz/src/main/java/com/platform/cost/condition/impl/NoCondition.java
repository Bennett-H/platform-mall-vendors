package com.platform.cost.condition.impl;

import com.platform.cost.action.AbstractAction;
import com.platform.cost.condition.AbstractCondition;
import com.platform.cost.model.acct.*;
import com.platform.cost.model.condition.ConditionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  无条件  就是 无 满件  满额  条件   判断当前 商品是否
 *
 * 例如  商品直接折扣  商品直接特价  有该商品就需要进行折扣  不需要判断条件
 * </pre>
 *
 * @author caozj cao.zhijun@163.com
 * @date 2024-01-20 下午5:17:12
 * @version 1.00.00
 *
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class NoCondition extends AbstractCondition {


	private static Logger logger = LoggerFactory.getLogger(NoCondition.class);

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
		// 无条件就是当前有商品 就可以满足条件  构造当前条件对象返回

		//条件满足满足
		conditionMode.setConditionFlag(true);

		//当前条件是否满足  无条件 直接满足
		conditionMode.setCurrConditionFlag(true);

		//其他暂时不需要 直接返回

		return conditionMode;
	}



}
