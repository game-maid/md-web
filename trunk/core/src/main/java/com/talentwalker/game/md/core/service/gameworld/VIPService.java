
package com.talentwalker.game.md.core.service.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: VipService
 * @Description: vip
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月2日 下午4:57:04
 */
@Service
public class VIPService extends GameSupport {
    /**
     * vip奖励配置文件名（.xls）
     */
    public static final String VIP_AWARD = "VIP_award";

    /**
     * 每日vip奖励
     */
    public static final String DAILY_VIP_REWARD = "dailyVipReward";
    /**
     * vip等级奖励
     */
    public static final String VIP_LEVEL_REWARD = "vipLevelReward";

    @Resource
    private GainPayService gainPayService;
    @Resource
    private LordRepository lordRepository;

    /**
     * @Description:获取每日vip奖励和vip等级奖励
     * @throws
     */
    public void list() {
        Lord lord = getLord();
        Map<Integer, Integer> dailyVipReward = lord.getDailyVipReward();
        List<Integer> levelVipReward = lord.getLevelVipReward();
        if (levelVipReward == null) {
            levelVipReward = new ArrayList<>();
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer vipLevel : levelVipReward) {
            map.put(vipLevel, 1);
        }
        this.gameModel.addObject(DAILY_VIP_REWARD, dailyVipReward);
        this.gameModel.addObject(VIP_LEVEL_REWARD, map);
    }

    /**
     * @Description:领取每日奖励
     * @param level
     * @throws
     */
    public void drawDailyReward(int level) {
        Lord lord = getLord();
        if (lord.getVipLevel() < level) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21009, "vip等级不足");
        }
        Map<Integer, Integer> dailyVipReward = lord.getDailyVipReward();
        if (!dailyVipReward.containsKey(level)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_35002, "该vip等级不存在");
        }
        if (dailyVipReward.get(level) != 0) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_35001, "该奖励已领取");
        }
        DataConfig dailyReward = getDataConfig().get(VIP_AWARD).get(level + "").get("dayAward");
        for (int i = 1; i <= 4; i++) {
            DataConfig dataConfig = dailyReward.get(i + "");
            if (dataConfig == null)
                continue;
            Map<String, Integer> rewardMap = dataConfig.getJsonObject();
            Set<String> itemIds = rewardMap.keySet();
            for (String itemId : itemIds) {
                gainPayService.gain(lord, itemId, rewardMap.get(itemId));
            }
        }
        // 修改状态
        dailyVipReward.put(level, 1);
        lord.setDailyVipReward(dailyVipReward);
        lordRepository.save(lord);
    }

    /**
     * @Description:领取vip等级奖励
     * @param level
     * @throws
     */
    public void drawlevelReward(int level) {
        Lord lord = getLord();
        if (lord.getVipLevel() < level) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21009, "vip等级不足");
        }
        List<Integer> levelVipReward = lord.getLevelVipReward();
        if (levelVipReward != null) {
            if (levelVipReward.contains(level)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_35001, "已领取该奖励");
            }
        } else {
            levelVipReward = new ArrayList<>();
        }
        DataConfig vipAwardConfig = getDataConfig().get(VIP_AWARD).get(level + "");
        if (vipAwardConfig == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_35002, "该vip等级不存在");
        }
        DataConfig levelReward = vipAwardConfig.get("levAward");
        for (int i = 1; i <= 4; i++) {
            DataConfig dataConfig = levelReward.get(i + "");
            if (dataConfig == null)
                continue;
            Map<String, Integer> rewardMap = dataConfig.getJsonObject();
            System.out.println(rewardMap);
            Set<String> itemIds = rewardMap.keySet();
            for (String itemId : itemIds) {
                System.out.println(rewardMap.get(itemId));
                gainPayService.gain(lord, itemId, rewardMap.get(itemId));
            }
        }
        // 修改状态
        levelVipReward.add(level);
        lord.setLevelVipReward(levelVipReward);
        lordRepository.save(lord);
    }

}
