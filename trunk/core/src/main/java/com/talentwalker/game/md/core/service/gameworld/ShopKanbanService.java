/**
 * @Title: ShopKanbanService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月18日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.config.ShopKanbanConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.ShopKanban;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.config.ShopKanbanConfigRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: ShopKanbanService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月18日 下午1:58:36
 */
@Service
public class ShopKanbanService extends GameSupport {
    @Autowired
    private ShopKanbanConfigRepository shopKanbanConfigRepository;
    @Autowired
    private GainPayService gainPayService;
    private final static String KANBAN_SELL_KEY = "kanban_sell";
    private final static String RES_KANBAN = "kanban";
    private final static String KANBAN_ID = "kanbanId";

    public void initKanBan(Lord lord) {
        DataConfig config = this.getDataConfig().get(KANBAN_SELL_KEY);
        if (config != null) {
            Iterator it = config.getJsonObject().keys();
            while (it.hasNext()) {
                String kanbanKey = it.next().toString();
                if (config.get(kanbanKey).get("itemId") == null) {
                    List<String> list = new ArrayList<>();
                    list.add(kanbanKey);
                    lord.setKanbanId(kanbanKey);
                    lord.setKanbanIds(list);
                    break;
                }
            }
        }
    }

    /**
     * @Description:获取看板商店所有列表
     * @throws
     */
    public void getKanBanList() {
        Lord lord = this.getLord();
        List<ShopKanban> list = new ArrayList<>();
        Map<String, ShopKanban> shop = this.getActivity(lord);
        Set<String> set = shop.keySet();
        for (String goodId : set) {
            list.add(shop.get(goodId));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RES_KANBAN, list);
        this.gameModel.addObject(ResponseKey.SHOP, map);
    }

    /**
     * @Description:
     * @param kanbanId
     * @throws
     */
    public void buy(String kanbanId) {
        Lord lord = this.getLord();
        Map<String, ShopKanban> ShopMap = this.getActivity(lord);
        if (!ShopMap.containsKey(kanbanId)) {
            // 商店活动已结束
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28007, "活动已结束");
        }
        ShopKanban shop = ShopMap.get(kanbanId);
        DataConfig config = this.getDataConfig().get(KANBAN_SELL_KEY).get(kanbanId);
        String itemId = config.getString("itemId");
        int amount = shop.getPrice();
        int itemAmount = lord.getItems().get(itemId) == null ? 0 : lord.getItems().get(itemId);
        if (itemAmount < amount) {
            int purchase = this.getDataConfig().get("item").get(itemId).getInteger("purchase");
            // 直接使用钻石购买
            gainPayService.pay(lord, ItemID.DIAMOND, (amount - itemAmount) * purchase);
            amount = itemAmount;
        }
        // 消耗道具
        if (amount > 0) {
            gainPayService.pay(lord, itemId, amount);
        }
        // 获得看板娘
        List<String> kanbanIds = lord.getKanbanIds();
        if (kanbanIds == null) {
            kanbanIds = new ArrayList<>();
        }
        kanbanIds.add(kanbanId);
        lord.setKanbanIds(kanbanIds);
        lord.setKanbanId(kanbanId);

        // 封装前端展示数据
        ShopMap.remove(kanbanId);
        List<ShopKanban> list = new ArrayList<>();
        Set<String> set = ShopMap.keySet();
        for (String goodId : set) {
            list.add(ShopMap.get(goodId));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RES_KANBAN, list);
        // 修改当前看板娘id
        lord.setKanbanId(kanbanId);
        lordRepository.save(lord);
        this.gameModel.addObject(ResponseKey.SHOP, map);

        Object obj = this.gameModel.getModel(ResponseKey.LORD);
        Map<String, Object> lordMap = new HashMap<String, Object>();
        if (obj != null) {
            lordMap = (Map<String, Object>) obj;
        }
        lordMap.put(KANBAN_ID, kanbanId);
        this.gameModel.addObject(ResponseKey.LORD, lordMap);
        this.gameModel.addObject(ResponseKey.KANBAN_IDS, lord.getKanbanIds());
    }

    /**
     * @Description: 获取活动
     * @param id
     * @throws
     */
    private Map<String, ShopKanban> getActivity(Lord lord) {
        String zoneId = this.getGameUser().getGameZoneId();
        List<ShopKanbanConfig> list = shopKanbanConfigRepository.findByDate(System.currentTimeMillis());
        Map<String, ShopKanban> map = new HashMap<String, ShopKanban>();
        List<String> kanbanIds = lord.getKanbanIds();
        for (ShopKanbanConfig config : list) {
            if (config.getZoneList().contains(zoneId)) {
                Map<String, Integer> goods = config.getGood();
                Set<String> set = goods.keySet();
                for (String goodId : set) {
                    if (kanbanIds != null && kanbanIds.contains(goodId)) {
                        // 已经存在看板id
                        continue;
                    }
                    ShopKanban kanban = new ShopKanban();
                    kanban.setEndTime(config.getEndTime());
                    kanban.setPutrush(config.getPutrush());
                    // 折前价格
                    Integer price = getDataConfig().get(KANBAN_SELL_KEY).get(goodId).getInteger("count");
                    kanban.setPrice((int) Math.ceil(goods.get(goodId) * price / 10));
                    kanban.setId(goodId);
                    map.put(goodId, kanban);
                }
            }
        }
        return map;
    }

    /**
     * @Description:更换看板娘id
     * @param kanbanId
     * @throws
     */
    public void change(String kanbanId) {
        Lord lord = getLord();
        if (!lord.getKanbanIds().contains(kanbanId)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21006, "没有拥有此看板娘,请购买后使用");
        }
        lord.setKanbanId(kanbanId);
        lordRepository.save(lord);
        Map<String, String> map = new HashMap<String, String>();
        map.put(KANBAN_ID, kanbanId);
        gameModel.addObject(ResponseKey.LORD, map);
    }
}
