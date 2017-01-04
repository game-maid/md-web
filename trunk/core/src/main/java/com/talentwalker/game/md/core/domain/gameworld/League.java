/**
 * @Title: League.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: League
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 上午11:26:08
 */
@Document(collection = "game_league")
public class League extends BaseDomain {

    private static final long serialVersionUID = 6458765291747527727L;
    /**
     * 联盟名称
     */
    protected String name;
    /**
     * 宣言
     */
    protected String notice;
    /**
     * 盟主id
     */
    @Field("president_id")
    protected String presidentId;
    /**
     * 会长名称
     */
    @Transient
    protected String presidentName;
    /**
     * 等级
     */
    protected int level;
    /**
     * 经验
     */
    protected int exp;

    /**
     * 贡献
     */
    protected int donate;
    /**
     * 联盟成员
     */
    @Field("lord_list")
    protected List<String> lordList;
    // /**
    // * 人数
    // */
    // protected int peopleNumber;
    /**
     * 申请人
     */
    protected List<String> applicant;
    /**
     * 是否自动通过（默认false：手动通过）true：自动通过
     */
    protected boolean automate;
    /**
     * 限制等级
     */
    @Field("limit_level")
    protected int limitLevel;
    /**
     * 限制vip等级
     */
    @Field("limit_vip")
    protected int limitVip;
    /**
     * 创建时间
     */
    @Field("create_time")
    protected long createTime;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return notice
     */
    public String getNotice() {
        return notice;
    }

    /**
     * @param notice 要设置的 notice
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     * @return presidentId
     */
    public String getPresidentId() {
        return presidentId;
    }

    /**
     * @param presidentId 要设置的 presidentId
     */
    public void setPresidentId(String presidentId) {
        this.presidentId = presidentId;
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
     * @return exp
     */
    public int getExp() {
        return exp;
    }

    /**
     * @param exp 要设置的 exp
     */
    public void setExp(int exp) {
        this.exp = exp;
    }

    /**
     * @return donate
     */
    public int getDonate() {
        return donate;
    }

    /**
     * @param donate 要设置的 donate
     */
    public void setDonate(int donate) {
        this.donate = donate;
    }

    /**
     * @return lordList
     */
    public List<String> getLordList() {
        return lordList;
    }

    /**
     * @param lordList 要设置的 lordList
     */
    public void setLordList(List<String> lordList) {
        this.lordList = lordList;
    }

    /**
     * @return applicant
     */
    public List<String> getApplicant() {
        return applicant;
    }

    /**
     * @param applicant 要设置的 applicant
     */
    public void setApplicant(List<String> applicant) {
        this.applicant = applicant;
    }

    /**
     * @return automate
     */
    public boolean isAutomate() {
        return automate;
    }

    /**
     * @param automate 要设置的 automate
     */
    public void setAutomate(boolean automate) {
        this.automate = automate;
    }

    /**
     * @return limitLevel
     */
    public int getLimitLevel() {
        return limitLevel;
    }

    /**
     * @param limitLevel 要设置的 limitLevel
     */
    public void setLimitLevel(int limitLevel) {
        this.limitLevel = limitLevel;
    }

    /**
     * @return limitVip
     */
    public int getLimitVip() {
        return limitVip;
    }

    /**
     * @param limitVip 要设置的 limitVip
     */
    public void setLimitVip(int limitVip) {
        this.limitVip = limitVip;
    }

    /**
     * @return createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 要设置的 createTime
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return presidentName
     */
    public String getPresidentName() {
        return presidentName;
    }

    /**
     * @param presidentName 要设置的 presidentName
     */
    public void setPresidentName(String presidentName) {
        this.presidentName = presidentName;
    }

}
