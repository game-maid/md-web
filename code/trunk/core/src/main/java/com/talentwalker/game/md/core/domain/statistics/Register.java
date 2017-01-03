
package com.talentwalker.game.md.core.domain.statistics;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Register
 * @Description: 用于统计注册人数
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月22日 上午10:39:20
 */
@Document(collection = "game_register")
public class Register extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = -6914224964588556911L;

    /**
     * 注册时间
     */
    @Field("register_time")
    private Long registerTime;
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
     * @return registerTime
     */
    public Long getRegisterTime() {
        return registerTime;
    }

    /**
     * @param registerTime 要设置的 registerTime
     */
    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
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
