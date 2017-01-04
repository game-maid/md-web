/**
 * @Title: DuelStore.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月18日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: DuelStore
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月18日 下午2:15:35
 */

public class DuelStore {
    /**
     * 刷新次数
     */
    protected int times;
    /**
     * 上一次自动刷新时间
     */
    protected long automaticTime;
    /**
     * 下一次自动刷新时间
     */
    protected long nextRefreshTime;
    /**
     * 手动刷新时间
     */
    protected long refreshTime;
    /**
     * 商品
     */
    List<List<Map<String, Object>>> goods;

    /**
     * @return times
     */
    public int getTimes() {
        return times;
    }

    /**
     * @param times 要设置的 times
     */
    public void setTimes(int times) {
        this.times = times;
    }

    /**
     * @return automaticTime
     */
    public long getAutomaticTime() {
        return automaticTime;
    }

    /**
     * @param automaticTime 要设置的 automaticTime
     */
    public void setAutomaticTime(long automaticTime) {
        this.automaticTime = automaticTime;
    }

    /**
     * @return refreshTime
     */
    public long getRefreshTime() {
        return refreshTime;
    }

    /**
     * @param refreshTime 要设置的 refreshTime
     */
    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    /**
     * @return goods
     */
    public List<List<Map<String, Object>>> getGoods() {
        return goods;
    }

    /**
     * @param goods 要设置的 goods
     */
    public void setGoods(List<List<Map<String, Object>>> goods) {
        this.goods = goods;
    }

    /**
     * @return nextRefreshTime
     */
    public long getNextRefreshTime() {
        return nextRefreshTime;
    }

    /**
     * @param nextRefreshTime 要设置的 nextRefreshTime
     */
    public void setNextRefreshTime(long nextRefreshTime) {
        this.nextRefreshTime = nextRefreshTime;
    }

}
