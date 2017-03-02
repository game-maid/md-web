/**
 * @Title: PrivateMesageStatus.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月3日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.index.Indexed;
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

    private String senderId;
    @Indexed
    private String receiverId;
    private int unReadCount;

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

}
