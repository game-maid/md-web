/**
 * @Title: DiamondStatisticsExcel.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月13日  张福涛
 */

package GoldDiamondStatisticsExcel;

/**
 * @ClassName: DiamondStatisticsExcel
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月13日 下午1:46:03
 */

public class DiamondStatisticsExcel {
    /**
     * 行号
     */
    private int index;
    /**
     * 功能名称
     */
    private String functionName;
    /**
     * 消费次数
     */
    private int payTimes;
    /**
     * 消费人数
     */
    private int payerNum;
    /**
     * 收费钻石数量
     */
    private int payNum;
    /**
     * 免费钻石数量
     */
    private int freeNum;

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
     * @return functionName
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * @param functionName 要设置的 functionName
     */
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    /**
     * @return payTimes
     */
    public int getPayTimes() {
        return payTimes;
    }

    /**
     * @param payTimes 要设置的 payTimes
     */
    public void setPayTimes(int payTimes) {
        this.payTimes = payTimes;
    }

    /**
     * @return payerNum
     */
    public int getPayerNum() {
        return payerNum;
    }

    /**
     * @param payerNum 要设置的 payerNum
     */
    public void setPayerNum(int payerNum) {
        this.payerNum = payerNum;
    }

    /**
     * @return payNum
     */
    public int getPayNum() {
        return payNum;
    }

    /**
     * @param payNum 要设置的 payNum
     */
    public void setPayNum(int payNum) {
        this.payNum = payNum;
    }

    /**
     * @return freeNum
     */
    public int getFreeNum() {
        return freeNum;
    }

    /**
     * @param freeNum 要设置的 freeNum
     */
    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

}
