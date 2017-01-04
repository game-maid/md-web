/**
 * @Title: RecruitController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月3日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.MissionService;
import com.talentwalker.game.md.core.service.gameworld.ShopRecruitService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: RecruitController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月3日 下午2:30:31
 */
@Controller
@RequestMapping(value = "game/recruit", method = RequestMethod.POST)
public class ShopRecruitController extends GameSupport {
    @Autowired
    private ShopRecruitService shopRecruitService;
    @Autowired
    private MissionService missionService;

    @GameResponse
    @RequestMapping(value = "main")
    public Object recruitMain() {
        shopRecruitService.recruitMain();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "textTriggering/{recruitKey}")
    public Object textTriggering(@PathVariable String recruitKey) {
        shopRecruitService.textTriggeringRecruit(recruitKey);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "oneTimesRecruit/{recruitKey}")
    public Object oneTimesRecruit(@PathVariable String recruitKey) {
        shopRecruitService.oneTimesRecruit(recruitKey);
        missionService.trigerMissionForRecruit(1);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "tenTimesRecruit/{recruitKey}")
    public Object tenTimesRecruit(@PathVariable String recruitKey) {
        shopRecruitService.tenTimesRecruit(recruitKey);
        missionService.trigerMissionForRecruit(10);
        return this.gameModel;
    }

    /**
     * @Description:新手引导招募武将
     * @param recruitKey
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("guideRecruit/{recruitKey}")
    public Object guideRecruit(@PathVariable String recruitKey) {
        shopRecruitService.guideRecruit(recruitKey);
        return this.gameModel;
    }
}
