/**
 * @Title: LeagueController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.LeagueService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: LeagueController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 下午3:16:02
 */
@Controller
@RequestMapping(value = "game/league", method = RequestMethod.POST)
public class LeagueController extends GameSupport {
    @Autowired
    private LeagueService leagueService;

    @GameResponse
    @RequestMapping(value = "main")
    public GameModel main() {
        leagueService.main();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "list")
    public GameModel leagueList() {
        leagueService.leagueList();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "create/{name}")
    public GameModel create(@PathVariable String name) {
        leagueService.create(name);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "checkName/{name}")
    public GameModel checkName(@PathVariable String name) {
        leagueService.checkName(name);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeNotice/{notice}")
    public GameModel changeNotice(@PathVariable String notice) {
        leagueService.changeNotice(notice);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "changeNotice")
    public GameModel changeNoticeNull() {
        leagueService.changeNotice("");
        return this.gameModel;
    }

    /**
     * 
     * @Description:退出联盟
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "exit")
    public GameModel exit() {
        leagueService.exit();
        return this.gameModel;
    }

    /**
     * 
     * @Description:申请加入联盟
     * @param leagueId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "apply/{leagueId}")
    public GameModel apply(@PathVariable String leagueId) {
        leagueService.apply(leagueId);
        return this.gameModel;
    }

    /**
     * 
     * @Description:公会申请人列表
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "applyList")
    public GameModel applyList() {
        leagueService.applyList();
        return this.gameModel;
    }

    /**
     * 
     * @Description:通过申请
     * @param lordId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "passApply/{lordId}")
    public GameModel passApply(@PathVariable String lordId) {
        leagueService.passApply(lordId);
        return this.gameModel;
    }

    /**
     * 
     * @Description:拒绝申请
     * @param lordId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "refusalApply/{lordId}")
    public GameModel refusalApply(@PathVariable String lordId) {
        leagueService.refusalApply(lordId);
        return this.gameModel;
    }

    /**
     * 
     * @Description:清空申请列表
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "emptyApply")
    public GameModel emptyApply() {
        leagueService.emptyApply();
        return this.gameModel;
    }

    /**
     * 
     * @Description:联盟设置
     * @param isAutomate
     * @param limitLevel
     * @param limitVipLevel
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "setApply/{isAutomate}/{limitLevel}/{limitVipLevel}")
    public GameModel setApply(@PathVariable boolean isAutomate, @PathVariable int limitLevel,
            @PathVariable int limitVipLevel) {
        leagueService.setApply(isAutomate, limitLevel, limitVipLevel);
        return this.gameModel;
    }

    /**
     * 
     * @Description:联盟成员信息
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "lordList")
    public GameModel lordList() {
        leagueService.lordList();
        return this.gameModel;
    }

    /**
     * 
     * @Description:捐献
     * @param type 捐献类型，gold捐献，diamond捐献
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "donate/{type}")
    public GameModel donate(@PathVariable String type) {
        leagueService.donate(type);
        return this.gameModel;
    }

    /**
     * 
     * @Description:踢出公会
     * @param lordId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "out/{lordId}")
    public GameModel out(@PathVariable String lordId) {
        leagueService.out(lordId);
        return this.gameModel;
    }

    /**
     * 
     * @Description: 降低职务
     * @param lordId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "dutyLower/{lordId}")
    public GameModel dutyLower(@PathVariable String lordId) {
        leagueService.dutyLower(lordId);
        return this.gameModel;
    }

    /**
     * 
     * @Description:提升职务
     * @param lordId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "dutyUp/{lordId}")
    public GameModel dutyUp(@PathVariable String lordId) {
        leagueService.dutyUp(lordId);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "buy/{goodKey}")
    public GameModel buyGood(@PathVariable Integer goodKey) {
        leagueService.buyGood(goodKey);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping(value = "findByLeague/{leagueId}")
    public GameModel findByLeagueId(@PathVariable String leagueId) {
        leagueService.findByLeagueId(leagueId);
        return this.gameModel;
    }
}
