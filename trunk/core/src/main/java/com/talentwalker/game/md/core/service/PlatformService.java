/**
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月24日 闫昆
 */

package com.talentwalker.game.md.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.domain.Platform;
import com.talentwalker.game.md.core.repository.GamePackageRepository;
import com.talentwalker.game.md.core.repository.PlatformRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: PlatformService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月24日 下午4:05:23
 */

@Service
public class PlatformService implements IPlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GamePackageRepository applyPackageRepository;

    /**
     * @Title: findAll
     * @Description:
     * @return
     * @see com.talentwalker.game.md.core.service.IPlatformService#findAll()
     */
    @Override
    public List<Platform> findAll() {
        return platformRepository.findAll();
    }

    /**
     * @Title: save
     * @Description:
     * @param platform
     * @return
     * @see com.talentwalker.game.md.core.service.IPlatformService#save(com.talentwalker.game.md.core.domain.Platform)
     */
    @Override
    public Platform save(Platform platform) {
        platform.setCreateDate(new Date());
        return platformRepository.save(platform);
    }

    /**
     * @Title: update
     * @Description:
     * @param platform
     * @return
     * @see com.talentwalker.game.md.core.service.IPlatformService#update(com.talentwalker.game.md.core.domain.Platform)
     */
    @Override
    public Platform update(Platform platform) {
        Platform p = platformRepository.findOne(platform.getId());
        if (null != p) {
            p.setName(platform.getName());
            platformRepository.save(p);
        }
        return p;
    }

    /**
     * @Title: showView
     * @Description:
     * @return
     * @see com.talentwalker.game.md.core.service.IPlatformService#showView()
     */
    @Override
    public List<Platform> showView() {
        List<Platform> list = platformRepository.findAll();
        for (Platform platform : list) {
            platform.setPackageList(applyPackageRepository.findById(platform.getId()));
        }
        return list;
    }

    /**
     * @Title: delete
     * @Description:
     * @param id
     * @see com.talentwalker.game.md.core.service.IPlatformService#delete(java.lang.String)
     */
    @Override
    public void delete(String id) {
        List<GamePackage> pList = applyPackageRepository.findById(id);
        if (pList != null && pList.size() > 0) {
            GameExceptionUtils.throwException("sys.platform.delete.fail");
        }

        platformRepository.delete(id);
    }

    /**.
     * <p>Title: findOne</p>
     * <p>Description: </p>
     * @return
     * @see com.talentwalker.game.md.core.service.IPlatformService#findOne()
     */
    @Override
    public Platform findOne(String id) {
        return platformRepository.findOne(id);

    }

}
