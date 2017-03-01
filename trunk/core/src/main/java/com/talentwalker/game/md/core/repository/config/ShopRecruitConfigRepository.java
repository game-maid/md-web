/**
 * @Title: ShopRecruitRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月1日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.config;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.config.ShopRecruitConfig;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: ShopRecruitRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月1日 下午12:02:30
 */

public interface ShopRecruitConfigRepository extends BaseMongoRepository<ShopRecruitConfig, ObjectId> {

    /**
     * @param zoneId 
     * @param time 
     * @Description:,,{'zone_list':{'$in':?1}}
     * @throws
     */
    @Query("{'$and':[{'end_time':{'$gte':?0}},{'start_time':{ '$lte':?0} },{'limit_vip':{'$lte':?1}}]}")
    List<ShopRecruitConfig> findByDate(long time, int vipLevel);

    @Query("{'$and':[{'end_time':{'$gte':?0}},{'start_time':{ '$lte':?0}},{'state':" + true + "}]}")
    List<ShopRecruitConfig> findByDate(long time);

}
