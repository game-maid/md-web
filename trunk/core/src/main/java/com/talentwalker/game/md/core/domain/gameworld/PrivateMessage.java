
package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: PrivateMessage
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月9日 上午10:54:23
 */
@Document(collection = "game_private_message")
@CompoundIndexes({@CompoundIndex(def = "{minId:1,maxId:1}") })
public class PrivateMessage extends BaseDomain {

    private static final long serialVersionUID = 2125791375691336047L;

    private String minId;

    private String maxId;

    private String senderId;

    private String receiverId;

    private String content;
    /**
     * 发送时间，保留一个月
     */
    @Indexed(expireAfterSeconds = 3600 * 24 * 30)
    private long sendTime;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return minId
     */
    public String getMinId() {
        return minId;
    }

    /**
     * @param minId 要设置的 minId
     */
    public void setMinId(String minId) {
        this.minId = minId;
    }

    /**
     * @return maxId
     */
    public String getMaxId() {
        return maxId;
    }

    /**
     * @param maxId 要设置的 maxId
     */
    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

}
