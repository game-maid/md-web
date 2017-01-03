/**
 * @Title: ClientSettingController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月17日  占志灵
 */

package com.talentwalker.game.md.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ClientSettingController
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月17日 下午7:34:42
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class ClientSettingController {
    /**
     * @Description: 获取渠道设置信息
     * @param version 客户端大版本号
     * @param ssoId 玩家sso账号唯一ID
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "portal/getChannelSetting/{ssoId}")
    public Object getChannelSetting(@RequestHeader String versionBig, @PathVariable String ssoId) {
        return null;
    }
}
