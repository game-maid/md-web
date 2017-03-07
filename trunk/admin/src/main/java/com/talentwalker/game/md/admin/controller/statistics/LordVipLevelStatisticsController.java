/**
 * @Title: LordLevelStatisticsController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月22日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.LordVipLevelStatisticsService;
import com.talentwalker.game.md.core.domain.statistics.LordLevel;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: LordLevelStatisticsController
 * @Description: 玩家等级统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月22日 下午5:42:46
 */
@Controller
@RequestMapping(value = "statistics/lord/vip/level", method = RequestMethod.GET)
public class LordVipLevelStatisticsController extends BaseController {
    @Autowired
    private LordVipLevelStatisticsService lordVipLevelStatisticsService;

    /**
     * @Description:跳转玩家等级统计页面
     * @return
     * @throws
     */
    @RequestMapping(value = "list")
    public String list() {
        return "statistics/lordVipLevel";
    }

    /**
     * @Description:分页查询玩家
     * @param userType 用户类型
     * @param startLong 
     * @param endLong 
     * @param zoneArr 要查询的区服
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "findPage", method = RequestMethod.POST)
    public Page<LordLevel> findPage(@RequestParam("userType") String userType,
            @RequestParam("startLong") Long startLong, @RequestParam("endLong") Long endLong,
            @RequestParam("zoneArr[]") String[] zoneArr) {
        return lordVipLevelStatisticsService.findPage(userType, startLong, endLong, zoneArr);
    }

    @GameResponse
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public Object export(String userType, Long startLong, Long endLong, String[] zoneArr,
            HttpServletResponse response) {
        lordVipLevelStatisticsService.export(userType, startLong, endLong, zoneArr, response);
        return null;
    }
}
