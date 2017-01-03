/**
 * @Title: Shop.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月1日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.config;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Shop
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月1日 下午6:52:43
 */
@Document(collection = "config_shop")
public class ShopConfig extends BaseDomain {
    private static final long serialVersionUID = 3458429775020344650L;
    /**
     * 名称
     */
    protected String name;
    /**
     * 描述
     */
    protected String explain;
    /**
     * 出售货币类型
     */
    protected String money;
    /**
     * 货币id（出售货币类型为道具时，此id必填）
     */
    @Transient
    protected String itemId;
    /**
     *  单价
     */
    protected int price;
    /**
     * 限定数量
     */
    @Field("limit_amount")
    protected int limitAmount;
    /**
     * 限定vip等级
     */
    @Field("limit_vip")
    protected int limitVip;
    /**
     * 活动开始时间
     */
    @Field("start_time")
    protected long startTime;
    /**
     * 活动结束时间
     */
    @Field("end_time")
    protected long endTime;
    /**
     * 
     */
    @Transient
    protected String startDate;
    /**
     * 活动结束时间
     */
    @Transient
    protected String endDate;
    /**
     * 活动排序
     */
    protected int putrush;
    /**
     *生效服务器
     */
    @Field("zone_list")
    protected List<String> zoneList;
    /**
     *获取类型 (1随机获取，2全部获取)
     */
    @Field("acquire_type")
    protected int acquireType;
    /**
     * 出售道具
     */
    @Field("item_data")
    protected List<Map<String, Object>> itemData;
    /**
     * 出售道具
     */
    @Transient
    protected String data;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return explain
     */
    public String getExplain() {
        return explain;
    }

    /**
     * @param explain 要设置的 explain
     */
    public void setExplain(String explain) {
        this.explain = explain;
    }

    /**
     * @return money
     */
    public String getMoney() {
        return money;
    }

    /**
     * @param money 要设置的 money
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * @return itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId 要设置的 itemId
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price 要设置的 price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return limitAmount
     */
    public int getLimitAmount() {
        return limitAmount;
    }

    /**
     * @param limitAmount 要设置的 limitAmount
     */
    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    /**
     * @return limitVip
     */
    public int getLimitVip() {
        return limitVip;
    }

    /**
     * @param limitVip 要设置的 limitVip
     */
    public void setLimitVip(int limitVip) {
        this.limitVip = limitVip;
    }

    /**
     * @return startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime 要设置的 startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @param endTime 要设置的 endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate 要设置的 startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate 要设置的 endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return putrush
     */
    public int getPutrush() {
        return putrush;
    }

    /**
     * @param putrush 要设置的 putrush
     */
    public void setPutrush(int putrush) {
        this.putrush = putrush;
    }

    /**
     * @return zoneList
     */
    public List<String> getZoneList() {
        return zoneList;
    }

    /**
     * @param zoneList 要设置的 zoneList
     */
    public void setZoneList(List<String> zoneList) {
        this.zoneList = zoneList;
    }

    /**
     * @return acquireType
     */
    public int getAcquireType() {
        return acquireType;
    }

    /**
     * @param acquireType 要设置的 acquireType
     */
    public void setAcquireType(int acquireType) {
        this.acquireType = acquireType;
    }

    /**
     * @return itemData
     */
    public List<Map<String, Object>> getItemData() {
        return itemData;
    }

    /**
     * @param itemData 要设置的 itemData
     */
    public void setItemData(List<Map<String, Object>> itemData) {
        this.itemData = itemData;
    }

    /**
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data 要设置的 data
     */
    public void setData(String data) {
        this.data = data;
    }

}
