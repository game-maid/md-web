/**
 * @Title: SoulShopController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月2日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.SoulStoreService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: SoulShopController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月2日 上午11:21:39
 */
@Controller
@RequestMapping(value = "game/store/soul", method = RequestMethod.POST)
public class SoulStoreController extends GameSupport {
    @Autowired
    private SoulStoreService soulStoreService;

    @GameResponse
    @RequestMapping(value = "main")
    public Object storeMain() {
        soulStoreService.mainInfo();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "buyItem/{location}")
    public Object buyItem(@PathVariable Integer location) {
        soulStoreService.buyItem(location);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "refresh")
    public Object refreshStore() {
        soulStoreService.manuallyRefreshStore();
        return this.gameModel;
    }
}
