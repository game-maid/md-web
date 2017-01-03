/**
 * @Title: PortalLog.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月28日 闫昆
 */

package com.talentwalker.game.md.core.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName: PortalLog
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月28日 下午5:29:46
 */

@Document(collection = "portal_log")
public class PortalLog extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    private String uri;

    private Object params;

    private Object result;

    private int code;

    private String ip;

    @Field("request_time")
    private long requestTime;

    private long cost;

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
     * @return params
     */
    public Object getParams() {
        return params;
    }

    /**
     * @param params 要设置的 params
     */
    public void setParams(Object params) {
        this.params = params;
    }

    /**
     * @return result
     */
    public Object getResult() {
        return result;
    }

    /**
     * @param result 要设置的 result
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code 要设置的 code
     */
    public void setCode(int code) {
        this.code = code;
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
     * @return requestTime
     */
    public long getRequestTime() {
        return requestTime;
    }

    /**
     * @param requestTime 要设置的 requestTime
     */
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * @return cost
     */
    public long getCost() {
        return cost;
    }

    /**
     * @param cost 要设置的 cost
     */
    public void setCost(long cost) {
        this.cost = cost;
    }

}
