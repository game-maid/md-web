/**
 * @Title: ResolveService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月2日  赵丽宝
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
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.SoulStore;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.SoulStoreRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

/**
 * @ClassName: ResolveService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月2日 下午4:48:08
 */
@Service
public class ResolveService extends GameSupport {
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private SoulStoreService soulStoreService;
    @Autowired
    private SoulStoreRepository soulStoreRepository;
    @Autowired
    private DuelService duelService;
    static final Integer CONFIG_STAR_LIMIT = 3;

    /**
     * @Description:分解魂
     * @param soulUids
     * @throws
     */
    public void resolveSoul(Map<String, Integer> soulIds) {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("heroSoul");
        double resolve = 0; // 突破币
        double iconValue = 0; // 魂币
        Iterator<String> it = soulIds.keySet().iterator();
        while (it.hasNext()) {
            String id = it.next();
            gainPayService.pay(lord, id, soulIds.get(id));
            resolve += config.get(id).getDouble("resolve") * soulIds.get(id);
            iconValue += config.get(id).getDouble("iconValue") * soulIds.get(id);
        }
        int soulGold = this.getSoulGold((int) Math.floor(iconValue));
        if ((int) Math.floor(resolve) > 0) {
            gainPayService.gain(lord, ItemID.BREAKCOIN, (int) Math.floor(resolve));
            lordRepository.save(lord);
        }
        // 获得魂币
        if (soulGold > 0) {
            SoulStore store = soulStoreService.getSoulStore(lord);
            gainPayService.gain(store, ItemID.SOUL_GOLD, soulGold);
            soulStoreRepository.save(store);
        }

    }

    /**
     * @Description:分解英雄
     * @param heroUids
     * @throws
     */
    public void resolveHero(List<String> heroUids) {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("heroConfig");
        int resolve = 0; // 突破币
        int iconValue = 0; // 魂币
        for (String uid : heroUids) {
            String heroId = lord.getHeros().get(uid).getHeroId();
            resolve += config.get(heroId).getInteger("resolve");
            iconValue += config.get(heroId).getInteger("iconValue");
            gainPayService.pay(lord, uid, 1);
        }
        int soulGold = this.getSoulGold(iconValue);
        if (soulGold > 0) {
            // 获得魂币
            SoulStore store = soulStoreService.getSoulStore(lord);
            gainPayService.gain(store, ItemID.SOUL_GOLD, soulGold);
            soulStoreRepository.save(store);
        }
        if (resolve > 0) {
            // 获得突破币
            gainPayService.gain(lord, ItemID.BREAKCOIN, resolve);
            lordRepository.save(lord);
        }
    }

    /**
     * @Description:根据区间值获得魂币
     * @param iconValue
     * @return
     * @throws
     */
    private int getSoulGold(int iconValue) {
        DataConfig config = this.getDataConfig().get("resolve_coin");
        Iterator it = config.getJsonObject().keys();
        int coinKey = 0; // 当前区间
        while (it.hasNext()) {
            String key = it.next().toString();
            if (iconValue >= coinKey && iconValue < Integer.parseInt(key)) {
                break;
            }
            coinKey = Integer.parseInt(key);
        }
        int icon = config.get(String.valueOf(coinKey)).getInteger("icon");
        Map<String, Integer> weightMap = config.get(String.valueOf(coinKey)).get("plus").getJsonObject();
        String index = RandomUtils.randomTableDouble(weightMap);
        int plus = StringUtils.isEmpty(index) ? 0 : Integer.parseInt(index);
        icon += plus;
        return icon;
    }

