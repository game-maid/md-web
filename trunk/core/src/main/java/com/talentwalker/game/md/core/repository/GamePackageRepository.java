/**
 * @Title: ApplyPackageRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月24日  赵丽宝
 */

package com.talentwalker.game.md.core.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.core.domain.GamePackage;

/**
 * @ClassName: ApplyPackageRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年5月24日 下午5:51:37
 */

public interface GamePackageRepository extends BaseMongoRepository<GamePackage, String> {

    @Query("{'platform.$id':?0}")
    public List<GamePackage> findById(String platformId);

}
