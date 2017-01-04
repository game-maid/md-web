/**
 * @Title: GameLogRepository.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月15日 闫昆
 */

package com.talentwalker.game.md.core.repository;

import com.talentwalker.game.md.core.domain.GameLog;

/**
 * @ClassName: GameLogRepository
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月15日 下午1:22:54
 */

public interface GameLogRepository extends BaseMongoRepository<GameLog, String> {

}
