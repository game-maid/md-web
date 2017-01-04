/**
 * @Title: Equip.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月12日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.io.Serializable;

/**
 * @ClassName: Equip
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月12日 上午10:26:48
 */

public class Equip implements Serializable {
    private static final long serialVersionUID = 8051756844465633241L;
    /**
     * 装备唯一id
     */
    protected String equipUid;
    /**
     * 装备标识
     */
    protected String equipId;
    /**
     * 强化等级
     */
    protected int level;
    /**
     * 精炼等级
     */
    protected int stage;
    /**
     * 精炼经验
     */
    protected int expStage;
    /**
     * 获得时间戳
     */
    protected long time;

    /**
     * @return equipUid
     */
    public String getEquipUid() {
        return equipUid;
    }

    /**
     * @param equipUid 要设置的 equipUid
     */
    public void setEquipUid(String equipUid) {
        this.equipUid = equipUid;
    }

    /**
     * @return equipId
     */
    public String getEquipId() {
        return equipId;
    }

    /**
     * @param equipId 要设置的 equipId
     */
    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    /**
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level 要设置的 level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return stage
     */
    public int getStage() {
        return stage;
    }

    /**
     * @param stage 要设置的 stage
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * @return expStage
     */
    public int getExpStage() {
        return expStage;
    }

    /**
     * @param expStage 要设置的 expStage
     */
    public void setExpStage(int expStage) {
        this.expStage = expStage;
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
