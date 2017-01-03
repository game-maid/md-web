/**
 * @Title: MenuRepository.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月7日 闫昆
 */

package com.talentwalker.game.md.admin.repository.sys;

import java.util.List;

import com.talentwalker.game.md.admin.domain.sys.Menu;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: MenuRepository
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月7日 下午11:44:33
 */

public interface MenuRepository extends BaseMongoRepository<Menu, String> {

    List<Menu> findByIsEnable(boolean isEnable);

    List<Menu> findByIdIn(List<String> ids);

    Menu findById(String id);

    List<Menu> findByParentId(String id);

}
