/**
 * @Title: Player.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.core.response;

import com.talentwalker.game.md.core.domain.gameworld.Lord;

/**
 * @ClassName: Player
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月13日 下午3:29:33
 */

public class Player extends Lord {
    private static final long serialVersionUID = -3674067040975333086L;

    public Player() {
    }

    public Player(Lord lord) {
        this.id = lord.getId();
        this.name = lord.getName();
        this.headIcon = lord.getHeadIcon();
        this.level = lord.getLevel();
        this.exp = lord.getExp();
        this.diamond = lord.getDiamond();
        this.gold = lord.getGold();
        this.vipScore = lord.getVipScore();
        this.vipLevel = lord.getVipLevel();
        this.strength = lord.getStrength();
        this.strengthTime = lord.getStrengthTime();
        this.energy = lord.getEnergy();
        this.energyTime = lord.getEnergyTime();
        this.heroLimit = lord.getHeroLimit();
        this.equipLimit = lord.getEquipLimit();
        this.skillLimit = lord.getSkillLimit();
        this.itemLimit = lord.getItemLimit();
        this.breakcoin = lord.getBreakcoin();
        this.equipTimes = lord.getEquipTimes();
        this.itemTimes = lord.getItemTimes();
        this.skillTimes = lord.getSkillTimes();
        this.heroTimes = lord.getHeroTimes();
        this.notes = lord.getNotes();
        this.strengthTimes = lord.getStrengthTimes();
        this.kanbanId = lord.getKanbanId();

        this.guidanceStep = lord.getGuidanceStep();
        this.signInRecords = lord.getSignInRecords();
        this.replenishSignTimes = lord.getReplenishSignTimes();
        this.dailyVipReward = lord.getDailyVipReward();
        this.levelVipReward = lord.getLevelVipReward();
        this.monthCard = lord.getMonthCard();
        this.freeStrengthTime = lord.getFreeStrengthTime();
        this.strengthDiscount = lord.getStrengthDiscount();
        this.strengthDiscountTime = lord.getStrengthDiscountTime();
        this.discountStrengthTime = lord.getDiscountStrengthTime();
        this.lvupRecord = lord.getLvupRecord();

        this.dailyFirstFreeStrenthStartTime = lord.getDailyFirstFreeStrenthStartTime();
        this.dailyFirstFreeStrenthEndTime = lord.getDailyFirstFreeStrenthEndTime();
        this.dailySecondFreeStrenthStartTime = lord.getDailySecondFreeStrenthStartTime();
        this.dailySecondFreeStrenthEndTime = lord.getDailySecondFreeStrenthEndTime();
    }

}
