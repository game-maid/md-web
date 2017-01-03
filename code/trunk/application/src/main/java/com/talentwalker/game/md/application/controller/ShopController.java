/**
 * @Title: ShopController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  闫昆
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.ShopService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ShopController
 * @Description: 商店接口
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月29日 下午3:40:42
 */

@Controller
@RequestMapping(value = "game/shop", method = RequestMethod.POST)
public class ShopController extends GameSupport {

    @Autowired
    private ShopService shopService;

    /**
     * @Description: 获取普通道具商店商品列表
     * @return
     */
    @GameResponse
    @RequestMapping("item/list")
    public GameModel itemList() {
        shopService.getShopList(ShopService.SHOP_TYPE_ITEM);
        return this.gameModel;
    }

    /**
     * @Description: 获取vip商店商品列表
     * @return
     */
    @GameResponse
    @RequestMapping("vip/list")
    public GameModel vipList() {
        shopService.getShopList(ShopService.SHOP_TYPE_VIP);
        return this.gameModel;
    }

    /**
     * @Description: 购买道具商店道具
     * @param itemKey
     * @param amount
     * @return
     */
    @GameResponse
    @RequestMapping("item/buy/{itemKey}/{amount}")
    public GameModel buyItem(@PathVariable String itemKey, @PathVariable int amount) {
        shopService.buy(itemKey, amount, ShopService.SHOP_TYPE_ITEM);
        return this.gameModel;
    }

    /**
     * @Description: 购买VIP商店道具
     * @param itemKey
     * @param amount
     * @return
     */
    @GameResponse
    @RequestMapping("vip/buy/{itemKey}/{amount}")
    public GameModel buyVip(@PathVariable String itemKey, @PathVariable int amount) {
        shopService.buy(itemKey, amount, ShopService.SHOP_TYPE_VIP);
        return this.gameModel;
    }

}
