/**
 * @Title: UserController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月3日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.StatisticsUserExcelService;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.service.statistics.UserStatisticsService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: UserController
 * @Description: 统计，用户相关
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月3日 上午10:40:19
 */
@Controller
@RequestMapping("statistics/user")
public class UserStatisticsController extends BaseController {
    @Resource
    private UserStatisticsService userStatisticsService;
    @Resource
    private StatisticsUserExcelService statisticsUserExcelService;

    @GameResponse
    @RequestMapping(value = "total", method = RequestMethod.GET)
    public String userTotal() {
        return "statistics/userTotal";
    }

    @GameResponse
    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String userNew() {
        return "statistics/newUser";
    }

    @GameResponse
    @RequestMapping(value = "total/select", method = RequestMethod.POST)
    public List<Map<String, Object>> totalSelect(String[] zoneArr) {
        return userStatisticsService.totalSelect(zoneArr);
    }

    @GameResponse
    @RequestMapping(value = "total/export", method = RequestMethod.GET)
    public Object totalExport(String[] zoneArr, HttpServletRequest req, HttpServletResponse res) {
        statisticsUserExcelService.totalExport(zoneArr, req, res);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "new/select", method = RequestMethod.POST)
    public List<Register> newSelect(String date, String zoneId) {
        return userStatisticsService.newSelect(date, zoneId);
    }

    @GameResponse
    @RequestMapping(value = "new/export", method = RequestMethod.GET)
    public Object newExport(String date, String zoneId, HttpServletRequest req, HttpServletResponse res) {
        statisticsUserExcelService.newExport(date, zoneId, req, res);
        return null;
    }
}
