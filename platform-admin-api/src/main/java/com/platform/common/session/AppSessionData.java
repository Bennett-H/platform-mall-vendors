package com.platform.common.session;

/**
 * @author caozj
 * @date 2018/7/26  下午12:02
 * @discription 类描述:
 */
public class AppSessionData extends  SessionData{

    //头像
    private String imageUrl ;

    //经销商编码
    private String dealerCode ;

    //经销商名称
    private String dealerName ;

    //角色编码
    private String roleCode ;

    //角色名称
    private String roleName ;

    //是否网销顾问
    private String ifNetSales ;

    private String mobile ;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIfNetSales() {
        return ifNetSales;
    }

    public void setIfNetSales(String ifNetSales) {
        this.ifNetSales = ifNetSales;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
