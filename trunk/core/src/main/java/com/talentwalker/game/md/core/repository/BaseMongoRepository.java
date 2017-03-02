
package com.talentwalker.game.md.core.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.talentwalker.game.md.core.repository.support.SearchFilter;

@NoRepositoryBean
public interface BaseMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {
    Long getNextSequence(String name);

    Page<T> findAll(SearchFilter searchFilter);

    List<T> findAllList(Pageable pageable);
}
