/**
 * @Title: StatisticsController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月14日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.StatisitcsService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: StatisticsController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月14日 下午4:24:50
 */
@Controller
@RequestMapping(value = "statistics", method = RequestMethod.GET)
public class StatisticsController extends BaseController {
    @Resource
    private StatisitcsService statisticsService;

    /**
     * @Description:根据语言类型获取（uri）功能名称配置
     * @param language
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "functionName")
    public List<Map<String, Object>> queryFunctionName() {
        Locale locale = LocaleContextHolder.getLocale();
        return statisticsService.queryFunctionName(locale.getLanguage());
    }

    /**
     * @Description:从道具表中查询道具
     * @return
     * @throws
     */
    public List<Map<String, Object>> queryItems() {
        return statisticsService.queryItems();
    }

}
