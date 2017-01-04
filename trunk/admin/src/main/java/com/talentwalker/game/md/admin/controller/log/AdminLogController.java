/**
 * @Title: AdminLogController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.log.Log;
import com.talentwalker.game.md.admin.service.log.AdminLogService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: AdminLog
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月13日 上午10:33:22
 */
@Controller
@RequestMapping(value = "adminLog")
public class AdminLogController extends BaseController {
    @Autowired
    private AdminLogService adminLogService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "log/adminLog";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<Log> list() {
        return adminLogService.findAll();
    }
}
