
package com.talentwalker.game.md.application.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.VIPService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: VIPController
 * @Description: VIP特权
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月2日 下午2:43:05
 */
@Controller
@RequestMapping(value = "game/vip", method = RequestMethod.POST)
public class VIPController extends GameSupport {
    @Resource
    private VIPService vipService;

    /**
     * @Description:获取每日vip奖励和vip等级奖励列表
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("list")
    public GameModel list() {
        vipService.list();
        return this.gameModel;
    }

    /**
     * @Description:领取每日vip奖励
     * @param level
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("draw/dailyReward/{vipLevel}")
    public GameModel drawDailyReward(@PathVariable int vipLevel) {
        vipService.drawDailyReward(vipLevel);
        return this.gameModel;
    }

    /**
     * @Description:领取vip等级奖励
     * @param level
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("draw/levelReward/{vipLevel}")
    public GameModel drawLevelReward(@PathVariable int vipLevel) {
        vipService.drawlevelReward(vipLevel);
        return this.gameModel;
    }
}
