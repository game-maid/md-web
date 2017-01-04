/**
 * @Title: LeagueLord.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: LeagueLord
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 上午11:26:38
 */
@Document(collection = "game_league_lord")
public class LeagueLord extends BaseDomain {

    private static final long serialVersionUID = 2941711857555233851L;
    /**
     * 个人资金
     */
    protected int fund;
    /**
     * 职务 1.盟主。2.副盟主。3.精英成员。4.见习成员
     */
    protected int duty;
    /**
     * 参与联盟时间
     */
    @Field("join_time")
    protected long joinTime;
    /**
     * 膜拜
     */
    protected Map<String, Object> worship;
    /**
     * 被膜拜
     */
    protected Map<String, Object> worshiped;
    /**
     * 每天贡献记录
     */
    @Field("every_donate")
    protected Map<String, Integer> everyDonate;
    /**
     * 退出联盟时间
     */
    @Field("exit_time")
    protected long exitTime;
    /**
     * 所属联盟
     */
    @Field("league_id")
    protected String leagueId;
    /**
     * 上一次所属联盟
     */
    @Field("last_league_id")
    protected String lastLeagueId;
    /**
     * 申请数量
     */
    @Field("apply_number")
    protected int applyNumber;
    /**
     * 上一次贡献钻石时间
     */
    @Field("last_diamond_donate_time")
    protected long lastDiamondDonateTime;
    /**
     * 上一次贡献金币时间
     */
    @Field("last_gold_donate_time")
    protected long lastGoldDonateTime;
    /**
     * 金币捐献次数
     */
    @Field("gold_donate_times")
    protected int goldDonateTimes;
    /**
     * 钻石捐献次数
     */
    @Field("diamond_donate_times")
    protected int diamondDonateTimes;
    /**
     * 上一次操作时间
     */
    @Field("last_time")
    protected long lastTime;
    /**
     * 商店
     */
    protected LeagueShop shop;

    /**
     * @return fund
     */
    public int getFund() {
        return fund;
    }

    /**
     * @param fund 要设置的 fund
     */
    public void setFund(int fund) {
        this.fund = fund;
    }

    /**
     * @return duty
     */
    public int getDuty() {
        return duty;
    }

    /**
     * @param duty 要设置的 duty
     */
    public void setDuty(int duty) {
        this.duty = duty;
    }

    /**
     * @return joinTime
     */
    public long getJoinTime() {
        return joinTime;
    }

    /**
     * @param joinTime 要设置的 joinTime
     */
    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    /**
     * @return worship
     */
    public Map<String, Object> getWorship() {
        return worship;
    }

    /**
     * @param worship 要设置的 worship
     */
    public void setWorship(Map<String, Object> worship) {
        this.worship = worship;
    }

    /**
     * @return worshiped
     */
    public Map<String, Object> getWorshiped() {
        return worshiped;
    }

    /**
     * @param worshiped 要设置的 worshiped
     */
    public void setWorshiped(Map<String, Object> worshiped) {
        this.worshiped = worshiped;
    }

    /**
     * @return everyDonate
     */
    public Map<String, Integer> getEveryDonate() {
        return everyDonate;
    }

    /**
     * @param everyDonate 要设置的 everyDonate
     */
    public void setEveryDonate(Map<String, Integer> everyDonate) {
        this.everyDonate = everyDonate;
    }

    /**
     * @return exitTime
     */
    public long getExitTime() {
        return exitTime;
    }

    /**
     * @param exitTime 要设置的 exitTime
     */
    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * @return leagueId
     */
    public String getLeagueId() {
        return leagueId;
    }

    /**
     * @param leagueId 要设置的 leagueId
     */
    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * @return lastLeagueId
     */
    public String getLastLeagueId() {
        return lastLeagueId;
    }

    /**
     * @param lastLeagueId 要设置的 lastLeagueId
     */
    public void setLastLeagueId(String lastLeagueId) {
        this.lastLeagueId = lastLeagueId;
    }

    /**
     * @return applyNumber
     */
    public int getApplyNumber() {
        return applyNumber;
    }

    /**
     * @param applyNumber 要设置的 applyNumber
     */
    public void setApplyNumber(int applyNumber) {
        this.applyNumber = applyNumber;
    }

    /**
     * @return lastDiamondDonateTime
     */
    public long getLastDiamondDonateTime() {
        return lastDiamondDonateTime;
    }

    /**
     * @param lastDiamondDonateTime 要设置的 lastDiamondDonateTime
     */
    public void setLastDiamondDonateTime(long lastDiamondDonateTime) {
        this.lastDiamondDonateTime = lastDiamondDonateTime;
    }

    /**
     * @return lastGoldDonateTime
     */
    public long getLastGoldDonateTime() {
        return lastGoldDonateTime;
    }

    /**
     * @param lastGoldDonateTime 要设置的 lastGoldDonateTime
     */
    public void setLastGoldDonateTime(long lastGoldDonateTime) {
        this.lastGoldDonateTime = lastGoldDonateTime;
    }

    /**
     * @return goldDonateTimes
     */
    public int getGoldDonateTimes() {
        return goldDonateTimes;
    }

    /**
     * @param goldDonateTimes 要设置的 goldDonateTimes
     */
    public void setGoldDonateTimes(int goldDonateTimes) {
        this.goldDonateTimes = goldDonateTimes;
    }

    /**
     * @return diamondDonateTimes
     */
    public int getDiamondDonateTimes() {
        return diamondDonateTimes;
    }

    /**
     * @param diamondDonateTimes 要设置的 diamondDonateTimes
     */
    public void setDiamondDonateTimes(int diamondDonateTimes) {
        this.diamondDonateTimes = diamondDonateTimes;
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
     * @return shop
     */
    public LeagueShop getShop() {
        // 重置购买记录
        if (!DateUtils.isSameDay(new Date(shop.getLastBuyTime()), new Date())) {
            shop.setRecord(new HashMap<Integer, Integer>());
            shop.setLastBuyTime(System.currentTimeMillis());
        }
        return shop;
    }

    /**
     * @param shop 要设置的 shop
     */
    public void setShop(LeagueShop shop) {
        this.shop = shop;
    }

}
