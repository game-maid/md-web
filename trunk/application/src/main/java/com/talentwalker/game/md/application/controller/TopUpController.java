/**
 * @Title: TopUpController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月8日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.TopUpService;
import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: TopUpController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月8日 下午3:39:02
 */
@Controller
@RequestMapping(value = "game/topUp", method = RequestMethod.POST)
public class TopUpController extends GameSupport {
    @Autowired
    private TopUpService topUpService;

    @GameResponse
    @RequestMapping(value = "firstRecord")
    public GameModel topUpFirstRecord() {
        Lord lord = this.getLord();
        topUpService.getFirstRecord();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "award")
    public GameModel firstAward() {
        topUpService.getFirstAward();
        return this.gameModel;
    }
}
