/**
 * @Title: ApplyPackageService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月24日  赵丽宝
 */

package com.talentwalker.game.md.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.domain.GamePackageBase;
import com.talentwalker.game.md.core.domain.Platform;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.GamePackageRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.PlatformRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

import net.sf.json.JSONArray;

/**
 * @ClassName: ApplyPackageService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年5月24日 下午5:46:17
 */
@Service
public class GamePackageService implements IGamePackageService {
    @Autowired
    private GamePackageRepository gamePackageRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameZoneRepository gameZoneRepository;

    /**.
     * <p>Title: findAll</p>
     * <p>Description: </p>
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#findAll()
     */
    @Override
    public List<GamePackage> findAll() {
        return gamePackageRepository.findAll();
    }

    /**.
     * <p>Title: save</p>
     * <p>Description: </p>
     * @param applyPackage
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#save(com.talentwalker.game.md.core.domain.GamePackage)
     */
    @Override
    public GamePackage save(GamePackage applyPackage) {
        applyPackage.setCreateDate(new Date());
        applyPackage.setPlatform(platformRepository.findOne(applyPackage.getPlatformId()));
        List<String> auditServerList = applyPackage.getServerAuditVisibleList();
        applyPackage.setServerAuditVisible(gameZoneRepository.findByIdIn(auditServerList));
        List<String> onlineServerList = applyPackage.getServerOnlineHideList();
        applyPackage.setServerOnlineHide(gameZoneRepository.findByIdIn(onlineServerList));
        return gamePackageRepository.save(applyPackage);
    }

    /**.
     * <p>Title: findAvailable</p>
     * <p>Description: </p>
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#findAvailable()
     */
    @Override
    public List<GamePackage> findAvailable() {
        // TODO Auto-generated method stub
        return null;
    }

    /**.
     * <p>Title: findOne</p>
     * <p>Description: </p>
     * @param id
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#findOne(java.lang.String)
     */
    @Override
    public GamePackage findOne(String id) {
        return gamePackageRepository.findOne(id);
    }

