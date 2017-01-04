/**
 * @Title: DuelRank.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月7日  赵丽宝
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: DuelRank
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年7月7日 下午6:15:15
 */
@Document
public class DuelRank extends BaseDomain {
    protected int rank;

    /**
     * @return rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank 要设置的 rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

}
