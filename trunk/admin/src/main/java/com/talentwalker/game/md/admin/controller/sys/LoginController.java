/**
 * @Title: LoginController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月28日  闫昆
 */

package com.talentwalker.game.md.admin.controller.sys;

import javax.naming.AuthenticationException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.service.sys.MenuService;
import com.talentwalker.game.md.admin.shiro.UserUtil;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: LoginController
 * @Description: 登陆控制器
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月28日 下午3:56:40
 */

@Controller
public class LoginController extends BaseController {

    @Value("${game.admin.title}")
    private String title;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String forwardLogin(Model model) {
        User user = UserUtil.getUser();
        // 如果用户没登出，执行登出逻辑
        if (null != user) {
            SecurityUtils.getSubject().logout();
        }
        model.addAttribute("title", title);
        return "sys/login";
    }

    /**
     * 处理登陆失败逻辑，用户名密码验证委托给shiro处理
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String doLoginFailed(RedirectAttributes redirect) {
        String exception = (String) ServletUtils.getRequest()
                .getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String msg = getMessage("sys.login.fail");
        if (UnknownAccountException.class.getName().equals(exception)) {
            msg = getMessage("sys.login.fail.incorrect");
        } else if (LockedAccountException.class.getName().equals(exception)) {
            msg = getMessage("sys.login.fail.locked");
        } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
            msg = getMessage("sys.login.fail.incorrect");
        } else if (AuthenticationException.class.getName().equals(exception)) {
            msg = getMessage("sys.login.fail");
        }
        redirect.addFlashAttribute("error", msg);
        redirect.addFlashAttribute("title", title);
        // 重定向，避免表单重复提交
        return redirect("login");
    }

    @RequestMapping(value = {"main", "" }, method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("user", UserUtil.getUser());
        model.addAttribute("title", title);
        model.addAttribute("menus", menuService.findMenus());
        return "sys/main";
    }

}
