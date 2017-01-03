
package com.talentwalker.game.md.application.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.talentwalker.game.md.core.repository.BaseMongoRepositoryImpl;

@SpringBootApplication(scanBasePackages = {"com.talentwalker.game" })
@EnableSpringDataWebSupport
@EnableMongoRepositories(basePackages = {"com.talentwalker.game" }, repositoryBaseClass = BaseMongoRepositoryImpl.class)
public class AppConfiguration {

}
