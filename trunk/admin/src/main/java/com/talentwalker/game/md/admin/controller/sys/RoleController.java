/**
 * @Title: RoleController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月6日 闫昆
 */

package com.talentwalker.game.md.admin.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.sys.Role;
import com.talentwalker.game.md.admin.service.sys.RoleService;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: RoleController
 * @Description: 后台角色管理控制器
 * @author <a href="mailto:yankun@talentwalker.com">闫昆</a> 于 2016年5月6日 上午11:55:34
 */

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "sys/role";
    }

    @GameResponse
    @RequiresPermissions("sys:role:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<Role> list(HttpServletRequest request) {
        String condition = request.getParameter("condition");
        if (StringUtils.isEmpty(condition)) {
            return roleService.findAll(request);
        } else {
            return roleService.findByCondition(request, condition);
        }
    }

    @GameResponse
    @RequestMapping(value = "available", method = RequestMethod.GET)
    public List<Role> getAvailableRole() {
        return roleService.findAvailableRole();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(@Valid Role role) throws GameException {
        roleService.saveRole(role);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        roleService.deleteOneById(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "pickList", method = RequestMethod.POST)
    public Object update(String id) {
        return roleService.getPickList(id);
    }

    @GameResponse
    @RequestMapping(value = "updataAllByRole", method = RequestMethod.POST)
    public Object updataAllByRole(@RequestBody @RequestParam("data[]") List<String> data, String id) {
        roleService.updataAllByRole(data, id);
        return null;
    }
}
