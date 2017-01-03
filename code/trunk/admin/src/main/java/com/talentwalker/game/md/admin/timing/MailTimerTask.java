/**
 * @Title: MailTimerTask.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月31日  赵丽宝
 */

package com.talentwalker.game.md.admin.timing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.talentwalker.game.md.admin.form.MailSendForm;
import com.talentwalker.game.md.admin.repository.gmtool.MailAdminSendRepository;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;

import net.sf.json.JSONArray;

/**
 * @ClassName: MailTimerTask
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月31日 下午1:55:51
 */

public class MailTimerTask extends TimerTask {
    private MailRepository mailRepository;
    private MailAdminSendRepository mailAdminSendRepository;
    private String mailSendKey;
    private GameUserRepository gameUserRepository;
    private MongoTemplate mongoTemplate;
    private IDataConfigManager dataConfigManager;

    /**
     * 创建一个新的实例 MailTimerTask.
     */
    public MailTimerTask(MailRepository mailRepository, MailAdminSendRepository mailAdminSendRepository,
            GameUserRepository gameUserRepository, String mailSendKey, MongoTemplate mongoTemplate,
            IDataConfigManager dataConfigManager) {
        this.mailRepository = mailRepository;
        this.mailAdminSendRepository = mailAdminSendRepository;
        this.mailSendKey = mailSendKey;
        this.gameUserRepository = gameUserRepository;
        this.mongoTemplate = mongoTemplate;
        this.dataConfigManager = dataConfigManager;
    }

    /**.
     * <p>Title: run</p>
     * <p>Description: </p>
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        MailSendForm mailSendForm = mailAdminSendRepository.findOne(mailSendKey);
        mailSendForm.setStatus(MailSendForm.STATUS_SENDING);
        mailAdminSendRepository.save(mailSendForm);
        List<Mail> mailList = new ArrayList<>();
        if (mailSendForm.getGoal() == MailSendForm.GOAL_LORD_ONE) {
            // 单一玩家
            List<String> palyers = JSONArray.fromObject(mailSendForm.getPlayer());
            for (String palyerId : palyers) {
                this.checkTypeLimitAmount(palyerId);
                Mail mail = new Mail();
                mail.setPlayerId(palyerId);
                mail.setSender(mailSendForm.getSender());
                mail.setSenderHeadIcon(mailSendForm.getSenderHeadIcon());
                mail.setCreateTime(mailSendForm.getCreateTime().getTime());
                mail.setSendTime(new Date());
                mail.setTitle(mailSendForm.getTitle());
                mail.setContent(mailSendForm.getContent());
                mail.setReward(mailSendForm.getReward());
                mail.setStatus(Mail.STATUS_UNREAD);
                mail.setType(Mail.TYPE_ADMIN);
                if (mailSendForm.getValidDay() > 0) {
                    mail.setFailureTime(new Date(
                            mail.getSendTime().getTime() + DateUtils.MILLIS_PER_DAY * mailSendForm.getValidDay()));
                }
                mailList.add(mail);
            }
        } else if (mailSendForm.getGoal() == MailSendForm.GOAL_LORD_ALL) {
            // 全部玩家
            List<GameUser> users = gameUserRepository.findByGameZoneIdIn(mailSendForm.getServers());
            for (GameUser user : users) {
                this.checkTypeLimitAmount(user.getId());
                Mail mail = new Mail();
                mail.setPlayerId(user.getId());
                mail.setSender(mailSendForm.getSender());
                mail.setSenderHeadIcon(mailSendForm.getSenderHeadIcon());
                mail.setCreateTime(mailSendForm.getCreateTime().getTime());
                mail.setSendTime(new Date());
                mail.setTitle(mailSendForm.getTitle());
                mail.setContent(mailSendForm.getContent());
                mail.setReward(mailSendForm.getReward());
                mail.setServer(user.getGameZoneId());
                mail.setStatus(Mail.STATUS_UNREAD);
                mail.setType(Mail.TYPE_ADMIN);
                if (mailSendForm.getValidDay() > 0) {
                    mail.setFailureTime(new Date(
                            mail.getSendTime().getTime() + DateUtils.MILLIS_PER_DAY * mailSendForm.getValidDay()));
                }
                mailList.add(mail);
            }
        } else if (mailSendForm.getGoal() == MailSendForm.GOAL_LORD_SELECT) {
            // 符合条件的玩家

        }
        if (mailList.size() > 0) {
            mailRepository.save(mailList);
        }
        mailSendForm.setStatus(MailSendForm.STATUS_SENT);
        mailAdminSendRepository.save(mailSendForm);
    }

    /**
     * @Description:
     * @param mailType
     * @param goalId
     * @throws
     */
    private void checkTypeLimitAmount(String goalId) {
        List<Mail> mails = mailRepository.findByPlayerIdAndTypeNot(goalId, Mail.TYPE_PVP);
        int limtAmount = dataConfigManager.getTest().get("messageLimit").get("system").getInteger("value");
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
