/**
 * @Title: UserRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月29日  闫昆
 */

package com.talentwalker.game.md.admin.repository.sys;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: UserRepository
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月29日 上午11:06:16
 */

public interface UserRepository extends BaseMongoRepository<User, String> {

    Page<User> findByUsernameOrName(String username, String name, Pageable pageable);

    User findByUsername(String username);

    User findById(String id);

    @Query("{'role_list.$id':?0}")
    List<User> findByRole(ObjectId roleId);

}
