/**
 * @Title: PortalLogController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月28日 闫昆
 */

package com.talentwalker.game.md.admin.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.log.PortalLogService;
import com.talentwalker.game.md.core.domain.PortalLog;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: PortalLogController
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月28日 下午5:42:06
 */

@Controller
@RequestMapping("portallog")
public class PortalLogController extends BaseController {

    @Autowired
    private PortalLogService portalLogService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "log/portallog";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<PortalLog> list() {
        return portalLogService.findAll();
    }

}
