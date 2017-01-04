/**
 * @Title: FormHoldService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月22日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.DuelRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

import net.sf.json.JSONObject;

/**
 * @ClassName: FormHoldService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月22日 下午4:37:35
 */
@Service
public class FormHoldService extends GameSupport {
    @Autowired
    private LordRepository lordRepository;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private DuelRepository duelRepository;
    /**
     * 武器
     */
    private static final int EQUIP_TYPE_WEAPON = 1;
    /**
     * 防具
     */
    private static final int EQUIP_TYPE_ARMOR = 2;
    /**
     * 饰品
     */
    private static final int EQUIP_TYPE_ACCESSORY = 3;
    /**
     * 符文
     */
    private static final int EQUIP_TYPE_RUNE = 4;
    /**
     * 技能数量
     */
    private static final int SKILL_CONUT = 3;
    /**
     * 突破装备槽位置
     */
    private static final int BREAK_EQUIP_INDEX = 3;
    /**
     * 突破技能槽位置
     */
    private static final int BREAK_SKILL_INDEX = 2;

    /**
     * @Description:更换武将
     * @param index
     * @param heroUid
     * @return
     * @throws
     */
    public void changeHero(int index, String heroUid) {
        Lord lord = this.getLord();
        checkHeroUid(lord, heroUid);
        FormHold formHold = new FormHold();
        formHold.setHeroUid(heroUid);
        formHold.setSkillUid(new ArrayList<>());
        formHold.setEquipUid(new ArrayList<>());
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = new ArrayList<FormHold>();
        if (forms.size() <= 0) {
            forms = new ArrayList<List<FormHold>>();
            formHolds.add(formHold);
            forms.add(formHolds);
        } else {
            formHolds = forms.get(0);
            if (formHolds.size() <= index) { // 武者下标不存在，添加新的武者
                checkHeroRepeat(formHolds, lord, heroUid, index);
                formHolds.add(formHold);
            } else {
                formHold = formHolds.get(index);
                checkHeroRepeat(formHolds, lord, heroUid, index);
                checkSoltSkillAndEquip(formHold, heroUid, lord);
                formHold.setHeroUid(heroUid);
                formHolds.set(index, formHold);
            }
            forms.set(0, formHolds);
        }
        lord.setForm(forms);
        Integer step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description:检查要上阵的武将是否是否觉醒或达到突破等级，如果尾觉醒或突破等级不够将撤掉原有的觉醒的技能和觉醒装备。
     * @param formHold
     * @param heroUid
     * @param lord
     * @throws
     */
    private void checkSoltSkillAndEquip(FormHold formHold, String heroUid, Lord lord) {
        Map<String, Hero> heros = lord.getHeros();
        Hero newHero = heros.get(heroUid);
        int breakLevel = newHero.getBreakLevel();
        String heroId = newHero.getHeroId();
        List<String> equips = formHold.getEquipUid();
        List<String> skills = formHold.getSkillUid();
        DataConfig rankUp = getDataConfig().get("rankUp");
        if (rankUp.getJsonObject().containsKey(heroId)) {
            DataConfig unlock = rankUp.get(heroId).get("unlock");
            Integer skillBreakLimit = unlock.getInteger("slotSkill");
            Integer equipBreakLimit = unlock.getInteger("slotEquip");
            if (breakLevel < skillBreakLimit && skills.size() > BREAK_SKILL_INDEX) {// 技能突破等级未达到
                skills.set(BREAK_SKILL_INDEX, null);
            }
            if (breakLevel < equipBreakLimit && equips.size() > BREAK_EQUIP_INDEX) {// 装备突破等级未达到
                equips.set(BREAK_EQUIP_INDEX, null);
            }
        } else {
            // 不可突破英雄，卸掉原位置突破技能和装备
            if (skills.size() > BREAK_SKILL_INDEX) {
                skills.set(BREAK_SKILL_INDEX, null);
            }
            if (equips.size() > BREAK_EQUIP_INDEX) {
                equips.set(BREAK_EQUIP_INDEX, null);
            }
        }
        formHold.setEquipUid(equips);
        formHold.setSkillUid(skills);
    }

    /**
     * 
     * @Description:下阵
     * @param index
     * @return
     * @throws
     */
    public void removeHero(int index) {
        Lord lord = this.getLord();
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = forms.get(0);
        if (formHolds.size() <= index) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23004);
        }
        if (formHolds.size() <= 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23003);
        }
        formHolds.remove(index);
        forms.set(0, formHolds);
        lord.setForm(forms);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description:交换武将
     * @param indexPer
     * @param index
     * @param heroUid
     * @return
     * @throws
     */
    public void exchange(Integer index1, Integer index2) {
        Lord lord = this.getLord();
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = forms.get(0);
        if (formHolds.size() <= index1 || formHolds.size() <= index2) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23004);
        }
        FormHold formHold1 = formHolds.get(index1);
        FormHold formHold2 = formHolds.get(index2);
        checkSoltSkillAndEquip(formHold1, formHold2.getHeroUid(), lord);
        checkSoltSkillAndEquip(formHold2, formHold1.getHeroUid(), lord);
        formHolds.set(index1, formHold2);
        formHolds.set(index2, formHold1);
        forms.set(0, formHolds);
        lord.setForm(forms);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description:记忆阵容
     * @param index
     * @throws
     */
    public void addFormHold(int index) {
        Lord lord = this.getLord();
        checkTactic(lord, index);
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = new ArrayList<FormHold>();
        if (forms.size() <= 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23005);
        }
        formHolds = forms.get(0);
        if (forms.size() <= index) {
            for (int i = forms.size(); i <= index; i++) {
                forms.add(null);
            }
        }
        forms.set(index, formHolds);
        lord.setForm(forms);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description:
     * @param lord
     * @param index 记忆位置
     * @throws
     */
    private void checkTactic(Lord lord, int index) {
        Map<Integer, Integer> m = lord.getFormStart();
        if (!m.containsKey(index) || m.get(index) != 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23015);
        }
        int leve = lord.getLevel();
        JSONObject config = this.getDataConfig().get("tacticLimit").getJsonObject();
        for (int i = config.size(); i > 0; i--) {
            int lv = JSONObject.fromObject(config.get(Integer.toString(i))).getInt("lv");
            if (leve >= lv) {
                int amount = JSONObject.fromObject(config.get(Integer.toString(i))).getInt("value");
                if (index + 1 > amount) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23012);
                }
                break;
            }
        }
    }

    /**
     * @Description: 提取阵容
     * @param index
     * @return
     * @throws
     */
    public void fetchFormHold(int index) {
        Lord lord = this.getLord();
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = new ArrayList<FormHold>();
        if (forms.size() <= index) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23006);
        }
        formHolds = forms.get(index);
        forms.set(0, formHolds);
        lord.setForm(forms);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description:更换技能
     * @param index
     * @return
     * @throws
     */
    public void changeSkill(int heroIndex, int skillIndex, String skillUid) {
        if (skillIndex >= this.SKILL_CONUT) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23016);
        }
        Lord lord = this.getLord();
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = forms.get(0);
        FormHold formHold = formHolds.get(heroIndex);
        if (skillIndex + 1 == this.SKILL_CONUT) {
            int breakLevel = lord.getHeros().get(formHold.getHeroUid()).getBreakLevel();
            int slotSkill = this.getDataConfig().get("rankUp")
                    .get(lord.getHeros().get(formHold.getHeroUid()).getHeroId()).get("unlock").getInteger("slotSkill");
            if (breakLevel < slotSkill) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23017);
            }
        }
        List<String> skills = formHold.getSkillUid();
        if (skillUid != null) {
            checkSkill(lord.getSkills(), formHolds, skillUid, skillIndex, heroIndex);
        }
        if (skills.size() <= skillIndex) {
            for (int i = skills.size(); i <= skillIndex; i++) {
                skills.add(null);
            }
        }
        skills.set(skillIndex, skillUid);
        formHold.setSkillUid(skills);
        formHolds.set(heroIndex, formHold);
        forms.set(0, formHolds);
        lord.setForm(forms);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description: 更换装备
     * @param heroIndex
     * @param equipIndex
     * @param equipUid
     * @return
     * @throws
     */
    public void changeEquip(int heroIndex, int equipIndex, String equipUid) {
        Lord lord = this.getLord();
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> formHolds = forms.get(0);
        if (equipUid != null) {
            checkEquip(lord.getEquips(), formHolds, equipUid, equipIndex);
            String index = getEquipIndex(formHolds, equipUid); // 已经装备此装备的位置(英雄位置)
            if (index != null) {
                int i = Integer.parseInt(index);
                // 移除重复装备
                formHolds.get(i).getEquipUid().set(equipIndex, null);
            }
        }
        FormHold formHold = formHolds.get(heroIndex);
        if (equipIndex + 1 == this.EQUIP_TYPE_RUNE) {
            int breakLevel = lord.getHeros().get(formHold.getHeroUid()).getBreakLevel();
            int slotEquip = this.getDataConfig().get("rankUp")
                    .get(lord.getHeros().get(formHold.getHeroUid()).getHeroId()).get("unlock").getInteger("slotEquip");
            if (breakLevel < slotEquip) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23018);
            }
        }
        List<String> equips = formHold.getEquipUid();
        if (equips.size() <= equipIndex) {
            for (int i = equips.size(); i <= equipIndex; i++) {
                equips.add(null);
            }
        }
        equips.set(equipIndex, equipUid);
        formHold.setEquipUid(equips);
        formHolds.set(heroIndex, formHold);
        forms.set(0, formHolds);
        lord.setForm(forms);
        Integer step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM, forms);
    }

    /**
     * @Description:
     * @param equips
     * @param formHolds
     * @throws
     */
    private void checkEquip(Map<String, Equip> equips, List<FormHold> formHolds, String equipUid, int equipIndex) {
        if (!equips.containsKey(equipUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23010);
        }
        String equipId = equips.get(equipUid).getEquipId();
        switch (equipIndex) {
        case 0:
            checkEquipType(EQUIP_TYPE_WEAPON, equipId);
            break;
        case 1:
            checkEquipType(EQUIP_TYPE_ARMOR, equipId);
            break;
        case 2:
            checkEquipType(EQUIP_TYPE_ACCESSORY, equipId);
            break;
        case 3:
            checkEquipType(EQUIP_TYPE_RUNE, equipId);
            break;
        default:
            break;
        }
    }

    private String getEquipIndex(List<FormHold> formHolds, String equipUid) {
        for (int i = 0; i < formHolds.size(); i++) {
            FormHold f = formHolds.get(i);
            List<String> equipForm = f.getEquipUid();
            for (String uid : equipForm) {
                if (equipUid.equals(uid)) {
                    return String.valueOf(i);
                }
            }
        }
        return null;
    }

    /**
     * @Description: 判断装备类型
     * @param equipType
     * @param equipUid
     * @throws
     */
    private void checkEquipType(int equipType, String equipUid) {
        int type = this.getDataConfig().get("equip").get(equipUid).getInteger("type");
        if (equipType != type) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23011);
        }
    }

    /**
     * @Description: heroUid、heroId是否重复
     * @param formHolds
     * @param lord
     * @param heroUid
     * @throws
     */
    private void checkHeroRepeat(List<FormHold> formHolds, Lord lord, String heroUid, int index) {
        Map<String, Hero> m = lord.getHeros();
        Hero hero = m.get(heroUid);
        String heroId = hero.getHeroId();
        for (int i = 0; i < formHolds.size(); i++) {
            if (i == index) {
                continue;
            }
            FormHold f = formHolds.get(i);
            if (heroUid.equals(f.getHeroUid())) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23002, heroUid);
            }
            if (heroId.equals(m.get(f.getHeroUid()).getHeroId())) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23002, heroId);
            }
        }
    }

    /**
     * 
     * @Description: heroUid是否存在
     * @param lord
     * @param heroUid
     * @throws
     */
    public void checkHeroUid(Lord lord, String heroUid) {
        Map<String, Hero> m = lord.getHeros();
        if (!m.containsKey(heroUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23001, heroUid);
        }
    }

    /**
     * 
     * @Description:
     * @param skills
     * @param formHolds
     * @param skillUid
     * @param index
     * @throws
     */
    private void checkSkill(Map<String, Skill> skills, List<FormHold> formHolds, String skillUid, int skillIndex,
            int heroIndex) {
        if (!skills.containsKey(skillUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23007);
        }
        String skillId = skills.get(skillUid).getSkillId();
        List<String> nowSkills = formHolds.get(heroIndex).getSkillUid(); // 当前技能
        // 检验当前英雄位置技能是否重复
        for (int i = 0; i < nowSkills.size(); i++) {
            if (i == skillIndex || skills.get(nowSkills.get(i)) == null) {
                continue;
            }
            if (skillId.equals(skills.get(nowSkills.get(i)).getSkillId())) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23008);
            }
        }

        // 阵容中技能存在查找
        int skillNum = 0; // 技能（id）存在数量
        for (int j = 0; j < formHolds.size(); j++) {
            FormHold formHold = formHolds.get(j);
            List<String> skill = formHold.getSkillUid();
            for (int i = 0; i < skill.size(); i++) {
                if (j == heroIndex && i == skillIndex) {
                    continue;
                }
                String str = skill.get(i);
                // 统计该技能存在的数量
                if (str != null && skillId.equals(skills.get(str).getSkillId())) {
                    skillNum++;
                }
                if (str != null && str.equals(skillUid)) {
                    skill.set(i, null);
                    formHold.setSkillUid(skill);
                    formHolds.set(j, formHold);
                }
            }
        }
        // 判断该技能是否为限制性技能
        DataConfig skillDataConfig = getDataConfig().get("skill").get(skillId);
        if (skillDataConfig.getJsonObject().containsKey("typeLimit")) {
            // 该技能为限制性技能
            int skillCountLimit = getDataConfig().get("skill_typeLimit")
                    .get(skillDataConfig.get(skillId).getString("typeLimit")).getInteger("limit");
            if (skillNum + 1 > skillCountLimit) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23019,
                        "该技能上阵数量不能超过" + skillCountLimit + "个");
            }
        }

        /*
         * if (listSkill.contains(skillUid)) { GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23008); }
         */
        /*
         * JSONObject skillJson = this.getDataConfig().get("skill").get(skillId).getJsonObject(); if
         * (skillJson.containsKey("typelimit")) { int limit = skillJson.getInt("typelimit"); if (skillNum >= limit) {
         * GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23009); } }
         */
    }

    /**
     * 
     * @Description:
     * @param lord
     * @param skillUid
     * @throws
     */
    public void removeFormSkill(Lord lord, String skillUid) {
        List<List<FormHold>> forms = lord.getForm();
        boolean isChangeForm = false;
        boolean isChangeDuel = false;
        for (int i = 0; i < forms.size(); i++) {
            List<FormHold> formHolds = forms.get(i);
            for (int j = 0; j < formHolds.size(); j++) {
                FormHold formHold = formHolds.get(j);
                List<String> skillUids = formHold.getSkillUid();
                if (skillUids.contains(skillUid)) {
                    skillUids.remove(skillUid);
                    formHold.setSkillUid(skillUids);
                    formHolds.set(j, formHold);
                    forms.set(i, formHolds);
                    isChangeForm = true;
                }
            }
        }
        lord.setForm(forms);
        // 删除防御阵容中已经被删除的技能
        Duel duel = duelRepository.findOne(lord.getId());
        if (duel != null && duel.getFormDefend() != null) {
            List<FormHold> formDefend = duel.getFormDefend();
            for (int j = 0; j < formDefend.size(); j++) {
                FormHold formHold = formDefend.get(j);
                List<String> skillUids = formHold.getSkillUid();
                if (skillUids.contains(skillUid)) {
                    skillUids.remove(skillUid);
                    formHold.setSkillUid(skillUids);
                    formDefend.set(j, formHold);
                    isChangeDuel = true;
                }
            }
            duel.setFormDefend(formDefend);
            duelRepository.save(duel);
        }
        if (isChangeForm) {
            lord.setForm(forms);
            this.gameModel.addObject(ResponseKey.FORM, lord.getForm());
        }
        if (isChangeDuel) {
            duelRepository.save(duel);
            this.gameModel.addObject(ResponseKey.DUEL, lord.getForm());
        }
    }

    /**
     * @Description:
     * @param lord
     * @param equipUid
     * @throws
     */
    public void removeFormEquip(Lord lord, String equipUid) {
        List<List<FormHold>> forms = lord.getForm();
        boolean isChangeForm = false;
        boolean isChangeDuel = false;
        for (int i = 0; i < forms.size(); i++) {
            List<FormHold> formHolds = forms.get(i);
            for (int j = 0; j < formHolds.size(); j++) {
                FormHold formHold = formHolds.get(j);
                List<String> equipUidList = formHold.getEquipUid();
                if (equipUidList.contains(equipUid)) {
                    equipUidList.remove(equipUid);
                    formHold.setEquipUid(equipUidList);
                    formHolds.set(j, formHold);
                    forms.set(i, formHolds);
                    isChangeForm = true;
                }
            }
        }
        lord.setForm(forms);
        // 删除防御阵容中已经被删除的装备
        Duel duel = duelRepository.findOne(lord.getId());
        if (duel != null && duel.getFormDefend() != null) {
            List<FormHold> formDefend = duel.getFormDefend();
            for (int j = 0; j < formDefend.size(); j++) {
                FormHold formHold = formDefend.get(j);
                List<String> equipUidList = formHold.getEquipUid();
                if (equipUidList.contains(equipUid)) {
                    equipUidList.remove(equipUid);
                    formHold.setEquipUid(equipUidList);
                    formDefend.set(j, formHold);
                    isChangeDuel = true;
                }
            }
            duel.setFormDefend(formDefend);
            duelRepository.save(duel);
        }
        if (isChangeForm) {
            lord.setForm(forms);
            this.gameModel.addObject(ResponseKey.FORM, lord.getForm());
        }
        if (isChangeDuel) {
            duelRepository.save(duel);
            this.gameModel.addObject(ResponseKey.DUEL, lord.getForm());
        }
    }

    /**
     * @Description:
     * @param lord
     * @param heroUid
     * @throws
     */
    public void removeFormHero(Lord lord, String heroUid) {
        List<List<FormHold>> forms = lord.getForm();
        boolean isChangeForm = false;
        boolean isChangeDuel = false;
        for (int i = 0; i < forms.size(); i++) {
            List<FormHold> formHolds = forms.get(i);
            for (int j = 0; j < formHolds.size(); j++) {
                FormHold formHold = formHolds.get(j);
                if (heroUid.equals(formHold.getHeroUid())) {
                    formHolds.remove(j);
                    forms.set(i, formHolds);
                    isChangeForm = true;
                }
            }
        }
        // 删除防御阵容中已经被删除的英雄
        Duel duel = duelRepository.findOne(lord.getId());
        if (duel != null && duel.getFormDefend() != null) {
            List<FormHold> formDefend = duel.getFormDefend();
            for (int j = 0; j < formDefend.size(); j++) {
                FormHold formHold = formDefend.get(j);
                if (heroUid.equals(formHold.getHeroUid())) {
                    formDefend.remove(j);
                    isChangeDuel = true;
                }
            }
            duel.setFormDefend(formDefend);
        }
        if (isChangeForm) {
            lord.setForm(forms);
            this.gameModel.addObject(ResponseKey.FORM, lord.getForm());
        }
        if (isChangeDuel) {
            duelRepository.save(duel);
            this.gameModel.addObject(ResponseKey.DUEL, lord.getForm());
        }
    }

    /**
     * @Description: 初始化启用阵容
     * @return
     * @throws
     */
    public Map<Integer, Integer> initFormStart() {
        JSONObject config = this.getDataConfig().get("teamRemember").getJsonObject();
        Iterator it = config.keys();
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        while (it.hasNext()) {
            String formKey = it.next().toString();
            int price = 0;
            try {
                price = JSONObject.fromObject(config.get(formKey)).getInt("price");
            } catch (Exception e) {
                continue;
            }
            if (price <= 0) {
                m.put(Integer.parseInt(formKey), 1);
            }
        }
        return m;
    }

    /**
     * @Description:解锁阵容
     * @param index
     * @throws
     */
    public void unlockForm(int index) {
        DataConfig dataConfig = this.getDataConfig().get("teamRemember").get(Integer.toString(index));
        int price = 0;
        try {
            price = dataConfig.getInteger("price");
        } catch (Exception e) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23013);
        }
        int lv = dataConfig.getInteger("needLv");
        Lord lord = this.getLord();
        int level = lord.getLevel();
        if (level < lv) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23014);
        }
        gainPayService.pay(lord, ItemID.DIAMOND, price);
        Map<Integer, Integer> m = lord.getFormStart();
        if (m == null) {
            m = new HashMap<Integer, Integer>();
        }
        m.put(index, 1);
        lord.setFormStart(m);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.FORM_START, m);
    }

}
