/**
 * @Title: LeagueNameRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import com.talentwalker.game.md.core.domain.gameworld.LeagueName;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: LeagueNameRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 下午5:06:24
 */

public interface LeagueNameRepository extends BaseMongoRepository<LeagueName, String> {
    public LeagueName findByName(String name);
}
