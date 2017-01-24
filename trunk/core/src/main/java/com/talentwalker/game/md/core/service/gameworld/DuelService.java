/**
 * @Title: DuelService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月6日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.DuelRank;
import com.talentwalker.game.md.core.domain.gameworld.DuelRobot;
import com.talentwalker.game.md.core.domain.gameworld.DuelStore;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.gameworld.DuelRepository;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: DuelService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月6日 下午3:12:35
 */
@Service
public class DuelService extends GameSupport {
    @Autowired
    private DuelRepository duelRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GameUserRepository gameUserRepository;
    @Autowired
    private GameZoneRepository gameZoneRepository;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private MailService mailService;
    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private MissionService missionService;
    @Autowired
    private HeroService heroService;
    /**
     * 排名key
     */
    private static final String RANK = "game_duel_rank_";
    /**
     * 机器人
     */
    private static final String ROBOT = "game_duel_robot_";
    /**
     * 擂台防守阵容
     */
    private static final String FORM_DEFEND = "formDefend";
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
     * 显示英雄数量
     */
    // private static final int HERO_SHOW_COUNT = 3;
    /**
     * 
     * @Description:登陆时调用
     * @throws
     */
    public Duel getMain(Lord lord) {
        Duel duel = getDuel(lord);
        return duel;
    }

    /**
     * @Description:
     * @throws
     */
    public void duelMain() {
        Lord lord = this.getLord();
        // 校验等级
        Duel duel = getDuel(lord);
        this.getRankList(duel, lord);
        // duel.setStore(null);
        this.gameModel.addObject(ResponseKey.DUEL, duel);
    }

    public void storeMain() {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        DuelStore store = duel.getStore();
        if (store == null) {
            store = new DuelStore();
            store.setAutomaticTime(System.currentTimeMillis());
            store.setRefreshTime(System.currentTimeMillis());
            store.setAutomaticTime(0);
            store.setTimes(0);
            // store.setGoods(this.refreshStore());
            // duel.setStore(store);
        }
        this.automaticRefreshStore(store);
        duel.setStore(store);
        duelRepository.save(duel);
    }

    public Duel getDuel(Lord lord) {
        Boolean changeDuel = false;
        Duel duel = duelRepository.findOne(lord.getId());
        String dataLogicId = this.getDataLogicId(lord);
        String rankKey = this.getRankKey(dataLogicId);
        int rank = this.getRank(lord, rankKey);
        // 初始化决斗信息
        if (duel == null) {
            duel = new Duel();
            duel.setId(lord.getId());
            duel.setPvpGold(0);
            duel.setHrank(rank);
            duel.setBuyTimes(0);
            duel.setTimes(0);
            duel.setSurplusTimes(0);
            duel.setLastTime(System.currentTimeMillis());
            changeDuel = true;
        }
        // 初始化决斗阵容
        List<FormHold> formDefend = duel.getFormDefend();
        if (formDefend == null || formDefend.isEmpty()) {
            if (lord.getForm() != null && lord.getForm().size() > 0) {
                formDefend = lord.getForm().get(0);
                duel.setFormDefend(formDefend);
                changeDuel = true;
            }
        }
        if (changeDuel) {
            duelRepository.save(duel);
        }
        duel.setRank(rank);
        return duel;
    }

    /**
     * @Description:
     * @param lord
     * @return
     * @throws
     */
    private void getRankList(Duel duel, Lord lord) {
        String dataLogicId = this.getDataLogicId(lord);
        this.recoverTimes(duel);
        // 推送高于玩家排行的玩家
        List<Integer> matchingList = matchingFront(lord, duel);
        // 可查看的前几名排行
        List<Map<String, Object>> topdRankList = topdisplayList(duel, lord, dataLogicId, matchingList);
        // 推送低于玩家排行的玩家
        List<Integer> matchingAfter = matchingAfter(lord, duel, dataLogicId);
        if (matchingAfter != null) {
            matchingList.addAll(matchingAfter);
        }
        Collections.sort(matchingList);
        // 格式推送数据
        List<Map<String, Object>> rankLordList = this.matchinData(duel, lord, dataLogicId, matchingList);
        topdRankList.addAll(rankLordList);
        duel.setRankList(topdRankList);
    }

