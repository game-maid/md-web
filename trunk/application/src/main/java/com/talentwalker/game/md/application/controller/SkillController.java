/**
 * @Title: SkillController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月27日 闫昆
 */

package com.talentwalker.game.md.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.MissionService;
import com.talentwalker.game.md.core.service.gameworld.SkillService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: SkillController
 * @Description: 技能接口
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月27日 下午5:13:03
 */

@Controller
@RequestMapping(value = "game/skill", method = RequestMethod.POST)
public class SkillController extends GameSupport {

    @Autowired
    private SkillService skillService;
    @Autowired
    private MissionService missionService;

    /**
     * @Description 技能升级
     * @param skillUid
     * @param cost 升级技能需要的道具列表
     */
    @GameResponse
    @RequestMapping(value = "addExp/{skillUid}")
    public GameModel addSkillExp(@PathVariable String skillUid, @RequestBody List<String> cost) {
        skillService.addExp(skillUid, cost);
        missionService.trigerMissionForSkill(1);
        return this.gameModel;
    }

}
