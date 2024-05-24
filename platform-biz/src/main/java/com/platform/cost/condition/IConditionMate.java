package com.platform.cost.condition;

import com.platform.cost.condition.impl.FullAmountCondition;
import com.platform.cost.condition.impl.FullNumCondition;
import com.platform.cost.condition.impl.NoCondition;
import com.platform.cost.constants.ConditionType;
import com.platform.cost.model.acct.AcctActitvtyJoinModel;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.model.condition.ConditionMode;


/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  活动条件 元数据定义
 * 优惠活动条件元数据  条件元数据分三种
 * 1.无条件   即该商品   特价  有这件商品就可以特价
 * 2.满金额   即活动商品 满足一定的金额  才可以参加优惠
 * 3.满件数   即活动商品 买的数量满足一定件数  才可以参加优惠
 * 活动
 * 活动条件
 * </pre>
 * @author caozj  cao.zhijun@163.com
 * @date 2024-01-20 下午8:31:56
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public interface IConditionMate<T> {

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
	public ConditionMode checkCondition(AcctModel model,
										AcctActitvtyJoinModel actitvtyJoinModel,
										ConditionMode conditionMode,
										String conditionValue);



	/**
	 *
	 * @Title: checkCondition
	 * @author caozj  cao.zhijun@163.com
	 * @date 2024-01-20 下午5:11:57
	 * @Description: 方法功能描述  获取当前条件对象
	 * @param
	 * @param
	 * @return
	 * @throws
	 */
	public T getCoditionObj(String  conditionValue);



}
