/**
 * @Title: FriendService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月7日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.gameworld.Friend;
import com.talentwalker.game.md.core.domain.gameworld.FriendStrength;
import com.talentwalker.game.md.core.domain.gameworld.FriendStrengthStatus;
import com.talentwalker.game.md.core.domain.gameworld.League;
import com.talentwalker.game.md.core.domain.gameworld.LeagueLord;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.PrivateMessage;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.gameworld.FriendRepository;
import com.talentwalker.game.md.core.repository.gameworld.FriendStrengthRepository;
import com.talentwalker.game.md.core.repository.gameworld.FriendStrengthStatusRepository;
import com.talentwalker.game.md.core.repository.gameworld.LeagueLordRepository;
import com.talentwalker.game.md.core.repository.gameworld.LeagueRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.repository.gameworld.PrivateMessageRepository;
import com.talentwalker.game.md.core.response.FriendInfo;
import com.talentwalker.game.md.core.response.LordInfo;
import com.talentwalker.game.md.core.sensitiveword.SensitiveWord;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.StringUtils;

/**
 * @ClassName: FriendService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月7日 上午10:58:37
 */
@Service
public class FriendService extends GameSupport {

    @Autowired
    private LordRepository lordRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private LeagueLordRepository leagueLordRepository;
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private PrivateMessageRepository privateMessageRepository;
    @Autowired
    private FriendStrengthRepository friendSendStrengthRepository;
    @Autowired
    private FriendStrengthStatusRepository friendStrengthStatusRepository;
    /**
     * 推荐好友数量
     */
    private static final int RECOMMOND_COUNT = 10;
    private static final int SAVE_DAY = 7;

    /**
     * @Description:推荐好友
     * @throws
     */
    public void recommond() {
        Lord lord = this.getLord();
        List<String> notIn = findFriendIds(lord.getId());
        notIn.add(lord.getId()); // 自己不被推荐
        int count = RECOMMOND_COUNT;
        int level = lord.getLevel();
        int levelLimit = this.getDataConfig().get("expPlayer").getJsonObject().size();
        boolean isLevelLimt = false;
        List<Lord> lords = new ArrayList<>();
        while (true) {
            List<Lord> lordList = lordRepository.findByIdNotInAndLevel(notIn, level,
                    new PageRequest(0, count, new Sort(Direction.DESC, "lastTime")));
            if (lordList != null && lordList.size() > 0) {
                lords.addAll(lordList);
            }
            if (lords.size() >= 10) {
                this.gameModel.addObject("recommond", this.getLordInfo(null, lords));
                break;
            }
            count = count - lordList.size();
            // 推荐等级
            if (isLevelLimt) {
                level = level - 1;
            } else {
                level += 1;
            }
            if (level > levelLimit) {
                level = lord.getLevel() - 1;
                isLevelLimt = true;
            }
        }
    }

    /**
     * @Description:主公当前好友列表
     * @param id
     * @return
     * @throws
     */
    private List<String> findFriendIds(String id) {
        List<Friend> friends = findFriends(id);
        List<String> ids = new ArrayList<>();
        for (Friend friend : friends) {
            ids.add(friend.getRequestId());
        }
        return ids;
    }

    /**
     * @Description:申请加为好友
     * @param id
     * @throws
     */
    public void request(String id) {
        Lord lord = this.getLord();
        String[] minMax = StringUtils.getMinMax(lord.getId(), id);
        Friend friend = friendRepository.findByMinIdAndMaxId(minMax[0], minMax[1]);
        try {
            // 检验好友数量
            checkFriendCount(lord, null);
            checkFriendCount(null, id);
        } catch (GameException e) {
            if (friend != null) {
                friendRepository.delete(friend);
            }
            throw e;
        }
        if (friend == null) {
            friend = new Friend();
            friend.setMinId(minMax[0]);
            friend.setMaxId(minMax[1]);
            friend.setRequestId(lord.getId());
            friend.setRequestedId(id);
            friend.setRequestTime(new Date());
            friend.setStatus(Friend.STATUS_REQUEST);
            friend.setFailureTime(new Date(new Date().getTime() + SAVE_DAY * DateUtils.MILLIS_PER_DAY));
            friendRepository.insert(friend);
        } else if (friend.getRequestId().equals(id)) {
            friend.agree();
            friendRepository.save(friend);
        }
        findFriendInfo(lord);
    }

