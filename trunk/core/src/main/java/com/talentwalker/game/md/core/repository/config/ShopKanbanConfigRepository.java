/**
 * @Title: ShopKanbanConfigRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月18日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.config;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.config.ShopKanbanConfig;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: ShopKanbanConfigRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月18日 下午5:27:03
 */

public interface ShopKanbanConfigRepository extends BaseMongoRepository<ShopKanbanConfig, ObjectId> {

    @Query("{'$and':[{'end_time':{'$gte':?0}},{'start_time':{ '$lte':?0} }]}")
    List<ShopKanbanConfig> findByDate(long time);

    @Query("{$or:[{$and:[{end_time:{$gte:?0}},{end_time:{$lte:?1}}]},{$and:[{start_time:{$gte:?0}},{start_time:{$lte:?1}}]}]}")
    List<ShopKanbanConfig> findByDate(long start, long end);
}
