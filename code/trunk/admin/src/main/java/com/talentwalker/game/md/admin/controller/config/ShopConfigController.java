/**
 * @Title: ShopController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.config.ShopConfigService;
import com.talentwalker.game.md.core.domain.config.ShopConfig;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ShopController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月29日 下午5:40:11
 */
@Controller
@RequestMapping(value = "shopConfig")
public class ShopConfigController extends BaseController {
    @Autowired
    private ShopConfigService shopService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward(Model model) {
        return "config/shopConfig";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<ShopConfig> list() {
        return shopService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(ShopConfig shop) {
        shopService.add(shop);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        shopService.delete(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String name, Integer value, String pk) throws Exception {
        shopService.update(pk, name, value);
        return null;
    }
}
