/**
 * @Title: BattleHero.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月14日  占志灵
 */

package com.talentwalker.game.md.core.battle.impl;

import com.talentwalker.game.md.core.battle.BattleSkill;
import com.talentwalker.game.md.core.domain.gameworld.Hero;

/**
 * @ClassName: BattleHero
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月14日 下午12:13:45
 */

public class DefaultBattleHero extends Hero {
    private static final long serialVersionUID = 7193939610520742915L;

    protected String name;
    protected int camp;
    protected String weaponType;
    protected int hp;
    protected int hpMax;
    protected int mp;
    protected int def;
    protected int atk;
    protected int hit;
    protected double basicHit;
    protected int dod;
    protected double basicDod;
    protected int cri;
    protected double basicCri;
    protected int resi;
    protected double cridmg;
    protected double speed;
    protected double apEff;
    protected double spEff;
    protected double healEff;
    protected BattleSkill[] skills;

}
