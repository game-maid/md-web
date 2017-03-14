/**
 * @Title: StageService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月7日  闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Stage;
import com.talentwalker.game.md.core.domain.gameworld.StageComposite;
import com.talentwalker.game.md.core.domain.gameworld.StageReward;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.StageRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomStoryType;
import com.talentwalker.game.md.core.util.RandomUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: StageService
 * @Description: 关卡
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月7日 下午5:08:45
 */

@Service
public class StageService extends GameSupport {

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private GainPayService gainPayService;

    @Autowired
    private HeroService heroService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private ShopRecruitService recruitService;

    private final static String SWEEP_ITEM = "item_1101";
    /**
     * 新手引导关卡 
     */
    private final static String GUIDE_STAGE_ID = "stage_1_1_2";

    public void enter(String stageId) {
        Lord lord = this.getLord();
        StageComposite sc = getStageComposite(lord);
        // 校验
        checkStage(lord, sc, stageId);

        // 消耗体力
        int start = this.getDataConfig().get("stage_stage").get(stageId).get("strength").getInteger("start");
        int end = this.getDataConfig().get("stage_stage").get(stageId).get("strength").getInteger("end");
        if (lord.getStrength() < (start + end)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21022, "体力不足");
        }
        gainPayService.pay(lord, ItemID.STRENGTH, start);

