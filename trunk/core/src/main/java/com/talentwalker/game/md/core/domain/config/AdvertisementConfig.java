/**
 * @Title: AdvertisementConfig.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  张福涛
 */

package com.talentwalker.game.md.core.domain.config;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: AdvertisementConfig
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午4:32:20
 */
@Document(collection = "config_advertisement")
public class AdvertisementConfig extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 1L;

}
