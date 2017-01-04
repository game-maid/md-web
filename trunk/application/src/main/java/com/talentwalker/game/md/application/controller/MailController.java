/**
 * @Title: MailController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月19日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.MailService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: MailController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月19日 下午12:24:20
 */
@Controller
@RequestMapping(value = "game/mail", method = RequestMethod.POST)
public class MailController extends GameSupport {
    @Autowired
    private MailService mailService;

    @GameResponse
    @RequestMapping("read/{mailId}")
    public GameModel readMail(@PathVariable String mailId) {
        mailService.readMail(mailId);
        return this.gameModel;
    }

    /**
     * @Description:获取所有擂台邮件
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getMailDuel")
    public GameModel getMailDuel() {
        mailService.getMailDuel();
        return this.gameModel;
    }

    /**
    * @Description:获取所有邮件（除擂台外）
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getMail")
    public GameModel getMail() {
        mailService.getMail();
        return this.gameModel;
    }

    /**
     * @Description:领取奖励
     * @param mailId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("rewardMail/{mailId}")
    public GameModel rewardMail(@PathVariable String mailId) {
        mailService.rewardMail(mailId);
        return this.gameModel;
    }

    /**
     * @Description:领取全部邮件奖励
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getRewardAll")
    public GameModel getRewardAll() {
        mailService.rewardMail();
        return this.gameModel;
    }

    /**
     * @Description:领取全部邮件奖励（擂台邮件）
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getRewardDuelAll")
    public GameModel getRewardDuelAll() {
        mailService.getRewardDuelAll();
        return this.gameModel;
    }

    /**
     * @Description:删除一个邮件
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("removeMail/{mailKey}")
    public GameModel removeMail(@PathVariable String mailKey) {
        mailService.removeMail(mailKey);
        return this.gameModel;
    }

    /**
     * @Description:删除所有邮件
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("removeMailAll")
    public GameModel removeMailAll() {
        mailService.removeMailAll();
        return this.gameModel;
    }

    /**
     * @Description:删除所有擂台邮件
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("removeMailDuelAll")
    public GameModel removeMailDuelAll() {
        mailService.removeMailDuelAll();
        return this.gameModel;
    }

}
