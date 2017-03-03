/**

 * @Title: NoticeService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月3日  张福涛
 */

package com.talentwalker.game.md.admin.service.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.config.NoticeConfig;
import com.talentwalker.game.md.core.repository.config.NoticeConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: NoticeService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月3日 上午10:41:24
 */
@Service
public class NoticeService extends BaseService {
    @Autowired
    private NoticeConfigRepository noticeConfigRepository;

    /**
     * @Description:添加修改广告
     * @param advertisementConfig
     * @throws
     */
    public void add(NoticeConfig noticeConfig) {
        if (StringUtils.isEmpty(noticeConfig.getId())) {
            String id = "Notice_" + System.currentTimeMillis()
                    + com.talentwalker.game.md.core.util.StringUtils.getRandomLowerStr(4);
            noticeConfig.setId(id);
        } else {
            NoticeConfig noticeConfigUpdate = noticeConfigRepository.findOne(noticeConfig.getId());
            noticeConfig.setVersion(noticeConfigUpdate.getVersion());
        }
        noticeConfigRepository.save(noticeConfig);
    }

    /**
     * @Description:分页查询
     * @return
     * @throws
     */
    public Page<NoticeConfig> findList() {
        SearchFilter searchFilter = SearchFilter.newSearchFilter();
        Page<NoticeConfig> page = noticeConfigRepository.findAll(searchFilter);
        return page;
    }

    /**
     * @Description:根据id删除
     * @param id
     * @throws
     */
    public void deleteById(String id) {
        noticeConfigRepository.delete(id);
    }

    /**
     * @Description:更新
     * @param id
     * @param name
     * @param value
     * @throws
     */
    public void update(String id, String name, String value) {
        NoticeConfig noticeConfig = noticeConfigRepository.findOne(id);
        if ("putrush".equals(name)) {
            noticeConfig.setPutrush(value);
        }
        noticeConfigRepository.save(noticeConfig);
    }
}
