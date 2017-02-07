/**
 * @Title: MissionService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.MissionDaily;
import com.talentwalker.game.md.core.domain.gameworld.MissionOnce;
import com.talentwalker.game.md.core.domain.gameworld.MissionStatus;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.MissionDailyRepository;
import com.talentwalker.game.md.core.repository.gameworld.MissionOnceRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: MissionService
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年8月5日 下午2:35:27
 */

@Service
public class MissionService extends GameSupport {

    public final static int MISSION_TYPE_STAGE = 3;
    public final static int MISSION_TYPE_BOX = 4;
    public final static int MISSION_TYPE_RECRUIT = 5;
    public final static int MISSION_TYPE_EQUIP = 6;
    public final static int MISSION_TYPE_SKILL = 7;
    public final static int MISSION_TYPE_HERO = 8;
    public final static int MISSION_TYPE_PVP = 9;
    public final static int MISSION_TYPE_LEAGUE = 10;

    @Autowired
    private MissionDailyRepository dailyRepository;

    @Autowired
    private MissionOnceRepository onceRepository;

    @Autowired
    private GainPayService gainPayService;

    public MissionDaily getDailyMissions(Lord lord) {
        MissionDaily mission = getMissionDaily(lord);
        this.gameModel.addObject(ResponseKey.MISSION_DAILY, mission);
        return mission;
    }

    public MissionOnce getOnceMissions(Lord lord) {
        MissionOnce mission = getMissionOnce(lord);
        this.gameModel.addObject(ResponseKey.MISSION_ONCE, mission);
        return mission;
    }

