
package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import org.springframework.data.domain.Page;

import com.talentwalker.game.md.core.domain.gameworld.Feedback;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: FeedbackRepository
 * @Description: 反馈
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月22日 上午11:53:21
 */
public interface FeedbackRepository extends BaseMongoRepository<Feedback, String> {
    /**
     * @Description:根据玩家Id查询
     * @param id
     * @return
     * @throws
     */
    List<Feedback> findByPlayerId(String id);

    /**
     * @Description:分页，排序，查询
     * @return
     * @throws
     */
    Page<Feedback> findAll(SearchFilter filter);
}
