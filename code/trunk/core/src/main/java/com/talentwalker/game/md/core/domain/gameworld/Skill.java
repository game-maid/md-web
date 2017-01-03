/**
 * @Title: Skill.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月12日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.io.Serializable;

/**
 * @ClassName: Skill
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月12日 上午10:25:43
 */

public class Skill implements Serializable {
    private static final long serialVersionUID = 7411091127967891620L;
    /**
     * 技能唯一id
     */
    protected String skillUid;
    /**
     * 技能标识
     */
    protected String skillId;
    /**
     * 等级
     */
    protected int level;
    /**
     * 经验
     */
    protected int exp;
    /**
     * 获得时间戳
     */
    protected long time;

    /**
     * @return skillUid
     */
    public String getSkillUid() {
        return skillUid;
    }

    /**
     * @param skillUid 要设置的 skillUid
     */
    public void setSkillUid(String skillUid) {
        this.skillUid = skillUid;
    }

    /**
     * @return skillId
     */
    public String getSkillId() {
        return skillId;
    }

    /**
     * @param skillId 要设置的 skillId
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
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
     * @return exp
     */
    public int getExp() {
        return exp;
    }

    /**
     * @param exp 要设置的 exp
     */
    public void setExp(int exp) {
        this.exp = exp;
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
