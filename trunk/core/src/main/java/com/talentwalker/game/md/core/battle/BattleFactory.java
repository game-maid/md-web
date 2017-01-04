/**
 * @Title: BattleFactory.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  占志灵
 */

package com.talentwalker.game.md.core.battle;

import com.talentwalker.game.md.core.battle.impl.TestBattle;

/**
 * @ClassName: BattleFactory
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月15日 上午10:43:41
 */

public class BattleFactory {
    public static Battle createBattle(BattleLord lord1, BattleLord lord2) {
        return new TestBattle(lord1, lord2);
    }
}
