/**
 * @Title: DuelRobotService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月8日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.gmtool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.gameworld.DuelRank;
import com.talentwalker.game.md.core.domain.gameworld.DuelRobot;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Skill;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.util.RandomUtils;
import com.talentwalker.game.md.core.util.UuidUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: DuelRobotService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月8日 下午12:03:01
 */
@Service
public class DuelRobotService extends BaseService {
    @Autowired
    private IDataConfigManager dataConfigManager;
    @Autowired
    private GameZoneRepository gameZoneRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 排名key
     */
    private static final String RANK = "game_duel_rank_";
    /**
     * 机器人key
     */
    private static final String ROBOT = "game_duel_robot_";
    /**
     * 机器人主公ID前缀
     */
    private static final String NPC = "NPC_";
    /**
     * 英雄与主公等级比例
     */
    private static final int LEVEL_SCALE = 3;

    /**
     * @Description:
     * @param zoneList
     * @throws
     */
    public void generate(String zoneId) {
        GameZone gameZoneList = gameZoneRepository.findOne(zoneId);
        String datalogicId = gameZoneList.getDataLogic().getId();
        String rankKey = this.getRankKey(datalogicId);
        String robotKey = this.getRobotKey(datalogicId);
        DataConfig config = dataConfigManager.getTest();
        DataConfig rankGroup = config.get("duel_rankScore");
        Iterator it = rankGroup.getJsonObject().keys();
        this.deleteRankList(rankKey);
        this.deleteRobot(robotKey);
        int rank = 0;
        List<DuelRobot> duelRobotList = new ArrayList<DuelRobot>();
        List<DuelRank> duelRankList = new ArrayList<DuelRank>();
        int lvLimit = config.get("lvLimit").get("hero").getInteger("value");
        while (it.hasNext()) {
            if (rank >= 5000) {
                break;
            }
            String index = it.next().toString();
            String index2 = Integer.toString(Integer.parseInt(index) - 1);
            int rankStart = rankGroup.get(index2) == null ? 1 : rankGroup.get(index2).getInteger("ranking") + 1;
            int rankEnd = rankGroup.get(index).getInteger("ranking");
            int level = rankGroup.get(index).getInteger("npclv");// 主公等级
            int heroLevel = level * lvLimit;// 英雄等级
            // 签名
            String notes = "";
            DataConfig newPlayerConfig = config.get("newPlayer");
            notes = newPlayerConfig.get("playerSign").getString("value");
            for (int i = rankStart; i <= rankEnd; i++) {
                rank += 1;
                String lordId = this.NPC + rank;
                // 排名
                DuelRank duelRank = new DuelRank();
                duelRank.setId(lordId);
                duelRank.setRank(rank);
                duelRankList.add(duelRank);

                String name = randomPlayerName(config);
                List<String> npcGroup = rankGroup.get(index).get("npcgroup").getJsonArray();
                int randomIntNpc = RandomUtils.randomInt(0, npcGroup.size() - 1);
                String randomNpc = npcGroup.get(randomIntNpc);
                DataConfig npcGroupConfig = config.get("duel_npcgroup").get(randomNpc);
                String head = npcGroupConfig.getString("playerHead");
                List<String> heroList = npcGroupConfig.get("duelnpc").getJsonArray();
                /*
                 * DataConfig tacticLimit = config.get("tacticLimit"); Iterator itLimit =
                 * tacticLimit.getJsonObject().keys(); while (itLimit.hasNext()) { int number =
                 * Integer.parseInt(itLimit.next().toString()); if (tacticLimit.get(number + "").getInteger("lv") >
                 * level) { int val = tacticLimit.get(number - 1 + "").getInteger("value"); heroList =
                 * heroList.subList(0, val); break; } }
                 */
                List<String> equipGroup = rankGroup.get(index).get("equipgroup").getJsonArray();
                int random1 = RandomUtils.randomInt(0, equipGroup.size() - 1);
                String randomEquip = equipGroup.get(random1);
                List<String> equipList = new ArrayList<String>();
                JSONObject equipJson = config.get("duel_equipgroup").get(randomEquip).get("equip").getJsonObject();
                Iterator equioIt = equipJson.keys();
                while (equioIt.hasNext()) {
                    equipList.add(equipJson.getString(equioIt.next().toString()));
                }
                List<FormHold> listForm = new ArrayList<FormHold>();
                Map<String, Equip> equips = new HashMap<String, Equip>();
                List<String> equipUid = new ArrayList<String>();
                for (String equipId : equipList) {
                    Equip equip = newEquip(equipId);
                    equipUid.add(equip.getEquipUid());
                    equips.put(equip.getEquipUid(), equip);
                }
                Map<String, Hero> heros = new HashMap<String, Hero>();
                for (String heroId : heroList) {
                    Hero hero = newHero(heroId);
                    hero.setLevel(heroLevel);
                    heros.put(hero.getHeroUid(), hero);
                    FormHold form = new FormHold();
                    form.setHeroUid(hero.getHeroUid());
                    form.setEquipUid(equipUid);
                    form.setSkillUid(new ArrayList<String>());
                    listForm.add(form);
                }
                // 机器人
                DuelRobot robot = new DuelRobot();
                robot.setId(lordId);
                robot.setName(name);
                robot.setLevel(level);
                robot.setHero(heros);
                robot.setRank(rank);
                robot.setEquips(equips);
                robot.setHeadIcon(head);
                robot.setFormDefend(listForm);
                robot.setNotes(notes);
                duelRobotList.add(robot);
                if (rank >= 5000) {
                    break;
                }
            }
        }
        mongoTemplate.insert(duelRobotList, robotKey);
        mongoTemplate.insert(duelRankList, rankKey);
    }

