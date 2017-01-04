/**
 * @Title: GameZoneBase.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月1日 闫昆
 */

package com.talentwalker.game.md.core.domain;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @ClassName: GameZoneBase
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月1日 下午3:14:58
 */

public class GameZoneBase extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    /**
     * 区服名字
     */
    @NotBlank
    private String name;
    /**
     * 状态
     */
    private int status;
    /**
     * 类型
     */
    private int type;
    /**
     * 显示顺序
     */
    private int sort;

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
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status 要设置的 status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type 要设置的 type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return sort
     */
    public int getSort() {
        return sort;
    }

    /**
     * @param sort 要设置的 sort
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

}
