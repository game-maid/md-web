/**
 * @Title: UserController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月29日  闫昆
 */

package com.talentwalker.game.md.admin.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.service.sys.UserService;
import com.talentwalker.game.md.admin.shiro.UserUtil;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: UserController
 * @Description: 后台用户管理控制器
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月29日 上午11:48:26
 */

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forword() {
        return "sys/user";
    }

    @GameResponse
    @RequiresPermissions("sys:user:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<User> list(HttpServletRequest request, Model model) {
        String condition = request.getParameter("condition");
        if (StringUtils.isEmpty(condition)) {
            return userService.findAll(request);
        } else {
            return userService.findByCondition(request, condition);
        }
    }

    @GameResponse
    @RequestMapping(value = "listAll", method = RequestMethod.POST)
    public Object findList() {
        return userService.findList();
    }

    @GameResponse
    @RequiresPermissions("sys:user:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object addUser(@Valid User user) {
        userService.saveUser(user);
        return null;
    }

    @RequestMapping(value = "changePass", method = RequestMethod.GET)
    public String forwardPass(Model model) {
        User user = UserUtil.getUser();
        model.addAttribute("user", user);
        return "sys/change_pass";
    }

    @GameResponse
    @RequestMapping(value = "changePass", method = RequestMethod.POST)
    public Object changePass(HttpServletRequest request) throws GameException {
        userService.changePass(request);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        userService.delete(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@Valid User user) {
        userService.updateUser(user);
        return null;
    }

}
