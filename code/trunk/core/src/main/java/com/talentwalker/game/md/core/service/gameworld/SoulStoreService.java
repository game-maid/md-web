/**
 * @Title: SoulStoreService.java
 * @Copyright (C) 2016 太能沃可
 * @Description: 魂商店
 * @Revision History:
 * @Revision 1.0 2016年12月2日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.SoulStore;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.SoulStoreRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

/**
 * @ClassName: SoulStoreService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月2日 上午11:29:17
 */
@Service
public class SoulStoreService extends GameSupport {
    static final String CONFIG_RESOLVE_OTHER = "resolve_other";
    static final String CONFIG_RESOLVE_COIN = "resolve_coin";
    static final String CONFIG_RESOLVE_STORE = "resolve_store";
    static final String CONFIG_RESOLVE_STORE1 = "resolve_store1";
    static final String CONFIG_RESOLVE_STORE2 = "resolve_store2";
    static final String CONFIG_RESOLVE_STORE_REFRESH = "resolve_storeRefresh";
    @Autowired
    private SoulStoreRepository soulStoreRepository;
    @Autowired
    private GainPayService gainPayService;

    public void mainInfo() {
        Lord lord = this.getLord();
        SoulStore store = getSoulStore(lord);
        this.gameModel.addObject(ResponseKey.SOUL_STORE, store);
    }

    public SoulStore getSoulStore(Lord lord) {
        SoulStore store = soulStoreRepository.findOne(lord.getId());
        if (store == null || store.getGoods() == null) {
            store = new SoulStore();
            // 初始化魂商店
            initSoulStore(store);
            store.setId(lord.getId());
        }
        // 自动刷新
        automaticRefreshStore(store);
        return store;
    }

