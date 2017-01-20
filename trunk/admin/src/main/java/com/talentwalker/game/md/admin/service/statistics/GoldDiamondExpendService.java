/**
 * @Title: GoldDiamondExpendService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月17日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(GoldDiamondExpendService.class);

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
    public void findList(String startStr, String endStr, String zoneId, String itemType, Integer userType,
            String lordId, Integer payType, Integer registerCondition, Integer function) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long startDate = 0L;
        long endDate = 0L;
        try {
            startDate = sdf.parse(startStr).getTime();
            endDate = sdf.parse(endStr).getTime() + DateUtils.MILLIS_PER_DAY;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 分组 统计总数 count
        int count = 0;
        List<DBObject> countList = new ArrayList<>();
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        String matchLordId = "{$match:{player_id:'" + lordId + "'}}";
        String matchTime = "{$match:{$and:[{request_time:{$gte:" + startDate + "}},{request_time:{$lt:" + endDate
                + "}}]}}";
        String matchItemType = "{$match:{expend_items:{$in:['" + itemType + "']}}}";
        String discountUri = "{$group:{_id:'$uri'}}";
        String groupNum = "{$group:{_id:'$count',count:{$sum:1}}}";
        countList.add((DBObject) JSON.parse(matchZoneId));
        countList.add((DBObject) JSON.parse(matchLordId));
        countList.add((DBObject) JSON.parse(matchTime));
        countList.add((DBObject) JSON.parse(matchItemType));
        countList.add((DBObject) JSON.parse(discountUri));
        countList.add((DBObject) JSON.parse(groupNum));
        AggregationOutput totalOutPut = mongoTemplate.getCollection("game_log").aggregate(countList);
        Iterator<DBObject> totalIt = totalOutPut.results().iterator();
        while (totalIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) totalIt.next();
            count = next.getInt("count");
            LOGGER.info("总条数：" + count);
        }
        // 分页 分组查询 list
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();
        int limit = pageable.getPageSize();
        int offset = pageable.getOffset();
        String limitStr = "{$limit:" + 3 + "}";
        String offsetStr = "{$skip:" + 0 + "}";
        // 道具数量 、消费次数
        List<DBObject> selectList = new ArrayList<>();
        String groupItemNum = "{$group:{_id:'$uri',expendTimes:{$sum:1},num:{$sum:'$result.pay.lord." + itemType
                + "'}}}";
        String sortByUri = "{$sort:{_id.uri:-1}}";
        selectList.add((DBObject) JSON.parse(matchZoneId));
        selectList.add((DBObject) JSON.parse(matchLordId));
        selectList.add((DBObject) JSON.parse(matchTime));
        selectList.add((DBObject) JSON.parse(matchItemType));
        selectList.add((DBObject) JSON.parse(groupItemNum));
        selectList.add((DBObject) JSON.parse(sortByUri));
        // selectList.add((DBObject) JSON.parse(offsetStr));
        // selectList.add((DBObject) JSON.parse(limitStr));
        AggregationOutput selectOutPut = mongoTemplate.getCollection("game_log").aggregate(selectList);
        Iterator<DBObject> selectIt = selectOutPut.results().iterator();
        while (selectIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) selectIt.next();
            String uri = next.getString("_id");
            int num = next.getInt("num");
            int expendTimes = next.getInt("expendTimes");
            System.out.println(uri + "-----" + num + "---------" + expendTimes);
        }
        // 消费人数
        List<DBObject> payList = new ArrayList<>();
        String discountLordId = "{$group:{_id:{player_id:'$player_id',uri:'$uri'}}}";
        String groupPayNum = "{$group:{_id:'$_id.uri',count:{$sum:1}}}";
        String sort = "{$sort:{'_id._id.uri':-1}}";
        payList.add((DBObject) JSON.parse(matchZoneId));
        payList.add((DBObject) JSON.parse(matchLordId));
        payList.add((DBObject) JSON.parse(matchTime));
        payList.add((DBObject) JSON.parse(matchItemType));
        payList.add((DBObject) JSON.parse(discountLordId));
        payList.add((DBObject) JSON.parse(groupPayNum));
        payList.add((DBObject) JSON.parse(sort));
        // payList.add((DBObject) JSON.parse(offsetStr));
        // payList.add((DBObject) JSON.parse(limitStr));
        AggregationOutput payOutPut = mongoTemplate.getCollection("game_log").aggregate(payList);
        Iterator<DBObject> payIt = payOutPut.results().iterator();
        System.out.println("----");
        while (payIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) payIt.next();
            String uri = next.getString("_id");
            int payTimes = next.getInt("count");
            System.out.println(uri + "----------" + payTimes);
        }
    }

}
