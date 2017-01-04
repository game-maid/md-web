/**
 * @Title: MailSendService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月19日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.gmtool;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

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
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.StringUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: MailSendService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月19日 下午12:39:45
 */
@Service
public class MailSendService extends BaseService {
    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private MailAdminSendRepository mailAdminSendRepository;
    @Autowired
    private GameUserRepository gameUserRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IDataConfigManager dataConfigManager;

    /**
     * @Description:
     * @param mailSendForm
     * @throws
     */
    public void send(MailSendForm mailSendForm) {
        try {
            MailSendForm form = new MailSendForm();
            if (!StringUtils.isPrarmsEmpty(mailSendForm.getId())) {
                form = mailAdminSendRepository.findOne(mailSendForm.getId());
            }
            final long sendTime = DateUtils
                    .parseDate(mailSendForm.getSendDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss").getTime();
            long delay = sendTime - System.currentTimeMillis() < 0 ? 0 : sendTime - System.currentTimeMillis();
            Map<String, Integer> reward = new HashMap<>();
            if (!StringUtils.isPrarmsEmpty(mailSendForm.getItems())) {
                reward = JSONObject.fromObject(mailSendForm.getItems());
            }
            form.setSender(mailSendForm.getSender());
            form.setSenderHeadIcon(mailSendForm.getSenderHeadIcon());
            form.setGoal(mailSendForm.getGoal());
            form.setPlayer(mailSendForm.getPlayer());
            form.setServers(mailSendForm.getServers());
            form.setCreateTime(new Date());
            form.setSendTime(new Date(sendTime));
            form.setTitle(mailSendForm.getTitle());
            form.setContent(mailSendForm.getContent());
            form.setReward(reward);
            form.setStatus(MailSendForm.STATUS_UNSENT);
            form.setValidDay(mailSendForm.getValidDay());
            String mailSendKey = mailAdminSendRepository.save(form).getId();
            // 定时发送
            Timer timer = new Timer(mailSendKey);
            timer.schedule(new MailTimerTask(mailRepository, mailAdminSendRepository, gameUserRepository, mailSendKey,
                    mongoTemplate, dataConfigManager), delay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    public Page<MailSendForm> findAll() {
        Sort sort = new Sort(Direction.DESC, "sendTime");
        SearchFilter searchFiter = SearchFilter.newSearchFilter(sort);
        return mailAdminSendRepository.findAll(searchFiter);
    }

    /**
     * @Description:
     * @param id
     * @return
     * @throws
     */
    public void cancel(String id) {
        MailSendForm form = mailAdminSendRepository.findOne(id);
        // 定时发送
        Timer timer = new Timer(id);
        if (form.getStatus() == MailSendForm.STATUS_UNSENT) {
            timer.cancel();
            form.setStatus(MailSendForm.STATUS_CANCEL);
        } else if (form.getStatus() == MailSendForm.STATUS_CANCEL) {
            long sendTime = form.getSendTime().getTime();
            long delay = sendTime - System.currentTimeMillis() < 0 ? 0 : sendTime - System.currentTimeMillis();
            timer.schedule(new MailTimerTask(mailRepository, mailAdminSendRepository, gameUserRepository, id,
                    mongoTemplate, dataConfigManager), delay);
            form.setStatus(MailSendForm.STATUS_UNSENT);
        }
        mailAdminSendRepository.save(form);
    }

    /**
     * @Description:
     * @param id
     * @throws
     */
    public void delete(String id) {
        MailSendForm form = mailAdminSendRepository.findOne(id);
        if (form.getStatus() == MailSendForm.STATUS_UNSENT) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("gmtool.mail.check.cancel"));
        }
        mailAdminSendRepository.delete(id);
    }

    /**
     * @Description:根据邮件ID查询
     * @param ids
     * @return
     * @throws
     */
    public Object findByIds(String[] ids) {
        ArrayList<String> mailIds = new ArrayList<>(Arrays.asList(ids));
        return mailAdminSendRepository.findByIdIn(mailIds);
    }

}
