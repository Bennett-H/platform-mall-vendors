package com.platform.cost.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Title:类中文名称
 * Description: 类功能的描述  条件判断
 * </pre>
 *
 * @author caozj  cao.zhijun@163.com
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 * @date 2024-01-20 上午11:38:01
 */
public abstract class AbstractCondition implements IConditionMate {

    private static Logger logger = LoggerFactory.getLogger(AbstractCondition.class);



    @Override
    public Integer getCoditionObj(String  conditionValue){




        return Integer.valueOf(conditionValue);
    }


}
