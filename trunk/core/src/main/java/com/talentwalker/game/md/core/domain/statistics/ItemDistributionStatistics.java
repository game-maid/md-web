/**
 * @Title: ItemDistributionStatistics.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月15日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: ItemDistributionStatistics
 * @Description: 道具分布统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月15日 下午3:27:59
 */
@Document(collection = "statistics_item_distribution")
public class ItemDistributionStatistics extends BaseDomain {

}
