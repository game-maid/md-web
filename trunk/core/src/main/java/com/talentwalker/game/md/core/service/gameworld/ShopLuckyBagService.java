/**
 * @Title: ShopLuckyBagService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月29日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.config.ShopConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Shop;
import com.talentwalker.game.md.core.domain.gameworld.ShopItem;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.config.ShopConfigRepository;
import com.talentwalker.game.md.core.repository.gameworld.ShopRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

/**
 * @ClassName: ShopLuckyBagService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月29日 下午5:50:20
 */
@Service
public class ShopLuckyBagService extends GameSupport {
    @Autowired
    private ShopConfigRepository shopConfigRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private GainPayService gainPayService;

    private final static String RES_LUCKBAG = "luckBag";

    /**
     * 随机获取
     */
    private final static int TYPE_ACQUIRE = 1;
    /**
     * 全部获取
     */
    private final static int TYPE_ACQUIRE_ALL = 2;

    /**
     * @Description:
     * @throws
     */
    public void getShopList() {
        Lord lord = this.getLord();
        Map<String, List<ShopItem>> map = new HashMap<String, List<ShopItem>>();
        List<ShopItem> shopItemList = this.getShopList(lord);
        map.put(RES_LUCKBAG, shopItemList);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description:
     * @param shop
     * @throws
     */
    private List<ShopItem> getShopList(Lord lord) {
        Shop shop = shopRepository.findOne(lord.getId());
        if (shop == null) {
            // 初始化shop对象
            shop = new Shop();
            shop.setId(lord.getId());
        }
        List<ShopItem> list = this.initItem(shop);
        shopRepository.save(shop);
        return list;
    }

    /**
     * @Description:
     * @param shop
     * @throws
     */
    private List<ShopItem> initItem(Shop shop) {
        Map<String, ShopItem> shopLuckBag = shop.getLuckyBagShop();
        String zone = this.getGameUser().getGameZoneId();
        List<ShopConfig> config = shopConfigRepository.findByDate(System.currentTimeMillis(),
                this.getLord().getVipLevel());
        Map<String, ShopItem> mapShop = new HashMap<>();
        List<ShopItem> shopItems = new ArrayList<>();
        for (int i = 0; i < config.size(); i++) {
            if (!config.get(i).getZoneList().contains(zone)) {
                continue;
            }
            if (shopLuckBag.containsKey(config.get(i).getId())) {
                ShopItem item = shopLuckBag.get(config.get(i).getId());
                mapShop.put(config.get(i).getId(), item);
                // 检验次数是否大于限制
                if (item.getTimes() >= config.get(i).getLimitAmount()) {
                    continue;
                }
                item.setConfig(config.get(i));
                shopItems.add(item);
                continue;
            }
            ShopItem item = new ShopItem(config.get(i).getId());
            mapShop.put(config.get(i).getId(), item);
            item.setConfig(config.get(i));
            shopItems.add(item);
        }
        shop.setLuckyBagShop(mapShop);
        return shopItems;

    }

    /**
     * @Description:
     * @param itemKey
     * @param amount
     * @throws
     */
    public void buy(String itemKey, Integer amount) {
        Lord lord = this.getLord();
        // 校验参数
        if (amount < 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28000);
        }

        Shop shop = shopRepository.findOne(lord.getId());
        ShopItem shopItem = shop.getLuckyBagShop().get(itemKey);
        if (shopItem == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28005, "不存在的福袋");
        }
        ShopConfig config = this.getShopConfig(itemKey, lord);
        // 检验购买次数
        if (shopItem.getTimes() + amount > config.getLimitAmount()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28004, "购买数量达到上限");
        }
        // 消耗
        pay(lord, itemKey, amount, config);

        // 获得
        gain(lord, itemKey, amount, config);

        // 更新购买信息
        shopItem.setBuyTime(System.currentTimeMillis());
        shopItem.setTimes(shopItem.getTimes() + amount);

        lordRepository.save(lord);
        shopRepository.save(shop);

        getShopList();

    }

    /**
     * @Description:
     * @param lord
     * @param itemKey
     * @param amount
     * @param config
     * @throws
     */
    private void gain(Lord lord, String itemKey, Integer amount, ShopConfig config) {
        String itemId = config.getMoney();
        int price = config.getPrice();
        gainPayService.pay(lord, itemId, price * amount);
    }

    /**
     * @Description:
     * @param lord
     * @param itemKey
     * @param amount
     * @param config
     * @throws
     */
    private void pay(Lord lord, String itemKey, Integer amount, ShopConfig config) {
        List<Map<String, Object>> list = config.getItemData();
        if (config.getAcquireType() == TYPE_ACQUIRE) {
            List<Integer> weightList = new ArrayList<Integer>();
            for (Map<String, Object> map : list) {
                weightList.add(Integer.parseInt(map.get("weight") + ""));
            }
            int random = RandomUtils.randomTable(weightList);
            gainPayService.gain(lord, list.get(random).get("id") + "",
                    Integer.parseInt(list.get(random).get("amount") + "") * amount);
        } else {
            for (Map<String, Object> map : list) {
                gainPayService.gain(lord, map.get("id") + "", Integer.parseInt(map.get("amount") + "") * amount);
            }
        }
    }

    /**
     * @Description:
     * @param itemKey
     * @return
     * @throws
     */
    private ShopConfig getShopConfig(String itemKey, Lord lord) {
        ShopConfig config = shopConfigRepository.findOne(new ObjectId(itemKey));
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28005, "不存在的福袋");
        }
        if (lord.getLevel() < config.getLimitVip()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21009, "VIP等级不足");
        }
        if (System.currentTimeMillis() > config.getEndTime() || System.currentTimeMillis() < config.getStartTime()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28006, "福袋已经过期");
        }
        return config;
    }
}
