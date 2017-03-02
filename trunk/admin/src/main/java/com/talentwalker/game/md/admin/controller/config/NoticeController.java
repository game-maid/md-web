
/**
 * @Title: NoticeController.java
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
 * @ClassName: NoticeController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午2:19:37
 */
@Controller
@RequestMapping(value = "notice", method = RequestMethod.GET)
public class NoticeController extends BaseController {
    /**
     * @Description:页面跳转
     * @return
     * @throws
     */
    @RequestMapping(value = "list")
    public String list() {
        return "config/notice";
    }

}
