/**
 * @Title: IDataLogicService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月21日 闫昆
 */

package com.talentwalker.game.md.core.service;

import java.util.List;
import java.util.Map;

import com.talentwalker.game.md.core.domain.DataZone;
import com.talentwalker.game.md.core.exception.GameException;

/**
 * @ClassName: IDataLogicService
 * @Description: 数据服操作接口
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月21日 下午5:47:42
 */

public interface IDataZoneService {

    public List<DataZone> findAll();

    public DataZone save(DataZone dataLogic);

    public DataZone update(DataZone dataLogic) throws GameException;

    public List<DataZone> findAvailable();

    public DataZone findOne(String id);

    public List<DataZone> showView();

    public List<Map<String, String>> getZoneInfo(String logicId);

    public void adjustZone(String logicId, String[] zoneIds);

    public void delete(String id);

}
