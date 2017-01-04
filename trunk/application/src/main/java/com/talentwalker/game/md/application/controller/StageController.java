/**
 * @Title: StageController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月7日  闫昆
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.StageService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: StageController
 * @Description: 关卡接口
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月7日 下午5:07:14
 */

@Controller
@RequestMapping(value = "game/stage", method = RequestMethod.POST)
public class StageController extends GameSupport {

    @Autowired
    private StageService stageService;

    @GameResponse
    @RequestMapping("data")
    public GameModel getData() {
        return this.gameModel;
    }

    /**
     * @Description: 进入副本
     * @param stageId
     * @return
     */
    @GameResponse
    @RequestMapping("enter/{stageId}")
    public GameModel stageEnter(@PathVariable String stageId) {
        stageService.enter(stageId);
        return this.gameModel;
    }

    /**
     * @Description: 副本结算
     * @param stageId
     * @param star 战斗结果
     * @return
     */
    @GameResponse
    @RequestMapping("settle/{stageId}/{star}")
    public GameModel stageSettle(@PathVariable String stageId, @PathVariable int star) {
        stageService.settle(stageId, star);
        return this.gameModel;
    }

    /**
     * @Description: 次数重置
     * @param stageId
     * @return
     */
    @GameResponse
    @RequestMapping("reset/{stageId}")
    public GameModel reset(@PathVariable String stageId) {
        stageService.reset(stageId);
        return this.gameModel;
    }

    /**
     * @Description: 单次扫荡
     * @param stageId
     * @return
     */
    @GameResponse
    @RequestMapping("sweep/{stageId}")
    public GameModel sweep(@PathVariable String stageId) {
        stageService.sweep(stageId, Boolean.FALSE);
        return this.gameModel;
    }

    /**
     * @Description: 多次扫荡
     * @param stageId
     * @return
     */
    @GameResponse
    @RequestMapping("multisweep/{stageId}")
    public GameModel multisweep(@PathVariable String stageId) {
        stageService.sweep(stageId, Boolean.TRUE);
        return this.gameModel;
    }

    /**
     * @Description: 领取星级奖励
     * @param pageId 章节ID
     * @param box 宝箱类型
     * @return
     */
    @GameResponse
    @RequestMapping("reward/{pageId}/{box}")
    public GameModel starReward(@PathVariable String pageId, @PathVariable String box) {
        stageService.reward(pageId, box);
        return this.gameModel;
    }

}
