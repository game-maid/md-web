
package com.talentwalker.game.md.core.repository.statistics;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.statistics.ActiveBaseData;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: ActiveBaseDataReposistory
 * @Description: 活跃存留 每日数据
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月26日 下午7:41:38
 */
public interface ActiveBaseDataReposistory extends BaseMongoRepository<ActiveBaseData, String> {

    @Query("{'$and':[{'date':{$gte:?0}},{'date':{$lt:?1}}]}")
    public List<ActiveBaseData> findByDate(String start, String end);

    public ActiveBaseData findByDate(String date);
}
