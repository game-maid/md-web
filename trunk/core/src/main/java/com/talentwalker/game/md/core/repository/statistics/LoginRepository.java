
package com.talentwalker.game.md.core.repository.statistics;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.statistics.Login;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: LoginRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月22日 上午11:09:45
 */
public interface LoginRepository extends BaseMongoRepository<Login, String> {

    @Query("{'$and':[{'lordId':?0},{'loginTime':{'$gt':?1}},{'loginTime':{'$lt':?2}}]}")
    List<Login> findByLordId(String lordId, Long startTime, Long endTime);
}
