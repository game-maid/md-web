/**
 * @Title: EquipController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月22日 闫昆
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.EquipService;
import com.talentwalker.game.md.core.service.gameworld.MissionService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

import net.sf.json.JSONObject;

/**
 * @ClassName: EquipController
 * @Description: 装备
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月22日 下午4:28:55
 */

@Controller
@RequestMapping(value = "game/equip", method = RequestMethod.POST)
public class EquipController extends GameSupport {

    @Autowired
    private EquipService equipService;
    @Autowired
    private MissionService missionService;

    /**
     * @Description 装备精炼
     * @param equipUid
     * @param items 精炼道具
     */
    @GameResponse
    @RequestMapping(value = "refine/{equipUid}")
    public GameModel refine(@PathVariable String equipUid, @RequestBody JSONObject items) {
        equipService.refine(equipUid, items);
        return this.gameModel;
    }

    /**
     * @Description 装备强化
     * @param equipUid
     */
    @GameResponse
    @RequestMapping(value = "strengthen/{equipUid}")
    public GameModel strengthen(@PathVariable String equipUid) {
        equipService.strengthen(equipUid);
        missionService.trigerMissionForEquip(1);
        return this.gameModel;
    }

}
