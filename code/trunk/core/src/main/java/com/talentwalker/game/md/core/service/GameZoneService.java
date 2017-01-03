
package com.talentwalker.game.md.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.DataZone;
import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.GameZoneBase;
import com.talentwalker.game.md.core.domain.PhysicalServer;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.DataZoneRepository;
import com.talentwalker.game.md.core.repository.GamePackageRepository;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.PhysicalRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

@Service
public class GameZoneService implements IGameZoneService {

    @Autowired
    private GameZoneRepository gameZoneRepository;

    @Autowired
    private DataZoneRepository dataLogicRepository;

    @Autowired
    private PhysicalRepository physicalRepository;

    @Autowired
    private GamePackageRepository applyPackageRepository;

    @Resource
    private GameUserRepository gameUserRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * .
     * <p>
     * Title: findAll
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.talentwalker.game.md.core.service.IGameZoneService#findAll()
     */
    @Override
    public List<GameZone> findAll() {
        return gameZoneRepository.findAll();
    }

    /**
     * .
     * <p>
     * Title: add
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param gameZone
     * @return
     * @see com.talentwalker.game.md.core.service.IGameZoneService#add(com.talentwalker.game.md.core.domain.GameZone)
     */
    @Override
    public GameZone add(GameZone gameZone) {
        return gameZoneRepository.insert(gameZone);
    }

    /**
     * .
     * <p>
     * Title: delete
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param gameZone
     * @see com.talentwalker.game.md.core.service.IGameZoneService#delete(com.talentwalker.game.md.core.domain.GameZone)
     */
    @Override
    public void delete(GameZone gameZone) {
        gameZoneRepository.delete(gameZone);
    }

    /**
     * @Title: update
     * @Description:
     * @param condition
     * @param value
     * @return
     * @throws GameException
     * @see com.talentwalker.game.md.core.service.IGameZoneService#update(java.lang.String, java.lang.String)
     */
    @Override
    public GameZone update(String id, String condition, String value) throws Exception {
        GameZone gamezone = gameZoneRepository.findOne(id);
        if (null == gamezone) {
            return null;
        }

        if ("name".equals(condition)) {
            gamezone.setName(value);
        } else if ("status".equals(condition)) {
            gamezone.setStatus(NumberUtils.toInt(value));
        } else if ("type".equals(condition)) {
            gamezone.setType(NumberUtils.toInt(value));
        } else if ("sort".equals(condition)) {
            gamezone.setSort(NumberUtils.toInt(value));
        } else if ("startTime".equals(condition)) {
            gamezone.setStartTime(DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss").getTime());
        } else if ("dataLogic".equals(condition)) {
            DataZone dataLogic = dataLogicRepository.findOne(value);
            if (null != dataLogic && dataLogic.getIsEnable()) {
                gamezone.setDataLogic(dataLogic);
            }
        } else if ("physicalServer".equals(condition)) {
            PhysicalServer server = physicalRepository.findOne(value);
            gamezone.setPhysicalServer(server);
        }
        return gameZoneRepository.save(gamezone);
    }

    /**
     * @Title: deleteByIds
     * @Description:
     * @param ids
     * @see com.talentwalker.game.md.core.service.IGameZoneService#deleteByIds(java.lang.String[])
     */
    @Override
    public void deleteByIds(String... ids) {
        for (String id : ids) {
            gameZoneRepository.delete(id);
        }
    }

    /**
     * @Title: findByType
     * @Description:
     * @param type
     * @return
     * @see com.talentwalker.game.md.core.service.IGameZoneService#findByType(int)
     */
    @Override
    public List<GameZone> findByType(int type) {
        return gameZoneRepository.findByType(type);
    }

    /**
     * @Title: findExcludeType
     * @Description:
     * @param type
     * @return
     * @see com.talentwalker.game.md.core.service.IGameZoneService#findExcludeType(int)
     */
    @Override
    public List<GameZone> findExcludeType(int type) {
        return gameZoneRepository.findByTypeNotIn(GameZone.TYPE_AUDIT);
    }

    /**
     * @Description: 获取服务器列表
     * @param packageId 游戏包ID
     * @param ssoId 玩家账号ID
     * @param version 游戏包大版本号
     * @return
     * @throws
     */
    public List<GameZoneBase> getZoneList(String packageId, String ssoId, String version) {
        List<GameZone> zoneList = gameZoneRepository.findAll();

        GamePackage pkg = applyPackageRepository.findOne(packageId);
        if (null == pkg) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10002);
        }
        if (!isWhite(pkg.getWhiteUid(), ssoId)) {
            if (version.compareTo(pkg.getVersionBigOnline()) <= 0) {
                zoneList = filterOnline(zoneList, pkg.getServerOnlineHide());
            } else {
                zoneList = filterAudit(zoneList, pkg.getServerAuditVisible());
            }
        }

