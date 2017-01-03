
package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Feedback;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.gameworld.FeedbackRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.sensitiveword.SensitiveWord;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: FeedbackService
 * @Description: 客服反馈
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月22日 上午11:47:05
 */
@Service
public class FeedbackService extends GameSupport {

    @Resource
    private FeedbackRepository feedbackRepository;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private GameUserRepository gameUserRepository;
    /**
     * 反馈信息
     */
    private final static String FEEDBACK = "feedback";

    /**
     * @Description:接收玩家反馈信息，保存
     * @param type
     * @param message
     * @throws
     */
    public void addFeedback(Integer type, String message) {
        Lord lord = getLord();
        // 玩家问题记录
        // List<Feedback> list = feedbackRepository.findByPlayerId(lord.getId());
        Query query = new Query();
        query.addCriteria(new Criteria("player_id").is(lord.getId()));
        query.with(new Sort(Direction.ASC, "create_time"));
        List<Feedback> list = mongoTemplate.find(query, Feedback.class);

        // 校验一小时内 ，五条问题
        long curDate = System.currentTimeMillis();
        int length = 0;
        if (list != null && (length = list.size()) >= 5) {
            // 检查倒数第五条的时间和当前时间是否间隔一小时
            Feedback feedback5 = list.get(length - 5);
            if (feedback5.getCreateTime().getTime() + 3600 * 1000 > curDate) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_33001, "提问频繁");
            }
        }

        // 校验敏感词汇
        if (!StringUtils.isEmpty(message)) {
            if (SensitiveWord.isContaintSensitiveWord(message)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21016);
            }
        }
        Feedback feedback = initFeedback(lord.getId(), type, message, new Date(curDate));
        if (list != null && list.size() >= 10) {
            feedbackRepository.delete(list.get(0).getId());
        }
        feedbackRepository.save(feedback);
    }

    /**
     * @Description:初始化反馈信息
     * @param type
     * @param message
     * @param time
     * @return
     * @throws
     */
    private Feedback initFeedback(String id, Integer type, String message, Date time) {
        GameUser gameUser = gameUserRepository.findOne(id);
        Feedback feedback = new Feedback();
        feedback.setCreateTime(time);
        feedback.setMessage(message);
        feedback.setType(type);
        feedback.setPlayerId(id);
        feedback.setGamezoneId(gameUser.getGameZoneId());
        return feedback;
    }

    /**
     * @Description:获取反馈信息
     * @throws
     */
    public void getFeedback() {
        Lord lord = getLord();
        Query query = new Query();
        query.addCriteria(new Criteria("player_id").is(lord.getId()));
        query.with(new Sort(Direction.ASC, "create_time"));
        List<Feedback> list = mongoTemplate.find(query, Feedback.class);
        gameModel.addObject(FEEDBACK, list);
    }

    public Page<Feedback> findAll() {

        SearchFilter filter = SearchFilter.newSearchFilter(new Sort(Direction.ASC, "status"));
        Page<Feedback> page = feedbackRepository.findAll(filter);

        List<Feedback> content = page.getContent();
        List<String> playerIds = new ArrayList<>();
        for (Feedback feedback : content) {
            List<String> mailIds = feedback.getMailIds();
            // 查询客服反馈信息

            // 拿到玩家id
            String playerId = feedback.getPlayerId();
            playerIds.add(playerId);
        }

        return page;

    }

    /**
     * @Description:
     * @param id
     * @throws
     */
    public void deleteById(String id) {
        feedbackRepository.delete(id);
    }

}
