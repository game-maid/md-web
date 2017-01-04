/**
 * @Title: shopRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月2日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.config;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.config.ShopConfig;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: shopRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月2日 下午1:18:11
 */

public interface ShopConfigRepository extends BaseMongoRepository<ShopConfig, ObjectId> {
    /**
     * @param zoneId 
     * @param time 
     * @Description: @Query("{'end_time':{'$gt':?0}}")
     * @throws
     */

    @Query("{'$and':[{'end_time':{'$gte':?0}},{'start_time':{ '$lte':?0} },{'limit_vip':{'$lte':?1}}]}")
    List<ShopConfig> findByDate(long time, int vipLevel);

}
