/**
 * @Title: GameZoneController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月17日 占志灵
 */

package com.talentwalker.game.md.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.GameZoneService;
import com.talentwalker.game.md.core.util.BaseGameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: GameZoneController
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月17日 下午12:25:24
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class GameZoneController extends BaseGameSupport {

    @Autowired
    private GameZoneService gameZoneService;

    /**
     * @Description: 获取游戏区服列表、历史登录区服列表
     * @param version 客户端大版本号
     * @param ssoId 玩家sso账号唯一ID
     * @return 
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "portal/getGameZoneList/{packageId}/{version}/{ssoId}")
    public GameModel getGameZoneList(@PathVariable String packageId, @PathVariable String ssoId,
            @PathVariable String version) {
        gameModel.addObject("zoneList", gameZoneService.getZoneList(packageId, ssoId, version));
        gameModel.addObject("usedZoneIdList", gameZoneService.getUsedZoneIds(ssoId));
        return gameModel;
    }

}
