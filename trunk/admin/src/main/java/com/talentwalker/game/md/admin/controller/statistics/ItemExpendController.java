/**
 * @Title: ItemExpendController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月13日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.ItemExpendService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ItemExpendController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月13日 下午5:41:42
 */
@Controller
@RequestMapping(value = "statistics/itemExpend", method = RequestMethod.GET)
public class ItemExpendController extends BaseController {

    @Autowired
    private ItemExpendService itemExpendService;

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

    @GameResponse
    @RequestMapping(value = "findList", method = RequestMethod.POST)
    public Object list(String startStr, String endStr, String zoneId, String itemId, Integer userType, String lordId,
            Integer payType, Integer registerCondition, String function) {
        return itemExpendService.findList(startStr, endStr, zoneId, itemId, userType, lordId, payType,
                registerCondition, function);
    }

    /**
     * @Description:文件导出
     * @param startStr
     * @param endStr
     * @param zoneId
     * @param diamondType
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public Object export(String startStr, String endStr, String zoneId, String itemId, Integer userType, String lordId,
            Integer payType, Integer registerCondition, String function, HttpServletRequest request,
            HttpServletResponse response) {
        itemExpendService.export(startStr, endStr, zoneId, itemId, userType, lordId, payType, registerCondition,
                function, request, response);
        return null;
    }
}
