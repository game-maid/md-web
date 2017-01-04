/**
 * @Title: TopUpService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月8日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.repository.gameworld.TopUpFirstRecordRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.service.gameworld.GainPayService;
import com.talentwalker.game.md.core.service.gameworld.TopUpCardService;
import com.talentwalker.game.md.core.util.GameSupport;

/**
 * @ClassName: TopUpService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月8日 下午7:50:55
 */
@Service
public class TopUpService extends GameSupport {
    @Autowired
    private TopUpFirstRecordRepository topUpFirstRecordRepository;
    @Autowired
    private GainPayService gainPayService;

    public void getFirstRecord() {
        Lord lord = this.getLord();
        this.gameModel.addObject(ResponseKey.TOP_UP_FIRST_RECORD, topUpFirstRecordRepository.findOne(lord.getId()));
        this.gameModel.addObject(ResponseKey.TOP_UP_FIRST_AWARD, lord.isTopUpFirstAward());
    }

    public void getFirstAward() {
        Lord lord = this.getLord();
        if (!lord.isTopUpFirstAward() && topUpFirstRecordRepository.findOne(lord.getId()) != null) {
            // 首充礼包
            DataConfig firstShopConfig = this.getDataConfig().get(TopUpCardService.CONFIG_CASH_SHOP_FIRST);
            Iterator<String> it = firstShopConfig.getJsonObject().keySet().iterator();
            Map<String, Integer> map = new HashMap<>();
            while (it.hasNext()) {
                String key = it.next();
                if (!firstShopConfig.get(key).getJsonObject().containsKey("itemID")) {
                    continue;
                }
                String itemId = firstShopConfig.get(key).get("itemID").getString("itemId");
                Integer amount = firstShopConfig.get(key).get("itemID").getInteger("amount");
                gainPayService.gain(lord, itemId, amount);
                map.put(itemId, amount);
            }
            lord.setTopUpFirstAward(true);
            lordRepository.save(lord);
            this.gameModel.addObject(ResponseKey.TOP_UP_FIRST, map);
            this.gameModel.addObject(ResponseKey.TOP_UP_FIRST_AWARD, lord.isTopUpFirstAward());
        }
    }
}
