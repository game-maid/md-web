/**
 * @Title: HeroService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月21日 闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.DuelRobot;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.HeroInfo;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Romance;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;
import com.talentwalker.game.md.core.util.UuidUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: HeroService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月21日 下午12:46:58
 */

@Service
public class HeroService extends GameSupport {

    /**
     * 英雄等级上限倍数
     */
    private final static int MULTIPLE_LIMIT = 3;

    @Autowired
    private GainPayService gainPayService;

    @Autowired
    private SkillService skillService;

    /**
     * 英雄配置表名称
     */
    final static String CONFIG_HERO_CONFIG = "heroConfig";
    /**
     * 
     */
    final static String CONFIG_FIGHT_POWER = "fightPower";
    /**
     * 技能配置
     */
    final static String CONFIG_SKILL = "skill";
    /**
     * 装备配置
     */
    final static String CONFIG_EQUIP = "equip";
    private static final Logger logger = Logger.getLogger(HeroService.class);

    private void checkHero(String heroId) {
        DataConfig config = this.getDataConfig().get(CONFIG_HERO_CONFIG).get(heroId);
        if (null == config) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, heroId);
        }
    }

    @SuppressWarnings("unchecked")
    public void addHeroExp(String heroUid, JSONObject items) {
        Lord lord = this.getLord();
        this.isHave(lord, heroUid);

        Map<String, Hero> heros = lord.getHeros();
        Hero hero = heros.get(heroUid);
        // 校验是否已经达到当前最高等级
        int limit = lord.getLevel() * MULTIPLE_LIMIT;
        if (hero.getLevel() >= limit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24001);
        }

        JSONObject config = this.getDataConfig().get("expHero").getJsonObject();
        // 校验武将是否达到满级
        if (hero.getLevel() >= config.size()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24003);
        }

        int exp = 0;
        Iterator<String> it = items.keys();
        while (it.hasNext()) {
            String itemId = it.next();
            int amount = items.getInt(itemId);
            DataConfig conf = this.getDataConfig().get("item").get(itemId);
            if (!"exp".equals(conf.getString("type"))) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24002, itemId);
            }
            exp += (conf.getInteger("params") * amount);
            gainPayService.pay(lord, itemId, amount);
        }
        // 计算经验值升级等级
        this.addExp(lord, hero, exp);

        // 新手引导记录步数
        int step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        lordRepository.save(lord);

        Object obj = this.gameModel.getModel(ResponseKey.HEROES);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put(heroUid, hero);
        this.gameModel.addObject(ResponseKey.HEROES, map);
    }

    private void addExp(Lord lord, Hero hero, int exp) {
        // 根据武将好感度增加武将经验值
        // int loveLevel = hero.getLoveLevel();
        // DataConfig dataConfig = getDataConfig().get("loveup").get(hero.getHeroId()).get("love").get(loveLevel + "");
        // if (dataConfig != null) {
        // DataConfig attrConfig = dataConfig.get("attr");
        // if (attrConfig != null) {
        // Integer increase = (Integer) attrConfig.getJsonObject().get("exp");
        // if (increase != null) {
        // exp *= (increase / 100 + 1);
        // }
        // }
        // }
        Map<String, Object> addExpHero = (Map<String, Object>) this.gameModel.getModel("addExpHero");
        if (addExpHero == null) {
            addExpHero = new HashMap<>();
        }
        addExpHero.put(hero.getHeroUid(), exp);
        this.gameModel.addObject(ResponseKey.ADD_EXP_HERO, addExpHero);

        int limit = lord.getLevel() * MULTIPLE_LIMIT;
        // 计算经验
        exp += hero.getExp();
        int heroLv = hero.getLevel();
        JSONObject config = this.getDataConfig().get("expHero").getJsonObject();
        double expData = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getDouble("expData");
        for (int i = hero.getLevel(); i < config.size(); i++) {
            int need = (int) Math.ceil(((JSONObject) config.get(i + "")).getInt("exp") * expData);
            if (exp < need) {
                break;
            }
            heroLv += 1;
            exp -= need;
        }
        if (heroLv > limit) {
            heroLv = limit;
            exp = ((JSONObject) config.get(Integer.toString(heroLv))).getInt("exp") - 1;
        }
        // 满级清零
        if (hero.getLevel() == config.size()) {
            exp = 0;
        }

        hero.setLevel(heroLv);
        hero.setExp(exp);
    }

    public void addHeroExp(Lord lord, String heroUid, int exp) {
        Hero hero = lord.getHeros().get(heroUid);

        int limit = lord.getLevel() * MULTIPLE_LIMIT;
        if (hero.getLevel() >= limit) {
            return;
        }

        JSONObject config = this.getDataConfig().get("expHero").getJsonObject();
        if (hero.getLevel() >= config.size()) {
            return;
        }
        // 计算升级
        this.addExp(lord, hero, exp);
    }

    public Hero newHero(String heroId) {
        this.checkHero(heroId);
        Hero hero = new Hero();
        hero.setExp(0);
        hero.setHeroUid(heroId + "_" + UuidUtils.getUuid());
        hero.setHeroId(heroId);
        hero.setLevel(1);
        hero.setBreakLevel(0);
        hero.setTime(System.currentTimeMillis());

        String defaultSkill = this.getDataConfig().get("heroConfig").get(heroId).getString("skillult");
        if (StringUtils.isNotEmpty(defaultSkill)) {
            hero.setDefaultSkill(skillService.newSkill(defaultSkill));
        }
        return hero;
    }

    public void breakHero(String heroUid, List<String> cost) {
        Lord lord = this.getLord();
        // 校验等级
        this.isLevelOpen(ConfigKey.HERO_BREAK, lord, true);
        this.isHave(lord, heroUid);

        Map<String, Hero> heros = lord.getHeros();
        Hero hero = heros.get(heroUid);
        // 校验武将是否可以突破
        int isBreak = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getInteger("isRankup");
        if (isBreak != 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24004);
        }

        // 觉醒武将不可突破
        int awake = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getInteger("isStarup");
        if (awake == 2) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24005);
        }

        // 校验武将等级
        int breakLv = hero.getBreakLevel();
        JSONObject config = this.getDataConfig().get("rankUp").get(hero.getHeroId()).get("rank").getJsonObject();
        if (breakLv >= config.size()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24006);
        }
        int limitLv = ((JSONObject) config.get((breakLv + 1) + "")).getInt("lv");
        if (hero.getLevel() < limitLv) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24007);
        }

        // 校验突破材料
        int costHero = ((JSONObject) config.get((breakLv + 1) + "")).getInt("costhero");
        if (cost.size() != costHero) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24008);
        }
        String heroType = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getString("heroLame");
        for (String uid : cost) {
            this.isHave(lord, uid);
            Hero h = heros.get(uid);
            String t = this.getDataConfig().get("heroConfig").get(h.getHeroId()).getString("heroLame");
            // 校验是否为同名武将
            if (!heroType.equals(t)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24009);
            }
            gainPayService.pay(lord, uid, 1);
        }
        int costCoin = ((JSONObject) config.get((breakLv + 1) + "")).getInt("cost");
        gainPayService.pay(lord, ItemID.BREAKCOIN, costCoin);

        hero.setBreakLevel(breakLv + 1);
        lordRepository.save(lord);

        Object obj = this.gameModel.getModel(ResponseKey.HEROES);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put(heroUid, hero);
        this.gameModel.addObject(ResponseKey.HEROES, map);
    }

    private Hero isHave(Lord lord, String heroUid) {
        Map<String, Hero> heros = lord.getHeros();
        if (!heros.containsKey(heroUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23001);
        }
        return heros.get(heroUid);
    }

    public void awake(String heroUid, List<String> cost) {
        Lord lord = this.getLord();
        // 校验等级
        this.isLevelOpen(ConfigKey.AWAVE, lord, true);
        this.isHave(lord, heroUid);

        Map<String, Hero> heros = lord.getHeros();
        Hero hero = heros.get(heroUid);

        // 校验武将是否可以觉醒
        int isAwake = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getInteger("isStarup");
        if (isAwake != 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24010);
        }

        // 校验武将是否满足觉醒条件
        int lvLimit = this.getDataConfig().get("starUp").get(hero.getHeroId()).getInteger("starupLv");
        int amount = this.getDataConfig().get("starUp").get(hero.getHeroId()).getInteger("samecardCost");
        int breakLv = this.getDataConfig().get("starUp").get(hero.getHeroId()).getInteger("samestarRankup");
        if (hero.getLevel() < lvLimit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24011);
        }
        if ((cost.size() - 1) != amount) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24012);
        }

        // 取出最有一个，最后一个是特殊武将
        String condition = cost.get(cost.size() - 1);
        cost.remove(cost.size() - 1);

        String heroType = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getString("heroLame");
        for (String uid : cost) {
            this.isHave(lord, uid);
            Hero h = heros.get(uid);
            String t = this.getDataConfig().get("heroConfig").get(h.getHeroId()).getString("heroLame");
            if (!t.equals(heroType)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24013);
            }
            gainPayService.pay(lord, uid, 1);
        }

        // 校验下特殊材料
        this.isHave(lord, condition);
        Hero special = heros.get(condition);
        int heroStar = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getInteger("star");
        int specailStar = this.getDataConfig().get("heroConfig").get(special.getHeroId()).getInteger("star");
        if (heroStar != specailStar) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24014);
        }
        if (special.getBreakLevel() < breakLv) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24015);
        }

        gainPayService.pay(lord, condition, 1);

        // 更换觉醒ID
        String awakeHero = this.getDataConfig().get("heroConfig").get(hero.getHeroId()).getString("starupTar");
        hero.setHeroId(awakeHero);

        lordRepository.save(lord);

        Object obj = this.gameModel.getModel(ResponseKey.HEROES);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put(heroUid, hero);
        this.gameModel.addObject(ResponseKey.HEROES, map);
    }

    /**
     * @Description:提升高感度
     * @param heroId
     * @param items
     * @throws
     */
    public void addHeroRomanceExp(String heroId, JSONObject items) {
        Lord lord = getLord();
        Romance romance = lord.getRomance().get(heroId);
        if (romance == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24022, "玩家从未获得过该类型武将");
        }
        int romanceLevel = romance.getLevel();
        int romanceExp = romance.getExp();
        // 检查好感度最高等级
        DataConfig heroRomanceConfig = getDataConfig().get(ConfigKey.ROMANCE_BASE).get(heroId);
        if (heroRomanceConfig.getInteger(ConfigKey.ROMANCE_BASE_MAXLV) <= romanceLevel) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24020, "好感度等级已经最高");
        }

        // 好感度道具校验
        DataConfig dataConfig = heroRomanceConfig.get(ConfigKey.ROMANCE_BASE_ROMANCE).get(romanceLevel + 1 + "")
                .get(ConfigKey.ROMANCE_BASE_ITEM);
        JSONArray itemsJson = dataConfig.getJsonArray();
        DataConfig itemConfig = getDataConfig().get(ConfigKey.ITEM);
        Set<String> itemKeySet = items.keySet();
        for (String itemId : itemKeySet) {
            Integer rank = itemConfig.get(itemId).getInteger(ConfigKey.ITEM_RANK);
            if (!itemsJson.contains(rank)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24016, "该道具不能使用");
            }
            gainPayService.pay(lord, itemId, items.getInt(itemId));
            romanceExp += itemConfig.get(itemId).getInteger(ConfigKey.ITEM_PARAMS) * items.getInt(itemId);
        }
        // 加经验
        gainPayService.gain(lord, ItemID.ROMANCE_EXP + "--" + heroId, romanceExp);
        lordRepository.save(lord);

    }

    /**
     * @Description:
     * @param lord
     * @throws
     */
    public void setFormHoldFP(Lord lord) {
        if (lord.getForm().size() <= 0) {
            return;
        }
        List<FormHold> formList = lord.getForm().get(0);
        if (formList == null) {
            return;
        }
        for (FormHold form : formList) {
            if (form == null || StringUtils.isEmpty(form.getHeroUid())) {
                continue;
            }
            Hero hero = lord.getHeros().get(form.getHeroUid());
            HeroInfo heroInfo = this.getHeroInfo(lord, hero, true);
            form.setFP(this.getHeroFP(heroInfo));
        }
        // lord.getForm().set(0, formList);
    }

    /**
     * @Description:
     * @param lord
     * @throws
     */
    public void setFormHoldFP(Lord lord, Duel duel) {
        List<FormHold> formList = duel.getFormDefend();
        if (formList == null) {
            return;
        }
        for (FormHold form : formList) {
            if (form == null || StringUtils.isEmpty(form.getHeroUid())) {
                continue;
            }
            Hero hero = lord.getHeros().get(form.getHeroUid());
            HeroInfo heroInfo = this.getHeroInfo(lord, hero, true);
            form.setFP(this.getHeroFP(heroInfo));
        }
        // lord.getForm().set(0, formList);
    }

    /**
     * @Description:
     * @param duelRobot
     * @throws
     */
    public void setFormHoldFPRobot(DuelRobot duelRobot) {
        List<FormHold> formList = duelRobot.getFormDefend();
        for (FormHold form : formList) {
            if (form == null || StringUtils.isEmpty(form.getHeroUid())) {
                continue;
            }
            Hero hero = duelRobot.getHero().get(form.getHeroUid());
            HeroInfo heroInfo = this.getHeroInfoRobot(duelRobot, hero, true);
            form.setFP(this.getHeroFP(heroInfo));
        }
    }

    /**
     * @Description:
     * @param lord
     * @param hero
     * @param isFormHold
     * @return
     * @throws
     */
    public HeroInfo getHeroInfoRobot(DuelRobot duelRobot, Hero hero, boolean isFormHold) {
        HeroInfo heroInfo = new HeroInfo();
        heroInfo.setHero(hero);
        List<FormHold> formList = duelRobot.getFormDefend();
        if (formList == null) {
            formList = new ArrayList<>();
        }
        List<Equip> equipList = new ArrayList<>();
        List<Skill> skillList = new ArrayList<>();
        if (isFormHold) {
            for (FormHold form : formList) {
                if (hero.getHeroUid().equals(form.getHeroUid())) {
                    List<String> strEquip = form.getEquipUid();
                    for (String equipUid : strEquip) {
                        equipList.add(duelRobot.getEquips().get(equipUid));
                    }
                    // 增加默认技能
                    if (hero.getDefaultSkill() != null) {
                        skillList.add(hero.getDefaultSkill());
                    }
                }
            }
        }
        heroInfo.setEquipList(equipList);
        heroInfo.setSkillList(skillList);
        return heroInfo;
    }

    /**
     * @Description:
     * @param lord
     * @param hero
     * @param isFormHold 是否是阵容战斗力 true：阵容中英雄战斗力，false:英雄战斗力
     * @return
     * @throws
     */
    public HeroInfo getHeroInfo(Lord lord, Hero hero, boolean isFormHold) {
        HeroInfo heroInfo = new HeroInfo();
        heroInfo.setHero(hero);
        List<FormHold> formList = lord.getForm().get(0);
        if (formList == null) {
            formList = new ArrayList<>();
        }
        List<Equip> equipList = new ArrayList<>();
        List<Skill> skillList = new ArrayList<>();
        if (isFormHold) {
            for (FormHold form : formList) {
                if (hero.getHeroUid().equals(form.getHeroUid())) {
                    List<String> strEquip = form.getEquipUid();
                    for (String equipUid : strEquip) {
                        equipList.add(lord.getEquips().get(equipUid));
                    }
                    List<String> strSkill = form.getSkillUid();
                    for (String skillUid : strSkill) {
                        skillList.add(lord.getSkills().get(skillUid));
                    }
                    // 增加默认技能
                    if (hero.getDefaultSkill() != null) {
                        skillList.add(hero.getDefaultSkill());
                    }
                }
            }
        }
        heroInfo.setEquipList(equipList);
        heroInfo.setSkillList(skillList);
        return heroInfo;
    }

    /**
     * @Description: 英雄战斗力
     * @param hero
     * @throws
     */
    public int getHeroFP(HeroInfo heroInfo) {
        Hero hero = heroInfo.getHero();
        DataConfig configHero = this.getDataConfig().get(CONFIG_HERO_CONFIG).get(hero.getHeroId());
        DataConfig configFightPower = this.getDataConfig().get(CONFIG_FIGHT_POWER);
        // 初始化战斗力
        double initFP = configHero.getDouble("fightPowerOrigin");
        // 等级战斗力
        double lvFP = Math.ceil(hero.getLevel() * configFightPower.get("lv").getDouble("coe"));
        // 技能、装备 额外增加属性
        Map<String, Map<String, Double>> skillExtraAttrs = new HashMap<>();

        // 技能战斗力
        double skillFP = 0;
        // 技能提升属性
        for (Skill skill : heroInfo.getSkillList()) {
            if (skill == null) {
                continue;
            }
            skillFP += this.getSkillFP(skill, skillExtraAttrs);
        }

        // 技能、装备 额外增加属性
        Map<String, Map<String, Double>> equipExtraAttrs = new HashMap<>();
        // 装备提升属性
        for (Equip equip : heroInfo.getEquipList()) {
            if (equip == null) {
                continue;
            }
            this.getEquipFP(equip, equipExtraAttrs);
        }

        // 属性战斗力
        double attrFP = 0;
        // 主属性战斗力
        double mainAttrFP = 0;
        // 副属性战斗力
        double lesserFP = 0;

        // 主属性包括 攻、防、血、内
        Map<String, String> mainAttrMap = new HashMap<>();
        mainAttrMap.put("atk", null);
        mainAttrMap.put("def", null);
        mainAttrMap.put("hp", null);
        mainAttrMap.put("mp", null);
        // 基础属性
        Map<String, Double> daseAttr = this.getHoreBaseAttrs(hero);
        Set<String> set = daseAttr.keySet();
        for (String attrValue : set) {
            if (!configFightPower.getJsonObject().containsKey(attrValue)) {
                continue;
            }
            // 基础属性
            double fp = daseAttr.get(attrValue);
            // 技能、装备提升属性
            double add = 0; // 属性加成
            double coe = 0; // 属性加成百分比
            if (skillExtraAttrs.containsKey(attrValue)) {
                add += skillExtraAttrs.get(attrValue).get("add");
                coe += skillExtraAttrs.get(attrValue).get("coe");
            }
            if (equipExtraAttrs.containsKey(attrValue)) {
                add += equipExtraAttrs.get(attrValue).get("add");
                coe += equipExtraAttrs.get(attrValue).get("coe");
            }
            fp = (fp + add) * (1 + coe);

            if (mainAttrMap.containsKey(attrValue)) {
                // 主属性
                mainAttrFP += fp * configFightPower.get(attrValue).getDouble("coe");
            } else {
                // 公式计算副属性
                switch (attrValue) {
                case "cridmg":
                    fp = (fp - 1.5) < 0 ? 0 : (fp - 1.5);
                    break;
                case "apeff":
                    fp = (1 - fp) < 0 ? 0 : (1 - fp);
                    break;
                case "speff":
                    fp = (1 - fp) < 0 ? 0 : (1 - fp);
                    break;
                case "healeff":
                    fp = (fp - 1) < 0 ? 0 : (fp - 1);
                    break;
                default:
                    break;
                }
                lesserFP += fp * configFightPower.get(attrValue).getDouble("coe");
            }

        }
        attrFP = mainAttrFP * (1 + lesserFP);
        int heroFP = (int) Math.ceil(initFP + lvFP + attrFP + skillFP);
        logger.info("技能属性加层！！！---" + JSONObject.fromObject(skillExtraAttrs).toString());
        logger.info("装备属性加层！！！---" + JSONObject.fromObject(equipExtraAttrs).toString());
        logger.info("技能属性加层！！！---" + JSONObject.fromObject(daseAttr).toString());
        logger.info(hero.getHeroId() + "/初始化战斗力/" + initFP);
        logger.info(hero.getHeroId() + "/等级战斗力/" + lvFP);
        logger.info(hero.getHeroId() + "/技能战斗力/" + skillFP);
        logger.info(hero.getHeroId() + "/主属性战斗力/" + mainAttrFP);
        logger.info(hero.getHeroId() + "/副战斗力/" + lesserFP);
        logger.info(hero.getHeroId() + "/属性战斗力/" + attrFP);

        return (int) Math.ceil(heroFP);
    }

    /**
     * @Description:英雄基础属性
     * @param hero
     * @param attrValue
     * @param configAttr
     * @throws
     */
    private Map<String, Double> getHoreBaseAttrs(Hero hero) {
        DataConfig configAttr = this.getDataConfig().get(ConfigKey.HERO_CONFIG).get(hero.getHeroId()).get("attr");
        DataConfig configRank = this.getDataConfig().get(ConfigKey.RANK_UP);
        int breakLevel = hero.getBreakLevel();
        Map<String, Double> attrs = new HashMap<>();
        Iterator<String> itAttr = configAttr.getJsonObject().keys();
        while (itAttr.hasNext()) {
            String attrValue = itAttr.next();
            double d1 = 0;
            double d2 = 0;
            if (configAttr.get(attrValue).getJsonObject().containsKey(0 + "")) {
                d1 = configAttr.get(attrValue).getDouble(0 + "");
            }
            if (configAttr.get(attrValue).getJsonObject().containsKey(1 + "")) {
                d2 = configAttr.get(attrValue).getDouble(1 + "");
            }
            double fp = d1 + d2 * (hero.getLevel() - 1);
            attrs.put(attrValue, fp); // 基础属性
        }

        if (breakLevel >= 0 && configRank.getJsonObject().containsKey(hero.getHeroId())) {
            DataConfig rankUp = configRank.get(hero.getHeroId()).get("rank");
            for (int i = breakLevel; i > 0; i--) {
                if (!rankUp.getJsonObject().containsKey(breakLevel + "")) {
                    continue;
                } else {
                    if (rankUp.get(breakLevel + "").getJsonObject().containsKey("attr")) {
                        continue;
                    }
                    DataConfig attConfig = rankUp.get(breakLevel + "").get("attr");
                    if (attConfig == null) {
                        continue;
                    }
                    Iterator<String> it = attConfig.getJsonObject().keys();
                    while (it.hasNext()) {
                        String attr = it.next();
                        if (attrs.containsKey(attr)) {
                            attrs.put(attr, attrs.get(attr) + attConfig.getDouble(attr));
                        } else {
                            attrs.put(attr, attConfig.getDouble(attr));
                        }
                    }
                }
            }
        }

        return attrs;

    }

    /**
     * @Description: 装备战斗力\提升属性
     * @param equip
     * @param equipFP
     * @param extraAttr
     * @throws
     */
    private void getEquipFP(Equip equip, Map<String, Map<String, Double>> extraAttr) {
        DataConfig config = this.getDataConfig().get(CONFIG_EQUIP).get(equip.getEquipId());

        if (config.getJsonObject().containsKey("attr")) {
            DataConfig attrConfig = config.get("attr");
            List<Map<String, Object>> itAttr = attrConfig.getJsonArray();
            for (Map<String, Object> map : itAttr) {
                Map<String, Double> equipAttr = new HashMap<>();
                String type = map.get("type").toString();
                double coe = 0;
                double add = 0;
                if (map.containsKey("value") && map.get("value") != null) {
                    add = JSONArray.fromObject(map.get("value")).getDouble(0)
                            + JSONArray.fromObject(map.get("value")).getDouble(1) * (equip.getLevel() - 1)
                            + JSONArray.fromObject(map.get("value")).getDouble(2) * (equip.getStage());
                }
                if (map.containsKey("percent") && map.get("percent") != null) {
                    coe = JSONArray.fromObject(map.get("percent")).getDouble(0)
                            + JSONArray.fromObject(map.get("percent")).getDouble(1) * (equip.getLevel() - 1)
                            + JSONArray.fromObject(map.get("percent")).getDouble(2) * (equip.getStage());
                }

                if (extraAttr.containsKey(type)) {
                    coe += extraAttr.get(type).get("coe");
                    add += extraAttr.get(type).get("add");
                }
                equipAttr.put("coe", coe);
                equipAttr.put("add", add);
                extraAttr.put(type, equipAttr);
            }
        }
    }

    /**
     * @Description: 技能战斗力\提升属性
     * @param skill
     * @param skillFP
     * @param skillAttr
     * @throws
     */
    private double getSkillFP(Skill skill, Map<String, Map<String, Double>> skillAttrs) {
        double skillFP = 0.0;
        DataConfig config = this.getDataConfig().get(CONFIG_SKILL).get(skill.getSkillId());
        if (config.getJsonObject().containsKey("fightPower")) {
            DataConfig configFightPower = config.get("fightPower");
            skillFP += configFightPower.getDouble(0) + configFightPower.getDouble(1) * skill.getLevel();
        }
        if (config.getJsonObject().containsKey("attr") && "psv".equals(config.getString("type"))) {
            DataConfig attrConfig = config.get("attr");
            List<Map<String, Object>> itAttr = attrConfig.getJsonArray();
            for (Map<String, Object> map : itAttr) {
                Map<String, Double> skillAttr = new HashMap<>();
                String type = map.get("type").toString();
                double coe = 0;
                double add = 0;
                if (map.containsKey("coe") && map.get("coe") != null) {
                    coe = JSONArray.fromObject(map.get("coe")).getDouble(0)
                            + JSONArray.fromObject(map.get("coe")).getDouble(1) * (skill.getLevel() - 1);
                }
                if (map.containsKey("add") && map.get("add") != null) {
                    add = JSONArray.fromObject(map.get("add")).getDouble(0)
                            + JSONArray.fromObject(map.get("add")).getDouble(1) * (skill.getLevel() - 1);
                }

                if (skillAttrs.containsKey(type)) {
                    coe += skillAttrs.get(type).get("coe");
                    add += skillAttrs.get(type).get("add");
                }
                skillAttr.put("coe", coe);
                skillAttr.put("add", add);
                skillAttrs.put(type, skillAttr);
            }
        }
        return skillFP;
    }

    private int intValue(Integer integer) {
        if (integer == null)
            return 0;
        return integer;
    }

    /**
     * @Description:好感度添加武将记录
     * @param heroId  
     * @param lord
     * @throws
     */
    public void romanceAddHero(String heroId, Lord lord) {
        Map<String, Romance> romance = lord.getRomance();
        if (romance == null) {
            romance = new HashMap<>();
            lord.setRomance(romance);
        }
        checkHero(heroId);
        if (!romance.containsKey(heroId)) {
            Romance newRomance = new Romance();
            romance.put(heroId, newRomance);
            HashMap<Object, Object> updateMap = new HashMap<>();
            Object model = this.gameModel.getModel(ResponseKey.ROMANCE);
            if (model != null) {
                updateMap = (HashMap<Object, Object>) model;
            }
            updateMap.put(heroId, newRomance);
            this.gameModel.addObject(ResponseKey.ROMANCE, updateMap);
        }
    }

    /**
     * @Description:好感度剧情
     * @param type 剧情类型 0：等级剧情 1：随机剧情
     * @param state 剧情的状态： 0：未进入剧情   1：进入剧情中间退出     2：剧情完成（解锁该等级立绘）
     * @param romanceLevel 好感度等级 （随机剧情-1）
     * @param heroUid 武将uid
     * @return
     * @throws
     */
    public void romanceStory(Integer type, Integer state, Integer romanceLevel, String heroId) {
        Lord lord = getLord();
        if (Romance.STORY_TYPE_LEVEL == type) {
            // 等级剧情
            // 校验好感度等级
            Map<String, Romance> romanceMap = lord.getRomance();
            Romance romance = romanceMap.get(heroId);
            if (romance == null) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24022, "玩家从未获得过该类型武将");
            }
            if (romanceLevel > romance.getLevel()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24021, "好感度等级不够");
            }
            Map<Integer, Integer> storyMap = romance.getStory();
            if (state == Romance.STORY_STATE_END && storyMap.get(romanceLevel) != Romance.STORY_STATE_END) {// 剧情完成
                // 发放奖励
                DataConfig awardConfig = getDataConfig().get(ConfigKey.ROMANCE_BASE).get(heroId)
                        .get(ConfigKey.ROMANCE_BASE_ROMANCE).get(romanceLevel + "").get(ConfigKey.ROMANCE_BASE_AWARD);
                if (awardConfig != null) {
                    JSONObject jsonObject = awardConfig.getJsonObject();
                    Set<String> keySet = jsonObject.keySet();
                    for (String itemId : keySet) {
                        gainPayService.gain(lord, itemId, jsonObject.getInt(itemId));
                    }

                }
            }
            storyMap.put(romanceLevel, state);

            Map<String, Romance> updateRomance = new HashMap<>();
            updateRomance.put(heroId, romance);
            this.gameModel.addObject(ResponseKey.ROMANCE, updateRomance);
        } else if (Romance.STORY_TYPE_RANDOM == type) {
            // 随机剧情
            Map<String, Integer> randomStory = lord.getRomanceRandomStory();

            Set<String> heroIdSet = randomStory.keySet();
            String storyId = "";// 剧情id
            for (String string : heroIdSet) {
                storyId = string;
            }
            Integer oldState = randomStory.get(storyId);
            if (state == Romance.STORY_STATE_END && oldState != Romance.STORY_STATE_END) {
                // 发送奖励
                getRomanceTHeaterAward(lord);
                // 记录随机剧情历史记录
                recordRomanceRandomStory(lord);
            }
            randomStory.put(storyId, state);
            this.gameModel.addObject(ResponseKey.ROMANCE_RANDOM_STORY, randomStory);
        }
        lordRepository.save(lord);
    }

    /**
     * @Description:计算好感度随机剧情，通过奖励
     * @param lord
     * @throws
     */
    private void getRomanceTHeaterAward(Lord lord) {
        int weightTotal = 0;
        DataConfig awardsConfig = getDataConfig().get(ConfigKey.ROMANCE_THRETER_AWARD).get(lord.getLevel() + "")
                .get(ConfigKey.ROMANCE_THRETER_AWARD_AND_WEIGHT);
        List<Map<String, Object>> awardAndWeightList = new ArrayList<>();
        for (int i = 1;; i++) {
            DataConfig weightConfig = awardsConfig.get(i + "");
            if (weightConfig == null) {
                break;
            }
            Map<String, Object> awardAndWeightMap = new HashMap<>();
            awardAndWeightMap.put(ConfigKey.ROMANCE_THRETER_AWARD_ITEMID,
                    weightConfig.getString(ConfigKey.ROMANCE_THRETER_AWARD_ITEMID));
            awardAndWeightMap.put(ConfigKey.ROMANCE_THEATERID_WEIGHT,
                    weightConfig.getInteger(ConfigKey.ROMANCE_THEATERID_WEIGHT));
            awardAndWeightMap.put(ConfigKey.ROMANCE_THRETER_AWARD_NUM,
                    weightConfig.getInteger(ConfigKey.ROMANCE_THRETER_AWARD_NUM));
            weightTotal += weightConfig.getInteger(ConfigKey.ROMANCE_THRETER_AWARD_WEIGHT);
            awardAndWeightList.add(awardAndWeightMap);
        }
        // 随机数
        int random = RandomUtils.randomInt(0, weightTotal);
        weightTotal = 0;
        for (Map<String, Object> map : awardAndWeightList) {
            weightTotal += (int) map.get(ConfigKey.ROMANCE_THRETER_AWARD_WEIGHT);
            if (weightTotal >= random) {
                gainPayService.gain(lord, (String) map.get(ConfigKey.ROMANCE_THRETER_AWARD_ITEMID),
                        (int) map.get(ConfigKey.ROMANCE_THRETER_AWARD_NUM));
                break;
            }
        }
    }

    /**
     * @Description:记录随机剧情历史记录
     * @param lord
     * @throws
     */
    private void recordRomanceRandomStory(Lord lord) {
        Map<String, Integer> curStory = lord.getRomanceRandomStory();

        Map<String, Romance> romanceMap = lord.getRomance();
        for (String storyId : curStory.keySet()) {
            Integer state = curStory.get(storyId);
            if (state == Romance.STORY_STATE_END) {
                Romance romance = romanceMap.get(storyId);
                Set<String> randomStoryRecord = romance.getRandomStoryRecord();
                if (randomStoryRecord == null) {
                    randomStoryRecord = new HashSet<>();
                    romance.setRandomStoryRecord(randomStoryRecord);
                }
                randomStoryRecord.add(storyId);
            }
        }
    }

    /**
     * @Description:获取武将id
     * @param heroUid
     * @param lord
     * @return
     * @throws
     */
    private String getHeroId(String heroUid, Lord lord) {
        Map<String, Hero> heros = lord.getHeros();
        Hero hero = heros.get(heroUid);
        if (hero == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, "武将uid不存在");
        }
        return hero.getHeroId();
    }

    /**
     * @Description:选择武将默认立绘图
     * @param level
     * @param heroId
     * @throws
     */
    public void romanceAddpic(Integer level, String heroId) {
        Lord lord = getLord();
        Map<String, Romance> romanceMap = lord.getRomance();
        Romance romance = romanceMap.get(heroId);
        // 好感度等级校验
        if (romance.getLevel() < level || level < 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24021, "好感度等级不够");
        }
        romance.setAddpic(level);
        HashMap<Object, Object> updateRomance = new HashMap<>();
        updateRomance.put(heroId, romance);
        this.gameModel.addObject(ResponseKey.ROMANCE, updateRomance);
        lordRepository.save(lord);
    }

}
