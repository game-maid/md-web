/**
 * @Title: LordRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: LordRepository
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月13日 下午1:33:46
 */

public interface LordRepository extends BaseMongoRepository<Lord, String> {
    public List<Lord> findByIdIn(List<String> ids);

    public Lord findByName(String name);

    public List<Lord> findByIdNotInAndLevel(Collection<String> notInIds, int level, Pageable pageable);

}
