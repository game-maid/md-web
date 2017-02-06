/**
 * @Title: GoldDiamondExpendController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月13日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.GoldDiamondExpendService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: GoldDiamondExpendController
 * @Description: 金币钻石消耗统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月13日 下午2:01:40
 */
@Controller
@RequestMapping(value = "statistics/goldDiamondExpend", method = RequestMethod.GET)
public class GoldDiamondExpendController extends BaseController {
    @Resource
    private GoldDiamondExpendService goldDiamondExpendService;

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "statistics/goldDiamondExpend";
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
    public Page<Map<String, Object>> list(String startStr, String endStr, String zoneId, String itemType,
            Integer userType, String lordId, Integer payType, Integer registerCondition, Integer function) {
        Page<Map<String, Object>> findList = goldDiamondExpendService.findList(startStr, endStr, zoneId, itemType,
                userType, lordId, payType, registerCondition, function);

        List<Map<String, Object>> content = findList.getContent();
        for (Map<String, Object> map : content) {
            Set<String> keySet = map.keySet();
            for (String string : keySet) {
                // System.out.println(string + "---" + map.get(string));
            }
        }
        return findList;
    }
}
