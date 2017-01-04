/**
 * @Title: DuelController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月6日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.DuelService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: DuelController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月6日 下午3:11:34
 */
@Controller
@RequestMapping(value = "game/duel", method = RequestMethod.POST)
public class DuelController extends GameSupport {
    @Autowired
    private DuelService duelService;

    @GameResponse
    @RequestMapping(value = "main")
    public Object duelMain() {
        duelService.duelMain();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "store")
    public Object storeMain() {
        duelService.storeMain();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeHero/{index}/{heroUid}")
    public Object changeHero(@PathVariable Integer index, @PathVariable String heroUid) {
        duelService.changeHero(index, heroUid);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeHero/{index}")
    public Object removeHero(@PathVariable Integer index) {
        duelService.removeHero(index);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "exchangeHero/{index1}/{index2}")
    public Object exchange(@PathVariable Integer index1, @PathVariable Integer index2) {
        duelService.exchange(index1, index2);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeSkill/{heroIndex}/{skillIndex}/{skillUid}")
    public Object changeSkill(@PathVariable Integer heroIndex, @PathVariable Integer skillIndex,
            @PathVariable String skillUid) {
        duelService.changeSkill(heroIndex, skillIndex, skillUid);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeSkill/{heroIndex}/{skillIndex}")
    public Object removeSkill(@PathVariable Integer heroIndex, @PathVariable Integer skillIndex) {
        duelService.changeSkill(heroIndex, skillIndex, null);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeEquip/{heroIndex}/{equipIndex}/{equipUid}")
    public Object changeEquip(@PathVariable Integer heroIndex, @PathVariable Integer equipIndex,
            @PathVariable String equipUid) {
        duelService.changeEquip(heroIndex, equipIndex, equipUid);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeEquip/{heroIndex}/{equipIndex}")
    public Object removeEquip(@PathVariable Integer heroIndex, @PathVariable Integer equipIndex) {
        duelService.changeEquip(heroIndex, equipIndex, null);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "addFormHold/{index}")
    public Object addFormHold(@PathVariable Integer index) {
        duelService.addFormHold(index);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "fetchFormHold/{index}")
    public Object fetchFormHold(@PathVariable Integer index) {
        duelService.fetchFormHold(index);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "buyTimes")
    public Object buyTimes() {
        duelService.buyTimes();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "lordFormInfo/{lordId}")
    public Object lordFormInfo(@PathVariable String lordId) {
        duelService.lordFormInfo(lordId);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "buyItem/{itemId}")
    public Object buyItem(@PathVariable String itemId) {
        duelService.buyItem(itemId);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "refreshStore")
    public Object refreshStore() {
        duelService.manuallyRefreshStore();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "enter/{rank}")
    public Object enter(@PathVariable Integer rank) {
        duelService.enter(rank);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "battle/{rank}/{result}")
    public Object battle(@PathVariable Integer rank, @PathVariable Integer result) {
        duelService.battle(rank, result);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "log")
    public Object battleLog() {
        duelService.battleLog();
        return this.gameModel;
    }
}
