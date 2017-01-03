/**
 * @Title: ShopKanban.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月18日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

/**
 * @ClassName: ShopKanban
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月18日 下午3:17:10
 */

public class ShopKanban {
    private String id;
    /**
     * 结束时间
     */
    private long endTime;
    /**
     * 折扣后价格
     */
    private int price;

    /**
     * 活动排序
     */
    private int putrush;

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
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id 要设置的 id
     */
    public void setId(String id) {
        this.id = id;
    }

}
