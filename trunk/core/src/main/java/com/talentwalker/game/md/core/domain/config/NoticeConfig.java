/**
 * @Title: NoticeConfig.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  张福涛
 */

package com.talentwalker.game.md.core.domain.config;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: NoticeConfig
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午4:32:33
 */
@Document(collection = "config_notice")
public class NoticeConfig extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 1L;
    /**
     * 是否适用于全服
     */
    protected boolean allZone;
    /**
     * 可用服务器列表
     */
    protected List<String> zoneList;
    /**
     * 状态
     */
    protected boolean state;
    /**
     * 名称
     */
    protected String name;
    /**
     * 生效时间
     */
    protected long startLong;
    /**
     * 失效时间
     */
    protected long endLong;
    /**
     * 商店序位
     */
    protected String putrush;
    /**
     * 文本内容
     */
    protected List<String> textList;
    /**
     * 颜色
     */
    protected List<String> rgbList;

    /**
     * 图片路径
     */
    protected String src;
    /**
     * 跳转地址
     */
    protected String stage;

    /**
     * @return allZone
     */
    public boolean getAllZone() {
        return allZone;
    }

    /**
     * @param allZone 要设置的 allZone
     */
    public void setAllZone(boolean allZone) {
        this.allZone = allZone;
    }

    /**
     * @return zoneList
     */
    public List<String> getZoneList() {
        return zoneList;
    }

    /**
     * @param zoneList 要设置的 zoneList
     */
    public void setZoneList(List<String> zoneList) {
        this.zoneList = zoneList;
    }

    /**
     * @return state
     */
    public boolean getState() {
        return state;
    }

    /**
     * @param state 要设置的 state
     */
    public void setState(boolean state) {
        this.state = state;
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
     * @return startLong
     */
    public long getStartLong() {
        return startLong;
    }

    /**
     * @param startLong 要设置的 startLong
     */
    public void setStartLong(long startLong) {
        this.startLong = startLong;
    }

    /**
     * @return endLong
     */
    public long getEndLong() {
        return endLong;
    }

    /**
     * @param endLong 要设置的 endLong
     */
    public void setEndLong(long endLong) {
        this.endLong = endLong;
    }

    /**
     * @return putrush
     */
    public String getPutrush() {
        return putrush;
    }

    /**
     * @param putrush 要设置的 putrush
     */
    public void setPutrush(String putrush) {
        this.putrush = putrush;
    }

    /**
     * @return src
     */
    public String getSrc() {
        return src;
    }

    /**
     * @param src 要设置的 src
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * @return textList
     */
    public List<String> getTextList() {
        return textList;
    }

    /**
     * @param textList 要设置的 textList
     */
    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    /**
     * @return rgbList
     */
    public List<String> getRgbList() {
        return rgbList;
    }

    /**
     * @param rgbList 要设置的 rgbList
     */
    public void setRgbList(List<String> rgbList) {
        this.rgbList = rgbList;
    }

    /**
     * @return stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * @param stage 要设置的 stage
     */
    public void setStage(String stage) {
        this.stage = stage;
    }

}
