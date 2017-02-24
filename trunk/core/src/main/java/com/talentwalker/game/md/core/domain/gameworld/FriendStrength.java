/**
 * @Title: FriendSendStrength.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月9日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;
import com.talentwalker.game.md.core.response.LordInfo;

/**
 * @ClassName: FriendSendStrength
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月9日 上午11:14:17
 */

@Document(collection = "game_friend_send_strength")
@CompoundIndexes({@CompoundIndex(def = "{senderId:1,sendTime:1}"), @CompoundIndex(def = "{receiverId:1,sendTime:1}") })

public class FriendStrength extends BaseDomain {
    private static final long serialVersionUID = -315010873713946284L;
    private String senderId;
    @Indexed
    private String receiverId;
    /**
     * 是否领取奖励 false:未领取，true：已领取
     */
    private boolean isReceive;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 发送日期
     */
    private String sendDate;
    /**
     * 失效时间(领取一天后消失)
     */
    @Field("failure_time")
    @Indexed(name = "idx_failureTime", expireAfterSeconds = 60 * 60 * 24 * 1)
    private Date failureTime;
    @Transient
    private LordInfo lordInfo;

    /**
     * @return senderId
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * @param senderId 要设置的 senderId
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * @return receiverId
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * @return failureTime
     */
    public Date getFailureTime() {
        return failureTime;
    }

    /**
     * @param failureTime 要设置的 failureTime
     */
    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
    }

    /**
     * @param receiverId 要设置的 receiverId
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * @return sendTime
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime 要设置的 sendTime
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return isReceive
     */
    public boolean isReceive() {
        return isReceive;
    }

    /**
     * @param isReceive 要设置的 isReceive
     */
    public void setReceive(boolean isReceive) {
        this.isReceive = isReceive;
    }

    /**
     * @return sendDate
     */
    public String getSendDate() {
        return sendDate;
    }

    /**
     * @param sendDate 要设置的 sendDate
     */
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * @return lordInfo
     */
    public LordInfo getLordInfo() {
        return lordInfo;
    }

    /**
     * @param lordInfo 要设置的 lordInfo
     */
    public void setLordInfo(LordInfo lordInfo) {
        this.lordInfo = lordInfo;
    }

}
