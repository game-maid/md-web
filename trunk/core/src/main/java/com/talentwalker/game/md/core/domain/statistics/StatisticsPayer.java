
package com.talentwalker.game.md.core.domain.statistics;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: StatisticsPayer
 * @Description: 每日每个玩家支付的时间（用于后台统计）
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月23日 下午7:35:18
 */
@Document(collection = "game_payer")
public class StatisticsPayer extends BaseDomain {
    @Field("zone_id")
    private String zoneId;

    @Field("package_id")
    private String packageId;
    /**
     * 每日支付时间
     */
    @Field("pay_time")
    private long payTime;
    /**
     * 玩家id
     */
    @Field("lord_id")
    private String lordId;

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
     * @return payTime
     */
    public long getPayTime() {
        return payTime;
    }

    /**
     * @param payTime 要设置的 payTime
     */
    public void setPayTime(long payTime) {
        this.payTime = payTime;
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

}
