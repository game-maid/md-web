/**
 * @Title: GameLogController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月16日 闫昆
 */

package com.talentwalker.game.md.admin.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.GameLog;
import com.talentwalker.game.md.core.service.GameLogService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: GameLogController
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月16日 上午9:59:17
 */

@Controller
@RequestMapping("gamelog")
public class GameLogController extends BaseController {

    @Autowired
    private GameLogService gamelogService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "log/gamelog";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<GameLog> list() {
        return gamelogService.findAll();
    }

}
