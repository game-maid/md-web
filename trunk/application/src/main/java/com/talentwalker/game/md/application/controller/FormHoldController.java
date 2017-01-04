/**
 * @Title: FormHoldController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月22日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.FormHoldService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: FormHoldController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月22日 下午3:58:28
 */
@Controller
@RequestMapping(value = "game/form", method = RequestMethod.POST)
public class FormHoldController extends GameSupport {
    @Autowired
    private FormHoldService formHoldService;

    @GameResponse
    @RequestMapping(value = "changeHero/{index}/{heroUid}")
    public Object changeHero(@PathVariable Integer index, @PathVariable String heroUid) {
        formHoldService.changeHero(index, heroUid);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeHero/{index}")
    public Object removeHero(@PathVariable Integer index) {
        formHoldService.removeHero(index);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "exchangeHero/{index1}/{index2}")
    public Object exchange(@PathVariable Integer index1, @PathVariable Integer index2) {
        formHoldService.exchange(index1, index2);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "addFormHold/{index}")
    public Object addFormHold(@PathVariable Integer index) {
        formHoldService.addFormHold(index);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "fetchFormHold/{index}")
    public Object fetchFormHold(@PathVariable Integer index) {
        formHoldService.fetchFormHold(index);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeSkill/{heroIndex}/{skillIndex}/{skillUid}")
    public Object changeSkill(@PathVariable Integer heroIndex, @PathVariable Integer skillIndex,
            @PathVariable String skillUid) {
        formHoldService.changeSkill(heroIndex, skillIndex, skillUid);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeSkill/{heroIndex}/{skillIndex}")
    public Object removeSkill(@PathVariable Integer heroIndex, @PathVariable Integer skillIndex) {
        formHoldService.changeSkill(heroIndex, skillIndex, null);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeEquip/{heroIndex}/{equipIndex}/{equipUid}")
    public Object changeEquip(@PathVariable Integer heroIndex, @PathVariable Integer equipIndex,
            @PathVariable String equipUid) {
        formHoldService.changeEquip(heroIndex, equipIndex, equipUid);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeEquip/{heroIndex}/{equipIndex}")
    public Object removeEquip(@PathVariable Integer heroIndex, @PathVariable Integer equipIndex) {
        formHoldService.changeEquip(heroIndex, equipIndex, null);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "unlockForm/{index}")
    public Object unlockForm(@PathVariable Integer index) {
        formHoldService.unlockForm(index);
        return this.gameModel;
    }
}
