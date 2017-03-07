/**
 * @Title: LordVipLevelStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月6日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

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
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.TopUpFirstRecord;
import com.talentwalker.game.md.core.domain.statistics.Login;
import com.talentwalker.game.md.core.domain.statistics.LordLevel;
import com.talentwalker.game.md.core.domain.statistics.LordLevelExcel;
import com.talentwalker.game.md.core.repository.gameworld.TopUpFirstRecordRepository;
import com.talentwalker.game.md.core.repository.statistics.LoginRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.ExportExcel;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: LordVipLevelStatisticsService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月6日 下午7:40:31
 */
@Service
public class LordVipLevelStatisticsService extends BaseService {
    /**
     * 付费用户
     */
    public static final String PAYER = "payer";
    /**
     * 付费全部用户（在查询截止时间前有过付费的玩家）
     */
    public static final String BEFORE_PAYER = "beforePayer";
    /**
     * 付费活跃用户
     */
    public static final String ACTIVE_PAYER = "activePayer";
    @Autowired
    private TopUpFirstRecordRepository firstRecordRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private LoginRepository loginRepository;

    /**
     * @Description:
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @return
     * @throws
     */
    public Page<LordLevel> findPage(String userType, Long startLong, Long endLong, String[] zoneArr) {
        // 查找符合条件的玩家id
        List<String> lordIdList = findLordIds(userType, zoneArr, endLong, startLong);
        String lordIds = JSON.serialize(lordIdList);

        List<LordLevel> content = new ArrayList<>();
        // 计算总数
        long total = findTotal(lordIds);
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();
        // 分页查询
        findList(content, lordIds, pageable, true);

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * @Description:查找详细信息
     * @param content
     * @param lordIds
     * @param pageable
     * @param b
     * @throws
     */
    private void findList(List<LordLevel> content, String lordIds, Pageable pageable, boolean isPage) {
        List<DBObject> pageList = new ArrayList<>();
        pageList.add((DBObject) JSON.parse("{$match:{_id:{$in:" + lordIds + "}}}"));
        pageList.add((DBObject) JSON.parse("{$group:{_id:{level:'$vip_level'},num:{$sum:1}}}"));
        pageList.add((DBObject) JSON.parse("{$sort:{_id.level:1}}"));
        if (isPage) {
            pageList.add((DBObject) JSON.parse("{$skip:" + pageable.getOffset() + "}"));
            pageList.add((DBObject) JSON.parse("{$limit:" + pageable.getPageSize() + "}"));
        }
        AggregationOutput out = mongoTemplate.getCollection("game_lord").aggregate(pageList);
        Iterator<DBObject> it = out.results().iterator();
        int totalNum = 0;
        while (it.hasNext()) {
            BasicDBObject result = (BasicDBObject) it.next();
            int level = ((BasicDBObject) result.get("_id")).getInt("level");// vip等级
            int num = result.getInt("num");// 人数
            LordLevel lordLevel = new LordLevel();
            lordLevel.setLevel(level);
            lordLevel.setNum(num);
            totalNum += num;
            content.add(lordLevel);
        }
        List<String> lordIdList = (List<String>) JSON.parse(lordIds);
        for (int i = 0; i < content.size(); i++) {
            LordLevel lordLevel = content.get(i);
            lordLevel.setProportion((lordLevel.getNum() * 100) / totalNum / 100.0D);
            int level = lordLevel.getLevel();
            Query query = new Query();
            query.addCriteria(Criteria.where("id").in(lordIdList));
            query.addCriteria(Criteria.where("vipLevel").is(level));
            List<Lord> lordList = mongoTemplate.find(query, Lord.class);
            lordLevel.setLordList(lordList);
        }
    }

    /** 
     * @Description:统计总条数
     * @param lordIds
     * @return
     * @throws 
     */
    private long findTotal(String lordIds) {
        List<DBObject> pipeline = new ArrayList<>();
        pipeline.add((DBObject) JSON.parse("{$match:{_id:{$in:" + lordIds + "}}}"));
        pipeline.add((DBObject) JSON.parse("{$group:{_id:{lordId:'$id',level:'$vip_level'}}}"));
        pipeline.add((DBObject) JSON.parse("{$group:{_id:{lordId:'$_id.lordId'},total:{$sum:1}}}"));
        AggregationOutput outTotal = mongoTemplate.getCollection("game_lord").aggregate(pipeline);
        Iterator<DBObject> it = outTotal.results().iterator();
        long total = 0L;
        while (it.hasNext()) {
            BasicDBObject result = (BasicDBObject) it.next();
            total = result.getInt("total");
        }
        return total;
    }

    /**
     * @Description:查找符合添加的玩家id
     * @param userType
     * @throws
     */
    private List<String> findLordIds(String userType, String[] zoneArr, long endLong, long startLong) {
        List<String> zoneList = Arrays.asList(zoneArr);
        List<String> lordIdList = new ArrayList<>();
        List<TopUpFirstRecord> topUpFirstRecordList = new ArrayList<>();
        if (PAYER.equals(userType)) {
            topUpFirstRecordList = firstRecordRepository.findList(zoneList);
        } else if (BEFORE_PAYER.equals(userType)) {
            topUpFirstRecordList = firstRecordRepository.findList(zoneList, endLong);
        } else if (ACTIVE_PAYER.equals(userType)) {
            Set<String> tempSet = new HashSet<>();
            List<Login> LoginList = loginRepository.findByDistinctLoginTimeAndZoneId(startLong, endLong, zoneArr);
            for (Login login : LoginList) {
                tempSet.add(login.getLordId());
            }
            topUpFirstRecordList = firstRecordRepository.findList(endLong, tempSet);
        }
        for (TopUpFirstRecord topUpFirstRecord : topUpFirstRecordList) {
            lordIdList.add(topUpFirstRecord.getId());
        }
        return lordIdList;
    }

    /**
     * @Description:
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @param response
     * @throws
     */
    public void export(String userType, Long startLong, Long endLong, String[] zoneArr, HttpServletResponse response) {
        List<LordLevel> content = new ArrayList<>();
        List<String> lordIdList = findLordIds(userType, zoneArr, endLong, startLong);
        String lordIds = JSON.serialize(lordIdList);
        findList(content, lordIds, null, false);
        // 生成excel文件
        // excel 内容
        List<LordLevelExcel> excelList = new ArrayList<>();
        int index = 0;
        for (LordLevel lordLevel : content) {
            for (Lord lord : lordLevel.getLordList()) {
                LordLevelExcel lordLevelExcel = new LordLevelExcel();
                lordLevelExcel.setIndex(++index);
                lordLevelExcel.setLevel(lordLevel.getLevel());
                lordLevelExcel.setLordId(lord.getId());
                lordLevelExcel.setLordName(lord.getName());
                lordLevelExcel.setNum(lordLevel.getNum());
                lordLevelExcel.setProportion(lordLevel.getProportion());
                excelList.add(lordLevelExcel);
            }
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.vip.level"),
                getMessage("statistics.lord.level.num"), getMessage("statistics.lord.level.user.level.proportion"),
                getMessage("gamelog.player"), getMessage("gmtool.lord.name") };
        ExportExcel<LordLevelExcel> excel = new ExportExcel<>();
        String path = ServletUtils.getRequest().getServletContext().getRealPath("/");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "lordVipLevel" + sdf1.format(System.currentTimeMillis()) + ".xls";
        // 在服务器生成excel文件
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path + System.getProperty("file.separator") + fileName);
            excel.exportExcel(header, excelList, out, "yyy-MM-dd HH:mm:ss");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        download(path + fileName, response);
    }

    /**
     * @Description:下载文件
     * @param path 要下载的文件路径
     * @param response
     * @throws
     */
    private void download(String path, HttpServletResponse response) {
        File file = new File(path);
        String fileName = file.getName();
        InputStream is = null;
        OutputStream os = null;
        try {
            // 读文件
            is = new BufferedInputStream(new FileInputStream(path));
            byte[] bufferByte = new byte[is.available()];
            is.read(bufferByte);
            // 清空response
            response.reset();
            // 设置响应头
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", file.length() + "");
            // 写文件
            os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            os.write(bufferByte);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭资源
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
