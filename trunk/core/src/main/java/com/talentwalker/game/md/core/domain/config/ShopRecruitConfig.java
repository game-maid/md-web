/**
 * @Title: ShopRecruit.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月1日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.config;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: ShopRecruit
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月1日 上午11:10:33
 */
@Document(collection = "config_shop_recruit")
public class ShopRecruitConfig extends BaseDomain {
    private static final long serialVersionUID = -5930115477989768272L;
    /**
     * 名称
     */
    protected String name;
    /**
     * 描述
     */
    protected List<String> descList;
    /**
     * 描述对应的颜色
     */
    protected List<String> rgbList;
    /**
     * 出售货币类型
     */
    protected String cost;
    /**
     * 货币id（出售货币类型为道具时，此id必填）
     */
    @Transient
    protected String itemId;
    /**
     * 活动开始时间
     */
    @Field("start_time")
    protected long startTime;
    /**
     * 活动结束时间
     */
    @Field("end_time")
    protected long endTime;
    /**
     * 
     */
    @Transient
    protected String startDate;
    /**
     * 活动结束时间
     */
    @Transient
    protected String endDate;
    /**
     * 状态(true:开启，false：关闭)
     */
    protected boolean state;
    /**
     * 稀有获取概率模式（1平均型、2成长型）
     */
    @Field("proType")
    protected int proType;
    /**
     * 不重复次数
     */
    protected int norepeat;
    /**
     * 平均概率
     */
    protected double average;
    /**
     * 成长基数
     */
    protected double probability;
    /**
     * 概率成长
     */
    @Field("probability_up")
    protected double probabilityUp;
    /**
     * 保底间隔
     */
    @Field("safety_times")
    protected int safetyTimes;
    /**
     * 次数限制类型（0：不限次，1：每日限次，2：总限次）
     */
    protected int limitTimesType;
    /**
     * 限制次数
     */
    protected int limitTimes;
    /**
     * 是否单抽
     */
    protected boolean isOneRecruit;
    /**
     * 是否十连抽
     */
    protected boolean continuous;
    /**
     * 首抽折扣
     */
    protected boolean discount;
    /**
     * 单独第一次价格
     */
    @Field("alone_price_1")
    protected int alonePrice1;
    /**
     * 单独第二次价格
     */
    @Field("alone_price_2")
    protected int alonePrice2;
    /**
     * 单独正常价格
     */
    @Field("alone_price_3")
    protected int alonePrice3;
    /**
     * 十连第一次价格
     */
    @Field("continuous_price_1")
    protected int continuousPrice1;
    /**
     * 十连第二次价格
     */
    @Field("continuous_price_2")
    protected int continuousPrice2;
    /**
     * 十连正常价格
     */
    @Field("continuous_price_3")
    protected int continuousPrice3;
    /**
     *生效服务器
     */
    @Field("zone_list")
    protected List<String> zoneList;
    /**
     * A池数据
     */
    @Field("a_data")
    protected List<Map<String, Object>> aData;
    /**
     * B池数据
     */
    @Field("b_data")
    protected List<Map<String, Object>> bData;
    /**
     * 初始稀有卡牌数量
     */
    protected int startA;
    /**
     * 初始普通卡牌数
     */
    protected int startB;
    /**
     * 每everyTimesA次抽卡 增加addA张卡牌
     */
    protected int everyTimesA;
    protected int addA;
    /**
     * 每everyTimesB次抽卡 增加addB张卡牌
     */
    protected int everyTimesB;
    protected int addB;
    /**
     * 图片地址
     */
    protected String src;
    /**
     * A池数据
     */
    @Transient
    protected String data1;
    /**
     * B池数据
     */
    @Transient
    protected String data2;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return descList
     */
    public List<String> getDescList() {
        return descList;
    }

    /**
     * @param descList 要设置的 descList
     */
    public void setDescList(List<String> descList) {
        this.descList = descList;
    }

