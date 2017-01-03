/**
 * @Title: UserForTgame.java
 * @Copyright (C) 2016 太能沃可
 * @Description: token验证
 * @Revision History:
 * @Revision 1.0 2016年12月6日  赵丽宝
 */

package com.talentwalker.game.md.portal.tgame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.repository.GameUserRepository;

/**
 * @ClassName: UserForTgame
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月6日 上午10:34:02
 */
@Controller
@RequestMapping(value = "portal/", method = RequestMethod.GET)
public class UserForTgame {
    @Autowired
    GameUserRepository gameUserRepository;

    /**
     * @Description:token验证 （后续加入）
     * @param userId
     * @param accessToken
     * @param packageId
     * @return
     * @throws
     */
    @RequestMapping("verifyToken")
    public Object verifToken(String userId, String accessToken, String packageId) {
        String requestUrl = CashShopForTgame.server.get(packageId);
        return null;
    }
}
