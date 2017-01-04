/**
 * @Title: GameUserServiceRemote.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月2日  占志灵
 */

package com.talentwalker.game.md.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.repository.GamePackageRepository;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.PhysicalRepository;

/**
 * @ClassName: GameUserServiceRemote
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月2日 下午3:44:57
 */
@Service
@CacheConfig(cacheResolver = "sessionCacheResolver")
public class GameUserServiceRemote implements IGameUserServiceRemote {
    @Autowired
    private GameUserRepository gameUserRepository;
    @Autowired
    private GameZoneRepository gameZoneRepository;
    @Autowired
    private GamePackageRepository gamePackageRepository;
    @Autowired
    private PhysicalRepository physicalRepository;

    /**.
     * <p>Title: updateGameUserCache</p>
     * <p>Description: </p>
     * @param gameUser
     * @return
     * @see com.talentwalker.game.md.core.service.IGameUserServiceRemote#updateGameUserCache(com.talentwalker.game.md.core.domain.GameUser)
     */
    @Override
    @Caching(evict = {@CacheEvict(key = "#p0", beforeInvocation = true) }, put = {@CachePut(key = "#p1") })
    public GameUser updateGameUserCache(String sessionIdOld, String sessionIdNew) {
        GameUser gameUser = gameUserRepository.findByGamesessionId(sessionIdNew);
        gameUser.setGameZone(gameZoneRepository.findOne(gameUser.getGameZoneId()));
        gameUser.setGamePackage(gamePackageRepository.findOne(gameUser.getPackageId()));
        gameUser.setPhysicalServer(physicalRepository.findOne(gameUser.getPhysicalServerId()));
        return gameUser;
    }

    /**.
     * <p>Title: deleteGameUserCache</p>
     * <p>Description: </p>
     * @param sessionId
     * @see com.talentwalker.game.md.core.service.IGameUserServiceRemote#deleteGameUserCache(java.lang.String)
     */
    @Override
    @CacheEvict(key = "#p0")
    public void deleteGameUserCache(String sessionId) {
    }

    /**.
     * <p>Title: removeAllGameUser</p>
     * <p>Description: </p>
     * @see com.talentwalker.game.md.core.service.IGameUserServiceRemote#removeAllGameUser()
     */
    @Override
    @CacheEvict(allEntries = true)
    public void removeAllGameUser(String zoneId) {
    }

    /**.
     * <p>Title: findGameUserCache</p>
     * <p>Description: </p>
     * @param sessionIdOld
     * @return
     * @see com.talentwalker.game.md.core.service.IGameUserServiceRemote#findGameUserCache(java.lang.String)
     */
    @Override
    @Cacheable(key = "#p0", unless = "#result==null")
    public GameUser findGameUserCache(String sessionId) {
        return null;
    }

}
