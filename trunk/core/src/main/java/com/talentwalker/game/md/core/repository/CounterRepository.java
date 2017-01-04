/**
 * @Title: CounterRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月30日  占志灵
 */

package com.talentwalker.game.md.core.repository;

import com.talentwalker.game.md.core.domain.Counter;

/**
 * @ClassName: CounterRepository
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月30日 下午5:46:03
 */

public interface CounterRepository extends BaseMongoRepository<Counter, String> {

}
