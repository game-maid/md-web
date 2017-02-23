/**
 * @Title: LordLevelStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月22日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.talentwalker.game.md.core.domain.gameworld.Lordname;
import com.talentwalker.game.md.core.domain.statistics.Login;
import com.talentwalker.game.md.core.domain.statistics.LordLevel;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.repository.statistics.LoginRepository;
import com.talentwalker.game.md.core.repository.statistics.RegisterRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: LordLevelStatisticsService
 * @Description: 玩家等级统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月22日 下午6:13:22
 */
@Service
public class LordLevelStatisticsService {

    /**
     * 全部用户
     */
    private static final String ALL_USER = "allUser";
    /**
     * 活跃用户
     */
    private static final String ACTIVE_USER = "activeUser";
    /**
     * 新用户
     */
    private static final String NEW_USER = "newUser";

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private RegisterRepository registerRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @Description:分页查询玩家等级
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @return
     * @throws
     */
    public Page<LordLevel> findPage(String userType, Long startLong, Long endLong, String[] zoneArr) {
        List<LordLevel> content = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if ((zoneArr.length - 1) != i) {
                sb.append(",");
            }
        }
        sb.append("]");
        String lordIds = queryLordIds(userType, startLong, endLong, zoneArr);
        // 计算总数
        long total = findTotal(userType, lordIds);
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();
        // 分页查询
        List<DBObject> pageList = new ArrayList<>();
        if (!ALL_USER.equals(userType)) {
            pageList.add((DBObject) JSON.parse("{$match:{_id:{$in:" + lordIds + "}}}"));
        }
        pageList.add((DBObject) JSON.parse("{$group:{_id:{level:'$level'},num:{$sum:1}}}"));
        pageList.add((DBObject) JSON.parse("{$sort:{_id.level:1}}"));
        pageList.add((DBObject) JSON.parse("{$skip:" + pageable.getOffset() + "}"));
        pageList.add((DBObject) JSON.parse("{$limit:" + pageable.getPageSize() + "}"));
        AggregationOutput out = mongoTemplate.getCollection("game_lord").aggregate(pageList);
        Iterator<DBObject> it = out.results().iterator();
        int totalNum = 0;
        while (it.hasNext()) {
            BasicDBObject result = (BasicDBObject) it.next();
            int level = ((BasicDBObject) result.get("_id")).getInt("level");// 等级
            int num = result.getInt("num");// 人数
            LordLevel lordLevel = new LordLevel();
            lordLevel.setLevel(level);
            lordLevel.setNum(num);
            totalNum += num;
            content.add(lordLevel);
        }
        List<String> list = (List<String>) JSON.parse(lordIds);
        System.out.println("玩家id:" + list.size() + "分页总数：" + totalNum);
        for (int i = 0; i < content.size(); i++) {
            LordLevel lordLevel = content.get(i);
            lordLevel.setProportion((lordLevel.getNum() * 100) / totalNum / 100.0D);
            int level = lordLevel.getLevel();
            Query query = new Query();
            for (String lordId : list) {

            }
            query.addCriteria(Criteria.where("player_id").orOperator());
            query.addCriteria(Criteria.where("level").is(level));
            List<Lordname> lordName = mongoTemplate.find(query, Lordname.class);
            lordLevel.setLordList(lordName);
        }

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * @Description:计算总数
     * @return
     * @throws
     */
    private long findTotal(String userType, String lordIds) {
        long total = 0L;
        List<DBObject> totalNumList = new ArrayList<>();
        if (!ALL_USER.equals(userType)) {
            totalNumList.add((DBObject) JSON.parse("{$match:{_id:{$in:" + lordIds + "}}}"));
        }
        totalNumList.add((DBObject) JSON.parse("{$group:{_id:{lordId:'$id',level:'$level'}}}"));
        totalNumList.add((DBObject) JSON.parse("{$group:{_id:{lordId:'$_id.lordId'},total:{$sum:1}}}"));
        AggregationOutput outTotal = mongoTemplate.getCollection("game_lord").aggregate(totalNumList);
        Iterator<DBObject> it = outTotal.results().iterator();
        while (it.hasNext()) {
            BasicDBObject result = (BasicDBObject) it.next();
            total = result.getInt("total");
        }
        return total;
    }

    /**
     * @Description:根据查询条件，查询符合条件的玩家id
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @return
     * @throws
     */
    private String queryLordIds(String userType, Long startLong, Long endLong, String[] zoneArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (ACTIVE_USER.equals(userType)) {// 活跃用户
            List<Login> loginList = loginRepository.findByDistinctLoginTimeAndZoneId(startLong, endLong, zoneArr);
            Iterator<Login> it = loginList.iterator();
            while (it.hasNext()) {
                sb.append("'").append(it.next().getLordId()).append("'");
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
        } else if (NEW_USER.equals(userType)) {// 新注册用户
            List<Register> registerList = registerRepository.findByRegisterTimeAndZoneArr(startLong, endLong, zoneArr);
            Iterator<Register> it = registerList.iterator();
            while (it.hasNext()) {
                sb.append("'").append(it.next().getId()).append("'");
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
