/**
 * @Title: PrivateMessageRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月9日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.talentwalker.game.md.core.domain.gameworld.PrivateMessage;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: PrivateMessageRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月9日 上午11:48:37
 */

public interface PrivateMessageRepository extends BaseMongoRepository<PrivateMessage, String> {
    List<PrivateMessage> findByMinIdAndMaxId(String minId, String maxId, Pageable pageable);
}
