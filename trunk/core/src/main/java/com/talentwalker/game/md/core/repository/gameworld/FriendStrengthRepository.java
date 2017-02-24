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

    // List<FriendStrength> findByReceiverIdAndSendDate(String id, String sendDate);

    List<FriendStrength> findBySenderIdAndSendDate(String id, String nowDateStr);

    @Query("{'$and':[{'sendTime':{'$gte':?1}},{'sendTime':{ '$lte':?2} },{'senderId':?0}]}")
    List<FriendStrength> findBySenderId(String senderId, Long startTime, Long endTime);

    @Query("{'$and':[{'sendTime':{'$gte':?1}},{'sendTime':{ '$lte':?2} },{'receiverId':?0}]}")
    List<FriendStrength> findByReceiverId(String receiverId, Long startTime, Long endTime);

    @Query("{'$and':[{'sendTime':{'$gte':?2}},{'sendTime':{ '$lte':?3} },{'receiverId':?0},{'senderId':?1}]}")
    List<FriendStrength> findByReceiverIdAndSenderId(String receiverId, String senderId, Long startTime, Long endTime);

    @Query("{'$and':[{'sendTime':{'$gte':?2}},{'receiverId':?0},{'senderId':?1}]}")
    FriendStrength findByReceiverIdAndSenderIdAndSendDate(String receiverId, String senderId, String sendDate);

}
