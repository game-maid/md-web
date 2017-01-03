/**
 * @Title: FriendStrengthState.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月9日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: FriendStrengthStatus
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月9日 下午12:29:03
 */
@Document(collection = "game_friend_strength_status")
public class FriendStrengthStatus extends BaseDomain {

    private static final long serialVersionUID = -5333667234174490828L;
    /**
     * 领取时间
     */
    private long getTime;
    /**
     * 领取次数
     */
    private int times;

    /**
     * @return getTime
     */
    public long getGetTime() {
        return getTime;
    }

    /**
     * @param getTime 要设置的 getTime
     */
    public void setGetTime(long getTime) {
        this.getTime = getTime;
    }

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

}
