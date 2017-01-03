/**
 * @Title: SoulShop.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月2日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: SoulShop
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月2日 上午11:23:30
 */
@Document(collection = "game_soul_store")
public class SoulStore extends BaseDomain {
    private static final long serialVersionUID = 1448942193394712548L;
    /**
     * 刷新次数
     */
    private int times;
    /**
     * 上一次自动刷新时间
     */
    private long automaticTime;
    /**
     * 下一次自动刷新时间
     */
    private long nextRefreshTime;
    /**
     * 手动刷新时间
     */
    private long refreshTime;
    /**
     * 商品
     */
    private List<Map<String, Object>> goods;

    private int soulGold;

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
    public List<Map<String, Object>> getGoods() {
        return goods;
    }

    /**
     * @param goods 要设置的 goods
     */
    public void setGoods(List<Map<String, Object>> goods) {
        this.goods = goods;
    }

    /**
     * @return soulGold
     */
    public int getSoulGold() {
        return soulGold;
    }

    /**
     * @param soulGold 要设置的 soulGold
     */
    public void setSoulGold(int soulGold) {
        this.soulGold = soulGold;
    }
}
