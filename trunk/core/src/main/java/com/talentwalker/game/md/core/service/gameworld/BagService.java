/**
 * @Title: BagService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月8日  闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: BagService
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月8日 下午4:16:59
 */

@Service
public class BagService extends GameSupport {

    public final static int TYPE_HERO = 1;
    public final static int TYPE_EQUIP = 2;
    public final static int TYPE_SKILL = 3;
    public final static int TYPE_ITEM = 4;
    /**
     * 随机礼包
     */
    private final static String BOX_RANDOM = "box1";
    /**
     * 全部获取礼包
     */
    private final static String BOX_ALL = "box2";
    /**
     * 需要钥匙开启宝箱
     */
    private final static String BOX_KEY = "box3";

    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private DuelService duelService;

    /**
     * @Description: 扩充背包
     * @param type 扩充的类型
     */
    public void expand(int type) {
        Lord lord = this.getLord();
        int times = 0;
        DataConfig config = null;
        int timeLimit = 0;
        switch (type) {
        case TYPE_HERO:
            times = lord.getHeroTimes();
            config = this.getDataConfig().get("bagLimit").get("heroLimit_" + (times + 1));
            timeLimit = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("heroLimit");
            break;
        case TYPE_EQUIP:
            times = lord.getEquipTimes();
            config = this.getDataConfig().get("bagLimit").get("bagEquip_" + (times + 1));
            timeLimit = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("bagEquip");
            break;
        case TYPE_SKILL:
            times = lord.getSkillTimes();
            config = this.getDataConfig().get("bagLimit").get("bagSkill_" + (times + 1));
            timeLimit = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("bagSkill");
            break;
        case TYPE_ITEM:
            times = lord.getItemTimes();
            config = this.getDataConfig().get("bagLimit").get("bagNormol_" + (times + 1));
            timeLimit = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("bagNormal");
            break;
        }

        // 检查vip等级是否可以购买
        if (times + 1 > timeLimit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21009);
        }

