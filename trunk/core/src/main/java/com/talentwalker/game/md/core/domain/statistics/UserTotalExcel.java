
package com.talentwalker.game.md.core.domain.statistics;

/**
 * @ClassName: ActiveAndPersistenceExcel
 * @Description: 后台 数据报表 总用户数Excel
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月28日 下午1:45:50
 */
public class UserTotalExcel {
    /**
     * 行号
     */
    private int index;
    private String zoneId;
    private String zoneName;
    /**
     * 区服总人数
     */
    private Integer zoneTotal;

    private String packageId;
    private String packageName;
    /**
     * 包人数
     */
    private Integer packageTotal;

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
     * @return zoneName
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * @param zoneName 要设置的 zoneName
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * @return packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName 要设置的 packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return zoneTotal
     */
    public Integer getZoneTotal() {
        return zoneTotal;
    }

    /**
     * @param zoneTotal 要设置的 zoneTotal
     */
    public void setZoneTotal(Integer zoneTotal) {
        this.zoneTotal = zoneTotal;
    }

    /**
     * @return packageTotal
     */
    public Integer getPackageTotal() {
        return packageTotal;
    }

    /**
     * @param packageTotal 要设置的 packageTotal
     */
    public void setPackageTotal(Integer packageTotal) {
        this.packageTotal = packageTotal;
    }

}
