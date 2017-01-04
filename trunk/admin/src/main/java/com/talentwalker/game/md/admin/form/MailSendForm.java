/**
 * @Title: MailSendForm.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月19日  赵丽宝
 */

package com.talentwalker.game.md.admin.form;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: MailSendForm
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月19日 下午3:28:14
 */
@Document(collection = "admin_mail")
public class MailSendForm extends BaseDomain {
    private static final long serialVersionUID = -8143836873840103037L;
    /******************* 状态 *******************/
    public final static int STATUS_SENT = 0; // 已发送
    public final static int STATUS_UNSENT = 1; // 待发送
    public final static int STATUS_CANCEL = 2; // 已取消
    public final static int STATUS_SENDING = 3; // 发送中

    /**
     * 发件人
     */
    protected String sender;
    /**
     * 发送者头像
     */
    protected String senderHeadIcon;
    /**
     * 单一玩家
     */
    public final static int GOAL_LORD_ONE = 1;
    /**
     * 全部玩家
     */
    public final static int GOAL_LORD_ALL = 2;
    /**
     * 符合条件玩家
     */
    public final static int GOAL_LORD_SELECT = 3;
    /**
     * 发送目标
     */
    private int goal;
    /**
     * 发送时间
     */
    @Transient
    private String sendDate;
    /**
     * 玩家列表
     */
    private List<String> player;
    /**
     * 条件
     */
    private String condition;
    /**
     * 奖励道具
     */
    @Transient
    private String items;
    /**
     * 区服
     */
    private List<String> servers;

    /**
     * 邮件标题
     */
    protected String title;

    /**
     * 邮件文本内容
     */
    protected String content;

    /**
     * 邮件奖励
     */
    protected Map<String, Integer> reward;
    /**
     * 状态
     */
    protected int status;

    /**
     * 邮件创建时间
     */
    @Field("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;

    /**
     * 邮件发送时间
     */
    @Field("send_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date sendTime;

    /**
     * 有效天数
     */
    protected int validDay;

    /**
     * @return goal
     */
    public int getGoal() {
        return goal;
    }

    /**
     * @param goal 要设置的 goal
     */
    public void setGoal(int goal) {
        this.goal = goal;
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
     * @return player
     */
    public List<String> getPlayer() {
        return player;
    }

    /**
     * @param player 要设置的 player
     */
    public void setPlayer(List<String> player) {
        this.player = player;
    }

    /**
     * @return condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition 要设置的 condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @return items
     */
    public String getItems() {
        return items;
    }

    /**
     * @param items 要设置的 items
     */
    public void setItems(String items) {
        this.items = items;
    }

    /**
     * @return servers
     */
    public List<String> getServers() {
        return servers;
    }

    /**
     * @param servers 要设置的 servers
     */
    public void setServers(List<String> servers) {
        this.servers = servers;
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
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 要设置的 createTime
     */
    public void setCreateTime(Date createTime) {
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
     * @return serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
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
     * @return validDay
     */
    public int getValidDay() {
        return validDay;
    }

    /**
     * @param validDay 要设置的 validDay
     */
    public void setValidDay(int validDay) {
        this.validDay = validDay;
    }

}
