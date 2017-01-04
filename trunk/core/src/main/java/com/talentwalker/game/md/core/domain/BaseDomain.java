
package com.talentwalker.game.md.core.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseDomain implements Serializable {
    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = -8776895662369643614L;
    @Id
    protected String id;
    @JsonIgnore
    @Version
    protected Long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
