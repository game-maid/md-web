/**
 * @Title: MissionStatus.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  闫昆
 */
 
package com.talentwalker.game.md.core.domain.gameworld;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: MissionStatus
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年8月5日 下午2:39:12
 */

public class MissionStatus {

    @JsonIgnore
    private String missionId;

    private boolean isAccept;

    private int progress;

    public MissionStatus(String missionId) {
        this.missionId = missionId;
        this.isAccept = false;
        this.progress = 0;
    }

    /**
     * @return missionId
     */
    public String getMissionId() {
        return missionId;
    }

    /**
     * @param missionId 要设置的 missionId
     */
    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    /**
     * @return isAccept
     */
    public boolean isAccept() {
        return isAccept;
    }

    /**
     * @param isAccept 要设置的 isAccept
     */
    public void setAccept(boolean isAccept) {
        this.isAccept = isAccept;
    }

    /**
     * @return progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress 要设置的 progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

}
