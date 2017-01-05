/**
 * @Title: HeroService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月21日 闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.config.ConfigKey;
import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.HeroInfo;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
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
        int loveLevel = hero.getLoveLevel();
        DataConfig dataConfig = getDataConfig().get("haoganup").get(hero.getHeroId()).get("haogan").get(loveLevel + "");
        if (dataConfig != null) {
            DataConfig attrConfig = dataConfig.get("attr");
            if (attrConfig != null) {
                Integer increase = (Integer) attrConfig.getJsonObject().get("epx");
                if (increase != null) {
                    exp *= (increase / 100 + 1);
                }
            }
        }
        Map<String, Object> addExpHero = (Map<String, Object>) this.gameModel.getModel("addExpHero");
        if (addExpHero == null) {
            addExpHero = new HashMap<>();
        }
        addExpHero.put(hero.getHeroUid(), exp);
        this.gameModel.addObject("addExpHero", addExpHero);

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

    private void isHave(Lord lord, String heroUid) {
        Map<String, Hero> heros = lord.getHeros();
        if (!heros.containsKey(heroUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23001);
        }
    }

    public void awake(String heroUid, List<String> cost) {
        Lord lord = this.getLord();
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
     * @param heroUid
     * @param items
     * @throws
     */
    public void addHeroLoveExp(String heroUid, JSONObject items) {
        Lord lord = getLord();
        Map<String, Hero> heros = lord.getHeros();
        // 英雄校验
        this.isHave(lord, heroUid);
        Hero hero = heros.get(heroUid);
        String heroId = hero.getHeroId();
        int loveLevel = hero.getLoveLevel();
        int loveExp = hero.getLoveExp();
        // 检查好感度最高等级
        DataConfig heroConfig = getDataConfig().get(ConfigKey.HERO_CONFIG).get(heroId);
        Integer star = heroConfig.getInteger(ConfigKey.HERO_CONFIG_STAR);
        Integer loveLevelMax = getDataConfig().get(ConfigKey.LOVEOPEN).get(star + "")
                .getInteger(ConfigKey.LOVEOPEN_LEVELMAX);
        if (loveLevelMax <= loveLevel) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24020, "好感度等级已经最高");
        }
        // 好感值道具校验
        Iterator<String> itemIds = items.keys();
        DataConfig heroLoveConfig = getDataConfig().get(ConfigKey.LOVEUP).get(heroId).get(ConfigKey.LOVEUP_LOVE)
                .get(loveLevel + 1 + "");
        int loveExpCost = intValue(heroLoveConfig.getInteger(ConfigKey.LOVEUP_COST));
        JSONArray itemsLimit = heroLoveConfig.get(ConfigKey.LOVEUP_ITEM).getJsonArray();
        DataConfig itemConfig = getDataConfig().get(ConfigKey.ITEM);
        while (itemIds.hasNext()) {
            String itemId = itemIds.next();
            if (!itemsLimit.contains(itemId)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24016, "该道具不能使用");
            }
            int amount = items.getInt(itemId);
            loveExp += amount * itemConfig.get(itemId).getInteger(ConfigKey.ITEM_PARAMS);
            gainPayService.pay(lord, itemId, amount);
        }
        // 加经验
        while (loveExp >= loveExpCost) {// 升级
            loveLevel++;
            loveExp -= loveExpCost;
            if (loveLevelMax <= loveLevel) {
                break;
            }
            loveExpCost = intValue(getDataConfig().get(ConfigKey.LOVEUP).get(heroId).get(ConfigKey.LOVEUP_LOVE)
                    .get(loveLevel + 1 + "").getInteger(ConfigKey.LOVEUP_COST));
        }

        hero.setLoveLevel(loveLevel);
        hero.setLoveExp(loveExp);
        heros.put(heroUid, hero);
        lord.setHeros(heros);

        lordRepository.save(lord);
        Map<String, Object> map = new HashMap<>();
        map.put(heroUid, hero);
        gameModel.addObject("heroes", map);

    }

    public HeroInfo getHeroInfo(Lord lord, Hero hero) {
        HeroInfo heroInfo = new HeroInfo();
        heroInfo.setHero(hero);
        List<FormHold> formList = lord.getForm().get(0);
        if (formList == null) {
            formList = new ArrayList<>();
        }
        List<Equip> equipList = new ArrayList<>();
        List<Skill> skillList = new ArrayList<>();
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
        int heroLevel = hero.getLevel();
        // 等级战斗力
        double lvFP = Math.ceil(hero.getLevel() * configFightPower.getDouble("lv"));

        // 技能、装备 额外增加属性
        Map<String, Double> extraAttr = new HashMap<>();

        // 技能战斗力
        double skillFP = 0;
        // 技能提升属性
        for (Skill skill : heroInfo.getSkillList()) {
            this.getSkillFP(skill, skillFP, extraAttr);
        }

        // 装备提升属性
        for (Equip equip : heroInfo.getEquipList()) {
            this.getEquipFP(equip, extraAttr);
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
        DataConfig configAttr = configHero.get("attr");
        Iterator<String> itAttr = configAttr.getJsonObject().keys();
        while (itAttr.hasNext()) {
            String attrValue = itAttr.next();
            double fp = 0;
            // 技能、装备提升属性
            if (extraAttr.containsKey(attrValue)) {
                fp += extraAttr.get(attrValue);
            }
            if (mainAttrMap.containsKey(attrValue)) {
                // 主属性
                fp += configAttr.get(attrValue).getDouble(0) + configAttr.get(attrValue).getDouble(1) * heroLevel;
                mainAttrFP += fp * configFightPower.get(attrValue).getDouble("coe");
            } else {
                // 副属性
                fp += configAttr.get(attrValue).getDouble(0) + configAttr.get(attrValue).getDouble(1) * heroLevel;

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

        return (int) Math.ceil(heroFP);
    }

    /**
     * @Description: 装备战斗力\提升属性
     * @param equip
     * @param equipFP
     * @param extraAttr
     * @throws
     */
    private void getEquipFP(Equip equip, Map<String, Double> extraAttr) {
        DataConfig config = this.getDataConfig().get(CONFIG_SKILL).get(equip.getEquipId());

    }

    /**
     * @Description: 技能战斗力\提升属性
     * @param skill
     * @param skillFP
     * @param skillAttr
     * @throws
     */
    private void getSkillFP(Skill skill, double skillFP, Map<String, Double> skillAttr) {
        DataConfig config = this.getDataConfig().get(CONFIG_SKILL).get(skill.getSkillId());
        if (config.getJsonObject().containsKey("fightPower")) {
            DataConfig configFightPower = config.get("fightPower");
            skillFP += configFightPower.getDouble(0) + configFightPower.getDouble(1) * skill.getLevel();
        }

    }

    private int intValue(Integer integer) {
        if (integer == null)
            return 0;
        return integer;
    }
}