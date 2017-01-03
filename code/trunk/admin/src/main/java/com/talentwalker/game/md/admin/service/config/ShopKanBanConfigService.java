
package com.talentwalker.game.md.admin.service.config;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.config.ShopKanbanConfig;
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.config.ShopKanbanConfigRepository;
import com.talentwalker.game.md.core.repository.support.SearchFilter;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

import net.sf.json.JSONObject;

@Service
public class ShopKanBanConfigService extends BaseService {
    @Resource
    private IDataConfigManager iDataConfigManager;

    @Resource
    private ShopKanbanConfigRepository kanbanConfigRepository;

    /**
     * @Description:按序位降序排列，显示看板娘活动列表
     * @return
     * @throws
     */
    public Page<ShopKanbanConfig> findList() {
        Sort sort = new Sort(Direction.ASC, "putrush");
        SearchFilter searchFilter = SearchFilter.newSearchFilter(sort);
        return kanbanConfigRepository.findAll(searchFilter);
    }

    /**
     * @Description:初始化看板娘配置信息
     * @return
     * @throws
     */
    public List<Map<String, Object>> initConfig() {
        List<Map<String, Object>> list = Lists.newArrayList();
        DataConfig config = iDataConfigManager.getTest().get("kanban_sell");
        Iterator it = config.getJsonObject().keys();
        while (it.hasNext()) {
            Map<String, Object> m = Maps.newHashMap();
            String id = (String) it.next();
            String name = "";
            if (config.get(id).getJsonObject().containsKey("name")) {
                name = config.get(id).getString("name");
            }
            if (config.get(id).getJsonObject().containsKey("itemId")
                    && config.get(id).getJsonObject().containsKey("count")) {
                String itemId = config.get(id).getString("itemId");
                Integer count = config.get(id).getInteger("count");
                m.put("value", id);
                m.put("label", id + "-" + name);
                m.put("name", name);
                m.put("itemId", itemId);
                m.put("count", count);
                list.add(m);
            }
        }
        return list;
    }

    /**
     * @Description:添加（修改）看板娘活动
     * @param kanBan
     * @throws
     */
    public void add(ShopKanbanConfig kanBan) {
        ShopKanbanConfig shopKanBan = new ShopKanbanConfig();
        // 时间校验
        long startTime = 0L;
        long endTime = 0L;
        try {
            startTime = DateUtils.parseDate(kanBan.getStartDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            endTime = DateUtils.parseDate(kanBan.getEndDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            if (endTime <= startTime) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("config.shop.recruit.time.error"));
            }
            // 时间校验
            shopKanBan.setStartTime(startTime);
            shopKanBan.setEndTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 看板娘折扣信息
        Map<String, Integer> goods = JSONObject.fromObject(kanBan.getGoods());
        List<String> zoneListCur = kanBan.getZoneList();// 当前活动的区服列表
        /**
         * 看板娘校验
         * 同一时间内不允许，相同看板娘出现在两个活动中
         * 开始时间小于当前活动的结束时间，或结束时间大于当前活动的开始时间 则为有时间重复的两个活动。
         */
        List<ShopKanbanConfig> list = kanbanConfigRepository.findByDate(startTime, endTime);
        for (ShopKanbanConfig shopKanbanConfig : list) {// 时间冲突的活动信息
            if (shopKanbanConfig.getId().equals(kanBan.getId())) {
                continue;
            }
            Map<String, Integer> good = shopKanbanConfig.getGood();
            if (!good.isEmpty()) {
                Set<String> kanBanIds = good.keySet();
                List<String> zoneList = shopKanbanConfig.getZoneList();// 时间冲突的区服列表
                for (String kanBanId : kanBanIds) {// 时间冲突的看板娘列表
                    if (goods.containsKey(kanBanId)) {
                        for (String zoneId : zoneList) {// 时间冲突，看板娘冲突，检查是否区服冲突
                            if (zoneListCur.contains(zoneId)) {
                                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT,
                                        getMessage("config.shop.kanban.repeat.time"));
                            }
                        }
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(kanBan.getId())) {// 如果id不为空 则查询数据库 进行修改
            shopKanBan = kanbanConfigRepository.findOne(new ObjectId(kanBan.getId()));
        }
        shopKanBan.setName(kanBan.getName());
        shopKanBan.setEndDate(kanBan.getExplain());
        shopKanBan.setZoneList(zoneListCur);
        shopKanBan.setPutrush(kanBan.getPutrush());
        shopKanBan.setExplain(kanBan.getExplain());
        shopKanBan.setGood(goods);
        kanbanConfigRepository.save(shopKanBan);
    }

    /**
     * @Description:
     * @param id 活动Id
     * @throws
     */
    public void deleteById(String id) {
        kanbanConfigRepository.delete(new ObjectId(id));
    }

    /**
     * @Description:修改数值类型，一个字段的值
     * @param id
     * @param field
     * @param num
     * @throws
     */
    public void update(String id, String field, Integer num) {
        ShopKanbanConfig kanban = kanbanConfigRepository.findOne(new ObjectId(id));
        if ("putrush".equals(field)) {
            kanban.setPutrush(num);
        }
        kanbanConfigRepository.save(kanban);
    }

}