    public GamePackage findOneNotNull(String id) {
        GamePackage packageInfo = gamePackageRepository.findOne(id);
        if (packageInfo == null) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10002);
        }
        return packageInfo;
    }

    /**.
     * <p>Title: update</p>
     * <p>Description: </p>
     * @param id
     * @param versionBigOnline
     * @param versionBigTest
     * @param versionSmallOnline
     * @param versionSmallTest
     * @param versionSmallAudit
     * @return
     * @throws GameException
     * @see com.talentwalker.game.md.core.service.IGamePackageService#update(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public GamePackage update(String name, String value, String id) throws GameException {
        GamePackage applyPackage = gamePackageRepository.findOne(id);
        if (applyPackage == null) {
            return null;
        }
        if ("name".equals(name)) {
            applyPackage.setName(value);
        } else if ("platform".equals(name)) {
            Platform platform = platformRepository.findOne(value);
            if (platform != null) {
                applyPackage.setPlatform(platform);
            }
        } else if ("versionBigOnline".equals(name)) {
            applyPackage.setVersionBigOnline(value);
        } else if ("versionBigTest".equals(name)) {
            applyPackage.setVersionBigTest(value);
        } else if ("versionSmallOnline".equals(name)) {
            applyPackage.setVersionSmallOnline(value);
        } else if ("versionSmallTest".equals(name)) {
            applyPackage.setVersionSmallTest(value);
        } else if ("versionSmallAudit".equals(name)) {
            applyPackage.setVersionSmallAudit(value);
        }
        return gamePackageRepository.save(applyPackage);
    }

    /**.
     * <p>Title: updata</p>
     * <p>Description: </p>
     * @param applyPackage
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#updata(com.talentwalker.game.md.core.domain.GamePackage)
     */
    @Override
    public GamePackage updata(GamePackage applyPackage) {
        GamePackage newApplyPackage = gamePackageRepository.findOne(applyPackage.getId());
        newApplyPackage.setName(applyPackage.getName());
        newApplyPackage.setVersionBigOnline(applyPackage.getVersionBigOnline());
        newApplyPackage.setVersionBigTest(applyPackage.getVersionBigTest());
        newApplyPackage.setVersionSmallAudit(applyPackage.getVersionSmallAudit());
        newApplyPackage.setVersionSmallOnline(applyPackage.getVersionSmallOnline());
        newApplyPackage.setVersionSmallTest(applyPackage.getVersionSmallTest());
        newApplyPackage.setWhiteCdnUrl(applyPackage.getWhiteCdnUrl());
        newApplyPackage.setCdnUrl(applyPackage.getCdnUrl());

        newApplyPackage.setPlatform(platformRepository.findOne(applyPackage.getPlatformId()));
        List<String> auditServerList = applyPackage.getServerAuditVisibleList();
        newApplyPackage.setServerAuditVisible(gameZoneRepository.findByIdIn(auditServerList));
        List<String> onlineServerList = applyPackage.getServerOnlineHideList();
        newApplyPackage.setServerOnlineHide(gameZoneRepository.findByIdIn(onlineServerList));
        return gamePackageRepository.save(newApplyPackage);
    }

    /**
     * @Title: getPackageInfo
     * @Description:
     * @param packageId
     * @param ssoId
     * @param version
     * @return
     */
    public Object getPackageInfo(String packageId, String ssoId, String version) {
        GamePackage packageInfo = gamePackageRepository.findOne(packageId);
        if (null == packageInfo) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10002);
        }
        GamePackageBase packageBase = new GamePackageBase();
        packageBase.setId(packageId);
        packageBase.setVersionBigOnline(packageInfo.getVersionBigOnline());
        packageBase.setVersionSmallOnline(packageInfo.getVersionSmallOnline());
        packageBase.setCdnUrl(packageInfo.getCdnUrl());

        boolean isWhite = isWhite(packageInfo.getWhiteUid(), ssoId);
        if (isWhite) {
            packageBase.setVersionBigOnline(packageInfo.getVersionBigTest());
            packageBase.setCdnUrl(packageInfo.getWhiteCdnUrl());
            packageBase.setVersionSmallOnline(packageInfo.getVersionSmallTest());
        }

        if (version.compareTo(packageInfo.getVersionBigOnline()) > 0) {
            if (!isWhite) {
                packageBase.setVersionSmallOnline(packageInfo.getVersionSmallAudit());
            }
        }

        packageBase.setUpdate(getUpdateSet(packageInfo.getUpdateSet(), version));
        return packageBase;
    }

    /**
     * @Description: 根据版本号获取更新配置
     * @param setList 配置列表
     * @param version 大版本号
     * @return
     * @throws
     */
    private Map<String, String> getUpdateSet(List<Map<String, String>> setList, String version) {
        if (setList != null && setList.size() > 0) {
            for (Map<String, String> map : setList) {
                if (version.equals(MapUtils.getString(map, "version"))) {
                    return map;
                }
            }
        }
        return new HashMap<String, String>();
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

    /**.
     * <p>Title: updataWhite</p>
     * <p>Description: </p>
     * @param id
     * @param uids
     * @see com.talentwalker.game.md.core.service.IGamePackageService#updataWhite(java.lang.String, java.util.List)
     */
    @Override
    public GamePackage updataWhite(String id, String whiteUidList) {
        List<Map<String, String>> addWhiteUid = JSONArray.fromObject(whiteUidList);
        GamePackage newApplyPackage = gamePackageRepository.findOne(id);
        newApplyPackage.setWhiteUid(addWhiteUid);
        return gamePackageRepository.save(newApplyPackage);
    }

    /**.
     * <p>Title: updataConfigSet</p>
     * <p>Description: </p>
     * @param id
     * @param configs
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#updataConfigSet(java.lang.String, java.util.List)
     */
    @Override
    public GamePackage updataConfigSet(String id, String configs) {
        GamePackage newApplyPackage = gamePackageRepository.findOne(id);
        List<Map<String, String>> updataConfig = JSONArray.fromObject(configs);
        newApplyPackage.setUpdateSet(updataConfig);
        return gamePackageRepository.save(newApplyPackage);
    }

    /**.
     * <p>Title: deleteOneUid</p>
     * <p>Description: </p>
     * @param id
     * @param uid
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#deleteOneUid(java.lang.String, java.lang.String)
     */
    @Override
    public GamePackage deleteOneUid(String id, String uid) {
        GamePackage newApplyPackage = gamePackageRepository.findOne(id);
        List<Map<String, String>> uidList = newApplyPackage.getWhiteUid();
        for (int i = 0; i < uidList.size(); i++) {
            if (uidList.get(i).get("uid").equals(uid)) {
                uidList.remove(i);
            }
        }
        newApplyPackage.setWhiteUid(uidList);
        return gamePackageRepository.save(newApplyPackage);
    }

    /**.
     * <p>Title: deleteOneVersion</p>
     * <p>Description: </p>
     * @param id
     * @param version
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#deleteOneVersion(java.lang.String, java.lang.String)
     */
    @Override
    public GamePackage deleteOneVersion(String id, String version) {
        GamePackage newApplyPackage = gamePackageRepository.findOne(id);
        List<Map<String, String>> configList = newApplyPackage.getUpdateSet();
        for (int i = 0; i < configList.size(); i++) {
            if (configList.get(i).get("version").equals(version)) {
                configList.remove(i);
            }
        }
        newApplyPackage.setUpdateSet(configList);
        return gamePackageRepository.save(newApplyPackage);
    }

    /**.
     * <p>Title: delete</p>
     * <p>Description: </p>
     * @param id
     * @return
     * @see com.talentwalker.game.md.core.service.IGamePackageService#delete(java.lang.String)
     */
    @Override
    public void delete(String id) {
        gamePackageRepository.delete(id);
    }
}
