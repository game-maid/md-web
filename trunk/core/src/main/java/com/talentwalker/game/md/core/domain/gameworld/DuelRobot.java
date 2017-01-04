/**
 * @Title: DuelRobot.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月11日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: DuelRobot
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月11日 上午10:08:58
 */
@Document
public class DuelRobot extends BaseDomain {
    /**
     * 名字
     */
    protected String name;
    /**
     * 头像
     */
    @Field("head_icon")
    protected String headIcon;
    /**
     * NPC等级
     */
    protected int level;
    /**
     * 当前排名
     */
    protected int rank;
    /**
     * 防守阵容
     */
    @Field("form_defend")
    protected List<FormHold> formDefend;
    /**
     * 装备
     */
    protected Map<String, Equip> equips;
    /**
     * 英雄
     */
    protected Map<String, Hero> hero;

    /**
     * 签名
     */
    protected String notes;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return headIcon
     */
    public String getHeadIcon() {
        return headIcon;
    }

    /**
     * @param headIcon 要设置的 headIcon
     */
    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
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
     * @return rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank 要设置的 rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return formDefend
     */
    public List<FormHold> getFormDefend() {
        return formDefend;
    }

    /**
     * @param formDefend 要设置的 formDefend
     */
    public void setFormDefend(List<FormHold> formDefend) {
        this.formDefend = formDefend;
    }

    /**
     * @return equips
     */
    public Map<String, Equip> getEquips() {
        return equips;
    }

    /**
     * @param equips 要设置的 equips
     */
    public void setEquips(Map<String, Equip> equips) {
        this.equips = equips;
    }

    /**
     * @return hero
     */
    public Map<String, Hero> getHero() {
        return hero;
    }

    /**
     * @param hero 要设置的 hero
     */
    public void setHero(Map<String, Hero> hero) {
        this.hero = hero;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**.
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DuelRobot [name=" + name + ", headIcon=" + headIcon + ", level=" + level + ", rank=" + rank
                + ", formDefend=" + formDefend + ", equips=" + equips + ", id=" + id + ", version=" + version;
    }

}
