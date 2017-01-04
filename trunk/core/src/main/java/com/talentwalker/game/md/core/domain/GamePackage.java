/**
 * @Title: Package.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月24日  赵丽宝
 */

package com.talentwalker.game.md.core.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName: Package
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年5月24日 下午5:13:29
 */

@Document(collection = "portal_package")
public class GamePackage extends GamePackageBase {
    private static final long serialVersionUID = -5008388674191575261L;

    @NotBlank
    private String name;

    @Field("version_big_test")
    private String versionBigTest;

    @Field("version_small_test")
    private String versionSmallTest;

    @Field("version_small_audit")
    private String versionSmallAudit;

    @Field("white_uid")
    private List<Map<String, String>> whiteUid;

    @Field("white_cdn_url")
    private String whiteCdnUrl;

    @DBRef
    @Field("server_audit_visible")
    private List<GameZone> serverAuditVisible;

    @DBRef
    @Field("server_online_hide")
    private List<GameZone> serverOnlineHide;

    @Field("update_set")
    private List<Map<String, String>> updateSet;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @DBRef
    private Platform platform;

    @Transient
    private String platformId;

    @Transient
    private List<String> serverAuditVisibleList;

    @Transient
    private List<String> serverOnlineHideList;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return versionBigTest
     */
    public String getVersionBigTest() {
        return versionBigTest;
    }

    /**
     * @param versionBigTest 要设置的 versionBigTest
     */
    public void setVersionBigTest(String versionBigTest) {
        this.versionBigTest = versionBigTest;
    }

    /**
     * @return versionSmallTest
     */
    public String getVersionSmallTest() {
        return versionSmallTest;
    }

    /**
     * @param versionSmallTest 要设置的 versionSmallTest
     */
    public void setVersionSmallTest(String versionSmallTest) {
        this.versionSmallTest = versionSmallTest;
    }

    /**
     * @return versionSmallAudit
     */
    public String getVersionSmallAudit() {
        return versionSmallAudit;
    }

    /**
     * @param versionSmallAudit 要设置的 versionSmallAudit
     */
    public void setVersionSmallAudit(String versionSmallAudit) {
        this.versionSmallAudit = versionSmallAudit;
    }

    /**
     * @return whiteUid
     */
    public List<Map<String, String>> getWhiteUid() {
        return whiteUid;
    }

    /**
     * @param whiteUid 要设置的 whiteUid
     */
    public void setWhiteUid(List<Map<String, String>> whiteUid) {
        this.whiteUid = whiteUid;
    }

    /**
     * @return whiteCdnUrl
     */
    public String getWhiteCdnUrl() {
        return whiteCdnUrl;
    }

    /**
     * @param whiteCdnUrl 要设置的 whiteCdnUrl
     */
    public void setWhiteCdnUrl(String whiteCdnUrl) {
        this.whiteCdnUrl = whiteCdnUrl;
    }

    /**
     * @return serverAuditVisible
     */
    public List<GameZone> getServerAuditVisible() {
        return serverAuditVisible;
    }

    /**
     * @param serverAuditVisible 要设置的 serverAuditVisible
     */
    public void setServerAuditVisible(List<GameZone> serverAuditVisible) {
        this.serverAuditVisible = serverAuditVisible;
    }

    /**
     * @return serverOnlineHide
     */
    public List<GameZone> getServerOnlineHide() {
        return serverOnlineHide;
    }

    /**
     * @param serverOnlineHide 要设置的 serverOnlineHide
     */
    public void setServerOnlineHide(List<GameZone> serverOnlineHide) {
        this.serverOnlineHide = serverOnlineHide;
    }

    /**
     * @return updateSet
     */
    public List<Map<String, String>> getUpdateSet() {
        return updateSet;
    }

    /**
     * @param updateSet 要设置的 updateSet
     */
    public void setUpdateSet(List<Map<String, String>> updateSet) {
        this.updateSet = updateSet;
    }

    /**
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate 要设置的 createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * @param platform 要设置的 platform
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
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
     * @return serverAuditVisibleList
     */
    public List<String> getServerAuditVisibleList() {
        return serverAuditVisibleList;
    }

    /**
     * @param serverAuditVisibleList 要设置的 serverAuditVisibleList
     */
    public void setServerAuditVisibleList(List<String> serverAuditVisibleList) {
        this.serverAuditVisibleList = serverAuditVisibleList;
    }

    /**
     * @return serverOnlineHideList
     */
    public List<String> getServerOnlineHideList() {
        return serverOnlineHideList;
    }

    /**
     * @param serverOnlineHideList 要设置的 serverOnlineHideList
     */
    public void setServerOnlineHideList(List<String> serverOnlineHideList) {
        this.serverOnlineHideList = serverOnlineHideList;
    }

}
