/**
 * @Title: ShopKanbanController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月18日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.ShopKanbanService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * 看板娘商店
 * @ClassName: ShopKanbanController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月18日 下午1:50:06
 */
@Controller
@RequestMapping(value = "game/kanban", method = RequestMethod.POST)
public class ShopKanbanController extends GameSupport {

    @Autowired
    private ShopKanbanService shopKanbanService;

    /**
     * @Description: 商店列表
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("sell/list")
    public Object getKanBanList() {
        shopKanbanService.getKanBanList();
        return this.gameModel;
    }

    /**
     * @Description: 兑换看板娘
     * @param kanbanId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("buy/{kanbanId}")
    public Object buyKanBan(@PathVariable String kanbanId) {
        shopKanbanService.buy(kanbanId);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("change/{kanbanId}")
    public Object change(@PathVariable String kanbanId) {
        shopKanbanService.change(kanbanId);
        return gameModel;
    }
}
