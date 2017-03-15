/**
 * @Title: GoldDiamondStatisticsExcel.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月7日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: GoldDiamondStatisticsExcel
 * @Description: 钻石金币统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月7日 下午7:38:35
 */

public class ItemExpendExcel {
    /**
     * 行号
     */
    private int index;
    /**
     * 功能名称
     */
    private String functionName;
    /**
     * 道具数量
     */
    private int itemNum;
    /**
     * 消费次数
     */
    private int payTimes;
    /**
     * 消费人数
     */
    private int payerNum;

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
     * @return itemNum
     */
    public int getItemNum() {
        return itemNum;
    }

    /**
     * @param itemNum 要设置的 itemNum
     */
    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
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

}
