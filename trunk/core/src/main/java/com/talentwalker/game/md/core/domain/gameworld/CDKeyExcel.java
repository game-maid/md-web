
package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;

/**
 * @ClassName: CDKeyExcel
 * @Description: 兑换码，导出excel文件
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月1日 上午10:32:09
 */
public class CDKeyExcel {
    /**
     * 行号
     */
    private int index;
    /**
     * 兑换码
     */
    private String id;
    /**
     * 批次名称
     */
    private String name;
    /**
     * 使用等级
     */
    private int level;
    /**
     * 奖励品级
     */
    private int rank;
    /**
     * 状态
     */
    private String status;
    /**
     * 生效时间
     */
    private Date startTime;
    /**
     * 过期时间
     */
    private Date endTime;
    /**
     * 使用者Id
     */
    private String userId;
    /**
     * 使用时间
     */
    private Date useTime;
    /**
     * 区服
     */
    private String zoneIds;
    /**
     * 包
     */
    private String packageIds;
    /**
     * 奖励配方
     */
    private String reward;

    public CDKeyExcel(int index, String id, String name, int level, int rank, String status, Date startTime,
            Date endTime, String userId, Date useTime, String zoneIds, String packageIds, String reward) {
        super();
        this.index = index;
        this.id = id;
        this.name = name;
        this.level = level;
        this.rank = rank;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.useTime = useTime;
        this.zoneIds = zoneIds;
        this.packageIds = packageIds;
        this.reward = reward;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public String getZoneIds() {
        return zoneIds;
    }

    public void setZoneIds(String zoneIds) {
        this.zoneIds = zoneIds;
    }

    public String getPackageIds() {
        return packageIds;
    }

    public void setPackageIds(String packageIds) {
        this.packageIds = packageIds;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

}
