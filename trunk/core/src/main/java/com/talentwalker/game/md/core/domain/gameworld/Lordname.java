/**
 * @Title: Lordname.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月27日 闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Lordname
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月27日 下午1:20:56
 */

@Document(collection = "game_lordname")
@CompoundIndex(name = "idx_gamezone_name", def = "{name:1,logic_id:1}", unique = true)
public class Lordname extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    public Lordname(String playerId, String name, String dataLogicId) {
        this.name = name;
        this.dataLogicId = dataLogicId;
        this.playerId = playerId;
    }

    @Field("player_id")
    private String playerId;

    private String name;

    @Field("logic_id")
    private String dataLogicId;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return dataLogicId
     */
    public String getDataLogicId() {
        return dataLogicId;
    }

    /**
     * @param dataLogicId 要设置的 dataLogicId
     */
    public void setDataLogicId(String dataLogicId) {
        this.dataLogicId = dataLogicId;
    }

    /**
     * @return playerId
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId 要设置的 playerId
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
