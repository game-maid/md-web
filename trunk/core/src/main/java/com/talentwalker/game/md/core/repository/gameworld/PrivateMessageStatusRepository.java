
package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import com.talentwalker.game.md.core.domain.gameworld.PrivateMessageStatus;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: PrivateMessageStatusRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月3日 下午12:02:11
 */
public interface PrivateMessageStatusRepository extends BaseMongoRepository<PrivateMessageStatus, String> {
    public List<PrivateMessageStatus> findByReceiverIdAndUnReadCountGreaterThanEqual(String receiverId, int count);
}
