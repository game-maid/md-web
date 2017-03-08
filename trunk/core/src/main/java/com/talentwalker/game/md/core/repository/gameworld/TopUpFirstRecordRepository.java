/**
 * @Title: TopUpFirstRecordRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月5日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.gameworld.TopUpFirstRecord;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: TopUpFirstRecordRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月5日 下午4:04:46
 */

public interface TopUpFirstRecordRepository extends BaseMongoRepository<TopUpFirstRecord, String> {

    @Query("{zone_id:{$in:?0}}")
    public List<TopUpFirstRecord> findList(List<String> zoneList);

    @Query("{$and:[{zone_id:{$in:?0}},{create_time:{$lte:?1}}]}")
    public List<TopUpFirstRecord> findList(List<String> zoneList, long time);

    @Query("{$and:[{create_time:{$lte:?0}},{_id:{$in:?1}}]}")
    public List<TopUpFirstRecord> findList(long endLong, Set<String> lordIds);

}
