
package com.talentwalker.game.md.admin.controller.config;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.config.ShopKanBanConfigService;
import com.talentwalker.game.md.core.domain.config.ShopKanbanConfig;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: KanBanShopController
 * @Description: 看板娘商店，出售看板娘皮肤
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月18日 下午1:20:42
 */
@Controller
@RequestMapping("kanBan")
public class ShopKanBanController extends BaseController {
    @Resource
    private ShopKanBanConfigService shopKanBanConfigService;

    /**
     * @Description:跳转页面到看板娘
     * @param model
     * @return
     * @throws
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward(Model model) {
        return "config/shopKanBanConfig";
    }

    /**
     * @Description:拿到看板娘活动列表
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<ShopKanbanConfig> list() {
        return shopKanBanConfigService.findList();
    }

    /**
     * @Description:初始化看板娘皮肤数据
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public List<Map<String, Object>> initInput() {
        return shopKanBanConfigService.initConfig();
    }

    /**
     * @Description:添加看板娘活动
     * @param kanBan
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(ShopKanbanConfig kanBan) {
        shopKanBanConfigService.add(kanBan);
        return null;
    }

    /**
     * @Description:删除
     * @param id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object deleteById(String id) {
        shopKanBanConfigService.deleteById(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String name, Integer value, String pk) throws Exception {
        shopKanBanConfigService.update(pk, name, value);
        return null;
    }
}
