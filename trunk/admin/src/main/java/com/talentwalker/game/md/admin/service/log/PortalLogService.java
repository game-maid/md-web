/**
 * @Title: PortalLogService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月28日 闫昆
 */

package com.talentwalker.game.md.admin.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.PortalLog;
import com.talentwalker.game.md.core.repository.PortalLogRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: PortalLogService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月28日 下午5:40:54
 */

@Service
public class PortalLogService extends BaseService {

    @Autowired
    private PortalLogRepository portalLogRepository;

    public Page<PortalLog> findAll() {
        SearchFilter filter = SearchFilter.newSearchFilter(new Sort(Direction.DESC, "requestTime"));
        return portalLogRepository.findAll(filter);
    }

}
