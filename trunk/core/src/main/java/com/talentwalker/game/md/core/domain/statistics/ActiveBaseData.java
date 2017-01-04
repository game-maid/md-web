
package com.talentwalker.game.md.core.domain.statistics;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: ActiveBaseData
 * @Description: 活跃存留 数据
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月22日 下午5:02:34
 */
@Document(collection = "statistics_active_base")
public class ActiveBaseData extends BaseDomain {
    public static final String DAY_TOTAL = "zzzdayTotal";
    public static final String ZONE_TOTAL = "zoneTotal";

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 7906217350403956649L;

    /**
     * 日期
     */
    @Indexed(unique = true)
    private String date;

    /**
     * 区服
     * zoneId , packageId, 详细数据
     */
    @Field("zone_data")
    private Map<String, Map<String, ActiveBasePackage>> zoneData;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Map<String, ActiveBasePackage>> getZoneData() {
        if (zoneData == null) {
            zoneData = new TreeMap<>();
        }
        return zoneData;
    }

    public void setZoneData(Map<String, Map<String, ActiveBasePackage>> zoneData) {
        this.zoneData = zoneData;
    }

}
