/**
 * @Title: PrivateMesageStatus.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月3日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: PrivateMesageStatus
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月3日 上午11:42:47
 */
@Document
public class PrivateMessageStatus extends BaseDomain {

    private static final long serialVersionUID = -7753557600338052044L;

    @DBRef
    private Lord sender;
    private String receiverId;
    private int unReadCount;
    @DBRef
    private PrivateMessage lastMessage;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    /**
     * @return sender
     */
    public Lord getSender() {
        return sender;
    }

    /**
     * @param sender 要设置的 sender
     */
    public void setSender(Lord sender) {
        this.sender = sender;
    }

    public PrivateMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(PrivateMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

}
