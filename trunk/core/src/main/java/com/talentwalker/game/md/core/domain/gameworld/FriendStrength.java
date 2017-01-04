/**
 * @Title: FriendSendStrength.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月9日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

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
     * 发送时间
     */
    private Date sendTime;

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

}
