
package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import com.talentwalker.game.md.core.domain.gameworld.CDKey;
import com.talentwalker.game.md.core.domain.gameworld.CDKeyBatch;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: CDKeyRepository
 * @Description: cdk 兑换码
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月29日 上午9:34:59
 */
public interface CDKeyRepository extends BaseMongoRepository<CDKey, String> {

    public CDKey findByIdAndBatch(String id, CDKeyBatch batch);

    public List<CDKey> findByBatch(CDKeyBatch batch);
}
