/**
 * @Title: DuelRobotControll.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月8日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.gmtool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.gmtool.DuelRobotService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: DuelRobotControll
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月8日 下午12:01:15
 */
@Controller
@RequestMapping(value = "duelRobot")
public class DuelRobotControll extends BaseController {
    @Autowired
    private DuelRobotService duelRobotService;

    @RequestMapping(value = "generate", method = RequestMethod.GET)
    public String forward(Model model) {
        return "gmtool/duelRobot";
    }

    @GameResponse
    @RequestMapping(value = "generate", method = RequestMethod.POST)
    public Object generate(String zoneId) {
        duelRobotService.generate(zoneId);
        return null;
    }
}
