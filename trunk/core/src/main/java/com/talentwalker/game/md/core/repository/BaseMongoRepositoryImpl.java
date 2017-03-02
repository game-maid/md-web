
package com.talentwalker.game.md.core.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.talentwalker.game.md.core.domain.Counter;
import com.talentwalker.game.md.core.repository.support.SearchFilter;

@NoRepositoryBean
public class BaseMongoRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID>
        implements BaseMongoRepository<T, ID> {
    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    public BaseMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    /**.
     * <p>Title: getNextSequence</p>
     * <p>Description: </p>
     * @param name
     * @return
     * @see com.talentwalker.game.framework.repository.BaseMongoRepository#getNextSequence(java.lang.String)
     */
    @Override
    public Long getNextSequence(String name) {
        Query query = new Query(Criteria.where("_id").is(name));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        Counter counter = mongoOperations.findAndModify(query, update, options, Counter.class);
        return counter.getSeq();
    }

    /**
     * @Title: findAll
     * @Description:
     * @param searchFilter
     * @return
     * @see com.talentwalker.game.framework.repository.BaseMongoRepository#findAll(com.talentwalker.game.framework.repository.support.SearchFilter)
     */
    @Override
    public Page<T> findAll(SearchFilter searchFilter) {
        Query query = new Query();
        query.with(searchFilter.getPageable());
        for (Criteria criteria : searchFilter.getCriteriaList()) {
            query.addCriteria(criteria);
        }
        Long count = mongoOperations.count(query, entityInformation.getJavaType(),
                entityInformation.getCollectionName());
        List<T> list = mongoOperations.find(query, entityInformation.getJavaType(),
                entityInformation.getCollectionName());
        return new PageImpl<T>(list, searchFilter.getPageable(), count);
    }

    /**.
     * <p>Title: findAllList</p>
     * <p>Description: </p>
     * @param pageable
     * @return
     * @see com.talentwalker.game.md.core.repository.BaseMongoRepository#findAllList(org.springframework.data.domain.Pageable)
     */
    @Override
    public List<T> findAllList(Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        List<T> list = mongoOperations.find(query, entityInformation.getJavaType(),
                entityInformation.getCollectionName());
        return list;
    }

}
