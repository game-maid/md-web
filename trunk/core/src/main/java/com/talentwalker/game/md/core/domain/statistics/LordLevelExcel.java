/**
 * @Title: LordLevelExcel.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月27日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: LordLevelExcel
 * @Description: Excel 文件导出
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月27日 上午11:16:28
 */

public class LordLevelExcel {
    /**
    * 行号
    */
    private int index;
    private int level;
    private int num;
    private double proportion;
    private String lordId;
    private String lordName;

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
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level 要设置的 level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num 要设置的 num
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return proportion
     */
    public double getProportion() {
        return proportion;
    }

    /**
     * @param proportion 要设置的 proportion
     */
    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    /**
     * @return lordId
     */
    public String getLordId() {
        return lordId;
    }

    /**
     * @param lordId 要设置的 lordId
     */
    public void setLordId(String lordId) {
        this.lordId = lordId;
    }

    /**
     * @return lordName
     */
    public String getLordName() {
        return lordName;
    }

    /**
     * @param lordName 要设置的 lordName
     */
    public void setLordName(String lordName) {
        this.lordName = lordName;
    }

}
