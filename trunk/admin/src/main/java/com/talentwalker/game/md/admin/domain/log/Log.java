/**
 * @Title: Log.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月12日 闫昆
 */

package com.talentwalker.game.md.admin.domain.log;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Log
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月12日 下午5:18:04
 */

@Document(collection = "admin_log")
public class Log extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    @Field("user_id")
    private String userId;

    private String ip;

    private String uri;

    private String method;

    private String params;

    private String result;

    @Field("create_time")
    @Indexed(name = "idx_createTime", expireAfterSeconds = 60 * 60 * 24 * 30)
    private Date createTime;

    /**
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId 要设置的 userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip 要设置的 ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri 要设置的 uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method 要设置的 method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return params
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params 要设置的 params
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result 要设置的 result
     */
    public void setResult(String result) {
        this.result = result;
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

}
