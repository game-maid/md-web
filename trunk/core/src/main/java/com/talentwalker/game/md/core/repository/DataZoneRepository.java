/**
 * @Title: DataLogicRepository.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月21日 闫昆
 */

package com.talentwalker.game.md.core.repository;

import java.util.List;

import com.talentwalker.game.md.core.domain.DataZone;

/**
 * @ClassName: DataLogicRepository
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月21日 下午5:50:53
 */

public interface DataZoneRepository extends BaseMongoRepository<DataZone, String> {

    public List<DataZone> findByIsEnable(boolean isEnable);

}
