/**
 * @Title: ItemDistributionController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月15日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;

/**
 * @ClassName: ItemDistributionController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月15日 下午6:17:57
 */
@Controller
@RequestMapping(value = "statistics/itemDistribution", method = RequestMethod.GET)
public class ItemDistributionController extends BaseController {

    /**
     * @Description:页面跳转
     * @return
     * @throws
     */
    @RequestMapping(value = "main")
    public String main() {
        return "statistics/itemDistribution";
    }

}
