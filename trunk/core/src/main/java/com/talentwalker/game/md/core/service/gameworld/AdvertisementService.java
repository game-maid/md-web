/**
 * @Title: AdvertisementService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月3日  张福涛
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.config.AdvertisementConfig;
import com.talentwalker.game.md.core.repository.config.AdvertisementConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: AdvertisementService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月3日 下午7:00:00
 */
@Service
public class AdvertisementService extends GameSupport {
    @Autowired
    private AdvertisementConfigRepository advertisementConfigRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IDataConfigManager configManager;

    /**
     * @Description:获取广告信息
     * @return
     * @throws
     */
    public void main() {
        GameUser gameUser = getGameUser();
        String zoneId = gameUser.getGameZone().getId();
        Query query = new Query();
        query.addCriteria(
                new Criteria().orOperator(Criteria.where("allZone").is(true), Criteria.where("zoneList").in(zoneId)));
        query.addCriteria(Criteria.where("startLong").lte(System.currentTimeMillis()));
        query.addCriteria(Criteria.where("endLong").gt(System.currentTimeMillis()));
        query.addCriteria(Criteria.where("state").is(true));
        query.with(new Sort(Direction.ASC, "putrush"));
        List<AdvertisementConfig> list = mongoTemplate.find(query, AdvertisementConfig.class);
        this.gameModel.addObject(ResponseKey.ADVERTISEMENT, list);
    }

    /**
     * @Description:添加修改广告
     * @param advertisementConfig
     * @throws
     */
    public void add(AdvertisementConfig advertisementConfig) {
        if (StringUtils.isEmpty(advertisementConfig.getId())) {
            String id = "AD_" + System.currentTimeMillis()
                    + com.talentwalker.game.md.core.util.StringUtils.getRandomLowerStr(4);
            advertisementConfig.setId(id);
        } else {
            AdvertisementConfig advertisementConfigUpdate = advertisementConfigRepository
                    .findOne(advertisementConfig.getId());
            advertisementConfig.setVersion(advertisementConfigUpdate.getVersion());
        }
        advertisementConfigRepository.save(advertisementConfig);
    }

    /**
     * @Description:分页查询
     * @return
     * @throws
     */
    public Page<AdvertisementConfig> findList() {
        SearchFilter searchFilter = SearchFilter.newSearchFilter();
        return advertisementConfigRepository.findAll(searchFilter);
    }

    /**
     * @Description:根据id删除
     * @param id
     * @throws
     */
    public void deleteById(String id) {
        advertisementConfigRepository.delete(id);
    }

    /**
     * @Description:更新
     * @param id
     * @param name
     * @param value
     * @throws
     */
    public void update(String id, String name, int value) {
        AdvertisementConfig advertisementConfig = advertisementConfigRepository.findOne(id);
        if ("putrush".equals(name)) {
            advertisementConfig.setPutrush(value);
        }
        advertisementConfigRepository.save(advertisementConfig);
    }

    /**
     * @Description:读取跳转路径配置
     * @return
     * @throws
     */
    public Object stageConfig() {
        DataConfig dataConfig = configManager.getTest().get(ConfigKey.ADVERTISEMENT_JUMPOUT);
        Set<String> keySet = dataConfig.getJsonObject().keySet();
        Map<String, String> resultMap = new HashMap<>();
        for (String key : keySet) {
            DataConfig keyConfig = dataConfig.get(key);
            String name = keyConfig.getString(ConfigKey.ADVERTISEMENT_JUMPOUT_NAME);
            String value = keyConfig.getString(ConfigKey.ADVERTISEMENT_JUMPOUT_TYPE);
            resultMap.put(name, value);
        }
        return resultMap;
    }
}
