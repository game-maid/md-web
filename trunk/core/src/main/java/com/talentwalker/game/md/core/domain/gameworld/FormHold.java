/**
 * @Title: FormHold.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

/**
 * @ClassName: FormHold
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月13日 下午4:57:08
 */

public class FormHold implements Serializable {
    private static final long serialVersionUID = -3419939396427139678L;

    protected String heroUid;
    protected List<String> skillUid;
    protected List<String> equipUid;
    /**
     * 战斗力
     */
    @Transient
    protected int FP;

    /**
     * @return heroUid
     */
    public String getHeroUid() {
        return heroUid;
    }

    /**
     * @param heroUid 要设置的 heroUid
     */
    public void setHeroUid(String heroUid) {
        this.heroUid = heroUid;
    }

    /**
     * @return skillUid
     */
    public List<String> getSkillUid() {
        return skillUid;
    }

    /**
     * @param skillUid 要设置的 skillUid
     */
    public void setSkillUid(List<String> skillUid) {
        this.skillUid = skillUid;
    }

    /**
     * @return equipUid
     */
    public List<String> getEquipUid() {
        if (equipUid == null) {
            equipUid = new ArrayList<>();
        }
        return equipUid;
    }

    /**
     * @param equipUid 要设置的 equipUid
     */
    public void setEquipUid(List<String> equipUid) {
        this.equipUid = equipUid;
    }

    /**
     * @return fP
     */
    public int getFP() {
        return FP;
    }

    /**
     * @param fP 要设置的 fP
     */
    public void setFP(int fP) {
        FP = fP;
    }

}
