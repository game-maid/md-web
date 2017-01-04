/**
 * @Title: LordController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月21日 闫昆
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.LordService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

import net.sf.json.JSONObject;

/**
 * @ClassName: LordController
 * @Description: 主公接口
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月21日 上午9:58:23
 */

@Controller
@RequestMapping(value = "game/lord", method = RequestMethod.POST)
public class LordController extends GameSupport {

    @Autowired
    private LordService lordService;

    /**
     * @Description:校验名称
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("checkName/{name}")
    public GameModel checkName(@PathVariable String name) {
        lordService.checkName(name);
        return this.gameModel;
    }

    /**
     * @Description: 重命名
     * @param name
     * @return
     */
    @GameResponse
    @RequestMapping(value = "rename/{name}")
    public GameModel rename(@PathVariable String name) {
        lordService.rename(name);
        return this.gameModel;
    }

    /**
     * @Description: 编辑签名
     * @param notes
     * @return
     */
    @GameResponse
    @RequestMapping("notes/{notes}")
    public GameModel notes(@PathVariable(value = "notes") String notes) {
        lordService.notes(notes);
        return this.gameModel;
    }

    /**
     * @Description: 编辑签名
     * @param notes
     * @return
     */
    @GameResponse
    @RequestMapping("notes")
    public GameModel changeNotes() {
        lordService.notes("");
        return this.gameModel;
    }

    /**
     * @Description: 修改头像
     * @param headicon
     * @return
     */
    @GameResponse
    @RequestMapping("headicon/{headIcon}")
    public GameModel changeHeadicon(@PathVariable String headIcon) {
        lordService.changeHeadicon(headIcon);
        return this.gameModel;
    }

    /**
     * @Description: 获得头像
     * @param headicon
     * @return
     */
    @GameResponse
    @RequestMapping("getHeadicon")
    public GameModel getHeadicon() {
        lordService.getHeadicon();
        return this.gameModel;
    }

    /**
     * @Description:获得图鉴
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getHandbook")
    public GameModel getHandbook() {
        lordService.getHandbook();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("addStrength")
    public GameModel addStrength(@RequestBody JSONObject items) {
        lordService.addStrength(items);
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("addEnergy")
    public GameModel addEnergy(@RequestBody JSONObject items) {
        lordService.addEnergy(items);
        return this.gameModel;
    }

    /**
     * @Description:钻石购买体力
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("buyStrength")
    public GameModel buyStrength() {
        lordService.buyStrength();
        return this.gameModel;
    }

    @GameResponse
    @RequestMapping("monthCardAward/{cardId}")
    public GameModel monthCardAward(@PathVariable String cardId) {
        lordService.monthCardAward(cardId);
        return this.gameModel;
    }

    /**
     * @Description:领取每日免费体力
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("freeStrength")
    public GameModel freeStrength() {
        lordService.freeStrength();
        return this.gameModel;
    }

    /**
     * @Description:每日免费体力小游戏结束后(折扣)
     * @param time ： 小游戏用时（秒）
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("freeStrengthDiscount/{time}")
    public GameModel freeStrengthEnd(@PathVariable Integer time) {
        lordService.freeStrengthDiscount(time);
        return this.gameModel;
    }

    /**
     * @Description: 在每日体力活动期间，购买折扣体力
     * @param buyTimes 要购买第几次折扣体力
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("buyDiscountStrength/{buyTimes}")
    public GameModel buyDiscountStrength(@PathVariable Integer buyTimes) {
        lordService.buyDiscountStrength(buyTimes);
        return this.gameModel;
    }

    /**
     * @Description:兑换玩家冲级奖励
     * @param level
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("drawLvup/{level}")
    public GameModel drawLvup(@PathVariable Integer level) {
        lordService.drawLvup(level);
        return this.gameModel;
    }

    /**
     * @Description: 黄金钟
     * @param amount 花费钻石的数量
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("goldBell/{amount}")
    public GameModel goldBell(@PathVariable Integer amount) {
        lordService.goldBell(amount);
        return this.gameModel;
    }

    /**
     * @Description:签到 、 补签
     * @param times 签到次数 第一次签到为1
     * @param status 0：签到     1：补签
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("signIn/{times}/{status}")
    public GameModel signIn(@PathVariable Integer times, @PathVariable Integer status) {
        lordService.signIn(times, status);
        return this.gameModel;
    }

    /**
     * @Description:记录新手引导做到第几步了
     * @param step
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("guidanceStep/{step}")
    public GameModel guidanceStep(@PathVariable Integer step) {
        lordService.guidanceStep(step);
        return this.gameModel;
    }

    /**
     * @Description:更新玩家数据
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("updateLord")
    public GameModel updateLord() {
        lordService.updateLord();
        return this.gameModel;
    }

}
