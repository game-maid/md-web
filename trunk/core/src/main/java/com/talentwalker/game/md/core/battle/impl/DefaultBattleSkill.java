/**
 * @Title: BattleSkill.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月14日  占志灵
 */

package com.talentwalker.game.md.core.battle.impl;

import com.talentwalker.game.md.core.battle.BattlePoint;
import com.talentwalker.game.md.core.battle.BattleSkillAttr;
import com.talentwalker.game.md.core.domain.gameworld.Skill;

/**
 * @ClassName: BattleSkill
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月14日 下午3:36:14
 */

public class DefaultBattleSkill extends Skill {
    private static final long serialVersionUID = -6970144960053437231L;

    protected String name;
    protected String type;
    protected double launchRate;
    protected String weaponAda;
    protected String tarType;
    protected boolean tarIsEnemy;
    protected int tarTime;
    protected int tarNumber;
    protected BattlePoint[] aoeRange;
    protected String dmgType;
    protected BattleSkillAttr[] battleSkillAttrs;
}
