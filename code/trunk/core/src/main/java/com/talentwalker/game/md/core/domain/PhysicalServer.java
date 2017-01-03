/**
 * @Title: PhysicalServer.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月30日 闫昆
 */

package com.talentwalker.game.md.core.domain;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName: PhysicalServer
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月30日 上午10:22:17
 */

@Document(collection = "portal_physical")
public class PhysicalServer extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    @NotBlank
    private String host;

    @NotBlank
    private String port;

    private String context;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host 要设置的 host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port 要设置的 port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context 要设置的 context
     */
    public void setContext(String context) {
        this.context = context;
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
