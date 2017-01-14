/**
 * @Title: GoldDiamondExpendController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月13日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: GoldDiamondExpendController
 * @Description: 金币钻石消耗统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月13日 下午2:01:40
 */
@Controller
@RequestMapping(value = "statistics/goldDiamondExpend", method = RequestMethod.GET)
public class GoldDiamondExpendController extends BaseController {
    @GameResponse
    @RequestMapping(value = "list")
    public String list() {
        return "statistics/goldDiamondExpend";
    }
}
