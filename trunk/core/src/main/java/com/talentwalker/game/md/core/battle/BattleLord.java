/**
 * @Title: BattleLord.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  占志灵
 */

package com.talentwalker.game.md.core.battle;

import java.io.IOException;

/**
 * @ClassName: BattleLord
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月15日 上午10:37:45
 */

public interface BattleLord {
    boolean hasNextHero();

    BattleHero peekNextHero();

    BattleHero pollNextHero();

    void refreshActionSort();

    void refresh();

    void sendMessage(String message) throws Exception;

    String getName();

    void close() throws IOException;
}
