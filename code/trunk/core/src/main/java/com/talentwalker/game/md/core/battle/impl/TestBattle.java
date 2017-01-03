/**
 * @Title: DefalutBattle.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  占志灵
 */

package com.talentwalker.game.md.core.battle.impl;

import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.PriorityBlockingQueue;

import com.talentwalker.game.md.core.battle.Battle;
import com.talentwalker.game.md.core.battle.BattleHero;
import com.talentwalker.game.md.core.battle.BattleLord;
import com.talentwalker.game.md.core.battle.BattleResult;
import com.talentwalker.game.md.core.util.RandomUtils;

/**
 * @ClassName: DefalutBattle
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月15日 上午10:44:53
 */

public class TestBattle implements Battle {
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

    public TestBattle(BattleLord battleLordAtk, BattleLord battleLordDef) {
        this.battleLordAtk = battleLordAtk;
        this.battleLordDef = battleLordDef;
        msgDeque = new ConcurrentLinkedDeque<String>();
        roundMax = 30;
    }

    public void start() {
        TIMER.schedule(new ActionBegin(), 100);
    }

    public void insert(String msg) {
        msgDeque.offer(msg);
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

    private boolean hasNextHero() {
        boolean has = true;
        if (heroQueue.isEmpty()) {
            if ((!battleLordAtk.hasNextHero()) && (!battleLordDef.hasNextHero())) {
                refresh();
                if (end) {
                    has = false;
                }
            }
        }

        return has;
    }

    private void refresh() {
        if (roundNow >= roundMax) {
            end = true;
        } else {
            roundNow++;
            battleLordAtk.refresh();
            battleLordDef.refresh();
            if ((!battleLordAtk.hasNextHero()) && (!battleLordDef.hasNextHero())) {
                end = true;
            }
        }
    }

    private class ActionBegin extends TimerTask {

        @Override
        public void run() {
            String msg = "战斗开始";
            try {
                battleLordAtk.sendMessage(msg);
            } catch (Throwable e) {
            }
            try {
                battleLordDef.sendMessage(msg);
            } catch (Throwable e) {
            }
            TIMER.schedule(new ActionBuff(), 100);
        }

    }

    private class ActionBuff extends TimerTask {

        @Override
        public void run() {
            String msg = "双方加buff";
            try {
                battleLordAtk.sendMessage(msg);
            } catch (Throwable e) {
            }
            try {
                battleLordDef.sendMessage(msg);
            } catch (Throwable e) {
            }
            TIMER.schedule(new ActionFight(), 100);
        }

    }

    private class ActionFight extends TimerTask {

        @Override
        public void run() {
            String msg;
            String insertMsg = msgDeque.poll();
            if (insertMsg != null) {
                msg = insertMsg;
            } else {
                roundNow++;
                msg = roundNow % 2 == 0 ? "【" + battleLordAtk.getName() + "】飞起一脚，踢中了平爷的屁股"
                        : "【" + battleLordDef.getName() + "】抡起一拳，打中了昆哥的鼻梁";
            }

            try {
                battleLordAtk.sendMessage(msg);
            } catch (Throwable e) {
            }
            try {
                battleLordDef.sendMessage(msg);
            } catch (Throwable e) {
            }
            if (roundNow >= roundMax) {
                try {
                    battleLordAtk.sendMessage("战斗结束");
                } catch (Throwable e) {
                }
                try {
                    battleLordDef.sendMessage("战斗结束");
                } catch (Throwable e) {
                }
                try {
                    battleLordAtk.close();
                } catch (Throwable e) {
                }
                try {
                    battleLordDef.close();
                } catch (Throwable e) {
                }
            } else {
                TIMER.schedule(new ActionFight(), 2000);
            }
        }

    }

    public static void main(String[] args) {
        PriorityBlockingQueue<BattleHero> queue = new PriorityBlockingQueue<>(16, new Comparator<BattleHero>() {

            @Override
            public int compare(BattleHero hero1, BattleHero hero2) {
                if (hero1.getSpeed() > hero2.getSpeed()) {
                    return -1;
                } else if (hero1.getSpeed() < hero2.getSpeed()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

    }
}
