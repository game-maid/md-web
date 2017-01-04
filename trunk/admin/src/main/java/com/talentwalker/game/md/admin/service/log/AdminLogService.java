/**
 * @Title: AdminLogService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.domain.log.Log;
import com.talentwalker.game.md.admin.repository.log.LogRepository;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: AdminLogService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月13日 下午1:50:14
 */
@Service
public class AdminLogService extends BaseService {
    @Autowired
    private LogRepository logRepository;

    /**
     * @Description:
     * @param request
     * @param condition
     * @return
     * @throws
     */
    public Page<Log> findAll() {
        Sort sort = new Sort(Direction.DESC, "createTime");
        SearchFilter searchFiter = SearchFilter.newSearchFilter(sort);
        return logRepository.findAll(searchFiter);
    }

}
