/**
 * @Title: LordInfo.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年12月7日  赵丽宝
 */

package com.talentwalker.game.md.core.response;

import com.talentwalker.game.md.core.domain.gameworld.Lord;

/**
 * @ClassName: LordInfo
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年12月7日 下午6:05:16
 */

public class LordInfo {
    /**
     * id
     */
    protected String id;
    /**
     * 名字
     */
    protected String name;
    /**
     * 头像
     */
    protected String headIcon;
    /**
     * 级别
     */
    protected int level;
    /**
     * vip等级
     */
    protected int vipLevel;
    /**
     * 上次访问时间
     */
    protected long lastTime;
    /**
     * 联盟名称
     */
    protected String leagueName;

    public void lordInfo(Lord lord) {
        this.id = lord.getId();
        this.level = lord.getLevel();
        this.vipLevel = lord.getVipLevel();
        this.name = lord.getName();
        this.headIcon = lord.getHeadIcon();
        this.lastTime = lord.getLastTime();
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id 要设置的 id
     */
    public void setId(String id) {
        this.id = id;
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
     * @return headIcon
     */
    public String getHeadIcon() {
        return headIcon;
    }

    /**
     * @param headIcon 要设置的 headIcon
     */
    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    /**
     * @return vipLevel
     */
    public int getVipLevel() {
        return vipLevel;
    }

    /**
     * @param vipLevel 要设置的 vipLevel
     */
    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    /**
     * @return leagueName
     */
    public String getLeagueName() {
        return leagueName;
    }

    /**
     * @param leagueName 要设置的 leagueName
     */
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level 要设置的 level
     */
    public void setLevel(int level) {
        this.level = level;
    }

}
