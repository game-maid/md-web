/**
 * @Title: TopUpStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月5日  张福涛
 */

package com.talentwalker.game.md.core.service.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        // 充值总额
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        List<DBObject> pipeline = new ArrayList<>();
        String matchCreateTimeStr = "{$match:{$and:[{create_time:{$gt:" + startDate.getTime() + "}},{create_time:{$lt:"
                + endDate.getTime() + "}}]}}";
        pipeline.add((DBObject) JSON.parse(matchCreateTimeStr));
        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        pipeline.add((DBObject) JSON.parse(matchZoneStr));
        if (!StringUtils.isEmpty(packageId)) {
            String matchPackageStr = "{$match:{package_id:'" + packageId + "'}}";
            DBObject matchPackageObj = (DBObject) JSON.parse(matchPackageStr);
            pipeline.add(matchPackageObj);
        }
        if (!StringUtils.isEmpty(orderState)) {
            String matchOrderState = "{$match:{state:" + orderState + "}}";
            DBObject matchOrderObj = (DBObject) JSON.parse(matchOrderState);
            pipeline.add(matchOrderObj);
        }

        if ("1".equals(itemType)) {// 钻石
            String matchItemType = "{$match:{product_type:" + itemType + "}}";
            DBObject matchItemTypeObj = (DBObject) JSON.parse(matchItemType);
            pipeline.add(matchItemTypeObj);
        } else if ("2".equals(itemType)) {// 月卡
            String matchItemType = "{$match:{product_type:{$ne:" + 1 + "}}}";
            DBObject matchItemTypeObj = (DBObject) JSON.parse(matchItemType);
            pipeline.add(matchItemTypeObj);
        }
        String groupStr = "{$group:{_id:{package_id:'$package_id'},total:{$sum:'$price'}}}";
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
            Map<String, Object> map = new HashMap<>();
            map.put("sumPrice", total);
            resultMap.put(packagId, map);
        }
        // 充值人数
        List<DBObject> payNumPipeline = new ArrayList<>();
        payNumPipeline.add((DBObject) JSON.parse(matchCreateTimeStr));
        payNumPipeline.add((DBObject) JSON.parse(matchZoneStr));
        if (!StringUtils.isEmpty(packageId)) {
            payNumPipeline.add((DBObject) JSON.parse("{$match:{package_id:'" + packageId + "'}}"));
        }
        if (!StringUtils.isEmpty(orderState)) {
            payNumPipeline.add((DBObject) JSON.parse("{$match:{state:" + orderState + "}}"));
        }
        if ("1".equals(itemType)) {// 钻石
            payNumPipeline.add((DBObject) JSON.parse("{$match:{product_type:" + itemType + "}}"));
        } else if ("2".equals(itemType)) {// 月卡
            payNumPipeline.add((DBObject) JSON.parse("{$match:{product_type:{$ne:" + 1 + "}}}"));
        }
        payNumPipeline.add((DBObject) JSON.parse("{$group:{_id:{package_id:'$package_id',lord_id:'$lordId'}}}"));
        payNumPipeline.add((DBObject) JSON.parse("{$group:{_id:{package_id:'$_id.package_id'},payNum:{$sum:1}}}"));
        AggregationOutput payNumOutput = mongoTemplate.getCollection("game_order").aggregate(payNumPipeline);
        Iterator<DBObject> payNumIt = payNumOutput.results().iterator();
        while (payNumIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) payNumIt.next();
            String packageId2 = ((BasicDBObject) next.get("_id")).getString("package_id");
            int payNum = next.getInt("payNum");
            Map<String, Object> map = resultMap.get(packageId2);
            map.put("num", payNum);
        }
        List<Object> list = new ArrayList<>();
        list.add(resultMap);
        return list;
    }

    /**
     * @Description:计算每日充值金额
     * @param packageId
     * @param orderState
     * @param itemType
     * @param zoneArr
     * @param startStr
     * @param endStr
     * @return
     * @throws
     */
    public List<Map<String, Object>> dailyIncome(String packageId, String orderState, String itemType, String[] zoneArr,
            String startStr, String endStr) {
        List<Map<String, Object>> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        long dayStartLong = 0L;
        try {
            startDate = DateUtils.parseDate(startStr, "yyyy-MM-dd HH:mm:ss");
            endDate = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm:ss");
            dayStartLong = sdf.parse(sdf.format(startDate)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        long endLong = endDate.getTime();
        while (dayStartLong < endLong) {

            List<DBObject> pipeline = new ArrayList<>();
            String matchStr = "{$match:{$and:[{create_time:{$gt:" + dayStartLong + "}},{create_time:{$lt:"
                    + (dayStartLong + DateUtils.MILLIS_PER_DAY) + "}}]}}";
            DBObject matchObj = (DBObject) JSON.parse(matchStr);
            pipeline.add(matchObj);
            String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
            DBObject matchZoneObj = (DBObject) JSON.parse(matchZoneStr);
            pipeline.add(matchZoneObj);
            if (!StringUtils.isEmpty(packageId)) {
                String matchPackageStr = "{$match:{package_id:'" + packageId + "'}}";
                DBObject matchPackageObj = (DBObject) JSON.parse(matchPackageStr);
                pipeline.add(matchPackageObj);
            }
            if (!StringUtils.isEmpty(orderState)) {
                String matchOrderState = "{$match:{state:" + orderState + "}}";
                DBObject matchOrderObj = (DBObject) JSON.parse(matchOrderState);
                pipeline.add(matchOrderObj);
            }
            if ("1".equals(itemType)) {
                String matchItemType = "{$match:{product_type:" + itemType + "}}";
                DBObject matchItemTypeObj = (DBObject) JSON.parse(matchItemType);
                pipeline.add(matchItemTypeObj);
            } else if ("2".equals(itemType)) {
                String matchItemType = "{$match:{product_type:{$ne:" + 1 + "}}}";
                DBObject matchItemTypeObj = (DBObject) JSON.parse(matchItemType);
                pipeline.add(matchItemTypeObj);
            }
            String group = "{$group:{_id:'$package_id',total:{$sum:'$price'}}}";
            DBObject groupObj = (DBObject) JSON.parse(group);
            pipeline.add(groupObj);
            AggregationOutput out = mongoTemplate.getCollection("game_order").aggregate(pipeline);
            Iterator<DBObject> it = out.results().iterator();
            double total = 0D;
            while (it.hasNext()) {
                BasicDBObject dbo = (BasicDBObject) it.next();
                double price = dbo.getDouble("total");
                total += price;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("date", dayStartLong);
            map.put("price", total);
            list.add(map);

            dayStartLong += DateUtils.MILLIS_PER_DAY;
        }
        return list;
    }

    /**
     * @Description:区服充值统计（按区服分组统计）
     * @param packageId
     * @param orderState
     * @param itemType
     * @param zoneArr
     * @param startStr
     * @param endStr
     * @return
     * @throws
     */
    public List<Map<String, Map<String, Object>>> zoneTopUp(String packageId, String orderState, String itemType,
            String[] zoneArr, String startStr, String endStr) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        List<Map<String, Map<String, Object>>> list = new ArrayList<>();
        list.add(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = DateUtils.parseDate(startStr, "yyyy-MM-dd HH:mm:ss");
            endDate = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        List<DBObject> pipeline = new ArrayList<>();// 分组查询充值次数和付费总计
        String matchCreateTimeStr = "{$match:{$and:[{create_time:{$gt:" + startDate.getTime() + "}},{create_time:{$lt:"
                + endDate.getTime() + "}}]}}";
        pipeline.add((DBObject) JSON.parse(matchCreateTimeStr));
        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        pipeline.add((DBObject) JSON.parse(matchZoneStr));
        if (!StringUtils.isEmpty(packageId)) {
            String matchPackageStr = "{$match:{package_id:'" + packageId + "'}}";
            DBObject matchPackageObj = (DBObject) JSON.parse(matchPackageStr);
            pipeline.add(matchPackageObj);
        }
        if (!StringUtils.isEmpty(packageId)) {
            pipeline.add((DBObject) JSON.parse("{$match:{package_id:'" + packageId + "'}}"));
        }
        if (!StringUtils.isEmpty(orderState)) {
            pipeline.add((DBObject) JSON.parse("{$match:{state:" + orderState + "}}"));
        }
        if ("1".equals(itemType)) {
            String matchItemType = "{$match:{product_type:" + itemType + "}}";
            DBObject matchItemTypeObj = (DBObject) JSON.parse(matchItemType);
            pipeline.add(matchItemTypeObj);
        } else if ("2".equals(itemType)) {
            String matchItemType = "{$match:{product_type:{$ne:" + 1 + "}}}";
            DBObject matchItemTypeObj = (DBObject) JSON.parse(matchItemType);
            pipeline.add(matchItemTypeObj);
        }
        String groupZoneStr = "{$group:{_id:'$zone_id',price:{$sum:'$price'},num:{$sum:1}}}";
        DBObject groupZoneDbo = (DBObject) JSON.parse(groupZoneStr);
        pipeline.add(groupZoneDbo);
        // 充值次数// 付费总计
        AggregationOutput out = mongoTemplate.getCollection("game_order").aggregate(pipeline);
        Iterator<DBObject> it = out.results().iterator();
        while (it.hasNext()) {
            Map<String, Object> tempMap = new HashMap<>();
            BasicDBObject dbo = (BasicDBObject) it.next();
            String zoneId = dbo.getString("_id");
            double price = dbo.getDouble("price");
            int num = dbo.getInt("num");
            map.put(zoneId, tempMap);
            tempMap.put("price", price);
            tempMap.put("num", num);
        }
        // 付费人数
        List<DBObject> payNumPipeline = new ArrayList<>();
        payNumPipeline.add((DBObject) JSON.parse(matchCreateTimeStr));
        payNumPipeline.add((DBObject) JSON.parse(matchZoneStr));
        if (!StringUtils.isEmpty(packageId)) {
            payNumPipeline.add((DBObject) JSON.parse("{$match:{package_id:'" + packageId + "'}}"));
        }
        if (!StringUtils.isEmpty(orderState)) {
            payNumPipeline.add((DBObject) JSON.parse("{$match:{state:" + orderState + "}}"));
        }
        if ("1".equals(itemType)) {// 钻石
            payNumPipeline.add((DBObject) JSON.parse("{$match:{product_type:" + itemType + "}}"));
        } else if ("2".equals(itemType)) {// 月卡
            payNumPipeline.add((DBObject) JSON.parse("{$match:{product_type:{$ne:" + 1 + "}}}"));
        }
        payNumPipeline.add((DBObject) JSON.parse("{$group:{_id:{zone_id:'$zone_id',lord_id:'$lordId'}}}"));
        payNumPipeline.add((DBObject) JSON.parse("{$group:{_id:{zone_id:'$_id.zone_id'},payNum:{$sum:1}}}"));
        AggregationOutput payNumOutput = mongoTemplate.getCollection("game_order").aggregate(payNumPipeline);
        Iterator<DBObject> payNumIt = payNumOutput.results().iterator();
        while (payNumIt.hasNext()) {
            BasicDBObject next = (BasicDBObject) payNumIt.next();
            String zoneId = ((BasicDBObject) next.get("_id")).getString("zone_id");
            int payNum = next.getInt("payNum");
            Map<String, Object> tempMap = map.get(zoneId);
            tempMap.put("payNum", payNum);
        }
        return list;
    }

}
