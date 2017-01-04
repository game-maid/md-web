/**
 * @Title: PackageController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月30日 闫昆
 */

package com.talentwalker.game.md.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.GamePackageService;
import com.talentwalker.game.md.core.util.BaseGameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: PackageController
 * @Description: 渠道信息
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月30日 下午5:23:35
 */

@Controller
public class PackageController extends BaseGameSupport {

    @Autowired
    private GamePackageService packageService;

    /**
     * @Description: 根据版本号和账号ssoId获取当前包信息
     * @param packageId 游戏包ID
     * @param ssoId 玩家账号ID
     * @param version 游戏包大版本号
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "portal/getPackageInfo/{packageId}/{version}/{ssoId}", method = RequestMethod.POST)
    public Object getPackageInfo(@PathVariable String packageId, @PathVariable String ssoId,
            @PathVariable String version) {
        return gameModel.addObject("packageInfo", packageService.getPackageInfo(packageId, ssoId, version));
    }

}
