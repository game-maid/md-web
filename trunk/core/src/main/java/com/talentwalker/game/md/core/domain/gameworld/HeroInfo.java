/**
 * @Title: HeroInfo.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月29日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;

/**
 * @ClassName: HeroInfo
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月29日 下午10:43:31
 */

public class HeroInfo {
    private Hero hero;
    private List<Equip> equipList;
    private List<Skill> skillList;

    /**
     * @return hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * @param hero 要设置的 hero
     */
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    /**
     * @return equipList
     */
    public List<Equip> getEquipList() {
        return equipList;
    }

    /**
     * @param equipList 要设置的 equipList
     */
    public void setEquipList(List<Equip> equipList) {
        this.equipList = equipList;
    }

    /**
     * @return skillList
     */
    public List<Skill> getSkillList() {
        return skillList;
    }

    /**
     * @param skillList 要设置的 skillList
     */
    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

}
