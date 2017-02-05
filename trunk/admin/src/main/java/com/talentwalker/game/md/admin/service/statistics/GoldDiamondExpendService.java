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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<Map<String, Object>> findList(String startStr, String endStr, String zoneId, String itemType,
            Integer userType, String lordId, Integer payType, Integer registerCondition, Integer function) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long startDate = 0L;
        long endDate = 0L;
        try {
            startDate = sdf.parse(startStr).getTime();
            endDate = sdf.parse(endStr).getTime() + DateUtils.MILLIS_PER_DAY;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long total = 0L;
        ArrayList<Map<String, Object>> content = new ArrayList<>();
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();

        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        String matchTime = "{$match:{$and:[{request_time:{$gte:" + startDate + "}},{request_time:{$lt:" + endDate
                + "}}]}}";
        String matchItemType = "{$match:{expend_items:{$in:['" + itemType + "']}}}";
        AggregationOutput totalOutPut = null;
        AggregationOutput selectOutPut = null;
        AggregationOutput payOutPut = null;
        if (userType == 0) {// 单个用户 根据用户id查询
            // 分组 统计总数 count
            List<DBObject> countList = new ArrayList<>();
            String matchLordId = "{$match:{player_id:'" + lordId + "'}}";
            String discountUri = "{$group:{_id:'$uri'}}";
            String groupNum = "{$group:{_id:'$count',count:{$sum:1}}}";
            countList.add((DBObject) JSON.parse(matchZoneId));
            countList.add((DBObject) JSON.parse(matchLordId));
            countList.add((DBObject) JSON.parse(matchTime));
            countList.add((DBObject) JSON.parse(matchItemType));
            countList.add((DBObject) JSON.parse(discountUri));
            countList.add((DBObject) JSON.parse(groupNum));
            totalOutPut = mongoTemplate.getCollection("game_log").aggregate(countList);

            // 分页 分组查询 list
            int limit = pageable.getPageSize();
            int offset = pageable.getOffset();
            String limitStr = "{$limit:" + limit + "}";
            String offsetStr = "{$skip:" + offset + "}";
            // 道具数量 、消费次数
            List<DBObject> selectList = new ArrayList<>();
            String groupItemNum = "{$group:{_id:{uri:'$uri'},expendTimes:{$sum:1},num:{$sum:'$result.pay.lord."
                    + itemType + "'}}}";
            String sortByUri = "{$sort:{_id.uri:-1}}";
            selectList.add((DBObject) JSON.parse(matchZoneId));
            selectList.add((DBObject) JSON.parse(matchLordId));
            selectList.add((DBObject) JSON.parse(matchTime));
            selectList.add((DBObject) JSON.parse(matchItemType));
            selectList.add((DBObject) JSON.parse(groupItemNum));
            selectList.add((DBObject) JSON.parse(sortByUri));
            selectList.add((DBObject) JSON.parse(offsetStr));
            selectList.add((DBObject) JSON.parse(limitStr));
            selectOutPut = mongoTemplate.getCollection("game_log").aggregate(selectList);

            // 消费人数
            List<DBObject> payList = new ArrayList<>();
            String discountLordId = "{$group:{_id:{player_id:'$player_id',uri:'$uri'}}}";
            String groupPayNum = "{$group:{_id:{uri:'$_id.uri'},count:{$sum:1}}}";
            String sort = "{$sort:{'_id.uri':-1}}";
            payList.add((DBObject) JSON.parse(matchZoneId));
            payList.add((DBObject) JSON.parse(matchLordId));
            payList.add((DBObject) JSON.parse(matchTime));
            payList.add((DBObject) JSON.parse(matchItemType));
            payList.add((DBObject) JSON.parse(discountLordId));
            payList.add((DBObject) JSON.parse(groupPayNum));
            payList.add((DBObject) JSON.parse(sort));
            payList.add((DBObject) JSON.parse(offsetStr));
            payList.add((DBObject) JSON.parse(limitStr));
            payOutPut = mongoTemplate.getCollection("game_log").aggregate(payList);

        } else {// 整体用户 根据条件查询
            DBObject dbo = null;

            // 分组 统计总数 count
            List<DBObject> countList = new ArrayList<>();
            String discountUri = "{$group:{_id:'$uri'}}";
            String groupNum = "{$group:{_id:'$count',count:{$sum:1}}}";
            countList.add((DBObject) JSON.parse(matchZoneId));
            countList.add((DBObject) JSON.parse(matchTime));
            countList.add((DBObject) JSON.parse(matchItemType));
            countList.add((DBObject) JSON.parse(discountUri));
            countList.add((DBObject) JSON.parse(groupNum));
            totalOutPut = mongoTemplate.getCollection("game_log").aggregate(countList);

            // 分页 分组查询 list
            int limit = pageable.getPageSize();
            int offset = pageable.getOffset();
            String limitStr = "{$limit:" + limit + "}";
            String offsetStr = "{$skip:" + offset + "}";
            // 道具数量 、消费次数
            List<DBObject> selectList = new ArrayList<>();
            String groupItemNum = "{$group:{_id:{uri:'$uri'},expendTimes:{$sum:1},num:{$sum:'$result.pay.lord."
                    + itemType + "'}}}";
            String sortByUri = "{$sort:{_id.uri:-1}}";
            selectList.add((DBObject) JSON.parse(matchZoneId));
            selectList.add((DBObject) JSON.parse(matchTime));
            selectList.add((DBObject) JSON.parse(matchItemType));
            selectList.add((DBObject) JSON.parse(groupItemNum));
            selectList.add((DBObject) JSON.parse(sortByUri));
            selectList.add((DBObject) JSON.parse(offsetStr));
            selectList.add((DBObject) JSON.parse(limitStr));
            selectOutPut = mongoTemplate.getCollection("game_log").aggregate(selectList);

            // 消费人数
            List<DBObject> payList = new ArrayList<>();
            String discountLordId = "{$group:{_id:{player_id:'$player_id',uri:'$uri'}}}";
            String groupPayNum = "{$group:{_id:{uri:'$_id.uri'},count:{$sum:1}}}";
            String sort = "{$sort:{'_id.uri':-1}}";
            payList.add((DBObject) JSON.parse(matchZoneId));
            payList.add((DBObject) JSON.parse(matchTime));
            payList.add((DBObject) JSON.parse(matchItemType));
            payList.add((DBObject) JSON.parse(discountLordId));
            payList.add((DBObject) JSON.parse(groupPayNum));
            payList.add((DBObject) JSON.parse(sort));
            payList.add((DBObject) JSON.parse(offsetStr));
            payList.add((DBObject) JSON.parse(limitStr));
            payOutPut = mongoTemplate.getCollection("game_log").aggregate(payList);
        }
        // 分组 统计总数 count 处理查询结果
        Iterator<DBObject> totalIt = totalOutPut.results().iterator();
        while (totalIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) totalIt.next();
            total = next.getLong("count");
        }
        // 道具数量 、消费次数 处理查询结果
        Iterator<DBObject> selectIt = selectOutPut.results().iterator();
        while (selectIt.hasNext()) {
            HashMap<String, Object> map = new HashMap<>();
            content.add(map);
            BasicDBObject next = (BasicDBObject) selectIt.next();
            String uri = ((BasicDBObject) next.get("_id")).getString("uri");
            int num = next.getInt("num");
            int expendTimes = next.getInt("expendTimes");
            map.put("functionName", uri);
            map.put("itemNum", num);
            map.put("expendTimes", expendTimes);
            System.out.println(uri + "-----" + num + "---------" + expendTimes);
        }
        // 消费人数 处理查询结果
        Iterator<DBObject> payIt = payOutPut.results().iterator();
        while (payIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) payIt.next();
            String uri = ((BasicDBObject) next.get("_id")).getString("uri");
            int payerNum = next.getInt("count");
            for (Map<String, Object> map : content) {
                if (map.get("functionName").equals(uri)) {
                    map.put("payerNum", payerNum);
                    break;
                }
            }
            System.out.println(uri + "----------" + payerNum);
        }
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * @Description:根据付费类型 和注册条件查询玩家的ids
     * @param payType
     * @param registerCondition
     * @return
     * @throws
     */
    private String findLordIds(long startDate, long endDate, String zoneId, String itemType, Integer userType,
            Integer payType, Integer registerCondition) {
        StringBuilder lordIds = new StringBuilder();
        String matchPayTime = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + endDate + "}}]}}";
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        String matchRegisterTime = "{$match:{$and:[{register_time:{$gt:" + startDate + "}},{register_time:{$lt:"
                + endDate + "}}]}}";
        if (payType != 0) {
            StringBuilder payLordIds = new StringBuilder();
            List<DBObject> payTypeList = new ArrayList<>();
            payTypeList.add((DBObject) JSON.parse(matchPayTime));
            payTypeList.add((DBObject) JSON.parse(matchZoneId));
            payTypeList.add((DBObject) JSON.parse("{$group:{_id:{lord_id:'$lord_id'}}}"));
            AggregationOutput payTypeOutput = mongoTemplate.getCollection("game_order").aggregate(payTypeList);
            Iterator<DBObject> payTypeIt = payTypeOutput.results().iterator();
            while (payTypeIt.hasNext()) {
                BasicDBObject next = (BasicDBObject) payTypeIt.next();
                String lordId = ((BasicDBObject) next.get("_id")).getString("lord_id");
                payLordIds.append("'").append(lordId).append("'");
                if (payTypeIt.hasNext()) {
                    payLordIds.append(",");
                }
            }
            if (registerCondition != 0) {// 注册条件
                List<DBObject> registerList = new ArrayList<>();
                registerList.add((DBObject) JSON.parse(matchRegisterTime));
                registerList.add((DBObject) JSON.parse("{$match:{_id:{$in:[" + payLordIds.toString() + "]}}}"));
                registerList.add((DBObject) JSON.parse("{$group:{_id:{lord_id:'$_id'}}}"));
                AggregationOutput registerOutput = mongoTemplate.getCollection("game_register").aggregate(registerList);
                Iterator<DBObject> registerIt = registerOutput.results().iterator();
                while (registerIt.hasNext()) {
                    BasicDBObject next = (BasicDBObject) registerIt.next();
                    String lordId = ((BasicDBObject) next.get("_id")).getString("lord_id");
                    lordIds.append("'").append(lordId).append("'");
                    if (registerIt.hasNext()) {
                        lordIds.append(",");
                    }
                }
            } else {
                lordIds = payLordIds;
            }
        } else {
            if (registerCondition != 0) {// 注册条件
                /**
                 * 
                 * 
                 * 
                 */
            }
        }

        return lordIds.toString();
    }
}
