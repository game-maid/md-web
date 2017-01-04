/**
 * @Title: DataLogicService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月21日 闫昆
 */

package com.talentwalker.game.md.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.DataZone;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.DataZoneRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: DataLogicService
 * @Description: 数据服操作实现
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月21日 下午5:48:27
 */

@Service
public class DataZoneService implements IDataZoneService {

    @Autowired
    private DataZoneRepository dataLogicRepository;

    @Autowired
    private GameZoneRepository gameZoneRepository;

    /**
     * @Title: findAll
     * @Description:
     * @return
     * @see com.talentwalker.game.md.core.service.IDataZoneService#findAll()
     */
    @Override
    public List<DataZone> findAll() {
        return dataLogicRepository.findAll();
    }

    /**
     * @Title: save
     * @Description:
     * @param dataLogic
     * @return
     * @see com.talentwalker.game.md.core.service.IDataZoneService#save(com.talentwalker.game.md.core.domain.DataZone)
     */
    @Override
    public DataZone save(DataZone dataLogic) {
        dataLogic.setCreateDate(new Date());
        return dataLogicRepository.save(dataLogic);
    }

    /**
     * @Title: update
     * @Description:
     * @param dataLogic
     * @return
     * @throws GameException
     * @see com.talentwalker.game.md.core.service.IDataZoneService#update(com.talentwalker.game.md.core.domain.DataZone)
     */
    @Override
    public DataZone update(DataZone dataLogic) throws GameException {
        DataZone logic = dataLogicRepository.findOne(dataLogic.getId());
        if (null != logic) {
            logic.setName(dataLogic.getName());
            // 如果要禁用，需保证此数据服下面没有关联的gamezone
            if (!dataLogic.getIsEnable()) {
                List<GameZone> list = gameZoneRepository.findByLogicId(dataLogic.getId());
                if (null != list && list.size() > 0) {
                    GameExceptionUtils.throwException("Update Failed");
                }
            }
            logic.setIsEnable(dataLogic.getIsEnable());
            dataLogicRepository.save(logic);
        }
        return logic;
    }

    /**
     * @Title: findAvailable
     * @Description:
     * @return
     * @see com.talentwalker.game.md.core.service.IDataZoneService#findAvailable()
     */
    @Override
    public List<DataZone> findAvailable() {
        return dataLogicRepository.findByIsEnable(Boolean.TRUE);
    }

    /**
     * @Title: findOne
     * @Description:
     * @param id
     * @return
     * @see com.talentwalker.game.md.core.service.IDataZoneService#findOne(java.lang.String)
     */
    @Override
    public DataZone findOne(String id) {
        return dataLogicRepository.findOne(id);
    }

    /**
     * @Title: showView
     * @Description:
     * @return
     * @see com.talentwalker.game.md.core.service.IDataZoneService#showView()
     */
    @Override
    public List<DataZone> showView() {
        List<DataZone> list = findAll();
        for (DataZone logic : list) {
            logic.setZoneList(gameZoneRepository.findByLogicId(logic.getId()));
        }
        return list;
    }

    /**
     * @Title: getZoneInfo
     * @Description:
     * @param logicId
     * @return
     * @see com.talentwalker.game.md.core.service.IDataZoneService#getZoneInfo(java.lang.String)
     */
    @Override
    public List<Map<String, String>> getZoneInfo(String logicId) {
        List<GameZone> list = gameZoneRepository.findByLogicId(logicId);
        List<GameZone> all = gameZoneRepository.findAll();
        List<Map<String, String>> rList = new ArrayList<Map<String, String>>();
        for (GameZone zone : all) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", zone.getId());
            map.put("text", zone.getName());
            for (GameZone have : list) {
                if (zone.getId().equals(have.getId())) {
                    map.put("option", "selected");
                    break;
                }
            }
            rList.add(map);
        }
        return rList;
    }

    /**
     * @Title: adjustZone
     * @Description:
     * @param logicId
     * @param zoneIds
     * @see com.talentwalker.game.md.core.service.IDataZoneService#adjustZone(java.lang.String, java.lang.String[])
     */
    @Override
    public void adjustZone(String logicId, String[] zoneIds) {
        if (zoneIds.length <= 0) {
            return;
        }

        DataZone logic = dataLogicRepository.findOne(logicId);
        if (null == logic) {
            return;
        }

        for (String zoneId : zoneIds) {
            GameZone gamezone = gameZoneRepository.findOne(zoneId);
            if (null != gamezone) {
                gamezone.setDataLogic(logic);
                gameZoneRepository.save(gamezone);
            }
        }
    }

    /**
     * @Title: delete
     * @Description:
     * @param id
     * @see com.talentwalker.game.md.core.service.IDataLogicService#delete(java.lang.String)
     */
    @Override
    public void delete(String id) {
        List<GameZone> list = gameZoneRepository.findByLogicId(id);
        if (list != null && list.size() > 0) {
            GameExceptionUtils.throwException("sys.datalogic.delete.fail");
        }

        dataLogicRepository.delete(id);
    }

}
