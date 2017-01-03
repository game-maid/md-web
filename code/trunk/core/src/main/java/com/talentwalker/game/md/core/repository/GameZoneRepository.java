
package com.talentwalker.game.md.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.GameZone;

public interface GameZoneRepository extends BaseMongoRepository<GameZone, String> {

    @Query("{'data_logic.$id':?0}")
    public List<GameZone> findByLogicId(String logicId);

    public List<GameZone> findByType(int type);

    public List<GameZone> findByTypeNotIn(int type);

    public List<GameZone> findByIdIn(List<String> ids);

    @Query("{'physical_server.$id':?0}")
    public List<GameZone> findByPhysicalId(ObjectId physicalId);

}
