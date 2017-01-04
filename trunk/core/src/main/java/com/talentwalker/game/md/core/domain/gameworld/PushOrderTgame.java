/**
 * @Title: OrderTgame.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月15日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

/**
 * TGame支付推送订单
 * @ClassName: OrderTgame
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月15日 下午7:22:53
 */

public class PushOrderTgame {
    public static String PLATFORM_ID = "tgame";

    /**
     * TGame平台订单号
     */
    private String orderId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品数量
     */
    private int productNum;
    /**
     * 商品价格
     */
    private double dealPrice;
    /**
     * 自定义
     */
    private String extInfo;
    /**
     * 
     */
    private String userId;
    /**
     * 签名
     */
    private String sign;

    /**
     * @return orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId 要设置的 orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId 要设置的 appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId 要设置的 productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return productNum
     */
    public int getProductNum() {
        return productNum;
    }

    /**
     * @param productNum 要设置的 productNum
     */
    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    /**
     * @return dealPrice
     */
    public double getDealPrice() {
        return dealPrice;
    }

    /**
     * @param dealPrice 要设置的 dealPrice
     */
    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }

    /**
     * @return extInfo
     */
    public String getExtInfo() {
        return extInfo;
    }

    /**
     * @param extInfo 要设置的 extInfo
     */
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    /**
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId 要设置的 userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign 要设置的 sign
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**.
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PushOrderTgame [orderId=" + orderId + ", appId=" + appId + ", productId=" + productId + ", productNum="
                + productNum + ", dealPrice=" + dealPrice + ", extInfo=" + extInfo + ", userId=" + userId + ", sign="
                + sign + "]";
    }

}
