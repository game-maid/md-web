
package com.talentwalker.game.md.core.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.talentwalker.game.md.core.repository.support.SearchFilter;

@NoRepositoryBean
public interface BaseMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {
    Long getNextSequence(String name);

    Page<T> findAll(SearchFilter searchFilter);
}
