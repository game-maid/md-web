/**
 * @Title: MongoConfig.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月11日  赵丽宝
 */

package com.talentwalker.game.md.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @ClassName: MongoConfig
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2017年1月11日 下午1:38:56
 */
@Configuration
public class MongoConfiguration {
    @Autowired
    private MongoDbFactory mongoFactory;

    @Autowired
    private MongoMappingContext mongoMappingContext;

    @Bean
    public MappingMongoConverter mongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        // this is my customization
        mongoConverter.setMapKeyDotReplacement("-");
        mongoConverter.afterPropertiesSet();
        return mongoConverter;
    }
}
