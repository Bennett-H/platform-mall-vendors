package com.platform.cost.model.acct;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 订单模型
 * @Description: 订单费用模型
 * @date 2024-01-20  20:36
 */
public class AcctModel {

    //订单ID
    public Long orderId ;

    //订单编码
    public Long orderCode ;

    //
    public String tenantId ;

    //原总价
    public double originTotalAmount ;

    //优惠总价
    public double pomitionAmount ;

    //优惠券价格
    public double couponAmount ;

    //抹零金额
    public double eraseAmount ;

    //积分抵扣价格
    public double scoreAmount ;

    //应付总价
    public double actualAmount ;

    //商品明细
    public List<AcctItemModel> acctItemModelList;

    //活动计算明细
    public List<AcctActitvtyInst> acctActitvtyInsts ;

    //订单活动参与记录明细  前端传入
    public List<AcctActitvtyJoinModel> acctActitvtyJoinModels ;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public double getOriginTotalAmount() {
        return originTotalAmount;
    }

    public void setOriginTotalAmount(double originTotalAmount) {
        this.originTotalAmount = originTotalAmount;
    }

    public double getPomitionAmount() {
        return pomitionAmount;
    }

    public void setPomitionAmount(double pomitionAmount) {
        this.pomitionAmount = pomitionAmount;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getEraseAmount() {
        return eraseAmount;
    }

    public void setEraseAmount(double eraseAmount) {
        this.eraseAmount = eraseAmount;
    }

    public double getScoreAmount() {
        return scoreAmount;
    }

    public void setScoreAmount(double scoreAmount) {
        this.scoreAmount = scoreAmount;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public List<AcctItemModel> getAcctItemModelList() {
        return acctItemModelList;
    }

    public void setAcctItemModelList(List<AcctItemModel> acctItemModelList) {
        this.acctItemModelList = acctItemModelList;
    }

    public List<AcctActitvtyJoinModel> getAcctActitvtyJoinModels() {
        return acctActitvtyJoinModels;
    }

    public void setAcctActitvtyJoinModels(List<AcctActitvtyJoinModel> acctActitvtyJoinModels) {
        this.acctActitvtyJoinModels = acctActitvtyJoinModels;
    }

    public List<AcctActitvtyInst> getAcctActitvtyInsts() {
        return acctActitvtyInsts;
    }



    public void setAcctActitvtyInsts(List<AcctActitvtyInst> acctActitvtyInsts) {
        this.acctActitvtyInsts = acctActitvtyInsts;
    }


    public AcctItemModel getAcctDetailByLineNum(Integer linNums){
        List<AcctItemModel> items =  this.getAcctItemModelList();

        AcctItemModel acctItem = null ;
        for(AcctItemModel acctItemModel:items){
            if(linNums.equals(acctItemModel.getLineNum())){
                acctItem = acctItemModel ;
                break;
            }
        }

        return acctItem ;
    }


    /* *
     * @MethodName: 增加算费明细
     * @Description: //TODO
     * @date 2024-01-20 20:36
     * @param [inst]
     * @return
     */
    public void addActitvtyInst(AcctActitvtyInst inst){
        List instList = this.getAcctActitvtyInsts();
        if(instList==null){
            instList = Lists.newArrayList();
        }
        instList.add(inst);

        this.updateAcct(inst);
    }

    /* *
     * @MethodName: 更新价格信息
     * @Description: //TODO
     * @date 2024-01-20 20:36
     * @param [inst]
     * @return
     */
    public void updateAcct(AcctActitvtyInst inst){

        //BigDecimal a = BigDecimal.valueOf(0.0);
        this.setPomitionAmount(this.getPomitionAmount()+inst.getDiscountPrice());
        //this.setActualAmount();
        List<AcctActitvtyInst.AcctActitvtyDetailInst> list = inst.getActitvtyDetailInstList();
        for(AcctActitvtyInst.AcctActitvtyDetailInst detailInst:list){
            Integer lineNum = detailInst.getLineNum();
            AcctItemModel itemModel = this.getAcctDetailByLineNum(lineNum);

            itemModel.setDiscountPrice(itemModel.getDiscountPrice()+detailInst.getDiscountPrice());
            itemModel.setFinalPrice(itemModel.getRetailPrice()-itemModel.getDiscountPrice());
        }

    }


}
