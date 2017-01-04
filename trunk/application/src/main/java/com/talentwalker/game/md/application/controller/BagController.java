/**
 * @Title: BagController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月8日  闫昆
 */

package com.talentwalker.game.md.application.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.BagService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: BagController
 * @Description: 背包接口
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月8日 下午4:16:06
 */

@Controller
@RequestMapping(value = "game/bag", method = RequestMethod.POST)
public class BagController extends GameSupport {

    @Autowired
    private BagService bagService;

    /**
     * @Description: 扩充英雄背包
     * @return
     */
    @GameResponse
    @RequestMapping("expandHero")
    public GameModel expandHero() {
        bagService.expand(BagService.TYPE_HERO);
        return this.gameModel;
    }

    /**
     * @Description: 扩充装备背包
     * @return
     */
    @GameResponse
    @RequestMapping("expandEquip")
    public GameModel expandEquip() {
        bagService.expand(BagService.TYPE_EQUIP);
        return this.gameModel;
    }

    /**
     * @Description: 扩充技能背包
     * @return
     */
    @GameResponse
    @RequestMapping("expandSkill")
    public GameModel expandSkill() {
        bagService.expand(BagService.TYPE_SKILL);
        return this.gameModel;
    }

    /**
     * @Description: 扩充道具背包
     * @return
     */
    @GameResponse
    @RequestMapping("expandItem")
    public GameModel expandItem() {
        bagService.expand(BagService.TYPE_ITEM);
        return this.gameModel;
    }

    /**
     * @Description: 出售道具
     * @param itemMap json格式
     * @return
     */
    @GameResponse
    @RequestMapping("sold")
    public GameModel sold(@RequestBody Map<String, Integer> itemMap) {
        bagService.sold(itemMap);
        return this.gameModel;
    }

    /**
     * @Description:一件出售装备
     * @param rank 品阶
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("instant/sold/equip")
    public GameModel instantSoldEquip(@RequestBody List<Integer> ranks) {
        bagService.instantSoldEquip(ranks);
        return this.gameModel;
    }

    /**
     * @Description:出售装备
     * @param equips 装备列表 ["uid1","uid2"...]
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("sold/equip")
    public GameModel soldEquip(@RequestBody List<String> equips) {
        bagService.soldEquip(equips);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("open/{itemId}")
    public GameModel openBox(@PathVariable String itemId) {
        bagService.openBox(itemId);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("open/tenTimes/{itemId}")
    public GameModel openBoxTenTimes(@PathVariable String itemId) {
        bagService.openBoxTenTimes(itemId);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("summon/{soulId}")
    public GameModel summon(@PathVariable String soulId) {
        bagService.summon(soulId);
        return this.gameModel;
    }
}
