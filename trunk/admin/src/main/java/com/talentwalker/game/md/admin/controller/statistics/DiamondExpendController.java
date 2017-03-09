/**
 * @Title: DiamondExpendController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月8日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.DiamondExpendService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: DiamondExpendController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月8日 下午2:13:06
 */
@Controller
@RequestMapping(value = "statistics/diamondExpend", method = RequestMethod.GET)
public class DiamondExpendController extends BaseController {
    @Autowired
    private DiamondExpendService diamondExpendService;

    @RequestMapping(value = "main")
    public String main() {
        return "statistics/diamondExpend";
    }

    /**
     * @Description:查询
     * @param startStr
     * @param endStr
     * @param zoneId
     * @param itemType
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "findList", method = RequestMethod.POST)
    public Page<Map<String, Object>> list(String startStr, String endStr, String zoneId, String diamondType,
            Integer userType, String lordId, Integer payType, Integer registerCondition, String function) {
        Page<Map<String, Object>> findList = diamondExpendService.findList(startStr, endStr, zoneId, diamondType,
                userType, lordId, payType, registerCondition, function);
        diamondExpendService.distribution(startStr, endStr, zoneId, diamondType, userType, lordId, payType,
                registerCondition, function);
        return findList;
    }

    @GameResponse
    @RequestMapping(value = "distribution", method = RequestMethod.POST)
    public Object distribution(String startStr, String endStr, String zoneId, String diamondType, Integer userType,
            String lordId, Integer payType, Integer registerCondition, String function) {
        return diamondExpendService.distribution(startStr, endStr, zoneId, diamondType, userType, lordId, payType,
                registerCondition, function);
    }
}
