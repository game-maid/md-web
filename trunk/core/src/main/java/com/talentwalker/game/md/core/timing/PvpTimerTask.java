/**
 * @Title: PvpTimerTask.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年9月12日  赵丽宝
 */

package com.talentwalker.game.md.core.timing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.gameworld.DuelRank;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;

/**
 * @ClassName: PvpTimerTask
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年9月12日 上午11:09:29
 */

public class PvpTimerTask extends TimerTask {
    private MongoTemplate mongoTemplate;
    private MailRepository mailRepository;
    private IDataConfigManager dataConfigManager;
    private final static String RANK_KEY = "game_duel_rank_";
    private String ServerId;
    private String dateValue;

    /**
     * 创建一个新的实例 PvpTimerTask.
     */
    public PvpTimerTask(MongoTemplate mongoTemplate, MailRepository mailRepository,
            IDataConfigManager dataConfigManager, String ServerId, String dateValue) {
        this.mongoTemplate = mongoTemplate;
        this.mailRepository = mailRepository;
        this.dataConfigManager = dataConfigManager;
        this.ServerId = ServerId;
        this.dateValue = dateValue;
    }

    /**.
     * <p>Title: run</p>
     * <p>Description: </p>
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        List<Mail> mails = new ArrayList<Mail>();
        DataConfig config = dataConfigManager.getTest().get("duel_rankScore");
        String rankKey = RANK_KEY + ServerId;
        String rankKeyDate = rankKey + "_" + dateValue;
        if (!mongoTemplate.collectionExists(rankKey)) {
            return;
        }
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<DuelRank> duelList = new ArrayList<>();
        if (mongoTemplate.collectionExists(rankKeyDate)) {
            return;
            // Query query = new Query();
            // query.with(new Sort(new Order(Direction.ASC, "rank")));
            // duelList = mongoTemplate.find(query, DuelRank.class, rankKeyDate);
        } else {
            Query query = new Query();
            query.with(new Sort(new Order(Direction.ASC, "rank")));
            duelList = mongoTemplate.find(query, DuelRank.class, rankKey);
            // 复制每日排行
            mongoTemplate.insert(duelList, rankKeyDate);
        }

        for (DuelRank duelRanl : duelList) {
            if (duelRanl.getId().contains("NPC_")) {
                continue;
            }
            Iterator it = config.getJsonObject().keys();
            while (it.hasNext()) {
                String index = it.next().toString();
                if (duelRanl.getRank() < config.get(index).getInteger("ranking")) {
                    String mailKey = config.get((Integer.parseInt(index) - 1) + "").getString("point");
                    if (map.containsKey(mailKey)) {
                        map.get(mailKey).add(duelRanl.getId());
                    } else {
                        List<String> l = new ArrayList<>();
                        l.add(duelRanl.getId());
                        map.put(mailKey, l);
                    }
                    break;
                }
            }
        }
        Set<String> setMailKey = map.keySet();
        for (String mailKey : setMailKey) {
            this.sendMails(map.get(mailKey), mailKey, null, null, mails);
        }
        mailRepository.save(mails);
    }

    /**
     * @Description: 批量发送邮件
     * @param goalId 发送目标
     * @param mailKey 邮件对应key
     * @param paramet 翻译字段中替换文字
     * @param reward 奖励
     * @param mailType 邮件类型
     * @throws
     */
    private void sendMails(List<String> goalIds, String mailKey, List<Object> paramet, Map<String, Integer> reward,
            List<Mail> mails) {
        for (String goalId : goalIds) {
            this.checkTypeLimitAmount(goalId);
            Mail mail = this.initMail(mailKey, paramet, reward);
            mail.setPlayerId(goalId);
            mails.add(mail);
        }
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    private Mail initMail(String mailKey, List<Object> paramet, Map<String, Integer> reward) {
        Mail mail = new Mail();
        if (paramet == null) {
            paramet = new ArrayList<>();
        }
        mail.setParamet(paramet);
        if (reward == null) {
            reward = new HashMap<>();
        }
        mail.setReward(reward);
        mail.setType(Mail.TYPE_PVP);
        DataConfig config = dataConfigManager.getTest().get("NPCmailsent").get(mailKey);
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
     * @Description:
     * @param mailType
     * @param goalId
     * @throws
     */
    private void checkTypeLimitAmount(String goalId) {
        List<Mail> mails = mailRepository.findByPlayerIdAndType(goalId, Mail.TYPE_PVP);
        int limtAmount = dataConfigManager.getTest().get("messageLimit").get("fighting").getInteger("value");
        if (mails.size() >= limtAmount) {
            // 删除邮件
            Query query = new Query();
            query.addCriteria(Criteria.where("player_id").is(goalId));
            query.addCriteria(Criteria.where("type").is(Mail.TYPE_PVP));
            query.with(new Sort(new Order(Direction.DESC, "send_time")));
            Mail m = mongoTemplate.findOne(query, Mail.class);
            if (m != null) {
                mailRepository.delete(m);
            }
        }
    }

}
