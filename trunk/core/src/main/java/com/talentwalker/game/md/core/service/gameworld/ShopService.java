/**
 * @Title: ShopService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  闫昆
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Shop;
import com.talentwalker.game.md.core.domain.gameworld.ShopItem;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.ShopRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

import net.sf.json.JSONObject;

/**
 * @ClassName: ShopService
 * @Description: 商城逻辑
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月29日 下午1:31:38
 */

@Service
public class ShopService extends GameSupport {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private GainPayService gainPayService;

    /**
     * 道具商店
     */
    public final static int SHOP_TYPE_ITEM = 1;

    /**
     * vip商店
     */
    public final static int SHOP_TYPE_VIP = 2;
    /**
     * 限量周期一天
     */
    private final static int SHOP_PERIOD_DAY = 1;
    /**
     * 限量周期一周
     */
    private final static int SHOP_PERIOD_WEEK = 1;
    /**
     * 限量周期一月
     */
    private final static int SHOP_PERIOD_MONTH = 1;

    private final static String RES_ITEMSHOP = "itemShop";
    private final static String RES_VIPSHOP = "vipShop";

    public void getShopList(int type) {
        Shop shop = getShop(this.getLord());
        Map<String, List<ShopItem>> map = new HashMap<String, List<ShopItem>>();
        if (type == SHOP_TYPE_ITEM) {
            map.put(RES_ITEMSHOP, toList(shop.getBasicShop()));
        } else if (type == SHOP_TYPE_VIP) {
            map.put(RES_VIPSHOP, toList(shop.getVipShop()));
        }

        // 过滤不能买的(购买数量达到限制不显示)
        this.checkBuyTime(map);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description:检验购买次数是否达到上限，达到上限的不显示在列表中
     * @param map
     * @throws
     */
    private void checkBuyTime(Map<String, List<ShopItem>> map) {
        for (String shopType : map.keySet()) {
            String configName = "";
            if (shopType == RES_ITEMSHOP) {
                configName = "shop_itemsell";
            } else if (shopType == RES_VIPSHOP) {
                configName = "shop_VIPitemsell";
            }
            DataConfig config = this.getDataConfig().get(configName);
            List<ShopItem> shopItems = map.get(shopType);
            for (int i = 0; i < map.get(shopType).size(); i++) {
                ShopItem shopItem = shopItems.get(i);
                if (config.get(shopItem.getItemKey()).getInteger("limit") != 0
                        && shopItem.getTimes() >= config.get(shopItem.getItemKey()).getInteger("limit")) {
                    shopItems.remove(i);
                }
            }
            map.put(shopType, shopItems);
        }

    }

    public void buy(String itemKey, int amount, int type) {
        Lord lord = this.getLord();

        // 校验参数
        if (amount < 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28000);
        }

        Shop shop = getShop(lord);
        ShopItem shopItem = getShopItem(shop, itemKey, type);
        if (shopItem == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28001);
        }
        // 配置名称
        String configName = "";
        // 购买vip商品校验vip等级
        if (type == SHOP_TYPE_VIP) {
            configName = "shop_VIPitemsell";
            int vipLimit = this.getDataConfig().get(configName).get(itemKey).getInteger("VIPlvDemand");
            if (lord.getVipLevel() < vipLimit) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21009);
            }
        } else {
            configName = "shop_itemsell";
        }
        // 校验购买次数
        int limit = this.getDataConfig().get(configName).get(itemKey).getInteger("limit");
        if (limit != 0 && shopItem.getTimes() + amount > limit) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28004, "购买数量达到上限");
        }
        // 消耗
        pay(lord, itemKey, amount, type);

        // 获得
        gainPayService.gain(lord, this.getDataConfig().get(configName).get(itemKey).getString("itemID"), amount);

        // 更新购买信息
        shopItem.setBuyTime(System.currentTimeMillis());
        shopItem.setTimes(shopItem.getTimes() + amount);

        lordRepository.save(lord);
        shopRepository.save(shop);

        getShopList(type);
    }

    private void pay(Lord lord, String itemKey, int amount, int type) {
        if (type == SHOP_TYPE_ITEM) {
            String itemId = this.getDataConfig().get("shop_itemsell").get(itemKey).getString("money");
            int number = this.getDataConfig().get("shop_itemsell").get(itemKey).getInteger("price");
            gainPayService.pay(lord, itemId, amount * number);
        } else if (type == SHOP_TYPE_VIP) {
            String itemId = this.getDataConfig().get("shop_VIPitemsell").get(itemKey).getString("money");
            int number = this.getDataConfig().get("shop_VIPitemsell").get(itemKey).getInteger("price");
            gainPayService.pay(lord, itemId, amount * number);
        }
    }

    private ShopItem getShopItem(Shop shop, String itemKey, int type) {
        if (type == SHOP_TYPE_ITEM) {
            return shop.getBasicShop().get(itemKey);
        } else if (type == SHOP_TYPE_VIP) {
            return shop.getVipShop().get(itemKey);
        }
        return null;
    }

    private Shop getShop(Lord lord) {
        Shop shop = shopRepository.findOne(lord.getId());
        if (shop == null) {
            // 初始化shop对象
            shop = new Shop();
            shop.setId(lord.getId());
        }

        initItem(shop.getBasicShop(), SHOP_TYPE_ITEM);
        initItem(shop.getVipShop(), SHOP_TYPE_VIP);

        // 根据限量周期进行重置购买次数，需宝宝完善
        checkLimeAmount(shop.getBasicShop(), SHOP_TYPE_ITEM);
        checkLimeAmount(shop.getVipShop(), SHOP_TYPE_VIP);
        return shop;
    }

    /**
     * @Description:限量周期
     * @param basicShop
     * @param shopTypeVip
     * @throws
     */
    private void checkLimeAmount(Map<String, ShopItem> shop, int type) {
        String configName = "";
        if (type == SHOP_TYPE_ITEM) {
            configName = "shop_itemsell";
        } else if (type == SHOP_TYPE_VIP) {
            configName = "shop_VIPitemsell";
        }
        DataConfig config = this.getDataConfig().get(configName);
        for (String key : shop.keySet()) {
            int limitRush = config.get(key).getInteger("limitRush");
            ShopItem shopItem = shop.get(key);
            if (SHOP_PERIOD_DAY == limitRush) {
                this.checkTime(Calendar.HOUR_OF_DAY, shopItem);
            } else if (SHOP_PERIOD_WEEK == limitRush) {
                this.checkTime(Calendar.DAY_OF_MONTH, shopItem);
            } else if (SHOP_PERIOD_MONTH == limitRush) {
                this.checkTime(Calendar.DAY_OF_WEEK, shopItem);
            }
        }
    }

    /**
     * @Description:周期刷新购买数量
     * @param hourOfDay
     * @param shopItem
     * @throws
     */
    private void checkTime(int CalendarType, ShopItem shopItem) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int now = c.get(CalendarType);
        c.setTimeInMillis(shopItem.getBuyTime());
        int old = c.get(CalendarType);
        if (now != old) {
            shopItem.setBuyTime(System.currentTimeMillis());
            shopItem.setTimes(0);
        }
    }

    /**
     * @Description: 根据配置初始化道具列表
     * @param shop
     * @param type
     */
    @SuppressWarnings("unchecked")
    private void initItem(Map<String, ShopItem> shop, int type) {
        String configName = "";
        if (type == SHOP_TYPE_ITEM) {
            configName = "shop_itemsell";
        } else if (type == SHOP_TYPE_VIP) {
            configName = "shop_VIPitemsell";
        }
        JSONObject itemSell = this.getDataConfig().get(configName).getJsonObject();
        Iterator<String> it = itemSell.keys();
        while (it.hasNext()) {
            String itemKey = it.next();
            if (!shop.containsKey(itemKey)) {
                ShopItem item = new ShopItem(itemKey);
                shop.put(itemKey, item);
            }
        }
    }

    private List<ShopItem> toList(Map<String, ShopItem> shop) {
        List<ShopItem> list = new ArrayList<ShopItem>();
        Iterator<String> it = shop.keySet().iterator();
        while (it.hasNext()) {
            list.add(shop.get(it.next()));
        }
        return list;
    }

}
