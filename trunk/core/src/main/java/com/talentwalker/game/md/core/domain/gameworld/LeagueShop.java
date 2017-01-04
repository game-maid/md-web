/**
 * @Title: LeagueShop.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月22日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName: LeagueShop
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月22日 下午1:50:30
 */

public class LeagueShop {
    /**
     * 购买记录
     */
    protected Map<Integer, Integer> record;
    /**
     * 上一次购买时间
     */
    @Field("last_buy_time")
    protected long lastBuyTime;

    /**
     * @return record
     */
    public Map<Integer, Integer> getRecord() {
        return record;
    }

    /**
     * @param record 要设置的 record
     */
    public void setRecord(Map<Integer, Integer> record) {
        this.record = record;
    }

    /**
     * @return lastBuyTime
     */
    public long getLastBuyTime() {
        return lastBuyTime;
    }

    /**
     * @param lastBuyTime 要设置的 lastBuyTime
     */
    public void setLastBuyTime(long lastBuyTime) {
        this.lastBuyTime = lastBuyTime;
    }

}
