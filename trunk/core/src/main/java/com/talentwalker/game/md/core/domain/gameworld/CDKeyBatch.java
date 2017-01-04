
package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Batch
 * @Description: 兑换码的批次
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月28日 下午7:38:57
 */
@Document(collection = "cd_key_batch")
public class CDKeyBatch extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 4812729715015737308L;

    /**
     * 同一批次最大数量10000
     */
    public static final int LIMIT_NUM = 10000;

    /**
     * 批次名称
     */
    protected String name;

    /**
     * 包ids
     */
    @Field("package_ids")
    protected List<String> packageIds;
    /**
     * 区服
     */
    @Field("zone_ids")
    protected List<String> zoneIds;
    /**
     * 开始时间
     */
    @Field("start_time")
    protected long startTime;
    @Transient
    protected String startDate;
    /**
     * 结束时间
     */
    @Field("end_time")
    protected long endTime;
    @Transient
    protected String endDate;
    /**
     * 奖励品级
     */
    protected int rank;
    /**
     * 等级限制，最低等级
     */
    protected int level;

    /***************邮件信息*******************/
    /**
     * 发送人
     */
    protected String sender;
    /**
     * 发送头像
     */
    protected String senderHeadIcon;
    /**
     * 有效天数
     */
    protected int validDay;

    /**
    * 标题
    */
    protected String title;
    /**
     * 详细
     */
    protected String content;
    /**
     * 奖励:map<String,Integer> 道具Id,道具数量
     */
    protected Map<String, Integer> reward;
    /**
     * 奖励
     */
    @Transient
    protected String rewards;
    /**
     * 数量
     */
    @Transient
    protected int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPackageIds() {
        return packageIds;
    }

    public void setPackageIds(List<String> packageIds) {
        this.packageIds = packageIds;
    }

    public List<String> getZoneIds() {
        return zoneIds;
    }

    public void setZoneIds(List<String> zoneIds) {
        this.zoneIds = zoneIds;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<String, Integer> getReward() {
        return reward;
    }

    public void setReward(Map<String, Integer> reward) {
        this.reward = reward;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderHeadIcon() {
        return senderHeadIcon;
    }

    public void setSenderHeadIcon(String senderHeadIcon) {
        this.senderHeadIcon = senderHeadIcon;
    }

    public int getValidDay() {
        return validDay;
    }

    public void setValidDay(int validDay) {
        this.validDay = validDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
