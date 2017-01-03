/**
 * @Title: LeagueRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import com.talentwalker.game.md.core.domain.gameworld.League;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: LeagueRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 下午3:23:48
 */

public interface LeagueRepository extends BaseMongoRepository<League, String> {
    // public League findById(String id);
    public List<League> findByApplicantIn(String id);

    public League findByName(String id);

    public List<League> findByIdIn(List<String> leagueIds);
}
