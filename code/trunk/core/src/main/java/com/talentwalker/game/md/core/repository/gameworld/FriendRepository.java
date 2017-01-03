/**
 * @Title: FriendRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月7日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.gameworld.Friend;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: FriendRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月7日 上午11:34:52
 */

public interface FriendRepository extends BaseMongoRepository<Friend, String> {

    @Query("{'$or':[{'minId':?0},{'maxId':?0}],'status':?1}")
    public List<Friend> findFriend(String id, int status);

    Friend findByMinIdAndMaxId(String minId, String maxId);

    List<Friend> findByRequestIdOrRequestedId(String requestId, String requestedId);

}
