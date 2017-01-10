/**
 * @Title: TopUpStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月5日  张福涛
 */

package com.talentwalker.game.md.core.service.statistics;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @ClassName: TopUpStatisticsService
 * @Description: 充值总览
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月5日 下午6:35:20
 */
@Service
public class TopUpStatisticsService {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * @Description: arpu占比
     * @param packageId
     * @param orderState
     * @param itemType
     * @param zoneArr
     * @return
     * @throws
     */
    public Object arpu(String packageId, String orderState, String itemType, String[] zoneArr, String startStr,
            String endStr) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = DateUtils.parseDate(startStr, "yyyy-MM-dd HH:mm:ss");
            endDate = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 充值总额// 充值人数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        List<DBObject> pipeline = new ArrayList<>();
        String matchStr = "{$match:{$and:[{create_time:{$gt:" + startDate.getTime() + "}},{create_time:{$lt:"
                + endDate.getTime() + "}}]}}";
        DBObject matchObj = (DBObject) JSON.parse(matchStr);
        pipeline.add(matchObj);
        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        DBObject matchZoneObj = (DBObject) JSON.parse(matchZoneStr);
        pipeline.add(matchZoneObj);
        if (!StringUtils.isEmpty(packageId)) {
            String matchPackageStr = "{$match:{package_id:" + packageId + "}}";
            DBObject matchPackageObj = (DBObject) JSON.parse(matchPackageStr);
            pipeline.add(matchPackageObj);
        }
        if ("1".equals(orderState)) {
            String matchOrderState = "{$match:{state:" + orderState + "}}";
            DBObject matchOrderObj = (DBObject) JSON.parse(matchOrderState);
            pipeline.add(matchOrderObj);
        } else if ("2".equals(orderState)) {
            String matchOrderState = "{$match:{state:{$ne:" + orderState + "}}}";
            DBObject matchOrderStateObj = (DBObject) JSON.parse(matchOrderState);
            pipeline.add(matchOrderStateObj);
        }
        String groupStr = "{$group:{_id:{package_id:'package_id'},totalNum:{$sum:1},total:{$sum:'$price'}}}";
        DBObject groupObj = (DBObject) JSON.parse(groupStr);
        pipeline.add(groupObj);
        AggregationOutput output = mongoTemplate.getCollection("game_order").aggregate(pipeline);
        Iterator<DBObject> it = output.results().iterator();
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        while (it.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) it.next();
            BasicDBObject keyValues = (BasicDBObject) dbo.get("_id");
            String packagId = keyValues.getString("package_id");
            double total = dbo.getDouble("total");
            int num = dbo.getInt("totalNum");
            Map<String, Object> map = new HashMap<>();
            map.put("sumPrice", total);
            map.put("num", num);
            resultMap.put(packagId, map);
        }
        List<Object> list = new ArrayList<>();
        list.add(resultMap);
        return list;
    }
}
