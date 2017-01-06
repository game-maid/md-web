/**
 * @Title: CashShopController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月4日  张福涛
 */

package com.talentwalker.game.md.application.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.CashShopService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: CashShopController
 * @Description: 充值
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月4日 下午3:52:33
 */
@Controller
@RequestMapping(value = "game/cashShop", method = RequestMethod.POST)
public class CashShopController extends GameSupport {
    @Resource
    private CashShopService cashShopService;

    /**
     * @Description:记录订单生成，并返给订单号
     * @param productId
     * @param quantity
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getOrderId/{productId}/{quantity}")
    public Object getOrderId(@PathVariable String productId, @PathVariable Integer quantity) {
        cashShopService.getOrderId(productId, quantity);
        return this.gameModel;
    }
}
