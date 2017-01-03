/**
 * @Title: PhysicalSserverController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月30日 闫昆
 */

package com.talentwalker.game.md.admin.controller.gameconfig;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.PhysicalServer;
import com.talentwalker.game.md.core.service.IPhysicalService;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: PhysicalSserverController
 * @Description: 物理服务器
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月30日 上午10:18:48
 */

@Controller
@RequestMapping("physical")
public class PhysicalSserverController extends BaseController {

    @Resource(name = "physicalService")
    private IPhysicalService physicalService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "gameconfig/physical";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<PhysicalServer> list() {
        return physicalService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Object add(@Valid PhysicalServer server) {
        physicalService.save(server);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@Valid PhysicalServer server) {
        physicalService.update(server);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        try {
            physicalService.delete(id);
        } catch (Exception e) {
            GameExceptionUtils.throwException(getMessage(e.getMessage()));
        }
        return null;
    }

    @GameResponse
    @RequestMapping(value = "findOne", method = RequestMethod.POST)
    public PhysicalServer findOne(String id) {
        return physicalService.findOne(id);
    }

}
