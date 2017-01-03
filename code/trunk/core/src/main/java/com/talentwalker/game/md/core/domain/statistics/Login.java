
package com.talentwalker.game.md.core.domain.statistics;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * 
 * @ClassName: Login
 * @Description: 每天活跃度
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月22日 上午11:03:08
 */
@Document(collection = "game_login")
public class Login extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 5234763462086289776L;

    /**
     * 登录时间
     */
    @Field("login_time")
    private long loginTime;
    /**
     * 玩家id
     */
    @Field("lord_id")
    private String lordId;

    /**
     * 区服id
     */
    @Field("zone_id")
    private String zoneId;
    /**
     * 包id
     */
    @Field("package_id")
    private String packageId;

    /**
     * @return loginTime
     */
    public long getLoginTime() {
        return loginTime;
    }

    /**
     * @param loginTime 要设置的 loginTime
     */
    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * @return lordId
     */
    public String getLordId() {
        return lordId;
    }

    /**
     * @param lordId 要设置的 lordId
     */
    public void setLordId(String lordId) {
        this.lordId = lordId;
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
     * @return serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