    @SuppressWarnings("unchecked")
    public void getActiveReward(String box) {
        Lord lord = this.getLord();
        MissionDaily daily = getMissionDaily(lord);

        // 校验宝箱是否存在
        DataConfig config = this.getDataConfig().get("mission_active").get(box);
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29004);
        }

        // 校验是否领取奖励
        if (daily.getActiveStatus().containsKey(box)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29003);
        }

        // 校验活跃值是否足够
        int active = config.getInteger("active");
        if (daily.getActive() < active) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29005);
        }

        // 添加奖励
        JSONObject reward = config.get("reward").getJsonObject();
        for (Iterator<String> it = reward.keys(); it.hasNext();) {
            String itemId = it.next();
            int amount = reward.getInt(itemId);
            gainPayService.gain(lord, itemId, amount);
        }

        daily.getActiveStatus().put(box, 1);
        lordRepository.save(lord);
        dailyRepository.save(daily);

        this.gameModel.addObject(ResponseKey.MISSION_DAILY, getDailyMissions(lord));
    }

    @SuppressWarnings("unchecked")
    public void getDailyReward(String missionId) {
        Lord lord = this.getLord();
        MissionDaily daily = getMissionDaily(lord);

        MissionStatus mission = daily.getMissions().get(missionId);
        if (mission == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29000);
        }

        // 检查是否已经领取
        if (mission.isAccept()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29001);
        }

        // 校验任务是否完成
        int limit = this.getDataConfig().get("mission_daily").get(missionId).get("progress").getJsonObject()
                .getInt("end");
        if (mission.getProgress() < limit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29002);
        }
        // 添加活跃值
        int active = this.getDataConfig().get("mission_daily").get(missionId).getInteger("active");
        daily.setActive(daily.getActive() + active);

        // 添加奖励
        JSONObject reward = this.getDataConfig().get("mission_daily").get(missionId).get("reward").getJsonObject();
        for (Iterator<String> it = reward.keys(); it.hasNext();) {
            String itemId = it.next();
            int amount = reward.getInt(itemId);
            gainPayService.gain(lord, itemId, amount);
        }

        mission.setAccept(true);

        lordRepository.save(lord);
        dailyRepository.save(daily);
        this.gameModel.addObject(ResponseKey.MISSION_DAILY, getDailyMissions(lord));
    }

    /**
     * @Description: 触发关卡日常
     * @param count
     */
    public void trigerMissionForStage(int count) {
        trigerMission(MISSION_TYPE_STAGE, count);
    }

    /**
     * @Description: 触发宝箱日常
     * @param count
     */
    public void trigerMissionForBox(int count) {
        trigerMission(MISSION_TYPE_BOX, count);
    }

    /**
     * @Description: 触发招募日常
     * @param count
     */
    public void trigerMissionForRecruit(int count) {
        trigerMission(MISSION_TYPE_RECRUIT, count);
    }

    /**
     * @Description: 触发装备强化日常
     * @param count
     */
    public void trigerMissionForEquip(int count) {
        trigerMission(MISSION_TYPE_EQUIP, count);
    }

    /**
     * @Description: 触发技能强化日常
     * @param count
     */
    public void trigerMissionForSkill(int count) {
        trigerMission(MISSION_TYPE_SKILL, count);
    }

    /**
     * @Description: 触发武将强化日常
     * @param count
     */
    public void trigerMissionForHero(int count) {
        trigerMission(MISSION_TYPE_HERO, count);
    }

    /**
     * @Description: 触发擂台日常
     * @param count
     */
    public void trigerMissionForPVP(int count) {
        trigerMission(MISSION_TYPE_PVP, count);
    }

    /**
     * @Description: 触发公会战争日常
     * @param count
     */
    public void trigerMissionForLeague(int count) {
        trigerMission(MISSION_TYPE_LEAGUE, count);
    }

    /**
     * @Description: 触发主线关卡任务
     * @param stageId
     */
    public void trigerMissionOnceForStage(String stageId, int count) {
        MissionOnce mission = getMissionOnce(this.getLord());
        Iterator<String> it = mission.getMissions().keySet().iterator();
        JSONObject config = this.getDataConfig().get("mission_once").getJsonObject();
        boolean isTriger = false;
        while (it.hasNext()) {
            String missionId = it.next();
            if (!mission.getMissions().get(missionId).isAccept()) {
                JSONObject progress = config.getJSONObject(missionId).getJSONObject("progress");
                if (progress.containsKey("stage") && progress.getString("stage").equals(stageId)) {
                    mission.getMissions().get(missionId)
                            .setProgress(count + mission.getMissions().get(missionId).getProgress());
                    isTriger = true;
                }
            }
        }

        if (isTriger) {
            flushOnceMission(this.getLord(), mission);
            onceRepository.save(mission);
            getOnceMissions(this.getLord());
        }
    }

    /**
     * @Description: 触发主线招募任务
     * @param heroId
     */
    public void trigerMissionOnceForRecruit(String heroId) {
        MissionOnce mission = getMissionOnce(this.getLord());
        int rank = this.getDataConfig().get("heroConfig").get(heroId).getInteger("star");
        if (mission.getData().containsKey(rank + "")) {
            if (mission.getData().get(rank + "").containsKey(heroId)) {
                mission.getData().get(rank + "").put(heroId, mission.getData().get(rank + "").get(heroId) + 1);
            } else {
                mission.getData().get(rank + "").put(heroId, 1);
            }
        }
        // if (rank >= 4) {
        // mission.getData().get(1 + "").put("rank", mission.getData().get(1 + "").get("rank") + 1);
        // }
        flushOnceMission(this.getLord(), mission);
        onceRepository.save(mission);
        getOnceMissions(this.getLord());
    }

    @SuppressWarnings("unchecked")
    public void getOnceReward(String missionId) {
        Lord lord = this.getLord();
        MissionOnce missions = getMissionOnce(lord);

        MissionStatus mission = missions.getMissions().get(missionId);
        if (mission == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29000);
        }

        // 检查是否已经领取
        if (mission.isAccept()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29001);
        }

        // 校验任务是否完成
        int limit = this.getDataConfig().get("mission_once").get(missionId).get("progress").getJsonObject()
                .getInt("end");
        if (mission.getProgress() < limit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29002);
        }

        // 添加奖励
        JSONObject reward = this.getDataConfig().get("mission_once").get(missionId).get("reward").getJsonObject();
        for (Iterator<String> it = reward.keys(); it.hasNext();) {
            String itemId = it.next();
            int amount = reward.getInt(itemId);
            gainPayService.gain(lord, itemId, amount);
        }

        mission.setAccept(true);

        lordRepository.save(lord);
        onceRepository.save(missions);
        this.gameModel.addObject(ResponseKey.MISSION_ONCE, getOnceMissions(lord));
    }

    /**
     * @Description: 触发主线主公等级任务
     */
    public void trigerMissionOnceForLevel(Lord lord) {
        MissionOnce mission = getMissionOnce(lord);
        flushOnceMission(lord, mission);
        onceRepository.save(mission);
        getOnceMissions(lord);
    }

    /**
     * @Description: 触发日常
     * @param type
     * @param count
     */
    private void trigerMission(int type, int count) {
        MissionDaily mission = getMissionDaily(this.getLord());
        Map<String, MissionStatus> missions = mission.getMissions();
        Iterator<String> it = missions.keySet().iterator();
        while (it.hasNext()) {
            String missionId = it.next();
            int missionType = this.getDataConfig().get("mission_daily").get(missionId).getInteger("type");
            if (missionType == type) {
                MissionStatus status = missions.get(missionId);
                if (!status.isAccept()) {
                    status.setProgress(status.getProgress() + count);
                    dailyRepository.save(mission);
                }
                break;
            }
        }
        this.gameModel.addObject(ResponseKey.MISSION_DAILY, mission);
    }

    private MissionOnce getMissionOnce(Lord lord) {
        MissionOnce mission = onceRepository.findOne(lord.getId());
        if (mission == null) {
            mission = new MissionOnce();
            mission.setId(lord.getId());
            Map<String, Map<String, Integer>> data = new HashMap<String, Map<String, Integer>>();
            DataConfig missionOnce = this.getDataConfig().get("mission_once");
            Iterator it = missionOnce.getJsonObject().keys();
            while (it.hasNext()) {
                String missionKey = it.next().toString();
                if (missionOnce.get(missionKey).getInteger("type") == 1) {
                    // 招募任务
                    data.put(missionOnce.get(missionKey).get("progress").getJsonObject().getString("rank"),
                            new HashMap<>());
                }
            }
            mission.setData(data);
            initOnceMission(lord, mission);
        }
        flushOnceMission(lord, mission);
        return mission;
    }

    @SuppressWarnings("unchecked")
    private void flushOnceMission(Lord lord, MissionOnce mission) {
        Map<String, MissionStatus> missionMap = mission.getMissions();
        // 更新任务进度
        JSONObject config = this.getDataConfig().get("mission_once").getJsonObject();
        Iterator<String> it = missionMap.keySet().iterator();
        while (it.hasNext()) {
            String missionId = it.next();
            int type = config.getJSONObject(missionId).getInt("type");
            if (type == 0) { // 关卡任务
                // String stageId = config.getJSONObject(missionId).getJSONObject("progress").getString("stage");
                // if (mission.getData().get(type + "") != null && mission.getData().get(type +
                // "").containsKey(stageId)) {
                // missionMap.get(missionId).setProgress(1);
                // }
            } else if (type == 1) { // 招募任务
                String rank = config.getJSONObject(missionId).getJSONObject("progress").getString("rank");
                if (mission.getData().containsKey(rank)) {
                    missionMap.get(missionId).setProgress(mission.getData().get(rank).size());
                }
                // if (mission.getData().get(type + "") != null) {
                // missionMap.get(missionId).setProgress(mission.getData().get(type + "").get("rank"));
                // }
            } else if (type == 2) { // 主公等级任务
                missionMap.get(missionId).setProgress(lord.getLevel());
            }
        }

        // 是否有新任务
        boolean isGetNew = false;
        for (Iterator<String> cit = config.keys(); cit.hasNext();) {
            String missionId = cit.next();
            if (!missionMap.containsKey(missionId)) {
                JSONObject missionConfig = config.getJSONObject(missionId);
                if (missionConfig.containsKey("missionPre")) { // 有前置任务
                    isGetNew = true;
                    JSONArray configArr = missionConfig.getJSONArray("missionPre");
                    for (int i = 0; i < configArr.size(); i++) {
                        String preMission = configArr.getString(i);
                        MissionStatus ms = missionMap.get(preMission);
                        if (ms == null) {
                            isGetNew = false;
                            break;
                        } else if (!ms.isAccept()) {
                            int condition = config.getJSONObject(preMission).getJSONObject("progress").getInt("end");
                            if (ms.getProgress() < condition) {
                                isGetNew = false;
                                break;
                            }
                        }
                    }
                    if (isGetNew) {
                        missionMap.put(missionId, new MissionStatus(missionId));
                    }
                } else { // 无前置任务
                    missionMap.put(missionId, new MissionStatus(missionId));
                    isGetNew = true;
                }
            }
        }

        if (isGetNew) {
            flushOnceMission(lord, mission);
        }
    }

    private MissionDaily getMissionDaily(Lord lord) {
        MissionDaily mission = dailyRepository.findOne(lord.getId());
        if (mission == null) {
            mission = new MissionDaily();
            mission.setId(lord.getId());
            mission.setActive(0);
            Map<String, Integer> status = new HashMap<String, Integer>();
            mission.setActiveStatus(status);
            mission.setMissions(new HashMap<>());
        }
        // 跨天重置任务
        if (!DateUtils.isSameDay(new Date(mission.getLastTime()), new Date())) {
            mission.setMissions(new HashMap<>());
            mission.setActive(0);
            mission.getActiveStatus().clear();
        }
        // 刷新任务
        initDailyMission(lord, mission);
        return mission;
    }

    /**
     * @Description: 刷新每日任务列表
     * @param lord
     * @param mission
     */
    @SuppressWarnings("unchecked")
    private void initDailyMission(Lord lord, MissionDaily mission) {
        JSONObject config = this.getDataConfig().get("mission_daily").getJsonObject();
        Iterator<String> it = config.keys();
        Map<String, MissionStatus> missionMap = new HashMap<String, MissionStatus>();
        while (it.hasNext()) {
            String missionId = it.next();
            int minLv = config.getJSONObject(missionId).getInt("levelMin");
            int maxLv = config.getJSONObject(missionId).getInt("levelMax");
            if (lord.getLevel() >= minLv && lord.getLevel() <= maxLv) {
                if (mission.getMissions().containsKey(missionId)) {
                    missionMap.put(missionId, mission.getMissions().get(missionId));
                } else {
                    missionMap.put(missionId, new MissionStatus(missionId));
                }
            }
        }
        mission.setMissions(missionMap);
        mission.setLastTime(System.currentTimeMillis());
    }

    /**
     * @Description: 初始化主线任务
     * @param lord
     * @param mission
     */
    @SuppressWarnings("unchecked")
    private void initOnceMission(Lord lord, MissionOnce mission) {
        JSONObject config = this.getDataConfig().get("mission_once").getJsonObject();
        Iterator<String> it = config.keys();
        Map<String, MissionStatus> missionMap = new HashMap<String, MissionStatus>();
        while (it.hasNext()) {
            String missionId = it.next();
            if (!config.getJSONObject(missionId).containsKey("missionPre")) {
                missionMap.put(missionId, new MissionStatus(missionId));
            }
        }
        mission.setMissions(missionMap);
    }

}
