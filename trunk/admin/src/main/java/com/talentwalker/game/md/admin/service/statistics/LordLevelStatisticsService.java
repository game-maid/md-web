/**
 * @Title: LordLevelStatisticsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月22日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.statistics.LordLevel;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: LordLevelStatisticsService
 * @Description: 玩家等级统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月22日 下午6:13:22
 */
@Service
public class LordLevelStatisticsService {
    /**
     * 全部用户
     */
    private static final String ALL_USER = "allUser";
    /**
     * 活跃用户
     */
    private static final String ACTIVE_USER = "active_user";
    /**
     * 新用户
     */
    private static final String NEW_USER = "newUser";

    /**
     * @Description:分页查询玩家等级
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @return
     * @throws
     */
    public Page<LordLevel> findPage(String userType, Long startLong, Long endLong, String[] zoneArr) {
        List<LordLevel> content = new ArrayList<>();
        long total = 0L;
        String lords = queryLordIds(userType, startLong, endLong, zoneArr);
        Pageable pageable = SearchFilter.newSearchFilter().getPageable();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * @Description:根据查询条件，查询符合条件的玩家id
     * @param userType
     * @param startLong
     * @param endLong
     * @param zoneArr
     * @return
     * @throws
     */
    private String queryLordIds(String userType, Long startLong, Long endLong, String[] zoneArr) {
        StringBuilder sb = new StringBuilder();
        if (ACTIVE_USER.equals(userType)) {// 活跃用户

        } else if (NEW_USER.equals(userType)) {// 新注册用户

        }
        return sb.toString();
    }

}
