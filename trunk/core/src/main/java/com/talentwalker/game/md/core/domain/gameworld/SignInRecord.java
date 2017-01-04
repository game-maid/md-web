
package com.talentwalker.game.md.core.domain.gameworld;

/**
 * @ClassName: SignInRecord
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月9日 下午4:05:20
 */
public class SignInRecord {
    /**
     * 单倍奖励
     */
    public static final int STATUS_1 = 1;
    /**
     * 双倍奖励
     */
    public static final int STATUS_2 = 2;

    /**
     * 签到时间
     */
    private long time;

    /**
     * 签到状态
     * 0：正常签到
     * 1：补签
     */
    private int status;
    /**
     * 领奖状态
     * 1：单倍奖励
     * 2：双倍奖励
     */
    private int rewardStatus;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(int rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

}
