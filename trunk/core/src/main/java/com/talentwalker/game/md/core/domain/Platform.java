/**
 * @Title: Platform.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月24日 闫昆
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
 * @ClassName: Platform
 * @Description: 平台
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月24日 下午3:59:48
 */

@Document(collection = "portal_platform")
public class Platform extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Transient
    private List<GamePackage> packageList;

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
     * @return packageList
     */
    public List<GamePackage> getPackageList() {
        return packageList;
    }

    /**
     * @param packageList 要设置的 packageList
     */
    public void setPackageList(List<GamePackage> packageList) {
        this.packageList = packageList;
    }

}
