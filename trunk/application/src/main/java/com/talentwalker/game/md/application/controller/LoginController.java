/**
 * @Title: LoginController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.LordService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: LoginController
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月13日 上午10:49:15
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class LoginController extends GameSupport {
    @Autowired
    private LordService lordService;

    @GameResponse
    @RequestMapping(value = "game/lord/login")
    public Object login() {
        lordService.login();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "game/lord/register/{name}")
    public Object register(@PathVariable String name) {
        lordService.register(name);
        return this.gameModel;
    }

    /**
     * @Description:随机获取名称
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("game/lord/random/name")
    public Object randomName() {
        lordService.randomName();
        return this.gameModel;
    }

    /**
     * @Description:初始化默认英雄
     * @param heroId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("game/lord/initHero/{heroId}")
    public Object initHero(@PathVariable String heroId) {
        lordService.initHero(heroId);
        return this.gameModel;
    }

}