        lordRepository.save(lord);
    }

    public void settle(String stageId, int star) {
        Lord lord = this.getLord();
        StageComposite sc = getStageComposite(lord);
        // 校验
        checkStage(lord, sc, stageId);
        // 计算结果
        Stage stage = sc.getStage(stageId);
        int heroExp = this.getDataConfig().get("stage_stage").get(stageId).getInteger("exp");
        if (star > 0) { // 战斗胜利，计算掉落，添加武将经验等
            // 新手引导
            guide(stageId, lord);

            int strength = this.getDataConfig().get("stage_stage").get(stageId).get("strength").getInteger("end");
            gainPayService.pay(lord, ItemID.STRENGTH, strength);
            // 添加奖励
            Map<String, Integer> reward = getStageReward(sc, stageId);
            Iterator<String> it = reward.keySet().iterator();
            while (it.hasNext()) {
                String itemId = it.next();
                gainPayService.gain(lord, itemId, reward.get(itemId));
            }
            // 增加金币
            int gold = this.getDataConfig().get("stage_stage").get(stageId).getInteger("gold");
            gainPayService.gain(lord, ItemID.GOLD, gold);

            stage.setTimes(stage.getTimes() + 1); // 记录次数
            // 更新结果
            if (star > stage.getStar()) {
                stage.setStar(star);
            }
            romanceRandomStroy(lord, RandomStoryType.PVE_WIN);
            // 触发招募
            recruitService.triggeringRecruit(lord, stageId);
        } else { // 战斗失败，添加部分英雄经验
            double mul = this.getDataConfig().get("stage_other").get("loseexp").getDouble("value");
            heroExp = (int) Math.floor(mul * heroExp);
            romanceRandomStroy(lord, RandomStoryType.PVE_LOSE);
        }
        stage.setLastTime(System.currentTimeMillis());
        Map<String, Stage> stages = sc.getStages();
        stages.put(stageId, stage);
        sc.setStages(stages);
        // 添加武将经验
        addHeroExp(lord, heroExp);
        // save
        lordRepository.save(lord);
        stageRepository.save(sc);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Stage> stageMap = new HashMap<String, Stage>();
        stageMap.put(stageId, stage);
        map.put("stages", stageMap);
        map.put("current", getCurrentStage(stages));
        map.put("refresh", sc.getRefresh());
        map.put("addExp", heroExp);
        Map<String, Object> model = (Map) this.gameModel.getModel(ResponseKey.LORD);
        if (model == null) {
            model = new HashMap<>();
        }
        model.put("guidanceStep", lord.getGuidanceStep());
        this.gameModel.addObject(ResponseKey.STAGE, map);
    }

    /**
     * @Description:新手引导 自动增加步数，增加特定奖励
     * @throws
     */
    private void guide(String stageId, Lord lord) {
        // 新手引导战斗成功，自动记录新手步数
        Integer step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
    }

    private void addHeroExp(Lord lord, int exp) {
        if (lord.getForm() != null && lord.getForm().size() > 0) {
            Object obj = this.gameModel.getModel(ResponseKey.HEROES);
            Map<String, Object> map = new HashMap<String, Object>();
            if (obj != null) {
                map = (Map<String, Object>) obj;
            }
            List<FormHold> list = lord.getForm().get(0);
            for (FormHold hold : list) {
                heroService.addHeroExp(lord, hold.getHeroUid(), exp);
                map.put(hold.getHeroUid(), lord.getHeros().get(hold.getHeroUid()));
            }
            this.gameModel.addObject(ResponseKey.HEROES, map);
        }
    }

    private void addHeroExpSweep(Lord lord, int exp) {
        if (lord.getForm() != null && lord.getForm().size() > 0) {
            Map<String, Hero> map = new HashMap<String, Hero>();
            List<FormHold> list = lord.getForm().get(0);
            for (FormHold hold : list) {
                map.put(hold.getHeroUid(), lord.getHeros().get(hold.getHeroUid()));
                heroService.addHeroExp(lord, hold.getHeroUid(), exp);
            }
            this.gameModel.addObject(ResponseKey.HEROES, map);
        }
    }

    public StageComposite stageData(Lord lord) {
        StageComposite sc = getStageComposite(lord);
        sc.setCurrent(getCurrentStage(sc.getStages()));
        return sc;
    }

    private Map<String, Integer> getStageReward(StageComposite sc, String stageId) {
        Map<String, Integer> fix = getFixedReward(stageId);
        Map<String, Integer> common = getCommonReward(stageId);
        Map<String, Integer> special = getSpecialReward(sc, stageId);
        Map<String, Integer> map = new HashMap<String, Integer>();
        mergeReward(map, fix);
        mergeReward(map, common);
        mergeReward(map, special);
        return map;
    }

    private void mergeReward(Map<String, Integer> map, Map<String, Integer> source) {
        if (!source.isEmpty()) {
            Iterator<String> it = source.keySet().iterator();
            while (it.hasNext()) {
                String itemId = it.next();
                int amount = source.get(itemId);
                if (map.containsKey(itemId)) {
                    map.put(itemId, map.get(itemId) + amount);
                } else {
                    map.put(itemId, amount);
                }
            }
        }
    }

    /**
     * @Description: 固定掉落
     * @param stageId
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Integer> getFixedReward(String stageId) {
        DataConfig config = this.getDataConfig().get("stage_stage").get(stageId).get("loot").get("constant");
        if (config != null) {
            Map<String, Integer> map = (Map<String, Integer>) JSONObject.toBean(config.getJsonObject(), Map.class);
            return map;
        }
        return new HashMap<String, Integer>();
    }

    /**
     * @Description: 通用掉落
     * @param stageId
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Integer> getCommonReward(String stageId) {
        DataConfig config = this.getDataConfig().get("stage_stage").get(stageId).get("loot").get("common");
        if (config != null) {
            Map<String, Integer> weight = (Map<String, Integer>) JSONObject.toBean(config.getJsonObject(), Map.class);
            String count = RandomUtils.randomTable(weight);
            int stageLv = this.getDataConfig().get("stage_stage").get(stageId).getInteger("lv");
            return getCommonDetail(Integer.parseInt(count), stageLv);
        }
        return new HashMap<String, Integer>();
    }

    /**
     * @Description: 特殊掉落
     * @param stageId
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Integer> getSpecialReward(StageComposite sc, String stageId) {
        DataConfig config = this.getDataConfig().get("stage_stage").get(stageId).get("loot").get("unique");
        if (config == null) {
            return new HashMap<String, Integer>();
        }

        JSONObject json = config.getJsonObject();
        Iterator<String> it = json.keys();
        Map<String, Double> lootMap = sc.getLoot();
        Map<String, Integer> items = new HashMap<String, Integer>();
        while (it.hasNext()) {
            JSONObject loot = json.getJSONObject(it.next());
            if (loot != null && loot.size() > 0) {
                String itemId = loot.getString("id");
                JSONArray lootrate = loot.getJSONArray("lootrate");
                Double cri = lootMap.get(itemId);
                if (cri == null) {
                    cri = lootrate.getDouble(0);
                }

                // 计算概率
                double random = RandomUtils.randomInt(1, 100);
                if (random < cri * 100) { // 命中
                    cri = (cri - lootrate.getDouble(2) > 0) ? (cri - lootrate.getDouble(2)) : 0;
                    Map<String, Integer> weightMap = loot.getJSONObject("num");
                    String amount = RandomUtils.randomTable(weightMap);
                    items.put(itemId, Integer.parseInt(amount));
                } else { // 未命中，增加概率
                    cri += lootrate.getDouble(1);
                }
                lootMap.put(itemId, cri);
            }
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> getCommonDetail(int count, int stageLv) {
        JSONObject config = this.getDataConfig().get("stage_loot").getJsonObject();
        Iterator<String> it = config.keys();
        Map<String, Integer> weightMap = new HashMap<String, Integer>();
        while (it.hasNext()) {
            String itemId = it.next();
            JSONObject obj = (JSONObject) config.get(itemId);
            Iterator<String> its = obj.keys();
            while (its.hasNext()) {
                String lv = its.next();
                if (stageLv < Integer.parseInt(lv)) {
                    weightMap.put(itemId, obj.getInt(lv));
                    break;
                }
            }
        }

        Map<String, Integer> items = new HashMap<String, Integer>();
        for (int i = 0; i < count; i++) {
            String itemId = RandomUtils.randomTable(weightMap);
            weightMap.remove(itemId);
            items.put(itemId, 1);
        }
        return items;
    }

    private void checkStage(Lord lord, StageComposite sc, String stageId) {
        DataConfig config = this.getDataConfig().get("stage_stage").get(stageId);
        // 校验下关卡ID是否存在
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27000);
        }

        // 校验前置关卡是否通关
        String preStageId = config.getString("stagelimit");
        if (StringUtils.isNotEmpty(preStageId)) {
            Stage presStage = sc.getStages().get(preStageId);
            if (presStage == null || presStage.getStar() < 0) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27001);
            }
        }

        // 校验等级
        int lvLimit = this.getDataConfig().get("stage_bigstage").get(getChapterId(stageId)).getInteger("lvlimit");
        if (lord.getLevel() < lvLimit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27003);
        }

        // 是否为一次性关卡
        Stage stage = sc.getStage(stageId);
        int stageType = config.getInteger("stagetype");
        if (stageType == 0 && stage.getStar() > 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27004);
        }

        // 校验挑战次数
        Integer timesLimit = config.getInteger("timelimit");
        if (timesLimit != null && stage.getTimes() + 1 > timesLimit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27005);
        }

    }

    private String getChapterId(String stageId) {
        String[] str = StringUtils.split(stageId, "_");
        if (str.length != 4) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27002);
        }
        return "bigstage_" + str[1] + "_" + str[2];
    }

    private StageComposite getStageComposite(Lord lord) {
        StageComposite sc = stageRepository.findOne(lord.getId());
        if (sc == null) {
            sc = new StageComposite();
            sc.setId(lord.getId());
            sc.setStages(new HashMap<String, Stage>());
            sc.setRewards(new HashMap<String, Map<String, StageReward>>());
            sc.setRefresh(0);
            sc.setRefreshTime(System.currentTimeMillis());
        }
        Map<String, Stage> stages = sc.getStages();
        Set<String> set = stages.keySet();
        for (String stageId : set) {
            sc.getStage(stageId);
        }
        // 重置购买次数
        if (!DateUtils.isSameDay(new Date(sc.getRefreshTime()), new Date())) {
            sc.setRefresh(0);
            sc.setRefreshTime(System.currentTimeMillis());
        }
        return sc;
    }

    public void reset(String stageId) {
        Lord lord = this.getLord();

        // 一次性关卡不能重置次数
        int isOnce = this.getDataConfig().get("stage_stage").get(stageId).getInteger("stagetype");
        if (isOnce == 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27006);
        }

        StageComposite sc = getStageComposite(lord);
        Stage stage = sc.getStage(stageId);
        // 如果还有挑战次数，不需要重置
        int timesLimit = this.getDataConfig().get("stage_stage").get(stageId).getInteger("timelimit");
        if (stage.getTimes() < timesLimit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27007);
        }

        // 是否还能购买次数
        int buyMax = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("stageTimes");
        if (sc.getRefresh() + 1 > buyMax) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27008);
        }

        // 消耗钻石
        int diamond = this.getDataConfig().get("stage_refresh").get((sc.getRefresh() + 1) + "").getInteger("value");
        gainPayService.pay(lord, ItemID.DIAMOND, diamond);

        sc.setRefresh(sc.getRefresh() + 1);
        sc.setRefreshTime(System.currentTimeMillis());
        stage.setTimes(0);

        lordRepository.save(lord);
        stageRepository.save(sc);

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Stage> stageMap = new HashMap<String, Stage>();
        stageMap.put(stageId, stage);
        map.put("stages", stageMap);
        map.put("current", getCurrentStage(sc.getStages()));
        map.put("refresh", sc.getRefresh());
        this.gameModel.addObject(ResponseKey.STAGE, map);
    }

    public void sweep(String stageId, boolean isMulti) {
        Lord lord = this.getLord();

        // 一次性关卡无法扫荡
        int isOnce = this.getDataConfig().get("stage_stage").get(stageId).getInteger("stagetype");
        if (isOnce == 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27009);
        }

        StageComposite sc = getStageComposite(lord);
        Stage stage = sc.getStage(stageId);
        // 三星才能扫荡
        if (stage.getStar() < 3) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27010);
        }

        // 是否还有挑战次数
        int timesLimit = this.getDataConfig().get("stage_stage").get(stageId).getInteger("timelimit");
        if (stage.getTimes() >= timesLimit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27005);
        }

        int times = 1; // 扫荡次数
        // 如果是连续扫荡，校验是否有权限
        if (isMulti) {
            int isSweep = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("stageMultisweep");
            if (isSweep == 0) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27011);
            }

            times = timesLimit - stage.getTimes();
        }

        // 消耗体力
        int strength = getStrength(stageId);
        gainPayService.pay(lord, ItemID.STRENGTH, times * strength);

        // 扫荡卷是否足够
        Integer hasItem = lord.getItems().get(SWEEP_ITEM);
        if (hasItem == null) {
            hasItem = 0;
        }
        // 如果扫荡卷不足，扣钻石
        if (hasItem < times) {
            if (hasItem != 0) {
                gainPayService.pay(lord, SWEEP_ITEM, hasItem);
            }
            gainPayService.pay(lord, ItemID.DIAMOND, times - hasItem);
        } else {
            gainPayService.pay(lord, SWEEP_ITEM, times);
        }

        // 添加金币奖励
        int gold = this.getDataConfig().get("stage_stage").get(stageId).getInteger("gold");
        gainPayService.gain(lord, ItemID.GOLD, gold * times);

        // 添加武将经验
        int heroExp = this.getDataConfig().get("stage_stage").get(stageId).getInteger("exp");
        addHeroExpSweep(lord, heroExp * times);
        List<Map<String, Integer>> responseList = new ArrayList<Map<String, Integer>>();
        // 掉落奖励
        Map<String, Integer> reward = new HashMap<String, Integer>();
        for (int i = 0; i < times; i++) {
            Map<String, Integer> tmp = getStageReward(sc, stageId);
            mergeReward(reward, tmp); // 获得掉落
            responseList.add(tmp); // 返回前端展示掉落
        }
        Iterator<String> it = reward.keySet().iterator();
        while (it.hasNext()) {
            String itemId = it.next();
            gainPayService.gain(lord, itemId, reward.get(itemId));
        }

        stage.setTimes(stage.getTimes() + times);
        stage.setLastTime(System.currentTimeMillis());

        lordRepository.save(lord);
        stageRepository.save(sc);

        // 触发日常任务
        missionService.trigerMissionForStage(times);
        // 触发主线任务
        missionService.trigerMissionOnceForStage(stageId, times);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("reward", responseList);
        responseMap.put("addExp", heroExp * times); // 英雄经验成长值
        this.gameModel.addObject(ResponseKey.SWWEP, responseMap);

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Stage> stageMap = new HashMap<String, Stage>();
        stageMap.put(stageId, stage);
        map.put("stages", stageMap);
        map.put("current", getCurrentStage(sc.getStages()));
        map.put("refresh", sc.getRefresh());
        this.gameModel.addObject(ResponseKey.STAGE, map);
    }

    private int getStrength(String stageId) {
        int start = this.getDataConfig().get("stage_stage").get(stageId).get("strength").getInteger("start");
        int end = this.getDataConfig().get("stage_stage").get(stageId).get("strength").getInteger("end");
        return start + end;
    }

    @SuppressWarnings("unchecked")
    public void reward(String pageId, String box) {
        Lord lord = this.getLord();

        DataConfig config = this.getDataConfig().get("stage_bigstage").get(pageId).get("box").get(box);
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27012);
        }

        int star = config.getJsonObject().getInt("star"); // 需要星数

        // 计算是否满足领奖条件
        int have = 0;
        StageComposite sc = getStageComposite(lord);
        Map<String, Stage> stages = sc.getStages();
        JSONArray array = this.getDataConfig().get("stage_bigstage").get(pageId).get("stage").getJsonArray();
        for (int i = 0; i < array.size(); i++) {
            if (stages.containsKey(array.getString(i)) && stages.get(array.getString(i)).getStar() > 0) {
                have += stages.get(array.getString(i)).getStar();
            }
        }
        if (have < star) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27013);
        }

        // 校验是否领奖
        if (sc.getRewards().containsKey(pageId) && sc.getRewards().get(pageId).containsKey(box)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27014);
        }

        // 记录领取标记
        addFlag(sc, pageId, box);

        Iterator<String> reward = config.getJsonObject().getJSONObject("award").keys(); // 奖励配置
        while (reward.hasNext()) {
            String itemId = reward.next();
            int amount = config.getJsonObject().getJSONObject("award").getInt(itemId);
            gainPayService.gain(lord, itemId, amount);
        }

        lordRepository.save(lord);
        stageRepository.save(sc);
        Map<String, Object> map = new HashMap<>();
        map.put("rewards", sc.getRewards());
        this.gameModel.addObject(ResponseKey.STAGE, map);
    }

    private void addFlag(StageComposite sc, String pageId, String box) {
        Map<String, Map<String, StageReward>> map = sc.getRewards();
        if (map.containsKey(pageId)) {
            Map<String, StageReward> reward = map.get(pageId);
            reward.put(box, new StageReward());
        } else {
            Map<String, StageReward> reward = new HashMap<String, StageReward>();
            reward.put(box, new StageReward());
            map.put(pageId, reward);
        }
    }

    @SuppressWarnings("unchecked")
    private String getCurrentStage(Map<String, Stage> stages) {
        JSONObject config = this.getDataConfig().get("stage_stage").getJsonObject();
        for (Iterator<String> it = config.keys(); it.hasNext();) {
            String stageId = it.next();
            if (!stages.containsKey(stageId)) {
                return stageId;
            } else {
                if (stages.get(stageId).getStar() <= 0) {
                    return stageId;
                }
            }
        }
        return null;
    }

}
