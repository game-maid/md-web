/**
 * @Title: MissionDaily.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: MissionDaily
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年8月5日 下午2:37:30
 */

@Document(collection = "game_mission_daily")
public class MissionDaily extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    @Field("last_time")
    private long lastTime;

    private Map<String, MissionStatus> missions;

    @Field("active_status")
    private Map<String, Integer> activeStatus;

    /**
     * 活跃点
     */
    private int active;

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
     * @return missions
     */
    public Map<String, MissionStatus> getMissions() {
        return missions;
    }

    /**
     * @param missions 要设置的 missions
     */
    public void setMissions(Map<String, MissionStatus> missions) {
        this.missions = missions;
    }

    /**
     * @return active
     */
    public int getActive() {
        return active;
    }

    /**
     * @param active 要设置的 active
     */
    public void setActive(int active) {
        this.active = active;
    }

    /**
     * @return activeStatus
     */
    public Map<String, Integer> getActiveStatus() {
        return activeStatus;
    }

    /**
     * @param activeStatus 要设置的 activeStatus
     */
    public void setActiveStatus(Map<String, Integer> activeStatus) {
        this.activeStatus = activeStatus;
    }

}
