/**
 * @Title: ResolveController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月2日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.ResolveService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ResolveController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月2日 下午4:49:28
 */
@Controller
@RequestMapping(value = "game/resolve", method = RequestMethod.POST)
public class ResolveController extends GameSupport {
    @Autowired
    private ResolveService resolveService;

    @GameResponse
    @RequestMapping(value = "soul")
    public GameModel resolveSoul(@RequestBody Map<String, Integer> soulIds) {
        resolveService.resolveSoul(soulIds);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "soul/instant")
    public GameModel instantResolveSoul() {
        resolveService.instantResolveSoul();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "hero")
    public GameModel resolveHero(@RequestBody List<String> heroUids) {
        resolveService.resolveHero(heroUids);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "hero/instant")
    public GameModel instantResolveHero() {
        resolveService.instantResolveHero();
        return this.gameModel;
    }
}
