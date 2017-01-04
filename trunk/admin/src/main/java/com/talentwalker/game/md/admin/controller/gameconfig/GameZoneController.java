
package com.talentwalker.game.md.admin.controller.gameconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentwalker.game.md.admin.form.GamezoneForm;
import com.talentwalker.game.md.core.domain.DataZone;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.service.IDataZoneService;
import com.talentwalker.game.md.core.service.IGameZoneService;
import com.talentwalker.game.md.core.service.IPhysicalService;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: GameZoneController
 * @Description: 游戏区服管理
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月17日 下午5:26:47
 */
@Controller
@RequestMapping("gamezone")
public class GameZoneController {

    @Resource(name = "gameZoneService")
    private IGameZoneService gameZoneService;

    @Resource(name = "physicalService")
    private IPhysicalService physicalService;

    @Resource(name = "dataLogicService")
    private IDataZoneService dataLogicService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "gameconfig/gamezone";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<GameZone> list() {
        return gameZoneService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object addGameZone(@Valid GamezoneForm zone) throws GameException {
        DataZone logic = dataLogicService.findOne(zone.getLogicId());
        if (null == logic || !logic.getIsEnable()) {
            GameExceptionUtils.throwException("Add fail");
        }
        GameZone gamezone = new GameZone();
        gamezone.setId(zone.getId());
        gamezone.setName(zone.getName());
        gamezone.setSort(zone.getSort());
        gamezone.setStatus(zone.getStatus());
        gamezone.setType(zone.getType());
        gamezone.setStartTime(zone.getOpenTime().getTime());
        gamezone.setCreateDate(new Date());
        gamezone.setDataLogic(logic);
        gamezone.setPhysicalServer(physicalService.findOne(zone.getPhysicalId()));
        gameZoneService.add(gamezone);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String name, String value, String pk) throws Exception {
        gameZoneService.update(pk, name, value);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        String[] ids = StringUtils.split(id, ",");
        if (null != ids && ids.length > 0) {
            gameZoneService.deleteByIds(ids);
        }
        return null;
    }

    @GameResponse
    @RequestMapping(value = "excludeSubmit", method = RequestMethod.POST)
    public List<GameZone> getExcludeSubmitZone() {
        return gameZoneService.findExcludeType(GameZone.TYPE_AUDIT);
    }

    @GameResponse
    @RequestMapping(value = "online", method = RequestMethod.POST)
    public List<GameZone> getOnlineZone() {
        return gameZoneService.findByType(GameZone.TYPE_ONLINE);
    }

    @GameResponse
    @RequestMapping(value = "findOne", method = RequestMethod.POST)
    public GameZone findOne(String id) {
        return gameZoneService.findOne(id);
    }

    /**
     * @Description:根据id,查询区服的名称
     * @param ids
     * @return
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "findNamesByIdIn", method = RequestMethod.POST)
    public Map<String, String> findNamesByIdIn(@RequestParam(value = "ids[]") String[] ids) {
        List<GameZone> list = gameZoneService.findByIdIn(new ArrayList<String>(Arrays.asList(ids)));
        Map<String, String> response = new HashMap<String, String>();
        for (GameZone gameZone : list) {
            response.put(gameZone.getId(), gameZone.getName());
        }
        return response;
    }

}