    /**
     * @Description:同意加为好友
     * @param id
     * @throws
     */
    public void agree(String id) {
        Lord lord = this.getLord();
        String[] minMax = StringUtils.getMinMax(lord.getId(), id);
        Friend friend = friendRepository.findByMinIdAndMaxId(minMax[0], minMax[1]);
        if (friend != null && friend.getRequestedId().equals(lord.getId())) {
            try {
                checkFriendCount(lord, null);
                checkFriendCount(null, id);
                friend.agree();
                friendRepository.save(friend);
                findFriendInfo(lord);
            } catch (GameException e) {
                friendRepository.delete(friend);
                throw e;
            }
        }
    }

    /**
     * @Description:查找好友相关信息（好友列表、请求列表，被请求列表）
     * @param lord
     * @throws
     */
    private void findFriendInfo(Lord lord) {
        List<String> allIds = new ArrayList<>();
        List<Friend> list = friendRepository.findByRequestIdOrRequestedId(lord.getId(), lord.getId());
        for (Friend friend : list) {
            if (friend.getRequestId().equals(lord.getId())) {
                allIds.add(friend.getRequestedId());
            } else {
                allIds.add(friend.getRequestId());
            }
        }
        // 格式好友信息
        Map<String, LordInfo> maps = this.getLordInfo(allIds, null);
        FriendInfo friendInfo = new FriendInfo();
        for (Friend friend : list) {
            if (friend.getStatus() == Friend.STATUS_AGREE) {
                if (friend.getRequestId().equals(lord.getId())) {
                    friendInfo.getFriends().add(maps.get(friend.getRequestedId()));
                } else {
                    friendInfo.getFriends().add(maps.get(friend.getRequestId()));
                }
            } else {
                if (friend.getRequestId().equals(lord.getId())) {
                    friendInfo.getRequesteds().add(maps.get(friend.getRequestedId()));
                } else {
                    friendInfo.getRequests().add(maps.get(friend.getRequestId()));
                }
            }
        }
        this.gameModel.addObject(friendInfo);
    }

    /**
     * @param lords 
     * @Description:
     * @param allIds
     * @return
     * @throws
     */
    private Map<String, LordInfo> getLordInfo(List<String> allIds, List<Lord> lords) {
        Map<String, LordInfo> lordInfoMap = new HashMap<>();
        if (allIds == null) {
            allIds = new ArrayList<>();
            for (Lord lord : lords) {
                allIds.add(lord.getId());
            }
            if (allIds.size() <= 0) {
                return lordInfoMap;
            }
        }
        if (lords == null) {
            lords = lordRepository.findByIdIn(allIds);
            if (lords == null) {
                return lordInfoMap;
            }
        }
        List<LeagueLord> leagueLords = leagueLordRepository.findByIdIn(allIds);
        Map<String, String> leagueLordMap = new HashMap<>();
        List<String> leagueIds = new ArrayList<>();
        for (LeagueLord leagueLord : leagueLords) {
            leagueLordMap.put(leagueLord.getId(), leagueLord.getLeagueId());
            if (!StringUtils.isPrarmsEmpty(leagueLord.getLeagueId())) {
                leagueIds.add(leagueLord.getLeagueId());
            }
        }
        List<League> leagues = leagueRepository.findByIdIn(leagueIds);
        Map<String, String> leagueName = new HashMap<>();
        for (League league : leagues) {
            leagueName.put(league.getId(), league.getName());
        }
        for (Lord lord : lords) {
            LordInfo lordInfo = new LordInfo();
            lordInfo.lordInfo(lord);
            lordInfo.setLeagueName(leagueName.get(leagueLordMap.get(lord.getId())));
            lordInfoMap.put(lord.getId(), lordInfo);
        }
        return lordInfoMap;
    }

