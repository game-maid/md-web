/**
 * @Title: LogRepository.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月12日 闫昆
 */

package com.talentwalker.game.md.admin.repository.log;

import com.talentwalker.game.md.admin.domain.log.Log;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: LogRepository
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月12日 下午5:38:27
 */

public interface LogRepository extends BaseMongoRepository<Log, String> {

}
