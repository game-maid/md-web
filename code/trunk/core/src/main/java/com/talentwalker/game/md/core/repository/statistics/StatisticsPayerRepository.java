
package com.talentwalker.game.md.core.repository.statistics;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.statistics.StatisticsPayer;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

public interface StatisticsPayerRepository extends BaseMongoRepository<StatisticsPayer, String> {

    @Query("{$and:[{lordId:?0},{payTime:{$gt:?1}},{payTime:{$lt:?2}}]}")
    public List<StatisticsPayer> findBylordIdAndPayTime(String id, long start, long end);

}
