/**
 * @Title: ShopService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月2日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.config;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.config.ShopConfig;
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.config.ShopConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

import net.sf.json.JSONArray;

/**
 * @ClassName: ShopService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月2日 下午1:17:24
 */
@Service
public class ShopConfigService extends BaseService {
    @Autowired
    private ShopConfigRepository shopRepository;

    /**
     * @Description:
     * @param shop
     * @throws
     */
    public void add(ShopConfig data) {
        ShopConfig shop = new ShopConfig();
        if (data.getId() != null) {
            shop = shopRepository.findOne(new ObjectId(data.getId()));
        }
        shop.setZoneList(data.getZoneList());
        shop.setName(data.getName());
        shop.setExplain(data.getExplain());
        if ("item".equals(data.getMoney())) {
            shop.setMoney(data.getItemId());
        } else {
            shop.setMoney(data.getMoney());
        }
        shop.setPrice(data.getPrice());
        shop.setLimitAmount(data.getLimitAmount());
        shop.setLimitVip(data.getLimitVip());
        try {
            long startTime = DateUtils.parseDate(data.getStartDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            long endTime = DateUtils.parseDate(data.getEndDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            if (endTime <= startTime) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("config.shop.recruit.time.error"));
            }
            // 时间校验
            shop.setStartTime(startTime);
            shop.setEndTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        shop.setPutrush(data.getPutrush());
        shop.setAcquireType(data.getAcquireType());
        List<Map<String, Object>> itemData = JSONArray.fromObject(data.getData());
        shop.setItemData(itemData);
        shopRepository.save(shop);
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    public Page<ShopConfig> findAll() {
        Sort sort = new Sort(Direction.ASC, "putrush");
        SearchFilter searchFiter = SearchFilter.newSearchFilter(sort);
        return shopRepository.findAll(searchFiter);
    }

    /**
     * @Description:
     * @param id
     * @throws
     */
    public void delete(String id) {
        shopRepository.delete(new ObjectId(id));
    }

    /**
     * @Description:
     * @param pk
     * @param name
     * @param value
     * @throws
     */
    public void update(String pk, String name, int value) {
        ShopConfig shop = shopRepository.findOne(new ObjectId(pk));
        if ("putrush".equals(name)) {
            shop.setPutrush(value);
        }
        shopRepository.save(shop);
    }

}
