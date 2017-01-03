
package com.talentwalker.game.md.core.service;

import java.util.List;

import com.talentwalker.game.md.core.domain.GameZone;

public interface IGameZoneService {
    public List<GameZone> findAll();

    public GameZone add(GameZone gameZone);

    public void delete(GameZone gameZone);

    public GameZone update(String id, String condition, String value) throws Exception;

    public void deleteByIds(String... ids);

    public List<GameZone> findByType(int type);

    public List<GameZone> findExcludeType(int type);

    public GameZone findOne(String id);

    public List<GameZone> findByIdIn(List<String> ids);
}
