/**
 * @Title: ShopRecruitService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月3日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.config.ShopRecruitConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Recruit;
import com.talentwalker.game.md.core.domain.gameworld.ShopRecruit;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.config.ShopRecruitConfigRepository;
import com.talentwalker.game.md.core.repository.gameworld.ShopRecruitRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: ShopRecruitService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月3日 下午5:10:56
 */
@Service
public class ShopRecruitService extends GameSupport {
    @Autowired
    private ShopRecruitRepository shopRecruitRepository;
    @Autowired
    private ShopRecruitConfigRepository shopRecruitConfigRepository;
    @Autowired
    private GameUserRepository gameUserRepository;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private MissionService missionService;

    public void recruitMain() {
        Lord lord = this.getLord();
        // 等级校验
        this.isLevelOpen(ConfigKey.HERO_RECRUITSOUL, lord, true);
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        List<Recruit> recruit = this.getShopRecruit(shopRecruit, lord);
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruit);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description: 常驻招募和触发招募
     * @param shopRecruit
     * @return
     * @throws
     */
    private List<Recruit> getResidentRecruit(ShopRecruit shopRecruit) {
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");
        Iterator it = config.getJsonObject().keys();
        List<Recruit> recruitList = new ArrayList<Recruit>();
        while (it.hasNext()) {
            String recruitId = it.next().toString();
            if (shopRecruit.getRecruit().containsKey(recruitId)) {
                if (this.checkOverdue(shopRecruit.getRecruit().get(recruitId), config.get(recruitId))) {
                    continue;
                }
                recruitList.add(shopRecruit.getRecruit().get(recruitId));
            }
        }
        return recruitList;
    }

    /**
     * @Description:获取招募数据
     * @param lord
     * @return
     * @throws
     */
    private List<Recruit> getShopRecruit(ShopRecruit shopRecruit, Lord lord) {
        List<Recruit> activityRecruit = new ArrayList<>();
        DataConfig config = this.getDataConfig().get(ConfigKey.SHOP_HERO_RECRUIT);
        int guidanceRcruit = lord.getGuidanceRcruit();
        if (guidanceRcruit == 0 && shopRecruit == null) {
            shopRecruit = new ShopRecruit();
            shopRecruit.setId(lord.getId());
            Map<String, Recruit> mapRecruit = new HashMap<String, Recruit>();
            Recruit recruit = initRecruit();
            recruit.setId(ConfigKey.SHOP_HERO_RECRUIT_FIRST);
            recruit.setType(4);
            mapRecruit.put(ConfigKey.SHOP_HERO_RECRUIT_FIRST, recruit);
            shopRecruit.setRecruit(mapRecruit);
        } else if (guidanceRcruit >= 1) {
            Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
            recruitMap.remove(ConfigKey.SHOP_HERO_RECRUIT_FIRST);
            commonRecruit(config, recruitMap, lord, shopRecruit, activityRecruit);
        }
        // 常驻招募和触发招募
        List<Recruit> recruit = getResidentRecruit(shopRecruit);
        // 当前存在的活动招募
        getActivityRecruit(lord, shopRecruit, activityRecruit);

        activityRecruit.addAll(recruit);
        this.setRecruit(shopRecruit, activityRecruit);
        shopRecruitRepository.save(shopRecruit);
        return activityRecruit;
    }

