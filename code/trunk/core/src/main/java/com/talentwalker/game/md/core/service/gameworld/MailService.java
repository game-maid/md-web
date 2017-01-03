/**
 * @Title: MailService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月23日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.LeagueLord;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.DuelRepository;
import com.talentwalker.game.md.core.repository.gameworld.LeagueLordRepository;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: MailService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月23日 下午2:54:51
 */
@Service
public class MailService extends GameSupport {
    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private DuelRepository duelRepository;
    @Autowired
    private LeagueLordRepository leagueLordRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @Description: 批量发送邮件
     * @param goalId 发送目标
     * @param mailKey 邮件对应key
     * @param paramet 翻译字段中替换文字
     * @param reward 奖励
     * @param mailType 邮件类型
     * @throws
     */
    public void sendMails(List<String> goalIds, String mailKey, List<Object> paramet, Map<String, Integer> reward,
            int mailType, List<Mail> mails) {
        Mail mail = this.initMail(mailKey, paramet, reward, mailType);
        for (String goalId : goalIds) {
            mail.setPlayerId(goalId);
            mails.add(mail);
        }
    }

    /**
     * @Description: 发送邮件
     * @param goalId 发送目标
     * @param mailKey 邮件对应key
     * @param paramet 翻译字段中替换文字
     * @param reward 奖励
     * @param mailType 邮件类型
     * @throws
     */
    public void sendMail(String goalId, String mailKey, List<Object> paramet, Map<String, Integer> reward,
            int mailType) {
        // 校验数量上限
        this.checkTypeLimitAmount(mailType, goalId);
        Mail mail = this.initMail(mailKey, paramet, reward, mailType);
        mail.setPlayerId(goalId);
        mailRepository.save(mail);
        if (mail.getType() == Mail.TYPE_PVP) {
            this.getMailDuel();
        } else {
            this.getMail();
        }
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    private Mail initMail(String mailKey, List<Object> paramet, Map<String, Integer> reward, int mailType) {
        Mail mail = new Mail();
        if (paramet == null) {
            paramet = new ArrayList<>();
        }
        mail.setParamet(paramet);
        if (reward == null) {
            reward = new HashMap<>();
        }
        mail.setReward(reward);
        mail.setType(mailType);
        DataConfig config = this.getDataConfig().get("NPCmailsent").get(mailKey);
        String title = config.getString("title");
        String sentNPC = config.getString("sentNPC");
        String iconID = config.getString("iconID");
        String desp = config.getString("desp");
        if (config.getJsonObject().containsKey("reward")) {
            mail.setReward(config.get("reward").getJsonObject());
        }
        mail.setCreateTime(System.currentTimeMillis());
        mail.setSender(sentNPC);
        mail.setSenderHeadIcon(iconID);
        mail.setSendTime(new Date());
        mail.setStatus(Mail.STATUS_UNREAD);
        mail.setTitle(title);
        mail.setContent(desp);
        if (config.getJsonObject().containsKey("mailset")) {
            mail.setFailureTime(
                    new Date(System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY * config.getInteger("mailset")));
        }
        return mail;
    }

    /**
    * 
    * @Description:后台邮件
    * @param mail
    * @throws
    */
    public void sendMailAdmin(Mail mail) {
        mail.setCreateTime(System.currentTimeMillis());
        mail.setSendTime(new Date());
        mail.setStatus(Mail.STATUS_UNREAD);
        mail.setType(Mail.TYPE_ADMIN);
        mailRepository.save(mail);
    }

    /**
     * @Description:阅读邮件
     * @param mailId
     * @throws
     */
    public void readMail(String mailId) {
        Mail mail = mailRepository.findOne(mailId);
        if (mail.getStatus() == Mail.STATUS_UNREAD) {
            mail.setStatus(Mail.STATUS_READ); // 已读
            mailRepository.save(mail);
        }
        if (mail.getType() == Mail.TYPE_PVP) {
            this.getMailDuel();
        } else {
            this.getMail();
        }
    }

    /**
     * @Description:领取邮件奖励
     * @param mailId
     * @throws
     */
    public void rewardMail(String mailId) {
        Lord lord = this.getLord();
        Mail mail = mailRepository.findOne(mailId);
        if (mail == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32000, "邮件不存在");
        }
        if (mail.getStatus() == Mail.STATUS_REWARD) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32001, "已经被领取");
        }
        Map<String, Integer> reward = mail.getReward();
        if (reward == null || reward.size() <= 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32002, "该邮件不是奖励邮件");
        }
        this.getReward(lord, reward);
        mail.setStatus(Mail.STATUS_REWARD);
        mailRepository.save(mail);
        lordRepository.save(lord);
        if (mail.getType() == Mail.TYPE_PVP) {
            this.getMailDuel();
        } else {
            this.getMail();
        }
    }

    /**
     * @Description:
     * @throws
     */
    public void getMailDuel() {
        List<Mail> mails = this.getMailPVP();
        Map<String, Object> map = new HashMap<>();
        map.put("mail", mails);
        this.gameModel.addObject(ResponseKey.DUEL, map);
    }

    /**
     * @Description:
     * @throws
     */
    public List<Mail> getMailNCP() {
        Lord lord = this.getLord();
        List<Mail> mails = mailRepository.findByPlayerIdAndTypeNot(lord.getId(), Mail.TYPE_PVP);
        if (mails == null) {
            mails = new ArrayList<>();
        }
        return mails;
    }

    public List<Mail> getMailPVP() {
        Lord lord = this.getLord();
        List<Mail> mails = mailRepository.findByPlayerIdAndType(lord.getId(), Mail.TYPE_PVP);
        if (mails == null) {
            mails = new ArrayList<>();
        }
        return mails;
    }

    /**
     * @Description:
     * @throws
     */
    public void getMail() {
        List<Mail> mails = this.getMailNCP();
        this.gameModel.addObject(ResponseKey.MAIL, mails);
    }

    /**
     * @Description:
     * @throws
     */
    public void rewardMail() {
        Lord lord = this.getLord();
        List<Mail> mails = this.getMailNCP();
        List<String> mailList = new ArrayList<String>();
        for (Mail mail : mails) {
            if (mail.getStatus() == Mail.STATUS_REWARD) {
                continue;
            }
            Map<String, Integer> reward = mail.getReward();
            if (reward == null || reward.size() > 0) {
                if (this.getRewardNotEroor(lord, reward)) {
                    mail.setStatus(Mail.STATUS_REWARD);
                    mailList.add(mail.getId());
                }
            }
        }
        mailRepository.save(mails);
        lordRepository.save(lord);
        this.getMail();
        this.gameModel.addObject("record", mailList);
    }

    /**
     * @Description:
     * @throws
     */
    public void getRewardDuelAll() {
        Lord lord = this.getLord();
        List<Mail> mails = this.getMailPVP();
        List<String> mailList = new ArrayList<String>();
        for (Mail mail : mails) {
            if (mail.getStatus() == Mail.STATUS_REWARD) {
                continue;
            }
            Map<String, Integer> reward = mail.getReward();
            if (reward == null || reward.size() > 0) {
                if (this.getRewardNotEroor(lord, reward)) {
                    mail.setStatus(Mail.STATUS_REWARD);
                    mailList.add(mail.getId());
                }
            }
        }
        mailRepository.save(mails);
        lordRepository.save(lord);
        this.getMailDuel();
        this.gameModel.addObject("record", mailList);
    }

    /**
     * @Description:
     * @throws
     */
    public void removeMailAll() {
        List<Mail> mails = this.getMailNCP();
        List<Mail> removeList = new ArrayList<>();
        for (Mail mail : mails) {
            if (mail.getStatus() == Mail.STATUS_UNREAD) {
                continue;
            }
            if (mail.getStatus() == Mail.STATUS_READ) {
                if (mail.getReward() != null && mail.getReward().size() > 0) {
                    continue;
                }
            }
            removeList.add(mail);
        }
        if (removeList.size() > 0) {
            mailRepository.delete(removeList);
        }
        this.getMail();

    }

    /**
     * @Description:
     * @throws
     */
    public void removeMailDuelAll() {
        List<Mail> mails = this.getMailPVP();
        List<Mail> removeList = new ArrayList<>();
        for (Mail mail : mails) {
            if (mail.getStatus() == Mail.STATUS_UNREAD) {
                continue;
            }
            if (mail.getStatus() == Mail.STATUS_READ) {
                if (mail.getReward() != null && mail.getReward().size() > 0) {
                    continue;
                }
            }
            removeList.add(mail);
        }
        if (removeList.size() > 0) {
            mailRepository.delete(removeList);
        }
        this.getMailDuel();
    }

    /**
     * @Description:
     * @param mailKey
     * @throws
     */
    public void removeMail(String mailKey) {
        Mail mail = mailRepository.findOne(mailKey);
        if (mail.getStatus() == Mail.STATUS_UNREAD) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32003, "邮件未读取，不能删除");
        } else if (mail.getStatus() == Mail.STATUS_READ) {
            if (mail.getReward() != null && mail.getReward().size() > 0) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32004, "奖励邮件未领取，不能删除");
            }
        }
        mailRepository.delete(mailKey);
        if (mail.getType() == Mail.TYPE_PVP) {
            this.getMailDuel();
        } else {
            this.getMail();
        }
    }

    /**
     * @Description:
     * @param mailType
     * @param goalId
     * @throws
     */
    private void checkTypeLimitAmount(int mailType, String goalId) {
        if (Mail.TYPE_PVP == mailType) {
            List<Mail> mails = mailRepository.findByPlayerIdAndType(goalId, Mail.TYPE_PVP);
            int limtAmount = this.getDataConfig().get("messageLimit").get("fighting").getInteger("value");
            if (mails.size() >= limtAmount) {
                // 删除邮件
                Query query = new Query();
                query.addCriteria(Criteria.where("player_id").is(goalId));
                query.addCriteria(Criteria.where("type").is(Mail.TYPE_PVP));
                query.with(new Sort(new Order(Direction.ASC, "send_time")));
                query.limit(mails.size() + 1 - limtAmount);
                List<Mail> ms = mongoTemplate.find(query, Mail.class);
                if (ms.size() > 0) {
                    mailRepository.delete(ms);
                }
            }
        } else {
            List<Mail> mails = mailRepository.findByPlayerIdAndTypeNot(goalId, Mail.TYPE_PVP);
            int limtAmount = this.getDataConfig().get("messageLimit").get("system").getInteger("value");
            if (mails.size() >= limtAmount) {
                // 删除邮件
                Query query = new Query();
                query.addCriteria(Criteria.where("player_id").is(goalId));
                query.addCriteria(Criteria.where("type").in(Mail.TYPE_NPC, Mail.TYPE_ADMIN));
                query.with(new Sort(new Order(Direction.ASC, "send_time")));
                query.limit(mails.size() + 1 - limtAmount);
                List<Mail> ms = mongoTemplate.find(query, Mail.class);
                if (ms.size() > 0) {
                    mailRepository.delete(ms);
                }
            }
        }
    }

    private boolean getRewardNotEroor(Lord lord, Map<String, Integer> reward) {
        boolean isTage = true;
        Duel duel = duelRepository.findOne(lord.getId());
        LeagueLord leagueLord = leagueLordRepository.findOne(lord.getId());
        ServletUtils.getRequest().setAttribute("currentLord_admin", lord);
        boolean isDuelChange = false;
        boolean isLeagueLordChange = false;
        Set<String> set = reward.keySet();
        for (String itemId : set) {
            if (StringUtils.startsWith(itemId, ItemID.SKILL)) {
                if (lord.getSkills().size() + reward.get(itemId) > lord.getSkillLimit()) {
                    isTage = false;
                }
            } else if (StringUtils.startsWith(itemId, ItemID.EQUIP)) {
                if (lord.getEquips().size() + reward.get(itemId) > lord.getEquipLimit()) {
                    isTage = false;
                }
            } else if (StringUtils.startsWith(itemId, ItemID.HERO)) {
                if (lord.getHeros().size() + reward.get(itemId) > lord.getHeroLimit()) {
                    isTage = false;
                }
            } else if (this.getDataConfig().get("item").getJsonObject().containsKey(itemId)) {
                if (!lord.getItems().containsKey(itemId) && lord.getItems().size() > lord.getItemLimit()) {
                    isTage = false;
                }
            }
            if (!isTage) {
                break;
            }
            if (ItemID.PVPGPLD.equals(itemId)) {
                isDuelChange = true;
                gainPayService.gain(duel, itemId, reward.get(itemId));
            } else if (ItemID.FUND.equals(itemId)) {
                isLeagueLordChange = true;
                gainPayService.gain(leagueLord, itemId, reward.get(itemId));
            } else {
                gainPayService.gain(lord, itemId, reward.get(itemId));
            }
        }
        if (isTage) {
            if (isDuelChange) {
                duelRepository.save(duel);
            }
            if (isLeagueLordChange) {
                leagueLordRepository.save(leagueLord);
            }
        }
        return isTage;
    }

    private void getReward(Lord lord, Map<String, Integer> reward) {
        Duel duel = duelRepository.findOne(lord.getId());
        LeagueLord leagueLord = leagueLordRepository.findOne(lord.getId());
        ServletUtils.getRequest().setAttribute("currentLord_admin", lord);
        boolean isDuelChange = false;
        boolean isLeagueLordChange = false;
        Set<String> set = reward.keySet();
        for (String itemId : set) {
            this.checkLimt(lord, itemId, reward.get(itemId));
            if (ItemID.PVPGPLD.equals(itemId)) {
                isDuelChange = true;
                gainPayService.gain(duel, itemId, reward.get(itemId));
            } else if (ItemID.FUND.equals(itemId)) {
                isLeagueLordChange = true;
                gainPayService.gain(leagueLord, itemId, reward.get(itemId));
            } else {
                gainPayService.gain(lord, itemId, reward.get(itemId));
            }
        }
        if (isDuelChange) {
            duelRepository.save(duel);
        }
        if (isLeagueLordChange) {
            leagueLordRepository.save(leagueLord);
        }
    }

    private void checkLimt(Lord lord, String itemId, int amount) {
        if (StringUtils.startsWith(itemId, ItemID.SKILL)) {
            if (lord.getSkills().size() + amount > lord.getSkillLimit()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32007, "技能已满");
            }
        } else if (StringUtils.startsWith(itemId, ItemID.EQUIP)) {
            if (lord.getEquips().size() + amount > lord.getEquipLimit()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32006, "装备已满");
            }
        } else if (StringUtils.startsWith(itemId, ItemID.HERO)) {
            if (lord.getHeros().size() + amount > lord.getHeroLimit()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32008, "武将已满");
            }
        } else if (this.getDataConfig().get("item").getJsonObject().containsKey(itemId)) {
            if (!lord.getItems().containsKey(itemId) && lord.getItems().size() > lord.getItemLimit()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_32005, "道具背包已满");
            }
        }
    }

}
