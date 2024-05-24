package com.platform.cost;


import com.platform.cost.model.actitvty.ActitvtyModel;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 活动加载器  根据ID加载活动信息
 * @Description: 类功能的描述
 * @date 2024-01-20  15:44
 */
public interface IActitvtyLoader {


    /* *
     * @MethodName: 根据活动ID  加载活动实例信息
     * @Description: //TODO
     * @date 2024-01-20 15:46
     * @param [actitvtyId]
     * @return
     */
    public ActitvtyModel getActitvtyById(String actitvtyId);


}
