
package com.talentwalker.game.md.application.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.FeedbackService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: FeedbackController
 * @Description: 用户反馈
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月21日 下午7:12:31
 */
@Controller
@RequestMapping(value = "game/feedback", method = RequestMethod.POST)
public class FeedbackController extends GameSupport {
    @Resource
    private FeedbackService feedbackService;

    @GameResponse
    @RequestMapping("add/{type}/{message}")
    public GameModel addFeedback(@PathVariable Integer type, @PathVariable String message) {
        feedbackService.addFeedback(type, message);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("getFeedback")
    public GameModel getFeedback() {
        feedbackService.getFeedback();
        return gameModel;
    }

}
