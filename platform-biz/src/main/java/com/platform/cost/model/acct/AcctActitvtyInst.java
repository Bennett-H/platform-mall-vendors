package com.platform.cost.model.acct;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 订单活动优惠信息
 * @Description: 类功能的描述
 * @date 2024-01-20  15:19
 */
public class AcctActitvtyInst {

    //活动ID
    public String actitvtyId;

    //活动名称
    public String actitvtyName;

    //积分数量
    public Integer scoreNum ;

    //零售单价
    public double retailPrice = 0.0 ;

    //优惠价
    public  double discountPrice = 0.0 ;

    //最终价格
    public  double finalPrice = 0.0 ;

    //优惠明细列表
    public List<AcctActitvtyDetailInst> actitvtyDetailInstList ;


    public String getActitvtyId() {
        return actitvtyId;
    }

    public void setActitvtyId(String actitvtyId) {
        this.actitvtyId = actitvtyId;
    }

    public String getActitvtyName() {
        return actitvtyName;
    }

    public void setActitvtyName(String actitvtyName) {
        this.actitvtyName = actitvtyName;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Integer getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(Integer scoreNum) {
        this.scoreNum = scoreNum;
    }

    public List<AcctActitvtyDetailInst> getActitvtyDetailInstList() {
        return actitvtyDetailInstList;
    }

    public void setActitvtyDetailInstList(List<AcctActitvtyDetailInst> actitvtyDetailInstList) {
        this.actitvtyDetailInstList = actitvtyDetailInstList;
    }


    /* *
     * @MethodName: 增加优惠参与明细
     * @Description: //TODO
     * @date 2024-01-20 01:27
     * @param []
     * @return
     */
    public void addActitvtyDetailInst(AcctActitvtyDetailInst detailInst){

        if(actitvtyDetailInstList==null){
            actitvtyDetailInstList = Lists.newArrayList();
        }
        actitvtyDetailInstList.add(detailInst);

        this.updateActitvtyInst();
    }

    /* *
     * @MethodName: 更新活动信息数据  当次活动明细数据都是由  明细分摊决定 当增加明细 重新计算整个价格
     * @Description: //TODO
     * @date 2024-01-20 12:12
     * @param []
     * @return
     */
    private void updateActitvtyInst(){

        //零售单价
        retailPrice = 0.0;
        //优惠价
        discountPrice = 0.0;
        //最终价格
        finalPrice = 0.0;
        actitvtyDetailInstList.forEach(item->{
            retailPrice = retailPrice + item.getRetailPrice();
            discountPrice = discountPrice+item.getDiscountPrice()*item.getSkuNum();

        });
        finalPrice = retailPrice-discountPrice;

    }

    public static class AcctActitvtyDetailInst {


        //商品行号
        public int lineNum;

        //skuID
        public Long skuId ;

        //skuID
        public String skuName ;

        //sku数量
        public int  skuNum ;

        //零售单价
        public double retailPrice ;

        //本次优惠价格
        public  double discountPrice;

        //最终价格
        public  double finalPrice ;

        public int getLineNum() {
            return lineNum;
        }

        public void setLineNum(int lineNum) {
            this.lineNum = lineNum;
        }

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public int getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(int skuNum) {
            this.skuNum = skuNum;
        }

        public double getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(double retailPrice) {
            this.retailPrice = retailPrice;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }
    }

}
