/**
 * @Title: AdminConfiguration.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月17日  占志灵
 */

package com.talentwalker.game.md.admin.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.talentwalker.game.md.core.repository.BaseMongoRepositoryImpl;

/**
 * @ClassName: AdminConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月17日 下午3:47:45
 */
@SpringBootApplication(scanBasePackages = {"com.talentwalker.game" })
@EnableSpringDataWebSupport
@EnableMongoRepositories(basePackages = {"com.talentwalker.game" }, repositoryBaseClass = BaseMongoRepositoryImpl.class)
public class AdminConfiguration {

}
