/**
 * @Title: ShopRecruit.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月3日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: ShopRecruit
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月3日 下午2:34:47
 */
@Document(collection = "game_shop_recruit")
public class ShopRecruit extends BaseDomain {
    private static final long serialVersionUID = -2399943862869250607L;
    /**
     * 招募活动列表
     */
    protected Map<String, Recruit> recruit;

    /**
     * @return recruit
     */
    public Map<String, Recruit> getRecruit() {
        return recruit;
    }

    /**
     * @param recruit 要设置的 recruit
     */
    public void setRecruit(Map<String, Recruit> recruit) {
        this.recruit = recruit;
    }

}
