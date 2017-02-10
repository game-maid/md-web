/**
 * @Title: Order.java
 * @Copyright (C) 2016 太能沃可
 * @Description:订单
 * @Revision History:
 * @Revision 1.0 2016年12月5日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;
import com.talentwalker.game.md.core.domain.GameUser;

/**
 * @ClassName: Order
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月5日 下午3:13:16
 */
@Document(collection = "game_order")
public class Order extends BaseDomain {
    public final static Integer STATE_YES = 0;
    public final static Integer STATE_NO = 1;
    private static final long serialVersionUID = 3974181411525113995L;
    @DBRef
    private Lord lord;
    @DBRef
    private GameUser gameUser;
    /**
     * 产品id
     */
    @Indexed
    private String productId;
    /**
     * 产品描述
     */
    private String productDesc;
    /**
     * 数量
     */
    private int quantity;
    private String customId;
    /**
     * 状态 0：表示支付成功 1：表示未支付成功
     */
    private int state;
    /**
     * 区服
     */
    @Field("zone_id")
    private String zoneId;
    /**
     * 包
     */
    @Field("package_id")
    private String packageId;
    /**
     * 该订单价格
     */
    private Double price;
    /**
     * 玩家id
     */
    private String lordId;
    /**
     * 充值时玩家的等级
     */
    @Field("lord_current_level")
    private int lordLevel;

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
     * 充值时玩家vip等级
     */
    @Field("lord_vip_level")
    private int lordVipLevel;
    /**
     * 充值时玩家的vip积分
     */
    @Field("lord_vip_score")
    private int lordVipScore;
    /**
     * 产品类型 1：金币  其他代表月卡
     */
    @Field("product_type")
    private int productType;
    /*
     * 支付第三方订单号
     */
    @Indexed
    @Field("order_id")
    private String orderId;
    /**
     * 创建时间
     */
    @Field("create_time")
    private long createTime;
    /**
     * 支付时间
     */
    @Field("pay_time")
    private long payTime;
    /**
     * 平台Id
     */
    @Field("platform_id")
    private String platformId;

    /**
     * @return lord
     */
    public Lord getLord() {
        return lord;
    }

    /**
     * @param lord 要设置的 lord
     */
    public void setLord(Lord lord) {
        this.lord = lord;
    }

    /**
     * @return gameUser
     */
    public GameUser getGameUser() {
        return gameUser;
    }

    /**
     * @param gameUser 要设置的 gameUser
     */
    public void setGameUser(GameUser gameUser) {
        this.gameUser = gameUser;
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
     * @return productDesc
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * @param productDesc 要设置的 productDesc
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity 要设置的 quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return customId
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * @param customId 要设置的 customId
     */
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * @return state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state 要设置的 state
     */
    public void setState(int state) {
        this.state = state;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLordId() {
        return lordId;
    }

    public void setLordId(String lordId) {
        this.lordId = lordId;
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
     * @return lordVipScore
     */
    public int getLordVipScore() {
        return lordVipScore;
    }

    /**
     * @param lordVipScore 要设置的 lordVipScore
     */
    public void setLordVipScore(int lordVipScore) {
        this.lordVipScore = lordVipScore;
    }

    /**
     * @return productType
     */
    public int getProductType() {
        return productType;
    }

    /**
     * @param productType 要设置的 productType
     */
    public void setProductType(int productType) {
        this.productType = productType;
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
     * @return createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 要设置的 createTime
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return payTime
     */
    public long getPayTime() {
        return payTime;
    }

    /**
     * @param payTime 要设置的 payTime
     */
    public void setPayTime(long payTime) {
        this.payTime = payTime;
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

}
