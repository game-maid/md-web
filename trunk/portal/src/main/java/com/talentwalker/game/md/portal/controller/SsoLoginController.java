/**
 * @Title: SsoLoginController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月17日  占志灵
 */

package com.talentwalker.game.md.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.PhysicalServer;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.service.GamePackageService;
import com.talentwalker.game.md.core.service.GameUserService;
import com.talentwalker.game.md.core.service.GameZoneService;
import com.talentwalker.game.md.core.util.BaseGameSupport;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: SsoLoginController
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月17日 下午7:43:49
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class SsoLoginController extends BaseGameSupport {
    @Autowired
    protected GameUserService gameUserService;
    @Autowired
    protected GamePackageService gamePackageService;
    @Autowired
    protected GameZoneService gameZoneService;

    /**
     * @Description:门户登录接口
     * @param gameZoneId 玩家所选区服id
     * @param packageId 玩家应用包id
     * @param clientVersion 客户端版本号
     * @param configVersion 客户端当前配置版本号
     * @param ssoId 玩家平台账号id
     * @return 最新配置、物理服务器信息、游戏登录用的sessionId
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "portal/ssoLogin/{gameZoneId}/{packageId}/{clientVersion}/{configVersion}/{ssoId}")
    public Object ssoLogin(@PathVariable String gameZoneId, @PathVariable String packageId,
            @PathVariable String clientVersion, @PathVariable String configVersion, @PathVariable String ssoId) {
        GamePackage packageInfo = gamePackageService.findOneNotNull(packageId);
        GameZone gameZone = gameZoneService.findOneNotNull(gameZoneId);
        if (gameZone.getStatus() == GameZone.STATUS_RECOVERING) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10003);
        }

        // 根据负载均衡算法，获取物理服务器
        PhysicalServer gameServer = gameUserService.getPhysicalService(gameZone);
        // 门户登录，生成sessionId
        GameUser user = gameUserService.userLogin(gameZone, packageInfo.getPlatform().getId(), packageId, clientVersion,
                ssoId);
        // 获得最新配置
        DataConfig dataConfig = gameUserService.getDataConfig(gameZone, configVersion);

        return gameModel.addObject("sessionId", user.getGamesessionId()).addObject("gameServer", gameServer)
                .addObject("dataConfig", dataConfig.getJsonObject()).addObject("dataConfigVersion",
                        dataConfig == null ? configVersion : gameZone.getType() + "." + dataConfig.getVersion());
    }

    // @ResponseBody
    // @RequestMapping(value = "portal/ssoLoginOut")
    // public void ssoLoginOut(HttpServletRequest request) {
    // request.getSession().invalidate();
    // }
    //
    // @ResponseBody
    // @RequestMapping(value = "portal/testSession")
    // public void testSession(HttpServletRequest request) {
    // System.out.println(request.getSession().getAttribute("sessionId"));
    // }
}
