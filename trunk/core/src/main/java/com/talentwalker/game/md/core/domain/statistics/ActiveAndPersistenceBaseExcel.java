
package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: ActiveAndPersistenceExcel
 * @Description: 活跃存留 基础数据Excel
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月28日 下午1:45:50
 */
public class ActiveAndPersistenceBaseExcel {
    /**
     * 行号
     */
    private int index;
    private String date;
    private String zoneId;
    private String packageId;

    /**
     * 活跃数
     */
    private Integer activeNum;
    /**
     * 新增用户数
     */
    private Integer newUserNum;
    /**
     * 新增付费用户
     */
    private Integer newPayerNum;
    /**
     * 付费人数
     */
    private Integer payerNum;
    /**
     * 收入金额
     */
    private Double incomeNum;
    /**
     * 收入次数
     */
    private Integer incomeTimes;
    /**
     * 新增付费率
     */
    private String newPayRate;
    /**
     * 活跃付费率
     */
    private String activePayRate;
    /**
     * ARPU
     */
    private Double arpu;
    /**
     * ARPPU
     */
    private Double arppu;
    /**
     * 次日存留率
     */
    private String secondPersistenceRate;
    /**
     * 三日存留率
     */
    private String thirdlyPersistenceRate;
    /**
     * 七日存留率
     */
    private String seventhPersistenceRate;
    /**
     * 次日LTV
     */
    private String secondLtv;
    /**
     * 三日LTV
     */
    private String thirdlyLtv;
    /**
     * 七日LTV
     */
    private String seventhLtv;

    /**
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index 要设置的 index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date 要设置的 date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return zoneId
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * @param zoneId 要设置的 zoneId
     */
    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * @return packageId
     */
    public String getPackageId() {
        return packageId;
    }

    /**
     * @param packageId 要设置的 packageId
     */
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    /**
     * @return activeNum
     */
    public Integer getActiveNum() {
        return activeNum;
    }

    /**
     * @param activeNum 要设置的 activeNum
     */
    public void setActiveNum(Integer activeNum) {
        this.activeNum = activeNum;
    }

    /**
     * @return newUserNum
     */
    public Integer getNewUserNum() {
        return newUserNum;
    }

    /**
     * @param newUserNum 要设置的 newUserNum
     */
    public void setNewUserNum(Integer newUserNum) {
        this.newUserNum = newUserNum;
    }

    /**
     * @return newPayerNum
     */
    public Integer getNewPayerNum() {
        return newPayerNum;
    }

    /**
     * @param newPayerNum 要设置的 newPayerNum
     */
    public void setNewPayerNum(Integer newPayerNum) {
        this.newPayerNum = newPayerNum;
    }

    /**
     * @return payerNum
     */
    public Integer getPayerNum() {
        return payerNum;
    }

    /**
     * @param payerNum 要设置的 payerNum
     */
    public void setPayerNum(Integer payerNum) {
        this.payerNum = payerNum;
    }

    /**
     * @return incomeNum
     */
    public Double getIncomeNum() {
        return incomeNum;
    }

    /**
     * @param incomeNum 要设置的 incomeNum
     */
    public void setIncomeNum(Double incomeNum) {
        this.incomeNum = incomeNum;
    }

    /**
     * @return incomeTimes
     */
    public Integer getIncomeTimes() {
        return incomeTimes;
    }

    /**
     * @param incomeTimes 要设置的 incomeTimes
     */
    public void setIncomeTimes(Integer incomeTimes) {
        this.incomeTimes = incomeTimes;
    }

    /**
     * @return newPayRate
     */
    public String getNewPayRate() {
        return newPayRate;
    }

    /**
     * @param newPayRate 要设置的 newPayRate
     */
    public void setNewPayRate(String newPayRate) {
        this.newPayRate = newPayRate;
    }

    /**
     * @return activePayRate
     */
    public String getActivePayRate() {
        return activePayRate;
    }

    /**
     * @param activePayRate 要设置的 activePayRate
     */
    public void setActivePayRate(String activePayRate) {
        this.activePayRate = activePayRate;
    }

    /**
     * @return arpu
     */
    public Double getArpu() {
        return arpu;
    }

    /**
     * @param arpu 要设置的 arpu
     */
    public void setArpu(Double arpu) {
        this.arpu = arpu;
    }

    /**
     * @return arppu
     */
    public Double getArppu() {
        return arppu;
    }

    /**
     * @param arppu 要设置的 arppu
     */
    public void setArppu(Double arppu) {
        this.arppu = arppu;
    }

    /**
     * @return secondPersistenceRate
     */
    public String getSecondPersistenceRate() {
        return secondPersistenceRate;
    }

    /**
     * @param secondPersistenceRate 要设置的 secondPersistenceRate
     */
    public void setSecondPersistenceRate(String secondPersistenceRate) {
        this.secondPersistenceRate = secondPersistenceRate;
    }

    /**
     * @return thirdlyPersistenceRate
     */
    public String getThirdlyPersistenceRate() {
        return thirdlyPersistenceRate;
    }

    /**
     * @param thirdlyPersistenceRate 要设置的 thirdlyPersistenceRate
     */
    public void setThirdlyPersistenceRate(String thirdlyPersistenceRate) {
        this.thirdlyPersistenceRate = thirdlyPersistenceRate;
    }

    /**
     * @return seventhPersistenceRate
     */
    public String getSeventhPersistenceRate() {
        return seventhPersistenceRate;
    }

    /**
     * @param seventhPersistenceRate 要设置的 seventhPersistenceRate
     */
    public void setSeventhPersistenceRate(String seventhPersistenceRate) {
        this.seventhPersistenceRate = seventhPersistenceRate;
    }

    /**
     * @return secondLtv
     */
    public String getSecondLtv() {
        return secondLtv;
    }

    /**
     * @param secondLtv 要设置的 secondLtv
     */
    public void setSecondLtv(String secondLtv) {
        this.secondLtv = secondLtv;
    }

    /**
     * @return thirdlyLtv
     */
    public String getThirdlyLtv() {
        return thirdlyLtv;
    }

    /**
     * @param thirdlyLtv 要设置的 thirdlyLtv
     */
    public void setThirdlyLtv(String thirdlyLtv) {
        this.thirdlyLtv = thirdlyLtv;
    }

    /**
     * @return seventhLtv
     */
    public String getSeventhLtv() {
        return seventhLtv;
    }

    /**
     * @param seventhLtv 要设置的 seventhLtv
     */
    public void setSeventhLtv(String seventhLtv) {
        this.seventhLtv = seventhLtv;
    }

}