    /**
     * @Description:
     * @param dataLogicId
     * @return
     * @throws
     */
    private List<Map<String, Object>> topdisplayList(Duel duel, Lord lord, String dataLogicId,
            List<Integer> matchingList) {
        List<Map<String, Object>> rankFront = new ArrayList<Map<String, Object>>();
        int count = this.getDataConfig().get("duel_other").get("topdisplay").getInteger("size");
        if (duel.getRank() < count) {
            count = duel.getRank() - 1;
        }
        if (count == 0) {
            return rankFront;
        }
        String rankKey = this.getRankKey(dataLogicId);
        String robotKey = this.getRobotKey(dataLogicId);
        Query query = new Query();
        query.with(new Sort(new Order(Direction.ASC, "rank")));
        query.limit(count);
        List<DuelRank> duelList = mongoTemplate.find(query, DuelRank.class, rankKey);
        for (DuelRank duelRank : duelList) {
            if (matchingList.contains(duelRank.getId())) {
                continue;
            }
            Map<String, Object> map = this.formatData(duel, lord, duelRank, robotKey, duel.getRank());
            if (map != null) {
                rankFront.add(map);
            }
        }
        return rankFront;
    }

    /**
     * @Description:
     * @param lord
     * @param matchingList
     * @return
     * @throws
     */
    private List<Map<String, Object>> matchinData(Duel duel, Lord lord, String dataLogicId,
            List<Integer> matchingList) {
        String rankKey = this.getRankKey(dataLogicId);
        String robotKey = this.getRobotKey(dataLogicId);
        List<Map<String, Object>> rankFront = new ArrayList<Map<String, Object>>();
        for (int randomRank : matchingList) {
            Query query = new Query(Criteria.where("rank").is(randomRank));
            DuelRank duelRank = mongoTemplate.findOne(query, DuelRank.class, rankKey);
            if (duelRank == null) {
                continue;
            }
            Map<String, Object> map = this.formatData(duel, lord, duelRank, robotKey, duel.getRank());
            if (map != null && duel.getRank() != randomRank) {
                map.put("fight", 1);
            }
            if (map != null) {
                rankFront.add(map);
            }
        }
        return rankFront;
    }

