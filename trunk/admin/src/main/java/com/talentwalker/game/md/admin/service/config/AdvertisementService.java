/**
 * @Title: AdvertisementService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  张福涛
 */

package com.talentwalker.game.md.admin.service.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.config.AdvertisementConfig;
import com.talentwalker.game.md.core.repository.config.AdvertisementConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

/**
 * @ClassName: AdvertisementService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午6:05:38
 */
@Service
public class AdvertisementService extends BaseService {
    @Autowired
    private AdvertisementConfigRepository advertisementConfigRepository;

    /**
     * @Description:添加修改广告
     * @param advertisementConfig
     * @throws
     */
    public void add(AdvertisementConfig advertisementConfig) {
        System.out.println(advertisementConfig.getId());
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
    public void update(String id, String name, String value) {
        AdvertisementConfig advertisementConfig = advertisementConfigRepository.findOne(id);
        if ("putrush".equals(name)) {
            advertisementConfig.setPutrush(value);
        }
        advertisementConfigRepository.save(advertisementConfig);
    }

}
