/**
 * @Title: ItemDistributionStatistics.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月15日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: ItemDistributionStatistics
 * @Description: 道具分布统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月15日 下午3:27:59
 */
@Document(collection = "statistics_item_distribution")
public class ItemDistributionStatistics extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 1L;
    /**
     * 武将、武将碎片、装备、装备碎片、技能、消耗品（item表中除了礼包其他道具）、礼包道具、货币类道具（钻石、金币、突破币）
     */
    /**
     * 武将
     */
    protected Map<String, Integer> hero = new HashMap<>();
    /**
     * 武将碎片
     */
    protected Map<String, Integer> soul = new HashMap<>();
    /**
     * 装备
     */
    protected Map<String, Integer> equip = new HashMap<>();
    /**
     * 技能
     */
    protected Map<String, Integer> skill = new HashMap<>();
    /**
     * 消耗品道具
     */
    protected Map<String, Integer> expend = new HashMap<>();
    /**
     * 礼包道具
     */
    protected Map<String, Integer> box = new HashMap<>();
    /**
     * 货币类道具
     */
    protected Map<String, Integer> currency = new HashMap<>();

    /**
     * @return hero
     */
    public Map<String, Integer> getHero() {
        return hero;
    }

    /**
     * @param hero 要设置的 hero
     */
    public void setHero(Map<String, Integer> hero) {
        this.hero = hero;
    }

    /**
     * @return soul
     */
    public Map<String, Integer> getSoul() {
        return soul;
    }

    /**
     * @param soul 要设置的 soul
     */
    public void setSoul(Map<String, Integer> soul) {
        this.soul = soul;
    }

    /**
     * @return equip
     */
    public Map<String, Integer> getEquip() {
        return equip;
    }

    /**
     * @param equip 要设置的 equip
     */
    public void setEquip(Map<String, Integer> equip) {
        this.equip = equip;
    }

    /**
     * @return skill
     */
    public Map<String, Integer> getSkill() {
        return skill;
    }

    /**
     * @param skill 要设置的 skill
     */
    public void setSkill(Map<String, Integer> skill) {
        this.skill = skill;
    }

    /**
     * @return expend
     */
    public Map<String, Integer> getExpend() {
        return expend;
    }

    /**
     * @param expend 要设置的 expend
     */
    public void setExpend(Map<String, Integer> expend) {
        this.expend = expend;
    }

    /**
     * @return box
     */
    public Map<String, Integer> getBox() {
        return box;
    }

    /**
     * @param box 要设置的 box
     */
    public void setBox(Map<String, Integer> box) {
        this.box = box;
    }

    /**
     * @return currency
     */
    public Map<String, Integer> getCurrency() {
        return currency;
    }

    /**
     * @param currency 要设置的 currency
     */
    public void setCurrency(Map<String, Integer> currency) {
        this.currency = currency;
    }

}
