/**
 * @Title: LordLevelStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月22日  张福涛
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
import java.util.Iterator;
import java.util.List;

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
import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.statistics.Login;
import com.talentwalker.game.md.core.domain.statistics.LordLevel;
import com.talentwalker.game.md.core.domain.statistics.LordLevelExcel;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.repository.statistics.LoginRepository;
import com.talentwalker.game.md.core.repository.statistics.RegisterRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.ExportExcel;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: LordLevelStatisticsService
 * @Description: 玩家等级统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月22日 下午6:13:22
 */
@Service
public class LordLevelStatisticsService extends BaseController {

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
    @Autowired
    private LordRepository lordRepository;

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
        String lordIds = queryLordIds(userType, startLong, endLong, zoneArr);
        // 计算总数
        long total = findTotal(userType, lordIds);
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();
        // 分页查询
        findList(content, lordIds, pageable, userType, true);

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * @Description:查询详细数据
     * @param content
     * @param lordIds
     * @param pageable
     * @param userType
     * @param isPage 是否分页
     * @throws
     */
    private void findList(List<LordLevel> content, String lordIds, Pageable pageable, String userType, Boolean isPage) {
        List<DBObject> pageList = new ArrayList<>();
        if (!ALL_USER.equals(userType)) {
            pageList.add((DBObject) JSON.parse("{$match:{_id:{$in:" + lordIds + "}}}"));
        }
        pageList.add((DBObject) JSON.parse("{$group:{_id:{level:'$level'},num:{$sum:1}}}"));
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
            int level = ((BasicDBObject) result.get("_id")).getInt("level");// 等级
            int num = result.getInt("num");// 人数
            LordLevel lordLevel = new LordLevel();
            lordLevel.setLevel(level);
            lordLevel.setNum(num);
            totalNum += num;
            content.add(lordLevel);
        }
        List<String> list = (List<String>) JSON.parse(lordIds);
        for (int i = 0; i < content.size(); i++) {
            LordLevel lordLevel = content.get(i);
            lordLevel.setProportion((lordLevel.getNum() * 100) / totalNum / 100.0D);
            int level = lordLevel.getLevel();
            Query query = new Query();
            query.addCriteria(Criteria.where("id").in(list));
            query.addCriteria(Criteria.where("level").is(level));
            List<Lord> lordList = mongoTemplate.find(query, Lord.class);
            lordLevel.setLordList(lordList);
        }
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

    /**
     * @Description: 导出excel文件
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @throws
     */
    public void export(String userType, Long startLong, Long endLong, String[] zoneArr, HttpServletResponse response) {
        List<LordLevel> content = new ArrayList<>();
        String lordIds = queryLordIds(userType, startLong, endLong, zoneArr);
        findList(content, lordIds, null, userType, false);
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
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.lord.level.user.level"),
                getMessage("statistics.lord.level.num"), getMessage("statistics.lord.level.user.level.proportion"),
                getMessage("gamelog.player"), getMessage("gmtool.lord.name") };
        ExportExcel<LordLevelExcel> excel = new ExportExcel<>();
        String path = ServletUtils.getRequest().getServletContext().getRealPath("/");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "GoldDiamondExpendStatistics" + sdf1.format(System.currentTimeMillis()) + ".xls";
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
