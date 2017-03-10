/**
 * @Title: NoticeService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月3日  张福涛
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.config.NoticeConfig;
import com.talentwalker.game.md.core.repository.config.NoticeConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: NoticeService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月3日 下午7:01:00
 */
@Service
public class NoticeService extends GameSupport {
    @Autowired
    private NoticeConfigRepository noticeConfigRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

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
    public void update(String id, String name, int value) {
        NoticeConfig noticeConfig = noticeConfigRepository.findOne(id);
        if ("putrush".equals(name)) {
            noticeConfig.setPutrush(value);
        }
        noticeConfigRepository.save(noticeConfig);
    }

    /**
     * @Description:查询合适的公告
     * @return
     * @throws
     */
    public Object main() {
        GameUser gameUser = getGameUser();
        String zoneId = gameUser.getGameZone().getId();
        Query query = new Query();
        query.addCriteria(
                new Criteria().orOperator(Criteria.where("allZone").is(true), Criteria.where("zoneList").in(zoneId)));
        query.addCriteria(Criteria.where("startLong").lte(System.currentTimeMillis()));
        query.addCriteria(Criteria.where("endLong").gt(System.currentTimeMillis()));
        query.addCriteria(Criteria.where("state").is(true));
        query.with(new Sort(Direction.ASC, "putrush"));
        List<NoticeConfig> list = mongoTemplate.find(query, NoticeConfig.class);
        this.gameModel.addObject(ResponseKey.NOTICE, list);
        return this.gameModel;
    }
}
