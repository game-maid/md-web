
package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: ActiveAndPersistenceExcel
 * @Description: 活跃存留 详细付费数Excel
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月28日 下午1:45:50
 */
public class ActiveAndPersistencePayExcel {
    /**
     * 行号
     */
    private int index;
    private String date;
    private String zoneId;
    private String packageId;

    /**
     * 新注册付费用户
     */
    private Integer newUserPayer;
    /**
     * 老用户新增付费
     */
    private Integer oldUserPayer;
    /**
     * 持续付费人数
     */
    private Integer continuePayer;
    /**
     * 新注册付费率
     */
    private String newUserPayerRate;

    /**
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index 要设置的 index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date 要设置的 date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return zoneId
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * @param zoneId 要设置的 zoneId
     */
    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * @return packageId
     */
    public String getPackageId() {
        return packageId;
    }

    /**
     * @param packageId 要设置的 packageId
     */
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    /**
     * @return newUserPayer
     */
    public Integer getNewUserPayer() {
        return newUserPayer;
    }

    /**
     * @param newUserPayer 要设置的 newUserPayer
     */
    public void setNewUserPayer(Integer newUserPayer) {
        this.newUserPayer = newUserPayer;
    }

    /**
     * @return oldUserPayer
     */
    public Integer getOldUserPayer() {
        return oldUserPayer;
    }

    /**
     * @param oldUserPayer 要设置的 oldUserPayer
     */
    public void setOldUserPayer(Integer oldUserPayer) {
        this.oldUserPayer = oldUserPayer;
    }

    /**
     * @return continuePayer
     */
    public Integer getContinuePayer() {
        return continuePayer;
    }

    /**
     * @param continuePayer 要设置的 continuePayer
     */
    public void setContinuePayer(Integer continuePayer) {
        this.continuePayer = continuePayer;
    }

    /**
     * @return newUserPayerRate
     */
    public String getNewUserPayerRate() {
        return newUserPayerRate;
    }

    /**
     * @param newUserPayerRate 要设置的 newUserPayerRate
     */
    public void setNewUserPayerRate(String newUserPayerRate) {
        this.newUserPayerRate = newUserPayerRate;
    }

}
