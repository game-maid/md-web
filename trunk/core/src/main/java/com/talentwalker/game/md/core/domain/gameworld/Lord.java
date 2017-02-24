/**
 * @Title: Lord.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月12日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Lord
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月12日 上午9:56:23
 */
@Document(collection = "game_lord")
public class Lord extends BaseDomain {
    private static final long serialVersionUID = 1917606196627264540L;

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
     * 签名
     */
    protected String notes;
    /**
     * 级别
     */
    protected int level;
    /**
     * 当前经验
     */
    protected int exp;
    /**
     * 钻石
     */
    protected int diamond;
    /**
     * 金币
     */
    protected int gold;
    /**
     * 突破币
     */
    protected int breakcoin;
    /**
     * vip等级
     */
    @Field("vip_level")
    protected int vipLevel;
    /**
     * 当前等级的vip积分
     */
    @Field("vip_score")
    protected int vipScore;
    /**
     * 体力值
     */
    protected int strength;
    /**
     * 体力值购买时间
     */
    @Field("strength_buy_time")
    protected long strengthBuyTime;
    /**
     * 体力值购买次数
     */
    @Field("strengthTimes")
    protected int strengthTimes;
    /**
     * 体力值最后结算时间
     */
    @Field("strength_time")
    protected long strengthTime;
    /**
     * 精力值
     */
    protected int energy;
    /**
     * 精力值最后结算时间
     */
    @Field("energy_time")
    protected long energyTime;
    /**
     * 阵容列表
     */
    protected List<List<FormHold>> form;
    /**
     * 阵容是否启用
     */
    protected Map<Integer, Integer> formStart;
    /**
     * 武将集合
     */
    protected Map<String, Hero> heros;
    /**
     * 装备集合
     */
    protected Map<String, Equip> equips;
    /**
     * 技能集合
     */
    protected Map<String, Skill> skills;
    /**
     * 道具集合
     */
    protected Map<String, Integer> items;
    /**
     * 英雄魂魄
     */
    protected Map<String, Integer> souls;
    /**
     * 注册时间
     */
    @Field("register_time")
    protected long registerTime;
    /**
     * 英雄数量上限
     */
    @Field("hero_limit")
    protected int heroLimit;
    /**
     * 扩充英雄背包次数
     */
    @Field("hero_times")
    protected int heroTimes;
    /**
     * 技能数量上限
     */
    @Field("skill_limit")
    protected int skillLimit;
    /**
     * 扩充技能背包次数
     */
    @Field("skill_times")
    protected int skillTimes;
    /**
     * 装备数量上限
     */
    @Field("equip_limit")
    protected int equipLimit;
    /**
     * 扩充装备背包次数
     */
    @Field("equip_times")
    protected int equipTimes;
    /**
     * 道具数量上限
     */
    @Field("item_limit")
    protected int itemLimit;
    /**
     * 扩充道具背包次数
     */
    @Field("item_times")
    protected int itemTimes;
    /**
     * 图鉴
     */
    @Field("handbook")
    protected Map<String, Map<String, Integer>> handbook;
    /**
     * 上一次访问时间
     */
    @Field("last_time")
    protected long lastTime;
    /**
     * 上一次修改签名时间
     */
    protected long lastNotesTime;

    /**
     * 现使用的看板id
     */
    protected String kanbanId;

    /**
     * 玩家拥有的所以看板id
     */
    protected List<String> kanbanIds;
    /**
     * 每日VIP奖励
     * <vip等级,0:未领取/1:已领取>
     */
    protected Map<Integer, Integer> dailyVipReward;
    /**
     * 最后一次每日VIP奖励的刷新时间
     */
    protected long lastGetDailyVipRewardTime;
    /**
     * vip等级奖励领取记录
     */
    protected List<Integer> levelVipReward;
    /**
     * 月卡
     */
    protected Map<String, MonthCard> monthCard;
    /**
     * 上次领取免费体力的时间
     */
    protected long freeStrengthTime;
    /**
     * 日常比赛折扣
     */
    protected double strengthDiscount;
    /**
     * 日常比赛折扣时间
     */
    protected long strengthDiscountTime;

