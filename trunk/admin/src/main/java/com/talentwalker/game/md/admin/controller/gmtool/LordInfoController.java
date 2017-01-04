/**
 * @Title: LordInfoController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月27日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.gmtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.gmtool.LordInfoService;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.StageComposite;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: LordInfoController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月27日 下午1:45:55
 */
@Controller
@RequestMapping(value = "lordInfo")
public class LordInfoController extends BaseController {
    @Autowired
    private LordInfoService lordInfoService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward(Model model) {
        return "gmtool/lordInfo";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Lord list(Integer type, String lordId, String gamezone) {
        return lordInfoService.find(type, lordId, gamezone);
    }

    @GameResponse
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public GameUser user(String id) {
        return lordInfoService.findUser(id);
    }

    @GameResponse
    @RequestMapping(value = "stage", method = RequestMethod.POST)
    public StageComposite stage(String playerId) {
        return lordInfoService.getStageInfo(playerId);
    }

    /**
     * @Description:根据玩家id,查询玩家信息
     * @param id
     * @return
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "findNamesByIds", method = RequestMethod.GET)
    public Map<String, String> findNamesByIds(@RequestParam(value = "ids[]") String[] ids) {
        List<Lord> list = lordInfoService.findByIdIn(new ArrayList<String>(Arrays.asList(ids)));
        Map<String, String> response = new HashMap<String, String>();
        for (Lord lord : list) {
            response.put(lord.getId(), lord.getName());
        }
        return response;
    }
}
