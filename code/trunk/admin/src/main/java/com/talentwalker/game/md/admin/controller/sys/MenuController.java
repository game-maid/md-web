/**
 * @Title: MenuController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月7日 闫昆
 */

package com.talentwalker.game.md.admin.controller.sys;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.sys.Menu;
import com.talentwalker.game.md.admin.service.sys.MenuService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: MenuController
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月7日 下午11:42:56
 */

@Controller
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @RequiresPermissions("sys:menu:list")
    public String forward(Model model) {
        model.addAttribute("menus", menuService.findAll());
        return "sys/menu";
    }

    @ResponseBody
    @RequestMapping(value = "treeList", method = RequestMethod.GET)
    public List<Map<String, Object>> treeList() {
        return menuService.treeList();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(@Valid Menu menu) {
        menuService.saveMenu(menu);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        menuService.delete(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "pickList", method = RequestMethod.POST)
    public Object pickList(String id) {
        return menuService.getPickList(id);
    }

    @GameResponse
    @RequestMapping(value = "updataRoleByMenu", method = RequestMethod.POST)
    public Object updataRoleByMenu(@RequestBody @RequestParam("data[]") List<String> data, String id) {
        menuService.updataRoleByMenu(data, id);
        return null;
    }
}
