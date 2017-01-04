/**
 * @Title: IApplyPackageService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月24日  赵丽宝
 */

package com.talentwalker.game.md.core.service;

import java.util.List;

import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.exception.GameException;

/**
 * @ClassName: IApplyPackageService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年5月24日 下午5:58:33
 */

public interface IGamePackageService {

    public List<GamePackage> findAll();

    public GamePackage save(GamePackage applyPackage);

    public GamePackage update(String name, String value, String pk) throws GameException;

    public List<GamePackage> findAvailable();

    public GamePackage findOne(String id);

    public GamePackage updata(GamePackage applyPackage);

    public GamePackage updataWhite(String id, String uids);

    public GamePackage updataConfigSet(String id, String configs);

    public GamePackage deleteOneUid(String id, String uid);

    public GamePackage deleteOneVersion(String id, String version);

    public void delete(String id);

}
