
package com.talentwalker.game.md.admin.controller.gmtool;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.gameworld.CDKey;
import com.talentwalker.game.md.core.service.gameworld.CDKeyService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: CDKeyController
 * @Description: cdk 
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月29日 下午1:28:57
 */
@Controller
@RequestMapping("cdk")
public class CDKeyController extends BaseController {

    @Resource
    private CDKeyService cDKeyService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "gmtool/cDKey";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<CDKey> list() {
        return cDKeyService.findAll();
    }

    /**
     * @Description:作废一个
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "cancellationOne", method = RequestMethod.POST)
    public Object cancellationOne(String id) {
        cDKeyService.cancellation(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "cancellationAll", method = RequestMethod.POST)
    public Object cancellationAll(String batchId, String cdkId) {
        cDKeyService.cancellationAll(batchId, cdkId);
        return null;
    }

}
