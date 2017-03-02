/**
 * @Title: SkillService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月21日 闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.UuidUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: SkillService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月21日 下午12:50:31
 */

@Service
public class SkillService extends GameSupport {
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private LordRepository lordRepository;

    private void checkSkill(String skillId) {
        DataConfig config = this.getDataConfig().get("skill").get(skillId);
        if (null == config) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, "NOT FOUND " + skillId);
        }
    }

    public Skill newSkill(String skillId) {
        this.checkSkill(skillId);
        Skill skill = new Skill();
        skill.setExp(0);
        skill.setLevel(1);
        skill.setSkillId(skillId);
        skill.setSkillUid(skillId + "_" + UuidUtils.getUuid());
        skill.setTime(System.currentTimeMillis());
        return skill;
    }

    private void isHave(Lord lord, String skillUid) {
        Map<String, Skill> skills = lord.getSkills();
        if (!skills.containsKey(skillUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23007);
        }
    }

    /**
     * @Description:技能升级
     * @param skillUid
     * @param cost
     * @throws
     */
    public void addExp(String skillUid, List<String> cost) {
        Lord lord = this.getLord();
        // 新手引导记录步数
        int step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        Map<String, Skill> skills = lord.getSkills();
        if (skills.containsKey(skillUid)) {
            this.addSkillExp(skillUid, cost, lord);
        } else {
            this.addDefaultSkillExp(skillUid, cost, lord);
        }

    }

    private void addSkillExp(String skillUid, List<String> cost, Lord lord) {
        Map<String, Skill> skills = lord.getSkills();
        int exp = skills.get(skillUid).getExp();
        int preLevel = skills.get(skillUid).getLevel();
        String skillId = skills.get(skillUid).getSkillId();
        Map<String, Integer> mapExp = this.getExp(cost, lord, preLevel, exp, skillId);
        skills.get(skillUid).setExp(mapExp.get("exp"));
        skills.get(skillUid).setLevel(mapExp.get("postLevel"));
        lord.setSkills(skills);
        Object obj = this.gameModel.getModel(ResponseKey.SKILLS);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put(skillUid, skills.get(skillUid));
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SKILLS, map);
    }

    private void addDefaultSkillExp(String skillUid, List<String> cost, Lord lord) {
        Map<String, Skill> skills = lord.getSkills();
        Map<String, Hero> heros = lord.getHeros();
        Iterator it = heros.keySet().iterator();
        String skillId = "";
        int preLevel = 0;
        int exp = 0;
        String heroUid = "";
        Skill skill = new Skill();
        while (it.hasNext()) {
            heroUid = it.next().toString();
            skill = heros.get(heroUid).getDefaultSkill();
            if (skill != null) {
                if (skillUid.equals(skill.getSkillUid())) {
                    skillId = skill.getSkillId();
                    preLevel = skill.getLevel();
                    exp = skill.getExp();
                    break;
                }
            }
        }
        if (skillId == "" || heroUid == "") {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23007);
        }
        Map<String, Integer> map = this.getExp(cost, lord, preLevel, exp, skillId);
        skill.setExp(map.get("exp"));
        skill.setLevel(map.get("postLevel"));
        Hero hero = heros.get(heroUid);
        hero.setDefaultSkill(skill);
        heros.put(heroUid, hero);
        lord.setHeros(heros);
        Object obj = this.gameModel.getModel(ResponseKey.SKILLS);
        Map<String, Object> mapSkill = new HashMap<String, Object>();
        if (obj != null) {
            mapSkill = (Map<String, Object>) obj;
        }
        // mapSkill.put(heroUid, hero);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SKILLS, mapSkill);
        Map<String, Object> mapHero = new HashMap<String, Object>();
        mapHero.put(heroUid, hero);
        this.gameModel.addObject(ResponseKey.HEROES, mapHero);
    }

    private Map<String, Integer> getExp(List<String> cost, Lord lord, int preLevel, int exp, String skillId) {
        int postLevel = preLevel;
        DataConfig skillConfig = this.getDataConfig().get("skill");
        DataConfig expSkillConfig = this.getDataConfig().get("expSkill");

        for (String uid : cost) {
            if (StringUtils.startsWith(uid, ItemID.SKILL)) {// 消耗技能
                this.isHave(lord, uid);
                exp += this.getExpAll(lord, uid, skillConfig, expSkillConfig);
            } else {// 消耗经验道具
                this.isHaveItem(lord, uid);
                exp += this.getItemExp(lord, uid);
            }
        }
        for (String uid : cost) {
            gainPayService.pay(lord, uid, 1);
        }

        String rank = Integer.toString(skillConfig.get(skillId).getInteger("rank"));
        JSONObject lv = expSkillConfig.get(rank).get("lv").getJsonObject();
        if (preLevel >= lv.size()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_25000);
        }
        for (int i = preLevel; i < lv.size(); i++) {
            int tempExp = lv.getInt(Integer.toString(i));
            if (tempExp <= exp) {
                exp -= tempExp;
                postLevel++;
            }
        }
        // 满级当前经验清零
        if (postLevel >= lv.size()) {
            exp = 0;
            postLevel = lv.size();
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("postLevel", postLevel);
        map.put("exp", exp);
        return map;
    }

    /**
     * @Description:计算该经验道具增加的经验值
     * @param lord
     * @param uid
     * @return
     * @throws
     */
    private int getItemExp(Lord lord, String item) {
        DataConfig configItems = getDataConfig().get(ConfigKey.ITEM);
        if (configItems.getJsonObject().containsKey(item)) {
            DataConfig configItem = configItems.get(item);
            if (ItemID.SKILL_TYPE.equals(configItem.getString(ConfigKey.ITEM_TYPE))) {
                return configItem.getInteger(ConfigKey.ITEM_PARAMS);
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24016, "道具不是技能经验道具不能使用");
            }
        } else {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, "道具id不存在");
        }
        return 0;
    }

    /**
     * @Description:
     * @param lord
     * @param uid
     * @throws
     */
    private void isHaveItem(Lord lord, String item) {
        Map<String, Integer> items = lord.getItems();
        if (!items.containsKey(item)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28001, "没有该道具");
        }
    }

    /**
     * @Description:
     * @throws
     */
    private int getExpAll(Lord lord, String skillUid, DataConfig skillConfig, DataConfig expSkillConfig) {
        int score = 0;
        Map<String, Skill> skills = lord.getSkills();
        int exp = skills.get(skillUid).getExp();
        int level = skills.get(skillUid).getLevel();
        String id = skills.get(skillUid).getSkillId();
        int expProvide = skillConfig.get(id).getInteger("lvupexpProvide");
        String rank = Integer.toString(skillConfig.get(id).getInteger("rank"));
        double percent = expSkillConfig.get(rank).getDouble("percent");
        for (int i = level - 1; i > 0; i--) {
            score += expSkillConfig.get(rank).get("lv").getInteger(Integer.toString(i));
        }
        score = (int) Math.ceil(score * percent);
        score += exp + expProvide;
        return score;
    }

}
