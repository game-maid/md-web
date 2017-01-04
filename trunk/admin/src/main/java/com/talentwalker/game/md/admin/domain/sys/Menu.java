/**
 * @Title: Menu.java
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
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Menu
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年5月3日 上午11:50:51
 */

@Document(collection = "admin_menu")
public class Menu extends BaseDomain {

    private String url;

    @NotBlank
    private String name;

    private String icon;

    private Integer sort;

    private String permission;

    @Field("is_enable")
    private Boolean isEnable;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @Field("parent_id")
    private String parentId;

    @Transient
    private List<Menu> childList;

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 要设置的 url
     */
    public void setUrl(String url) {
        this.url = url;
    }

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
     * @return icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon 要设置的 icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 要设置的 sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @param permission 要设置的 permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
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
     * @return parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId 要设置的 parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return childList
     */
    public List<Menu> getChildList() {
        return childList;
    }

    /**
     * @param childList 要设置的 childList
     */
    public void setChildList(List<Menu> childList) {
        this.childList = childList;
    }

}
