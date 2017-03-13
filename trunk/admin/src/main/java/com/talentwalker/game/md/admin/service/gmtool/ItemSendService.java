/**
 * @Title: ItemSendService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.gmtool;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.talentwalker.game.md.admin.domain.log.Log;
import com.talentwalker.game.md.admin.repository.log.LogRepository;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.LeagueLord;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.SoulStore;
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.DuelRepository;
import com.talentwalker.game.md.core.repository.gameworld.LeagueLordRepository;
import com.talentwalker.game.md.core.repository.gameworld.SoulStoreRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.service.gameworld.GainPayService;
import com.talentwalker.game.md.core.service.gameworld.LordService;
import com.talentwalker.game.md.core.service.gameworld.SoulStoreService;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.ServletUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: ItemSendService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月15日 下午3:09:54
 */
@Service
public class ItemSendService extends BaseService {
    @Autowired
    private IDataConfigManager dataConfigManager;
    @Autowired
    private LordService lordService;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private DuelRepository duelRepository;
    @Autowired
    private LeagueLordRepository leagueLordRepository;
    @Autowired
    private SoulStoreService soulStoreService;
    @Autowired
    public SoulStoreRepository soulStoreRepository;

    /**
     * @Description: 道具下拉列表
     * @return
     * @throws
     */
    public List<Map<String, String>> findItem() {
        List<Map<String, String>> equipList = Lists.newArrayList();
        JSONObject config = dataConfigManager.getTest().get("equip").getJsonObject();
        config.putAll(dataConfigManager.getTest().get("heroConfig").getJsonObject());
        config.putAll(dataConfigManager.getTest().get("item").getJsonObject());
        config.putAll(dataConfigManager.getTest().get("skill").getJsonObject());
        config.putAll(dataConfigManager.getTest().get("heroSoul").getJsonObject());
        Iterator it = config.keys();
        while (it.hasNext()) {
            Map<String, String> m = Maps.newHashMap();
            String id = (String) it.next();
            // 去除特技发送
            if (StringUtils.startsWith(id, ItemID.SKILL)) {
                if (StringUtils.indexOf(id, "_n") > 0 || StringUtils.indexOf(id, "_u") > 0) {
                    continue;
                }
            }
            String name = "";
            if (JSONObject.fromObject(config.get(id)).containsKey("name")) {
                name = JSONObject.fromObject(config.get(id)).getString("name");
            }
            m.put("value", id);
            m.put("label", id + "-" + name);
            m.put("name", name);
            equipList.add(m);
        }
        addLordItem(equipList);
        return equipList;
    }

    /**
     * @Description:
     * @param equipList
     * @throws
     */
    private void addLordItem(List<Map<String, String>> equipList) {
        List<String> list = Lists.newArrayList();
        list.add(ItemID.DIAMOND);
        list.add(ItemID.GOLD);
        list.add(ItemID.EXP);
        list.add(ItemID.VIPSCORE);
        list.add(ItemID.STRENGTH);
        list.add(ItemID.ENERGY);
        list.add(ItemID.BREAKCOIN);
        list.add(ItemID.PVPGPLD);
        list.add(ItemID.SOUL_GOLD);
        for (String id : list) {
            Map<String, String> m = Maps.newHashMap();
            String name = getMessage("sys.item." + id);
            m.put("value", id);
            m.put("label", id + "-" + name);
            m.put("name", name);
            equipList.add(m);
        }
    }

    public Lord findLord(String id) {
        Lord lord = lordService.findOne(id);
        if (lord == null) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("sys.item.nonentity"));
        }
        return lord;
    }

    /**
     * @Description:
     * @param itemData
     * @param lord
     * @throws
     */
    public void send(String itemData, String lordData, String comment) {
        List<Map<String, Object>> itemList = JSONArray.fromObject(itemData);
        List<Map<String, String>> lordList = JSONArray.fromObject(lordData);
        if (itemList == null) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("sys.item.swal.item.null"));
        }
        if (lordList == null) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("sys.item.swal.lord.null"));
        }
        for (Map<String, String> lordMap : lordList) {
            Lord lord = lordService.findOne(lordMap.get("id"));
            Duel duel = duelRepository.findOne(lordMap.get("id"));
            LeagueLord leagueLord = leagueLordRepository.findOne(lordMap.get("id"));
            SoulStore soulStore = soulStoreRepository.findOne(lord.getId());
            ServletUtils.getRequest().setAttribute("currentLord_admin", lord);
            boolean isDuelChange = false;
            boolean isLordChange = false;
            boolean isLeagueLordChange = false;
            boolean isSoultStoreChange = false;
            for (Map<String, Object> m : itemList) {
                if (ItemID.PVPGPLD.equals(m.get("id").toString())) {
                    isDuelChange = true;
                    gainPayService.gain(duel, m.get("id").toString(), (int) m.get("amount"));
                } else if (ItemID.FUND.equals(m.get("id").toString())) {
                    isLeagueLordChange = true;
                    gainPayService.gain(leagueLord, m.get("id").toString(), (int) m.get("amount"));
                } else if (ItemID.SOUL_GOLD.equals(m.get("id").toString())) {
                    isSoultStoreChange = true;
                    if (soulStore == null) {
                        soulStore = new SoulStore();
                        // 初始化魂商店
                        soulStoreService.initSoulStore(soulStore);
                        soulStore.setId(lord.getId());
                    }
                    gainPayService.gain(soulStore, m.get("id").toString(), (int) m.get("amount"));
                } else {
                    isLordChange = true;
                    gainPayService.gain(lord, m.get("id").toString(), (int) m.get("amount"));
                }

            }
            if (isDuelChange) {
                duelRepository.save(duel);
            }
            if (isLordChange) {
                lordService.save(lord);
            }
            if (isLeagueLordChange) {
                leagueLordRepository.save(leagueLord);
            }
            if (isSoultStoreChange) {
                soulStoreRepository.save(soulStore);
            }
        }
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    public Page<Log> findAll() {
        Sort sort = new Sort(Direction.DESC, "createTime");
        SearchFilter searchFiter = SearchFilter.newSearchFilter(sort);
        searchFiter.addCriteria(Criteria.where("uri").is("/itemSend/send"));
        searchFiter.addCriteria(Criteria.where("method").is("POST"));
        return logRepository.findAll(searchFiter);
    }
}
