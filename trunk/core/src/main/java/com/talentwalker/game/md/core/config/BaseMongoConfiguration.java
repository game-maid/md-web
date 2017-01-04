
package com.talentwalker.game.md.core.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoAction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import com.mongodb.WriteConcern;

@Configuration
public class BaseMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter converter)
            throws UnknownHostException {
        MongoTemplate MongoTemplate = new MongoTemplate(mongoDbFactory, converter);
        MongoTemplate.setWriteConcernResolver(new WriteConcernResolver() {
            @Override
            public WriteConcern resolve(MongoAction action) {
                return WriteConcern.JOURNALED;
            }
        });
        MongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return MongoTemplate;
    }
}
