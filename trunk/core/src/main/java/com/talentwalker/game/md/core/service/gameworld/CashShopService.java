/**
 * @Title: CashShopService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月4日  张福涛
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.config.ConfigKey;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Order;
import com.talentwalker.game.md.core.domain.gameworld.PushOrderTgame;
import com.talentwalker.game.md.core.repository.gameworld.OrderRepository;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: CashShopService
 * @Description: 充值
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月4日 下午4:01:46
 */
@Service
public class CashShopService extends GameSupport {
    @Resource
    private OrderRepository orderRepository;

    /**
     * @Description:记录订单，生成订单
     * @param productId
     * @param quantity
     * @throws
     */
    public void getOrderId(String productId, Integer quantity) {
        Lord lord = getLord();
        GameUser gameUser = getGameUser();
        // 订单号 lordId+时间戳
        String orderId = lord.getId() + new Date().getTime();
        // 保存订单，状态未支付
        Order order = new Order();
        DataConfig dataConfig = getDataConfig().get(ConfigKey.CASH_SHOP_CONFIG).get(productId);
        order.setId(orderId);
        order.setLord(lord);
        order.setGameUser(gameUser);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setState(Order.STATE_NO);
        order.setZoneId(gameUser.getGameZoneId());
        order.setPackageId(gameUser.getPackageId());
        order.setLordId(lord.getId());
        order.setLordVipLevel(lord.getVipLevel());
        order.setLordVipScore(lord.getVipScore());
        order.setPrice(dataConfig.getDouble(ConfigKey.CASH_SHOP_CONFIG_PRICE));
        order.setProductType(dataConfig.getInteger(ConfigKey.CASH_SHOP_CONFIG_TYPE));
        order.setCreateTime(System.currentTimeMillis());
        order.setPlatformId(PushOrderTgame.PLATFORM_ID);
        orderRepository.save(order);
        this.gameModel.addObject("orderId", orderId);
    }

}
