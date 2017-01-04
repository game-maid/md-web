/**
 * @Title: StageComposite.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月7日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: StageComposite
 * @Description: 关卡数据实体
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月7日 下午5:42:09
 */

@Document(collection = "game_stage")
public class StageComposite extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    @Transient
    private String current;

    /**
     * 关卡记录
     */
    private Map<String, Stage> stages;

    /**
     * 星级奖励记录
     */
    private Map<String, Map<String, StageReward>> rewards;

    /**
     * 物品掉落累计概率
     */
    @JsonIgnore
    private Map<String, Double> loot = new HashMap<String, Double>();
    /**
     * 今日刷新次数
     */
    private int refresh;

    /**
     * 上次购买次数时间戳
     */
    private long refreshTime;

    /**
     * @return stages
     */
    public Map<String, Stage> getStages() {
        return stages;
    }

    /**
     * @param stages 要设置的 stages
     */
    public void setStages(Map<String, Stage> stages) {
        this.stages = stages;
    }

    /**
     * @return rewards
     */
    public Map<String, Map<String, StageReward>> getRewards() {
        return rewards;
    }

    /**
     * @param rewards 要设置的 rewards
     */
    public void setRewards(Map<String, Map<String, StageReward>> rewards) {
        this.rewards = rewards;
    }

    /**
     * @return loot
     */
    public Map<String, Double> getLoot() {
        return loot;
    }

    /**
     * @param loot 要设置的 loot
     */
    public void setLoot(Map<String, Double> loot) {
        this.loot = loot;
    }

    public Stage getStage(String stageId) {
        Stage stage = this.stages.get(stageId);
        if (stage == null) {
            stage = new Stage();
        }
        // 重置挑战次数
        if (!DateUtils.isSameDay(new Date(stage.getLastTime()), new Date())) {
            stage.setTimes(0);
            stage.setLastTime(System.currentTimeMillis());
        }
        return stage;
    }

    /**
     * @return current
     */
    public String getCurrent() {
        return current;
    }

    /**
     * @param current 要设置的 current
     */
    public void setCurrent(String current) {
        this.current = current;
    }

    /**
     * @return refresh
     */
    public int getRefresh() {
        return refresh;
    }

    /**
     * @param refresh 要设置的 refresh
     */
    public void setRefresh(int refresh) {
        this.refresh = refresh;
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

}