        List<GameZoneBase> list = new ArrayList<GameZoneBase>();
        for (GameZone zone : zoneList) {
            GameZoneBase base = new GameZoneBase();
            base.setId(zone.getId());
            base.setName(zone.getName());
            base.setType(zone.getType());
            base.setStatus(zone.getStatus());
            base.setSort(zone.getSort());
            list.add(base);
        }
        return list;
    }

    /**
     * @Description:获取曾用服ids
     * @param ssoId
     * @return
     * @throws
     */
    public List<String> getUsedZoneIds(String ssoId) {
        List<GameUser> usedGameUser = gameUserRepository.findBySsoId(ssoId);
        List<String> lordIds = new ArrayList<>();
        for (GameUser gameUser : usedGameUser) {
            lordIds.add(gameUser.getId());
        }
        // 按登录时间降序排列
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(lordIds));
        query.with(new Sort(Direction.DESC, "lastTime"));
        List<Lord> lords = mongoTemplate.find(query, Lord.class);
        List<String> usedZoneIds = new ArrayList<>();
        for (Lord lord : lords) {
            for (GameUser gameUser : usedGameUser) {
                if (gameUser.getId().equals(lord.getId()) && !usedZoneIds.contains(gameUser.getGameZoneId())) {
                    usedZoneIds.add(gameUser.getGameZoneId());
                }
            }
        }
        return usedZoneIds;
    }

    /**
     * @Description: 正式包过滤需要屏蔽的正式服
     * @param allList
     * @param hidenList
     * @return
     * @throws
     */
    private List<GameZone> filterOnline(List<GameZone> allList, List<GameZone> hidenList) {
        List<GameZone> list = new ArrayList<GameZone>();
        for (GameZone zone : allList) {
            if (zone.getType() == GameZone.TYPE_ONLINE) {
                if (hidenList != null && hidenList.size() > 0) {
                    boolean flag = false;
                    for (GameZone hidden : hidenList) {
                        if (zone.getId().equals(hidden.getId())) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        list.add(zone);
                    }
                } else {
                    list.add(zone);
                }

            }
        }
        return list;
    }

    /**
     * @Description: 审核包添加可见的服
     * @param allList
     * @param visibleList
     * @return
     * @throws
     */
    private List<GameZone> filterAudit(List<GameZone> allList, List<GameZone> visibleList) {
        List<GameZone> list = new ArrayList<GameZone>();
        for (GameZone zone : allList) {
            if (zone.getType() == GameZone.TYPE_AUDIT) {
                list.add(zone);
            } else {
                if (visibleList != null && visibleList.size() > 0) {
                    for (GameZone visible : visibleList) {
                        if (zone.getId().equals(visible.getId())) {
                            list.add(zone);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * @Description: 用户是否在白名单里
     * @param whiteList
     * @param ssoId
     * @return
     * @throws
     */
    private boolean isWhite(List<Map<String, String>> whiteList, String ssoId) {
        if (whiteList != null && whiteList.size() > 0) {
            for (Map<String, String> whiteMap : whiteList) {
                if (ssoId.equals(MapUtils.getString(whiteMap, "uid"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public GameZone findOneNotNull(String id) {
        GameZone gameZone = gameZoneRepository.findOne(id);
        if (gameZone == null) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10001);
        }
        return gameZone;
    }

    /**.
     * <p>Title: findOne</p>
     * <p>Description: </p>
     * @param id
     * @return
     * @see com.talentwalker.game.md.core.service.IGameZoneService#findOne(java.lang.String)
     */
    @Override
    public GameZone findOne(String id) {
        return gameZoneRepository.findOne(id);
    }

    /**
     * <p>Title: findByIdIn</p>
     * <p>Description: </p>
     * @param ids
     * @return
     * @see com.talentwalker.game.md.core.service.IGameZoneService#findByIdIn(java.util.List)
     */
    @Override
    public List<GameZone> findByIdIn(List<String> ids) {
        return gameZoneRepository.findByIdIn(ids);
    }

}
