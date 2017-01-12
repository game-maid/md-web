/**
 * @Title: TopUpStatisticsExcel.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月12日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: TopUpStatisticsExcel
 * @Description: 充值平台总览 excel 导出
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月12日 下午12:35:22
 */

public class TopUpStatisticsExcel {
    /**
     * 行号
     */
    private int index;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 道具描述
     */
    private String desc;
    /**
     * 道具ID
     */
    private String productId;
    /**
     * 数量
     */
    private int num;
    /**
     * 玩家ID
     */
    private String lordId;
    /**
     * 平台ID
     */
    private String platformId;
    /**
     * 区服
     */
    private String zoneName;
    /**
     * 包
     */
    private String packageName;
    /**
     * 价格
     */
    private double price;
    /**
     * 订单状态
     */
    private String state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 充值时等级
     */
    private int lordLevel;
    /**
     * 充值时vip等级
     */
    private int lordVipLevel;
    /**
     * 道具类型
     */
    private String productType;
    /**
     * 充值时vip积分
     */
    private int vipScore;
    /**
     * 第三方支付平台订单
     */
    private String otherOrderId;

    /**
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index 要设置的 index
     */
    public void setIndex(int index) {
        this.index = index;
    }

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
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc 要设置的 desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
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
     * @return num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num 要设置的 num
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return lordId
     */
    public String getLordId() {
        return lordId;
    }

    /**
     * @param lordId 要设置的 lordId
     */
    public void setLordId(String lordId) {
        this.lordId = lordId;
    }

    /**
     * @return platformId
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * @param platformId 要设置的 platformId
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    /**
     * @return zoneName
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * @param zoneName 要设置的 zoneName
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * @return packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName 要设置的 packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price 要设置的 price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state 要设置的 state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 要设置的 createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return payTime
     */
    public String getPayTime() {
        return payTime;
    }

    /**
     * @param payTime 要设置的 payTime
     */
    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    /**
     * @return lordLevel
     */
    public int getLordLevel() {
        return lordLevel;
    }

    /**
     * @param lordLevel 要设置的 lordLevel
     */
    public void setLordLevel(int lordLevel) {
        this.lordLevel = lordLevel;
    }

    /**
     * @return lordVipLevel
     */
    public int getLordVipLevel() {
        return lordVipLevel;
    }

    /**
     * @param lordVipLevel 要设置的 lordVipLevel
     */
    public void setLordVipLevel(int lordVipLevel) {
        this.lordVipLevel = lordVipLevel;
    }

    /**
     * @return productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType 要设置的 productType
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return vipScore
     */
    public int getVipScore() {
        return vipScore;
    }

    /**
     * @param vipScore 要设置的 vipScore
     */
    public void setVipScore(int vipScore) {
        this.vipScore = vipScore;
    }

    /**
     * @return otherOrderId
     */
    public String getOtherOrderId() {
        return otherOrderId;
    }

    /**
     * @param otherOrderId 要设置的 otherOrderId
     */
    public void setOtherOrderId(String otherOrderId) {
        this.otherOrderId = otherOrderId;
    }

}
