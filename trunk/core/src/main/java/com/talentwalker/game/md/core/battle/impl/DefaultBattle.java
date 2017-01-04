/**
 * @Title: DefalutBattle.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  占志灵
 */

package com.talentwalker.game.md.core.battle.impl;

import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.talentwalker.game.md.core.battle.BattleHero;
import com.talentwalker.game.md.core.battle.BattleLord;
import com.talentwalker.game.md.core.battle.BattleResult;
import com.talentwalker.game.md.core.util.RandomUtils;

/**
 * @ClassName: DefalutBattle
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月15日 上午10:44:53
 */

public class DefaultBattle {
    private static Timer TIMER = new Timer("battleTimer", true);

    private int roundMax;
    private int roundNow;
    private BattleResult result;
    private boolean end;
    private BattleLord battleLordAtk;
    private BattleLord battleLordDef;
    private int randomSeed;

    private ConcurrentLinkedDeque<BattleHero> heroQueue;
    private ConcurrentLinkedDeque<String> msgDeque;

    public void start() {
    }

    /**
     * @Description:获得当前轮，下一个出手的武将，如果当前轮没有可以出手的武将，则返回null
     * @return
     * @throws
     */
    private BattleHero pollNextHero() {
        BattleHero hero = heroQueue.poll();
        if (hero == null) {
            BattleHero heroAtk = battleLordAtk.peekNextHero();
            BattleHero heroDef = battleLordDef.peekNextHero();
            if (heroAtk != null || heroDef != null) {
                if (heroAtk == null) {
                    hero = heroDef;
                } else if (heroDef == null) {
                    hero = heroAtk;
                } else {
                    BattleHero heroAdd = null;
                    double speedAtk = heroAtk.getSpeed();
                    double speedDef = heroDef.getSpeed();
                    if (speedAtk > speedDef) {
                        hero = heroAtk;
                        heroAdd = heroDef;
                    } else if (speedAtk < speedDef) {
                        hero = heroDef;
                        heroAdd = heroAtk;
                    } else {
                        int random = RandomUtils.fakeRandomInt(1, 2, randomSeed);
                        if (random == 1) {
                            hero = heroAtk;
                            heroAdd = heroDef;
                        } else {
                            hero = heroDef;
                            heroAdd = heroAtk;
                        }
                    }
                    heroQueue.offer(heroAdd);
                }
            }
        }

        return hero;
    }

}
