/**
 * @Title: User.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月29日  闫昆
 */

package com.talentwalker.game.md.admin.domain.sys;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: User
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月29日 上午10:57:34
 */

@Document(collection = "admin_user")
public class User extends BaseDomain {

    /**
     * 登录名
     */
    @NotBlank
    @Indexed(unique = true)
    private String username;

    private String password;

    private String salt;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @DBRef
    @Field("role_list")
    private List<Role> roleList;

    @Field("is_enable")
    private Boolean isEnable;

    @Field("is_super")
    private Boolean isSuper;

    @JsonIgnore
    @Transient
    private List<String> roles;

    /**
     * 用户姓名
     */
    @NotBlank
    private String name;

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username 要设置的 username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password 要设置的 password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt 要设置的 salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return roleList
     */
    public List<Role> getRoleList() {
        return roleList;
    }

    /**
     * @param roleList 要设置的 roleList
     */
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
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
     * @return isSuper
     */
    public Boolean getIsSuper() {
        return isSuper;
    }

    /**
     * @param isSuper 要设置的 isSuper
     */
    public void setIsSuper(Boolean isSuper) {
        this.isSuper = isSuper;
    }

    /**
     * @return roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @param roles 要设置的 roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
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

}
