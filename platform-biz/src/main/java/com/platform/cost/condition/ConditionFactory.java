package com.platform.cost.condition;

import com.platform.cost.action.AbstractAction;
import com.platform.cost.condition.impl.FullAmountCondition;
import com.platform.cost.condition.impl.FullNumCondition;
import com.platform.cost.condition.impl.NoCondition;
import com.platform.cost.constants.ConditionType;
import com.platform.cost.model.acct.*;
import com.platform.cost.model.condition.ConditionMode;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  获取条件校验类
 * </pre>
 * @author caozj  cao.zhijun@163.com
 * @date 2024-01-20 下午2:41:22
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class ConditionFactory {



	public static  Map<String, IConditionMate> conditionMateMap = null;

	private static void initConditionMate(){
		conditionMateMap = new HashMap<>();
		conditionMateMap.put(ConditionType.CONDITION_FULL_AMOUNT,new FullAmountCondition());
		conditionMateMap.put(ConditionType.CONDITION_FULL_NUM,new FullNumCondition());
		conditionMateMap.put(ConditionType.CONDITION_TYPE_NOT,new NoCondition());

	}

	/**
	 *
	 * @Title: getCondionMate
	 * @author caozj  cao.zhijun@163.com
	 * @date 2020年6月21日 下午4:01:00
	 * @Description: 方法功能描述  根据条件类型 获取不同的条件判定类
	 * @param conditionType
	 * @return
	 * @throws
	 */

	public static IConditionMate getCondionMate(String conditionType){
		if(conditionMateMap==null){
			initConditionMate();
		}


		return conditionMateMap.get(conditionType) ;
	}



}
