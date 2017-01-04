
package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Feedback
 * @Description: 客服反馈
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月22日 上午10:42:47
 */
@Document(collection = "game_feedback")
public class Feedback extends BaseDomain {
    private static final long serialVersionUID = -7277189192108151731L;
    /**
     * 反馈类型
     */
    public final static int TYPE_CONSULT = 0;// 咨询
    public final static int TYPE_BUG = 1;// bug
    public final static int TYPE_COMPLAINT = 2;// 投诉
    public final static int TYPE_SUGGEST = 3;// 建议
    /**
     * 解决状态
     */
    public final static int STATUS_UNSOLVED = 0;// 未解决
    public final static int STATUS_SOLVING = 1;// 解决中
    public final static int STATUS_INEXTRICABLIITY = 2;// 无法解决
    public final static int STATUS_RESOLVED = 3;// 已解决

    /**
     * 反馈玩家Id
     */
    @Field("player_id")
    protected String playerId;

    /**
     * 创建时间
     */
    @Field("create_time")
    protected Date createTime;

    /**
     * 区服ID
     */
    @Field("gamezone_id")
    protected String gamezoneId;

    /**
     * 反馈类型
     */
    protected int type;

    /**
     * 反馈状态
     */
    protected int status;

    /**
     * 玩家反馈内容
     */
    protected String message;

    /**
     * 客服反馈邮件的id
     */
    @Field("mail_ids")
    protected List<String> mailIds;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMailIds() {
        return mailIds;
    }

    public void setMailIds(List<String> mailIds) {
        this.mailIds = mailIds;
    }

    public String getGamezoneId() {
        return gamezoneId;
    }

    public void setGamezoneId(String gamezoneId) {
        this.gamezoneId = gamezoneId;
    }

}
