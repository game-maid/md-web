
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
    /**
     * rank 道具品质
     */
    public static final String ITEM_RANK = "rank";
    /**
     * 道具名称
     */
    public static final String ITEM_NAME = "name";

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
     * 是否折扣
     */
    public static final String SHOP_HERO_RECRUIT_SINGLE_FREE = "singleFree";
    /**
     * 新手招募招募类型
     */
    public static final String SHOP_HERO_RECRUIT_TYPE = "type";
    /**
     * 剧情招募
     */
    public static final String SHOP_HERO_RECRUIT_FIRST = "recruit_0001";
    /**
     * 保底次数
     */
    public static final String SHOP_HERO_RECRUIT_SAFETY_TIMES = "safetyTimes";
    /**
     * 消耗类型
     */
    public static final String SHOP_HERO_RECRUIT_COST = "cost";
    /**
     * A池内容
     */
    public static final String SHOP_HERO_RECRUIT_POLL_A = "poll_A";
    /**
     * B池内容
     */
    public static final String SHOP_HERO_RECRUIT_POLL_B = "poll_B";
    /**
     * 起始投入C数量
     */
    public static final String SHOP_HERO_RECRUIT_START_C = "start_C";
    /**
     * 起始投入D数量
     */
    public static final String SHOP_HERO_RECRUIT_START_D = "start_D";
    /**
     * 投放C间隔及数量
     */
    public static final String SHOP_HERO_RECRUIT_GOING_C = "going_C";
    /**
     * 投放D间隔及数量
     */
    public static final String SHOP_HERO_RECRUIT_GOING_D = "going_D";
    /**
     * 抽中稀有的基礎概率
     */
    public static final String SHOP_HERO_RECRUIT_PROBABILITY = "probability";
    /**
     * 概率成長
     */
    public static final String SHOP_HERO_RECRUIT_PROBABILITYUP = "probabilityUp";
    /**
     * 單抽第一次
     */
    public static final String SHOP_HERO_RECRUIT_SINGLE_1 = "single_1";
    /**
     * 單抽第二次
     */
    public static final String SHOP_HERO_RECRUIT_SINGLE_2 = "single_2";
    /**
     * 單抽第三次
     */
    public static final String SHOP_HERO_RECRUIT_SINGLE_3 = "single_3";
    /**
     * 十连抽第一次
     */
    public static final String SHOP_HERO_RECRUIT_TENTIMES_1 = "tentimes_1";
    /**
     * 十连抽第二次
     */
    public static final String SHOP_HERO_RECRUIT_TENTIMES_2 = "tentimes_2";
    /**
     * 十连抽第三次
     */
    public static final String SHOP_HERO_RECRUIT_TENTIMES_3 = "tentimes_3";
    /**
     * 武将id
     */
    public static final String SHOP_HERO_RECRUIT_HEROID = "ID";
    /**
     * 武将添加到卡池的权重
     */
    public static final String SHOP_HERO_RECRUIT_GOTOWEIGHT = "gotoWeight";
    /**
     * 武将被抽取到的权重
     */
    public static final String SHOP_HERO_RECRUIT_GETWEIGHT = "getWeight";
    /**
     * 最大抽取次数
     */
    public static final String SHOP_HERO_RECRUIT_GETTIMES = "gettimes";
    /**********************突破计算*****************************/
    public static final String RANK_UP = "rankUp";

    /**********************擂台*****************************/
    /**
     * 结算奖励
     */
    public static final String DUEL_DRAW = "duel_draw";

    /**********************其他*****************************/
    /**
     * 权重
     */
    public static final String WEIGHT = "weight";

    /*******************功能名称表（uri 翻译表）***************************/
    public static final String FUNCTION_NAME = "functionName";

    /*******************功能开启等级限制***************************/
    /**
     * 功能开启等级限制
     */
    public static final String FUNCTION_LEVEL_OPEN = "function_levelopen";
    /**
     * 擂台
     */
    public static final String DUEL = "duel";
    /**
     * 关卡
     */
    public static final String STAGE = "stage";
    /**
     * 装备-强化
     */
    public static final String EQUIP_UPDATE = "equip_update";
    /**
     * 英雄-招募
     */
    public static final String HERO_RECRUITSOUL = "hero_recruitSoul";
    /**
     * 英雄-突破
     */
    public static final String HERO_BREAK = "hero_break";
    /**
     * 联盟
     */
    public static final String LEAGUE = "league";
    /**
     * 啦啦队
     */
    public static final String FORMSEVEN = "formSeven";
    /**
     * 装备-精炼
     */
    public static final String EQUIP_REFINE = "equip_refine";
    /**
     * 英雄觉醒
     */
    public static final String AWAVE = "awave";
    /**
     * 一键分解
     */
    public static final String RESOLVEALL = "resolveAll";

    /*****************新好感度配置（第二版）*********************/
    public static final String ROMANCE_BASE = "romance_base";
    /**
     * 字段romance
     */
    public static final String ROMANCE_BASE_ROMANCE = "romance";
    /**
     * 剧情奖励
     */
    public static final String ROMANCE_BASE_AWARD = "award";
    /**
     * 好感度等级上限
     */
    public static final String ROMANCE_BASE_MAXLV = "romanceMaxLv";
    /**
     * 可使用的好感度经验道具
     */
    public static final String ROMANCE_BASE_ITEM = "item";
    /**
     * 升级需要的好感度经验值
     */
    public static final String ROMANCE_BASE_COST = "cost";
    /*********************随机剧情触发概率参数配置***************************/
    public static final String ROMANCE_THEATER_PROBABILITY = "romance_theaterProbability";
    /**
     * 触发概率
     */
    public static final String ROMANCE_THEATER_PROBABILITY_TRIGGERPRO = "triggerPro";
    /**
     * 触发间隔
     */
    public static final String ROMANCE_THEATER_PROBABILITY_TRIGGERTIMING = "triggerTiming";
    /*******************好感度剧情表*********************************/
    public static final String ROMANCE_THEATERID = "romance_theaterID";
    /**
     * 剧情
     */
    public static final String ROMANCE_THEATERID_THEATER = "theater";
    /**
     *权重
     */
    public static final String ROMANCE_THEATERID_WEIGHT = "weight";
    /**
     *剧情id
     */
    public static final String ROMANCE_THEATERID_WEIGHT_ID = "ID";
    /*************************好感度随机剧情奖励***********************/
    public static final String ROMANCE_THRETER_AWARD = "romance_theaterTriggerAward";
    /**
     *奖励及权重
     */
    public static final String ROMANCE_THRETER_AWARD_AND_WEIGHT = "awardAndWeight";
    /**
     * 道具id
     */
    public static final String ROMANCE_THRETER_AWARD_ITEMID = "itemID";
    /**
     * 权重
     */
    public static final String ROMANCE_THRETER_AWARD_WEIGHT = "weight";
    /**
     * 数量
     */
    public static final String ROMANCE_THRETER_AWARD_NUM = "num";
    /*************************好感度随机剧情奖励***********************/
    /**
     * 好友赠送体力表名
     */
    public static final String FRIEND_ENERGY = "friend_energy";
    /**
     * 赠送体力值
     */
    public static final String SEND_ENERGY = "sendEnergy";
    /**
     * 收取体力数量
     */
    public static final String RECEIVE_ENERGY = "receiveEnergy";
    /*******************后台广告、公告跳转路径配置********************/
    public static final String ADVERTISEMENT_JUMPOUT = "advertisementJumpout";
    /**
     * name（名称）
     */
    public static final String ADVERTISEMENT_JUMPOUT_NAME = "name";
    /**
     * vlue(跳转路径)
     */
    public static final String ADVERTISEMENT_JUMPOUT_TYPE = "type";

}
