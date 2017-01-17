/**
 * @Title: GoldDiamondExpendService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月17日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: GoldDiamondExpendService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月17日 下午5:51:20
 */
@Service
public class GoldDiamondExpendService extends BaseService {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * @Description:
     * @param zoneId
     * @param itemType
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @throws
     */
    public void findList(String zoneId, Integer itemType, Integer userType, String lordId, Integer payType,
            Integer registerCondition, Integer function) {
        SearchFilter filter = SearchFilter.newSearchFilter();
        Pageable pageable = filter.getPageable();

        // 分组 统计总数 count
        List<DBObject> countList = new ArrayList<>();
        String matchLordId = "{$match:{player_id:" + lordId + "}}";
        String matchItemType = "{$match:{}}";
        mongoTemplate.getCollection("game_log").aggregate(countList);
        // 分页 分组查询 list

    }

}
