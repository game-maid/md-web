/**
 * @Title: PhysicalService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月30日 闫昆
 */

package com.talentwalker.game.md.core.service;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.PhysicalServer;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.PhysicalRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: PhysicalService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月30日 上午10:25:27
 */

@Service
public class PhysicalService implements IPhysicalService {

    @Autowired
    private PhysicalRepository physicalRepository;

    @Autowired
    private GameZoneRepository gameZoneRepository;

    /**
     * @Title: findAll
     * @Description:
     * @return
     * @see com.talentwalker.game.md.core.service.IPhysicalService#findAll()
     */
    @Override
    public List<PhysicalServer> findAll() {
        return physicalRepository.findAll();
    }

    /**
     * @Title: save
     * @Description:
     * @param server
     * @return
     * @see com.talentwalker.game.md.core.service.IPhysicalService#save(com.talentwalker.game.md.core.domain.PhysicalServer)
     */
    @Override
    public PhysicalServer save(PhysicalServer server) {
        server.setCreateDate(new Date());
        server.setId(null);
        return physicalRepository.save(server);
    }

    /**
     * @Title: update
     * @Description:
     * @param server
     * @return
     * @see com.talentwalker.game.md.core.service.IPhysicalService#update(com.talentwalker.game.md.core.domain.PhysicalServer)
     */
    @Override
    public PhysicalServer update(PhysicalServer server) {
        PhysicalServer p = physicalRepository.findOne(server.getId());
        if (null != p) {
            p.setContext(server.getContext());
            p.setHost(server.getHost());
            p.setPort(server.getPort());
            p.setName(server.getName());
            physicalRepository.save(p);
        }
        return p;
    }

    /**
     * @Title: findOne
     * @Description:
     * @param physicalId
     * @return
     * @see com.talentwalker.game.md.core.service.IPhysicalService#findOne(java.lang.String)
     */
    @Override
    public PhysicalServer findOne(String physicalId) {
        return physicalRepository.findOne(physicalId);
    }

    /**
     * @Title: delete
     * @Description:
     * @param id
     * @see com.talentwalker.game.md.core.service.IPhysicalService#delete(java.lang.String)
     */
    @Override
    public void delete(String id) {
        List<GameZone> list = gameZoneRepository.findByPhysicalId(new ObjectId(id));
        if (list != null && list.size() > 0) {
            GameExceptionUtils.throwException("sys.physical.delete.fail");
        }

        physicalRepository.delete(id);
    }

}
