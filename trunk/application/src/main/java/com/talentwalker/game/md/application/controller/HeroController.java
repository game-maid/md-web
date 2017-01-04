/**
 * @Title: HeroController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月24日 闫昆
 */

package com.talentwalker.game.md.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.HeroService;
import com.talentwalker.game.md.core.service.gameworld.MissionService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

import net.sf.json.JSONObject;

/**
 * @ClassName: HeroController
 * @Description: 武将接口
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月24日 下午4:22:36
 */

@Controller
@RequestMapping(value = "game/hero", method = RequestMethod.POST)
public class HeroController extends GameSupport {

    @Autowired
    private HeroService heroService;
    @Autowired
    private MissionService missionService;

    /**
     * @Description 武将升级
     * @param heroUid
     * @param items 升级道具
     */
    @GameResponse
    @RequestMapping(value = "addExp/{heroUid}")
    public GameModel addHeroExp(@PathVariable String heroUid, @RequestBody JSONObject items) {
        heroService.addHeroExp(heroUid, items);
        missionService.trigerMissionForHero(1);
        return this.gameModel;
    }

    /**
     * @Description 武将突破
     * @param heroUid
     * @param cost 吃掉的英雄列表
     */
    @GameResponse
    @RequestMapping(value = "break/{heroUid}")
    public GameModel breakHero(@PathVariable String heroUid, @RequestBody List<String> cost) {
        heroService.breakHero(heroUid, cost);
        return this.gameModel;
    }

    /**
     * @Description 武将觉醒
     * @param heroUid
     * @param cost 吃掉的英雄列表
     */
    @GameResponse
    @RequestMapping(value = "awake/{heroUid}")
    public GameModel awakeHero(@PathVariable String heroUid, @RequestBody List<String> cost) {
        heroService.awake(heroUid, cost);
        return this.gameModel;
    }

    /**
     * @Description:提升好感度
     * @param heroUid
     * @param items 经验道具
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "addLoveExp/{heroUid}")
    public GameModel addLoveExp(@PathVariable String heroUid, @RequestBody JSONObject items) {
        heroService.addHeroLoveExp(heroUid, items);
        return this.gameModel;
    }

}
