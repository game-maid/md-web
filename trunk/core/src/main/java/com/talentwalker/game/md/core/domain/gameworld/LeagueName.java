/**
 * @Title: LeagueName.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: LeagueName
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 上午11:26:58
 */
@Document(collection = "game_league_name")
public class LeagueName extends BaseDomain {

    private static final long serialVersionUID = -9179117786630604531L;
    /**
     * 公会名称
     */
    protected String name;

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

}
