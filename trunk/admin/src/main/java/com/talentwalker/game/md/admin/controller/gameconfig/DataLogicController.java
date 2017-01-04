/**
 * @Title: DataLogicController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月21日 闫昆
 */

package com.talentwalker.game.md.admin.controller.gameconfig;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.DataZone;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.service.IDataZoneService;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: DataLogicController
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月21日 下午5:58:55
 */

@Controller
@RequestMapping("datalogic")
public class DataLogicController extends BaseController {

    @Resource(name = "dataLogicService")
    private IDataZoneService dataLogicService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "gameconfig/datalogic";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<DataZone> list() {
        return dataLogicService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object save(@Valid DataZone dataLogic) {
        dataLogicService.save(dataLogic);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@Valid DataZone dataLogic) throws GameException {
        dataLogicService.update(dataLogic);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "available", method = RequestMethod.POST)
    public List<DataZone> getAvailable() {
        return dataLogicService.findAvailable();
    }

    @GameResponse
    @RequestMapping(value = "view", method = RequestMethod.POST)
    public List<DataZone> getView() {
        return dataLogicService.showView();
    }

    @GameResponse
    @RequestMapping(value = "zone", method = RequestMethod.POST)
    public List<Map<String, String>> getZone(@RequestParam("id") String id) {
        return dataLogicService.getZoneInfo(id);
    }

    @GameResponse
    @RequestMapping(value = "adjust", method = RequestMethod.POST)
    public Object adjust(String logicId, @RequestParam("zoneIds[]") String[] zoneIds) {
        dataLogicService.adjustZone(logicId, zoneIds);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        try {
            dataLogicService.delete(id);
        } catch (Exception e) {
            GameExceptionUtils.throwException(getMessage(e.getMessage()));
        }
        return null;
    }

}
