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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        activityRecruit.addAll(recruit);
        this.setRecruit(shopRecruit, activityRecruit);
        shopRecruitRepository.save(shopRecruit);
        return activityRecruit;
    }

    /**
     * @Description:日常和活动招募
     * @param config
     * @param recruitMap
     * @param lord
     * @param shopRecruit
     * @param activityRecruit
     * @throws
     */
    private void commonRecruit(DataConfig config, Map<String, Recruit> recruitMap, Lord lord, ShopRecruit shopRecruit,
            List<Recruit> activityRecruit) {
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
        activityRecruit = getActivityRecruit(lord, shopRecruit);
    }

    public void textTriggeringRecruit(String recruitId) {
        // 检验触发招募是否存在
        DataConfig config = this.getDataConfig().get("shop_heroRecruit");
        if (!config.getJsonObject().containsKey(recruitId)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28002);
        }
        Lord lord = this.getLord();
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
        times++;
        // 获得英雄
        String resultHeroId = null;
        // 保底次数
        boolean isRare = false; // true:必抽稀有池，false：普通池
        int safetyTimes = config.getInteger("safetyTimes");
        if (safetyTimes > 0) {
            if (times % safetyTimes == 0) {
                isRare = true;
            }
        }
        // 抽中稀有池概率
        double probability = recruit.getProbability();
        if (probability <= 0) {
            probability = config.getDouble("probability");
        }
        int random = RandomUtils.randomInt(1, 10 * 100000);
        Map<String, Integer> weightMap = new HashMap<String, Integer>();
        if (!isRare && random > probability * 100000) {
            // 普通池B池
            double probabilityUp = config.getDouble("probabilityUp");
            probability += probabilityUp; // 概率成长

            List<JSONObject> arr = config.get("poll_B").getJsonArray();
            for (JSONObject heroData : arr) {
                String heroId = heroData.getString("ID");
                Integer weight = heroData.getInt("weight");
                weightMap.put(heroId, weight);
            }
            resultHeroId = RandomUtils.randomTable(weightMap);
        } else {
            // 稀有池A池
            probability = 0; // 清空抽A池概率
            int aTimes = recruit.getaTimes();
            aTimes++;
            recruit.setaTimes(aTimes);
            List<JSONObject> arr = config.get("poll_A").getJsonArray();
            // 稀有池原权重副本（用于条件冲突，导致卡池符合条件权重数据为空时）
            Map<String, Integer> weightTemp = new HashMap<String, Integer>();
            for (JSONObject heroData : arr) {
                String heroId = heroData.getString("ID");
                Integer weight = heroData.getInt("weight");
                weightMap.put(heroId, weight);
                weightTemp.put(heroId, weight);
            }
            // 稀有不重复次数
            int norepeat = config.getInteger("norepeat");
            List<String> oldHeros = recruit.getOldHeros();
            if (oldHeros != null && oldHeros.size() > 0) {
                int end = (oldHeros.size() - norepeat) <= 0 ? 0 : oldHeros.size() - norepeat;
                for (int i = oldHeros.size() - 1; i >= end; i--) {
                    if (weightMap.containsKey(oldHeros.get(i))) {
                        weightMap.remove(oldHeros.get(i));
                    }
                }
            }
            // 获取新卡间隔
            int getnew = config.getInteger("getnew");
            Set<List> set = new HashSet<>();
            set.add(oldHeros);
            if (getnew > 0 && times % getnew == 0) {
                for (Iterator it = set.iterator(); it.hasNext();) {
                    String heroId = it.next().toString();
                    if (weightMap.containsKey(heroId)) {
                        weightMap.remove(heroId);
                    }
                }
            } else if (oldHeros.size() > norepeat) {
                // 抽旧卡(旧卡数大于重复次数，抽旧卡，否则抽新卡)
                Map<String, Integer> weightMapTemp = new HashMap<String, Integer>();
                for (Iterator it = set.iterator(); it.hasNext();) {
                    String heroId = it.next().toString();
                    if (weightMap.containsKey(heroId)) {
                        weightMapTemp.put(heroId, weightMap.get(heroId));
                    }
                }
                weightMap = weightMapTemp;
            }
            if (weightMap.size() <= 0) {
                weightMap = weightTemp;
            }
            resultHeroId = RandomUtils.randomTable(weightMap);
            oldHeros.add(resultHeroId);
            recruit.setOldHeros(oldHeros);
        }
        recruit.setTimes(times);
        recruit.setProbability(probability);
        return resultHeroId;

    }

    /**
     * @Description:
     * @param recruitKey
     * @param dataConfig
     * @throws
     */
    private void tenTimesConfigRecruit(String recruitKey, DataConfig config) {
        Lord lord = this.getLord();
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
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        ShopRecruitConfig config = shopRecruitConfigRepository.findOne(new ObjectId(recruitKey));
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28003, "未找到活动招募配置");
        }
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
        ShopRecruit shopRecruit = shopRecruitRepository.findOne(lord.getId());
        Map<String, Recruit> recruitMap = shopRecruit.getRecruit();
        ShopRecruitConfig config = shopRecruitConfigRepository.findOne(new ObjectId(recruitKey));
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28003, "未找到活动招募配置");
        }
        Recruit recruit = recruitMap.get(recruitKey);

        this.randomActivityHeroId(lord, recruit, config);
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
     * @Description:
     * @return
     * @throws
     */
    private void randomActivityHeroId(Lord lord, Recruit recruit, ShopRecruitConfig config) {
        int times = recruit.getTimes();
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
        if (probability <= 0) {
            probability = config.getProbability();
        }
        int random = RandomUtils.randomInt(1, 10 * 100000);
        Map<String, Integer> weightMap = new HashMap<String, Integer>();
        String resultHeroId = "";
        if (!isRare && random > probability * 100000) {
            // 普通池B池
            if (config.getProType() == 2) { // 2成长型
                probability += config.getProbabilityUp(); // 概率成长
            }

            List<Map<String, Object>> arr = config.getbData();
            Map<String, Integer> heroAmount = new HashMap<String, Integer>();
            for (Map<String, Object> heroData : arr) {
                String heroId = heroData.get("heroId").toString();
                Integer weight = Integer.parseInt(heroData.get("weight").toString());
                weightMap.put(heroId, weight);
                heroAmount.put(heroId, Integer.parseInt(heroData.get("amount").toString()));
            }
            // 获得英雄
            resultHeroId = RandomUtils.randomTable(weightMap);
            gainPayService.gain(lord, resultHeroId, heroAmount.get(resultHeroId));
        } else {
            // 稀有池A池
            probability = 0; // 清空抽A池概率
            int aTimes = recruit.getaTimes();
            recruit.setaTimes(aTimes++);
            List<Map<String, Object>> arr = config.getaData();
            // 稀有池原权重副本（用于条件冲突，导致卡池符合条件权重数据为空时）
            Map<String, Integer> weightTemp = new HashMap<String, Integer>();
            Map<String, Integer> heroAmount = new HashMap<String, Integer>();
            for (Map<String, Object> heroData : arr) {
                String heroId = heroData.get("heroId").toString();
                Integer weight = Integer.parseInt(heroData.get("weight").toString());
                heroAmount.put(heroId, Integer.parseInt(heroData.get("amount").toString()));
                weightMap.put(heroId, weight);
                weightTemp.put(heroId, weight);
            }
            // 稀有不重复次数
            int norepeat = config.getNorepeat();
            List<String> oldHeros = recruit.getOldHeros();
            if (oldHeros != null && oldHeros.size() > 0) {
                int end = (oldHeros.size() - norepeat) <= 0 ? 0 : oldHeros.size() - norepeat;
                for (int i = oldHeros.size() - 1; i >= end; i--) {
                    if (weightMap.containsKey(oldHeros.get(i))) {
                        weightMap.remove(oldHeros.get(i));
                    }
                }
            }
            // 容错，条件冲突时
            if (weightMap.size() <= 0) {
                weightMap = weightTemp;
            }
            // 获得英雄
            resultHeroId = RandomUtils.randomTable(weightMap);
            oldHeros.add(resultHeroId);
            recruit.setOldHeros(oldHeros);
            gainPayService.gain(lord, resultHeroId, heroAmount.get(resultHeroId));
        }
        recruit.setTimes(times);
        recruit.setProbability(probability);
        missionService.trigerMissionOnceForRecruit(resultHeroId);
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
    private List<Recruit> getActivityRecruit(Lord lord, ShopRecruit shopRecruit) {
        List<Recruit> recruitList = new ArrayList<Recruit>();
        Map<String, Recruit> map = shopRecruit.getRecruit();
        String zone = this.getGameUser().getGameZoneId();
        List<ShopRecruitConfig> config = shopRecruitConfigRepository.findByDate(System.currentTimeMillis(),
                lord.getVipLevel());
        for (int i = 0; i < config.size(); i++) {
            if (!config.get(i).getZoneList().contains(zone)) {
                continue;
            }
            if (map.containsKey(config.get(i).getId())) {
                Recruit r = map.get(config.get(i).getId());
                r.setConfig(config.get(i));
                recruitList.add(r);
                continue;
            }
            Recruit recruit = this.initRecruit();
            recruit.setType(3);
            recruit.setConfig(config.get(i));
            recruit.setId(config.get(i).getId());
            recruitList.add(recruit);
        }
        return recruitList;
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