    /**
     * @param lord 
     * @Description: 校验朋友数量
     * @param id
     * @param isMy
     * @throws
     */
    private void checkFriendCount(Lord lord, String id) {
        boolean isMy = true;
        if (lord == null) {
            isMy = false;
            lord = lordRepository.findOne(id);
        }
        id = lord.getId();
        int friendMax = this.getDataConfig().get("VIP").get(lord.getVipLevel() + "").getInteger("friendsup");
        List<Friend> friends = findFriends(id);
        if (friends != null && friends.size() >= friendMax) {
            if (isMy) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_37000);
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_37001);
            }
        }
    }

    /**
     * @Description:
     * @param id
     * @return
     * @throws
     */
    private List<Friend> findFriends(String id) {
        return friendRepository.findFriend(id, Friend.STATUS_AGREE);
    }

    /**
     * @Description:
     * @param id
     * @throws
     */
    public void findLordById(String id) {
        Lord lord = lordRepository.findOne(id);
        List<Lord> lords = new ArrayList<>();
        lords.add(lord);
        this.gameModel.addObject("findResult", this.getLordInfo(null, lords));
    }

    /**
     * @Description:
     * @param name
     * @throws
     */
    public void findLordByName(String name) {
        Lord lord = lordRepository.findByName(name);
        List<Lord> lords = new ArrayList<>();
        lords.add(lord);
        this.gameModel.addObject("findResult", this.getLordInfo(null, lords));
    }

    /**
     * @Description:拒绝加好友
     * @param id
     * @throws
     */
    public void refuse(String id) {
        Lord lord = this.getLord();
        String[] minMax = StringUtils.getMinMax(lord.getId(), id);
        Friend friend = friendRepository.findByMinIdAndMaxId(minMax[0], minMax[1]);
        if (friend != null) {
            friendRepository.delete(friend);
            findFriendInfo(lord);
        }
    }

    /**
     * @Description:删除好友
     * @param id
     * @throws
     */
    public void delete(String id) {
        Lord lord = this.getLord();
        String[] minMax = StringUtils.getMinMax(lord.getId(), id);
        Friend friend = friendRepository.findByMinIdAndMaxId(minMax[0], minMax[1]);
        if (friend != null) {
            friendRepository.delete(friend);
            findFriendInfo(lord);
        }
    }

    /**
     * @Description:刷新好友信息
     * @throws
     */
    public void friendInfo() {
        Lord lord = this.getLord();
        findFriendInfo(lord);
    }

    /**
     * @Description:
     * @param lordId
     * @throws
     */
    public void getOneLord(String lordId) {
        Lord lord = lordRepository.findOne(lordId);
        this.gameModel.addObject("FriendLord", lord);
    }

    /**
     * @Description:发送体力
     * @param receiverId
     * @throws
     */
    public void sendStrength(String receiverId) {
        Lord lord = this.getLord();
        // 校验发送
        this.checkSendRepetition(lord, receiverId);

        FriendStrength friendSendStrength = new FriendStrength();
        friendSendStrength.setSenderId(lord.getId());
        friendSendStrength.setReceiverId(receiverId);
        friendSendStrength.setSendTime(new Date());
        friendSendStrength = friendSendStrengthRepository.insert(friendSendStrength);
        // friendStrengthStatus.getFriendStrengths().add(friendSendStrength);
        // friendStrengthStatusRepository.save(friendStrengthStatus);
    }

    /**
     * @Description: 校验一天内重复给同一个主公发送体力
     * @param receiverId
     * @throws
     */
    private void checkSendRepetition(Lord lord, String receiverId) {
        Map<String, Integer> record = lord.getGivesStrengthRecord();
        if (record.containsKey(receiverId)) {
            // 已经发送过体力了
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_37002);
        }
    }

    // /**
    // * @Description:当前发送过的
    // * @param lordId
    // * @return
    // * @throws
    // */
    // private List<String> getTodaySend(String lordId) {
    // List<FriendStrength> list = friendSendStrengthRepository.findReceiversBySenderId(lordId, this.getStartTime(),
    // this.getEndTime());
    // List<String> lords = new ArrayList<>();
    // for (FriendStrength temp : list) {
    // lords.add(temp.getReceiverId());
    // }
    // return lords;
    // }

    /**
     * @Description:
     * @param lordId
     * @throws
     */
    public void getAward(String lordId) {
        Lord lord = this.getLord();
        // 领取状态

        FriendStrengthStatus friendStrengthStatus = friendStrengthStatusRepository.findOne(lord.getId());
        if (friendStrengthStatus == null) {
            friendStrengthStatus = new FriendStrengthStatus();
            friendStrengthStatus.setId(lord.getId());
            friendStrengthStatus.setTimes(0);
            friendStrengthStatus.setGetTime(System.currentTimeMillis());
        }

    }

    private Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    private Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * @Description:获取体力列表
     * @throws
     */
    public void getStrengthList() {
        Lord lord = this.getLord();
        List<FriendStrength> list = friendSendStrengthRepository.findByReceiverId(lord.getId());
        for (FriendStrength strength : list) {

        }
        this.gameModel.addObject("sendStrengthList", list);
    }

    /**
     * @Description: 发送私聊消息
     * @param content  内容
     * @param receiverId 接受着id
     * @throws
     */
    public void sendPrivateMessag(String content, String receiverId) {
        Lord lord = this.getLord();
        // 构造消息
        PrivateMessage message = new PrivateMessage();
        message.setSenderId(lord.getId());
        message.setReceiverId(receiverId);
        message.setContent(SensitiveWord.replaceSensitiveWord(content, '*'));
        message.setSendTime(new Date().getTime());
        privateMessageRepository.insert(message);
    }
}
