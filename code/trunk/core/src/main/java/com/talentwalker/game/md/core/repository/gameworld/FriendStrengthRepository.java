/**
 * @Title: FriendSendStrengthRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月9日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.gameworld.FriendStrength;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: FriendSendStrengthRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月9日 上午11:58:18
 */

public interface FriendStrengthRepository extends BaseMongoRepository<FriendStrength, String> {

    List<FriendStrength> findByReceiverId(String id);

    // @Query("{'$and':[{'getTime':{'$gte':?1}},{'getTime':{ '$lte':?2} },{'senderId':?0}]}")
    // List<FriendStrength> findReceiversBySenderId(String senderId, Long startTime, Long endTime);

    @Query("{'$and':[{'getTime':{'$gte':?1}},{'getTime':{ '$lte':?2} },{'receiverId':?0}]}")
    List<FriendStrength> findReceiversByReceiverId(String receiverId, Long startTime, Long endTime);

}
