/**
 * @Title: InstallController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月9日 闫昆
 */

package com.talentwalker.game.md.admin.controller.sys;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.repository.sys.MenuRepository;
import com.talentwalker.game.md.admin.repository.sys.UserRepository;
import com.talentwalker.game.md.core.response.GameResponseEntity;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: InstallController
 * @Description: 初始化安装，主要初始化菜单和一个管理员用户，未完成
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月9日 下午10:08:14
 */

@Controller
public class InstallController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @GameResponse
    @RequestMapping(value = "install", method = RequestMethod.GET)
    public Object install() {
        User user = new User();
        user.setCreateDate(new Date());
        user.setIsEnable(true);
        user.setIsSuper(true);
        user.setUsername("admin");
        user.setPassword("943896f1a27320ea72875ae188a19d10");
        user.setSalt("uUM8HART");
        userRepository.save(user);

        return GameResponseEntity.EMPTY;
    }

}
