/**
 * @Title: IGameUserService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月2日  占志灵
 */

package com.talentwalker.game.md.core.service;

import com.talentwalker.game.md.core.domain.GameUser;

/**
 * @ClassName: IGameUserService
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月2日 上午11:11:04
 */

public interface IGameUserServiceRemote {
    public GameUser updateGameUserCache(String sessionIdOld, String sessionIdNew);

    public void deleteGameUserCache(String sessionId);

    public void removeAllGameUser(String zoneId);

    public GameUser findGameUserCache(String sessionId);
}
