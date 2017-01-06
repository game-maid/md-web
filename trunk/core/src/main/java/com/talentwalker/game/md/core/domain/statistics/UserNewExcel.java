/**
 * @Title: UserNewExcel.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月4日  张福涛
 */

package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: UserNewExcel
 * @Description: 后台 数据报表 新增用户数Excel
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月4日 下午1:56:44
 */

public class UserNewExcel {
    /**
     * 编号 下标
     */
    private Integer index;
    /**
     * 区信息 区id-区名
     */
    private String zoneMes;
    /**
     * 包信息 包ID-包名
     */
    private String packageMes;
    /**
     * 玩家id
     */
    private String lordId;
    /*
     * 注册时间 yyyy/MM/dd HH:mm:ss
     */
    private String registerTime;

    /**
     * @return index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index 要设置的 index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return zoneMes
     */
    public String getZoneMes() {
        return zoneMes;
    }

    /**
     * @param zoneMes 要设置的 zoneMes
     */
    public void setZoneMes(String zoneMes) {
        this.zoneMes = zoneMes;
    }

    /**
     * @return packageMes
     */
    public String getPackageMes() {
        return packageMes;
    }

    /**
     * @param packageMes 要设置的 packageMes
     */
    public void setPackageMes(String packageMes) {
        this.packageMes = packageMes;
    }

    /**
     * @return lordId
     */
    public String getLordId() {
        return lordId;
    }

    /**
     * @param lordId 要设置的 lordId
     */
    public void setLordId(String lordId) {
        this.lordId = lordId;
    }

    /**
     * @return registerTime
     */
    public String getRegisterTime() {
        return registerTime;
    }

    /**
     * @param registerTime 要设置的 registerTime
     */
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

}
