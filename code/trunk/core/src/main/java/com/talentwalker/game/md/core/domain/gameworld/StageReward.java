/**
 * @Title: StageReward.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月7日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

/**
 * @ClassName: StageReward
 * @Description: 关卡星级奖励对象
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月7日 下午5:46:03
 */

public class StageReward {

    /**
     * 领取标记
     */
    private boolean flag;

    /**
     * 领取时间
     */
    private long time;

    public StageReward() {
        this.flag = true;
        this.time = System.currentTimeMillis();
    }

    /**
     * @return flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag 要设置的 flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * @return time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time 要设置的 time
     */
    public void setTime(long time) {
        this.time = time;
    }

}
