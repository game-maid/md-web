/**
 * @Title: GameSupport.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.MonthCard;
import com.talentwalker.game.md.core.domain.gameworld.SignInRecord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.service.IGameUserServiceRemote;
import com.talentwalker.game.md.core.service.gameworld.TopUpCardService;

/**
 * @ClassName: GameSupport
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月13日 下午2:10:32
 */

public class GameSupport extends BaseGameSupport {
    @Autowired
    protected IGameUserServiceRemote gameUserServiceRemote;
    @Autowired
    protected IDataConfigManager dataConfigManager;
    @Autowired
    protected LordRepository lordRepository;
    @Autowired
    protected GameUserRepository gameUserRepository;
    @Autowired
    private GameZoneRepository gameZoneRepository;

    /**
     * @Description:获得当前的GameUser对象
     * @return
     * @throws
     */
    protected GameUser getGameUser() {
        HttpServletRequest request = ServletUtils.getRequest();
        Lord currentLord = (Lord) request.getAttribute("currentLord_admin");// 后台操作的主公
        if (currentLord == null) {
            String sessionId = request.getHeader("sessionId");
            return gameUserServiceRemote.findGameUserCache(sessionId);
        } else {
            GameUser gameUser = gameUserRepository.findOne(currentLord.getId());
            gameUser.setGameZone(gameZoneRepository.findOne(gameUser.getGameZoneId()));
            return gameUser;
        }
    }

    /**
     * @Description:获得当前的主公对象，进行体力精力恢复等需要恢复计算后的
     * @return
     * @throws
     */
    protected Lord getLord() {
        HttpServletRequest request = ServletUtils.getRequest();
        Lord lord = (Lord) request.getAttribute("lord");
        if (lord == null) {
            lord = lordRepository.findOne(getGameUser().getId());
            if (lord == null) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21001);
            }
            request.setAttribute("lord", lord);
        }
        this.everyday(lord);
        this.restoreEnergy(lord);
        this.restoreStrength(lord);
        lord.setLastTime(System.currentTimeMillis());
        if (!DateUtils.isSameDay(new Date(lord.getStrengthBuyTime()), new Date())) {
            lord.setStrengthBuyTime(System.currentTimeMillis());
            lord.setStrengthTimes(0);
        }
        // 刷新每日vip奖励
        if (!DateUtils.isSameDay(new Date(lord.getLastGetDailyVipRewardTime()), new Date())) {
            lord.setLastGetDailyVipRewardTime(System.currentTimeMillis());
            Map<Integer, Integer> dailyVipReward = new HashMap<>();
            dailyVipReward.put(lord.getVipLevel(), 0);
            lord.setDailyVipReward(dailyVipReward);
        }
        // 刷新签到信息
        Map<Integer, SignInRecord> signInRecords = lord.getSignInRecords();
        if (signInRecords != null && signInRecords.size() > 0) {
            SignInRecord signInRecord = signInRecords.get(1);
            Date signTime = new Date(signInRecord.getTime());
            Date curDate = new Date();
            if (!this.isSameMonth(curDate, signTime)) {
                signInRecords.clear();
                lord.setSignInRecords(signInRecords);
                lord.setReplenishSignTimes(0);
            }
        }
        // 刷新赠送体力记录
        if (!DateUtils.isSameDay(new Date(lord.getGivesStrengthTime()), new Date())) {
            lord.setGivesStrengthTime(System.currentTimeMillis());
            lord.setGivesStrengthRecord(new HashMap<>());
        }

        return lord;
    }

    /**
     * @Description:日常刷新
     * @param lord
     * @throws
     */
    private void everyday(Lord lord) {
        Map<String, MonthCard> monthCards = new HashMap<>();
        monthCards.putAll(lord.getMonthCard());
        Set<String> set = monthCards.keySet();
        DataConfig config = this.getDataConfig().get(TopUpCardService.CONFIG_CASH_SHOP);
        // 刷新月卡
        for (String cardId : set) {
            if (config.get(cardId).getInteger("type") == TopUpCardService.CONFIG_SHOP_TYPE_CARD_UNLIMIT) {
                continue;
            }
            if (config.get(cardId) == null) {
                lord.getMonthCard().remove(cardId);
                continue;
            }
            MonthCard monthCard = monthCards.get(cardId);
            int days = config.get(cardId).getInteger(TopUpCardService.CONFIG_CASH_SHOP_EFFECT_DAYS);
            if (monthCard.getCreateTime() + days * DateUtils.MILLIS_PER_DAY * monthCard.getAmount() <= System
                    .currentTimeMillis()) {
                lord.getMonthCard().remove(cardId);
            }
        }
    }

    /**
     * @Description:获得对应的配置对象
     * @return
     * @throws
     */
    protected DataConfig getDataConfig() {
        DataConfig dataConfig = null;
        GameUser gameUser = getGameUser();
        GameZone gameZone = gameUser.getGameZone();
        int gameZoneType = gameZone.getType();
        if (gameZoneType == GameZone.TYPE_TEST) {
            dataConfig = dataConfigManager.getTest();
        } else if (gameZoneType == GameZone.TYPE_AUDIT) {
            dataConfig = dataConfigManager.getSubmit();
        } else if (gameZoneType == GameZone.TYPE_ONLINE) {
            dataConfig = dataConfigManager.getOnline();
        }
        return dataConfig;
    }

    /**
     * @Description 恢复体力
     * @param lord
     */
    protected void restoreStrength(Lord lord) {
        long now = System.currentTimeMillis();
        int strengthLimit = this.getDataConfig().get("strength").get(lord.getLevel() + "").getInteger("strength");
        int strength = lord.getStrength();
        if (strength < strengthLimit) {
            int interval = this.getDataConfig().get("strength").get(lord.getLevel() + "")
                    .getInteger("strength_perback");
            int restore = (int) Math.floor((now - lord.getStrengthTime()) / (interval * 1000));
            int needsCount = strengthLimit - strength;
            if (restore > needsCount) {
                lord.setStrength(strength + needsCount);
                lord.setStrengthTime(now);
            } else {
                lord.setStrength(strength + restore);
                lord.setStrengthTime(lord.getStrengthTime() + restore * interval * 1000);
            }
        } else {
            lord.setStrengthTime(now);
        }

    }

    /**
     * @Description 恢复精力
     * @param lord
     */
    protected void restoreEnergy(Lord lord) {
        long now = System.currentTimeMillis();
        int energyLimit = this.getDataConfig().get("strength").get(lord.getLevel() + "").getInteger("energy");
        int energy = lord.getEnergy();
        if (energy < energyLimit) {
            int interval = this.getDataConfig().get("strength").get(lord.getLevel() + "").getInteger("energy_perback");
            int restore = (int) Math.floor((now - lord.getEnergyTime()) / (interval * 1000));
            int needsCount = energyLimit - energy;
            if (restore > needsCount) {
                lord.setEnergy(energy + needsCount);
                lord.setEnergyTime(now);
            } else {
                lord.setEnergy(energy + restore);
                lord.setEnergyTime(lord.getEnergyTime() + restore * interval * 1000);
            }
        } else {
            lord.setEnergyTime(now);
        }
    }

    /**
     * @Description:判断两个日期是否为同一个月
     * @param date1
     * @param date2
     * @return true ： 是同一个月  ；false： 不是同一个月
     * @throws
     */
    private boolean isSameMonth(Date date1, Date date2) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        if (sdf1.format(date1).equals(sdf1.format(date2)) && sdf2.format(date1).equals(sdf2.format(date2)))
            return true;
        return false;
    }
}
