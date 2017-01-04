/**
 * @Title: ShopKanbanConfig.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月18日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.config;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: ShopKanbanConfig
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月18日 下午3:55:27
 */
@Document(collection = "config_shop_kanban")
public class ShopKanbanConfig extends BaseDomain {
    private static final long serialVersionUID = 5504559054881323600L;
    /**
     * 名称
     */
    protected String name;
    /**
     * 描述
     */
    protected String explain;
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
     * 商品
     * String 看板娘Id
     * Integer 折扣 （九折==9）
     */
    protected Map<String, Integer> good;

    /**
     * 商品    
     */
    @Transient
    protected String goods;

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

    public Map<String, Integer> getGood() {
        return good;
    }

    public void setGood(Map<String, Integer> good) {
        this.good = good;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

}
