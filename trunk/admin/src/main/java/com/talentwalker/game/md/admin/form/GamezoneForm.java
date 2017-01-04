/**
 * @Title: GamezoneForm.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月18日 闫昆
 */
 

package com.talentwalker.game.md.admin.form;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.talentwalker.game.md.core.domain.GameZone;

/**
 * @ClassName: GamezoneForm
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月18日 下午6:36:26
 */

public class GamezoneForm extends GameZone {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    @NotBlank
    private String logicId;

    private String physicalId;

    /**
     * @return openTime
     */
    public Date getOpenTime() {
        return openTime;
    }

    /**
     * @param openTime 要设置的 openTime
     */
    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    /**
     * @return logicId
     */
    public String getLogicId() {
        return logicId;
    }

    /**
     * @param logicId 要设置的 logicId
     */
    public void setLogicId(String logicId) {
        this.logicId = logicId;
    }

    /**
     * @return physicalId
     */
    public String getPhysicalId() {
        return physicalId;
    }

    /**
     * @param physicalId 要设置的 physicalId
     */
    public void setPhysicalId(String physicalId) {
        this.physicalId = physicalId;
    }
    
}
