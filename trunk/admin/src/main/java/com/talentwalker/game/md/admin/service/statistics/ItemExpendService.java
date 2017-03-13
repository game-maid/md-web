/**
 * @Title: ItemExpendService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月13日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.talentwalker.game.md.admin.service.BaseService;

/**
 * @ClassName: ItemExpendService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月13日 下午8:31:55
 */
@Service
public class ItemExpendService extends BaseService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @Description:查询
     * @param startStr
     * @param endStr
     * @param zoneId
     * @param itemId
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @return
     * @throws
     */
    public Object findList(String startStr, String endStr, String zoneId, String itemId, Integer userType,
            String lordId, Integer payType, Integer registerCondition, String function) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long startDate = 0L;
        long endDate = 0L;
        try {
            startDate = sdf.parse(startStr).getTime();
            endDate = sdf.parse(endStr).getTime() + DateUtils.MILLIS_PER_DAY;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String lordIds = null;
        if (userType != 0) {
            lordIds = findLordIds(startDate, endDate, zoneId, payType, registerCondition, itemId);// 符合要求的玩家id
        }
        System.out.println(lordIds);
        // 查询总条数
        long total = countTotal(startDate, endDate, zoneId, itemId, userType, lordId, payType, registerCondition,
                function, lordIds);
        // 查询列表

        return null;
    }

    /**
     * @Description:查询总条数
     * @param startDate
     * @param endDate
     * @param zoneId
     * @param itemId
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @param lordIds
     * @return
     * @throws
     */
    private long countTotal(long startDate, long endDate, String zoneId, String itemId, Integer userType, String lordId,
            Integer payType, Integer registerCondition, String function, String lordIds) {
        AggregationOutput totalOutPut = null;
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        String matchTime = "{$match:{$and:[{request_time:{$gte:" + startDate + "}},{request_time:{$lt:" + endDate
                + "}}]}}";
        String matchItemType = "{$match:{expend_items:{$in:['" + itemId + "']}}}]}}";
        String matchUri = "{$match:{uri:{$regex:'/" + function + "/'}}}";
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
        } else {// 整体用户 根据条件查询
            String matchLordIds = "";
            matchLordIds = "{$match:{player_id:{$in:[" + lordIds + "]}}}";
            // 分组 统计总数 count
            List<DBObject> countList = new ArrayList<>();
            String discountUri = "{$group:{_id:'$uri'}}";
            String groupNum = "{$group:{_id:'$count',count:{$sum:1}}}";
            countList.add((DBObject) JSON.parse(matchZoneId));
            countList.add((DBObject) JSON.parse(matchTime));
            countList.add((DBObject) JSON.parse(matchItemType));
            countList.add((DBObject) JSON.parse(matchLordIds));
            if (!StringUtils.isEmpty(function)) {
                countList.add((DBObject) JSON.parse(matchUri));
            }
            countList.add((DBObject) JSON.parse(discountUri));
            countList.add((DBObject) JSON.parse(groupNum));
            totalOutPut = mongoTemplate.getCollection("game_log").aggregate(countList);
        }
        long total = 0L;
        // 分组 统计总数 count 处理查询结果
        Iterator<DBObject> totalIt = totalOutPut.results().iterator();
        while (totalIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) totalIt.next();
            total = next.getLong("count");
        }
        System.out.println("total:" + total);
        return total;
    }

    /**
     * @Description:查询符合条件的玩家id
     * @param startDate
     * @param endDate
     * @param zoneId
     * @param payType
     * @param registerCondition
     * @param itemId
     * @return
     * @throws
     */
    private String findLordIds(long startDate, long endDate, String zoneId, Integer payType, Integer registerCondition,
            String itemId) {
        List<String> lordIdList = new ArrayList<>();
        String matchPayTime = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + endDate + "}}]}}";
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        // 根据注册条件查询
        List<String> registerLordIdList = new ArrayList<>();
        findLordByRegister(registerCondition, zoneId, startDate, endDate, registerLordIdList);

        if (payType != 0) {
            List<String> payTypeLordList = new ArrayList<>();
            List<DBObject> payTypeList = new ArrayList<>();
            payTypeList.add((DBObject) JSON.parse(matchPayTime));
            payTypeList.add((DBObject) JSON.parse(matchZoneId));
            payTypeList.add((DBObject) JSON.parse("{$group:{_id:{lord_id:'$lordId'}}}"));
            AggregationOutput payTypeOutput = mongoTemplate.getCollection("game_order").aggregate(payTypeList);
            Iterator<DBObject> payTypeIt = payTypeOutput.results().iterator();
            while (payTypeIt.hasNext()) {
                BasicDBObject next = (BasicDBObject) payTypeIt.next();
                String lordId = ((BasicDBObject) next.get("_id")).getString("lord_id");
                payTypeLordList.add(lordId);
            }
            if (payType == 1) {// 付费用户
                Iterator<String> it = payTypeLordList.iterator();
                while (it.hasNext()) {
                    String lordId = it.next();
                    if (!registerLordIdList.contains(lordId)) {
                        it.remove();
                    }
                }

                lordIdList = payTypeLordList;
            } else {// 未付费用户
                Iterator<String> it = registerLordIdList.iterator();
                while (it.hasNext()) {
                    String lordId = it.next();
                    if (payTypeLordList.contains(lordId)) {
                        it.remove();
                    }
                }
                lordIdList = registerLordIdList;
            }
        } else {// 没有付费条件 按注册条件查询
            lordIdList = registerLordIdList;
        }
        String lordIds = listToString(lordIdList);
        return lordIds;
    }

    /**
     * @Description:
     * @param registerCondition
     * @param zoneId
     * @param startDate
     * @param endDate
     * @param registerLordIdList
     * @throws
     */
    private void findLordByRegister(Integer registerCondition, String zoneId, long startDate, long endDate,
            List<String> lordIdList) {
        String matchRegisterTime = "{$match:{$and:[{register_time:{$gt:" + startDate + "}},{register_time:{$lt:"
                + endDate + "}}]}}";
        String matchLoginTime = "{$match:{$and:[{login_time:{$gt:" + startDate + "}},{login_time:{$lt:" + endDate
                + "}}]}}";
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        if (registerCondition == 1) {// 活跃用户
            List<DBObject> registerList = new ArrayList<>();
            registerList.add((DBObject) JSON.parse(matchLoginTime));
            registerList.add((DBObject) JSON.parse(matchZoneId));
            registerList.add((DBObject) JSON.parse("{$group:{_id:{lord_id:'$lord_id'}}}"));
            AggregationOutput registerOutput = mongoTemplate.getCollection("game_login").aggregate(registerList);
            Iterator<DBObject> registerIt = registerOutput.results().iterator();
            while (registerIt.hasNext()) {
                BasicDBObject next = (BasicDBObject) registerIt.next();
                String lordId = ((BasicDBObject) next.get("_id")).getString("lord_id");
                lordIdList.add(lordId);
            }
        } else if (registerCondition == 2) {// 新用户
            List<DBObject> registerList = new ArrayList<>();
            registerList.add((DBObject) JSON.parse(matchRegisterTime));
            registerList.add((DBObject) JSON.parse(matchZoneId));
            registerList.add((DBObject) JSON.parse("{$group:{_id:{lord_id:'$_id'}}}"));
            AggregationOutput registerOutput = mongoTemplate.getCollection("game_register").aggregate(registerList);
            Iterator<DBObject> registerIt = registerOutput.results().iterator();
            while (registerIt.hasNext()) {
                BasicDBObject next = (BasicDBObject) registerIt.next();
                String lordId = ((BasicDBObject) next.get("_id")).getString("lord_id");
                lordIdList.add(lordId);
            }
        } else if (registerCondition == 3) {// 老用户
            List<DBObject> registerList = new ArrayList<>();
            registerList.add((DBObject) JSON.parse("{$match:{$or:[{register_time:{$gte:" + endDate
                    + "}},{register_time:{$lte:" + startDate + "}}]}}"));
            registerList.add((DBObject) JSON.parse(matchZoneId));
            registerList.add((DBObject) JSON.parse("{$group:{_id:{lord_id:'$_id'}}}"));
            AggregationOutput registerOutput = mongoTemplate.getCollection("game_register").aggregate(registerList);
            Iterator<DBObject> registerIt = registerOutput.results().iterator();
            while (registerIt.hasNext()) {
                BasicDBObject next = (BasicDBObject) registerIt.next();
                String lordId = ((BasicDBObject) next.get("_id")).getString("lord_id");
                lordIdList.add(lordId);
            }
        }
    }

    /**
     * @Description:将集合转成用于查询的字符串
     * @param list
     * @return
     * @throws
     */
    private String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append("'").append(list.get(i)).append("'");
            if ((list.size() - 1) != i) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
