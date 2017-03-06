/**
 * @Title: NoticeController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月3日  张福涛
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.NoticeService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: NoticeController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月3日 下午6:56:25
 */
@Controller
@RequestMapping(value = "game/notice", method = RequestMethod.POST)
public class NoticeController extends GameSupport {
    @Autowired
    private NoticeService noticeService;

    @RequestMapping("main")
    @GameResponse
    public Object main() {
        noticeService.main();
        return this.gameModel;
    }
}
