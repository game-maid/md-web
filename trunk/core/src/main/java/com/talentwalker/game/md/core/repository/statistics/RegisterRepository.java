
package com.talentwalker.game.md.core.repository.statistics;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: RegisterRepository
 * @Description: 
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月22日 上午10:39:07
 */
public interface RegisterRepository extends BaseMongoRepository<Register, String> {
    @Query("{$and:[{'register_time':{$gte:?0}},{'register_time':{$lt:?1}},{'zone_id':?2}]}")
    public List<Register> findByRegisterTimeAndZoneId(long start, long end, String zoneId);
}
