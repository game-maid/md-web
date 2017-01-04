/**
 * @Title: ShopRecruitController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.config.ShopRecruitConfigService;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.config.ShopRecruitConfig;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

import net.sf.json.JSONObject;

/**
 * @ClassName: ShopRecruitController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月29日 上午10:44:09
 */
@Controller
@RequestMapping(value = "recruit")
public class ShopRecruitController extends BaseController {
    @Autowired
    private ShopRecruitConfigService shopRecruitService;
    @Autowired
    private IDataConfigManager dataConfigManager;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward(Model model) {
        return "config/shopRecruitConfig";
    }

    @GameResponse
    @RequestMapping(value = "vipList", method = RequestMethod.POST)
    public Set vipList() {
        JSONObject config = dataConfigManager.getTest().get("VIP").getJsonObject();
        return config.keySet();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object addActivityRecruit(ShopRecruitConfig recruit) {
        shopRecruitService.add(recruit);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<ShopRecruitConfig> list() {
        return shopRecruitService.findAll();

    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        shopRecruitService.delete(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String name, Integer value, String pk) throws Exception {
        shopRecruitService.update(pk, name, value);
        return null;
    }
}
