/**
 * @Title: MonthCard.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月5日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

/**
 * @ClassName: MonthCard
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月5日 下午6:30:54
 */

public class MonthCard {
    /**
     * 上次领取奖励时间
     */
    private long lastTime;
    /**
     * 月卡获得时间
     */
    private long createTime;
    /**
     * 月卡数量
     */
    private int amount;

    /**
     * @return lastTime
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * @param lastTime 要设置的 lastTime
     */
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
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
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount 要设置的 amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

}
