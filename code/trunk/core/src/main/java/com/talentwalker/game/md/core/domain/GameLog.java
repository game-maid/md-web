/**
 * @Title: GameLog.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月14日 闫昆
 */

package com.talentwalker.game.md.core.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName: GameLog
 * @Description: 游戏日志
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月14日 下午5:36:48
 */

@Document(collection = "game_log")
public class GameLog extends BaseDomain {
    private static final long serialVersionUID = -5008388674191575261L;

    @Field("player_id")
    private String playerId;

    private String uri;

    private Object params;

    private Object result;

    private int code;

    private String ip;

    @Field("zone_id")
    private String zoneId;

    @Field("platform_id")
    private String platformId;

    @Field("sso_id")
    private String ssoId;

    @Field("package_id")
    private String packageId;

    @Field("physicalserver_id")
    private String physicalserverId;

    @Field("session_id")
    private String sessionId;

    @Field("client_version")
    private String clientVersion;

    @Field("request_time")
    private long requestTime;

    private long cost;

    @Field("pre_level")
    private int preLevel;

    @Field("post_level")
    private int postLevel;

    @Field("pre_gold")
    private int preGold;

    @Field("post_gold")
    private int postGold;

    @Field("pre_diamond")
    private int preDiamond;

    @Field("post_diamond")
    private int postDiamond;

    @Field("pre_vipscore")
    private int preVipscore;

    @Field("post_vipscore")
    private int postVipscore;

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

    /**
     * @return preLevel
     */
    public int getPreLevel() {
        return preLevel;
    }

    /**
     * @param preLevel 要设置的 preLevel
     */
    public void setPreLevel(int preLevel) {
        this.preLevel = preLevel;
    }

    /**
     * @return postLevel
     */
    public int getPostLevel() {
        return postLevel;
    }

    /**
     * @param postLevel 要设置的 postLevel
     */
    public void setPostLevel(int postLevel) {
        this.postLevel = postLevel;
    }

    /**
     * @return preGold
     */
    public int getPreGold() {
        return preGold;
    }

    /**
     * @param preGold 要设置的 preGold
     */
    public void setPreGold(int preGold) {
        this.preGold = preGold;
    }

    /**
     * @return postGold
     */
    public int getPostGold() {
        return postGold;
    }

    /**
     * @param postGold 要设置的 postGold
     */
    public void setPostGold(int postGold) {
        this.postGold = postGold;
    }

    /**
     * @return preDiamond
     */
    public int getPreDiamond() {
        return preDiamond;
    }

    /**
     * @param preDiamond 要设置的 preDiamond
     */
    public void setPreDiamond(int preDiamond) {
        this.preDiamond = preDiamond;
    }

    /**
     * @return postDiamond
     */
    public int getPostDiamond() {
        return postDiamond;
    }

    /**
     * @param postDiamond 要设置的 postDiamond
     */
    public void setPostDiamond(int postDiamond) {
        this.postDiamond = postDiamond;
    }

    /**
     * @return preVipscore
     */
    public int getPreVipscore() {
        return preVipscore;
    }

    /**
     * @param preVipscore 要设置的 preVipscore
     */
    public void setPreVipscore(int preVipscore) {
        this.preVipscore = preVipscore;
    }

    /**
     * @return postVipscore
     */
    public int getPostVipscore() {
        return postVipscore;
    }

    /**
     * @param postVipscore 要设置的 postVipscore
     */
    public void setPostVipscore(int postVipscore) {
        this.postVipscore = postVipscore;
    }

    /**
     * @return zoneId
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * @param zoneId 要设置的 zoneId
     */
    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * @return platformId
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * @param platformId 要设置的 platformId
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    /**
     * @return ssoId
     */
    public String getSsoId() {
        return ssoId;
    }

    /**
     * @param ssoId 要设置的 ssoId
     */
    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    /**
     * @return packageId
     */
    public String getPackageId() {
        return packageId;
    }

    /**
     * @param packageId 要设置的 packageId
     */
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    /**
     * @return physicalserverId
     */
    public String getPhysicalserverId() {
        return physicalserverId;
    }

    /**
     * @param physicalserverId 要设置的 physicalserverId
     */
    public void setPhysicalserverId(String physicalserverId) {
        this.physicalserverId = physicalserverId;
    }

    /**
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId 要设置的 sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return clientVersion
     */
    public String getClientVersion() {
        return clientVersion;
    }

    /**
     * @param clientVersion 要设置的 clientVersion
     */
    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

}
