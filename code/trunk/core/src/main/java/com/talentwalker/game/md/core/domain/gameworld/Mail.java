/**
 * @Title: Mail.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月9日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Mail
 * @Description: 邮件
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年8月9日 上午10:59:53
 */

@Document(collection = "game_mail")
public class Mail extends BaseDomain {

    /******************* 邮件类型 *******************/
    public final static int TYPE_ADMIN = 1; // 后台邮件
    public final static int TYPE_PVP = 2; // 擂台邮件
    public final static int TYPE_NPC = 3; // 系统邮件

    /******************* 邮件状态 *******************/
    public final static int STATUS_UNREAD = 0; // 未读状态
    public final static int STATUS_READ = 1; // 已读、未领取奖励状态
    public final static int STATUS_REWARD = 2; // 已读、已领取奖励状态

    /**
     * @Fields serialVersionUID : Description
     */
    protected static final long serialVersionUID = 1L;

    /**
     * 收件人ID
     */
    @Field("player_id")
    protected String playerId;

    /**
     * 区服code
     */
    protected String server;

    /**
     * 邮件标题
     */
    protected String title;

    /**
     * 邮件文本内容
     */
    protected String content;

    /**
     * 邮件类型
     */
    protected int type;

    /**
     * 邮件状态
     */
    protected int status;

    /**
     * 邮件奖励
     */
    protected Map<String, Integer> reward;

    /**
     * 发件人
     */
    protected String sender;

    /**
     * 邮件创建时间
     */
    @Field("create_time")
    protected long createTime;

    /**
     * 邮件发送时间，设置TTL，发送7天后自动删除邮件
     */
    @Field("send_time")
    // @Indexed(name = "idx_sendTime", expireAfterSeconds = 60 * 60 * 24 * 7)
    protected Date sendTime;

    /**
     * 失效时间
     */
    @Field("failure_time")
    @Indexed(name = "idx_failureTime", expireAfterSeconds = 0)
    protected Date failureTime;
    /**
     * 发送者头像
     */
    protected String senderHeadIcon;
    /**
     * 替换翻译的参数组
     */
    protected List<Object> paramet;

    /**
     * @return playerId
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId 要设置的 playerId
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title 要设置的 title
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type 要设置的 type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status 要设置的 status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return reward
     */
    public Map<String, Integer> getReward() {
        return reward;
    }

    /**
     * @param reward 要设置的 reward
     */
    public void setReward(Map<String, Integer> reward) {
        this.reward = reward;
    }

    /**
     * @return sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender 要设置的 sender
     */
    public void setSender(String sender) {
        this.sender = sender;
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
     * @return server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server 要设置的 server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return senderHeadIcon
     */
    public String getSenderHeadIcon() {
        return senderHeadIcon;
    }

    /**
     * @param senderHeadIcon 要设置的 senderHeadIcon
     */
    public void setSenderHeadIcon(String senderHeadIcon) {
        this.senderHeadIcon = senderHeadIcon;
    }

    /**
     * @return paramet
     */
    public List<Object> getParamet() {
        return paramet;
    }

    /**
     * @param paramet 要设置的 paramet
     */
    public void setParamet(List<Object> paramet) {
        this.paramet = paramet;
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

}
