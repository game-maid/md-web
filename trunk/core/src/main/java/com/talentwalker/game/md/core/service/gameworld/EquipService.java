/**
 * @Title: EquipService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月21日 闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;
import com.talentwalker.game.md.core.util.UuidUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: EquipService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月21日 下午12:49:03
 */

@Service
public class EquipService extends GameSupport {

    @Autowired
    private GainPayService gainPayService;

    private void checkEquip(String equipId) {
        DataConfig config = this.getDataConfig().get("equip").get(equipId);
        if (null == config) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21005, equipId);
        }
    }

    public Equip newEquip(String equipId) {
        this.checkEquip(equipId);
        Equip equip = new Equip();
        equip.setEquipId(equipId);
        equip.setEquipUid(equipId + "_" + UuidUtils.getUuid());
        equip.setLevel(1);
        equip.setStage(0);
        equip.setExpStage(0);
        equip.setTime(System.currentTimeMillis());
        return equip;
    }

    public void strengthen(String equipUid) {
        Lord lord = this.getLord();
        // 校验是否有这件装备
        Map<String, Equip> equips = lord.getEquips();
        if (!equips.containsKey(equipUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_22001);
        }

        Equip equip = equips.get(equipUid);
        // 校验是否已经达到最高等级
        int limit = getMaxLevel(lord, equip);
        if (equip.getLevel() >= limit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_22002);
        }

        // 计算需要消耗的金币数量
        int cost = getStrengthCost(equip);
        gainPayService.pay(lord, ItemID.GOLD, cost);

        // 根据权重获取提升等级
        int up = RandomUtils.randomTable(getStrengthenWeight(equip, lord.getVipLevel())) + 1;
        equip.setLevel(equip.getLevel() + up);
        equips.put(equipUid, equip);
        lord.setEquips(equips);
        // 新手引导记录步数
        Integer step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        lordRepository.save(lord);
        Map<String, Equip> map = new HashMap<String, Equip>();
        map.put(equipUid, equip);

        this.gameModel.addObject(ResponseKey.EQUIPS, map);
        this.gameModel.addObject("strengthen", up);
    }

    @SuppressWarnings("unchecked")
    public void refine(String equipUid, JSONObject items) {
        Lord lord = this.getLord();
        // 校验是否有这件装备
        Map<String, Equip> equips = lord.getEquips();
        if (!equips.containsKey(equipUid)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_22001);
        }

        // 装备是否可以精炼
        Equip equip = equips.get(equipUid);
        int isRefine = this.getDataConfig().get("equip").get(equip.getEquipId()).getInteger("isrefine");
        if (isRefine != 1) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_22005);
        }

        int givenExp = 0;
        Iterator<String> it = items.keys();
        while (it.hasNext()) {
            String itemId = it.next();
            int amount = items.getInt(itemId);
            // 校验下是否为精炼装备
            DataConfig config = this.getDataConfig().get("item").get(itemId);
            if (!"refine".equals(config.getString("type"))) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_22003, itemId);
            }
            givenExp += (config.getInteger("params") * amount);
            gainPayService.pay(lord, itemId, amount);
        }

        int refineLv = equip.getStage();
        int refineExp = equip.getExpStage() + givenExp;
        JSONObject config = this.getDataConfig().get("equip").get(equip.getEquipId()).get("refinelv").getJsonObject();
        // 校验是否精炼满级
        if (refineLv >= config.size()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_22004);
        }
        for (int i = refineLv; i < config.size(); i++) {
            int needExp = config.getInt((i + 1) + "");
            if (refineExp < needExp) {
                break;
            }
            refineLv += 1;
            refineExp -= needExp;
        }

        // 如果精炼满级，经验清零
        if (refineLv == config.size()) {
            refineExp = 0;
        }
        equip.setExpStage(refineExp);
        equip.setStage(refineLv);
        equips.put(equipUid, equip);

        lordRepository.save(lord);

        Object obj = this.gameModel.getModel(ResponseKey.EQUIPS);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put(equipUid, equip);

        this.gameModel.addObject(ResponseKey.EQUIPS, map);

    }

    // 获得装备的等级上限
    private int getMaxLevel(Lord lord, Equip equip) {
        double lvLimit = this.getDataConfig().get("equip").get(equip.getEquipId()).getDouble("updateLvLimit");
        // 判断lvLimit是否为整数；
        if ((int) lvLimit == lvLimit) {
            // 整数时最大上限计算公式：Math.floor(lord.getLevel() * lvLimit)；
            return (int) Math.floor(lord.getLevel() * lvLimit);
        } else {
            // 小数时最大上限计算公式：Math.floor(lord.getLevel() * lvLimit)+1；
            return (int) Math.floor(lord.getLevel() * lvLimit) + 1;
        }
    }

    private int getStrengthCost(Equip equip) {
        double lvLimit = this.getDataConfig().get("equip").get(equip.getEquipId()).getDouble("updateLvLimit");
        int param = (int) Math.ceil((equip.getLevel() - 1) * (3 / lvLimit)) + 1;
        double base = this.getDataConfig().get("equipUpdate").get(param + "").getDouble("value");
        int mul = this.getDataConfig().get("equip").get(equip.getEquipId()).getInteger("updateGold");
        return (int) Math.ceil(base * mul);
    }

    private List<Integer> getStrengthenWeight(Equip equip, int vipLv) {
        double lvLimit = this.getDataConfig().get("equip").get(equip.getEquipId()).getDouble("updateLvLimit");
        if (lvLimit == 3.0) {
            return new ArrayList<Integer>();
        }
        JSONArray config = this.getDataConfig().get("VIP").get(vipLv + "").get("equipUpdateComboRate").getJsonArray();
        LinkedList<Integer> weight = new LinkedList<Integer>();
        int sum = 0;
        for (int i = 0; i < config.size(); i++) {
            int rate = (int) (config.getDouble(i) * 100);
            sum += rate;
            weight.add(i, rate);
        }
        weight.push(100 - sum);
        return weight;
    }

}
