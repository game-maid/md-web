
package com.talentwalker.game.md.core.service.statistics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.talentwalker.game.md.core.domain.statistics.ActiveBaseData;
import com.talentwalker.game.md.core.domain.statistics.ActiveBasePackage;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.repository.statistics.ActiveBaseDataReposistory;

/**
 * @ClassName: ActiveAndPersistenceService
 * @Description: 活跃和存留
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月21日 下午2:40:10
 */
@Service
public class ActiveAndPersistenceService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private ActiveBaseDataReposistory activeBaseDataReposistory;

    /**
     * @Description:基础数据
     * @param dateType
     * @param dateStr
     * @param zoneArr
     * @return
     * @throws
     */
    public List<ActiveBaseData> baseSelect(String dateType, String dateStr, String[] zoneArr) {
        List<ActiveBaseData> tableData = new ArrayList<>();
        List<String> zoneList = new ArrayList<>();
        zoneList.addAll(Arrays.asList(zoneArr));// 要查询的区
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cur = Calendar.getInstance();
        String format = sdf.format(new Date());
        try {
            cur.setTime(sdf.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 日
        if (dateType.equals("day")) {
            if (DateUtils.isSameDay(date, cur.getTime())) {// 查询当天数据
                baseData(cur, zoneStr, zoneList, tableData);
            } else {
                tableData.add(activeBaseDataReposistory.findByDate(dateStr));
                checkZone(tableData, zoneList);
            }
            persistenceData(cal, zoneStr, zoneList, tableData, false);
            cal.setTime(date);
            ltvData(cal, zoneStr, zoneList, tableData, false);
            // 周
        } else if (dateType.equals("week")) {
            cal.add(Calendar.DAY_OF_MONTH, 7);
            if (DateUtils.isSameDay(date, cur.getTime())) {// 查询当前周 且今天为周一 即查询当日数据
                baseData(cur, zoneStr, zoneList, tableData);
            } else if (cal.getTimeInMillis() > cur.getTimeInMillis()) {// 查询当前周 但今天不是周一
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cur.getTime()));
                checkZone(tableData, zoneList);
                baseData(cur, zoneStr, zoneList, tableData);
            } else {// 查询非当前周
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cal.getTime()));
                checkZone(tableData, zoneList);
            }
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(date);
            long endLong = cal.getTimeInMillis();
            while (startCal.getTimeInMillis() < endLong) {
                persistenceData(startCal, zoneStr, zoneList, tableData, true);
            }
            startCal.setTime(date);
            while (startCal.getTimeInMillis() < endLong) {
                ltvData(startCal, zoneStr, zoneList, tableData, false);
            }
            // 月
        } else {
            cal.add(Calendar.MONTH, 1);
            if (DateUtils.isSameDay(date, cur.getTime())) {// 查询当前月 且今天为一号 即查询当日数据
                baseData(cur, zoneStr, zoneList, tableData);
            } else if (cal.getTimeInMillis() > cur.getTimeInMillis()) {// 查询当前月 但今天不是一号
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cur.getTime()));
                checkZone(tableData, zoneList);
                baseData(cur, zoneStr, zoneList, tableData);
            } else {// 查询非当前月
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cal.getTime()));
                checkZone(tableData, zoneList);
            }
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(date);
            long endLong = cal.getTimeInMillis();
            while (startCal.getTimeInMillis() < endLong) {
                persistenceData(startCal, zoneStr, zoneList, tableData, true);
            }
            startCal.setTime(date);
            while (startCal.getTimeInMillis() < endLong) {
                ltvData(startCal, zoneStr, zoneList, tableData, false);
            }
        }
        return tableData;

    }

    /**
     * @Description:查询详细付费数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    public List<ActiveBaseData> paySelect(String dateType, String dateStr, String[] zoneArr) {
        List<ActiveBaseData> tableData = new ArrayList<>();
        List<String> zoneList = new ArrayList<>();
        zoneList.addAll(Arrays.asList(zoneArr));// 要查询的区
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date calDate = cal.getTime();
        Calendar cur = Calendar.getInstance();
        String format = sdf.format(new Date());
        try {
            cur.setTime(sdf.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 日
        if (dateType.equals("day")) {
            if (DateUtils.isSameDay(calDate, cur.getTime())) {// 查询当天数据
                payData(cur, zoneStr, zoneList, tableData);
            } else {
                tableData.add(activeBaseDataReposistory.findByDate(dateStr));
                checkZone(tableData, zoneList);
            }
            // 周
        } else if (dateType.equals("week")) {
            cal.add(Calendar.DAY_OF_MONTH, 7);
            if (DateUtils.isSameDay(calDate, cur.getTime())) {// 查询当前周 且今天为周一 即查询当日数据
                payData(cur, zoneStr, zoneList, tableData);
            } else if (cal.getTimeInMillis() > cur.getTimeInMillis()) {// 查询当前周 但今天不是周一
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cur.getTime()));
                checkZone(tableData, zoneList);
                payData(cur, zoneStr, zoneList, tableData);
            } else {// 查询非当前周
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cal.getTime()));
                checkZone(tableData, zoneList);
            }
            // 月
        } else {
            cal.add(Calendar.MONTH, 1);
            if (DateUtils.isSameDay(calDate, cur.getTime())) {// 查询当前月 且今天为一号 即查询当日数据
                payData(cur, zoneStr, zoneList, tableData);
            } else if (cal.getTimeInMillis() > cur.getTimeInMillis()) {// 查询当前月 但今天不是一号
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cur.getTime()));
                checkZone(tableData, zoneList);
                payData(cur, zoneStr, zoneList, tableData);
            } else {// 查询非当前月
                tableData = activeBaseDataReposistory.findByDate(dateStr, sdf.format(cal.getTime()));
                checkZone(tableData, zoneList);
            }
        }
        return tableData;
    }

    /**
     * @Description:详细付费数据查询
     * @param cur
     * @param zoneStr
     * @param zoneList
     * @param tableData
     * @throws
     */
    private void payData(Calendar cal, String zoneStr, List<String> zoneList, List<ActiveBaseData> tableData) {
        long startDate = cal.getTime().getTime();// yyyy-MM-21 00:00:00
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endDate = cal.getTime().getTime();// yyyy-MM-22 00:00:00

        // 当天注册用户
        String registerIds = queryNewLordIdsStr(startDate, endDate, zoneList);
        // 新注册付费用户
        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        String matchStr = "{$match:{$and:[{create_time:{$gt:" + startDate + "}},{create_time:{$lt:" + endDate + "}}]}}";
        String group = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:1}}}";
        String matchlordIds = "{$match:{_id:{$in:[" + registerIds + "]}}}";
        getNum(startDate, tableData, "game_topup_first_record", "setNewUserPayer", int.class, group, matchStr,
                matchlordIds);
        // 首冲用户（新增付费用户）
        String matchNewPayerNum = "{$match:{$and:[{create_time:{$gt:" + startDate + "}},{create_time:{$lt:" + endDate
                + "}}]}}";
        getNum(startDate, tableData, "game_topup_first_record", "setNewPayerNum", int.class, group, matchZoneStr,
                matchNewPayerNum);
        // 付费人数
        String matchPayerNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + endDate + "}}]}}";
        getNum(startDate, tableData, "game_payer", "setPayerNum", int.class, group, matchZoneStr, matchPayerNum);
        // 新增用户数
        String matchNewUserNum = "{$match:{$and:[{register_time:{$gt:" + startDate + "}},{register_time:{$lt:" + endDate
                + "}}]}}";
        getNum(startDate, tableData, "game_register", "setNewUserNum", int.class, group, matchZoneStr, matchNewUserNum);
    }

    /**
     * @Description:过滤要查询的区,重新计算天总计
     * @param tableData
     * @param zoneList
     * @throws
     */
    private void checkZone(List<ActiveBaseData> tableData, List<String> zoneList) {
        for (ActiveBaseData activeBaseData : tableData) {
            if (activeBaseData == null)
                continue;
            Map<String, Map<String, ActiveBasePackage>> zoneData = activeBaseData.getZoneData();
            Iterator<Entry<String, Map<String, ActiveBasePackage>>> iterator = zoneData.entrySet().iterator();
            Map<String, ActiveBasePackage> dayTotal = new HashMap<>();
            ActiveBasePackage abp = new ActiveBasePackage();
            dayTotal.put(ActiveBaseData.DAY_TOTAL, abp);
            int activeNum = 0;
            int newUserNum = 0;
            int newPayerNum = 0;
            int payerNum = 0;
            double incomeNum = 0D;
            int incomeTimes = 0;
            int preOneNewUserNum = 0;
            int preTwoNewUserNum = 0;
            int preThreeNewUserNum = 0;
            int preFourNewUserNum = 0;
            int preFiveNewUserNum = 0;
            int preSixNewUserNum = 0;
            int preThirteenNewUserNum = 0;
            int preFourteenNewUserNum = 0;
            int preTwentyNewUserNum = 0;
            int preTwentySevenNewUserNum = 0;
            int preTwentyNineNewUserNum = 0;
            int preThirtyFourNewUserNum = 0;
            int preFortyEightNewUserNum = 0;
            int preFiftyFiveNewUserNum = 0;
            int preOnePersistence = 0;
            int preTwoPersistence = 0;
            int preThreePersistence = 0;
            int preFourPersistence = 0;
            int preFivePersistence = 0;
            int preSixPersistence = 0;
            int preFourteenPersistence = 0;
            int preTwentyNinePersistence = 0;
            double persentIncomeNum = 0D;
            double preOneIncomeNum = 0D;
            double preTwoIncomeNum = 0D;
            double preSixIncomeNum = 0D;
            double preThirteenIncomeNum = 0D;
            double preTwentyIncomeNum = 0D;
            double preTwentySevenIncomeNum = 0D;
            double preThirtyFourIncomeNum = 0D;
            double preFortyEightIncomeNum = 0D;
            double preFiftyFiveIncomeNum = 0D;
            int newUserPayer = 0;
            while (iterator.hasNext()) {
                String zoneId = iterator.next().getKey();
                Map<String, ActiveBasePackage> packageData = zoneData.get(zoneId);
                if (!zoneList.contains(zoneId)) {
                    iterator.remove();
                    continue;
                }
                ActiveBasePackage temp = packageData.get(ActiveBaseData.ZONE_TOTAL);
                activeNum += temp.getActiveNum();
                newUserNum += temp.getNewUserNum();
                newPayerNum += temp.getNewPayerNum();
                payerNum += temp.getPayerNum();
                incomeNum += temp.getIncomeNum();
                incomeTimes += temp.getIncomeTimes();
                preOneNewUserNum += temp.getPreOneNewUserNum();
                preTwoNewUserNum += temp.getPreOneNewUserNum();
                preThreeNewUserNum += temp.getPreThreeNewUserNum();
                preFourNewUserNum += temp.getPreFourNewUserNum();
                preFiveNewUserNum += temp.getPreFiveNewUserNum();
                preSixNewUserNum += temp.getPreSixNewUserNum();
                preThirteenNewUserNum += temp.getPreFourteenNewUserNum();
                preFourteenNewUserNum += temp.getPreFourteenNewUserNum();
                preTwentyNewUserNum += temp.getPreTwentyNewUserNum();
                preTwentySevenNewUserNum += temp.getPreTwentySevenNewUserNum();
                preTwentyNineNewUserNum += temp.getPreTwentyNineNewUserNum();
                preThirtyFourNewUserNum += temp.getPreThirtyFourNewUserNum();
                preFortyEightNewUserNum += temp.getPreFortyEightNewUserNum();
                preFiftyFiveNewUserNum += temp.getPreFiftyFiveNewUserNum();
                preOnePersistence += temp.getPreOnePersistence();
                preTwoPersistence += temp.getPreTwoPersistence();
                preThreePersistence += temp.getPreThreePersistence();
                preFourPersistence += temp.getPreFourPersistence();
                preFivePersistence += temp.getPreFivePersistence();
                preSixPersistence += temp.getPreSixPersistence();
                preFourteenPersistence += temp.getPreFourteenPersistence();
                preTwentyNinePersistence += temp.getPreTwentyNinePersistence();
                persentIncomeNum += temp.getPersentIncomeNum();
                preOneIncomeNum += temp.getPreOneIncomeNum();
                preTwoIncomeNum += temp.getPreTwoIncomeNum();
                preSixIncomeNum += temp.getPreSixIncomeNum();
                preTwentyIncomeNum += temp.getPreTwentySevenIncomeNum();
                preTwentySevenIncomeNum += temp.getPreTwentySevenIncomeNum();
                preThirtyFourIncomeNum += temp.getPreThirtyFourIncomeNum();
                preFortyEightIncomeNum += temp.getPreFortyEightIncomeNum();
                preFiftyFiveIncomeNum += temp.getPreFiftyFiveIncomeNum();
                newUserPayer += temp.getNewUserPayer();
            }
            abp.setActiveNum(activeNum);
            abp.setNewUserNum(newUserNum);
            abp.setNewPayerNum(newPayerNum);
            abp.setPayerNum(payerNum);
            abp.setIncomeNum(incomeNum);
            abp.setIncomeTimes(incomeTimes);
            abp.setPreOneNewUserNum(preOneNewUserNum);
            abp.setPreTwoNewUserNum(preTwoNewUserNum);
            abp.setPreThreeNewUserNum(preThreeNewUserNum);
            abp.setPreFourNewUserNum(preFourNewUserNum);
            abp.setPreFiveNewUserNum(preFiveNewUserNum);
            abp.setPreSixNewUserNum(preSixNewUserNum);
            abp.setPreThirteenNewUserNum(preThirteenNewUserNum);
            abp.setPreFourteenNewUserNum(preFourteenNewUserNum);
            abp.setPreTwentyNewUserNum(preTwentyNewUserNum);
            abp.setPreTwentySevenNewUserNum(preTwentySevenNewUserNum);
            abp.setPreTwentyNineNewUserNum(preTwentyNineNewUserNum);
            abp.setPreThirtyFourNewUserNum(preThirtyFourNewUserNum);
            abp.setPreFortyEightNewUserNum(preFortyEightNewUserNum);
            abp.setPreFiftyFiveNewUserNum(preFiftyFiveNewUserNum);
            abp.setPreOnePersistence(preOnePersistence);
            abp.setPreTwoPersistence(preTwoPersistence);
            abp.setPreThreePersistence(preThreePersistence);
            abp.setPreFourPersistence(preFourPersistence);
            abp.setPreFivePersistence(preFivePersistence);
            abp.setPreSixPersistence(preSixPersistence);
            abp.setPreFourteenPersistence(preFourteenPersistence);
            abp.setPreTwentyNinePersistence(preTwentyNinePersistence);
            abp.setPersentIncomeNum(persentIncomeNum);
            abp.setPreOneIncomeNum(preOneIncomeNum);
            abp.setPreTwoIncomeNum(preTwoIncomeNum);
            abp.setPreSixIncomeNum(preSixIncomeNum);
            abp.setPreThirteenIncomeNum(preThirteenIncomeNum);
            abp.setPreTwentyIncomeNum(preTwentyIncomeNum);
            abp.setPreTwentySevenIncomeNum(preTwentySevenIncomeNum);
            abp.setPreThirtyFourIncomeNum(preThirtyFourIncomeNum);
            abp.setPreFortyEightIncomeNum(preFortyEightIncomeNum);
            abp.setPreFiftyFiveIncomeNum(preFiftyFiveIncomeNum);
            abp.setNewUserPayer(newUserPayer);

            zoneData.put(ActiveBaseData.DAY_TOTAL, dayTotal);
        }

    }

    /**
     * @Description:每日定时统计任务，记录当天所有区服的数据
     * @throws
     */
    public void timerRecordTodayData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(sdf.format(cal.getTime()));
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startDate = cal.getTime().getTime();// yyyy-MM-21 00:00:00
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endDate = cal.getTime().getTime();// yyyy-MM-22 00:00:00

        long preOneStart = startDate - DateUtils.MILLIS_PER_DAY;
        long preOneEnd = endDate - DateUtils.MILLIS_PER_DAY;

        long preTwoStart = startDate - DateUtils.MILLIS_PER_DAY * 2;
        long preTwoEnd = endDate - DateUtils.MILLIS_PER_DAY * 2;

        long preThreeStart = startDate - DateUtils.MILLIS_PER_DAY * 3;
        long preThreeEnd = endDate - DateUtils.MILLIS_PER_DAY * 3;

        long preFourStart = startDate - DateUtils.MILLIS_PER_DAY * 4;
        long preFourEnd = endDate - DateUtils.MILLIS_PER_DAY * 4;

        long preFiveStart = startDate - DateUtils.MILLIS_PER_DAY * 5;
        long preFiveEnd = endDate - DateUtils.MILLIS_PER_DAY * 5;

        long preSixStart = startDate - DateUtils.MILLIS_PER_DAY * 6;
        long preSixEnd = endDate - DateUtils.MILLIS_PER_DAY * 6;

        long preFourteenStart = startDate - DateUtils.MILLIS_PER_DAY * 14;
        long preFourteenEnd = endDate - DateUtils.MILLIS_PER_DAY * 14;

        long preTwentyNineStart = startDate - DateUtils.MILLIS_PER_DAY * 29;
        long preTwentyNineEnd = endDate - DateUtils.MILLIS_PER_DAY * 29;

        List<ActiveBaseData> tableData = new ArrayList<>();
        // 活跃数
        String matchLoginTime = "{$match:{$and:[{login_time:{$gt:" + startDate + "}},{login_time:{$lt:" + endDate
                + "}}]}}";
        String group = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:1}}}";
        getNum(startDate, tableData, "game_login", "setActiveNum", int.class, group, matchLoginTime);
        // 新增用户数
        String matchNewUserNum = "{$match:{$and:[{register_time:{$gt:" + startDate + "}},{register_time:{$lt:" + endDate
                + "}}]}}";
        getNum(startDate, tableData, "game_register", "setNewUserNum", int.class, group, matchNewUserNum);
        // 新增付费用户
        String matchNewPayerNum = "{$match:{$and:[{create_time:{$gt:" + startDate + "}},{create_time:{$lt:" + endDate
                + "}}]}}";
        getNum(startDate, tableData, "game_topup_first_record", "setNewPayerNum", int.class, group, matchNewPayerNum);
        // 付费人数
        String matchPayerNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + endDate + "}}]}}";
        getNum(startDate, tableData, "game_payer", "setPayerNum", int.class, group, matchPayerNum);
        // 收入金额
        String groupPrice = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:'$price'}}}";
        String matchIncomeNum = "{$match:{$and:[{time:{$gt:" + startDate + "}},{time:{$lt:" + endDate + "}}]}}";
        incomeNum(startDate, tableData, "game_order", "setIncomeNum", double.class, groupPrice, matchIncomeNum);
        // 收入次数
        String matchIncomeTimes = "{$match:{$and:[{time:{$gt:" + startDate + "}},{time:{$lt:" + endDate + "}}]}}";
        getNum(startDate, tableData, "game_order", "setIncomeTimes", int.class, group, matchIncomeTimes);

        // 前一天新增用户数
        String matchRegisterTime = "{$match:{$and:[{register_time:{$gt:" + preOneStart + "}},{register_time:{$lt:"
                + preOneEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreOneNewUserNum", int.class, group, matchRegisterTime);
        // 前一天注册用户
        String preOneLordIdsStr = queryNewLordIdsStr(preOneStart, preOneEnd, null);
        // 前一天存留数
        String matchLordsPreOnePersistence = "{$match:{lord_id:{$in:[" + preOneLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreOnePersistence", int.class, group, matchLoginTime,
                matchLordsPreOnePersistence);
        // 前两天新增用户数
        String matchPreTwoNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preTwoStart + "}},{register_time:{$lt:"
                + preTwoEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreTwoNewUserNum", int.class, group, matchPreTwoNewUserNum);
        // 前两天注册用户ids
        String preTwoLordIdsStr = queryNewLordIdsStr(preTwoStart, preTwoEnd, null);
        // 前两天存留数
        String matchLordsPreTwoPersistence = "{$match:{lord_id:{$in:[" + preTwoLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreTwoPersistence", int.class, group, matchLoginTime,
                matchLordsPreTwoPersistence);
        // 前三天新增用户
        String matchPreThreeNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preThreeStart
                + "}},{register_time:{$lt:" + preThreeEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreThreeNewUserNum", int.class, group,
                matchPreThreeNewUserNum);
        // 前三天存留数
        String preThreeLordIdsStr = queryNewLordIdsStr(preThreeStart, preThreeEnd, null);
        String matchLordsPreThreePersistence = "{$match:{lord_id:{$in:[" + preThreeLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreThreePersistence", int.class, group, matchLoginTime,
                matchLordsPreThreePersistence);
        // 前四天新增用户
        String matchPreFourNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preFourStart + "}},{register_time:{$lt:"
                + preFourEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreFourNewUserNum", int.class, group, matchPreFourNewUserNum);
        // 前四天存留数
        String preFourLordIdsStr = queryNewLordIdsStr(preFourStart, preFourEnd, null);
        String matchLordsPreFourPersistence = "{$match:{lord_id:{$in:[" + preFourLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreFourPersistence", int.class, group, matchLoginTime,
                matchLordsPreFourPersistence);
        // 前五天新增用户
        String matchPreFiveNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preFiveStart + "}},{register_time:{$lt:"
                + preFiveEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreFiveNewUserNum", int.class, group, matchPreFiveNewUserNum);
        // 前五天存留数
        String preFiveLordIdsStr = queryNewLordIdsStr(preFiveStart, preFiveEnd, null);
        String matchLordsPreFivePersistence = "{$match:{lord_id:{$in:[" + preFiveLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreFivePersistence", int.class, group, matchLoginTime,
                matchLordsPreFivePersistence);
        // 前六天新增用户数
        String matchPreSixNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preSixStart + "}},{register_time:{$lt:"
                + preSixEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreSixNewUserNum", int.class, group, matchPreSixNewUserNum);
        // 前六天存留数
        String preSixLordIdsStr = queryNewLordIdsStr(preSixStart, preSixEnd, null);
        String matchLordsPreSixPersistence = "{$match:{lord_id:{$in:[" + preSixLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreSixPersistence", int.class, group, matchLoginTime,
                matchLordsPreSixPersistence);
        // 前十四天新增用户
        String matchPreFourteenNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preFourteenStart
                + "}},{register_time:{$lt:" + preFourteenEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreFourteenNewUserNum", int.class, group,
                matchPreFourteenNewUserNum);
        // 前十四天存留数
        String preFourteenLordIdsStr = queryNewLordIdsStr(preFourteenStart, preFourteenEnd, null);
        String matchLordsPreFourteenPersistence = "{$match:{lord_id:{$in:[" + preFourteenLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreFourteenPersistence", int.class, group, matchLoginTime,
                matchLordsPreFourteenPersistence);
        // 前二十九天新增用户
        String matchPreTwentyNineNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preTwentyNineStart
                + "}},{register_time:{$lt:" + preTwentyNineEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreTwentyNineNewUserNum", int.class, group,
                matchPreTwentyNineNewUserNum);
        // 前二十九天存留数
        String preTwentyNineLordIdsStr = queryNewLordIdsStr(preTwentyNineStart, preTwentyNineEnd, null);
        String matchLordsPreTwentyNinePersistence = "{$match:{lord_id:{$in:[" + preTwentyNineLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreFourteenPersistence", int.class, group, matchLoginTime,
                matchLordsPreTwentyNinePersistence);

        // 前一天注册的用户付费总数
        String matchPreOneIncomeNum = "{$match:{$and:[{time:{$gt:" + preOneStart + "}},{time:{$lt:" + endDate + "}}]}}";
        String matchLordsPreOneIncomeNum = "{$match:{lord_id:{$in:[" + preOneLordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice,
                matchPreOneIncomeNum, matchLordsPreOneIncomeNum);
        // 前两天注册的付费总数
        String matchPreTwoIncomeNum = "{$match:{$and:[{time:{$gt:" + preTwoStart + "}},{time:{$lt:" + endDate + "}}]}}";
        String matchLordsPreTwoIncomeNum = "{$match:{lord_id:{$in:[" + preTwoLordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice,
                matchPreTwoIncomeNum, matchLordsPreTwoIncomeNum);
        // 前六天注册的付费总数
        String matchPreSixIncomeNum = "{$match:{$and:[{time:{$gt:" + preSixStart + "}},{time:{$lt:" + endDate + "}}]}}";
        String matchLordsPreSixIncomeNum = "{$match:{lord_id:{$in:[" + preSixLordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice,
                matchPreSixIncomeNum, matchLordsPreSixIncomeNum);
        // 当天注册用户
        String registerIds = queryNewLordIdsStr(startDate, endDate, null);
        // 新注册付费用户
        String matchCreateTimeStr = "{$match:{$and:[{create_time:{$gt:" + startDate + "}},{create_time:{$lt:" + endDate
                + "}}]}}";
        String matchlordIds = "{$match:{_id:{$in:[" + registerIds + "]}}}";
        getNum(startDate, tableData, "game_topup_first_record", "setNewUserPayer", int.class, group, matchCreateTimeStr,
                matchlordIds);

        ActiveBaseData activeBaseData = tableData.get(0);
        activeBaseDataReposistory.save(activeBaseData);

        // --------------------------

    }

    /**
     * @Description:基础数据查询计算
     * @param cal
     * @param zoneStr
     * @param zoneList
     * @param tableData
     * @throws
     */
    private void baseData(Calendar cal, String zoneStr, List<String> zoneList, List<ActiveBaseData> tableData) {
        long startDate = cal.getTime().getTime();// yyyy-MM-21 00:00:00
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endDate = cal.getTime().getTime();// yyyy-MM-22 00:00:00

        long preOneStart = startDate - DateUtils.MILLIS_PER_DAY;
        long preOneEnd = endDate - DateUtils.MILLIS_PER_DAY;

        long preTwoStart = startDate - DateUtils.MILLIS_PER_DAY * 2;
        long preTwoEnd = endDate - DateUtils.MILLIS_PER_DAY * 2;

        long preSixStart = startDate - DateUtils.MILLIS_PER_DAY * 6;
        long preSixEnd = endDate - DateUtils.MILLIS_PER_DAY * 6;

        // 活跃数
        String matchStr = "{$match:{$and:[{login_time:{$gt:" + startDate + "}},{login_time:{$lt:" + endDate + "}}]}}";
        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        String group = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:1}}}";
        getNum(startDate, tableData, "game_login", "setActiveNum", int.class, group, matchZoneStr, matchStr);
        // 新增用户数
        String matchNewUserNum = "{$match:{$and:[{register_time:{$gt:" + startDate + "}},{register_time:{$lt:" + endDate
                + "}}]}}";
        getNum(startDate, tableData, "game_register", "setNewUserNum", int.class, group, matchZoneStr, matchNewUserNum);
        // 新增付费用户
        String matchNewPayerNum = "{$match:{$and:[{create_time:{$gt:" + startDate + "}},{create_time:{$lt:" + endDate
                + "}}]}}";
        getNum(startDate, tableData, "game_topup_first_record", "setNewPayerNum", int.class, group, matchZoneStr,
                matchNewPayerNum);
        // 付费人数
        String matchPayerNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + endDate + "}}]}}";
        getNum(startDate, tableData, "game_payer", "setPayerNum", int.class, group, matchZoneStr, matchPayerNum);
        // 收入金额
        String groupPrice = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:'$price'}}}";
        String matchIncomeNum = "{$match:{$and:[{time:{$gt:" + startDate + "}},{time:{$lt:" + endDate + "}}]}}";
        incomeNum(startDate, tableData, "game_order", "setIncomeNum", double.class, groupPrice, matchZoneStr,
                matchIncomeNum);
        // 收入次数
        // incomeTimes(startDate, endDate, zoneStr, tableData);
        String matchIncomeTimes = "{$match:{$and:[{time:{$gt:" + startDate + "}},{time:{$lt:" + endDate + "}}]}}";
        getNum(startDate, tableData, "game_order", "setIncomeTimes", int.class, group, matchZoneStr, matchIncomeTimes);
        // 前一天新增用户数
        String matchPreOneNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preOneStart + "}},{register_time:{$lt:"
                + preOneEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreOneNewUserNum", int.class, group, matchZoneStr,
                matchPreOneNewUserNum);
        // 前一天注册用户
        String preOneLordIdsStr = queryNewLordIdsStr(preOneStart, preOneEnd, zoneList);
        // 前一天存留数
        String matchLordsPreOnePersistence = "{$match:{lord_id:{$in:[" + preOneLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreOnePersistence", int.class, group, matchZoneStr, matchStr,
                matchLordsPreOnePersistence);
        // 前两天新增用户数
        String matchPreTwoNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preTwoStart + "}},{register_time:{$lt:"
                + preTwoEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreTwoNewUserNum", int.class, group, matchZoneStr,
                matchPreTwoNewUserNum);
        // 前两天注册用户ids
        String preTwoLordIdsStr = queryNewLordIdsStr(preTwoStart, preTwoEnd, zoneList);
        // 前两天存留数
        String matchLordsPreTwoPersistence = "{$match:{lord_id:{$in:[" + preTwoLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreTwoPersistence", int.class, group, matchZoneStr, matchStr,
                matchLordsPreTwoPersistence);
        // 前六天新增用户数
        String matchPreSixNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preSixStart + "}},{register_time:{$lt:"
                + preSixEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreSixNewUserNum", int.class, group, matchZoneStr,
                matchPreSixNewUserNum);
        // 前六天存留数
        String preSixLordIdsStr = queryNewLordIdsStr(preSixStart, preSixEnd, zoneList);
        String matchLordsPreSixPersistence = "{$match:{lord_id:{$in:[" + preSixLordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreSixPersistence", int.class, group, matchZoneStr, matchStr,
                matchLordsPreSixPersistence);
        // 前一天注册的用户付费总数
        String matchPreOneIncomeNum = "{$match:{$and:[{time:{$gt:" + preOneStart + "}},{time:{$lt:" + endDate + "}}]}}";
        String matchLordsPreOneIncomeNum = "{$match:{lord_id:{$in:[" + preOneLordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice, matchZoneStr,
                matchPreOneIncomeNum, matchLordsPreOneIncomeNum);
        // 前两天注册的付费总数
        String matchPreTwoIncomeNum = "{$match:{$and:[{time:{$gt:" + preTwoStart + "}},{time:{$lt:" + endDate + "}}]}}";
        String matchLordsPreTwoIncomeNum = "{$match:{lord_id:{$in:[" + preTwoLordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice, matchZoneStr,
                matchPreTwoIncomeNum, matchLordsPreTwoIncomeNum);
        // 前六天注册的付费总数
        String matchPreSixIncomeNum = "{$match:{$and:[{time:{$gt:" + preSixStart + "}},{time:{$lt:" + endDate + "}}]}}";
        String matchLordsPreSixIncomeNum = "{$match:{lord_id:{$in:[" + preSixLordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice, matchZoneStr,
                matchPreSixIncomeNum, matchLordsPreSixIncomeNum);
    }

    /**
     * @Description:新增用户Ids
     * @param startDate
     * @param endDate
     * @param zoneList
     * @return
     * @throws
     */
    private String queryNewLordIdsStr(Long startDate, Long endDate, List<String> zoneList) {
        List<String> lordIds = queryNewLordIds(startDate, endDate, zoneList);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < lordIds.size(); j++) {
            sb.append("'").append(lordIds.get(j)).append("'");
            if (j != lordIds.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * @Description:新增用户Ids
     * @param startDate
     * @param endDate
     * @param zoneList
     * @return
     * @throws
     */
    private List<String> queryNewLordIds(Long startDate, Long endDate, List<String> zoneList) {
        List<String> registerIds = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("register_time").gt(startDate).lt(endDate));
        if (zoneList != null) {
            query.addCriteria(Criteria.where("zone_id").in(zoneList));
        }
        List<Register> registers = mongoTemplate.find(query, Register.class);
        for (Register register : registers) {
            registerIds.add(register.getId());
        }
        return registerIds;
    }

    /**
     * @Description:收入次数
     * @param startDate
     * @param endDate
     * @param zoneStr
     * @param tableData
     * @throws
     */
    private void incomeTimes(long startDate, long endDate, String zoneList, List<ActiveBaseData> tableData) {
        List<DBObject> pipeline = new ArrayList<>();

        String groupStr = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:1}}}";

        String matchStr = "{$match:{$and:[{time:{$gt:" + startDate + "}},{time:{$lt:" + endDate + "}}]}}";

        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneList + "]}}}";
        DBObject match = (DBObject) JSON.parse(matchStr);
        DBObject matchZone = (DBObject) JSON.parse(matchZoneStr);
        DBObject group = (DBObject) JSON.parse(groupStr);
        pipeline.add(match);
        pipeline.add(matchZone);
        pipeline.add(group);
        AggregationOutput output = mongoTemplate.getCollection("game_order").aggregate(pipeline);
        Iterator<DBObject> iterator = output.results().iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateKey = sdf.format(new Date(startDate));

        ActiveBaseData abd = null;
        int index = -1;
        for (int i = 0; i < tableData.size(); i++) {
            ActiveBaseData temp = tableData.get(i);
            if (temp.getDate().equals(dateKey)) {
                abd = temp;
                index = i;
            }
        }

        if (abd == null) {
            abd = new ActiveBaseData();
            abd.setDate(dateKey);
        }
        Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
        if (zoneData == null) {
            zoneData = new TreeMap<>();
        }
        // 天总计
        Map<String, ActiveBasePackage> dayTotalData = zoneData.get(ActiveBaseData.DAY_TOTAL);
        if (dayTotalData == null) {
            dayTotalData = new TreeMap<>();
            dayTotalData.put(ActiveBaseData.DAY_TOTAL, new ActiveBasePackage());
        }

        int zoneTotal = 0;// 区总计
        int dayTotal = 0;// 天总计
        String zoneFlag = "";// 标记区
        int flag = 0;
        while (iterator.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) iterator.next();
            BasicDBObject keyValues = (BasicDBObject) dbo.get("_id");
            String zoneId = keyValues.getString("zone_id");
            String packageId = keyValues.getString("package_id");
            int total = dbo.getInt("total");

            Map<String, ActiveBasePackage> zoneMap = zoneData.get(zoneId);

            if (zoneMap == null) {
                zoneMap = new TreeMap<>();
            }
            ActiveBasePackage abp = zoneMap.get(packageId);
            if (abp == null) {
                abp = new ActiveBasePackage();
            }
            abp.setIncomeTimes(total);

            zoneMap.put(packageId, abp);
            zoneData.put(zoneId, zoneMap);

            if (flag == 0) {
                zoneFlag = zoneId;
                flag++;
            }

            // 区总计 计算
            if (!zoneFlag.equals(zoneId)) {
                Map<String, ActiveBasePackage> preZoneMap = zoneData.get(zoneFlag);
                // 区总计
                ActiveBasePackage zoneTotalData = preZoneMap.get(ActiveBaseData.ZONE_TOTAL);
                if (zoneTotalData == null) {
                    zoneTotalData = new ActiveBasePackage();
                }
                zoneTotalData.setIncomeTimes(zoneTotal);
                preZoneMap.put(ActiveBaseData.ZONE_TOTAL, zoneTotalData);
                zoneData.put(zoneFlag, preZoneMap);
                zoneTotal = 0;
                zoneFlag = zoneId;
            }
            zoneTotal += total;
            dayTotal += total;
            if (!iterator.hasNext()) {
                // 区总计
                ActiveBasePackage zoneTotalData = zoneMap.get(ActiveBaseData.ZONE_TOTAL);
                if (zoneTotalData == null) {
                    zoneTotalData = new ActiveBasePackage();
                }
                zoneTotalData.setIncomeTimes(zoneTotal);
                zoneMap.put(ActiveBaseData.ZONE_TOTAL, zoneTotalData);
                zoneData.put(zoneFlag, zoneMap);
                // 天总计
                ActiveBasePackage activeBasePackage = dayTotalData.get(ActiveBaseData.DAY_TOTAL);
                activeBasePackage.setIncomeTimes(dayTotal);
                dayTotalData.put(ActiveBaseData.DAY_TOTAL, activeBasePackage);
                zoneData.put(ActiveBaseData.DAY_TOTAL, dayTotalData);
            }

        }

        abd.setZoneData(zoneData);
        if (index == -1) {
            tableData.add(abd);
        }
    }

    /**
     * @Description:收入金额
     * @param startDate
     * @param endDate
     * @param zoneStr
     * @param tableData
     * @throws
     */
    private void incomeNum(Long startDate, List<ActiveBaseData> tableData, String collectionName, String methodName,
            Class param, String group, String... querys) {
        List<DBObject> pipeline = new ArrayList<>();
        for (String str : querys) {
            DBObject query = (DBObject) JSON.parse(str);
            pipeline.add(query);
        }
        DBObject groupDBO = (DBObject) JSON.parse(group);
        pipeline.add(groupDBO);
        AggregationOutput output = mongoTemplate.getCollection(collectionName).aggregate(pipeline);
        Iterator<DBObject> iterator = output.results().iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateKey = sdf.format(new Date(startDate));

        ActiveBaseData abd = null;
        int index = -1;
        for (int i = 0; i < tableData.size(); i++) {
            ActiveBaseData temp = tableData.get(i);
            if (temp.getDate().equals(dateKey)) {
                abd = temp;
                index = i;
            }
        }

        if (abd == null) {
            abd = new ActiveBaseData();
            abd.setDate(dateKey);
        }
        Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
        if (zoneData == null) {
            zoneData = new TreeMap<>();
        }
        // 天总计
        Map<String, ActiveBasePackage> dayTotalData = zoneData.get(ActiveBaseData.DAY_TOTAL);
        if (dayTotalData == null) {
            dayTotalData = new TreeMap<>();
            dayTotalData.put(ActiveBaseData.DAY_TOTAL, new ActiveBasePackage());
        }
        zoneData.put(ActiveBaseData.DAY_TOTAL, dayTotalData);
        Method method = null;
        try {
            method = ActiveBasePackage.class.getMethod(methodName, param);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        double zoneTotal = 0;// 区总计
        double dayTotal = 0;// 天总计
        String zoneFlag = "";// 标记区
        int flag = 0;
        while (iterator.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) iterator.next();
            BasicDBObject keyValues = (BasicDBObject) dbo.get("_id");
            String zoneId = keyValues.getString("zone_id");
            String packageId = keyValues.getString("package_id");
            double total = dbo.getDouble("total");

            Map<String, ActiveBasePackage> zoneMap = zoneData.get(zoneId);

            if (zoneMap == null) {
                zoneMap = new TreeMap<>();
            }
            ActiveBasePackage abp = zoneMap.get(packageId);
            if (abp == null) {
                abp = new ActiveBasePackage();
            }
            // abp.setIncomeNum(total);
            try {
                method.invoke(abp, total);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            zoneMap.put(packageId, abp);
            zoneData.put(zoneId, zoneMap);

            if (flag == 0) {
                zoneFlag = zoneId;
                flag++;
            }

            // 区总计 计算
            if (!zoneFlag.equals(zoneId)) {
                Map<String, ActiveBasePackage> preZoneMap = zoneData.get(zoneFlag);
                // 区总计
                ActiveBasePackage zoneTotalData = preZoneMap.get(ActiveBaseData.ZONE_TOTAL);
                if (zoneTotalData == null) {
                    zoneTotalData = new ActiveBasePackage();
                }
                // zoneTotalData.setIncomeNum(zoneTotal);
                try {
                    method.invoke(zoneTotalData, zoneTotal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                preZoneMap.put(ActiveBaseData.ZONE_TOTAL, zoneTotalData);
                zoneData.put(zoneFlag, preZoneMap);
                zoneTotal = 0;
                zoneFlag = zoneId;
            }
            zoneTotal += total;
            dayTotal += total;
            if (!iterator.hasNext()) {
                // 区总计
                ActiveBasePackage zoneTotalData = zoneMap.get(ActiveBaseData.ZONE_TOTAL);
                if (zoneTotalData == null) {
                    zoneTotalData = new ActiveBasePackage();
                }
                // zoneTotalData.setIncomeNum(zoneTotal);
                try {
                    method.invoke(zoneTotalData, zoneTotal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                zoneMap.put(ActiveBaseData.ZONE_TOTAL, zoneTotalData);
                zoneData.put(zoneFlag, zoneMap);
                // 天总计
                ActiveBasePackage activeBasePackage = dayTotalData.get(ActiveBaseData.DAY_TOTAL);
                // activeBasePackage.setIncomeNum(dayTotal);
                try {
                    method.invoke(activeBasePackage, dayTotal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                dayTotalData.put(ActiveBaseData.DAY_TOTAL, activeBasePackage);
                zoneData.put(ActiveBaseData.DAY_TOTAL, dayTotalData);
            }

        }

        abd.setZoneData(zoneData);
        if (index == -1) {
            tableData.add(abd);
        }
    }

    /**    
     * @Description:统计数量（整数）
     * @param startDate
     * @param tableData
     * @param collectionName 集合名
     * @param methodName 方法名
     * @param param 方法参数
     * @param group 分组条件
     * @param querys 查询条件
     * @throws
     */
    private void getNum(Long startDate, List<ActiveBaseData> tableData, String collectionName, String methodName,
            Class param, String group, String... querys) {
        List<DBObject> pipeline = new ArrayList<>();

        for (String query : querys) {
            DBObject dbo = (DBObject) JSON.parse(query);
            pipeline.add(dbo);
        }
        DBObject groupDBO = (DBObject) JSON.parse(group);
        pipeline.add(groupDBO);
        AggregationOutput output = mongoTemplate.getCollection(collectionName).aggregate(pipeline);
        Iterator<DBObject> iterator = output.results().iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateKey = sdf.format(new Date(startDate));

        ActiveBaseData abd = null;
        int index = -1;
        for (int i = 0; i < tableData.size(); i++) {
            ActiveBaseData temp = tableData.get(i);
            if (temp.getDate().equals(dateKey)) {
                abd = temp;
                index = i;
            }
        }

        if (abd == null) {
            abd = new ActiveBaseData();
            abd.setDate(dateKey);
        }
        Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
        if (zoneData == null) {
            zoneData = new TreeMap<>();
        }
        // 天总计
        Map<String, ActiveBasePackage> dayTotalData = zoneData.get(ActiveBaseData.DAY_TOTAL);
        if (dayTotalData == null) {
            dayTotalData = new TreeMap<>();
            dayTotalData.put(ActiveBaseData.DAY_TOTAL, new ActiveBasePackage());
        }
        zoneData.put(ActiveBaseData.DAY_TOTAL, dayTotalData);
        int zoneTotal = 0;// 区总计
        int dayTotal = 0;// 天总计
        String zoneFlag = "";// 标记区
        int flag = 0;
        Method method = null;
        try {
            method = ActiveBasePackage.class.getMethod(methodName, param);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        while (iterator.hasNext()) {
            BasicDBObject dbo = (BasicDBObject) iterator.next();
            BasicDBObject keyValues = (BasicDBObject) dbo.get("_id");
            String zoneId = keyValues.getString("zone_id");
            String packageId = keyValues.getString("package_id");
            int total = dbo.getInt("total");
            Map<String, ActiveBasePackage> zoneMap = zoneData.get(zoneId);

            if (zoneMap == null) {
                zoneMap = new TreeMap<>();
            }
            ActiveBasePackage abp = zoneMap.get(packageId);
            if (abp == null) {
                abp = new ActiveBasePackage();
            }
            // abp.setActiveNum(activeNum);
            try {
                method.invoke(abp, total);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            zoneMap.put(packageId, abp);
            zoneData.put(zoneId, zoneMap);

            if (flag == 0) {
                zoneFlag = zoneId;
                flag++;
            }

            // 区总计 计算
            if (!zoneFlag.equals(zoneId)) {
                Map<String, ActiveBasePackage> preZoneMap = zoneData.get(zoneFlag);
                // 区总计
                ActiveBasePackage zoneTotalData = preZoneMap.get(ActiveBaseData.ZONE_TOTAL);
                if (zoneTotalData == null) {
                    zoneTotalData = new ActiveBasePackage();
                }
                // zoneTotalData.setActiveNum(zoneTotal);
                try {
                    method.invoke(zoneTotalData, zoneTotal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                preZoneMap.put(ActiveBaseData.ZONE_TOTAL, zoneTotalData);
                zoneData.put(zoneFlag, preZoneMap);
                zoneTotal = 0;
                zoneFlag = zoneId;
            }
            zoneTotal += total;
            // 天总计 计算
            dayTotal += total;

            if (!iterator.hasNext()) {
                // 区总计
                ActiveBasePackage zoneTotalData = zoneMap.get(ActiveBaseData.ZONE_TOTAL);
                if (zoneTotalData == null) {
                    zoneTotalData = new ActiveBasePackage();
                }
                // zoneTotalData.setActiveNum(zoneTotal);
                try {
                    method.invoke(zoneTotalData, zoneTotal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                zoneMap.put(ActiveBaseData.ZONE_TOTAL, zoneTotalData);
                zoneData.put(zoneFlag, zoneMap);
                // 天总计
                ActiveBasePackage activeBasePackage = dayTotalData.get(ActiveBaseData.DAY_TOTAL);
                // activeBasePackage.setActiveNum(dayTotal);
                try {
                    method.invoke(activeBasePackage, dayTotal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                dayTotalData.put(ActiveBaseData.DAY_TOTAL, activeBasePackage);
                zoneData.put(ActiveBaseData.DAY_TOTAL, dayTotalData);
            }
        }

        abd.setZoneData(zoneData);
        if (index == -1) {
            tableData.add(abd);
        }
    }

    /**
     * @Description:详细存留数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    public List<ActiveBaseData> persistenceSelect(String dateType, String dateStr, String[] zoneArr) {
        List<ActiveBaseData> tableData = new ArrayList<>();
        List<String> zoneList = new ArrayList<>();
        zoneList.addAll(Arrays.asList(zoneArr));// 要查询的区
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date calDate = cal.getTime();
        Calendar cur = Calendar.getInstance();
        String format = sdf.format(new Date());
        try {
            cur.setTime(sdf.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 日
        if (dateType.equals("day")) {
            persistenceData(cal, zoneStr, zoneList, tableData, true);
            // 周
        } else if (dateType.equals("week")) {
            long endLong = cal.getTimeInMillis() + DateUtils.MILLIS_PER_DAY * 7;
            while (cal.getTimeInMillis() < endLong) {
                persistenceData(cal, zoneStr, zoneList, tableData, true);
            }
            // 月
        } else {
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(cal.getTime());
            endCal.add(Calendar.MONTH, 1);
            long endLong = endCal.getTimeInMillis();
            while (cal.getTimeInMillis() < endLong) {
                persistenceData(cal, zoneStr, zoneList, tableData, true);
            }
        }
        return tableData;
    }

    /**
     * @Description:
     * @param cal要查询当天的日期
     * @param zoneStr 区服
     * @param zoneList 区服
     * @param tableData 
     * @param flag true: 查询全部  false:查询次日 三日 七日留存
     * @throws
     */
    private void persistenceData(Calendar cal, String zoneStr, List<String> zoneList, List<ActiveBaseData> tableData,
            boolean flag) {
        long startDate = cal.getTime().getTime();// yyyy-MM-21 00:00:00
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endDate = cal.getTime().getTime();// yyyy-MM-22 00:00:00

        long preOneStart = startDate + DateUtils.MILLIS_PER_DAY;
        long preOneEnd = endDate + DateUtils.MILLIS_PER_DAY;

        long preTwoStart = startDate + DateUtils.MILLIS_PER_DAY * 2;
        long preTwoEnd = endDate + DateUtils.MILLIS_PER_DAY * 2;

        long preThreeStart = startDate + DateUtils.MILLIS_PER_DAY * 3;
        long preThreeEnd = endDate + DateUtils.MILLIS_PER_DAY * 3;

        long preFourStart = startDate + DateUtils.MILLIS_PER_DAY * 4;
        long preFourEnd = endDate + DateUtils.MILLIS_PER_DAY * 4;

        long preFiveStart = startDate + DateUtils.MILLIS_PER_DAY * 5;
        long preFiveEnd = endDate + DateUtils.MILLIS_PER_DAY * 5;

        long preSixStart = startDate + DateUtils.MILLIS_PER_DAY * 6;
        long preSixEnd = endDate + DateUtils.MILLIS_PER_DAY * 6;

        long preFourteenStart = startDate + DateUtils.MILLIS_PER_DAY * 14;
        long preFourteenEnd = endDate + DateUtils.MILLIS_PER_DAY * 14;

        long preTwentyNineStart = startDate + DateUtils.MILLIS_PER_DAY * 29;
        long preTwentyNineEnd = endDate + DateUtils.MILLIS_PER_DAY * 29;

        String matchZone = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        String group = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:1}}}";
        // 前一天新增用户数
        String matchRegisterTime = "{$match:{$and:[{register_time:{$gt:" + preOneStart + "}},{register_time:{$lt:"
                + preOneEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreOneNewUserNum", int.class, group, matchZone,
                matchRegisterTime);
        // 当天注册用户
        String lordIdsStr = queryNewLordIdsStr(startDate, endDate, zoneList);
        // 前一天存留数
        String matchOneLoginTime = "{$match:{$and:[{login_time:{$gt:" + preOneStart + "}},{login_time:{$lt:" + preOneEnd
                + "}}]}}";
        String matchLordsPreOnePersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreOnePersistence", int.class, group, matchZone,
                matchOneLoginTime, matchLordsPreOnePersistence);
        // 前两天新增用户数
        String matchPreTwoNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preTwoStart + "}},{register_time:{$lt:"
                + preTwoEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreTwoNewUserNum", int.class, group, matchZone,
                matchPreTwoNewUserNum);

        // 前两天存留数
        String matchTwoLoginTime = "{$match:{$and:[{login_time:{$gt:" + preTwoStart + "}},{login_time:{$lt:" + preTwoEnd
                + "}}]}}";
        String matchLordsPreTwoPersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreTwoPersistence", int.class, group, matchZone,
                matchTwoLoginTime, matchLordsPreTwoPersistence);
        // 前六天新增用户数
        String matchPreSixNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preSixStart + "}},{register_time:{$lt:"
                + preSixEnd + "}}]}}";
        getNum(startDate, tableData, "game_register", "setPreSixNewUserNum", int.class, group, matchZone,
                matchPreSixNewUserNum);
        // 前六天存留数
        String matchSixLoginTime = "{$match:{$and:[{login_time:{$gt:" + preSixStart + "}},{login_time:{$lt:" + preSixEnd
                + "}}]}}";
        String matchLordsPreSixPersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
        getNum(startDate, tableData, "game_login", "setPreSixPersistence", int.class, group, matchZone,
                matchSixLoginTime, matchLordsPreSixPersistence);
        if (flag) {
            // 前三天新增用户
            String matchPreThreeNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preThreeStart
                    + "}},{register_time:{$lt:" + preThreeEnd + "}}]}}";
            getNum(startDate, tableData, "game_register", "setPreThreeNewUserNum", int.class, group, matchZone,
                    matchPreThreeNewUserNum);
            // 前三天存留数
            String matchThreeLoginTime = "{$match:{$and:[{login_time:{$gt:" + preThreeStart + "}},{login_time:{$lt:"
                    + preThreeEnd + "}}]}}";
            String matchLordsPreThreePersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            getNum(startDate, tableData, "game_login", "setPreThreePersistence", int.class, group, matchZone,
                    matchThreeLoginTime, matchLordsPreThreePersistence);
            // 前四天新增用户
            String matchPreFourNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preFourStart
                    + "}},{register_time:{$lt:" + preFourEnd + "}}]}}";
            getNum(startDate, tableData, "game_register", "setPreFourNewUserNum", int.class, group, matchZone,
                    matchPreFourNewUserNum);
            // 前四天存留数
            String matchFourLoginTime = "{$match:{$and:[{login_time:{$gt:" + preFourStart + "}},{login_time:{$lt:"
                    + preFourEnd + "}}]}}";
            String matchLordsPreFourPersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            getNum(startDate, tableData, "game_login", "setPreFourPersistence", int.class, group, matchZone,
                    matchFourLoginTime, matchLordsPreFourPersistence);
            // 前五天新增用户
            String matchPreFiveNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preFiveStart
                    + "}},{register_time:{$lt:" + preFiveEnd + "}}]}}";
            getNum(startDate, tableData, "game_register", "setPreFiveNewUserNum", int.class, group, matchZone,
                    matchPreFiveNewUserNum);
            // 前五天存留数
            String matchFiveLoginTime = "{$match:{$and:[{login_time:{$gt:" + preFiveStart + "}},{login_time:{$lt:"
                    + preFiveEnd + "}}]}}";
            String matchLordsPreFivePersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            getNum(startDate, tableData, "game_login", "setPreFivePersistence", int.class, group, matchZone,
                    matchFiveLoginTime, matchLordsPreFivePersistence);

            // 前十四天新增用户
            String matchPreFourteenNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preFourteenStart
                    + "}},{register_time:{$lt:" + preFourteenEnd + "}}]}}";
            getNum(startDate, tableData, "game_register", "setPreFourteenNewUserNum", int.class, group, matchZone,
                    matchPreFourteenNewUserNum);
            // 前十四天存留数
            String matchFourteenLoginTime = "{$match:{$and:[{login_time:{$gt:" + preFourteenStart
                    + "}},{login_time:{$lt:" + preFourteenEnd + "}}]}}";
            String matchLordsPreFourteenPersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            getNum(startDate, tableData, "game_login", "setPreFourteenPersistence", int.class, group, matchZone,
                    matchFourteenLoginTime, matchLordsPreFourteenPersistence);
            // 前二十九天新增用户
            String matchPreTwentyNineNewUserNum = "{$match:{$and:[{register_time:{$gt:" + preTwentyNineStart
                    + "}},{register_time:{$lt:" + preTwentyNineEnd + "}}]}}";
            getNum(startDate, tableData, "game_register", "setPreTwentyNineNewUserNum", int.class, group, matchZone,
                    matchPreTwentyNineNewUserNum);
            // 前二十九天存留数
            String matchTwentyNineLoginTime = "{$match:{$and:[{login_time:{$gt:" + preTwentyNineStart
                    + "}},{login_time:{$lt:" + preTwentyNineEnd + "}}]}}";
            String matchLordsPreTwentyNinePersistence = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            getNum(startDate, tableData, "game_login", "setPreFourteenPersistence", int.class, group, matchZone,
                    matchTwentyNineLoginTime, matchLordsPreTwentyNinePersistence);
        }

    }

    /**
     * @Description:详细LTV数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    public List<ActiveBaseData> ltvSelect(String dateType, String dateStr, String[] zoneArr) {
        List<ActiveBaseData> tableData = new ArrayList<>();
        List<String> zoneList = new ArrayList<>();
        zoneList.addAll(Arrays.asList(zoneArr));// 要查询的区
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zoneArr.length; i++) {
            sb.append("'").append(zoneArr[i]).append("'");
            if (i != zoneArr.length - 1) {
                sb.append(",");
            }
        }
        String zoneStr = sb.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date calDate = cal.getTime();
        Calendar cur = Calendar.getInstance();
        String format = sdf.format(new Date());
        try {
            cur.setTime(sdf.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 日
        if (dateType.equals("day")) {
            ltvData(cal, zoneStr, zoneList, tableData, true);
            // 周
        } else if (dateType.equals("week")) {
            long endLong = cal.getTimeInMillis() + DateUtils.MILLIS_PER_DAY * 7;
            while (cal.getTimeInMillis() < endLong) {
                ltvData(cal, zoneStr, zoneList, tableData, true);
            }
            // 月
        } else {
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(cal.getTime());
            endCal.add(Calendar.MONTH, 1);
            long endLong = endCal.getTimeInMillis();
            while (cal.getTimeInMillis() < endLong) {
                ltvData(cal, zoneStr, zoneList, tableData, true);
            }
        }
        return tableData;

    }

    /**
     * @Description: 详细LTV 查询统计
     * @param cur
     * @param zoneStr
     * @param zoneList
     * @param tableData
     * @throws
     */
    private void ltvData(Calendar cal, String zoneStr, List<String> zoneList, List<ActiveBaseData> tableData,
            boolean flag) {
        long startDate = cal.getTime().getTime();// yyyy-MM-21 00:00:00
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endDate = cal.getTime().getTime();// yyyy-MM-22 00:00:00

        long preOneStart = startDate + DateUtils.MILLIS_PER_DAY;
        long preOneEnd = endDate + DateUtils.MILLIS_PER_DAY;

        long preTwoStart = startDate + DateUtils.MILLIS_PER_DAY * 2;
        long preTwoEnd = endDate + DateUtils.MILLIS_PER_DAY * 2;

        long preSixStart = startDate + DateUtils.MILLIS_PER_DAY * 6;
        long preSixEnd = endDate + DateUtils.MILLIS_PER_DAY * 6;

        long preThirteenStart = startDate + DateUtils.MILLIS_PER_DAY * 13;
        long preThirteenEnd = endDate + DateUtils.MILLIS_PER_DAY * 13;

        long preTwentyStart = startDate + DateUtils.MILLIS_PER_DAY * 20;
        long preTwentyEnd = endDate + DateUtils.MILLIS_PER_DAY * 20;

        long preTwentySevenStart = startDate + DateUtils.MILLIS_PER_DAY * 27;
        long preTwentySevenEnd = endDate + DateUtils.MILLIS_PER_DAY * 27;

        long preThirtyFourStart = startDate + DateUtils.MILLIS_PER_DAY * 34;
        long preThirtyFourEnd = endDate + DateUtils.MILLIS_PER_DAY * 34;

        long preFortyEightStart = startDate + DateUtils.MILLIS_PER_DAY * 34;
        long preFortyEightEnd = endDate + DateUtils.MILLIS_PER_DAY * 34;

        long preFiftyFiveStart = startDate + DateUtils.MILLIS_PER_DAY * 34;
        long preFiftyFiveEnd = endDate + DateUtils.MILLIS_PER_DAY * 34;

        String matchZoneStr = "{$match:{zone_id:{$in:[" + zoneStr + "]}}}";
        String group = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:1}}}";
        String groupPrice = "{$group:{_id:{zone_id:'$zone_id',package_id:'$package_id'},total:{$sum:'$price'}}}";

        // 新增用户数
        String matchPreOneNewUserNum = "{$match:{$and:[{register_time:{$gt:" + startDate + "}},{register_time:{$lt:"
                + endDate + "}}]}}";
        getNum(startDate, tableData, "game_register", "setNewUserNum", int.class, group, matchZoneStr,
                matchPreOneNewUserNum);

        // 当天注册用户
        String lordIdsStr = queryNewLordIdsStr(startDate, endDate, zoneList);

        // 查询当天注册的用户从当天到次日为止的付费总数
        String matchPreOneIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + preOneEnd
                + "}}]}}";
        String matchLordsPreOneIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreOneIncomeNum", double.class, groupPrice, matchZoneStr,
                matchPreOneIncomeNum, matchLordsPreOneIncomeNum);
        // 注册两天后的付费总数
        String matchPreTwoIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + preTwoEnd
                + "}}]}}";
        String matchLordsPreTwoIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreTwoIncomeNum", double.class, groupPrice, matchZoneStr,
                matchPreTwoIncomeNum, matchLordsPreTwoIncomeNum);
        // 注册六天后的付费总数
        String matchPreSixIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + preSixEnd
                + "}}]}}";
        String matchLordsPreSixIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
        incomeNum(startDate, tableData, "game_order", "setPreSixIncomeNum", double.class, groupPrice, matchZoneStr,
                matchPreSixIncomeNum, matchLordsPreSixIncomeNum);
        if (flag) {
            // 当天注册用户当天为止付费总数
            String matchPersentIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:" + endDate
                    + "}}]}}";
            String matchLordsPersentIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPersentIncomeNum", double.class, groupPrice, matchZoneStr,
                    matchPersentIncomeNum, matchLordsPersentIncomeNum);
            // 注册十三天内的用户付费总数
            String matchPreThirteenIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:"
                    + preThirteenEnd + "}}]}}";
            String matchLordsPreThirteenIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPreThirteenIncomeNum", double.class, groupPrice,
                    matchZoneStr, matchPreThirteenIncomeNum, matchLordsPreThirteenIncomeNum);
            // 注册二十天内的付费总数
            String matchPreTwentyIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:"
                    + preTwentyEnd + "}}]}}";
            String matchLordsPreTwentyIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPreTwentyIncomeNum", double.class, groupPrice,
                    matchZoneStr, matchPreTwentyIncomeNum, matchLordsPreTwentyIncomeNum);
            // 注册二十七天内的付费总数
            String matchPreTwentySevenIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:"
                    + preTwentySevenEnd + "}}]}}";
            String matchLordsPreTwentySevenIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPreTwentySevenIncomeNum", double.class, groupPrice,
                    matchZoneStr, matchPreTwentySevenIncomeNum, matchLordsPreTwentySevenIncomeNum);
            // 前三十四天注册的用户付费总数
            String matchPreThirtyFourIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:"
                    + preThirtyFourEnd + "}}]}}";
            String matchLordsPreThirtyFourIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPreThirtyFourIncomeNum", double.class, groupPrice,
                    matchZoneStr, matchPreThirtyFourIncomeNum, matchLordsPreThirtyFourIncomeNum);
            // 前四十八天注册的付费总数
            String matchPreFortyEightIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:"
                    + preFortyEightEnd + "}}]}}";
            String matchLordsPreFortyEightIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPreFortyEightIncomeNum", double.class, groupPrice,
                    matchZoneStr, matchPreFortyEightIncomeNum, matchLordsPreFortyEightIncomeNum);
            // 前五十六天注册的付费总数
            String matchPreFiftyFiveIncomeNum = "{$match:{$and:[{pay_time:{$gt:" + startDate + "}},{pay_time:{$lt:"
                    + preFiftyFiveEnd + "}}]}}";
            String matchLordsPreFiftyFiveIncomeNum = "{$match:{lord_id:{$in:[" + lordIdsStr + "]}}}";
            incomeNum(startDate, tableData, "game_order", "setPreFiftyFiveIncomeNum", double.class, groupPrice,
                    matchZoneStr, matchPreFiftyFiveIncomeNum, matchLordsPreFiftyFiveIncomeNum);
        }

    }

}
