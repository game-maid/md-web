/**
 * @Title: GameUserService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月23日  占志灵
 */

package com.talentwalker.game.md.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.PhysicalServer;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.PhysicalRepository;
import com.talentwalker.game.md.core.util.RemoteServiceUtils;
import com.talentwalker.game.md.core.util.UuidUtils;

/**
 * @ClassName: GameUserService
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月23日 下午12:28:39
 */
@Service
public class GameUserService {
    @Autowired
    private GameUserRepository gameUserRepository;
    @Autowired
    private PhysicalRepository physicalRepository;
    @Autowired
    protected IDataConfigManager dataConfigManager;

    /**
     * 
     * @Description:门户登录；生成sessionid，通知上次的物理服务器去掉GameUser缓存，通知新的物理服务器缓存新的GameUser缓存
     * @param gameZone
     * @param platformId
     * @param packageId
     * @param clientVersion
     * @param ssoId
     * @return
     * @throws
     */
    public GameUser userLogin(GameZone gameZone, String platformId, String packageId, String clientVersion,
            String ssoId) {
        String physicServerIdNew = gameZone.getPhysicalServer().getId();
        GameUser user = gameUserRepository.findDistinctBySsoIdAndPlatformIdAndGameZoneId(ssoId, platformId,
                gameZone.getId());
        if (user == null) {
            user = new GameUser();
            Long userId = gameUserRepository.getNextSequence("userid");
            user.setId(userId + "");
            user.setPlatformId(platformId);
            user.setSsoId(ssoId);
            user.setGameZoneId(gameZone.getId());
        }
        String physicServerIdOld = user.getPhysicalServerId();
        String sessionIdOld = user.getGamesessionId();
        user.setClientVersion(clientVersion);
        user.setPackageId(packageId);
        user.setPhysicalServerId(physicServerIdNew);
        user.setGamesessionId(gameZone.getId() + "-" + UuidUtils.getUuid());
        user = gameUserRepository.save(user);
        String sessionIdNew = user.getGamesessionId();

        // 刷新物理服缓存
        IGameUserServiceRemote userServiceNew = (IGameUserServiceRemote) RemoteServiceUtils.getRemoteService(
                gameZone.getPhysicalServer().getHost(), gameZone.getPhysicalServer().getPort(),
                gameZone.getPhysicalServer().getContext(), IGameUserServiceRemote.class, "/remote/gameUserService");

        if (physicServerIdOld == null || physicServerIdNew.equals(physicServerIdOld)) {
            if (sessionIdOld == null) {
                sessionIdOld = sessionIdNew;
            }
            userServiceNew.updateGameUserCache(sessionIdOld, sessionIdNew);
        } else {
            PhysicalServer serverOld = physicalRepository.findOne(physicServerIdOld);
            IGameUserServiceRemote userServiceOld = (IGameUserServiceRemote) RemoteServiceUtils.getRemoteService(
                    serverOld.getHost(), serverOld.getPort(), serverOld.getContext(), IGameUserServiceRemote.class,
                    "/remote/gameUserService");
            try {
                userServiceOld.deleteGameUserCache(sessionIdOld);
            } catch (Throwable e) {

            }
            userServiceNew.updateGameUserCache(sessionIdOld, sessionIdNew);
        }

        return user;
    }

    /**
     * 
     * @Description:根据负载均衡算法，获得物理服务器；目前简单的通过配置获得
     * @param gameZone
     * @return
     * @throws
     */
    public PhysicalServer getPhysicalService(GameZone gameZone) {
        return gameZone.getPhysicalServer();
    }

    /**
     * 
     * @Description:根据玩家选择的区服、客户端当前版本号，获取最新的配置
     * @param gameZone
     * @return
     * @throws
     */
    public DataConfig getDataConfig(GameZone gameZone, String configVersion) {
        DataConfig dataConfig = null;
        String configVersionNow = null;
        int gameZoneType = gameZone.getType();
        if (gameZoneType == GameZone.TYPE_TEST) {
            dataConfig = dataConfigManager.getTest();
        } else if (gameZoneType == GameZone.TYPE_AUDIT) {
            dataConfig = dataConfigManager.getSubmit();
        } else if (gameZoneType == GameZone.TYPE_ONLINE) {
            dataConfig = dataConfigManager.getOnline();

        }
        configVersionNow = gameZoneType + "." + dataConfig.getVersion();
        if (configVersion.equals(configVersionNow)) {
            dataConfig = null;
        }
        return dataConfig;
    }

}
