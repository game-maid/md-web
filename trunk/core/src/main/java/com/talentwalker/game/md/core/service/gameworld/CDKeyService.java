
package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.CDKey;
import com.talentwalker.game.md.core.domain.gameworld.CDKeyBatch;
import com.talentwalker.game.md.core.domain.gameworld.CDKeyUseRecord;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.gameworld.CDKeyBatchRepository;
import com.talentwalker.game.md.core.repository.gameworld.CDKeyRepository;
import com.talentwalker.game.md.core.repository.gameworld.CDKeyUseRecordRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: CDKeyService
 * @Description: CDK兑换码
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月28日 下午5:45:18
 */
@Service
public class CDKeyService extends GameSupport {

    @Resource
    private CDKeyRepository cDKeyRepository;

    @Resource
    private GameUserRepository gameUserRepository;

    @Resource
    private CDKeyUseRecordRepository cdKeyUseRecordRepository;

    @Resource
    private MailService mailService;

    @Resource
    private CDKeyBatchRepository batchRepository;
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * @Description:使用兑换码兑换
     * @param key：兑换码
     * @throws
     */
    public void exchange(String key) {
        Lord lord = getLord();
        String lordId = lord.getId();
        GameUser gameUser = gameUserRepository.findOne(lordId);

        CDKey cdk = cDKeyRepository.findOne(key);
        if (cdk == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34008, "兑换码不存在");
        }
        CDKeyBatch batch = cdk.getBatch();
        String id = batch.getId();// 批次的id
        List<String> packageIds = batch.getPackageIds();// 包ids
        List<String> zoneIds = batch.getZoneIds();// 区服Ids
        long startTime = batch.getStartTime();
        long endTime = batch.getEndTime();
        int status = cdk.getStatus();// 状态
        int level = batch.getLevel();
        Map<String, Integer> reward = batch.getReward();
        if (!packageIds.contains(gameUser.getPackageId())) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34001, "渠道(包id）不符合");
        }
        if (!zoneIds.contains(gameUser.getGameZoneId())) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34002, "区服不符合");
        }
        long curTime = System.currentTimeMillis();
        if (curTime > endTime || curTime < startTime) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34004, "时间不符合");
        }
        CDKeyUseRecord useRecord = cdKeyUseRecordRepository.findOne(batch.getId());
        if (useRecord == null) {
            useRecord = new CDKeyUseRecord();
            useRecord.setId(batch.getId());
            useRecord.setRecord(new HashSet<>());
        }
        Set<String> record = useRecord.getRecord();
        if (record != null && record.contains(lordId)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34005, "已使用过该批次兑换码");
        }
        if (cdk.getStatus() == CDKey.USED) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34003, "该兑换码已被使用");
        }
        if (cdk.getStatus() == CDKey.CANCELLATION) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34006, "该兑换码已经作废");
        }
        if (lord.getLevel() < level) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34007, "玩家等级不够");
        }
        // 修改兑换码状态，保存兑换人id和使用时间
        cdk.setStatus(CDKey.USED);
        cdk.setUseId(lordId);
        cdk.setUseTime(curTime);
        cDKeyRepository.save(cdk);
        // 物品发送邮箱
        Mail mail = new Mail();
        mail.setSender(batch.getSender());
        mail.setContent(batch.getContent());
        mail.setFailureTime(new Date(System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY * batch.getValidDay()));
        mail.setTitle(batch.getTitle());
        mail.setSenderHeadIcon(batch.getSenderHeadIcon());
        mail.setPlayerId(lordId);
        mail.setReward(reward);
        mailService.sendMailAdmin(mail);
        // 保存该批次已使用玩家列表
        record.add(lordId);
        useRecord.setRecord(record);
        cdKeyUseRecordRepository.save(useRecord);

        gameModel.addObject("reward", reward);
    }

    public Page<CDKey> findAll() {
        HttpServletRequest req = ServletUtils.getRequest();
        String batchId = req.getParameter("batchId");
        SearchFilter searchFilter = SearchFilter.newSearchFilter();
        CDKeyBatch batch = batchRepository.findOne(batchId);
        if (batch != null) {
            searchFilter.addCriteria(Criteria.where("batch").is(batch));
        }
        return cDKeyRepository.findAll(searchFilter);
    }

    /**
     * @Description:根据id作废一个
     * @param id
     * @throws
     */
    public void cancellation(String id) {
        CDKey cdk = cDKeyRepository.findOne(id);
        cdk.setStatus(CDKey.CANCELLATION);
        cDKeyRepository.save(cdk);
    }

    /**
     * @Description:根据条件批量作废
     * @param packageId
     * @param batchId
     * @param cdkId
     * @throws
     */
    public void cancellationAll(String batchId, String cdkId) {
        CDKey cdk = null;
        List<CDKey> list = new ArrayList<>();
        /*
         * if (!batchId.isEmpty()) { CDKeyBatch batch = batchRepository.findOne(batchId); if (!cdkId.isEmpty()) { cdk =
         * cDKeyRepository.findByIdAndBatch(cdkId, batch); } else { list = cDKeyRepository.findByBatch(batch); } } else
         * { cdk = cDKeyRepository.findOne(cdkId); } if (cdk != null) { list.add(cdk); }
         */

        Query query = new Query();
        if (!batchId.isEmpty()) {
            query.addCriteria(Criteria.where("batch").is(batchRepository.findOne(batchId)));
        }
        if (!cdkId.isEmpty()) {
            query.addCriteria(Criteria.where("id").is(cdkId));
        }
        list = mongoTemplate.find(query, CDKey.class);

        for (int i = 0; i < list.size(); i++) {
            CDKey cDKey = list.get(i);
            cDKey.setStatus(CDKey.CANCELLATION);
            list.set(i, cDKey);
        }
        cDKeyRepository.save(list);
    }

}