    /**
     * @return rgbList
     */
    public List<String> getRgbList() {
        return rgbList;
    }

    /**
     * @param rgbList 要设置的 rgbList
     */
    public void setRgbList(List<String> rgbList) {
        this.rgbList = rgbList;
    }

    /**
     * @return cost
     */
    public String getCost() {
        return cost;
    }

    /**
     * @param cost 要设置的 cost
     */
    public void setCost(String cost) {
        this.cost = cost;
    }

    /**
     * @return itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId 要设置的 itemId
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return proType
     */
    public int getProType() {
        return proType;
    }

    /**
     * @param proType 要设置的 proType
     */
    public void setProType(int proType) {
        this.proType = proType;
    }

    /**
     * @return average
     */
    public double getAverage() {
        return average;
    }

    /**
     * @param average 要设置的 average
     */
    public void setAverage(double average) {
        this.average = average;
    }

    /**
     * @return probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * @param probability 要设置的 probability
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

    /**
     * @return probabilityUp
     */
    public double getProbabilityUp() {
        return probabilityUp;
    }

    /**
     * @param probabilityUp 要设置的 probabilityUp
     */
    public void setProbabilityUp(double probabilityUp) {
        this.probabilityUp = probabilityUp;
    }

    /**
     * @return safetyTimes
     */
    public int getSafetyTimes() {
        return safetyTimes;
    }

    /**
     * @param safetyTimes 要设置的 safetyTimes
     */
    public void setSafetyTimes(int safetyTimes) {
        this.safetyTimes = safetyTimes;
    }

    /**
     * @return norepeat
     */
    public int getNorepeat() {
        return norepeat;
    }

    /**
     * @param norepeat 要设置的 norepeat
     */
    public void setNorepeat(int norepeat) {
        this.norepeat = norepeat;
    }

    /**
     * @return continuous
     */
    public boolean getContinuous() {
        return continuous;
    }

    /**
     * @param continuous 要设置的 continuous
     */
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    /**
     * @return discount
     */
    public boolean getDiscount() {
        return discount;
    }

    /**
     * @param discount 要设置的 discount
     */
    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    /**
     * @return alonePrice1
     */
    public int getAlonePrice1() {
        return alonePrice1;
    }

    /**
     * @param alonePrice1 要设置的 alonePrice1
     */
    public void setAlonePrice1(int alonePrice1) {
        this.alonePrice1 = alonePrice1;
    }

    /**
     * @return alonePrice2
     */
    public int getAlonePrice2() {
        return alonePrice2;
    }

    /**
     * @param alonePrice2 要设置的 alonePrice2
     */
    public void setAlonePrice2(int alonePrice2) {
        this.alonePrice2 = alonePrice2;
    }

    /**
     * @return continuousPrice1
     */
    public int getContinuousPrice1() {
        return continuousPrice1;
    }

    /**
     * @param continuousPrice1 要设置的 continuousPrice1
     */
    public void setContinuousPrice1(int continuousPrice1) {
        this.continuousPrice1 = continuousPrice1;
    }

    /**
     * @return continuousPrice2
     */
    public int getContinuousPrice2() {
        return continuousPrice2;
    }

    /**
     * @param continuousPrice2 要设置的 continuousPrice2
     */
    public void setContinuousPrice2(int continuousPrice2) {
        this.continuousPrice2 = continuousPrice2;
    }

    /**
     * @return zoneList
     */
    public List<String> getZoneList() {
        return zoneList;
    }

    /**
     * @param zoneList 要设置的 zoneList
     */
    public void setZoneList(List<String> zoneList) {
        this.zoneList = zoneList;
    }

    /**
     * @return data1
     */
    public String getData1() {
        return data1;
    }

    /**
     * @param data1 要设置的 data1
     */
    public void setData1(String data1) {
        this.data1 = data1;
    }

    /**
     * @return data2
     */
    public String getData2() {
        return data2;
    }

