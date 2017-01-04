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
    private static final long serialVersionUID = 3974181411525113995L;
    @DBRef
    private Lord lord;
    @DBRef
    private GameUser gameUser;
    @Indexed
    private String productId;
    private String productDesc;
    private int quantity;
    private String customId;
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
    @Field("lord_id")
    private String lordId;

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

}
