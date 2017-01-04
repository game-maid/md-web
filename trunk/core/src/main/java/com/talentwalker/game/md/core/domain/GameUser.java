/**
 * @Title: GameSession.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月23日  占志灵
 */

package com.talentwalker.game.md.core.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName: GameSession
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月23日 上午10:51:52
 */
@Document(collection = "portal_gameuser")
@CompoundIndex(name = "index_platfomuid_gamezone", def = "{sso_id:1,platform_id:1,gamezone_id:1}", unique = true)
public class GameUser extends BaseDomain {

    private static final long serialVersionUID = -5100901401172420984L;

    /**
     * 平台ID
     */
    @Field("platform_id")
    private String platformId;
    /**
     * 平台账号ID
     */
    @Field("sso_id")
    private String ssoId;
    /**
     * 游戏区服ID
     */
    @Field("gamezone_id")
    private String gameZoneId;
    @Transient
    private GameZone gameZone;
    /**
     * 登录游戏服务器的凭证
     */
    @Field("gamesession_id")
    @Indexed(unique = true)
    private String gamesessionId;
    /**
     * 客户端的版本号
     */
    @Field("client_version")
    private String clientVersion;
    /**
     * 客户端包ID
     */
    @Field("package_id")
    private String packageId;
    @Transient
    private GamePackage gamePackage;

    /**
     * 物理服务器id
     */
    @Field("physicalserver_id")
    private String physicalServerId;
    @Transient
    private PhysicalServer physicalServer;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getGameZoneId() {
        return gameZoneId;
    }

    public void setGameZoneId(String gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public String getGamesessionId() {
        return gamesessionId;
    }

    public void setGamesessionId(String gamesessionId) {
        this.gamesessionId = gamesessionId;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPhysicalServerId() {
        return physicalServerId;
    }

    public void setPhysicalServerId(String physicalServerId) {
        this.physicalServerId = physicalServerId;
    }

    public GameZone getGameZone() {
        return gameZone;
    }

    public void setGameZone(GameZone gameZone) {
        this.gameZone = gameZone;
    }

    public GamePackage getGamePackage() {
        return gamePackage;
    }

    public void setGamePackage(GamePackage gamePackage) {
        this.gamePackage = gamePackage;
    }

    public PhysicalServer getPhysicalServer() {
        return physicalServer;
    }

    public void setPhysicalServer(PhysicalServer physicalServer) {
        this.physicalServer = physicalServer;
    }
}
