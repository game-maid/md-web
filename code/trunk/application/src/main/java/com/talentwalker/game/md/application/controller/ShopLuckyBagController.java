/**
 * @Title: ShopLuckyBag.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月29日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.ShopLuckyBagService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ShopLuckyBag
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月29日 下午5:46:06
 */
@Controller
@RequestMapping(value = "game/shopLuckyBag", method = RequestMethod.POST)
public class ShopLuckyBagController extends GameSupport {
    @Autowired
    private ShopLuckyBagService shopLuckyBagService;

    /**
     * @Description: 获取普通道具商店商品列表
     * @return
     */
    @GameResponse
    @RequestMapping("list")
    public GameModel itemList() {
        shopLuckyBagService.getShopList();
        return this.gameModel;
    }

    /**
     * 
     * @Description:购买
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("buy/{itemKey}/{amount}")
    public GameModel buy(@PathVariable String itemKey, @PathVariable Integer amount) {
        shopLuckyBagService.buy(itemKey, amount);
        return this.gameModel;
    }
}
