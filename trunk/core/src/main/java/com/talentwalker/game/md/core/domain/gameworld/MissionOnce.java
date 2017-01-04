/**
 * @Title: MissionOnce.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月9日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: MissionOnce
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年8月9日 上午11:50:27
 */

@Document(collection = "game_mission_once")
public class MissionOnce extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    private Map<String, MissionStatus> missions;

    private Map<String, Map<String, Integer>> data;

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
     * @return data
     */
    public Map<String, Map<String, Integer>> getData() {
        return data;
    }

    /**
     * @param data 要设置的 data
     */
    public void setData(Map<String, Map<String, Integer>> data) {
        this.data = data;
    }

}
