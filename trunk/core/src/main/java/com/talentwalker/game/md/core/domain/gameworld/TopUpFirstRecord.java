/**
 * @Title: TopUpFirstRecord.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月5日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: TopUpFirstRecord
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月5日 下午4:00:24
 */
@Document(collection = "game_topup_first_record")
public class TopUpFirstRecord extends BaseDomain {
    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = -5076895615495856698L;
    private Map<String, Integer> record;
    @Field("create_time")
    private long createTime;
    @Field("zone_id")
    private String zoneId;
    @Field("package_id")
    private String packageId;

    /**
     * @return record
     */
    public Map<String, Integer> getRecord() {
        return record;
    }

    /**
     * @param record 要设置的 record
     */
    public void setRecord(Map<String, Integer> record) {
        this.record = record;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

}
