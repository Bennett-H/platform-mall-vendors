package com.platform.cost.constants;

/**
 *
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  优惠条件类型
 * </pre>
 * @author caozj  cao.zhijun@163.com
 * @date 2020年6月18日 下午1:59:14
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public  class ConditionType {


	 //* 1.无条件   即该商品   特价  有这件商品就可以特价
	 public static final String  CONDITION_TYPE_NOT = "CONDITION-TYPE-NOT" ;

	 //* 2.满金额   即活动商品 满足一定的金额  才可以参加优惠
	 public static final String  CONDITION_FULL_NUM = "CONDITION-TYPE-NUM" ;

	 //* 3.满件数   即活动商品 买的数量满足一定件数  才可以参加优惠
	 public static final String  CONDITION_FULL_AMOUNT = "CONDITION-TYPE-AMOUNT" ;
}
