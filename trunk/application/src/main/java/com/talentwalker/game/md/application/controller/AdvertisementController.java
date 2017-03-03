/**
 * @Title: AdvertisementController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月3日  张福涛
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.AdvertisementService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: AdvertisementController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月3日 下午6:55:57
 */
@Controller
@RequestMapping(value = "game/advertisement", method = RequestMethod.POST)
public class AdvertisementController extends GameSupport {
    @Autowired
    private AdvertisementService advertisementService;

    /**
     * @Description:获取广告信息
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "main")
    public Object main() {
        advertisementService.main();
        return this.gameModel;
    }
}
