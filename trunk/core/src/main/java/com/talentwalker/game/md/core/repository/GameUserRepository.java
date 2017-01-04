/**
 * @Title: GameUserRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月23日  占志灵
 */

package com.talentwalker.game.md.core.repository;

import java.util.List;

import com.talentwalker.game.md.core.domain.GameUser;

/**
 * @ClassName: GameUserRepository
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月23日 下午12:27:04
 */

public interface GameUserRepository extends BaseMongoRepository<GameUser, String> {

    public GameUser findDistinctBySsoIdAndPlatformIdAndGameZoneId(String ssoId, String platformId, String gameZoneId);

    public GameUser findByGamesessionId(String gamesessionId);

    public List<GameUser> findByGameZoneIdIn(List<String> zoneId);

    public List<GameUser> findByIdIn(List<String> ids);

    public List<GameUser> findBySsoId(String ssoId);
}