    private Map<String, Object> formatData(Duel duel, Lord lord, DuelRank duelRank, String robotKey, int rank) {
        Map<String, Object> rankMap = new HashMap<String, Object>();
        if (this.isNPC(duelRank.getId())) {
            Query query1 = new Query(Criteria.where("id").is(duelRank.getId()));
            DuelRobot duelRobot = mongoTemplate.findOne(query1, DuelRobot.class, robotKey);
            heroService.setFormHoldFPRobot(duelRobot);
            rankMap.put("id", duelRobot.getId());
            rankMap.put("name", duelRobot.getName());
            rankMap.put("level", duelRobot.getLevel());
            rankMap.put("headIcon", duelRobot.getHeadIcon());
            rankMap.put("rank", duelRank.getRank());
            rankMap.put("notes", duelRobot.getNotes());
            List<Map<String, Object>> heroList = new ArrayList<Map<String, Object>>();
            List<FormHold> formHoldList = duelRobot.getFormDefend();
            int FP = 0;
            for (FormHold formHold : formHoldList) {
                Map<String, Object> map = new HashMap<String, Object>();
                Hero hero = duelRobot.getHero().get(formHold.getHeroUid());
                map.put("heroId", hero.getHeroId());
                map.put("level", hero.getLevel());
                map.put("breakLevel", hero.getBreakLevel());
                map.put("loveLevel", hero.getLoveLevel());
                map.put("heroFP", formHold.getFP());
                heroList.add(map);
                FP += formHold.getFP();
            }
            rankMap.put("FP", FP); // 战斗力
            rankMap.put("heros", heroList);
        } else {
            if (duelRank.getRank() != rank) {
                Duel duel2 = duelRepository.findOne(duelRank.getId());
                Lord lord2 = lordRepository.findOne(duelRank.getId());
                heroService.setFormHoldFP(lord2);
                if (duel2 == null || lord2 == null) {
                    return null;
                }
                rankMap.put("id", lord2.getId());
                rankMap.put("name", lord2.getName());
                rankMap.put("level", lord2.getLevel());
                rankMap.put("headIcon", lord2.getHeadIcon());
                rankMap.put("rank", duelRank.getRank());
                rankMap.put("notes", lord2.getNotes());
                List<Map<String, Object>> heroList = new ArrayList<Map<String, Object>>();
                List<FormHold> forms = duel2.getFormDefend();
                if (forms == null) {
                    forms = new ArrayList<>();
                }
                int FP = 0;
                for (int i = 0; i < forms.size(); i++) {
                    FormHold form = forms.get(i);
                    Hero hero = lord2.getHeros().get(form.getHeroUid());
                    if (hero == null) {
                        continue;
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("heroId", hero.getHeroId());
                    map.put("level", hero.getLevel());
                    map.put("breakLevel", hero.getBreakLevel());
                    map.put("loveLevel", hero.getLoveLevel());
                    map.put("heroFP", form.getFP());
                    FP += form.getFP();
                    heroList.add(map);
                }
                rankMap.put("FP", FP);
                rankMap.put("heros", heroList);
            } else {
                heroService.setFormHoldFP(lord);
                rankMap.put("id", lord.getId());
                rankMap.put("name", lord.getName());
                rankMap.put("level", lord.getLevel());
                rankMap.put("headIcon", lord.getHeadIcon());
                rankMap.put("rank", duelRank.getRank());
                rankMap.put("notes", lord.getNotes());
                rankMap.put("self", 1);
                List<Map<String, Object>> heroList = new ArrayList<Map<String, Object>>();
                List<FormHold> forms = duel.getFormDefend();
                if (forms == null) {
                    forms = new ArrayList<>();
                }
                int FP = 0;
                for (int i = 0; i < forms.size(); i++) {
                    FormHold form = forms.get(i);
                    Hero hero = lord.getHeros().get(form.getHeroUid());
                    if (hero == null) {
                        continue;
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("heroId", hero.getHeroId());
                    map.put("level", hero.getLevel());
                    map.put("breakLevel", hero.getBreakLevel());
                    map.put("loveLevel", hero.getLoveLevel());
                    map.put("heroFP", form.getFP());
                    heroList.add(map);
                    FP += form.getFP();
                }
                rankMap.put("FP", FP);
                rankMap.put("heros", heroList);
            }
        }
        return rankMap;
    }

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
        Duel duel = duelRepository.findOne(lord.getId());
        FormHold formHold = new FormHold();
        formHold.setHeroUid(heroUid);
        formHold.setSkillUid(new ArrayList<>());
        formHold.setEquipUid(new ArrayList<>());
        List<FormHold> formHolds = duel.getFormDefend();
        if (formHolds == null) {
            formHolds = new ArrayList<FormHold>();
            formHolds.add(formHold);
        } else {
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
        }
        duel.setFormDefend(formHolds);
        duelRepository.save(duel);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(FORM_DEFEND, duel.getFormDefend());
        this.gameModel.addObject(ResponseKey.DUEL, data);
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
        Duel duel = duelRepository.findOne(lord.getId());
        List<FormHold> formHolds = duel.getFormDefend();
        if (formHolds.size() <= index) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23004);
        }
        if (formHolds.size() <= 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23003);
        }
        formHolds.remove(index);
        duel.setFormDefend(formHolds);
        duelRepository.save(duel);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(FORM_DEFEND, duel.getFormDefend());
        this.gameModel.addObject(ResponseKey.DUEL, data);
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
        Duel duel = duelRepository.findOne(lord.getId());
        List<FormHold> formHolds = duel.getFormDefend();
        if (formHolds.size() <= index1 || formHolds.size() <= index2) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23004);
        }
        FormHold formHold1 = formHolds.get(index1);
        FormHold formHold2 = formHolds.get(index2);
        checkSoltSkillAndEquip(formHold1, formHold2.getHeroUid(), lord);
        checkSoltSkillAndEquip(formHold2, formHold1.getHeroUid(), lord);
        formHolds.set(index1, formHold2);
        formHolds.set(index2, formHold1);
        duel.setFormDefend(formHolds);
        duelRepository.save(duel);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(FORM_DEFEND, duel.getFormDefend());
        this.gameModel.addObject(ResponseKey.DUEL, data);
    }

    /**
     * @Description:更换技能
     * @param heroIndex 英雄位置
     * @param skillIndex 技能位置
     * @param skillUid 技能UID
     * @throws
     */
    public void changeSkill(int heroIndex, int skillIndex, String skillUid) {
        if (skillIndex >= SKILL_CONUT) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23016);
        }
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        List<FormHold> formHolds = duel.getFormDefend();
        FormHold formHold = formHolds.get(heroIndex);
        if (skillIndex + 1 == SKILL_CONUT) {
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
        duel.setFormDefend(formHolds);
        duelRepository.save(duel);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(FORM_DEFEND, duel.getFormDefend());
        this.gameModel.addObject(ResponseKey.DUEL, data);
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
        for (int i = 0; i < nowSkills.size(); i++) {
            if (i == skillIndex || skills.get(nowSkills.get(i)) == null) {
                continue;
            }
            if (skillId.equals(skills.get(nowSkills.get(i)).getSkillId())) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23008);
            }
        }

        int skillNum = 0;
        for (int j = 0; j < formHolds.size(); j++) {
            FormHold formHold = formHolds.get(j);
            List<String> skill = formHold.getSkillUid();
            for (int i = 0; i < skill.size(); i++) {
                if (j == heroIndex && i == skillIndex) {
                    continue;
                }
                String str = skill.get(i);
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
        DataConfig skillDataConfig = getDataConfig().get("skill").get(skillId);
        if (skillDataConfig.getJsonObject().containsKey("typeLimit")) {
            // 要替换的技能为限制技能
            int skillCountLimit = getDataConfig().get("skill_typeLimit").get(skillDataConfig.getString("typeLimit"))
                    .getInteger("limit");
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
     * @Description: 更换装备
     * @param heroIndex
     * @param equipIndex
     * @param equipUid
     * @return
     * @throws
     */
    public void changeEquip(int heroIndex, int equipIndex, String equipUid) {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        List<FormHold> formHolds = duel.getFormDefend();
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
        if (equipIndex + 1 == EQUIP_TYPE_RUNE) {
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
        duel.setFormDefend(formHolds);
        duelRepository.save(duel);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(FORM_DEFEND, duel.getFormDefend());
        this.gameModel.addObject(ResponseKey.DUEL, data);
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
     * @Description:记忆阵容
     * @param index
     * @throws
     */
    public void addFormHold(int index) {
        Lord lord = this.getLord();
        checkTactic(lord, index);
        Duel duel = duelRepository.findOne(lord.getId());
        List<FormHold> formHolds = duel.getFormDefend();
        List<List<FormHold>> forms = lord.getForm();
        if (forms.size() <= 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_23005);
        }
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
        Duel duel = duelRepository.findOne(lord.getId());
        duel.setFormDefend(formHolds);
        duelRepository.save(duel);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(FORM_DEFEND, duel.getFormDefend());
        this.gameModel.addObject(ResponseKey.DUEL, data);
    }

    public String getDataLogicId(Lord lord) {
        return gameZoneRepository.findOne(this.getGameUser().getGameZoneId()).getDataLogic().getId();
    }

    /**
     * 
     * @Description:
     * @param dataLogicId
     * @return
     * @throws
     */
    public String getRankKey(String dataLogicId) {
        String rankKey = RANK + dataLogicId;
        return rankKey;
    }

    private String getRobotKey(String dataLogicId) {
        String robotKey = ROBOT + dataLogicId;
        return robotKey;
    }

    public int getRank(Lord lord, String rankKey) {
        int count = 0;
        DuelRank duelRank = mongoTemplate.findById(lord.getId(), DuelRank.class, rankKey);
        if (duelRank == null) {
            count = (int) mongoTemplate.count(new Query(), rankKey);
            count++;
            DuelRank rankData = new DuelRank();
            rankData.setId(lord.getId());
            rankData.setRank(count);
            mongoTemplate.save(rankData, rankKey);
        } else {
            count = duelRank.getRank();
        }
        return count;
    }

    private void recoverTimes(Duel duel) {
        Date lastTime = new Date(duel.getLastTime());
        Date time = new Date();
        if (!DateUtils.isSameDay(lastTime, time)) {
            duel.setBuyTimes(0);
            duel.setTimes(0);
            duel.setLastTime(System.currentTimeMillis());
            duelRepository.save(duel);
        }
    }

    /**
     * @Description:
     * @throws
     */
    public void buyTimes() {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        int buyTimes = duel.getBuyTimes();
        int vipLevel = lord.getVipLevel();
        int vipTimes = this.getDataConfig().get("VIP").get(Integer.toString(vipLevel)).getInteger("duelTimes");
        if (vipTimes <= buyTimes) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26000);
        }
        int surplusTimes = duel.getSurplusTimes();
        surplusTimes += 1;
        buyTimes += 1;
        int cost = this.getDataConfig().get("duel_VIPcost").get(Integer.toString(buyTimes)).getInteger("cost");
        duel.setBuyTimes(buyTimes);
        duel.setSurplusTimes(surplusTimes);
        gainPayService.pay(lord, ItemID.GOLD, cost);
        duelRepository.save(duel);
        Map<String, Object> map = new HashMap<>();
        map.put("buyTimes", buyTimes);
        map.put("surplusTimes", surplusTimes);
        this.gameModel.addObject(ResponseKey.DUEL, map);
    }

    private List<Integer> matchingFront(Lord lord, Duel duel) {
        List<Integer> rankList = new ArrayList<Integer>();
        int rank = duel.getRank();
        rankList.add(rank);
        DataConfig config = this.getDataConfig().get("duel_frontpush");
        Iterator it = config.getJsonObject().keys();
        while (it.hasNext()) {
            String index = it.next().toString();
            int ranking = config.get(index).getInteger("ranking");
            int ranEnd = ranking;
            if (config.getJsonObject().containsKey(Integer.toString(Integer.parseInt(index) + 1))) {
                ranEnd = config.get(Integer.toString(Integer.parseInt(index) + 1)).getInteger("ranking");
            } else { // 超过配置范围，按最后一个范围计算
                rank = ranking;
            }
            if (rank >= ranking && rank < ranEnd) {
                DataConfig frontConfig = config.get(index).get("front");
                for (int i = 1; i <= frontConfig.getJsonObject().size(); i++) {
                    List<Integer> frontList = frontConfig.get(Integer.toString(i)).getJsonArray();
                    int randomRank = RandomUtils.randomInt(rank - frontList.get(0), rank - frontList.get(1));
                    rankList.add(randomRank);
                }
                break;
            }
        }

        return rankList;
    }

    /**
     * @Description:判断是否是机器人
     * @param id
     * @return true:机器人、false:玩家
     * @throws
     */
    private Boolean isNPC(String id) {
        if (id.contains("NPC_")) {
            return true;
        }
        return false;
    }

    private List<Integer> matchingAfter(Lord lord, Duel duel, String dataLogicId) {
        int rank = duel.getRank();
        String rankKey = this.getRankKey(dataLogicId);
        int count = (int) mongoTemplate.count(new Query(), rankKey);
        DataConfig dataConfig = this.getDataConfig().get("duel_other");
        List<Integer> rankAfter = dataConfig.get("rankAfter").get("size").getJsonArray();
        int lowercount = dataConfig.get("lowercount").getInteger("size");
        List<Integer> rankList = new ArrayList<Integer>();
        int ranking = rank + rankAfter.get(0);
        int rankEnd = rank + rankAfter.get(1);
        if (rankEnd > count) {
            rankEnd = count;
            if ((rankEnd - ranking) < lowercount) {
                lowercount = rankAfter.get(0) - rankEnd;
            }
        }
        for (int i = 0; i < lowercount; i++) {
            int randomCount = RandomUtils.randomInt(ranking, rankEnd);
            if (rankList.contains(randomCount)) {
                i--;
                continue;
            }
            rankList.add(randomCount);
        }
        return rankList;
    }

    /**
     * @Description:刷新商店
     * @return
     * @throws
     */
    public List<List<Map<String, Object>>> refreshStore() {
        DataConfig config = this.getDataConfig();
        DataConfig store = config.get("duel_store");
        Iterator it = store.getJsonObject().keys();
        List<List<Map<String, Object>>> storeList = new ArrayList<List<Map<String, Object>>>();
        while (it.hasNext()) {
            // 每组商品
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            String storeKey = it.next().toString();
            List<Integer> numberList = store.get(storeKey).get("quantity").getJsonArray();
            int quantity = RandomUtils.randomInt(numberList.get(0), numberList.get(1));
            DataConfig storeConfig = config.get(storeKey);
            Iterator iterator = storeConfig.getJsonObject().keys();
            Map<String, Integer> weightMap = new HashMap<String, Integer>();
            while (iterator.hasNext()) {
                String itemId = iterator.next().toString();
                int weight = storeConfig.get(itemId).getInteger("weight");
                weightMap.put(itemId, weight);
            }
            for (int i = 0; i < quantity; i++) {
                String itemId = RandomUtils.randomTable(weightMap);
                weightMap.remove(itemId);
                int amount = storeConfig.get(itemId).getInteger("quantity");
                int pvpGold = storeConfig.get(itemId).getInteger("pvpGold");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", itemId);
                map.put("amount", amount);
                map.put("price", pvpGold);
                map.put("isbuy", 0);
                list.add(map);
            }
            storeList.add(list);
        }
        return storeList;
    }

    /**
     * @Description:手动刷新商店
     * @throws
     */
    public void manuallyRefreshStore() {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        DuelStore store = duel.getStore();
        int times = store.getTimes();
        times += 1;
        DataConfig config = this.getDataConfig().get("duel_storeRefresh");
        if (!config.getJsonObject().containsKey(Integer.toString(times))) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26001);
        }
        int diamond = config.get(Integer.toString(times)).getInteger("diamond");
        gainPayService.pay(lord, ItemID.DIAMOND, diamond);
        store.setTimes(times);
        store.setRefreshTime(System.currentTimeMillis());
        store.setGoods(this.refreshStore());
        duel.setStore(store);
        duelRepository.save(duel);
        lordRepository.save(lord);
        Map<String, Object> storeMap = new HashMap<String, Object>();
        storeMap.put("store", store);
        this.gameModel.addObject(ResponseKey.DUEL, storeMap);
    }

    /**
     * @Description:自动刷新
     * @param duel
     * @throws
     */
    private void automaticRefreshStore(DuelStore store) {
        // DuelStore store = duel.getStore();
        long automaticTime = store.getAutomaticTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = DateUtils.parseDate(sdf.format(System.currentTimeMillis()), "yyyy-MM-dd", "yyyy-MM-dd");
            // 刷新时间点
            int storeRefresh1 = this.getDataConfig().get("duel_other").get("storeRefresh1").getInteger("size") * 60 * 60
                    * 1000;
            int storeRefresh2 = this.getDataConfig().get("duel_other").get("storeRefresh2").getInteger("size") * 60 * 60
                    * 1000;
            int storeRefresh3 = this.getDataConfig().get("duel_other").get("storeRefresh3").getInteger("size") * 60 * 60
                    * 1000;
            if (automaticTime < date.getTime() + storeRefresh3
                    && System.currentTimeMillis() >= date.getTime() + storeRefresh3) {
                store.setAutomaticTime(System.currentTimeMillis());
                store.setGoods(this.refreshStore());
                store.setNextRefreshTime(date.getTime() + storeRefresh1 + 24 * 60 * 60 * 1000);
            } else if (automaticTime < date.getTime() + storeRefresh2
                    && System.currentTimeMillis() >= date.getTime() + storeRefresh2) {
                store.setAutomaticTime(System.currentTimeMillis());
                store.setGoods(this.refreshStore());
                store.setNextRefreshTime(date.getTime() + storeRefresh3);
            } else if (automaticTime < date.getTime() + storeRefresh1
                    && System.currentTimeMillis() >= date.getTime() + storeRefresh1) {
                store.setAutomaticTime(System.currentTimeMillis());
                store.setGoods(this.refreshStore());
                store.setNextRefreshTime(date.getTime() + storeRefresh2);
            }
            Map<String, Object> storeMap = new HashMap<String, Object>();
            Object obj = this.gameModel.getModel(ResponseKey.DUEL);
            if (obj != null) {
                storeMap = (Map<String, Object>) obj;
            }
            storeMap.put("store", store);
            this.gameModel.addObject(ResponseKey.DUEL, storeMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 购买商品
     * @param itemId
     * @throws
     */
    public void buyItem(String itemId) {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        DuelStore store = duel.getStore();
        List<List<Map<String, Object>>> listGoods = store.getGoods();
        labe: for (int i = 0; i < listGoods.size(); i++) {
            List<Map<String, Object>> listMap = listGoods.get(i);
            for (int j = 0; j < listMap.size(); j++) {
                Map<String, Object> map = listMap.get(j);
                if (itemId.equals(map.get("id"))) {
                    int isbuy = Integer.parseInt(map.get("isbuy").toString());
                    if (isbuy == 1) {
                        GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26002);
                    }
                    int pvpGold = duel.getPvpGold();
                    gainPayService.pay(duel, ItemID.PVPGPLD, (int) map.get("price"));
                    map.put("isbuy", 1);
                    listMap.set(j, map);
                    listGoods.set(i, listMap);
                    store.setGoods(listGoods);
                    gainPayService.gain(lord, itemId, (int) map.get("amount"));
                    this.automaticRefreshStore(store);
                    duel.setStore(store);
                    duelRepository.save(duel);
                    lordRepository.save(lord);
                    break labe;
                }
            }
        }
    }

    /**
     * @Description:
     * @param lordId
     * @throws
     */
    public void lordFormInfo(String lordId) {
        List<Map<String, Object>> forms = new ArrayList<Map<String, Object>>();
        Lord lord = this.getLord();
        if (this.isNPC(lordId)) { // 机器人
            String dataLogicId = this.getDataLogicId(lord);
            String robotKey = this.getRobotKey(dataLogicId);
            Query query = new Query(Criteria.where("id").is(lordId));
            DuelRobot duelRobot = mongoTemplate.findOne(query, DuelRobot.class, robotKey);
            List<FormHold> formHoldList = duelRobot.getFormDefend();
            Map<String, Equip> equips = duelRobot.getEquips();
            Map<String, Hero> heros = duelRobot.getHero();
            for (FormHold formHold : formHoldList) {
                List<Equip> equipList = new ArrayList<Equip>();
                for (String equipUid : formHold.getEquipUid()) {
                    equipList.add(equips.get(equipUid));
                }
                Hero hero = heros.get(formHold.getHeroUid());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("hero", hero);
                map.put("skills", new ArrayList<Skill>());
                map.put("equips", equipList);
                forms.add(map);
            }
        } else {
            Lord lord1 = lordRepository.findOne(lordId);
            Duel duel = duelRepository.findOne(lordId);
            List<FormHold> defendForm = duel.getFormDefend();
            Map<String, Hero> heros = lord1.getHeros();
            Map<String, Skill> skills = lord1.getSkills();
            Map<String, Equip> equips = lord1.getEquips();
            for (FormHold formHold : defendForm) {
                Map<String, Object> map = new HashMap<String, Object>();
                Hero hero = heros.get(formHold.getHeroUid());
                List<String> skillUidList = formHold.getSkillUid();
                List<Skill> skillList = new ArrayList<Skill>();
                for (String skillUid : skillUidList) {
                    skillList.add(skills.get(skillUid));
                }
                List<String> equipUidList = formHold.getEquipUid();
                List<Equip> equipList = new ArrayList<Equip>();
                for (String equipUid : equipUidList) {
                    equipList.add(equips.get(equipUid));
                }
                map.put("hero", hero);
                map.put("skills", skillList);
                map.put("equips", equipList);
                forms.add(map);
            }
        }
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("enemyForm", forms);
        this.gameModel.addObject(ResponseKey.DUEL, responseMap);
    }

    /**
     * @Description: 
     * @param index 对手排名
     * @throws
     */
    public void battle(int index, int result) {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        String dataLogicId = this.getDataLogicId(lord);
        String rankKey = this.getRankKey(dataLogicId);
        DuelRank enemyDuelRrank = mongoTemplate.findOne(new Query(Criteria.where("rank").is(index)), DuelRank.class,
                rankKey);
        Map<String, Object> responseMap = new HashMap<String, Object>();
        int rank = 0; // 当前排名
        if (result == 1) {
            DuelRank duelRrank = mongoTemplate.findOne(new Query(Criteria.where("id").is(lord.getId())), DuelRank.class,
                    rankKey);
            rank = duelRrank.getRank();
            if (rank > index) {
                responseMap.put("rank", index);
                if (duel.getHrank() > index) {
                    responseMap.put("uprank", duel.getHrank() - index);
                    responseMap.put("hrank", index);
                    duel.setHrank(index);
                    this.breakthroughReward(lord, rank, index);
                    lordRepository.save(lord); // 触发最高排名获得钻石奖励
                }
                enemyDuelRrank.setRank(rank);
                duelRrank.setRank(index);
                mongoTemplate.save(enemyDuelRrank, rankKey);
                mongoTemplate.save(duelRrank, rankKey);
            }
            duelRepository.save(duel);
        }

        if (!isNPC(enemyDuelRrank.getId())) {
            this.sendMail(lord, enemyDuelRrank.getId(), rank, result);
        }
        Object obj = this.gameModel.getModel(ResponseKey.DUEL);
        if (obj != null) {
            responseMap = (Map<String, Object>) obj;
        }
        responseMap.put("result", result);

        this.gameModel.addObject(ResponseKey.DUEL, responseMap);
        missionService.trigerMissionForPVP(1);
    }

    /**
     * @Description:给对手发送文件
     * @param lord
     * @param id
     * @param rank2
     * @param result
     * @throws
     */
    private void sendMail(Lord lord, String id, int rank, int result) {
        DataConfig config = this.getDataConfig().get("duel_Msg");
        Iterator it = config.getJsonObject().keys();
        List<String> contentList = new ArrayList<>();
        String state = "lose";
        List<Object> paramet = new ArrayList<>();
        paramet.add(lord.getName());
        if (result != 1) {
            state = "win";
        } else {
            paramet.add(rank);
        }
        while (it.hasNext()) {
            String key = it.next().toString();
            if (key.contains(state)) {
                contentList.add(key);
            }
        }
        int random = RandomUtils.randomInt(0, contentList.size() - 1);
        String mailKey = config.get(contentList.get(random)).getString("id");
        mailService.sendMail(id, mailKey, paramet, null, Mail.TYPE_PVP);
    }

    /**
     * @Description: 突破奖励
     * @throws
     */
    private void breakthroughReward(Lord lord, int rank, int index) {
        DataConfig dataConfig = this.getDataConfig().get("duel_rankScore");
        Iterator it = dataConfig.getJsonObject().keys();
        while (it.hasNext()) {
            String configIndex = it.next().toString();
            int ranking = dataConfig.get(configIndex).getInteger("ranking");
            /*
             * if (ranking > rank) { configIndex = Integer.toString(Integer.parseInt(configIndex) - 1); if
             * (dataConfig.get(configIndex).getJsonObject().containsKey("breakthrough")) { Double diamond =
             * dataConfig.get(configIndex).getDouble("breakthrough"); int price = (int) Math.rint(diamond * (rank -
             * index)); if (price > 0) { gainPayService.gain(lord, ItemID.DIAMOND, price); } } break; }
             */
            if (ranking >= rank) {
                Double amount = dataConfig.get(configIndex).getDouble("breakthrough");
                if (amount != null) {
                    int price = (int) Math.rint(amount * (rank - index));
                    if (price > 0) {
                        gainPayService.gain(lord, ItemID.DIAMOND, price);
                    }
                }
                break;
            }

        }
    }

    /**
     * @Description:校验经验
     * @param lord
     * @throws
     */
    private void checkEnergy(Lord lord) {
        int energy = lord.getEnergy();
        if (energy < 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26005);
        }
        gainPayService.pay(lord, ItemID.ENERGY, 1);
    }

    /**
     * @Description: 检验挑战次数
     * @param duel
     * @throws
     */
    private void checkBattleTimes(Duel duel) {
        int daytimes = this.getDataConfig().get("duel_other").get("daytime").getInteger("size");
        int surplusTimes = duel.getSurplusTimes();
        int times = duel.getTimes();
        if (daytimes + surplusTimes - times < 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26004);
        }
        if (times < daytimes) {
            duel.setTimes(times + 1);
        } else {
            duel.setSurplusTimes(surplusTimes - 1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("times", duel.getTimes());
        map.put("surplusTimes", duel.getSurplusTimes());
        this.gameModel.addObject(ResponseKey.DUEL, map);
    }

    /**
     * @Description:获取战斗日志列表
     * @throws
     */
    public void battleLog() {
        Lord lord = this.getLord();
        List<Mail> mails = mailRepository.findByPlayerIdAndType(lord.getId(), Mail.TYPE_PVP);
        Map<String, Object> map = new HashMap<>();
        map.put("mail", mails);
        this.gameModel.addObject(ResponseKey.DUEL, map);
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
     * @Description: 进入战斗
     * @param index
     * @throws
     */
    public void enter(int index) {
        Lord lord = this.getLord();
        Duel duel = duelRepository.findOne(lord.getId());
        String dataLogicId = this.getDataLogicId(lord);
        String rankKey = this.getRankKey(dataLogicId);
        DuelRank enemyDuelRrank = mongoTemplate.findOne(new Query(Criteria.where("rank").is(index)), DuelRank.class,
                rankKey);
        if (enemyDuelRrank == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26007, "未找到挑战对手");
        }
        // 检验体力
        this.checkEnergy(lord);
        // 检验挑战次数
        this.checkBattleTimes(duel);
        lordRepository.save(lord);
        duelRepository.save(duel);
    }
}
