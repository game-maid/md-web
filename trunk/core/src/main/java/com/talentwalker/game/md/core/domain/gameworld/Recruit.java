/**
 * @Title: Recruit.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月4日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.talentwalker.game.md.core.domain.BaseDomain;
import com.talentwalker.game.md.core.domain.config.ShopRecruitConfig;

/**
 * @ClassName: Recruit
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月4日 上午10:08:41
 */

public class Recruit extends BaseDomain {
    private static final long serialVersionUID = 8893675373611708397L;
    /**
     * 招募总次数
     */
    protected int times;
    /**
     * 单抽次数
     */
    protected int oneTimes;
    /**
     * 十连抽次数
     */
    protected int tenTimes;
    /**
     * 招募类型1：普通，2：触发，3活动,4新手池
     */
    protected int type;
    /**
     * 抽中稀有池（A池）次数
     */
    protected int aTimes;
    /**
     * 抽中稀有池（A池）概率
     */
    protected double probability;
    /**
     * 稀有不重复数组（数组内heroId不能被招募）
     */
    protected List<String> oldHeros;
    /**
     * 触发时间
     */
    protected long triggeringTime;
    /**
     * 配置
     */
    @Transient
    protected ShopRecruitConfig config;

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

    public int getOneTimes() {
        return oneTimes;
    }

    public void setOneTimes(int oneTimes) {
        this.oneTimes = oneTimes;
    }

    /**
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type 要设置的 type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return aTimes
     */
    public int getaTimes() {
        return aTimes;
    }

    /**
     * @param aTimes 要设置的 aTimes
     */
    public void setaTimes(int aTimes) {
        this.aTimes = aTimes;
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
     * @return oldHeros
     */
    public List<String> getOldHeros() {
        return oldHeros;
    }

    /**
     * @param oldHeros 要设置的 oldHeros
     */
    public void setOldHeros(List<String> oldHeros) {
        this.oldHeros = oldHeros;
    }

    /**
     * @return triggeringTime
     */
    public long getTriggeringTime() {
        return triggeringTime;
    }

    /**
     * @param triggeringTime 要设置的 triggeringTime
     */
    public void setTriggeringTime(long triggeringTime) {
        this.triggeringTime = triggeringTime;
    }

    /**
     * @return config
     */
    public ShopRecruitConfig getConfig() {
        return config;
    }

    /**
     * @param config 要设置的 config
     */
    public void setConfig(ShopRecruitConfig config) {
        this.config = config;
    }

    /**
     * @return tenTimes
     */
    public int getTenTimes() {
        return tenTimes;
    }

    /**
     * @param tenTimes 要设置的 tenTimes
     */
    public void setTenTimes(int tenTimes) {
        this.tenTimes = tenTimes;
    }

}