    /**
     * @param data2 要设置的 data2
     */
    public void setData2(String data2) {
        this.data2 = data2;
    }

    /**
     * @return startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime 要设置的 startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @param endTime 要设置的 endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate 要设置的 startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate 要设置的 endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return aData
     */
    public List<Map<String, Object>> getaData() {
        return aData;
    }

    /**
     * @param aData 要设置的 aData
     */
    public void setaData(List<Map<String, Object>> aData) {
        this.aData = aData;
    }

    /**
     * @return bData
     */
    public List<Map<String, Object>> getbData() {
        return bData;
    }

    /**
     * @param bData 要设置的 bData
     */
    public void setbData(List<Map<String, Object>> bData) {
        this.bData = bData;
    }

    /**
     * @return alonePrice3
     */
    public int getAlonePrice3() {
        return alonePrice3;
    }

    /**
     * @param alonePrice3 要设置的 alonePrice3
     */
    public void setAlonePrice3(int alonePrice3) {
        this.alonePrice3 = alonePrice3;
    }

    /**
     * @return continuousPrice3
     */
    public int getContinuousPrice3() {
        return continuousPrice3;
    }

    /**
     * @param continuousPrice3 要设置的 continuousPrice3
     */
    public void setContinuousPrice3(int continuousPrice3) {
        this.continuousPrice3 = continuousPrice3;
    }

    /**
     * @return state
     */
    public boolean getState() {
        return state;
    }

    /**
     * @param state 要设置的 state
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * @return limitTimesType
     */
    public int getLimitTimesType() {
        return limitTimesType;
    }

    /**
     * @param limitTimesType 要设置的 limitTimesType
     */
    public void setLimitTimesType(int limitTimesType) {
        this.limitTimesType = limitTimesType;
    }

    /**
     * @return limitTimes
     */
    public int getLimitTimes() {
        return limitTimes;
    }

    /**
     * @param limitTimes 要设置的 limitTimes
     */
    public void setLimitTimes(int limitTimes) {
        this.limitTimes = limitTimes;
    }

    /**
     * @return isOneRecruit
     */
    public boolean getIsOneRecruit() {
        return isOneRecruit;
    }

    /**
     * @param isOneRecruit 要设置的 isOneRecruit
     */
    public void setIsOneRecruit(boolean isOneRecruit) {
        this.isOneRecruit = isOneRecruit;
    }

    /**
     * @return startA
     */
    public int getStartA() {
        return startA;
    }

    /**
     * @param startA 要设置的 startA
     */
    public void setStartA(int startA) {
        this.startA = startA;
    }

    /**
     * @return startB
     */
    public int getStartB() {
        return startB;
    }

    /**
     * @param startB 要设置的 startB
     */
    public void setStartB(int startB) {
        this.startB = startB;
    }

    /**
     * @return everyTimesA
     */
    public int getEveryTimesA() {
        return everyTimesA;
    }

    /**
     * @param everyTimesA 要设置的 everyTimesA
     */
    public void setEveryTimesA(int everyTimesA) {
        this.everyTimesA = everyTimesA;
    }

    /**
     * @return addA
     */
    public int getAddA() {
        return addA;
    }

    /**
     * @param addA 要设置的 addA
     */
    public void setAddA(int addA) {
        this.addA = addA;
    }

    /**
     * @return everyTimesB
     */
    public int getEveryTimesB() {
        return everyTimesB;
    }

    /**
     * @param everyTimesB 要设置的 everyTimesB
     */
    public void setEveryTimesB(int everyTimesB) {
        this.everyTimesB = everyTimesB;
    }

    /**
     * @return addB
     */
    public int getAddB() {
        return addB;
    }

    /**
     * @param addB 要设置的 addB
     */
    public void setAddB(int addB) {
        this.addB = addB;
    }

    /**
     * @return src
     */
    public String getSrc() {
        return src;
    }

    /**
     * @param src 要设置的 src
     */
    public void setSrc(String src) {
        this.src = src;
    }

}
