/**
 * @Title: GameLogService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月15日 闫昆
 */

package com.talentwalker.game.md.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GameLog;
import com.talentwalker.game.md.core.repository.GameLogRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: GameLogService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月15日 下午2:57:37
 */

@Service
public class GameLogService {

    @Autowired
    private GameLogRepository gamelogRepository;

    public Page<GameLog> findAll() {
        SearchFilter filter = SearchFilter.newSearchFilter(new Sort(Direction.DESC, "requestTime"));
        return gamelogRepository.findAll(filter);
    }

}