    /**
     * @Description:
     * @param rankKey
     * @throws
     */
    private void deleteRankList(String rankKey) {
        mongoTemplate.remove(new Query(), rankKey);
    }

    /**
     * @Description:
     * @param robotKey
     * @throws
     */
    private void deleteRobot(String robotKey) {
        mongoTemplate.remove(new Query(), robotKey);
    }

    /**
     * @Description:
     * @param config
     * @return
     * @throws
     */
    private String randomPlayerName(DataConfig config) {
        DataConfig dataName1 = config.get("name1");
        DataConfig dataName2 = config.get("name2");
        int random1 = RandomUtils.randomInt(1, dataName1.getJsonObject().size());
        int random2 = RandomUtils.randomInt(1, dataName2.getJsonObject().size());
        String name = dataName1.get(Integer.toString(random1)).getString("name1")
                + dataName2.get(Integer.toString(random2)).getString("name2");
        return name;
    }

    /**
     * @Description:
     * @param dataZoneId 互通区Id
     * @return
     * @throws
     */
    private String getRankKey(String datalogicId) {
        return this.RANK + datalogicId;
    }

    /**
     * @Description:
     * @param dataZoneId 互通区Id
     * @return
     * @throws
     */
    private String getRobotKey(String datalogicId) {
        return this.ROBOT + datalogicId;
    }

    private Hero newHero(String heroId) {
        Hero hero = new Hero();
        hero.setExp(0);
        hero.setHeroUid(heroId + "_" + UuidUtils.getUuid());
        hero.setHeroId(heroId);
        hero.setLevel(1);
        hero.setBreakLevel(0);
        hero.setTime(System.currentTimeMillis());
        DataConfig config = dataConfigManager.getTest();
        String defaultSkill = config.get("heroConfig").get(heroId).getString("skillult");
        if (StringUtils.isNotEmpty(defaultSkill)) {
            hero.setDefaultSkill(this.newSkill(defaultSkill));
        }
        return hero;
    }

    private Skill newSkill(String skillId) {
        Skill skill = new Skill();
        skill.setExp(0);
        skill.setLevel(1);
        skill.setSkillId(skillId);
        skill.setSkillUid(skillId + "_" + UuidUtils.getUuid());
        skill.setTime(System.currentTimeMillis());
        return skill;
    }

    private Equip newEquip(String equipId) {
        Equip equip = new Equip();
        equip.setEquipId(equipId);
        equip.setEquipUid(equipId + "_" + UuidUtils.getUuid());
        equip.setLevel(1);
        equip.setStage(0);
        equip.setExpStage(0);
        equip.setTime(System.currentTimeMillis());
        return equip;
    }
}
