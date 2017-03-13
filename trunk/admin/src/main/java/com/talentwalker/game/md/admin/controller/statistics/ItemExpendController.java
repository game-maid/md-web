/**
 * @Title: ItemExpendController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月13日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ItemExpendController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月13日 下午5:41:42
 */
@Controller
@RequestMapping(value = "statistics/itemExpend", method = RequestMethod.GET)
public class ItemExpendController extends BaseController {
    /**
     * @Description:页面跳转
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "main")
    public String main() {
        return "statistics/itemExpend";
    }

}
