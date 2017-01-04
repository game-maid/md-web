
package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Date;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Friend
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月7日 上午10:53:33
 */
@Document(collection = "game_friend")
@CompoundIndexes({@CompoundIndex(def = "{minId:1,maxId:1}", unique = true),
        @CompoundIndex(def = "{requestedId:1,requestId:1,status:1}"),
        @CompoundIndex(def = "{requestedId:1,requestId:1}") })
public class Friend extends BaseDomain {
    public static int STATUS_REQUEST = 1;
    public static int STATUS_AGREE = 2;

    private static final long serialVersionUID = -4169131579419404526L;
    private String minId;
    private String maxId;
    /**
     * 请求者id
     */
    private String requestId;
    /**
     * 被请求者id
     */
    private String requestedId;
    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 同意时间
     */
    private Date agreeTime;
    /**
     * 自动删除时间
     */
    @Indexed(name = "idx_failureTime", expireAfterSeconds = 0)
    private Date failureTime;
    /**
     * 状态
     */
    private int status;

    public void agree() {
        this.agreeTime = new Date();
        this.status = STATUS_AGREE;
        this.failureTime = null;
    }

    public String getRequestedId() {
        return requestedId;
    }

    public void setRequestedId(String requestedId) {
        this.requestedId = requestedId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getAgreeTime() {
        return agreeTime;
    }

    public void setAgreeTime(Date agreeTime) {
        this.agreeTime = agreeTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