        // 是否达到购买上限
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21010);
        }

        // 消耗钻石
        String itemId = config.getString("type");
        int amount = config.getInteger("price");
        int add = config.getInteger("bagnumber");
        gainPayService.pay(lord, itemId, amount);

        // 扩容
        this.addBag(lord, add, type);
        lordRepository.save(lord);

    }

    private void addBag(Lord lord, int add, int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        switch (type) {
        case TYPE_HERO:
            lord.setHeroLimit(lord.getHeroLimit() + add);
            lord.setHeroTimes(lord.getHeroTimes() + 1);
            map.put("heroLimit", lord.getHeroLimit());
            map.put("heroTimes", lord.getHeroTimes());
            break;
        case TYPE_EQUIP:
            lord.setEquipLimit(lord.getEquipLimit() + add);
            lord.setEquipTimes(lord.getEquipTimes() + 1);
            map.put("equipLimit", lord.getEquipLimit());
            map.put("equipTimes", lord.getEquipTimes());
            break;
        case TYPE_SKILL:
            lord.setSkillLimit(lord.getSkillLimit() + add);
            lord.setSkillTimes(lord.getSkillTimes() + 1);
            map.put("skillLimit", lord.getSkillLimit());
            map.put("skillTimes", lord.getSkillTimes());
            break;
        case TYPE_ITEM:
            lord.setItemLimit(lord.getItemLimit() + add);
            lord.setItemTimes(lord.getItemTimes() + 1);
            map.put("itemLimit", lord.getItemLimit());
            map.put("itemTimes", lord.getItemTimes());
            break;
        }
        Object obj = this.gameModel.getModel(ResponseKey.LORD);
        if (obj != null) {
            map.putAll((Map<String, Object>) obj);
        }
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    public void sold(Map<String, Integer> itemMap) {
        Lord lord = this.getLord();

        Iterator<String> it = itemMap.keySet().iterator();
        // 消耗道具，计算获得总金币数
        while (it.hasNext()) {
            String itemId = it.next();
            int amount = itemMap.get(itemId);
            int price = this.getDataConfig().get("item").get(itemId).getInteger("price");

            gainPayService.pay(lord, itemId, amount);
            gainPayService.gain(lord, ItemID.GOLD, price * amount);
        }

        lordRepository.save(lord);
    }

    /**
     * @Description:开启礼包（一次）
     * @param itemKey
     * @throws
     */
    public void openBox(String itemId) {
        Lord lord = this.getLord();
        Integer step = lord.getGuidanceStep();
        if (step < 999) {
            // lord.setGuidanceStep(999);
            lord.setGuidanceStep(++step);
        }
        this.openBox(lord, itemId, 1);
        lordRepository.save(lord);
    }

    /**
     * @Description:开启宝箱（10次）
     * @param itemKey
     * @throws
     */
    public void openBoxTenTimes(String itemId) {
        Lord lord = this.getLord();
        this.openBox(lord, itemId, 10);
        lordRepository.save(lord);
    }

    /**
     * 
     * @Description:开启宝箱
     * @param lord
     * @param itemId
     * @param times
     * @throws
     */
    private void openBox(Lord lord, String itemId, int times) {
        DataConfig config = this.getDataConfig().get("item").get(itemId);
        List<Map<String, Integer>> boxList = new ArrayList<>();
        Map<String, Integer> awardMap = new HashMap<>();
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21013, "找不到配置");
        }
        if (!StringUtils.startsWith(config.getString("type"), "box")) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21014, "不能开启的道具");
        }
        gainPayService.pay(lord, itemId, times);
        if (BOX_RANDOM.equals(config.getString("type"))) {
            List<Map<String, Object>> params = config.get("params").getJsonArray();
            if (params == null) {
                Map<String, Integer> weightMap = new HashMap<String, Integer>();
                JSONObject paramsJson = config.get("params").getJsonObject();
                Iterator it = paramsJson.keys();
                while (it.hasNext()) {
                    String item = it.next().toString();
                    int weight = ((JSONObject) paramsJson.get(item)).getInt("weight");
                    weightMap.put(item, weight);
                }
                for (int i = 0; i < times; i++) {
                    String id = RandomUtils.randomTable(weightMap);
                    int amount = ((JSONObject) paramsJson.get(id)).getInt("value");
                    // 开箱奖励
                    if (awardMap.containsKey(id)) {
                        amount = awardMap.get(id) + amount;
                    }
                    awardMap.put(id, amount);
                    // 前端展示
                    Map<String, Integer> map = new HashMap<>();
                    map.put(id, amount);
                    boxList.add(map);
                }
            } else {
                List<Integer> weightList = new ArrayList<Integer>();
                for (Map<String, Object> map : params) {
                    weightList.add(Integer.parseInt(map.get("weight").toString()));
                }
                for (int i = 0; i < times; i++) {
                    int random = RandomUtils.randomTable(weightList);
                    Map<String, Object> award = params.get(random);
                    String id = award.get("id").toString();
                    int amount = Integer.parseInt(award.get("value").toString());
                    // 开箱奖励
                    if (awardMap.containsKey(id)) {
                        amount = awardMap.get(id) + amount;
                    }
                    awardMap.put(id, amount);
                    // 前端展示
                    Map<String, Integer> map = new HashMap<>();
                    map.put(id, amount);
                    boxList.add(map);
                }
            }
        } else if (BOX_ALL.equals(config.getString("type"))) {
            JSONObject params = config.get("params").getJsonObject();
            Iterator it = params.keys();
            for (int i = 0; i < times; i++) {
                while (it.hasNext()) {
                    String id = it.next().toString();
                    int amount = params.getInt(id);
                    // 开箱奖励
                    if (awardMap.containsKey(id)) {
                        amount = awardMap.get(id) + amount;
                    }
                    awardMap.put(id, amount);
                    // 前端展示
                    Map<String, Integer> map = new HashMap<>();
                    map.put(id, amount);
                    boxList.add(map);
                }
            }

        } else if (BOX_KEY.equals(config.getString("type"))) {
            gainPayService.pay(lord, config.getString("keybar"), 1);
            Map<String, Integer> weightMap = new HashMap<String, Integer>();
            JSONObject params = config.get("params").getJsonObject();
            Iterator it = params.keys();
            while (it.hasNext()) {
                String item = it.next().toString();
                int weight = ((JSONObject) params.get(item)).getInt("weight");
                weightMap.put(item, weight);
            }
            for (int i = 0; i < times; i++) {
                String id = RandomUtils.randomTable(weightMap);
                int amount = ((JSONObject) params.get(id)).getInt("value");
                // 开箱奖励
                if (awardMap.containsKey(id)) {
                    amount = awardMap.get(id) + amount;
                }
                awardMap.put(id, amount);
                // 前端展示
                Map<String, Integer> map = new HashMap<>();
                map.put(id, amount);
                boxList.add(map);
            }
            missionService.trigerMissionForBox(times);
        } else {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21014, "不能开启的道具");
        }
        Set<String> set = awardMap.keySet();
        for (String item : set) {
            gainPayService.gain(lord, item, awardMap.get(item));
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("boxAward", boxList);
        this.gameModel.addObject("bag", responseMap);
    }

    /**
     * @Description:召唤英雄
     * @param soulId
     * @throws
     */
    public void summon(String soulId) {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("heroSoul").get(soulId);
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21013, "找不到配置");
        }
        gainPayService.pay(lord, soulId, config.getInteger("num"));
        gainPayService.gain(lord, config.getString("heroID"), 1);
        // 记录新手引导
        int step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        lordRepository.save(lord);
    }

    /**
     * @Description:一件出售装备
     * @param rank
     * @throws
     */
    public void instantSoldEquip(List<Integer> rank) {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("equip");
        Map<String, Equip> equipMap = lord.getEquips();
        Iterator it = equipMap.entrySet().iterator();
        Map<String, Integer> map = new HashMap<String, Integer>();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String uid = entry.getKey().toString();
            Equip equip = (Equip) entry.getValue();
            String equipId = equip.getEquipId();
            if (rank.size() > 0 && !rank.contains(config.get(equipId).getInteger("rank"))) {
                continue;
            }
            if (!config.get(equipId).getJsonObject().containsKey("sellGold")) {
                // 不可卖出的装备
                continue;
            }
            if (this.checkUpLevel(equipMap.get(uid))) {
                continue;
            }
            // 是否正在使用
            if (this.checkEquipForm(lord, uid)) {
                continue;
            }
            int price = this.getDataConfig().get("equip").get(equipId).getInteger("sellGold");
            map.put(uid, price);
        }
        Iterator mapIt = map.entrySet().iterator();
        while (mapIt.hasNext()) {
            Map.Entry entrymap = (Map.Entry) mapIt.next();
            gainPayService.pay(lord, entrymap.getKey().toString(), 1);
            gainPayService.gain(lord, ItemID.GOLD, (int) entrymap.getValue());
        }
        lordRepository.save(lord);
    }

    /**
     * @Description:装备是否在阵容（防守阵容）中使用
     * @param lord
     * @param uid
     * @return
     * @throws
     */
    private boolean checkEquipForm(Lord lord, String uid) {
        if (lord.getForm().size() > 0 && lord.getForm().get(0) != null) {
            List<FormHold> forms = lord.getForm().get(0);
            if (forms != null && forms.size() > 0) {
                for (FormHold form : forms) {
                    if (form.getEquipUid().contains(uid)) {
                        return true;
                    }
                }
            }
        }
        Duel duel = duelService.getDuel(lord);
        List<FormHold> dforms = duel.getFormDefend();
        if (dforms != null && dforms.size() > 0) {
            for (FormHold dform : dforms) {
                if (dform.getEquipUid().contains(uid)) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * @Description: 检验装备是否精炼、升级
     * @param equip
     * @return true：已精炼或强化，不可售卖 false：可以售出
     * @throws
     */
    private boolean checkUpLevel(Equip equip) {
        if (equip.getLevel() > 1 || equip.getStage() > 0 || equip.getExpStage() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @Description:出售装备
     * @param equips
     * @throws
     */
    public void soldEquip(List<String> equips) {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("equip");
        for (String uid : equips) {
            String equipId = lord.getEquips().get(uid).getEquipId();
            if (!config.get(equipId).getJsonObject().containsKey("sellGold")) {
                // 不可卖出的装备
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21028, equipId + "装备不可卖出");
            }
            int price = this.getDataConfig().get("equip").get(equipId).getInteger("sellGold");
            gainPayService.pay(lord, uid, 1);
            gainPayService.gain(lord, ItemID.GOLD, price);
        }
        lordRepository.save(lord);
    }

}
