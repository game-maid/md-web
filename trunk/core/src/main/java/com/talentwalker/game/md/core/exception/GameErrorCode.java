/**
 * @Title: GameErrorCode.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月24日  占志灵
 */

package com.talentwalker.game.md.core.exception;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: GameErrorCode
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月24日 下午2:09:52
 */

public class GameErrorCode extends ErrorCode {
    static {
        try {
            Properties pro = PropertiesLoaderUtils
                    .loadProperties(new EncodedResource(new ClassPathResource("errorMessage.properties"), "utf-8"));
            GameExceptionUtils.setMessage(pro);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***************************公共错误码************************************/

    /**
    * 游戏区服不存在
    */
    public static final Integer PORTAL_ERROR_10001 = 10001;
    /**
     * 游戏包不存在
     */
    public static final Integer PORTAL_ERROR_10002 = 10002;
    /**
     * 游戏区服维护中
     */
    public static final Integer PORTAL_ERROR_10003 = 10003;
    /**
     * 消息头中没有sessionId
     */
    public static final Integer PORTAL_ERROR_10004 = 10004;
    /**
     * 未登录门户
     */
    public static final Integer PORTAL_ERROR_10005 = 10005;

    /***************************游戏逻辑错误码************************************/
    /**
     * 未注册角色
     */
    public static final Integer GAME_ERROR_21001 = 21001;
    /**
     * 已经注册过角色
     */
    public static final Integer GAME_ERROR_21002 = 21002;
    /**
     * 道具数量不能小于0
     */
    public static final Integer GAME_ERROR_21003 = 21003;
    /**
     * 道具添加、消耗失败，未知的对象
     */
    public static final Integer GAME_ERROR_21004 = 21004;
    /**
     * 道具ID不存在
     */
    public static final Integer GAME_ERROR_21005 = 21005;
    /**
     * 道具不足
     */
    public static final Integer GAME_ERROR_21006 = 21006;
    /**
     * 背包已满
     */
    public static final Integer GAME_ERROR_21007 = 21007;
    /**
     * 名称已被注册
     */
    public static final Integer GAME_ERROR_21008 = 21008;
    /**
     * vip等级不足
     */
    public static final Integer GAME_ERROR_21009 = 21009;
    /**
     * 已达到购买上限
     */
    public static final Integer GAME_ERROR_21010 = 21010;
    /**
     * 头像icon不存在
     */
    public static final Integer GAME_ERROR_21011 = 21011;
    /**
     * 头像未解锁
     */
    public static final Integer GAME_ERROR_21012 = 21012;
    /**
     * 没有找到配置
     */
    public static final Integer GAME_ERROR_21013 = 21013;
    /**
     * 不能开启的道具
     */
    public static final Integer GAME_ERROR_21014 = 21014;
    /**
     * 含有非法字符
     */
    public static final Integer GAME_ERROR_21015 = 21015;
    /**
     * 含有敏感字符
     */
    public static final Integer GAME_ERROR_21016 = 21016;
    /**
     * 名字长度超长
     */
    public static final Integer GAME_ERROR_21017 = 21017;
    /**
     * 签名领取时间内
     */
    public static final Integer GAME_ERROR_21018 = 21018;
    /**
     * 钻石不足
     */
    public static final Integer GAME_ERROR_21019 = 21019;
    /**
     * 金币不足
     */
    public static final Integer GAME_ERROR_21020 = 21020;
    /**
     * 突破币不足
     */
    public static final Integer GAME_ERROR_21021 = 21021;
    /**
     * 体力不足
     */
    public static final Integer GAME_ERROR_21022 = 21022;
    /**
     * 精力不足
     */
    public static final Integer GAME_ERROR_21023 = 21023;
    /**
     * 魂魄不足
     */
    public static final Integer GAME_ERROR_21024 = 21024;
    /**
     * 擂台币不足
     */
    public static final Integer GAME_ERROR_21025 = 21025;
    /**
     * 联盟资金不足
     */
    public static final Integer GAME_ERROR_21026 = 21026;
    /**
     * 魂币不足
     */
    public static final Integer GAME_ERROR_21027 = 21027;
    /**
     * 装备不可卖出
     */
    public static final Integer GAME_ERROR_21028 = 21028;
    /*********************** 装备 ************************/
    /**
     * 无此装备
     */
    public static final Integer GAME_ERROR_22001 = 22001;
    /**
     * 强化等级已达上限
     */
    public static final Integer GAME_ERROR_22002 = 22002;
    /**
     * 消耗道具不是精炼道具
     */
    public static final Integer GAME_ERROR_22003 = 22003;
    /**
     * 装备已经达到精炼最高等级
     */
    public static final Integer GAME_ERROR_22004 = 22004;
    /**
     * 装备不允许精炼
     */
    public static final Integer GAME_ERROR_22005 = 22005;
    /*********************** 阵容 ************************/
    /**
     * 无此武者
     */
    public static final Integer GAME_ERROR_23001 = 23001;
    /**
     * 该武者已上阵
     */
    public static final Integer GAME_ERROR_23002 = 23002;
    /**
     * 上阵队伍中应至少安排一名武者
     */
    public static final Integer GAME_ERROR_23003 = 23003;
    /**
     * 当前位置无武将存在
     */
    public static final Integer GAME_ERROR_23004 = 23004;
    /**
     * 当前没有可记忆的阵容
     */
    public static final Integer GAME_ERROR_23005 = 23005;
    /**
     * 提取的阵容不存在
     */
    public static final Integer GAME_ERROR_23006 = 23006;
    /**
     * 无此技能
     */
    public static final Integer GAME_ERROR_23007 = 23007;
    /**
     * 武将已经使用该技能
     */
    public static final Integer GAME_ERROR_23008 = 23008;
    /**
     * 技能达到上限
     */
    public static final Integer GAME_ERROR_23009 = 23009;
    /**
     * 无此装备
     */
    public static final Integer GAME_ERROR_23010 = 23010;
    /**
     * 此装备不能添加到该位置
     */
    public static final Integer GAME_ERROR_23011 = 23011;
    /**
     * 记忆栏未开启（玩家等级未达到）
     */
    public static final Integer GAME_ERROR_23012 = 23012;
    /**
     * 当前阵容不能购买
     */
    public static final Integer GAME_ERROR_23013 = 23013;
    /**
     * 未达到解锁等级
     */
    public static final Integer GAME_ERROR_23014 = 23014;
    /**
     * 记忆栏未解锁（不能记忆阵容）
     */
    public static final Integer GAME_ERROR_23015 = 23015;
    /**
     * 超出技能数量上线
     */
    public static final Integer GAME_ERROR_23016 = 23016;
    /**
     * 技能未解锁（技能突破等级未达到）
     */
    public static final Integer GAME_ERROR_23017 = 23017;
    /**
     * 技能未解锁
     */
    public static final Integer GAME_ERROR_23018 = 23018;
    /**
     * 该技能为限制技能，上阵技能数量超限
     */
    public static final Integer GAME_ERROR_23019 = 23019;
    /*********************** 武将 ************************/
    /**
     * 武将已经满级
     */
    public static final Integer GAME_ERROR_24001 = 24001;
    /**
     * 道具无法提升经验
     */
    public static final Integer GAME_ERROR_24002 = 24002;
    /**
     * 武将已满级
     */
    public static final Integer GAME_ERROR_24003 = 24003;
    /**
     * 武将不可突破
     */
    public static final Integer GAME_ERROR_24004 = 24004;
    /**
     * 觉醒武将不可突破
     */
    public static final Integer GAME_ERROR_24005 = 24005;
    /**
     * 武将突破等级已满级
     */
    public static final Integer GAME_ERROR_24006 = 24006;
    /**
     * 武将等级不足，无法突破
     */
    public static final Integer GAME_ERROR_24007 = 24007;
    /**
     * 突破材料不足
     */
    public static final Integer GAME_ERROR_24008 = 24008;
    /**
     * 突破材料不是同名武将
     */
    public static final Integer GAME_ERROR_24009 = 24009;
    /**
     * 武将不可觉醒
     */
    public static final Integer GAME_ERROR_24010 = 24010;
    /**
     * 武将无法觉醒，等级不足
     */
    public static final Integer GAME_ERROR_24011 = 24011;
    /**
     * 武将无法觉醒，消耗同名卡牌数量错误
     */
    public static final Integer GAME_ERROR_24012 = 24012;
    /**
     * 觉醒材料不是同名武将
     */
    public static final Integer GAME_ERROR_24013 = 24013;
    /**
     * 觉醒材料不是相同稀有度的武将
     */
    public static final Integer GAME_ERROR_24014 = 24014;
    /**
     * 觉醒材料突破等级不足
     */
    public static final Integer GAME_ERROR_24015 = 24015;
    /**
     * 道具不能使用
     */
    public static final Integer GAME_ERROR_24016 = 24016;
    /**
     * 月卡不存在
     */
    public static final Integer GAME_ERROR_24017 = 24017;
    /**
     * 月卡已领取
     */
    public static final Integer GAME_ERROR_24018 = 24018;
    /**
     * 月卡已领取
     */
    public static final Integer GAME_ERROR_24019 = 24019;
    /**
    * 武将好感度已经满
    */
    public static final Integer GAME_ERROR_24020 = 24020;
    /*********************** 技能************************/
    /**
     * 技能等级达到满级，不能升级
     */
    public static final Integer GAME_ERROR_25000 = 25000;
    /*********************** 擂台************************/
    /**
     * 购买次数已用完
     */
    public static final Integer GAME_ERROR_26000 = 26000;
    /**
     * 擂台商店刷新次数达到上限
     */
    public static final Integer GAME_ERROR_26001 = 26001;
    /**
     * 商品已经购买，不能重复购买
     */
    public static final Integer GAME_ERROR_26002 = 26002;
    /**
     * pvpGold擂台币不足
     */
    public static final Integer GAME_ERROR_26003 = 26003;
    /**
     * 挑战次数已用完
     */
    public static final Integer GAME_ERROR_26004 = 26004;
    /**
     * 主公精力不足
     */
    public static final Integer GAME_ERROR_26005 = 26005;
    /**
     * 冷却时间内不能继续挑战（挑战失败冷却5分钟）
     */
    public static final Integer GAME_ERROR_26006 = 26006;
    /**
     * 未找到挑战对手
     */
    public static final Integer GAME_ERROR_26007 = 26007;
    /*********************** 关卡 **********************/
    /**
     * 关卡ID不存在
     */
    public static final Integer GAME_ERROR_27000 = 27000;
    /**
     * 前一关卡未通过
     */
    public static final Integer GAME_ERROR_27001 = 27001;
    /**
     * 关卡ID格式错误
     */
    public static final Integer GAME_ERROR_27002 = 27002;
    /**
     * 等级不足，无法挑战
     */
    public static final Integer GAME_ERROR_27003 = 27003;
    /**
     * 无法多次挑战一次性关卡
     */
    public static final Integer GAME_ERROR_27004 = 27004;
    /**
     * 无挑战次数
     */
    public static final Integer GAME_ERROR_27005 = 27005;
    /**
     * 一次性关卡不能重置次数
     */
    public static final Integer GAME_ERROR_27006 = 27006;
    /**
     * 还有次数，不用重置
     */
    public static final Integer GAME_ERROR_27007 = 27007;
    /**
     * 不能重置，已达重置上限
     */
    public static final Integer GAME_ERROR_27008 = 27008;
    /**
     * 一次性关卡无法扫荡
     */
    public static final Integer GAME_ERROR_27009 = 27009;
    /**
     * 三星才能扫荡
     */
    public static final Integer GAME_ERROR_27010 = 27010;
    /**
     * 未开启连续扫荡
     */
    public static final Integer GAME_ERROR_27011 = 27011;
    /**
     * 未找到奖励配置
     */
    public static final Integer GAME_ERROR_27012 = 27012;
    /**
     * 星数不够，无法领取
     */
    public static final Integer GAME_ERROR_27013 = 27013;
    /**
     * 已经领取奖励
     */
    public static final Integer GAME_ERROR_27014 = 27014;
    /*********************** 商店 **********************/
    /**
     * 数量不能小于0
     */
    public static final Integer GAME_ERROR_28000 = 28000;
    /**
     * 未找到道具
     */
    public static final Integer GAME_ERROR_28001 = 28001;
    /**
     * 触发招募不存在
     */
    public static final Integer GAME_ERROR_28002 = 28002;
    /**
     * 未找到活动招募配置
     */
    public static final Integer GAME_ERROR_28003 = 28003;
    /**
     * 购买数量达到上限
     */
    public static final Integer GAME_ERROR_28004 = 28004;
    /**
     * 不存在的福袋
     */
    public static final Integer GAME_ERROR_28005 = 28005;
    /**
     * 福袋已经过期
     */
    public static final Integer GAME_ERROR_28006 = 28006;
    /**
     * 活动已结束
     */
    public static final Integer GAME_ERROR_28007 = 28007;
    /*********************** 任务 **********************/
    /**
     * 找不到任务
     */
    public static final Integer GAME_ERROR_29000 = 29000;
    /**
     * 已经领取奖励
     */
    public static final Integer GAME_ERROR_29001 = 29001;
    /**
     * 任务未完成
     */
    public static final Integer GAME_ERROR_29002 = 29002;
    /**
     * 当前宝箱已经领取
     */
    public static final Integer GAME_ERROR_29003 = 29003;
    /**
     * 未知的活跃宝箱
     */
    public static final Integer GAME_ERROR_29004 = 29004;
    /**
     * 活跃度不足
     */
    public static final Integer GAME_ERROR_29005 = 29005;
    /**
     * 请购买上一次商品，才能购买此次商品
     */
    public static final Integer GAME_ERROR_29006 = 29006;
    /*********************** 联盟 **********************/
    /**
     * 已加入联盟
     */
    public static final Integer GAME_ERROR_31000 = 31000;
    /**
     * 名称已经被使用
     */
    public static final Integer GAME_ERROR_31001 = 31001;
    /**
     * 无权限
     */
    public static final Integer GAME_ERROR_31002 = 31002;
    /**
     * 申请数量达到上限
     */
    public static final Integer GAME_ERROR_31003 = 31003;
    /**
     * 主公等级未达到申请要求
     */
    public static final Integer GAME_ERROR_31004 = 31004;
    /**
     * VIP等级未达到申请要求
     */
    public static final Integer GAME_ERROR_31005 = 31005;
    /**
     * 贡献次数用完
     */
    public static final Integer GAME_ERROR_31006 = 31006;
    /**
     * 联盟人数超过上限
     */
    public static final Integer GAME_ERROR_31007 = 31007;
    /**
     * 退出联盟冷却时间（6小时）内，不能加入其它联盟
     */
    public static final Integer GAME_ERROR_31008 = 31008;
    /**
     * 只能对低于自己职务两级的玩家操作
     */
    public static final Integer GAME_ERROR_31009 = 31009;
    /**
     * 该玩家职位已经最低
     */
    public static final Integer GAME_ERROR_31010 = 31010;
    /**
     * 该职务人数达到上限
     */
    public static final Integer GAME_ERROR_31011 = 31011;
    /**
     * 物品不能重复购买
     */
    public static final Integer GAME_ERROR_31012 = 31012;
    /**
     * 购买的物品不存在
     */
    public static final Integer GAME_ERROR_31013 = 31013;
    /**
     * 联盟等级不够，当前物品未解锁
     */
    public static final Integer GAME_ERROR_31014 = 31014;
    /**
     * 玩家已经取消申请
     */
    public static final Integer GAME_ERROR_31015 = 31015;
    /**
     * 禁止玩家对自己操作（踢出联盟，升职，降职）
     */
    public static final Integer GAME_ERROR_31016 = 31016;
    /*********************** 邮件 **********************/
    /**
     * 邮件不存在
     */
    public static final Integer GAME_ERROR_32000 = 32000;
    /**
     * 已经被领取
     */
    public static final Integer GAME_ERROR_32001 = 32001;
    /**
     * 该邮件不是奖励邮件
     */
    public static final Integer GAME_ERROR_32002 = 32002;
    /**
     * 邮件未读取，不能删除
     */
    public static final Integer GAME_ERROR_32003 = 32003;
    /**
     * 奖励邮件未领取，不能删除
     */
    public static final Integer GAME_ERROR_32004 = 32004;
    /**
     * 道具已满，不能领取
     */
    public static final Integer GAME_ERROR_32005 = 32005;
    /**
     * 装备已满，不能领取
     */
    public static final Integer GAME_ERROR_32006 = 32006;
    /**
     * 技能已满，不能领取
     */
    public static final Integer GAME_ERROR_32007 = 32007;
    /**
     * 武将已满，不能领取
     */
    public static final Integer GAME_ERROR_32008 = 32008;
    /**
     * 不是本人邮件，不能领取
     */
    public static final Integer GAME_ERROR_32009 = 32009;
    /**********************客服反馈********************/
    /**
     * 提问频繁（一小时内最多反馈5条信息）
     */
    public static final Integer GAME_ERROR_33001 = 33001;
    /**********************CDK兑换码********************/
    /**
     * 渠道（包id）不符合
     */
    public static final Integer GAME_ERROR_34001 = 34001;
    /**
     * 区服不符合
     */
    public static final Integer GAME_ERROR_34002 = 34002;
    /**
     * 该兑换码已使用过
     */
    public static final Integer GAME_ERROR_34003 = 34003;
    /**
     * 时间不符合，未在使用时间内（未到或已超出时间） 
     */
    public static final Integer GAME_ERROR_34004 = 34004;
    /**
     * 已使用过该批次兑换码
     */
    public static final Integer GAME_ERROR_34005 = 34005;
    /**
     * 该兑换码已作废
     */
    public static final Integer GAME_ERROR_34006 = 34006;
    /**
     * 等级未达到
     */
    public static final Integer GAME_ERROR_34007 = 34007;
    /**
     * 兑换码不存在
     */
    public static final Integer GAME_ERROR_34008 = 34008;
    /***********************VIP*************************/
    /**
     * 奖励已领取
     */
    public static final Integer GAME_ERROR_35001 = 35001;
    /**
     * 该vip等级不存在，不可领取该等级奖励
     */
    public static final Integer GAME_ERROR_35002 = 35002;
    /***********************分解*************************/
    /**
     * 没有可分解的魂
     */
    public static final Integer GAME_ERROR_36000 = 36000;
    /**
     * 没有可分解的英雄
     */
    public static final Integer GAME_ERROR_36001 = 36001;
    /***********************好友*************************/
    /**
     * 好友达到上限
     */
    public static final Integer GAME_ERROR_37000 = 37000;
    /**
     * 对方好友达到上限
     */
    public static final Integer GAME_ERROR_37001 = 37001;
    /**
     * 当天已经送过体力给该好友
     */
    public static final Integer GAME_ERROR_37002 = 37002;
    /*******************签到*************************/
    /**
     * 已经签到
     */
    public static final Integer GAME_ERROR_38001 = 38001;
    /**
     * 不能领取该天的奖励
     */
    public static final Integer GAME_ERROR_38002 = 38002;

}
