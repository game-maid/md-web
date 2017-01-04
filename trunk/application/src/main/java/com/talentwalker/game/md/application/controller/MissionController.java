/**
 * @Title: MissionController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  闫昆
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.MissionService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: MissionController
 * @Description: 任务接口，包括每日任务和主线任务
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年8月5日 下午2:34:07
 */

@Controller
@RequestMapping(value = "game/mission", method = RequestMethod.POST)
public class MissionController extends GameSupport {

    @Autowired
    private MissionService missionService;

    /**
     * @Description: 领取日常奖励
     * @param missionId
     * @return
     */
    @GameResponse
    @RequestMapping("daily/{missionId}")
    public GameModel getDailyReward(@PathVariable String missionId) {
        missionService.getDailyReward(missionId);
        return this.gameModel;
    }

    /**
     * @Description: 领取活跃奖励
     * @param box 宝箱key，-》mission_active的key
     * @return
     */
    @GameResponse
    @RequestMapping("active/{box}")
    public GameModel getActiveReward(@PathVariable String box) {
        missionService.getActiveReward(box);
        return this.gameModel;
    }

    /**
     * @Description: 领取主线任务奖励
     * @param missionId
     * @return
     */
    @GameResponse
    @RequestMapping("once/{missionId}")
    public GameModel getOnceReward(@PathVariable String missionId) {
        missionService.getOnceReward(missionId);
        return this.gameModel;
    }

}
