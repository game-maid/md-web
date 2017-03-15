/**
 * @Title: ItemDistributionRepository.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月15日  张福涛
 */

package com.talentwalker.game.md.core.repository.statistics;

import com.talentwalker.game.md.core.domain.statistics.ItemDistributionStatistics;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: ItemDistributionRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月15日 下午4:39:12
 */

public interface ItemDistributionRepository extends BaseMongoRepository<ItemDistributionStatistics, String> {

}
