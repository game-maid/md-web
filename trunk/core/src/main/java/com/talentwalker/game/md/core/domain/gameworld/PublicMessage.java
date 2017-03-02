/**
 * @Title: PublicMessage.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;
import com.talentwalker.game.md.core.response.LordInfo;

/**
 * @ClassName: PublicMessage
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2017年3月2日 下午3:20:19
 */
@Document(collection = "game_private_message")
@CompoundIndexes({@CompoundIndex(def = "{minId:1,maxId:1}") })
public class PublicMessage extends BaseDomain {
    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = -6700541349393222343L;

    private LordInfo sender;

    private String receiverId;

    private String content;
    /**
     * 发送时间，保留一个月
     */
    @Indexed(expireAfterSeconds = 3600 * 24 * 30)
    private long sendTime;

    /**
     * @return sender
     */
    public LordInfo getSender() {
        return sender;
    }

    /**
     * @param sender 要设置的 sender
     */
    public void setSender(LordInfo sender) {
        this.sender = sender;
    }

    /**
     * @return sendTime
     */
    public long getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime 要设置的 sendTime
     */
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
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
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 要设置的 content
     */
    public void setContent(String content) {
        this.content = content;
    }

}
