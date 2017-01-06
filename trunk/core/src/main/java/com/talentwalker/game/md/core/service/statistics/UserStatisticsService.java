/**
 * @Title: UserStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月3日  张福涛
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

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.repository.statistics.RegisterRepository;

/**
 * @ClassName: UserStatisticsService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月3日 上午11:53:53
 */
@Service
public class UserStatisticsService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private RegisterRepository registerRepository;

    /**
     * @Description:查询总用户数
     * @param zoneArr
     * @return
     * @throws
     */
    public List<Map<String, Object>> totalSelect(String[] zoneArr) {
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        String matchZone = "{$match:{gamezone_id:{$in:[" + zoneStr + "]}}}";
        String group = "{$group:{_id:{zone_id:'$gamezone_id',package_id:'$package_id'},total:{$sum:1}}}";
        String sort = "{$sort:{_id.zone_id:-1}}";
        List<DBObject> pipeline = new ArrayList<>();
        DBObject groupDBO = (DBObject) JSON.parse(group);
        DBObject matchZoneDBO = (DBObject) JSON.parse(matchZone);
        DBObject sortDBO = (DBObject) JSON.parse(sort);
        pipeline.add(matchZoneDBO);
        pipeline.add(groupDBO);
        pipeline.add(sortDBO);
        AggregationOutput output = mongoTemplate.getCollection("portal_gameuser").aggregate(pipeline);
        Iterator<DBObject> iterator = output.results().iterator();

        int flag = -1;
        String zoneFlag = "";
        int zoneTotal = 0;
        while (iterator.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) iterator.next();
            BasicDBObject keyValues = (BasicDBObject) dbo.get("_id");
            String zoneId = keyValues.getString("zone_id");
            String packageId = keyValues.getString("package_id");
            int total = dbo.getInt("total");
            if (flag == -1) {
                flag++;
                Map<String, Object> map = new HashMap<>();
                map.put("zoneId", zoneId);
                zoneFlag = zoneId;
                list.add(flag, map);
            } else if (!zoneFlag.equals(zoneId)) {
                Map<String, Object> preMap = list.get(flag);
                preMap.put("zoneTotal", zoneTotal);
                list.set(flag, preMap);
                flag++;
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("zoneId", zoneId);
                zoneFlag = zoneId;
                list.add(flag, newMap);
                zoneTotal = 0;
            } else if (!iterator.hasNext()) {
                Map<String, Object> preMap = list.get(flag);
                preMap.put("zoneTotal", zoneTotal);
                list.set(flag, preMap);
            }
            Map<String, Object> map = list.get(flag);
            Map<String, Integer> packageMap = (Map<String, Integer>) map.get("package");
            if (packageMap == null) {
                packageMap = new HashMap<>();
            }
            packageMap.put(packageId, total);
            map.put("package", packageMap);
            zoneTotal += total;
        }
        return list;
    }

    /**
     * @Description:查询该天注册人的信息
     * @param date 日期
     * @param zoneId 区id
     * @return
     * @throws
     */
    public List<Register> newSelect(String dateStr, String zoneId) {
        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr, "yyyy/MM/dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long start = date.getTime();
        long end = start + DateUtils.MILLIS_PER_DAY;
        return registerRepository.findByRegisterTimeAndZoneId(start, end, zoneId);
    }

}