    /**
     * @Description: 一件分解魂
     * @param ranks
     * @throws
     */
    public void instantResolveSoul() {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("heroSoul");
        double resolve = 0; // 突破币
        double iconValue = 0; // 魂币
        Map<String, Integer> soulIds = new HashMap<String, Integer>();
        soulIds.putAll(lord.getSouls());
        if (soulIds.size() <= 0) {
            // 没有可被分解的魂
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_36000);
        }
        Iterator<String> it = soulIds.keySet().iterator();
        while (it.hasNext()) {
            String id = it.next();
            // 大于三星的魂不可一件分解
            if (config.get(id).getInteger("star") > CONFIG_STAR_LIMIT) {
                continue;
            }
            int amount = soulIds.get(id);
            resolve += config.get(id).getDouble("resolve") * amount;
            iconValue += config.get(id).getDouble("iconValue") * amount;
            gainPayService.pay(lord, id, amount);
        }
        if (lord.getSouls().size() >= soulIds.size()) {
            // 没有可被分解的魂
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_36000);
        }
        int soulGold = this.getSoulGold((int) Math.floor(iconValue));
        if ((int) Math.floor(resolve) > 0) {
            gainPayService.gain(lord, ItemID.BREAKCOIN, (int) Math.floor(resolve));
            lordRepository.save(lord);
        }
        // 获得魂币
        if (soulGold > 0) {
            SoulStore store = soulStoreService.getSoulStore(lord);
            gainPayService.gain(store, ItemID.SOUL_GOLD, soulGold);
            soulStoreRepository.save(store);
        }
    }

    /**
     * @Description:
     * @param heroUids
     * @throws
     */
    public void instantResolveHero() {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("heroConfig");
        int resolve = 0; // 突破币
        int iconValue = 0; // 魂币
        Map<String, Hero> heros = new HashMap<String, Hero>();
        heros.putAll(lord.getHeros());
        if (heros.size() <= 0) {
            // 没有可分解的英雄
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_36001);
        }
        Iterator it = heros.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapEnt = (Map.Entry) it.next();
            String heroUid = mapEnt.getKey().toString();
            Hero hero = (Hero) mapEnt.getValue();
            String heroId = hero.getHeroId();
            // 大于三星的英雄不可一件分解
            if (config.get(heroId).getInteger("star") > CONFIG_STAR_LIMIT) {
                continue;
            }
            // 升级后的英雄不可一件分解
            if (hero.getExp() > 0 || hero.getLevel() > 1 || hero.getBreakLevel() > 0) {
                continue;
            }
            // 已觉醒英雄不可卖出
            if (config.get(heroId).getInteger("isStarup") == 2) {
                continue;
            }
            // 在阵容的英雄不可分解
            if (this.checkHeroForm(lord, heroUid)) {
                continue;
            }
            resolve += config.get(heroId).getInteger("resolve");
            iconValue += config.get(heroId).getInteger("iconValue");
            gainPayService.pay(lord, heroUid, 1);
        }
        if (lord.getHeros().size() >= heros.size()) {
            // 没有可分解的英雄
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_36001);
        }
        int soulGold = this.getSoulGold(iconValue);
        if (soulGold > 0) {
            // 获得魂币
            SoulStore store = soulStoreService.getSoulStore(lord);
            gainPayService.gain(store, ItemID.SOUL_GOLD, soulGold);
            soulStoreRepository.save(store);
        }
        if (resolve > 0) {
            // 获得突破币
            gainPayService.gain(lord, ItemID.BREAKCOIN, resolve);
            lordRepository.save(lord);
        }
    }

    /**
     * @Description:检验英雄是否在阵容和擂台防守阵容中
     * @param lord
     * @param heroUid 
     * @return true:被使用的英雄不可分解，false:可分解
     * @throws
     */
    private boolean checkHeroForm(Lord lord, String heroUid) {
        if (lord.getForm().size() > 0 && lord.getForm().get(0) != null) {
            List<FormHold> forms = lord.getForm().get(0);
            if (forms != null && forms.size() > 0) {
                for (FormHold form : forms) {
                    if (form.getHeroUid().equals(heroUid)) {
                        return true;
                    }
                }
            }
        }
        Duel duel = duelService.getDuel(lord);
        List<FormHold> dforms = duel.getFormDefend();
        if (dforms != null && dforms.size() > 0) {
            for (FormHold dform : dforms) {
                if (dform.getHeroUid().equals(heroUid)) {
                    return true;
                }
            }
        }
        return false;
    }
}
