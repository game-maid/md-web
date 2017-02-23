/**
 * @Title: LordLevel.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月22日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

import java.util.List;

import com.talentwalker.game.md.core.domain.gameworld.Lordname;

/**
 * @ClassName: LordLevel
 * @Description: 玩家等级统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月22日 下午6:14:36
 */

public class LordLevel {
    /**
     * 等级
     */
    private int level;
    /**
     * 数量
     */
    private int num;
    /**
     * 占比
     */
    private double proportion;
    /**
     * 玩家信息
     */
    private List<Lordname> lordList;

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
     * @return lordList
     */
    public List<Lordname> getLordList() {
        return lordList;
    }

    /**
     * @param lordList 要设置的 lordList
     */
    public void setLordList(List<Lordname> lordList) {
        this.lordList = lordList;
    }

}
