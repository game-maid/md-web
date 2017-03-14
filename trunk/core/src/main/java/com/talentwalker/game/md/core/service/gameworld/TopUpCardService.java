/**
 * @Title: TopUpCardService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月5日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.MonthCard;
import com.talentwalker.game.md.core.domain.gameworld.TopUpFirstRecord;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.repository.gameworld.TopUpFirstRecordRepository;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: TopUpCardService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月5日 下午3:20:21
 */
@Service
public class TopUpCardService extends GameSupport {
    @Autowired
    private IDataConfigManager dataConfigManager;
    @Autowired
    private TopUpFirstRecordRepository topUpFirstRecordRepository;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private LordRepository lordRepository;

    public static final String CONFIG_CASH_SHOP = "cashShop_config";
    public static final String CONFIG_CASH_SHOP_FIRST = "cashShop_firstCash"; // 首充礼包配置
    public static final String CONFIG_CASH_SHOP_EFFECT_DAYS = "effectDays";
    private static final String CONFIG_CASH_SHOP_BASE_AMOUNT = "baseAmount"; // 购买钻石基数
    private static final String CONFIG_CASH_SHOP_SEND_AMOUNT = "sendAmount"; // 非首充买送
    private static final String CONFIG_CASH_SHOP_MULTIPLE = "multiple"; // 首充翻倍
    private static final Integer CONFIG_SHOP_TYPE_ITEM = 1; // 充值道具
    private static final Integer CONFIG_SHOP_TYPE_CARD = 2; // 月卡
    public static final Integer CONFIG_SHOP_TYPE_CARD_UNLIMIT = 3; // 无限月卡

    public void topUp(Lord lord, String productId, GameUser gameUser) {
        ServletUtils.getRequest().setAttribute("currentLord_admin", lord);
        DataConfig config = this.getDataConfig();
        // 首充记录
        TopUpFirstRecord topUpFirstRecord = topUpFirstRecordRepository.findOne(lord.getId());
        DataConfig topupConfig = config.get(CONFIG_CASH_SHOP).get(productId);
        int vipScore = topupConfig.getInteger(CONFIG_CASH_SHOP_BASE_AMOUNT);
        int baseAmount = vipScore;
        int persentAmount = 0;
        if (topUpFirstRecord == null) {
            topUpFirstRecord = new TopUpFirstRecord();
            topUpFirstRecord.setRecord(new HashMap<>());
            topUpFirstRecord.setId(lord.getId());
            topUpFirstRecord.setCreateTime(System.currentTimeMillis());
            topUpFirstRecord.setPackageId(gameUser.getPackageId());
            topUpFirstRecord.setZoneId(gameUser.getGameZoneId());
        }
        if (!topUpFirstRecord.getRecord().containsKey(productId)) {
            // 首充翻倍
            persentAmount = baseAmount * (topupConfig.getInteger(CONFIG_CASH_SHOP_MULTIPLE) - 1);
            if (topupConfig.getInteger("type") != CONFIG_SHOP_TYPE_ITEM) {
                persentAmount += topupConfig.getInteger(CONFIG_CASH_SHOP_SEND_AMOUNT);
            }
            topUpFirstRecord.getRecord().put(productId, 1);
        } else {
            // if (topupConfig.getInteger("type") != CONFIG_SHOP_TYPE_ITEM) {
            // vipScore += topupConfig.getInteger(CONFIG_CASH_SHOP_SEND_AMOUNT);
            // } else {
            // persentAmount = topupConfig.getInteger(CONFIG_CASH_SHOP_SEND_AMOUNT);
            // }
            persentAmount = topupConfig.getInteger(CONFIG_CASH_SHOP_SEND_AMOUNT);
            topUpFirstRecord.getRecord().put(productId, topUpFirstRecord.getRecord().get(productId) + 1);
        }
        if (topupConfig.getInteger("type") != CONFIG_SHOP_TYPE_ITEM) {
            // 充值月卡
            if (lord.getMonthCard().containsKey(productId)) {
                MonthCard monthCard = lord.getMonthCard().get(productId);
                monthCard.setAmount(monthCard.getAmount() + 1);
                lord.getMonthCard().put(productId, monthCard);
            } else {
                Iterator<String> it = lord.getMonthCard().keySet().iterator();
                long endTime = System.currentTimeMillis();
                // 判断是否是永久月卡
                if (config.get(CONFIG_CASH_SHOP).get(productId).getInteger("type") != CONFIG_SHOP_TYPE_CARD_UNLIMIT) {
                    while (it.hasNext()) {
                        String itemId = it.next();
                        if (config.get(CONFIG_CASH_SHOP).get(itemId).getInteger("type") != CONFIG_SHOP_TYPE_CARD) {
                            continue;
                        }
                        long tempEndTime = lord.getMonthCard().get(itemId).getCreateTime()
                                + config.get(CONFIG_CASH_SHOP).get(itemId).getInteger(CONFIG_CASH_SHOP_EFFECT_DAYS)
                                        * DateUtils.MILLIS_PER_DAY * lord.getMonthCard().get(itemId).getAmount();
                        if (tempEndTime > endTime) {
                            endTime = tempEndTime;
                        }
                    }
                }
                MonthCard monthCard = new MonthCard();
                monthCard.setCreateTime(endTime);
                monthCard.setLastTime(0);
                monthCard.setAmount(1);
                lord.getMonthCard().put(productId, monthCard);
            }
        }
        if (persentAmount > 0) {
            gainPayService.gain(lord, ItemID.DIAMOND, persentAmount);
        }
        if (baseAmount > 0) {
            gainPayService.gain(lord, ItemID.PERSENT_DIAMOND, baseAmount);
        }
        if (vipScore > 0) {
            gainPayService.gain(lord, ItemID.VIPSCORE, vipScore);
        }
        topUpFirstRecordRepository.save(topUpFirstRecord);
        lordRepository.save(lord);
    }

}
