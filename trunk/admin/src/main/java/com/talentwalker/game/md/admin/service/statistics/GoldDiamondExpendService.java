/**
 * @Title: GoldDiamondExpendService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月17日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
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
    public void findList(String zoneId, String itemType, Integer userType, String lordId, Integer payType,
            Integer registerCondition, Integer function) {
        SearchFilter filter = SearchFilter.newSearchFilter();
        Pageable pageable = filter.getPageable();

        // 分组 统计总数 count
        int count = 0;
        List<DBObject> countList = new ArrayList<>();
        String matchLordId = "{$match:{player_id:'" + lordId + "'}}";
        String matchItemType = "{$match:{expend_items:{$in:['" + itemType + "']}}}";
        String discountLordId = "{$group:{_id:'$uri'}}";
        String groupNum = "{$group:{_id:'$count',count:{$sum:1}}}";
        countList.add((DBObject) JSON.parse(matchLordId));
        countList.add((DBObject) JSON.parse(matchItemType));
        countList.add((DBObject) JSON.parse(discountLordId));
        countList.add((DBObject) JSON.parse(groupNum));
        AggregationOutput totalOutPut = mongoTemplate.getCollection("game_log").aggregate(countList);
        Iterator<DBObject> totalIt = totalOutPut.results().iterator();
        while (totalIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) totalIt.next();
            count = next.getInt("count");
        }
        // 分页 分组查询 list

        List<DBObject> selectList = new ArrayList<>();
        String groupItemNum = "{$group:{_id:'$uri',num:{$sum:}}}";
        selectList.add((DBObject) JSON.parse(matchLordId));
        selectList.add((DBObject) JSON.parse(matchItemType));
        selectList.add((DBObject) JSON.parse(groupItemNum));
        AggregationOutput selectOutPut = mongoTemplate.getCollection("game_log").aggregate(selectList);
        Iterator<DBObject> selectIt = selectOutPut.results().iterator();
        while (selectIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) selectIt.next();
            String uri = next.getString("_id");
            int num = next.getInt("num");
            System.out.println(uri + "-----" + num);
        }
    }

}
