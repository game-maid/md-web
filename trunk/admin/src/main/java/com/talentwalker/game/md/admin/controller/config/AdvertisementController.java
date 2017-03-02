/**
 * @Title: AdvertisementController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  张福涛
 */

package com.talentwalker.game.md.admin.controller.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;

/**
 * @ClassName: AdvertisementController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午2:20:10
 */
@RequestMapping(value = "advertisement", method = RequestMethod.GET)
@Controller
public class AdvertisementController extends BaseController {

    @RequestMapping(value = "list")
    public String list() {
        return "config/advertisement";
    }
}
