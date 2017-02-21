/**
 * @Title: GainPayService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月21日 闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.LeagueLord;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.domain.gameworld.Romance;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.domain.gameworld.SoulStore;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

import net.sf.json.JSONObject;

/**
 * @ClassName GainPayService
 * @Description: 道具消耗、获得
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月21日 上午10:06:04
 */

@Component
public class GainPayService extends GameSupport {

    @Autowired
    private HeroService heroService;

    @Autowired
    private EquipService equipService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private LordService lordService;

    @Autowired
    private FormHoldService formService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MailService mailService;

    private void check(int have, int number) {
        if (have + number < 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21006);
        }
    }

    private void gainPay(Object obj, String itemId, int number) {
        if (obj instanceof Lord) {
            Lord lord = (Lord) obj;
            if (ItemID.DIAMOND.equals(itemId)) { // 钻石
                int diamond = lord.getDiamond();
                if (number < 0) {
                    // this.check(diamond, number);
                    if (diamond + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21019, "钻石不足");
                    }
                }
                lord.setDiamond(diamond + number);
                this.lordResponse(itemId, number, lord.getDiamond());
            } else if (ItemID.GOLD.equals(itemId)) { // 金币
                int gold = lord.getGold();
                if (number < 0) {
                    // this.check(gold, number);
                    if (gold + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21020, "金币不足");
                    }
                }
                lord.setGold(gold + number);
                this.lordResponse(itemId, number, lord.getGold());
            } else if (ItemID.BREAKCOIN.equals(itemId)) { // 突破币
                int coin = lord.getBreakcoin();
                if (number < 0) {
                    // this.check(coin, number);
                    if (coin + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21021, "突破币不足");
                    }
                }
                lord.setBreakcoin(coin + number);
                this.lordResponse(itemId, number, lord.getBreakcoin());
            } else if (ItemID.EXP.equals(itemId)) { // 经验
                if (number > 0) {
                    int preLevel = lord.getLevel();
                    int postLevel = lord.getLevel();
                    int exp = lord.getExp();
                    JSONObject config = this.getDataConfig().get("expPlayer").getJsonObject();
                    // 如果满级不做处理
                    if (preLevel < config.size()) {
                        exp += number;
                        for (int i = preLevel; i < config.size(); i++) {
                            int tmpExp = ((JSONObject) config.get(i + "")).getInt("exp");
                            if (exp < tmpExp) {
                                break;
                            }
                            exp -= tmpExp;
                            postLevel++;
                        }
                        // 如果满级经验清零
                        if (postLevel >= config.size()) {
                            exp = 0;
                        }
                    }

                    int add = postLevel - preLevel; // 升了几级
                    // 玩家升级，触发升级效果
                    if (add > 0) {
                        JSONObject upConfig = this.getDataConfig().get("strength").getJsonObject();
                        for (int i = 0; i < add; i++) {
                            int strength = ((JSONObject) upConfig.get((preLevel + i) + "")).getInt("strength_lvup");
                            int energy = ((JSONObject) upConfig.get((preLevel + i) + "")).getInt("energy_lvup");
                            if (strength > 0) {
                                this.gain(lord, ItemID.STRENGTH, strength);
                            }
                            if (energy > 0) {
                                this.gain(lord, ItemID.ENERGY, energy);
                            }
                        }
                        lord.setLevel(postLevel);
                        // 触发主线升级任务
                        missionService.trigerMissionOnceForLevel(lord);
                        this.lordResponse(ItemID.LEVEL, add, lord.getLevel());
                    }

                    lord.setExp(exp);
                    // 不确定，跟前端协定
                    this.lordResponse(itemId, number, lord.getExp());
                }
            } else if (ItemID.VIPSCORE.equals(itemId)) { // vip积分
                if (number > 0) {
                    int score = lord.getVipScore() + number;
                    JSONObject config = this.getDataConfig().get("VIP").getJsonObject();
                    int up = 0;
                    for (int i = 0; i < config.size(); i++) {
                        up += ((JSONObject) config.get(i + "")).getInt("score");
                        if (score >= up) {
                            lord.setVipLevel(i);
                            Map<Integer, Integer> dailyVipReward = lord.getDailyVipReward();
                            dailyVipReward.put(i, 0);
                        }
                    }
                    lord.setVipScore(score);
                    this.lordResponse(itemId, number, lord.getVipScore());
                }
            } else if (ItemID.STRENGTH.equals(itemId)) { // 体力
                this.restoreStrength(lord);
                int have = lord.getStrength();
                if (number > 0) {
                    int limit = this.getDataConfig().get("strength").get(lord.getLevel() + "")
                            .getInteger("strength_max");
                    if (have + number > limit) {
                        number = limit - have;
                        have = limit;
                    } else {
                        have += number;
                    }
                    if (number > 0) {
                        this.lordResponse(itemId, number, have);
                    }
                } else {
                    // this.check(lord.getStrength(), number);
                    if (lord.getStrength() + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21022, "体力不足");
                    }
                    have += number;
                    // 消耗体力同时获得主公经验
                    this.gain(lord, ItemID.EXP, Math.abs(number));
                    this.lordResponse(itemId, number, have);
                }
                lord.setStrength(have);
                Map<String, Object> mapLord = this.getResponse(ResponseKey.LORD);
                mapLord.put("strengthTime", lord.getStrengthTime());
            } else if (ItemID.ENERGY.equals(itemId)) { // 精力
                this.restoreEnergy(lord);
                int have = lord.getEnergy();
                if (number > 0) {
                    int limit = this.getDataConfig().get("strength").get(lord.getLevel() + "").getInteger("energy_max");
                    if (have + number > limit) {
                        number = limit - have;
                        have = limit;
                    } else {
                        have += number;
                    }
                    if (number > 0) {
                        this.lordResponse(itemId, number, have);
                    }
                } else {
                    // this.check(lord.getEnergy(), number);
                    if (lord.getEnergy() + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21023, "精力不足");
                    }
                    have += number;
                    this.lordResponse(itemId, number, have);
                }
                lord.setEnergy(have);
                Map<String, Object> mapLord = this.getResponse(ResponseKey.LORD);
                mapLord.put("energyTime", lord.getEnergyTime());
            } else if (StringUtils.startsWith(itemId, ItemID.EQUIP)) { // 装备
                Map<String, Equip> equips = lord.getEquips();
                Map<String, Object> newEquip = new HashMap<String, Object>();
                if (number > 0) {
                    lordService.heroHandbook(ItemID.EQUIP, itemId, lord);
                    if (equips.size() + number > lord.getEquipLimit()) {
                        int diff = lord.getEquipLimit() - equips.size();
                        // 发邮件补(number - diff)个道具
                        this.sendItemMail(lord, itemId, number - diff);
                        number = diff;
                    }
                    for (int i = 0; i < number; i++) {
                        Equip equip = equipService.newEquip(itemId);
                        equips.put(equip.getEquipUid(), equip);
                        newEquip.put(equip.getEquipUid(), equip);
                    }
                    this.gainResponse(ResponseKey.EQUIPS, newEquip);
                } else if (number < 0) {
                    if (equips.containsKey(itemId)) {
                        equips.remove(itemId);
                        formService.removeFormEquip(lord, itemId);
                        newEquip.put(itemId, null);
                        this.payResponse(ResponseKey.EQUIPS, itemId);
                    } else {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23010);
                    }
                }
                lord.setEquips(equips);
            } else if (StringUtils.startsWith(itemId, ItemID.SKILL)) { // 技能
                Map<String, Skill> skills = lord.getSkills();
                if (number > 0) {
                    // 图鉴
                    lordService.heroHandbook(ItemID.SKILL, itemId, lord);
                    if (skills.size() + number > lord.getSkillLimit()) {
                        int diff = lord.getSkillLimit() - skills.size();
                        // 发邮件补(number - diff)个道具
                        this.sendItemMail(lord, itemId, number - diff);
                        number = diff;
                    }
                    Map<String, Object> newSkill = new HashMap<String, Object>();
                    for (int i = 0; i < number; i++) {
                        Skill skill = skillService.newSkill(itemId);
                        skills.put(skill.getSkillUid(), skill);
                        newSkill.put(skill.getSkillUid(), skill);
                    }
                    this.gainResponse(ResponseKey.SKILLS, newSkill);
                } else if (number < 0) {
                    if (skills.containsKey(itemId)) {
                        skills.remove(itemId);
                        formService.removeFormSkill(lord, itemId);
                        this.payResponse(ResponseKey.SKILLS, itemId);
                    } else {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23007);
                    }
                }
                lord.setSkills(skills);
            } else if (StringUtils.startsWith(itemId, ItemID.HERO)) { // 英雄
                Map<String, Hero> heros = lord.getHeros();
                if (number > 0) {
                    // 武将图鉴
                    lordService.heroHandbook(ItemID.HERO, itemId, lord);
                    // 技能图鉴
                    String skillId = getDataConfig().get("heroConfig").get(itemId).getString("skillult");
                    lordService.heroHandbook(ItemID.SKILL, skillId, lord);
                    // 好感度
                    heroService.romanceAddHero(itemId, lord);
                    if (heros.size() + number > lord.getHeroLimit()) {
                        int diff = lord.getHeroLimit() - heros.size();
                        // 发邮件补(number - diff)个道具
                        this.sendItemMail(lord, itemId, number - diff);
                        number = diff;
                    }
                    Map<String, Object> newHero = new HashMap<String, Object>();
                    for (int i = 0; i < number; i++) {
                        Hero hero = heroService.newHero(itemId);
                        heros.put(hero.getHeroUid(), hero);
                        newHero.put(hero.getHeroUid(), hero);
                    }
                    this.gainResponse(ResponseKey.HEROES, newHero);
                } else if (number < 0) {
                    if (heros.containsKey(itemId)) {
                        heros.remove(itemId);
                        formService.removeFormHero(lord, itemId);
                        this.payResponse(ResponseKey.HEROES, itemId);
                    } else {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23001);
                    }
                }
                lord.setHeros(heros);
            } else if (itemId.startsWith(ItemID.ROMANCE_EXP)) {// 好感度经验
                if (number > 0) {
                    String[] items = itemId.split("--");
                    itemId = items[1];
                    Romance romance = lord.getRomance().get(itemId);
                    int romanceLevel = romance.getLevel();
                    DataConfig romanceLevelsConfig = getDataConfig().get(ConfigKey.ROMANCE_BASE).get(itemId)
                            .get(ConfigKey.ROMANCE_BASE_ROMANCE);
                    DataConfig romanceConfig = romanceLevelsConfig.get(romanceLevel + 1 + "");
                    Integer romanceExpCost = romanceConfig.getInteger(ConfigKey.ROMANCE_BASE_COST);
                    int romanceMaxLv = getDataConfig().get(ConfigKey.ROMANCE_BASE).get(itemId)
                            .getInteger(ConfigKey.ROMANCE_BASE_MAXLV);
                    while (number >= romanceExpCost) {// 升级
                        Map<Integer, Integer> storyMap = romance.getStory();
                        if (storyMap == null) {
                            storyMap = new HashMap<>();
                            romance.setStory(storyMap);
                        }
                        romanceLevel++;
                        storyMap.put(romanceLevel, Romance.STORY_STATE_LOCK);
                        number -= romanceExpCost;
                        if (romanceMaxLv <= romanceLevel) {
                            break;
                        }
                        romanceExpCost = romanceLevelsConfig.get(romanceLevel + 1 + "")
                                .getInteger(ConfigKey.ROMANCE_BASE_COST);
                    }
                    romance.setExp(number);
                    romance.setLevel(romanceLevel);
                    HashMap<Object, Object> updateRomanceMap = new HashMap<>();
                    updateRomanceMap.put(itemId, romance);
                    this.gameModel.addObject(ResponseKey.ROMANCE, updateRomanceMap);
                }
            } else if (StringUtils.startsWith(itemId, ItemID.SOUL)) { // 魂
                Map<String, Integer> souls = lord.getSouls();
                int count = 0;
                if (souls == null) {
                    souls = new HashMap<>();
                }
                if (souls.containsKey(itemId)) {
                    count = souls.get(itemId);
                }
                int have = count + number;
                if (have < 0) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21024, "魂魄不足");
                } else if (have == 0) {
                    souls.remove(itemId);
                } else if (have > 0) {
                    souls.put(itemId, have);
                }
                this.mapIntegerResponse(ResponseKey.SOULS, itemId, number, have);
                lord.setSouls(souls);
            } else if (this.getDataConfig().get("item").getJsonObject().containsKey(itemId)) {
                Map<String, Integer> items = lord.getItems();
                int count = 0;
                if (items.containsKey(itemId)) {
                    count = items.get(itemId);
                }
                int have = count + number;
                if (have < 0) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21006, "道具不足");
                } else if (have == 0) {
                    items.remove(itemId);
                } else if (have > 0) {
                    if (number > 0) {
                        // 校验上限
                        int pileLimit = this.getDataConfig().get("item").get(itemId).getInteger("pileLimit");
                        if (have > pileLimit) {
                            have = pileLimit;
                        }
                        if (!items.containsKey(itemId)) {
                            // 校验背包上限
                            if (lord.getItems().size() >= lord.getItemLimit()) {
                                this.sendItemMail(lord, itemId, have);
                                have = 0;
                            }
                        }
                    }
                    if (have > 0) {
                        // 图鉴
                        lordService.heroHandbook(ItemID.ITEM, itemId, lord);
                        items.put(itemId, have);
                    }
                }
                this.mapIntegerResponse(ResponseKey.ITEMS, itemId, number, have);
                lord.setItems(items);
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, itemId);
            }
        } else if (obj instanceof Duel) {
            Duel duel = (Duel) obj;
            if (ItemID.PVPGPLD.equals(itemId)) {
                int pvpGold = duel.getPvpGold();
                if (number < 0) {
                    // this.check(pvpGold, number);
                    if (pvpGold + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21025, "擂台币不足");
                    }
                }
                duel.setPvpGold(number + pvpGold);
                this.mapIntegerResponse(ResponseKey.DUEL, itemId, number, duel.getPvpGold());
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, itemId);
            }
        } else if (obj instanceof LeagueLord) {
            LeagueLord leagueLord = (LeagueLord) obj;
            if (ItemID.FUND.equals(itemId)) {
                int fund = leagueLord.getFund();
                if (number < 0) {
                    // this.check(fund, number);
                    if (fund + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21026, "联盟资金不足");
                    }
                }
                leagueLord.setFund(fund + number);
                this.mapIntegerResponse(ResponseKey.LEAGUE_LORD, itemId, number, leagueLord.getFund());
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, itemId);
            }
        } else if (obj instanceof SoulStore) {
            SoulStore store = (SoulStore) obj;
            if (ItemID.SOUL_GOLD.equals(itemId)) {
                int soulGold = store.getSoulGold();
                if (number < 0) {
                    if (soulGold + number < 0) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21027, "联盟资金不足");
                    }
                }
                store.setSoulGold(soulGold + number);
                this.mapIntegerResponse(ResponseKey.SOUL_STORE, itemId, number, store.getSoulGold());
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, itemId);
            }
        } else {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21004);
        }

    }

    /**
     * @Description:
     * @param lord
     * @param itemId
     * @param i
     * @throws
     */
    private void sendItemMail(Lord lord, String itemId, int number) {
        Map<String, Integer> reward = new HashMap<>();
        reward.put(itemId, number);
        mailService.sendMail(lord.getId(), this.getDataConfig().get("bagMsg").get("bagfull_01").getString("id"), null,
                reward, Mail.TYPE_NPC);
    }

    /**
     * 
     * @Description:返回前端类型 Map<String,Integer>
     * @param itemId
     * @param number
     * @param postNumber
     * @throws
     */
    @SuppressWarnings("unchecked")
    private void mapIntegerResponse(String key, String itemId, int number, int postNumber) {
        Map<String, Object> map = null;
        if (number > 0) {
            map = this.getResponse(ResponseKey.GAIN);
        } else {
            map = this.getResponse(ResponseKey.PAY);
        }

        Object obj = map.get(key);
        Map<String, Integer> tmp = new HashMap<String, Integer>();
        if (obj != null) {
            tmp = (Map<String, Integer>) obj;
        }

        Integer amount = tmp.get(itemId);
        if (amount == null) {
            amount = 0;
        }
        tmp.put(itemId, amount + Math.abs(number));
        map.put(key, tmp);
        Map<String, Object> mapKey = this.getResponse(key);
        mapKey.put(itemId, postNumber);
    }

    @SuppressWarnings("unchecked")
    private void gainResponse(String key, Map<String, Object> newMap) {
        Map<String, Object> map = this.getResponse(ResponseKey.GAIN);
        Map<String, Object> tmp = new HashMap<String, Object>();
        Object obj = map.get(key);
        if (obj != null) {
            tmp = (Map<String, Object>) obj;
        }
        tmp.putAll(newMap);
        map.put(key, tmp);
        Map<String, Object> mapKey = this.getResponse(key);
        mapKey.putAll(newMap);
    }

    @SuppressWarnings("unchecked")
    private void payResponse(String key, String itemId) {
        Map<String, Object> map = this.getResponse(ResponseKey.PAY);
        List<String> list = new ArrayList<String>();
        Object obj = map.get(key);
        if (obj != null) {
            list = (List<String>) obj;
        }
        list.add(itemId);
        map.put(key, list);
        Map<String, Object> mapKey = this.getResponse(key);
        mapKey.put(itemId, null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getResponse(String key) {
        Object obj = this.gameModel.getModel(key);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        } else {
            this.gameModel.addObject(key, map);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private void lordResponse(String itemId, int number, int postNumber) {
        Map<String, Object> map = null;
        if (number < 0) {
            map = this.getResponse(ResponseKey.PAY);
        } else if (number > 0) {
            map = this.getResponse(ResponseKey.GAIN);
        }

        Object obj = map.get(ResponseKey.LORD);
        Map<String, Integer> tmp = new HashMap<String, Integer>();
        if (obj != null) {
            tmp = (Map<String, Integer>) obj;
        }

        Integer amount = tmp.get(itemId);
        if (amount == null) {
            amount = 0;
        }
        tmp.put(itemId, amount + Math.abs(number));
        map.put(ResponseKey.LORD, tmp);
        Map<String, Object> mapLord = this.getResponse(ResponseKey.LORD);
        mapLord.put(itemId, postNumber);
    }

    /**
     * @Description 道具获得
     * @param obj 数据模型对象
     * @param itemId 道具ID
     * @param number 获得数量，必须大于0
     */
    public void gain(Object obj, String itemId, int number) {
        if (number <= 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21003);
        }
        this.gainPay(obj, itemId, number);
    }

    /**
     * @Description 道具消耗
     * @param obj 数据模型对象
     * @param itemId 道具ID
     * @param number 获得数量，必须大于0
     */
    public void pay(Object obj, String itemId, int number) {
        if (number <= 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21003);
        }
        this.gainPay(obj, itemId, -number);
    }

    private void checkItem(String itemId) {
        DataConfig config = this.getDataConfig().get("item").get(itemId);
        if (null == config) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, itemId);
        }
    }

}
