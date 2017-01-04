/**
 * @Title: leagueLordRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import com.talentwalker.game.md.core.domain.gameworld.LeagueLord;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: leagueLordRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 下午3:21:51
 */

public interface LeagueLordRepository extends BaseMongoRepository<LeagueLord, String> {
    public List<LeagueLord> findByIdIn(List<String> ids);

    public List<LeagueLord> findByDutyAndIdIn(int duty, List<String> ids);
}
