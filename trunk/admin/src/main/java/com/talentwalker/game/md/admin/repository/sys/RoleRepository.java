/**
 * @Title: RoleRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月4日  闫昆
 */

package com.talentwalker.game.md.admin.repository.sys;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.admin.domain.sys.Role;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: RoleRepository
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年5月4日 上午10:13:47
 */

public interface RoleRepository extends BaseMongoRepository<Role, String> {

    public List<Role> findByIdIn(List<String> roleList);

    public Role findById(String id);

    public Page<Role> findByIdOrName(String id, String name, Pageable pageable);

    public List<Role> findByIsEnable(boolean isEnable);

    @Query("{'menu_list.$id':?0}")
    public List<Role> findByMenuId(ObjectId id);

}
