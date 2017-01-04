/**
 * @Title: PackageBase.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月1日  占志灵
 */

package com.talentwalker.game.md.core.domain;

import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName: PackageBase
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月1日 上午11:47:00
 */

public class GamePackageBase extends BaseDomain {
    private static final long serialVersionUID = -1539149139538137582L;

    @Field("version_big_online")
    protected String versionBigOnline;

    @Field("version_small_online")
    protected String versionSmallOnline;

    @Field("cdn_url")
    protected String cdnUrl;

    @Transient
    private Map<String, String> update;

    public String getVersionBigOnline() {
        return versionBigOnline;
    }

    public void setVersionBigOnline(String versionBigOnline) {
        this.versionBigOnline = versionBigOnline;
    }

    public String getVersionSmallOnline() {
        return versionSmallOnline;
    }

    public void setVersionSmallOnline(String versionSmallOnline) {
        this.versionSmallOnline = versionSmallOnline;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    /**
     * @return update
     */
    public Map<String, String> getUpdate() {
        return update;
    }

    /**
     * @param update 要设置的 update
     */
    public void setUpdate(Map<String, String> update) {
        this.update = update;
    }

}
