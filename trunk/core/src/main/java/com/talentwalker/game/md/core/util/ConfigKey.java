
package com.talentwalker.game.md.core.util;

/**
 * @ClassName: ConfigKey
 * @Description: 配置文件名称、字段名
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月14日 上午11:23:46
 */
public class ConfigKey {
    /******************heroConfig 英雄配置*****************/
    /**
     * heroConfig 英雄配置
     */
    public static final String HERO_CONFIG = "heroConfig";
    /**
     * 星级
     */
    public static final String HERO_CONFIG_STAR = "star";

    /******************loveup 好感度配置*****************/
    /**
     * loveup 好感度配置
     */
    public static final String LOVEUP = "loveup";
    /**
     * love 
     */
    public static final String LOVEUP_LOVE = "love";
    /**
     * item
     */
    public static final String LOVEUP_ITEM = "item";
    /**
     * cost 经验消耗
     */
    public static final String LOVEUP_COST = "cost";

    /*******************loveopen 好感度限制配置**************/
    /**
     * loveopen 好感度限制配置
     */
    public static final String LOVEOPEN = "loveopen";
    /**
     * 好感度等级限制
     */
    public static final String LOVEOPEN_LEVELMAX = "levelmax";
    /**********************item 道具配置*********************/
    /**
     * item 配置
     */
    public static final String ITEM = "item";
    /**
     * params 参数
     */
    public static final String ITEM_PARAMS = "params";
    /**
     * type 道具类型
     */
    public static final String ITEM_TYPE = "type";
    /********************guide_other*****************************/
    /**
     * guide_other 新手引导 战斗奖励
     */
    public static final String GUIDE_OTHER = "guide_other";
    /**
     * equipId 奖励id
     */
    public static final String GUIDE_OTHER_EQUIP_ID = "equipId";
    /**
     * stageId 关卡id
     */
    public static final String GUIDE_OTHER_STAGE_ID = "stageId";
    /**********************充值配置*************************/
    public static final String CASH_SHOP_CONFIG = "cashShop_config";
    /**
     * 充值金额
     */
    public static final String CASH_SHOP_CONFIG_PRICE = "price";
    /**
     * 类型 1：钻石充值  2和3 代表月卡
     */
    public static final String CASH_SHOP_CONFIG_TYPE = "type";
    /**
     * 描述
     */
    public static final String CASH_SHOP_CONFIG_DESP = "desp";

    /**********************英雄招募*****************************/
    public static final String SHOP_HERO_RECRUIT = "shop_heroRecruit";
    /**
     * 新手招募第一次key
     */
    public static final String SHOP_HERO_RECRUIT_FIRST = "recruit_0001";
    /**
     * 新手招募第二次key
     */
    public static final String SHOP_HERO_RECRUIT_SECOND = "recruit_0002";
    /**
     * 新手招募招募类型
     */
    public static final String SHOP_HERO_RECRUIT_TYPE = "type";
    /**********************突破计算*****************************/
    public static final String RANK_UP = "rankUp";

    /**********************擂台*****************************/
    /**
     * 结算奖励
     */
    public static final String DUEL_DRAW = "duel_draw";

    /**********************其他*****************************/

    public static final String WEIGHT = "weight";
    /*******************功能名称表（uri 翻译表）***************************/
    public static final String FUNCTION_NAME = "functionName";

}
