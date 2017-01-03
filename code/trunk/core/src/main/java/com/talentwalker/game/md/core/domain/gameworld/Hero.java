/**
 * @Title: Hero.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月8日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.io.Serializable;

/**
 * @ClassName: Hero
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月8日 下午12:01:07
 */

public class Hero implements Serializable {
    private static final long serialVersionUID = -7965751465180193778L;
    /**
     * 武将唯一id
     */
    protected String heroUid;
    /**
     * 武将标识
     */
    protected String heroId;
    /**
     * 等级
     */
    protected int level;
    /**
     * 经验
     */
    protected int exp;
    /**
     * 突破等级
     */
    protected int breakLevel;
    /**
     * 英雄默认技能
     */
    protected Skill defaultSkill;
    /**
     * 获得英雄时间戳
     */
    protected long time;

    /**
     * 好感度等级
     */
    protected int loveLevel;
    /**
     * 好感度经验
     */
    protected int loveExp;

    /**
     * @return heroUid
     */
    public String getHeroUid() {
        return heroUid;
    }

    /**
     * @param heroUid 要设置的 heroUid
     */
    public void setHeroUid(String heroUid) {
        this.heroUid = heroUid;
    }

    /**
     * @return heroId
     */
    public String getHeroId() {
        return heroId;
    }

    /**
     * @param heroId 要设置的 heroId
     */
    public void setHeroId(String heroId) {
        this.heroId = heroId;
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
     * @return breakLevel
     */
    public int getBreakLevel() {
        return breakLevel;
    }

    /**
     * @param breakLevel 要设置的 breakLevel
     */
    public void setBreakLevel(int breakLevel) {
        this.breakLevel = breakLevel;
    }

    /**
     * @return defaultSkill
     */
    public Skill getDefaultSkill() {
        return defaultSkill;
    }

    /**
     * @param defaultSkill 要设置的 defaultSkill
     */
    public void setDefaultSkill(Skill defaultSkill) {
        this.defaultSkill = defaultSkill;
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

    public int getLoveLevel() {
        return loveLevel;
    }

    public void setLoveLevel(int loveLevel) {
        this.loveLevel = loveLevel;
    }

    public int getLoveExp() {
        return loveExp;
    }

    public void setLoveExp(int loveExp) {
        this.loveExp = loveExp;
    }

}