    /**
     * 购买折扣体力的时间
     * Integer : 购买的第几次的体力 
     * Long : 购买该次体力的时间
     */
    protected Map<Integer, Long> discountStrengthTime;
    /**
     * 冲级奖励领取记录
     * Integer : 等级
     * Long ： 领取时间
     */
    protected Map<Integer, Long> lvupRecord;
    /**
     * 首充记录
     */
    protected TopUpFirstRecord topUpFirstRecord;
    /**
     * 首充奖励是否被领取 true：已经领取，false：未领取
     */
    protected boolean topUpFirstAward;

    /**
     * 本月签到记录 
     * Integer : 第几次签到
     */
    protected Map<Integer, SignInRecord> signInRecords;
    /**
     * 本月补签次数
     */
    protected int replenishSignTimes;
    /**
     * 记录新手引导
     */
    protected int guidanceStep;
    /**
     * 新手武将 (新手引导时选择的英雄)
     */
    protected String guidanceHeroId;
    /**
     * 赠送体力记录
     */
    protected int givesStrengthTimes;

    /**
     * 最后一次赠送奖励领取时间
     */
    protected Date givesStrengthTime;
    /**
     * 新手招募第步了
     */
    @Field("guidance_recruit")
    protected int guidanceRcruit;

    /**
     * 好感度
     * Map<武将id，好感度>
     */
    protected Map<String, Romance> romance;
    /**
     * 当前随机好感度剧情（状态）
     * Map<heroId,Map<第几个剧情，剧情完成状态>>（）
     */
    @Field("romance_random_story")
    protected Map<String, Map<Integer, Integer>> romanceRandomStory;

    /**
     * 随机好感度剧情生成时间
     */
    @Field("romance_story_time")
    protected long romanceStoryTime;

    /**
     * 每天第一次免费体力开始领取的时间
     */
    @Transient
    protected Long dailyFirstFreeStrenthStartTime;
    /**
     * 每天第一次免费体力结束领取的时间
     */
    @Transient
    protected Long dailyFirstFreeStrenthEndTime;
    /**
     * 每天第2次免费体力开始领取的时间
     */
    @Transient
    protected Long dailySecondFreeStrenthStartTime;
    /**
     * 每天第2次免费体力结束领取的时间
     */
    @Transient
    protected Long dailySecondFreeStrenthEndTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getVipScore() {
        return vipScore;
    }