    /**
     * @Description:自动刷新
     * @param duel
     * @throws
     */
    private void automaticRefreshStore(SoulStore store) {
        long automaticTime = store.getAutomaticTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = DateUtils.parseDate(sdf.format(System.currentTimeMillis()), "yyyy-MM-dd", "yyyy-MM-dd");
            // 刷新时间点
            int storeRefresh1 = this.getDataConfig().get(CONFIG_RESOLVE_OTHER).get("storeRefresh1").getInteger("size")
                    * 60 * 60 * 1000;
            int storeRefresh2 = this.getDataConfig().get(CONFIG_RESOLVE_OTHER).get("storeRefresh2").getInteger("size")
                    * 60 * 60 * 1000;
            int storeRefresh3 = this.getDataConfig().get(CONFIG_RESOLVE_OTHER).get("storeRefresh3").getInteger("size")
                    * 60 * 60 * 1000;
            if (automaticTime < date.getTime() + storeRefresh3
                    && System.currentTimeMillis() >= date.getTime() + storeRefresh3) {
                store.setAutomaticTime(System.currentTimeMillis());
                store.setNextRefreshTime(date.getTime() + storeRefresh1 + 24 * 60 * 60 * 1000);
                this.refreshStore(store);
                soulStoreRepository.save(store);
            } else if (automaticTime < date.getTime() + storeRefresh2
                    && System.currentTimeMillis() >= date.getTime() + storeRefresh2) {
                store.setAutomaticTime(System.currentTimeMillis());
                store.setNextRefreshTime(date.getTime() + storeRefresh3);
                this.refreshStore(store);
                soulStoreRepository.save(store);
            } else if (automaticTime < date.getTime() + storeRefresh1
                    && System.currentTimeMillis() >= date.getTime() + storeRefresh1) {
                store.setAutomaticTime(System.currentTimeMillis());
                store.setNextRefreshTime(date.getTime() + storeRefresh2);
                this.refreshStore(store);
                soulStoreRepository.save(store);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:手动刷新商店
     * @throws
     */
    public void manuallyRefreshStore() {
        Lord lord = this.getLord();
        SoulStore store = this.getSoulStore(lord);
        int times = store.getTimes();
        times += 1;
        DataConfig config = this.getDataConfig().get(CONFIG_RESOLVE_STORE_REFRESH);
        if (!config.getJsonObject().containsKey(Integer.toString(times))) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26001);
        }
        int diamond = config.get(Integer.toString(times)).getInteger("diamond");
        gainPayService.pay(lord, ItemID.DIAMOND, diamond);
        store.setTimes(times);
        store.setRefreshTime(System.currentTimeMillis());
        this.refreshStore(store);
        soulStoreRepository.save(store);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SOUL_STORE, store);
    }

    /**
     * @Description: 购买商品
     * @param itemId
     * @throws
     */
    public void buyItem(int location) {
        Lord lord = this.getLord();
        SoulStore store = this.getSoulStore(lord);
        List<Map<String, Object>> listGoods = store.getGoods();
        Map<String, Object> map = listGoods.get(location);
        if (!(boolean) map.get("isbuy")) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26002);
        }
        String itemId = map.get("id").toString();
        int price = (int) map.get("price");
        int amount = (int) map.get("amount");
        // 消耗魂币
        gainPayService.pay(store, ItemID.SOUL_GOLD, price);

        // 获得魂商品
        gainPayService.gain(lord, itemId, amount);

        map.put("isbuy", false);
        soulStoreRepository.save(store);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SOUL_STORE, store);
    }

    /**
     * @Description:自动刷新商店
     * @param store
     * @return
     * @throws
     */
    private void refreshStore(SoulStore store) {
        List<Map<String, Object>> goodList = new ArrayList<>();
        // 商品总数量
        int storeTotal = this.getDataConfig().get(CONFIG_RESOLVE_OTHER).get("storeTotal").getInteger("size");
        DataConfig configStore = this.getDataConfig().get(CONFIG_RESOLVE_STORE);
        Map<String, Integer> weightMap = new HashMap<String, Integer>();
        Iterator it = configStore.getJsonObject().keySet().iterator();
        while (it.hasNext()) {
            String goodNum = it.next().toString();
            int weight = configStore.get(goodNum).getInteger("weight")
                    + (configStore.get(goodNum).getInteger("add") * store.getTimes());
            weightMap.put(goodNum, weight);

        }
        String randomKey = RandomUtils.randomTableDouble(weightMap);

        // 获取魂商品
        this.getGood(CONFIG_RESOLVE_STORE1, Integer.parseInt(randomKey), goodList);
        storeTotal = storeTotal - Integer.parseInt(randomKey);
        this.getGood(CONFIG_RESOLVE_STORE2, storeTotal, goodList);
        store.setGoods(goodList);
    }

    /**
     * @Description:获取魂商品
     * @param configName 配置名称
     * @param amount 数量
     * @param goodList 商品列表
     * @throws
     */
    private void getGood(String configName, int amount, List<Map<String, Object>> goodList) {
        DataConfig config = this.getDataConfig().get(configName);
        Map<String, Integer> weightMap = new HashMap<String, Integer>();
        Iterator it = config.getJsonObject().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) it.next();
            String key = mapEntry.getKey().toString();
            int weight = config.get(key).getInteger("weight");
            weightMap.put(key, weight);
        }
        for (int i = 0; i < amount; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String randomKey = RandomUtils.randomTable(weightMap);
            weightMap.remove(randomKey);
            map.put("id", config.get(randomKey).getString("id"));
            map.put("amount", config.get(randomKey).getInteger("amount"));
            map.put("price", config.get(randomKey).getInteger("price"));
            map.put("isbuy", true);
            goodList.add(map);
        }
    }

    /**
     * @Description:初始化商店
     * @param store
     * @throws
     */
    public void initSoulStore(SoulStore store) {
        store.setRefreshTime(System.currentTimeMillis());
        store.setTimes(0);
        store.setSoulGold(0);
    }
}
