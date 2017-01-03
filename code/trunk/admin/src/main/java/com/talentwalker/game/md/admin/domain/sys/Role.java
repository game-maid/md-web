/**
 * @Title: Role.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月3日  闫昆
 */

package com.talentwalker.game.md.admin.domain.sys;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Role
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年5月3日 上午11:48:07
 */

@Document(collection = "admin_role")
public class Role extends BaseDomain {

    @NotBlank
    private String name;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Field("is_enable")
    private Boolean isEnable;

    @DBRef
    @Field("menu_list")
    private List<Menu> menuList;

    @Transient
    private String menuIds;

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
     * @return menuList
     */
    public List<Menu> getMenuList() {
        return menuList;
    }

    /**
     * @param menuList 要设置的 menuList
     */
    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
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
     * @return menuIds
     */
    public String getMenuIds() {
        return menuIds;
    }

    /**
     * @param menuIds 要设置的 menuIds
     */
    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

}
