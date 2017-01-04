
package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: PrivateMessage
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月9日 上午10:54:23
 */
@Document(collection = "game_private_message")
public class PrivateMessage extends BaseDomain {

    private static final long serialVersionUID = 2125791375691336047L;
    @Indexed
    private String senderId;
    @Indexed
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
}
