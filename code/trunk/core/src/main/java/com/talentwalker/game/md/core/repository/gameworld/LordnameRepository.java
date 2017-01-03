/**
 * @Title: LordnameRepository.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月27日 闫昆
 */

package com.talentwalker.game.md.core.repository.gameworld;

import com.talentwalker.game.md.core.domain.gameworld.Lordname;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: LordnameRepository
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月27日 下午1:58:24
 */

public interface LordnameRepository extends BaseMongoRepository<Lordname, String> {
    public Lordname findByNameAndDataLogicId(String name, String logicId);
}
