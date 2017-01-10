/**
 * @Title: TopUpStatisticsController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月5日  张福涛
 */

package com.talentwalker.game.md.admin.controller.statistics;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.OrderService;
import com.talentwalker.game.md.core.domain.gameworld.Order;
import com.talentwalker.game.md.core.service.gameworld.CashShopService;
import com.talentwalker.game.md.core.service.statistics.TopUpStatisticsService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: TopUpStatisticsController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月5日 上午10:47:13
 */
@Controller
@RequestMapping(value = "statistics/topUp")
public class TopUpStatisticsController extends BaseController {

    @Resource
    private OrderService orderService;

    @Resource
    private CashShopService cashShopService;

    @Resource
    private TopUpStatisticsService topUpStatisticsService;

    /**
     * @Description:跳转到平台充值总览页面
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "statistics/topUp";
    }

    /**
     * @Description:分页查询订单详情
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "order/list", method = RequestMethod.POST)
    public Page<Order> findOrder(@RequestParam("packageId") String packageId,
            @RequestParam("orderState") String orderState, @RequestParam("itemType") String itemType,
            @RequestParam("zoneList[]") String[] zoneArr) {
        return orderService.findList(packageId, orderState, itemType, zoneArr);
    }

    /**
     * @Description:获取cashShop_config配置信息
     * @return
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "config", method = RequestMethod.POST)
    public Object cashShopConfig() {
        return cashShopService.cashShopConfig();
    }

    /**
     * @Description:计算arpu占比
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "arpu", method = RequestMethod.POST)
    public Object arpu(@RequestParam("packageId") String packageId, @RequestParam("orderState") String orderState,
            @RequestParam("itemType") String itemType, @RequestParam("zoneList[]") String[] zoneArr,
            @RequestParam("startStr") String startStr, @RequestParam("endStr") String endStr) {
        return topUpStatisticsService.arpu(packageId, orderState, itemType, zoneArr, startStr, endStr);
    }

}
