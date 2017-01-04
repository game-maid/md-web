/**
 * @Title: Stage.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月7日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

/**
 * @ClassName: Stage
 * @Description: 关卡对象
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月7日 下午5:10:13
 */

public class Stage {

    public Stage() {
        this.times = 0;
        this.lastTime = System.currentTimeMillis();
        this.star = 0;
    }

    /**
     * 今日已挑战次数
     */
    private int times;

    /**
     * 上次挑战时间戳
     */
    private long lastTime;

    /**
     * 战斗结果
     */
    private int star;

    /**
     * @return times
     */
    public int getTimes() {
        return times;
    }

    /**
     * @param times 要设置的 times
     */
    public void setTimes(int times) {
        this.times = times;
    }

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
     * @return star
     */
    public int getStar() {
        return star;
    }

    /**
     * @param star 要设置的 star
     */
    public void setStar(int star) {
        this.star = star;
    }

}
