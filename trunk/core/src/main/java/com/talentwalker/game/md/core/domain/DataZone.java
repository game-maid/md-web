/**
 * @Title: DataLogic.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月21日 闫昆
 */

package com.talentwalker.game.md.core.domain;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName: DataLogic
 * @Description: 数据逻辑服
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月21日 下午5:28:03
 */

@Document(collection = "portal_datalogic")
public class DataZone extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    @Field("is_enable")
    private Boolean isEnable;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Transient
    private List<GameZone> zoneList;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return isEnable
     */
    public Boolean getIsEnable() {
        return isEnable;
    }

    /**
     * @param isEnable 要设置的 isEnable
     */
    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate 要设置的 createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return zoneList
     */
    public List<GameZone> getZoneList() {
        return zoneList;
    }

    /**
     * @param zoneList 要设置的 zoneList
     */
    public void setZoneList(List<GameZone> zoneList) {
        this.zoneList = zoneList;
    }

}
