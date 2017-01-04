/**
 * @Title: Shop.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年7月29日  闫昆
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: Shop
 * @Description: 商城对象
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年7月29日 下午1:33:02
 */

@Document(collection = "game_shop")
public class Shop extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */
    private static final long serialVersionUID = 1L;

    private Map<String, ShopItem> basicShop;

    private Map<String, ShopItem> vipShop;

    private Map<String, ShopItem> luckyBagShop;

    public Shop() {
        this.basicShop = new HashMap<String, ShopItem>();
        this.vipShop = new HashMap<String, ShopItem>();
        this.luckyBagShop = new HashMap<String, ShopItem>();
    }

    /**
     * @return basicShop
     */
    public Map<String, ShopItem> getBasicShop() {
        return basicShop;
    }

    /**
     * @param basicShop 要设置的 basicShop
     */
    public void setBasicShop(Map<String, ShopItem> basicShop) {
        this.basicShop = basicShop;
    }

    /**
     * @return vipShop
     */
    public Map<String, ShopItem> getVipShop() {
        return vipShop;
    }

    /**
     * @param vipShop 要设置的 vipShop
     */
    public void setVipShop(Map<String, ShopItem> vipShop) {
        this.vipShop = vipShop;
    }

    /**
     * @return luckyBagShop
     */
    public Map<String, ShopItem> getLuckyBagShop() {
        return luckyBagShop;
    }

    /**
     * @param luckyBagShop 要设置的 luckyBagShop
     */
    public void setLuckyBagShop(Map<String, ShopItem> luckyBagShop) {
        this.luckyBagShop = luckyBagShop;
    }

}
