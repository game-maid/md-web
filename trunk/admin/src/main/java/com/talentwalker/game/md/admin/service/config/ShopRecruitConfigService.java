/**
 * @Title: ShopRecruitConfigService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.config;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.config.ShopRecruitConfig;
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.config.ShopRecruitConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

import net.sf.json.JSONArray;

/**
 * @ClassName: ShopRecruitService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月29日 下午12:52:00
 */
@Service
public class ShopRecruitConfigService extends BaseService {
    @Autowired
    private ShopRecruitConfigRepository shopRecruitRepository;

    /**
     * @Description:
     * @param recruit
     * @throws
     */
    public void add(ShopRecruitConfig recruit) {
        ShopRecruitConfig shopRecruit = new ShopRecruitConfig();
        if (!StringUtils.isEmpty(recruit.getId())) {
            shopRecruit = shopRecruitRepository.findOne(new ObjectId(recruit.getId()));
        }
        shopRecruit.setZoneList(recruit.getZoneList());
        shopRecruit.setName(recruit.getName());
        shopRecruit.setDescribe(recruit.getDescribe());

        if ("item".equals(recruit.getCost())) {
            shopRecruit.setCost(recruit.getItemId());
        } else {
            shopRecruit.setCost(recruit.getCost());
        }
        try {
            long startTime = DateUtils.parseDate(recruit.getStartDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            long endTime = DateUtils.parseDate(recruit.getEndDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            // 时间校验
            if (endTime <= startTime) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("config.shop.recruit.time.error"));
            }
            shopRecruit.setStartTime(startTime);
            shopRecruit.setEndTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        shopRecruit.setProType(recruit.getProType());
        if (recruit.getProType() == 1) {
            shopRecruit.setProbability(recruit.getProbability());
        } else {
            shopRecruit.setProbability(recruit.getProbability());
            shopRecruit.setProbabilityUp(recruit.getProbabilityUp());
        }
        shopRecruit.setSrc(recruit.getSrc());
        shopRecruit.setState(recruit.getState());
        shopRecruit.setStartA(recruit.getStartA());
        shopRecruit.setStartB(recruit.getStartB());
        shopRecruit.setEveryTimesA(recruit.getEveryTimesA());
        shopRecruit.setEveryTimesB(recruit.getEveryTimesB());
        shopRecruit.setAddA(recruit.getAddA());
        shopRecruit.setAddB(recruit.getAddB());
        shopRecruit.setLimitTimesType(recruit.getLimitTimesType());
        shopRecruit.setLimitTimes(recruit.getLimitTimes());
        shopRecruit.setOneRecruit(recruit.getIsOneRecruit());
        shopRecruit.setSafetyTimes(recruit.getSafetyTimes());
        shopRecruit.setContinuous(recruit.getContinuous());
        shopRecruit.setDiscount(recruit.getDiscount());
        shopRecruit.setAlonePrice1(recruit.getAlonePrice1());
        shopRecruit.setAlonePrice2(recruit.getAlonePrice2());
        shopRecruit.setAlonePrice3(recruit.getAlonePrice3());
        shopRecruit.setContinuousPrice1(recruit.getContinuousPrice1());
        shopRecruit.setContinuousPrice2(recruit.getContinuousPrice2());
        shopRecruit.setContinuousPrice3(recruit.getContinuousPrice3());
        List<Map<String, Object>> aData = JSONArray.fromObject(recruit.getData1());
        List<Map<String, Object>> bData = JSONArray.fromObject(recruit.getData2());
        shopRecruit.setaData(aData);
        shopRecruit.setbData(bData);
        shopRecruitRepository.save(shopRecruit);
    }

    /**
     * @param request 
     * @Description:
     * @return
     * @throws
     */
    public Page<ShopRecruitConfig> findAll() {
        Sort sort = new Sort(Direction.ASC, "number");
        SearchFilter searchFiter = SearchFilter.newSearchFilter(sort);
        return shopRecruitRepository.findAll(searchFiter);
    }

    /**
     * @Description:
     * @param id
     * @throws
     */
    public void delete(String id) {
        shopRecruitRepository.delete(new ObjectId(id));
    }

    /**
     * @Description:
     * @param pk
     * @param name
     * @param value
     * @throws
     */
    public void update(String pk, String name, Integer value) {
        ShopRecruitConfig shop = shopRecruitRepository.findOne(new ObjectId(pk));

        shopRecruitRepository.save(shop);
    }
}
