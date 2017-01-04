/**
 * @Title: LordInfoService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月27日  赵丽宝
 */

package com.talentwalker.game.md.admin.service.gmtool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Lordname;
import com.talentwalker.game.md.core.domain.gameworld.Stage;
import com.talentwalker.game.md.core.domain.gameworld.StageComposite;
import com.talentwalker.game.md.core.domain.gameworld.StageReward;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordnameRepository;
import com.talentwalker.game.md.core.repository.gameworld.StageRepository;

/**
 * @ClassName: LordInfoService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月27日 下午1:46:24
 */
@Service
public class LordInfoService extends BaseService {
    @Autowired
    private LordnameRepository lordnameRepository;
    @Autowired
    private LordRepository lordRepository;
    @Autowired
    private GameUserRepository gameUserRepository;
    @Autowired
    private GameZoneRepository gameZoneRepository;
    @Autowired
    private StageRepository stageRepository;

    /**
     * @Description:
     * @param type 1：按玩家Id查询，2：按玩家姓名查询
     * @param id 玩家id
     * @param gamezone 区服
     * @throws
     */
    public Lord find(Integer type, String id, String gamezone) {
        if (type == 2) {
            GameZone zone = gameZoneRepository.findOne(gamezone);
            Lordname lordname = lordnameRepository.findByNameAndDataLogicId(id, zone.getDataLogic().getId());
            if (lordname == null) {
                return null;
            }
            id = lordname.getPlayerId();
        }
        Lord lord = lordRepository.findOne(id);
        return lord;
    }

    public GameUser findUser(String id) {
        return gameUserRepository.findOne(id);
    }

    public StageComposite getStageInfo(String playerId) {
        StageComposite sc = stageRepository.findOne(playerId);
        if (sc == null) {
            sc = new StageComposite();
            sc.setStages(new HashMap<String, Stage>());
            sc.setRewards(new HashMap<String, Map<String, StageReward>>());
        }
        return sc;
    }

    /**|
     * @Description:根据多个id查询玩家list
     * @param ids
     * @return
     * @throws
     */
    public List<Lord> findByIdIn(List<String> ids) {
        return lordRepository.findByIdIn(ids);
    }
}
