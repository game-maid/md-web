/**
 * @Title: RandomStoryType.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月16日  张福涛
 */

package com.talentwalker.game.md.core.util;

/**
 * @ClassName: RandomStoryType
 * @Description: 好感度随机剧情的类型
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月16日 下午2:42:57
 */

public enum RandomStoryType {
    /**
     * 副本战斗胜利
     */
    PVE_WIN("trigger_PVEwin"),
    /**
     * 副本战斗失败
     */
    PVE_LOSE("trigger_PVElose"),
    /**
     * 擂台战斗胜利
     */
    PVP_WIN("trigger_PVPwin"),
    /**
     * 擂台战斗失败
     */
    PVP_LOSE("trigger_PVPlose"),
    /**
     * 消耗金币
     */
    USED_GOLD("trigger_UsedGold"),
    /**
     * 消耗钻石
     */
    USED_DIAMOND("trigger_UsedDiamond"),
    /**
     * 充值
     */
    TOPUP_DIAMOND("trigger_TopupDiamond"),
    /**
     * 完成（主线、支线）任务
     */
    MISSION_COMPLETE("trigger_MissionComplete");

    private String type;

    private RandomStoryType(String type) {
        this.type = type;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type 要设置的 type
     */
    public void setType(String type) {
        this.type = type;
    }

}
