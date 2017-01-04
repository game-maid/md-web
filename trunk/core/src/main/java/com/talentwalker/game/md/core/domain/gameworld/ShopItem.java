/**
 * @Title: ShopItem.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.talentwalker.game.md.core.domain.config.ShopConfig;

/**
 * @ClassName: ShopItem
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月29日 下午1:34:27
 */

public class ShopItem {

    private String itemKey;

    private int times;

    @JsonIgnore
    private long buyTime;
    @Transient
    private ShopConfig config;

    public ShopItem(String itemKey) {
        this.itemKey = itemKey;
        this.buyTime = System.currentTimeMillis();
    }

    /**
     * @return itemKey
     */
    public String getItemKey() {
        return itemKey;
    }

    /**
     * @param itemKey 要设置的 itemKey
     */
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    /**
     * @return times
     */
    public int getTimes() {
        return times;
    }

    /**
     * @param times 要设置的 times
     */
    public void setTimes(int times) {
        this.times = times;
    }

    /**
     * @return buyTime
     */
    public long getBuyTime() {
        return buyTime;
    }

    /**
     * @param buyTime 要设置的 buyTime
     */
    public void setBuyTime(long buyTime) {
        this.buyTime = buyTime;
    }

    /**
     * @return config
     */
    public ShopConfig getConfig() {
        return config;
    }

    /**
     * @param config 要设置的 config
     */
    public void setConfig(ShopConfig config) {
        this.config = config;
    }

}
