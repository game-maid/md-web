/**
 * @Title: IPlatformService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月24日 闫昆
 */

package com.talentwalker.game.md.core.service;

import java.util.List;

import com.talentwalker.game.md.core.domain.Platform;

/**
 * @ClassName: IPlatformService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月24日 下午4:04:49
 */

public interface IPlatformService {

    public List<Platform> findAll();

    public Platform save(Platform platform);

    public Platform update(Platform platform);

    public List<Platform> showView();

    public void delete(String id);

    public Platform findOne(String id);

}
