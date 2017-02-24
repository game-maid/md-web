/**
 * @Title: Romance.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月15日  张福涛
 */

package com.talentwalker.game.md.core.domain.gameworld;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: Romance
 * @Description: 好感度
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月15日 上午11:36:37
 */

public class Romance implements Serializable {
    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 1L;
    /**
     * 剧情状态 0 锁定
     */
    public static final Integer STORY_STATE_LOCK = 0;
    /**
     * 剧情状态 1 进入剧情
     */
    public static final Integer STORY_STATE_START = 1;
    /**
     * 剧情状态 2 完成剧情
     */
    public static final Integer STORY_STATE_END = 2;

    /**
     * 剧情类型 0：等级剧情
     */
    public static final Integer STORY_TYPE_LEVEL = 0;
    /**
     * 剧情类型 1：随机剧情
     */
    public static final Integer STORY_TYPE_RANDOM = 1;

    /**
    * 好感度等级
    */
    private int level;
    /**
     * 升级后剩余经验
     */
    private int exp;
    /**
     * 剧情解锁
     * 0,1,2三种状态 0未解锁，1开始剧情后中途退出，2完成剧情（同时解锁该级立绘）
     */
    private Map<Integer, Integer> story;
    /**
     * 随机剧情记录
     */
    private Set<String> randomStoryRecord;
    /**
     * 当前使用(那个等级)的立绘
     */
    private int addpic;

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

    /**
     * @return exp
     */
    public int getExp() {
        return exp;
    }

    /**
     * @param exp 要设置的 exp
     */
    public void setExp(int exp) {
        this.exp = exp;
    }

    /**
     * @return story
     */
    public Map<Integer, Integer> getStory() {
        return story;
    }

    /**
     * @param story 要设置的 story
     */
    public void setStory(Map<Integer, Integer> story) {
        this.story = story;
    }

    /**
     * @return addpic
     */
    public int getAddpic() {
        return addpic;
    }

    /**
     * @param addpic 要设置的 addpic
     */
    public void setAddpic(int addpic) {
        this.addpic = addpic;
    }

    /**
     * @return randomStoryRecord
     */
    public Set<String> getRandomStoryRecord() {
        return randomStoryRecord;
    }

    /**
     * @param randomStoryRecord 要设置的 randomStoryRecord
     */
    public void setRandomStoryRecord(Set<String> randomStoryRecord) {
        this.randomStoryRecord = randomStoryRecord;
    }

}