    public void setVipScore(int vipScore) {
        this.vipScore = vipScore;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public long getStrengthTime() {
        return strengthTime;
    }

    public void setStrengthTime(long strengthTime) {
        this.strengthTime = strengthTime;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public long getEnergyTime() {
        return energyTime;
    }

    public void setEnergyTime(long energyTime) {
        this.energyTime = energyTime;
    }

    /**
     * @return form
     */
    public List<List<FormHold>> getForm() {
        if (form == null) {
            form = new ArrayList<>();
        }
        return form;
    }

    /**
     * @param form 要设置的 form
     */
    public void setForm(List<List<FormHold>> form) {
        this.form = form;
    }

    public Map<String, Hero> getHeros() {
        return heros;
    }

    public void setHeros(Map<String, Hero> heros) {
        this.heros = heros;
    }

    public Map<String, Equip> getEquips() {
        return equips;
    }

    public void setEquips(Map<String, Equip> equips) {
        this.equips = equips;
    }

    public Map<String, Skill> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, Skill> skills) {
        this.skills = skills;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    /**
     * @return registerTime
     */
    public long getRegisterTime() {
        return registerTime;
    }

    /**
     * @param registerTime 要设置的 registerTime
     */
    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * @return vipLevel
     */
    public int getVipLevel() {
        return vipLevel;
    }

    /**
     * @param vipLevel 要设置的 vipLevel
     */
    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    /**
     * @return heroLimit
     */
    public int getHeroLimit() {
        return heroLimit;
    }

    /**
     * @param heroLimit 要设置的 heroLimit
     */
    public void setHeroLimit(int heroLimit) {
        this.heroLimit = heroLimit;
    }

    /**
     * @return skillLimit
     */
    public int getSkillLimit() {
        return skillLimit;
    }

    /**
     * @param skillLimit 要设置的 skillLimit
     */
    public void setSkillLimit(int skillLimit) {
        this.skillLimit = skillLimit;
    }

    /**
     * @return equipLimit
     */
    public int getEquipLimit() {
        return equipLimit;
    }

    /**
     * @param equipLimit 要设置的 equipLimit
     */
    public void setEquipLimit(int equipLimit) {
        this.equipLimit = equipLimit;
    }

    /**
     * @return itemLimit
     */
    public int getItemLimit() {
        return itemLimit;
    }

    /**
     * @param itemLimit 要设置的 itemLimit
     */
    public void setItemLimit(int itemLimit) {
        this.itemLimit = itemLimit;
    }

    /**
     * @return breakcoin
     */
    public int getBreakcoin() {
        return breakcoin;
    }

    /**
     * @param breakcoin 要设置的 breakcoin
     */
    public void setBreakcoin(int breakcoin) {
        this.breakcoin = breakcoin;
    }

    /**
     * @return formStart
     */
    public Map<Integer, Integer> getFormStart() {
        return formStart;
    }

    /**
     * @param formStart 要设置的 formStart
     */
    public void setFormStart(Map<Integer, Integer> formStart) {
        this.formStart = formStart;
    }

    /**
     * @return heroTimes
     */
    public int getHeroTimes() {
        return heroTimes;
    }

    /**
     * @param heroTimes 要设置的 heroTimes
     */
    public void setHeroTimes(int heroTimes) {
        this.heroTimes = heroTimes;
    }

    /**
     * @return skillTimes
     */
    public int getSkillTimes() {
        return skillTimes;
    }

    /**
     * @param skillTimes 要设置的 skillTimes
     */
    public void setSkillTimes(int skillTimes) {
        this.skillTimes = skillTimes;
    }

    /**
     * @return equipTimes
     */
    public int getEquipTimes() {
        return equipTimes;
    }

    /**
     * @param equipTimes 要设置的 equipTimes
     */
    public void setEquipTimes(int equipTimes) {
        this.equipTimes = equipTimes;
    }

    /**
     * @return itemTimes
     */
    public int getItemTimes() {
        return itemTimes;
    }

    /**
     * @param itemTimes 要设置的 itemTimes
     */
    public void setItemTimes(int itemTimes) {
        this.itemTimes = itemTimes;
    }

    /**
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes 要设置的 notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return handbook
     */
    public Map<String, Map<String, Integer>> getHandbook() {
        return handbook;
    }

    /**
     * @param handbook 要设置的 handbook
     */
    public void setHandbook(Map<String, Map<String, Integer>> handbook) {
        this.handbook = handbook;
    }

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
     * @return souls
     */
    public Map<String, Integer> getSouls() {
        return souls;
    }

    /**
     * @param souls 要设置的 souls
     */
    public void setSouls(Map<String, Integer> souls) {
        this.souls = souls;
    }

    /**
     * @return lastNotesTime
     */
    public long getLastNotesTime() {
        return lastNotesTime;
    }

    /**
     * @param lastNotesTime 要设置的 lastNotesTime
     */
    public void setLastNotesTime(long lastNotesTime) {
        this.lastNotesTime = lastNotesTime;
    }

    /**
     * @return strengthBuyTime
     */
    public long getStrengthBuyTime() {
        return strengthBuyTime;
    }

    /**
     * @param strengthBuyTime 要设置的 strengthBuyTime
     */
    public void setStrengthBuyTime(long strengthBuyTime) {
        this.strengthBuyTime = strengthBuyTime;
    }

    /**
     * @return strengthTimes
     */
    public int getStrengthTimes() {
        return strengthTimes;
    }

    /**
     * @param strengthTimes 要设置的 strengthTimes
     */
    public void setStrengthTimes(int strengthTimes) {
        this.strengthTimes = strengthTimes;
    }

    /**
     * @return kanbanId
     */
    public String getKanbanId() {
        return kanbanId;
    }

    /**
     * @param kanbanId 要设置的 kanbanId
     */
    public void setKanbanId(String kanbanId) {
        this.kanbanId = kanbanId;
    }

    /**
     * @return kanbanIds
     */
    public List<String> getKanbanIds() {
        return kanbanIds;
    }

    /**
     * @param kanbanIds 要设置的 kanbanIds
     */
    public void setKanbanIds(List<String> kanbanIds) {
        this.kanbanIds = kanbanIds;
    }

    public Map<Integer, Integer> getDailyVipReward() {
        return dailyVipReward;
    }

    public void setDailyVipReward(Map<Integer, Integer> dailyVipReward) {
        this.dailyVipReward = dailyVipReward;
    }

    public long getLastGetDailyVipRewardTime() {
        return lastGetDailyVipRewardTime;
    }

    public void setLastGetDailyVipRewardTime(long lastGetDailyVipRewardTime) {
        this.lastGetDailyVipRewardTime = lastGetDailyVipRewardTime;
    }

    public List<Integer> getLevelVipReward() {
        return levelVipReward;
    }

    public void setLevelVipReward(List<Integer> levelVipReward) {
        this.levelVipReward = levelVipReward;
    }

    /**
     * @return monthCard
     */
    public Map<String, MonthCard> getMonthCard() {
        if (monthCard == null) {
            monthCard = new HashMap<String, MonthCard>();
        }
        return monthCard;
    }

    /**
     * @param monthCard 要设置的 monthCard
     */
    public void setMonthCard(Map<String, MonthCard> monthCard) {
        this.monthCard = monthCard;
    }

    public long getFreeStrengthTime() {
        return freeStrengthTime;
    }

    public void setFreeStrengthTime(long freeStrengthTime) {
        this.freeStrengthTime = freeStrengthTime;
    }

    public double getStrengthDiscount() {
        return strengthDiscount;
    }

    public void setStrengthDiscount(double strengthDiscount) {
        this.strengthDiscount = strengthDiscount;
    }

    public long getStrengthDiscountTime() {
        return strengthDiscountTime;
    }

    public void setStrengthDiscountTime(long strengthDiscountTime) {
        this.strengthDiscountTime = strengthDiscountTime;
    }

    public Map<Integer, Long> getDiscountStrengthTime() {
        return discountStrengthTime;
    }

    public void setDiscountStrengthTime(Map<Integer, Long> discountStrengthTime) {
        this.discountStrengthTime = discountStrengthTime;
    }

    public Map<Integer, Long> getLvupRecord() {
        return lvupRecord;
    }

    public void setLvupRecord(Map<Integer, Long> lvupRecord) {
        this.lvupRecord = lvupRecord;
    }

    /**
     * @return topUpFirstRecord
     */
    public TopUpFirstRecord getTopUpFirstRecord() {
        return topUpFirstRecord;
    }

    /**
     * @param topUpFirstRecord 要设置的 topUpFirstRecord
     */
    public void setTopUpFirstRecord(TopUpFirstRecord topUpFirstRecord) {
        this.topUpFirstRecord = topUpFirstRecord;
    }

    /**
     * @return topUpFirstAward
     */
    public boolean isTopUpFirstAward() {
        return topUpFirstAward;
    }

    /**
     * @param topUpFirstAward 要设置的 topUpFirstAward
     */
    public void setTopUpFirstAward(boolean topUpFirstAward) {
        this.topUpFirstAward = topUpFirstAward;
    }

    public Map<Integer, SignInRecord> getSignInRecords() {
        if (signInRecords == null)
            signInRecords = new HashMap<>();
        return signInRecords;
    }

    public void setSignInRecords(Map<Integer, SignInRecord> signInRecords) {
        this.signInRecords = signInRecords;
    }

    public int getReplenishSignTimes() {
        return replenishSignTimes;
    }

    public void setReplenishSignTimes(int replenishSignTimes) {
        this.replenishSignTimes = replenishSignTimes;
    }

    public int getGuidanceStep() {
        return guidanceStep;
    }

    public void setGuidanceStep(int guidanceStep) {
        this.guidanceStep = guidanceStep;
    }

    public String getGuidanceHeroId() {
        return guidanceHeroId;
    }

    public void setGuidanceHeroId(String guidanceHeroId) {
        this.guidanceHeroId = guidanceHeroId;
    }

    /**
     * @return givesStrengthTimes
     */
    public int getGivesStrengthTimes() {
        return givesStrengthTimes;
    }

    /**
     * @param givesStrengthTimes 要设置的 givesStrengthTimes
     */
    public void setGivesStrengthTimes(int givesStrengthTimes) {
        this.givesStrengthTimes = givesStrengthTimes;
    }

    /**
     * @return givesStrengthTime
     */
    public Date getGivesStrengthTime() {
        if (givesStrengthTime == null) {
            givesStrengthTime = new Date();
        }
        return givesStrengthTime;
    }

    /**
     * @param givesStrengthTime 要设置的 givesStrengthTime
     */
    public void setGivesStrengthTime(Date givesStrengthTime) {
        this.givesStrengthTime = givesStrengthTime;
    }

    public Long getDailyFirstFreeStrenthStartTime() {
        return dailyFirstFreeStrenthStartTime;
    }

    public void setDailyFirstFreeStrenthStartTime(Long dailyFirstFreeStrenthStartTime) {
        this.dailyFirstFreeStrenthStartTime = dailyFirstFreeStrenthStartTime;
    }

    public Long getDailyFirstFreeStrenthEndTime() {
        return dailyFirstFreeStrenthEndTime;
    }

    public void setDailyFirstFreeStrenthEndTime(Long dailyFirstFreeStrenthEndTime) {
        this.dailyFirstFreeStrenthEndTime = dailyFirstFreeStrenthEndTime;
    }

    public Long getDailySecondFreeStrenthStartTime() {
        return dailySecondFreeStrenthStartTime;
    }

    public void setDailySecondFreeStrenthStartTime(Long dailySecondFreeStrenthStartTime) {
        this.dailySecondFreeStrenthStartTime = dailySecondFreeStrenthStartTime;
    }

    public Long getDailySecondFreeStrenthEndTime() {
        return dailySecondFreeStrenthEndTime;
    }

    public void setDailySecondFreeStrenthEndTime(Long dailySecondFreeStrenthEndTime) {
        this.dailySecondFreeStrenthEndTime = dailySecondFreeStrenthEndTime;
    }

    /**
     * @return guidanceRcruit
     */
    public int getGuidanceRcruit() {
        return guidanceRcruit;
    }

    /**
     * @param guidanceRcruit 要设置的 guidanceRcruit
     */
    public void setGuidanceRcruit(int guidanceRcruit) {
        this.guidanceRcruit = guidanceRcruit;
    }

    /**
     * @return romance
     */
    public Map<String, Romance> getRomance() {
        return romance;
    }

    /**
     * @param romance 要设置的 romance
     */
    public void setRomance(Map<String, Romance> romance) {
        this.romance = romance;
    }

    /**
     * @return romanceRandomStory
     */
    public Map<String, Map<Integer, Integer>> getRomanceRandomStory() {
        return romanceRandomStory;
    }

    /**
     * @param romanceRandomStory 要设置的 romanceRandomStory
     */
    public void setRomanceRandomStory(Map<String, Map<Integer, Integer>> romanceRandomStory) {
        this.romanceRandomStory = romanceRandomStory;
    }

    /**
     * @return romanceStoryTime
     */
    public long getRomanceStoryTime() {
        return romanceStoryTime;
    }

    /**
     * @param romanceStoryTime 要设置的 romanceStoryTime
     */
    public void setRomanceStoryTime(long romanceStoryTime) {
        this.romanceStoryTime = romanceStoryTime;
    }

}