    /**
     * @Description:日常和活动招募
     * @param config
     * @param recruitMap 原有招募列表
     * @param lord
     * @param shopRecruit
     * @param activityRecruit 新的招募列表
     * @throws
     */
    private void commonRecruit(DataConfig config, Map<String, Recruit> recruitMap, Lord lord, ShopRecruit shopRecruit,
            List<Recruit> activityRecruit) {
        // 检查现有活动招募是否过期
        Set<String> keySet = recruitMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            Recruit recruit = recruitMap.get(iterator.next());
            if (recruit.getType() == Recruit.TYPE_ACTIVITY) {// 活动招募
                ShopRecruitConfig activityConfig = recruit.getConfig();
                if (activityConfig == null) {
                    continue;
                }
                long endTime = activityConfig.getEndTime();
                if (System.currentTimeMillis() >= endTime || !activityConfig.getState()) {
                    iterator.remove();
                }
            }
        }
        // 普通招募
        Iterator<String> it = config.getJsonObject().keys();
        while (it.hasNext()) {
            String recruitId = it.next();
            Integer type = config.get(recruitId).getInteger(ConfigKey.SHOP_HERO_RECRUIT_TYPE);
            if (type == 1 && !recruitMap.containsKey(recruitId)) {// 基础招募
                Recruit recruit = initRecruit();
                recruit.setId(recruitId);
                recruit.setType(1);
                recruitMap.put(recruitId, recruit);
            }
        }
        // 当前存在的活动招募
        getActivityRecruit(lord, shopRecruit, activityRecruit);
    }

    public void triggeringRecruit(Lord lord, String condition) {
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Iterator<String> it = config.getJsonObject().keys();
        while (it.hasNext()) {
            String recruitId = it.next();
            if (shopRecruit != null && shopRecruit.getRecruit().containsKey(recruitId)) {
                continue;
            }
            Integer type = config.get(recruitId).getInteger(ConfigKey.SHOP_HERO_RECRUIT_TYPE);
            if (type == 2) {// 触发招募
                if (config.get(recruitId).get("typeby").equals(condition)) {
                    // 初始化触发招募
                    Recruit triggeringRecruit = initRecruit();
                    triggeringRecruit.setId(recruitId);
                    triggeringRecruit.setType(2);
                    shopRecruit.getRecruit().put(recruitId, triggeringRecruit);
                    // 获取当前招募列表
                    List<Recruit> recruit = this.getShopRecruit(shopRecruit, lord);
                    Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
                    map.put("recruit", recruit);
                    shopRecruitRepository.save(shopRecruit);
                    this.gameModel.addObject(ResponseKey.SHOP, map);
                    return;
                }
            }
        }
    }

    public void triggeringRecruit(String recruitId) {
        Lord lord = this.getLord();
        // 检验触发招募是否存在
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");
        if (!config.getJsonObject().containsKey(recruitId)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28002);
        }
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        // 初始化触发招募
        Recruit triggeringRecruit = initRecruit();
        triggeringRecruit.setId(recruitId);
        triggeringRecruit.setType(2);
        shopRecruit.getRecruit().put(recruitId, triggeringRecruit);
        // 获取当前招募列表
        List<Recruit> recruit = this.getShopRecruit(shopRecruit, lord);
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruit);
        shopRecruitRepository.save(shopRecruit);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description: 
     * @param shopRecruit
     * @param recruit
     * @throws
     */
    private void setRecruit(ShopRecruit shopRecruit, List<Recruit> recruit) {
        Map<String, Recruit> mapRecruit = new HashMap<String, Recruit>();
        for (Recruit r : recruit) {
            mapRecruit.put(r.getId(), r);
        }
        shopRecruit.setRecruit(mapRecruit);
    }

    /**
     * 
     * @Description:校验是否逾期
     * @param recruit
     * @param config
     * @return true:逾期，false:未逾期
     * @throws
     */
    private boolean checkOverdue(Recruit recruit, DataConfig config) {
        int type = config.getInteger("type");
        int getTime = config.getInteger("gettime");
        if (type == 2 && getTime > 0) {// 触发招募
            long time = recruit.getTriggeringTime() + getTime * 60 * 1000;
            if (time < System.currentTimeMillis()) {
                return true;
            }
            int times = config.getJsonObject().containsKey("gettimes") ? config.getInteger("gettimes") : 0;
            if (recruit.getTimes() >= times) {
                return true;
            }
        }

        return false;

    }

    /**
     * @Description:招募一次
     * @param recruitKey
     * @throws
     */
    public void oneTimesRecruit(String recruitKey) {
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");
        if (config.getJsonObject().containsKey(recruitKey)) {
            // 配置招募
            this.singleExtract(recruitKey, config.get(recruitKey));
        } else {
            // 活动招募
            this.activityRecruit(recruitKey);
        }
    }

    /**
     * @Description:十连抽
     * @param recruitKey
     * @throws
     */
    public void tenTimesRecruit(String recruitKey) {
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");
        if (config.getJsonObject().containsKey(recruitKey)) {
            // 配置招募
            this.tenTimesConfigRecruit(recruitKey, config.get(recruitKey));
        } else {
            // 活动招募
            this.tenTimesActivityRecruit(recruitKey);
        }
    }

    /**
     * @Description:配置招募（基础招募和活动招募）
     * @param recruitKey
     * @param config
     * @throws
     */
    private void singleExtract(String recruitKey, DataConfig config) {
        Lord lord = this.getLord();
        // 等级校验
        this.isLevelOpen(ConfigKey.HERO_RECRUITSOUL, lord, true);
        // 剧情引导记录步数
        guide(lord);
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        Recruit recruit = recruitMap.get(recruitKey);
        // 抽中英雄
        String resultHeroId = this.randomHero(recruit, config);
        // 计算消耗
        int oneTimes = recruit.getOneTimes() + 1;
        recruit.setOneTimes(oneTimes);
        String cost = config.getString("cost");
        int costAmount = 0;
        if (oneTimes >= 3) {
            costAmount = config.getInteger("single_3");
        } else if (oneTimes >= 2) {
            costAmount = config.getInteger("single_2");
        } else {
            costAmount = config.getInteger("single_1");
        }
        if (costAmount != 0) {
            gainPayService.pay(lord, cost, costAmount);
        }
        gainPayService.gain(lord, resultHeroId, 1);

        recruitMap.put(recruitKey, recruit);
        shopRecruit.setRecruit(recruitMap);
        List<Recruit> recruitList = this.getShopRecruit(shopRecruit, lord);
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruitList);
        shopRecruitRepository.save(shopRecruit);

        lord.setGuidanceRcruit(lord.getGuidanceRcruit() + 1);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SHOP, map);
        missionService.trigerMissionOnceForRecruit(resultHeroId);
    }

    /**
     * @Description:剧情引导记录步数
     * @param lord
     * @throws
     */
    private void guide(Lord lord) {
        int step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
    }

    /**
     * @Description:
     * @param recruit
     * @throws
     */
    private String randomHero(Recruit recruit, DataConfig config) {
        int times = recruit.getTimes();
        // 刷新卡池（C、D）
        refreshPoll(times, recruit, config);
        times++;
        // 获得英雄
        String resultHeroId = null;
        // 保底次数
        boolean isRare = false; // true:必抽稀有池，false：普通池
        int safetyTimes = config.getInteger(ConfigKey.SHOP_HERO_RECRUIT_SAFETY_TIMES);
        if (safetyTimes > 0) {
            if (times % safetyTimes == 0) {
                isRare = true;
            }
        }
        // 抽中稀有池概率
        double probability = recruit.getProbability();
        int random = RandomUtils.randomInt(1, 1 * 100000);
        if (!isRare && random > probability * 100000) {
            // 普通池A池
            double probabilityUp = config.getDouble(ConfigKey.SHOP_HERO_RECRUIT_PROBABILITYUP);
            int aTimes = recruit.getaTimes();
            recruit.setaTimes(++aTimes);
            probability += probabilityUp; // 概率成长
            Map<String, Integer> pollC = recruit.getPollC();
            resultHeroId = RandomUtils.randomTable(pollC);
        } else {
            // 稀有池B池
            probability = config.getDouble("probability"); // 清空抽B池概率
            Map<String, Integer> pollD = recruit.getPollD();
            resultHeroId = RandomUtils.randomTable(pollD);
        }
        recruit.setTimes(times);
        recruit.setProbability(probability);
        return resultHeroId;

    }

    /**
     * @Description:刷新C、D卡池（活动招募）
     * @param times
     * @param recruit
     * @throws
     */
    private void refreshPoll(int times, Recruit recruit, ShopRecruitConfig config) {
        Map<String, Integer> pollC = recruit.getPollC();
        Map<String, Integer> pollD = recruit.getPollD();
        if (times == 0) {
            Integer startC = config.getStartA();
            Integer startD = config.getStartB();
            addPoll(startC, config.getaData(), pollC);
            addPoll(startD, config.getbData(), pollD);
        } else {
            // C卡池
            Integer limitTimesC = config.getEveryTimesA();
            if (recruit.getTimes() % limitTimesC == 0) {
                Integer numC = config.getAddA();
                addPoll(numC, config.getaData(), pollC);
            }
            // D卡池
            Integer limitTimesD = config.getEveryTimesB();
            if (recruit.getTimes() % limitTimesD == 0) {
                Integer numD = config.getAddB();
                addPoll(numD, config.getbData(), pollD);
            }
        }
    }

    /**
     * @Description:刷新C、D卡池（配置招募）
     * @param times
     * @param recruit
     * @throws
     */
    private void refreshPoll(int times, Recruit recruit, DataConfig config) {
        Map<String, Integer> pollC = recruit.getPollC();
        Map<String, Integer> pollD = recruit.getPollD();
        if (times == 0) {
            Integer startC = config.getInteger(ConfigKey.SHOP_HERO_RECRUIT_START_C);
            Integer startD = config.getInteger(ConfigKey.SHOP_HERO_RECRUIT_START_D);
            addPoll(startC, config.get(ConfigKey.SHOP_HERO_RECRUIT_POLL_A), pollC);
            addPoll(startD, config.get(ConfigKey.SHOP_HERO_RECRUIT_POLL_B), pollD);
        } else {
            List<Integer> goingCList = config.get(ConfigKey.SHOP_HERO_RECRUIT_GOING_C).getJsonArray();
            List<Integer> goingDList = config.get(ConfigKey.SHOP_HERO_RECRUIT_GOING_C).getJsonArray();
            // C卡池
            Integer limitTimesC = goingCList.get(0);
            if (recruit.getTimes() % limitTimesC == 0) {
                Integer numC = goingCList.get(1);
                addPoll(numC, config.get(ConfigKey.SHOP_HERO_RECRUIT_POLL_A), pollC);
            }
            // D卡池
            Integer limitTimesD = goingDList.get(0);
            if (recruit.getTimes() % limitTimesD == 0) {
                Integer numD = goingDList.get(1);
                addPoll(numD, config.get(ConfigKey.SHOP_HERO_RECRUIT_POLL_B), pollD);
            }
        }
    }

    /**
     * @Description:向C/D池中 添加英雄（配置招募）
     * @param num :添加数量
     * @param config ：
     * @param pollRecurit ：目标卡池
     * @param pollKey ： ConfigKey.SHOP_HERO_RECRUIT_POLL_A    /  ConfigKey.SHOP_HERO_RECRUIT_POLL_B
     * @throws
     */
    private void addPoll(Integer num, DataConfig config, Map<String, Integer> pollRecurit) {
        List<JSONObject> poll = config.getJsonArray();
        // 获得添加权重列表 去除CD卡池中已有
        Map<String, Integer> gotoWeightMap = new HashMap<>();
        for (JSONObject json : poll) {
            String heroId = json.getString(ConfigKey.SHOP_HERO_RECRUIT_HEROID);
            if (pollRecurit.containsKey(heroId)) {
                continue;
            }
            int gotoWeight = json.getInt(ConfigKey.SHOP_HERO_RECRUIT_GOTOWEIGHT);
            gotoWeightMap.put(heroId, gotoWeight);
        }
        // 添加
        for (int i = 0; i < num; i++) {
            if (gotoWeightMap.size() == 0) {
                return;
            }
            String heroId = RandomUtils.randomTable(gotoWeightMap);
            for (JSONObject jsonA : poll) {
                if (jsonA.getString(ConfigKey.SHOP_HERO_RECRUIT_HEROID).equals(heroId)) {
                    int getWeight = jsonA.getInt(ConfigKey.SHOP_HERO_RECRUIT_GETWEIGHT);
                    pollRecurit.put(heroId, getWeight);
                }
            }
            gotoWeightMap.remove(heroId);
        }
    }

    /**
     * @Description:向C/D池中 添加英雄(活动招募)
     * @param num :添加数量
     * @param config ：
     * @param pollRecurit ：目标卡池
     * @param pollKey ： ConfigKey.SHOP_HERO_RECRUIT_POLL_A    /  ConfigKey.SHOP_HERO_RECRUIT_POLL_B
     * @throws
     */
    private void addPoll(Integer num, List<Map<String, Object>> poll, Map<String, Integer> pollRecurit) {
        // 获得添加权重列表 去除CD卡池中已有
        Map<String, Integer> gotoWeightMap = new HashMap<>();
        for (Map<String, Object> map : poll) {
            String heroId = (String) map.get("heroId");
            if (pollRecurit.containsKey(heroId)) {
                continue;
            }
            int gotoWeight = Integer.parseInt(map.get("gotoWeight").toString());
            gotoWeightMap.put(heroId, gotoWeight);
        }
        // 添加
        for (int i = 0; i < num; i++) {
            if (gotoWeightMap.size() == 0) {
                return;
            }
            String heroId = RandomUtils.randomTable(gotoWeightMap);
            for (Map<String, Object> map : poll) {
                if (map.get("heroId").equals(heroId)) {
                    int getWeight = Integer.parseInt(map.get("getWeight").toString());
                    pollRecurit.put(heroId, getWeight);
                }
            }
            gotoWeightMap.remove(heroId);
        }
    }

    /**
     * @Description:
     * @param recruitKey
     * @param dataConfig
     * @throws
     */
    private void tenTimesConfigRecruit(String recruitKey, DataConfig config) {
        Lord lord = this.getLord();
        // 等级校验
        this.isLevelOpen(ConfigKey.HERO_RECRUITSOUL, lord, true);
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        Recruit recruit = recruitMap.get(recruitKey);
        for (int i = 0; i < 10; i++) {
            // 抽中英雄
            String resultHeroId = this.randomHero(recruit, config);
            gainPayService.gain(lord, resultHeroId, 1);
            missionService.trigerMissionOnceForRecruit(resultHeroId);
        }
        // 计算消耗
        String cost = config.getString("cost");
        int tenTimes = recruit.getTenTimes() + 1;
        int costAmount = 0;
        if (tenTimes <= 1) {
            costAmount = config.getInteger("tentimes_1");
        } else if (tenTimes <= 2) {
            costAmount = config.getInteger("tentimes_2");
        } else {
            costAmount = config.getInteger("tentimes_3");
        }
        gainPayService.pay(lord, cost, costAmount);
        recruit.setTenTimes(tenTimes);
        recruitMap.put(recruitKey, recruit);
        shopRecruit.setRecruit(recruitMap);
        // 刷新招募列表
        List<Recruit> recruitList = this.getShopRecruit(shopRecruit, lord);
        // 返回值格式
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruitList);
        this.gameModel.addObject(ResponseKey.SHOP, map);
        shopRecruitRepository.save(shopRecruit);
        lordRepository.save(lord);
    }

    /**
     * @Description:
     * @param recruitKey
     * @throws
     */
    private void tenTimesActivityRecruit(String recruitKey) {
        Lord lord = this.getLord();
        // 等级校验
        this.isLevelOpen(ConfigKey.HERO_RECRUITSOUL, lord, true);
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        ShopRecruitConfig config = shopRecruitConfigRepository.findOne(new ObjectId(recruitKey));
        // 校验
        checkActiveRecruit(config);

        Recruit recruit = recruitMap.get(recruitKey);
        for (int i = 0; i < 10; i++) {
            this.randomActivityHeroId(lord, recruit, config);
        }
        int tenTimes = recruit.getTenTimes() + 1;
        // 计算消耗
        String cost = config.getCost();
        int costAmount = 0;
        if (tenTimes <= 1) {
            costAmount = config.getAlonePrice1();
        } else if (tenTimes <= 2) {
            costAmount = config.getAlonePrice2();
        } else {
            costAmount = config.getAlonePrice3();
        }
        gainPayService.pay(lord, cost, costAmount);
        recruit.setTenTimes(tenTimes);
        recruitMap.put(recruitKey, recruit);
        shopRecruit.setRecruit(recruitMap);
        // 刷新招募列表
        List<Recruit> recruitList = this.getShopRecruit(shopRecruit, lord);
        // 格式化返回值
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruitList);
        shopRecruitRepository.save(shopRecruit);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description:活动招募
     * @throws
     */
    private void activityRecruit(String recruitKey) {
        Lord lord = this.getLord();
        // 等级校验
        this.isLevelOpen(ConfigKey.HERO_RECRUITSOUL, lord, true);
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        ShopRecruitConfig config = recruitMap.get(recruitKey).getConfig();
        // 检查活动招募
        checkActiveRecruit(config);
        Recruit recruit = recruitMap.get(recruitKey);
        // 抽中英雄
        String resultHeroId = this.randomActivityHeroId(lord, recruit, config);
        int oneTimes = recruit.getOneTimes() + 1;
        recruit.setOneTimes(oneTimes);
        // 计算消耗
        String cost = config.getCost();
        int costAmount = 0;
        if (oneTimes <= 1) {
            costAmount = config.getAlonePrice1();
        } else if (oneTimes <= 2) {
            costAmount = config.getAlonePrice2();
        } else {
            costAmount = config.getAlonePrice3();
        }
        gainPayService.pay(lord, cost, costAmount);

        recruitMap.put(recruitKey, recruit);
        shopRecruit.setRecruit(recruitMap);
        // 刷新招募列表
        List<Recruit> recruitList = this.getShopRecruit(shopRecruit, lord);
        // 格式化返回值
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruitList);
        shopRecruitRepository.save(shopRecruit);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description:检查活动招募是否存在 过期 关闭
     * @param config
     * @throws
     */
    private void checkActiveRecruit(ShopRecruitConfig config) {
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28003, "未找到活动招募配置");
        }
        if (!config.getState() || config.getEndTime() < System.currentTimeMillis()) {// 已经关闭
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28007, "活动已经结束");
        }
    }

    /**
     * @Description:活动招募抽中武将
     * @param lord
     * @param recruit
     * @param config
     * @return
     * @throws
     */
    private String randomActivityHeroId(Lord lord, Recruit recruit, ShopRecruitConfig config) {
        int times = recruit.getTimes();
        // 刷新poll池
        refreshPoll(times, recruit, config);
        times++;
        // 保底次数
        boolean isRare = false; // true:必抽稀有池，false：普通池
        int safetyTimes = config.getSafetyTimes();
        if (safetyTimes > 0) {
            if (times % safetyTimes == 0) {
                isRare = true;
            }
        }
        // 抽中稀有池概率
        double probability = recruit.getProbability();
        int random = RandomUtils.randomInt(1, 1 * 100000);
        String resultHeroId;
        if (!isRare && random > probability * 100000) {
            // 普通池A池
            double probabilityUp = config.getProbabilityUp();
            int aTimes = recruit.getaTimes();
            recruit.setaTimes(++aTimes);
            probability += probabilityUp; // 概率成长
            Map<String, Integer> pollC = recruit.getPollC();
            resultHeroId = RandomUtils.randomTable(pollC);
            for (Map<String, Object> map : config.getaData()) {
                if (map.get("heroId").equals(resultHeroId)) {
                    gainPayService.gain(lord, resultHeroId, (int) map.get("amount"));
                }
            }
        } else {
            // 稀有池B池
            probability = config.getProbability(); // 清空抽B池概率
            Map<String, Integer> pollD = recruit.getPollD();
            resultHeroId = RandomUtils.randomTable(pollD);
            for (Map<String, Object> map : config.getbData()) {
                if (map.get("heroId").equals(resultHeroId)) {
                    gainPayService.gain(lord, resultHeroId, (int) map.get("amount"));
                }
            }
        }
        recruit.setTimes(times);
        recruit.setProbability(probability);
        if (resultHeroId.startsWith(ItemID.HERO)) {
            missionService.trigerMissionOnceForRecruit(resultHeroId);
        }
        return resultHeroId;
    }

    private Recruit initRecruit() {
        Recruit recruit = new Recruit();
        recruit.setProbability(0);
        recruit.setaTimes(0);
        recruit.setTimes(0);
        recruit.setOldHeros(new ArrayList<>());
        recruit.setTriggeringTime(System.currentTimeMillis());
        return recruit;
    }

    /**
     * @Description: 活动招募
     * @param lord
     * @param shopRecruit 现主公身上的招募
     * @return
     * @throws
     */
    private void getActivityRecruit(Lord lord, ShopRecruit shopRecruit, List<Recruit> activityRecruit) {
        Map<String, Recruit> map = shopRecruit.getRecruit();
        String zone = this.getGameUser().getGameZoneId();
        // 添加新增招募
        List<ShopRecruitConfig> config = shopRecruitConfigRepository.findByDate(System.currentTimeMillis());
        for (int i = 0; i < config.size(); i++) {
            if (!config.get(i).getZoneList().contains(zone)) {
                continue;
            }
            if (map.containsKey(config.get(i).getId())) {
                Recruit r = map.get(config.get(i).getId());
                r.setConfig(config.get(i));
                activityRecruit.add(r);
                continue;
            }
            Recruit recruit = this.initRecruit();
            recruit.setType(Recruit.TYPE_ACTIVITY);
            recruit.setConfig(config.get(i));
            recruit.setId(config.get(i).getId());
            activityRecruit.add(recruit);
        }
    }

    /**
     * @Description:新手引导招募武将
     * @param recruitKey
     * @throws
     */
    public void guideRecruit(String recruitKey) {
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");

        Lord lord = this.getLord();

        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        Recruit recruit = recruitMap.get(recruitKey);
        // 抽中英雄
        String resultHeroId = this.randomHero(recruit, config.get(recruitKey));

        gainPayService.gain(lord, resultHeroId, 1);
        recruitMap.put(recruitKey, recruit);
        shopRecruit.setRecruit(recruitMap);
        List<Recruit> recruitList = this.getShopRecruit(shopRecruit, lord);
        Map<String, List<Recruit>> map = new HashMap<String, List<Recruit>>();
        map.put("recruit", recruitList);
        Integer step = lord.getGuidanceStep();
        lord.setGuidanceStep(++step);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SHOP, map);
        // 主线任务
        // missionService.trigerMissionOnceForRecruit(resultHeroId);
    }
}
