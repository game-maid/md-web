/**
 * @Title: FriendController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月7日  赵丽宝
 */

package com.talentwalker.game.md.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.service.gameworld.FriendService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: FriendController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月7日 上午11:04:26
 */
@Controller
@RequestMapping(value = "game/friend", method = RequestMethod.POST)
public class FriendController extends GameSupport {
    @Autowired
    private FriendService friendService;

    /**
     * @Description:推荐好友
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "recommond")
    public GameModel recommond() {
        friendService.recommond();
        return this.gameModel;
    }

    /**
     * @Description:通过id查找
     * @param id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "findById/{id}")
    public GameModel findLordById(@PathVariable String id) {
        friendService.findLordById(id);
        return this.gameModel;
    }

    /**
     * @Description:通过名字查找
     * @param name
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "findByName/{name}")
    public GameModel findByName(@PathVariable String name) {
        friendService.findLordByName(name);
        return this.gameModel;

    }

    /**
     * @Description:请求加好友
     * @param id 被请求者id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "request/{id}")
    public GameModel request(@PathVariable String id) {
        friendService.request(id);
        return this.gameModel;
    }

    /**
     * @Description:同意加好友
     * @param id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "agree/{id}")
    public GameModel agree(@PathVariable String id) {
        friendService.agree(id);
        return this.gameModel;
    }

    /**
     * @Description:拒绝加好友
     * @param id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "refuse/{id}")
    public GameModel refuse(@PathVariable String id) {
        friendService.refuse(id);
        return this.gameModel;
    }

    /**
     * @Description:删除好友
     * @param id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "delete/{id}")
    public GameModel delete(@PathVariable String id) {
        friendService.delete(id);
        return this.gameModel;
    }

    /**
     * @Description:刷新好友相关信息
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "findFriendInfo")
    public GameModel findFriendInfo() {
        friendService.friendInfo();
        return this.gameModel;
    }

    /**
     * @Description:一个主公信息
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "oneLord/{lordId}")
    public GameModel getOneLord(@PathVariable String lordId) {
        friendService.getOneLord(lordId);
        return this.gameModel;
    }

    /**
     * @Description: 赠送体力
     * @param receiverId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("send/strength/{receiverId}")
    public GameModel sendStrength(@PathVariable String receiverId) {
        friendService.sendStrength(receiverId);
        return this.gameModel;
    }

    /**
     * @Description: 可领取体力列表
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getStrengthList")
    public GameModel getStrengthList() {
        friendService.getStrengthList(this.getLord());
        return this.gameModel;
    }

    /**
     * @Description: 领取体力
     * @param id 体力赠送唯一键值id
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("getAward/{id}")
    public GameModel getAward(@PathVariable String id) {
        friendService.getAward(id);
        return this.gameModel;
    }

    /**
     * @Description: 发送私有消息
     * @param content
     * @param receiverId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("send/private/{content}/{receiverId}")
    public GameModel sendPrivateMessag(@PathVariable String content, @PathVariable String receiverId) {
        friendService.sendPrivateMessag(content, receiverId);
        return this.gameModel;
    }

    /**
     * @Description:刷新和某人的聊天记录
     * @param friendId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "findPrivateMessages/{friendId}/{count}")
    public Object findPrivateMessages(@PathVariable String friendId, @PathVariable Integer count) {
        friendService.findPrivateMessages(friendId, count);
        return this.gameModel;
    }

    /**
     * @Description: 发送世界消息
     * @param content
     * @param receiverId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("send/public/{content}")
    public GameModel sendPrivateMessag(@PathVariable String content) {
        friendService.sendPublicMessag(content);
        return this.gameModel;
    }

    /**
     * @Description: 发送世界消息
     * @param content
     * @param receiverId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("findPublicMessage/{count}")
    public GameModel findPublicMessag(@PathVariable Integer count) {
        friendService.findPublicMessage(count);
        return this.gameModel;
    }

    /**
     * @Description: 世界最新消息
     * @param content
     * @param receiverId
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("findPublicMessage/now")
    public GameModel findPublicMessage() {
        friendService.findPublicMessage();
        return this.gameModel;
    }

}
