/**
 * @Title: GoldDiamondExpendService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月17日  张福涛
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.talentwalker.game.md.core.domain.statistics.GoldDiamondStatisticsExcel;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.ExportExcel;

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
        // 计算总数
        long total = countTotal(startDate, endDate, zoneId, itemType, userType, lordId, payType, registerCondition,
                function);
        // 查询列表
        ArrayList<Map<String, Object>> content = new ArrayList<>();
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();
        findContent(startDate, endDate, zoneId, itemType, userType, lordId, payType, registerCondition, function,
                pageable, content);
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * @Description:计算总数
     * @param startDate
     * @param endDate
     * @param zoneId
     * @param itemType
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @return
     * @throws
     */
    private long countTotal(long startDate, long endDate, String zoneId, String itemType, Integer userType,
            String lordId, Integer payType, Integer registerCondition, Integer function) {
        AggregationOutput totalOutPut = null;
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        String matchTime = "{$match:{$and:[{request_time:{$gte:" + startDate + "}},{request_time:{$lt:" + endDate
                + "}}]}}";
        String matchItemType = "{$match:{expend_items:{$in:['" + itemType + "']}}}";

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
            String lordIds = findLordIds(startDate, endDate, zoneId, payType, registerCondition);// 符合要求的玩家id
            String matchLordIds = "";
            if (payType == 2 && registerCondition == 0) {
                matchLordIds = "{$match:{player_id:{$nin:[" + lordIds + "]}}}";
            } else {
                matchLordIds = "{$match:{player_id:{$in:[" + lordIds + "]}}}";
            }
            // 分组 统计总数 count
            List<DBObject> countList = new ArrayList<>();
            String discountUri = "{$group:{_id:'$uri'}}";
            String groupNum = "{$group:{_id:'$count',count:{$sum:1}}}";
            countList.add((DBObject) JSON.parse(matchZoneId));
            countList.add((DBObject) JSON.parse(matchTime));
            countList.add((DBObject) JSON.parse(matchItemType));
            if (payType != 0 || registerCondition != 0) {
                countList.add((DBObject) JSON.parse(matchLordIds));
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
        return total;
    }

    /**
     * @Description:查询列表
     * @param startDate
     * @param endDate
     * @param zoneId
     * @param itemType
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @param pageable==null 查询全部 || pageable!=null 分页查询
     * @return
     * @throws
     */
    private void findContent(long startDate, long endDate, String zoneId, String itemType, Integer userType,
            String lordId, Integer payType, Integer registerCondition, Integer function, Pageable pageable,
            List<Map<String, Object>> content) {
        String matchZoneId = "{$match:{zone_id:'" + zoneId + "'}}";
        String matchTime = "{$match:{$and:[{request_time:{$gte:" + startDate + "}},{request_time:{$lt:" + endDate
                + "}}]}}";
        String matchItemType = "{$match:{expend_items:{$in:['" + itemType + "']}}}";
        String limitStr = "";
        String offsetStr = "";
        if (pageable != null) {
            limitStr = "{$limit:" + pageable.getPageSize() + "}";
            offsetStr = "{$skip:" + pageable.getOffset() + "}";
        }
        AggregationOutput selectOutPut = null;
        AggregationOutput payOutPut = null;
        if (userType == 0) {// 单个用户 根据用户id查询
            String matchLordId = "{$match:{player_id:'" + lordId + "'}}";
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
            if (pageable != null) {
                selectList.add((DBObject) JSON.parse(offsetStr));
                selectList.add((DBObject) JSON.parse(limitStr));
            }
            selectOutPut = mongoTemplate.getCollection("game_log").aggregate(selectList);

            // 消费人数
            List<DBObject> payList = new ArrayList<>();
            String discountLordId = "{$group:{_id:{player_id:'$player_id',uri:'$uri'}}}";
            String groupPayNum = "{$group:{_id:{uri:'$_id.uri'},count:{$sum:1}}}";
            payList.add((DBObject) JSON.parse(matchZoneId));
            payList.add((DBObject) JSON.parse(matchLordId));
            payList.add((DBObject) JSON.parse(matchTime));
            payList.add((DBObject) JSON.parse(matchItemType));
            payList.add((DBObject) JSON.parse(discountLordId));
            payList.add((DBObject) JSON.parse(groupPayNum));
            payList.add((DBObject) JSON.parse(sortByUri));
            if (pageable != null) {
                payList.add((DBObject) JSON.parse(offsetStr));
                payList.add((DBObject) JSON.parse(limitStr));
            }
            payOutPut = mongoTemplate.getCollection("game_log").aggregate(payList);

        } else {// 整体用户 根据条件查询
            String lordIds = findLordIds(startDate, endDate, zoneId, payType, registerCondition);// 符合要求的玩家id
            String matchLordIds = "";
            if (payType == 2 && registerCondition == 0) {
                matchLordIds = "{$match:{player_id:{$nin:[" + lordIds + "]}}}";
            } else {
                matchLordIds = "{$match:{player_id:{$in:[" + lordIds + "]}}}";
            }
            // 道具数量 、消费次数
            List<DBObject> selectList = new ArrayList<>();
            String groupItemNum = "{$group:{_id:{uri:'$uri'},expendTimes:{$sum:1},num:{$sum:'$result.pay.lord."
                    + itemType + "'}}}";
            String sortByUri = "{$sort:{_id.uri:-1}}";
            selectList.add((DBObject) JSON.parse(matchZoneId));
            selectList.add((DBObject) JSON.parse(matchTime));
            selectList.add((DBObject) JSON.parse(matchItemType));
            if (payType != 0 || registerCondition != 0) {
                selectList.add((DBObject) JSON.parse(matchLordIds));
            }
            selectList.add((DBObject) JSON.parse(groupItemNum));
            selectList.add((DBObject) JSON.parse(sortByUri));
            if (pageable != null) {
                selectList.add((DBObject) JSON.parse(offsetStr));
                selectList.add((DBObject) JSON.parse(limitStr));
            }
            selectOutPut = mongoTemplate.getCollection("game_log").aggregate(selectList);

            // 消费人数
            List<DBObject> payList = new ArrayList<>();
            String discountLordId = "{$group:{_id:{player_id:'$player_id',uri:'$uri'}}}";
            String groupPayNum = "{$group:{_id:{uri:'$_id.uri'},count:{$sum:1}}}";
            payList.add((DBObject) JSON.parse(matchZoneId));
            payList.add((DBObject) JSON.parse(matchTime));
            payList.add((DBObject) JSON.parse(matchItemType));
            if (payType != 0 || registerCondition != 0) {
                payList.add((DBObject) JSON.parse(matchLordIds));
            }
            payList.add((DBObject) JSON.parse(discountLordId));
            payList.add((DBObject) JSON.parse(groupPayNum));
            payList.add((DBObject) JSON.parse(sortByUri));
            if (pageable != null) {
                payList.add((DBObject) JSON.parse(offsetStr));
                payList.add((DBObject) JSON.parse(limitStr));
            }
            payOutPut = mongoTemplate.getCollection("game_log").aggregate(payList);
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
        }
    }

    /**
     * @Description:根据付费类型 和注册条件查询满足要求的玩家的id
     * @param payType
     * @param registerCondition
     * @return
     * @throws
     */
    private String findLordIds(long startDate, long endDate, String zoneId, Integer payType,
            Integer registerCondition) {
        StringBuilder lordIds = new StringBuilder();
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
            if (registerCondition != 0) {// 注册条件
                if (payType == 1) {// 付费用户
                    for (String lordId : payTypeLordList) {
                        if (!registerLordIdList.contains(lordId)) {
                            payTypeLordList.remove(lordId);
                        }
                    }
                    lordIdList = payTypeLordList;
                } else {// 未付费用户
                    for (String lordId : registerLordIdList) {
                        if (payTypeLordList.contains(lordId)) {
                            registerLordIdList.remove(lordId);
                        }
                    }
                    lordIdList = registerLordIdList;
                }
            } else {
                lordIdList = payTypeLordList;
            }
        } else if (registerCondition != 0) {// 没有付费条件 按注册条件查询
            lordIdList = registerLordIdList;
        } else {// 查询所有用户
            return null;
        }
        for (int i = 0; i < lordIdList.size(); i++) {
            lordIds.append("'").append(lordIdList.get(i)).append("'");
            if ((lordIdList.size() - 1) != i) {
                lordIds.append(",");
            }
        }
        return lordIds.toString();
    }

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
     * @Description:导出
     * @param startStr
     * @param endStr
     * @param zoneId
     * @param itemType
     * @param userType
     * @param lordId
     * @param payType
     * @param registerCondition
     * @param function
     * @throws
     */
    public void export(String startStr, String endStr, String zoneId, String itemType, Integer userType, String lordId,
            Integer payType, Integer registerCondition, Integer function, HttpServletRequest request,
            HttpServletResponse response) {
        // 查询
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long startDate = 0L;
        long endDate = 0L;
        try {
            startDate = sdf.parse(startStr).getTime();
            endDate = sdf.parse(endStr).getTime() + DateUtils.MILLIS_PER_DAY;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Map<String, Object>> content = new ArrayList<>();
        findContent(startDate, endDate, zoneId, itemType, userType, lordId, payType, registerCondition, function, null,
                content);
        System.out.println(content.size());
        // 生成excel文件
        // excel 内容
        List<GoldDiamondStatisticsExcel> excelList = new ArrayList<>();
        int index = 0;
        for (Map<String, Object> map : content) {
            GoldDiamondStatisticsExcel gdse = new GoldDiamondStatisticsExcel();
            gdse.setIndex(++index);
            gdse.setFunctionName((String) map.get("functionName"));
            gdse.setItemNum((int) map.get("itemNum"));
            gdse.setPayTimes((int) map.get("expendTimes"));
            gdse.setPayerNum((int) map.get("payerNum"));
            excelList.add(gdse);
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.gold.function.name"),
                getMessage("statistics.gold.item.num"), getMessage("statistics.gold.expend.times"),
                getMessage("statistics.gold.peyer.num") };
        ExportExcel<GoldDiamondStatisticsExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
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
