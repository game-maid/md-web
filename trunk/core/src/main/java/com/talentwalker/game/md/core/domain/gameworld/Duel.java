/**
 * @Title: Duel.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月6日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Duel
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月6日 下午3:20:30
 */
@Document(collection = "game_duel")
public class Duel extends BaseDomain {
    private static final long serialVersionUID = -8131554006821814419L;
    /**
     * 挑战次数
     */
    protected int times;
    /**
     * 剩余挑战次数
     */
    @Field("surplus_times")
    protected int surplusTimes;
    /**
     * 购买次数
     */
    @Field("buy_time")
    protected int buyTimes;
    /**
     * pvp币
     */
    @Field("pvp_gold")
    protected int pvpGold;
    /**
     * 最高排名
     */
    protected int hrank;
    /**
     * 当前排名
     */
    @Transient
    protected int rank;
    /**
     * 排名列表
     */
    @Transient
    protected List<Map<String, Object>> rankList;
    /**
     * 防守阵容
     */
    @Field("form_defend")
    protected List<FormHold> formDefend;
    /**
     * 最后一次进入战斗时间
     */
    @Field("last_time")
    protected long lastTime;
    /**
     * 商店
     */
    protected DuelStore store;
    /**
     * 奖励缓存
     */
    protected List<Map<String, Integer>> award;

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
     * @return surplusTimes
     */
    public int getSurplusTimes() {
        return surplusTimes;
    }

    /**
     * @param surplusTimes 要设置的 surplusTimes
     */
    public void setSurplusTimes(int surplusTimes) {
        this.surplusTimes = surplusTimes;
    }

    /**
     * @return buyTimes
     */
    public int getBuyTimes() {
        return buyTimes;
    }

    /**
     * @param buyTimes 要设置的 buyTimes
     */
    public void setBuyTimes(int buyTimes) {
        this.buyTimes = buyTimes;
    }

    /**
     * @return pvpGold
     */
    public int getPvpGold() {
        return pvpGold;
    }

    /**
     * @param pvpGold 要设置的 pvpGold
     */
    public void setPvpGold(int pvpGold) {
        this.pvpGold = pvpGold;
    }

    /**
     * @return hrank
     */
    public int getHrank() {
        return hrank;
    }

    /**
     * @param hrank 要设置的 hrank
     */
    public void setHrank(int hrank) {
        this.hrank = hrank;
    }

    /**
     * @return rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank 要设置的 rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return rankList
     */
    public List<Map<String, Object>> getRankList() {
        return rankList;
    }

    /**
     * @param rankList 要设置的 rankList
     */
    public void setRankList(List<Map<String, Object>> rankList) {
        this.rankList = rankList;
    }

    /**
     * @return formDefend
     */
    public List<FormHold> getFormDefend() {
        return formDefend;
    }

    /**
     * @param formDefend 要设置的 formDefend
     */
    public void setFormDefend(List<FormHold> formDefend) {
        this.formDefend = formDefend;
    }

    /**
     * @return lastTime
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * @param lastTime 要设置的 lastTime
     */
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @return store
     */
    public DuelStore getStore() {
        if (store != null && !DateUtils.isSameDay(new Date(store.getRefreshTime()), new Date())) {
            store.setTimes(0);
        }
        return store;
    }

    /**
     * @param store 要设置的 store
     */
    public void setStore(DuelStore store) {
        this.store = store;
    }

    /**
     * @return award
     */
    public List<Map<String, Integer>> getAward() {
        return award;
    }

    /**
     * @param award 要设置的 award
     */
    public void setAward(List<Map<String, Integer>> award) {
        this.award = award;
    }

}
