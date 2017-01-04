/**
 * @Title: IPhysicalService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月30日 闫昆
 */
 

package com.talentwalker.game.md.core.service;

import java.util.List;

import com.talentwalker.game.md.core.domain.PhysicalServer;

/**
 * @ClassName: IPhysicalService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月30日 上午10:24:10
 */

public interface IPhysicalService {

    public List<PhysicalServer> findAll();

    public PhysicalServer save(PhysicalServer server);

    public PhysicalServer update(PhysicalServer server);

    public PhysicalServer findOne(String physicalId);

    public void delete(String id);

}
