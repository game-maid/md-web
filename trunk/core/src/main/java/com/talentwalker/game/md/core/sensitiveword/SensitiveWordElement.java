/**
 * @Title: SensitiveWordElement.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  占志灵
 */

package com.talentwalker.game.md.core.sensitiveword;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: SensitiveWordElement
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月5日 下午3:27:22
 */

public class SensitiveWordElement {
    // private Character character;
    private boolean isEnd = false;
    private Map<Character, SensitiveWordElement> nextElements = new HashMap<>();

    // public Character getCharacter() {
    // return character;
    // }
    //
    // public void setCharacter(Character character) {
    // this.character = character;
    // }

    public SensitiveWordElement get(Character c) {
        return nextElements.get(c);
    }

    public void add(Character c, SensitiveWordElement element) {
        nextElements.put(c, element);
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Map<Character, SensitiveWordElement> getNextElements() {
        return nextElements;
    }

    public void setNextElements(Map<Character, SensitiveWordElement> nextElements) {
        this.nextElements = nextElements;
    }

}
