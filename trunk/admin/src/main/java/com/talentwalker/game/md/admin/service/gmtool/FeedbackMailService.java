
package com.talentwalker.game.md.admin.service.gmtool;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.form.MailSendForm;
import com.talentwalker.game.md.admin.repository.gmtool.MailAdminSendRepository;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.admin.timing.MailTimerTask;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Feedback;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.gameworld.FeedbackRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.StringUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: FeedbackMailService
 * @Description: 反馈邮件
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月25日 下午1:19:55
 */
@Service
public class FeedbackMailService extends BaseService {
    @Resource
    private FeedbackRepository feedbackRepository;

    @Resource
    private MailAdminSendRepository mailAdminSendRepository;

    @Resource
    private MailRepository mailRepository;

    @Autowired
    private GameUserRepository gameUserRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IDataConfigManager dataConfigManager;

    @Resource
    private LordRepository lordRepository;

    /**
     * @Description:查询反馈信息，玩家区服信息，玩家信息
     * @return
     * @throws
     */
    public Page<Feedback> list() {
        SearchFilter filter = SearchFilter.newSearchFilter(new Sort(Direction.ASC, "status"));
        Page<Feedback> page = feedbackRepository.findAll(filter);

  /*      List<Feedback> content = page.getContent();
        List<String> playerIds = new ArrayList<>();
        List<Feedback> tempContent = new ArrayList<>();
        for (Feedback feedback : content) {
            List<String> mailIds = feedback.getMailIds();
            // 查询客服反馈信息
            List<MailSendForm> mails = mailAdminSendRepository.findByIdIn(mailIds);
            List<Map<String, Object>> reply = new ArrayList<>();
            for (MailSendForm mail : mails) {
                Map<String, Object> map = new HashMap<>();
                map.put("createTime", mail.getCreateTime());
                map.put("serviceId", mail.getSender());
                map.put("content", mail.getContent());
                reply.add(map);
            }
            feedback.setReply(reply);
            tempContent.add(feedback);
            // 拿到玩家id
            String playerId = feedback.getPlayerId();
            playerIds.add(playerId);
        }

        // 查询玩家区服信息
        List<GameUser> gameUsers = gameUserRepository.findByIdIn(playerIds);
        // 查询玩家信息
        List<Lord> lords = lordRepository.findByIdIn(playerIds);
        List<Feedback> tempContent2 = new ArrayList<>();
        for (Feedback feedback : tempContent) {
            String playerId = feedback.getPlayerId();
            for (Lord lord : lords) {
                if (playerId.equals(lord.getId())) {
                    feedback.setLevel(lord.getLevel());
                    feedback.setVipLevel(lord.getVipLevel());
                    feedback.setName(lord.getName());
                }
            }
            for (GameUser gameUser : gameUsers) {
                if (playerId.equals(gameUser.getId())) {
                    feedback.setGamezone(gameUser.getGameZoneId());
                    feedback.setSsoId(gameUser.getSsoId());
                }
            }
            tempContent2.add(feedback);
        }*/

        return page;
    }

    /**
     * @Description:定时发送邮件，并将邮件Id保存到玩家反馈信息中
     * @param mailSendForm 
     * @param feedbackId 
     * @throws
     */
    public void send(MailSendForm mailSendForm, String feedbackId) {
        Feedback feedback = feedbackRepository.findOne(feedbackId);// 玩家反馈信息
        try {
            // 定时发送邮件
            MailSendForm form = new MailSendForm();
            final long sendTime = DateUtils
                    .parseDate(mailSendForm.getSendDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss").getTime();
            long delay = sendTime - System.currentTimeMillis() < 0 ? 0 : sendTime - System.currentTimeMillis();
            Map<String, Integer> reward = new HashMap<>();
            if (!StringUtils.isPrarmsEmpty(mailSendForm.getItems())) {
                reward = JSONObject.fromObject(mailSendForm.getItems());
            }
            form.setSender(mailSendForm.getSender());
            form.setSenderHeadIcon(mailSendForm.getSenderHeadIcon());
            form.setGoal(MailSendForm.GOAL_LORD_ONE);
            List<String> playIds = new ArrayList<>();
            playIds.add(feedback.getPlayerId());
            form.setPlayer(playIds);
            form.setServers(mailSendForm.getServers());
            form.setCreateTime(new Date());
            form.setSendTime(new Date(sendTime));
            form.setTitle(mailSendForm.getTitle());
            form.setContent(mailSendForm.getContent());
            form.setReward(reward);
            form.setStatus(MailSendForm.STATUS_UNSENT);
            form.setValidDay(mailSendForm.getValidDay());
            String mailSendKey = mailAdminSendRepository.save(form).getId();
            // 将邮件id保存到玩家反馈信息中
            List<String> mailIds = feedback.getMailIds();
            if (mailIds == null) {
                mailIds = new ArrayList<>();
            }
            mailIds.add(mailSendKey);
            feedback.setMailIds(mailIds);
            feedbackRepository.save(feedback);

            // 定时发送
            Timer timer = new Timer(mailSendKey);
            timer.schedule(new MailTimerTask(mailRepository, mailAdminSendRepository, gameUserRepository, mailSendKey,
                    mongoTemplate, dataConfigManager), delay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
