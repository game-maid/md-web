/**
 * @Title: OrderService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月5日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.gameworld.Order;
import com.talentwalker.game.md.core.repository.gameworld.OrderRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: OrderService
 * @Description: 充值订单
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月5日 下午6:40:23
 */
@Service
public class OrderService extends BaseService {
    @Resource
    private OrderRepository orderRepository;

    /**
     * @Description: 分页查询
     * @param packageId
     * @param orderState
     * @param itemType
     * @param zoneArr
     * @return
     * @throws
     */
    public Page<Order> findList(String packageId, String orderState, String itemType, String[] zoneArr) {
        SearchFilter searchFilter = SearchFilter.newSearchFilter();
        searchFilter.addCriteria(Criteria.where("zone_id").in(zoneArr));
        if (!StringUtils.isEmpty(packageId)) {// 按包查询
            searchFilter.addCriteria(Criteria.where("package_id").is(packageId));
        }
        if ("0".equals(orderState)) {// 已支付
            searchFilter.addCriteria(Criteria.where("state").is(0));
        } else if ("1".equals(orderState)) {// 未支付
            searchFilter.addCriteria(Criteria.where("state").is(1));
        }
        if ("1".equals(itemType)) {// 钻石
            searchFilter.addCriteria(Criteria.where("product_type").is(1));
        } else if ("2".equals(itemType)) {// 月卡
            searchFilter.addCriteria(Criteria.where("product_type").ne(1));
        }

        Page<Order> findAll = orderRepository.findAll(searchFilter);
        return findAll;
    }

}
